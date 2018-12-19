package com.trackme.trackmeapplication.service.health;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Tasks;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.service.bluetooth.BluetoothServer;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.GeocoderNotAvailableException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;
import com.trackme.trackmeapplication.service.util.Constants;

import net.minidev.json.JSONArray;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HealthService extends Service {

    private static final int HEALTH_FOREGROUND_ID = 1338;

    private Date mBirthDate;
    private final IBinder mBinder = new LocalBinder();
    private Handler mHandler;

    public Date getUserBirthDate() {
        return mBirthDate;
    }

    public Handler getHealthDataHandler() {
        return mHandler;
    }

    public void setUserBirthDate(Date birthDate) {
        mBirthDate = birthDate;
    }


    /**
     * Class used for the binding.
     */
    public class LocalBinder extends Binder {
        public HealthService getService() {
            return HealthService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(new HealthDataCallback(new HealthServiceHelperImpl(this)));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra(getString(R.string.birth_year_key))) {
            return super.onStartCommand(intent, flags, startId);
        }

        String pattern = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
        try {
            mBirthDate = dateFormat.parse(intent.getStringExtra(getString(R.string.birth_year_key)));
        } catch (ParseException e) {
            Log.d(getString(R.string.debug_tag), getString(R.string.log_cannot_parse_date));
            return super.onStartCommand(intent, flags, startId);
        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            Log.d(getString(R.string.debug_tag), getString(R.string.log_debug_bluetooth_existing));
            if (bluetoothAdapter.isEnabled()) {
                Log.d(getString(R.string.debug_tag), getString(R.string.log_bluetooth_enabled));
                BluetoothServer bluetoothServer = new BluetoothServer(bluetoothAdapter, mHandler);
                bluetoothServer.start();
                startNotification(getString(R.string.notification_text));
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Return the emergency number room based on the country code
     *
     * @param countryCode the country code
     * @return the emergency number room based on the country code
     * @throws EmergencyNumberNotFoundException if there is no emergency room in the data of the app
     */
    public String getEmergencyRoomNumber(String countryCode) throws EmergencyNumberNotFoundException {
        if (Build.TYPE.equals("staging") && Build.TYPE.equals("release")) {
            // For the prototype
            return "+393384967148";
        } else {
            //For the real life
            ReadContext ctx = JsonPath.parse(getEmergencyRoomJson());
            String jsonPath = "$.data[?(@.Country.ISOCode == '" + countryCode + "')]..All";
            List<JSONArray> jsonArrays = ctx.read(jsonPath, List.class);
            for (JSONArray arr : jsonArrays) {
                if (arr.get(0) != null)
                    return (String) arr.get(0);
            }
            throw new EmergencyNumberNotFoundException();
        }
    }

    /**
     * @return the location of the user using GPS (need ACCESS_LOCATION permission)
     * @throws NoPermissionException if the permission is not granted
     * @throws InterruptedException  if location service got interrupted
     * @throws ExecutionException    if location service got execution problem
     * @throws TimeoutException      if location service elapsed too long
     */
    public Location getUserLocation() throws NoPermissionException, InterruptedException, ExecutionException, TimeoutException {
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(this.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionException();
        }
        Location location = Tasks.await(fusedLocationClient.getLastLocation(), 1000, TimeUnit.MILLISECONDS);
        if (location == null)
            throw new TimeoutException("No last location available");
        return location;
    }

    /**
     * Retrieves the country code from the geocoder (need ACCESS_LOCATION permission)
     *
     * @param location the location of the user
     * @return the country code where the user is
     */
    public String getCountryCode(Location location) throws GeocoderNotAvailableException {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses.isEmpty()){
                RestTemplate restTemplate = new RestTemplate();
                return restTemplate.getForObject(String.format("http://api.geonames.org/countryCode?lat=%s&lng=%s&username=%s",
                        Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), Constants.GEONAME_USERNAME),
                        String.class);
            }
            Address obj = addresses.get(0);
            return obj.getCountryCode();
        } catch (IOException e) {
            Log.d(getString(R.string.debug_tag), "IO Exception, couldn't get address from location");
            return "";
        }
    }


    /**
     * @return the string containing all the json from the resources/raw/emergency_number.json
     */
    private String getEmergencyRoomJson() {
        InputStream is = getResources().openRawResource(R.raw.emergency_number);
        int size;
        String emergencyJson;
        try {
            size = is.available();
            byte[] buffer = new byte[size];
            int numBytes = is.read(buffer);
            if (numBytes != size) {
                Log.d(getString(R.string.debug_tag), "Cannot read every bytes of the file");
                return "";
            }
            is.close();
            emergencyJson = new String(buffer);
        } catch (IOException e) {
            Log.d(getString(R.string.debug_tag), "IO Exception, impossible to read from emergency number json");
            return "";
        }
        return emergencyJson;
    }

    private void startNotification(String contentText){
        Intent notificationIntent = new Intent(this, UserHomeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = startNotificationForegroundWithChannel(pendingIntent, contentText);
        }else {
            notification = startNotificationForegroundWithoutChannel(pendingIntent, contentText);
        }
        startForeground(HEALTH_FOREGROUND_ID, notification);
    }

    /**
     * Start the notification foreground w/o a notification channel for devices with API before Oreo
     */
    private Notification startNotificationForegroundWithoutChannel(PendingIntent pendingIntent, String contentText) {
        return buildNotification(new Notification.Builder(this), pendingIntent, contentText);
    }


    /**
     * Start the notification foreground with a notification channel for devices with API Oreo or
     * after
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification startNotificationForegroundWithChannel(PendingIntent pendingIntent, String contentText) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = getString(R.string.notification_channel_id);
        CharSequence channelName = getString(R.string.notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);

        return buildNotification(new Notification
                .Builder(this, getString(R.string.notification_channel_id)), pendingIntent, contentText);
    }

    private Notification buildNotification(Notification.Builder builder, PendingIntent pendingIntent, String contentText) {
        return builder.setContentTitle(getText(R.string.notification_title))
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setContentText(contentText)
                .setTicker(getText(R.string.ticker_text))
                .build();
    }
}
