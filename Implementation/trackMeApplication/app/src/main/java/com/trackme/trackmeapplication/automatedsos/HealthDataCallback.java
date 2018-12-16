package com.trackme.trackmeapplication.automatedsos;

import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.InvalidHealthDataException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;
import com.trackme.trackmeapplication.automatedsos.model.HealthDataInspector;
import com.trackme.trackmeapplication.automatedsos.model.HealthDataInspectorImpl;
import com.trackme.trackmeapplication.automatedsos.model.MessageType;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class HealthDataCallback implements Callback {

    private SOSServiceHelper helper;

    /**
     * Constructor.
     * Create a health data callback to handle message from the bluetooth socket
     *
     * @param helper the SOS helper running in notification foreground
     */
    public HealthDataCallback(SOSServiceHelper helper) {
        this.helper = helper;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MessageType.HEALTH_DATA:
                boolean success = false;
                HealthData healthData = (HealthData) message.obj;

                // Save health data if it is a valid one
                if (healthData.isValidData())
                    helper.saveHealthDataInLocalDB(healthData);

                // Make calls if necessary
                HealthDataInspector healthDataInspector = new HealthDataInspectorImpl(helper.getUserBirthDate());
                try {
                    if (healthDataInspector.isGraveCondition(healthData)) {
                        if (!helper.hasRecentEmergencyCall()) {
                            success = helper.makeEmergencyCall();
                        }
                    }
                } catch (InvalidHealthDataException e) {
                    Log.d(helper.getService().getString(R.string.debug_tag), "Cannot make call due to invalid health data");
                } catch (EmergencyNumberNotFoundException e) {
                    Log.d(helper.getService().getString(R.string.debug_tag), "Cannot make call due to no number available");
                } catch (NoPermissionException e) {
                    Log.d(helper.getService().getString(R.string.debug_tag), "Cannot make call due to no permission (GPS)");
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    Log.d(helper.getService().getString(R.string.debug_tag), "Cannot make call due to no GPS available");
                }
                return success;
            default:
                break;
        }

        return false;
    }
}
