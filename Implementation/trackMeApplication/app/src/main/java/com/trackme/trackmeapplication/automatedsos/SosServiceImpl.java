package com.trackme.trackmeapplication.automatedsos;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;
import com.trackme.trackmeapplication.home.userHome.UserHomeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SosServiceImpl extends SosService {

    private static final int FOREGROUND_ID = 1338;
    private static final String GEOAPI_USERNAME = "trackmeadmin";

    private Date mBirthDate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Handler mHandler = new Handler(new HealthDataCallback(this));

        //TODO get birth date
        mBirthDate = null;

        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(getString(R.string.debug_tag), "No call permission");
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

    @Override
    public String getEmergencyRoomNumber() throws EmergencyNumberNotFoundException, NoPermissionException {
        // For the prototype
        // return "+393384967148";

        // For the real life
        String countryName = getCountryName(getUserLocation());
        ReadContext ctx = JsonPath.parse(getEmergencyRoomJson());
        String jsonPath = "$.data[?(@.Country.Name == '{0}')]..All";
        List<String> numbers = ctx.read(String.format(jsonPath, countryName), List.class);
        for (String number : numbers) {
            if (number.isEmpty())
                return number;
        }
        throw new EmergencyNumberNotFoundException();
    }

    @Override
    public Date getUserBirthDate() {
        return mBirthDate;
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
    private Location getUserLocation() throws NoPermissionException {
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(this.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionException();
        }
        return fusedLocationClient.getLastLocation().getResult();
    }

    /**
     * Retrieves the country name from the geocoder (need ACCESS_LOCATION permission)
     *
     * @param location the location of the user
     * @return the country name where the user is
     */
    private String getCountryName(Location location){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);
            return obj.getCountryName();
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
