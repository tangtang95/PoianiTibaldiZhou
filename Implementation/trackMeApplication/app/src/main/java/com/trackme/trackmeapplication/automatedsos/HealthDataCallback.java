package com.trackme.trackmeapplication.automatedsos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.InvalidHealthDataException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;
import com.trackme.trackmeapplication.automatedsos.model.HealthData;
import com.trackme.trackmeapplication.automatedsos.model.HealthDataInspector;
import com.trackme.trackmeapplication.automatedsos.model.HealthDataInspectorImpl;
import com.trackme.trackmeapplication.automatedsos.model.MessageType;

import java.util.List;
import java.util.Objects;

public class HealthDataCallback implements Callback {

    private SosService service;

    /**
     * Constructor.
     * Create a health data callback to handle message from the bluetooth socket
     *
     * @param service the SOS service running in notification foreground
     */
    public HealthDataCallback(SosService service) {
        this.service = service;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MessageType.HEALTH_DATA:
                boolean success = false;
                HealthData healthData = (HealthData) message.obj;

                HealthDataInspector healthDataInspector = new HealthDataInspectorImpl(service.getUserBirthDate());
                try {
                    if (healthDataInspector.isGraveCondition(healthData)) {
                        if (!hasRecentEmergencyCall()) {
                            success = makeEmergencyCall();
                        }
                    }
                } catch (InvalidHealthDataException e) {
                    Log.d(service.getString(R.string.debug_tag), "Cannot make call due to invalid health data");
                } catch (EmergencyNumberNotFoundException e) {
                    Log.d(service.getString(R.string.debug_tag), "Cannot make call due to no number avaialable");
                } catch (NoPermissionException e) {
                    Log.d(service.getString(R.string.debug_tag), "Cannot make call due to no permission (GPS)");
                }
                // TODO save data on a file/DB
                return success;
            default: //DO NOTHING
                break;
        }

        return true;
    }

    /**
     * Make an emergency call to the number given by the SosService
     *
     * @return true if successful, false otherwise
     * @throws EmergencyNumberNotFoundException when there is no emergency number available in the actual country
     */
    private boolean makeEmergencyCall() throws EmergencyNumberNotFoundException, NoPermissionException {
        if (ActivityCompat.checkSelfPermission(service.getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(service.getString(R.string.debug_tag), "no permission");
            return false;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service.getEmergencyRoomNumber()));
        List<ResolveInfo> activityList = service.getPackageManager().queryIntentActivities(callIntent, 0);
        ResolveInfo app = activityList.get(0);
        callIntent.setClassName(app.activityInfo.packageName, app.activityInfo.name);
        service.startActivity(callIntent);
        return true;
    }

    /**
     * Checks if there are recent (within 1 hour from now) emergency calls from the call registry
     *
     * @return true if there are recent emergency calls, false otherwise
     * @throws EmergencyNumberNotFoundException when there is no emergency number available in the actual country
     */
    private boolean hasRecentEmergencyCall() throws EmergencyNumberNotFoundException, NoPermissionException {
        if (ActivityCompat.checkSelfPermission(service, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // If permission not granted, return true so that the call is not going to be done.
            return true;
        }
        //TODO FIX query
        final String[] projection = {CallLog.Calls.NUMBER};
        final String selection = CallLog.Calls.NUMBER + " = ? AND " + CallLog.Calls.OUTGOING_TYPE + "is ?";
        Cursor cursor = service.getContentResolver().query(CallLog.CONTENT_URI, projection,
                selection, new String[]{service.getEmergencyRoomNumber(), "TRUE"},
                CallLog.Calls.DEFAULT_SORT_ORDER);
        return Objects.requireNonNull(cursor).getCount() > 0;
    }
}
