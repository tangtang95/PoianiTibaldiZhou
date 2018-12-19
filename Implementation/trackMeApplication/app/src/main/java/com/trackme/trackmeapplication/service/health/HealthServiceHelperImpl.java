package com.trackme.trackmeapplication.service.health;

import android.Manifest;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.GeocoderNotAvailableException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class HealthServiceHelperImpl implements HealthServiceHelper {

    private HealthService service;
    private AppDatabase appDatabase;

    public HealthServiceHelperImpl(HealthService service) {
        this.service = service;
        this.appDatabase = Room.databaseBuilder(service.getApplicationContext(),
                AppDatabase.class, service.getString(R.string.persistent_database_name)).build();
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public Date getUserBirthDate() {
        return service.getUserBirthDate();
    }

    @Override
    public Handler getHealthDataHandler() {
        return service.getHealthDataHandler();
    }

    @Override
    public void setUserBirthDate(Date birthDate) {
        service.setUserBirthDate(birthDate);
    }

    @Override
    public void saveHealthData(HealthData healthData) {
        // TODO IN AsyncTask
        //appDatabase.getHealthDataDao().insert(healthData);
    }

    @Override
    public boolean hasRecentEmergencyCall() {
        // TODO IN AsyncTask
        //return appDatabase.getEmergencyCallDao().getNumberOfRecentCalls() > 0;
        return false;
    }

    @Override
    public boolean makeEmergencyCall() throws InterruptedException, ExecutionException, TimeoutException, NoPermissionException, EmergencyNumberNotFoundException, GeocoderNotAvailableException {
        if (ActivityCompat.checkSelfPermission(service.getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionException();
        }

        // Make call
        String phoneNumber = service.getEmergencyRoomNumber(getCurrentCountryCode());
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: FAKE " + phoneNumber));
        List<ResolveInfo> activityList = service.getPackageManager().queryIntentActivities(callIntent, 0);
        ResolveInfo app = activityList.get(0);
        callIntent.setClassName(app.activityInfo.packageName, app.activityInfo.name);
        service.startActivity(callIntent);

        // Save Emergency call in the DB
        EmergencyCall emergencyCall = new EmergencyCall();
        emergencyCall.setPhoneNumber(phoneNumber);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        emergencyCall.setTimestamp(new Timestamp(calendar.getTime().getTime()));

        //TODO Async Task
        //appDatabase.getEmergencyCallDao().insert(emergencyCall);
        return true;
    }

    private String getCurrentCountryCode() throws InterruptedException, ExecutionException, TimeoutException, NoPermissionException, GeocoderNotAvailableException {
        return service.getCountryCode(service.getUserLocation());
    }
}
