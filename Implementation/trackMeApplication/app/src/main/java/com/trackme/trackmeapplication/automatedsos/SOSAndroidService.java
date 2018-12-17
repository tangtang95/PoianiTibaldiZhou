package com.trackme.trackmeapplication.automatedsos;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Tasks;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;

import net.minidev.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SOSAndroidService extends Service {

    private static final int FOREGROUND_ID = 1338;

    private Date mBirthDate;
    private final IBinder mBinder = new LocalBinder();
    private Handler mHandler;

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        public SOSAndroidService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SOSAndroidService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(new HealthDataCallback(new SOSServiceHelperImpl(this)));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!intent.hasExtra(getString(R.string.birth_date_key))){
            return super.onStartCommand(intent, flags, startId);
        }

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.YEAR_FIELD);
        try {
            mBirthDate = dateFormat.parse(intent.getStringExtra(getString(R.string.birth_date_key)));
        } catch (ParseException e) {
            Log.d(getString(R.string.debug_tag), "Could not parse date due to date format");
            return super.onStartCommand(intent, flags, startId);
        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            Log.d(getString(R.string.debug_tag), "Bluetooth adapter existing");
            if (bluetoothAdapter.isEnabled()) {
                Log.d(getString(R.string.debug_tag), "Bluetooth is enabled");
                BluetoothServer bluetoothServer = new BluetoothServer(bluetoothAdapter, mHandler);
                bluetoothServer.start();
            }
        }

        Log.d(getString(R.string.debug_tag), "Start notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startNotificationForegroundWithChannel();
        } else {
            startNotificationForegroundWithoutChannel();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public String getEmergencyRoomNumber() throws EmergencyNumberNotFoundException, NoPermissionException,
            InterruptedException, ExecutionException, TimeoutException {
        // For the prototype
        // return "+393384967148";

        // For the real life
        String countryCode = getCountryCode(getUserLocation());
        ReadContext ctx = JsonPath.parse(getEmergencyRoomJson());
        String jsonPath = "$.data[?(@.Country.ISOCode == '" + countryCode + "')]..All";
        List<JSONArray> jsonArrays = ctx.read(jsonPath, List.class);
        for (JSONArray arr : jsonArrays) {
            if (arr.get(0) != null)
                return (String) arr.get(0);
        }
        throw new EmergencyNumberNotFoundException();
    }

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

    /**
     * @return the location of the user using GPS (need ACCESS_LOCATION permission)
     */
    private Location getUserLocation() throws NoPermissionException, InterruptedException, ExecutionException, TimeoutException {
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(this.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionException();
        }
        return Tasks.await(fusedLocationClient.getLastLocation(), 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieves the country code from the geocoder (need ACCESS_LOCATION permission)
     *
     * @param location the location of the user
     * @return the country code where the user is
     */
    private String getCountryCode(Location location){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);
            return obj.getCountryCode();

        } catch (IOException e) {
            Log.d(getString(R.string.debug_tag), "IO Exception, couldn't get address from location");
            return "";
        }
    }

    /**
     * Start the notification foreground w/o a notification channel for devices with API before Oreo
     */
    private void startNotificationForegroundWithoutChannel() {
        Intent notificationIntent = new Intent(this, UserHomeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle(getText(R.string.notification_title))
                        .setSmallIcon(R.drawable.icon)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.ticker_text))
                        .build();

        startForeground(FOREGROUND_ID, notification);
    }


    /**
     * Start the notification foreground with a notification channel for devices with API Oreo or
     * after
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startNotificationForegroundWithChannel() {
        Intent notificationIntent = new Intent(this, UserHomeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);

        Notification notification =
                new Notification.Builder(this, notificationChannel.getId())
                        .setContentTitle(getText(R.string.notification_title))
                        .setSmallIcon(R.drawable.icon)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.ticker_text))
                        .build();

        startForeground(FOREGROUND_ID, notification);
    }
}
