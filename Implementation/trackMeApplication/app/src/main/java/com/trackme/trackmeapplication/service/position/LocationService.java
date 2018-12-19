package com.trackme.trackmeapplication.service.position;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

public class LocationService extends Service {

    // Acquire a reference to the system Location Manager
    private LocationManager locationManager;

    // Define a listener that responds to location updates
    private LocationListener locationListener = new UserLocationListener(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpGPS();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Set up the GPS service, which will get the location
     */
    private void setUpGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }

}
