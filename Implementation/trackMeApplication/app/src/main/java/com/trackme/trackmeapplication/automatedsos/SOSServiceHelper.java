package com.trackme.trackmeapplication.automatedsos;

import android.app.Service;
import android.os.Handler;

import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface SOSServiceHelper {

    /**
     * @return the service application of SOS
     */
    Service getService();

    /**
     * Retrieves the birth date of the user of the service
     *
     * @return the java.util.Date of the user
     */
    Date getUserBirthDate();

    /**
     * Retrieves the handler managing the health data callback
     *
     * @return the handler of health data callback
     */
    Handler getHealthDataHandler();

    /**
     *
     * @param birthDate
     */
    void setUserBirthDate(Date birthDate);

    void saveHealthDataInLocalDB(HealthData healthData);

    boolean hasRecentEmergencyCall();

    /**
     * Make an emergency call to the number given by the SOSServiceHelper
     *
     * @return true if successful, false otherwise
     * @throws EmergencyNumberNotFoundException when there is no emergency number available in the actual country
     */
    boolean makeEmergencyCall() throws InterruptedException, ExecutionException, TimeoutException,
            NoPermissionException, EmergencyNumberNotFoundException;
}
