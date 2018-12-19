package com.trackme.trackmeapplication.service.health;

import android.app.Service;
import android.os.Handler;

import com.trackme.trackmeapplication.service.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.service.exception.GeocoderNotAvailableException;
import com.trackme.trackmeapplication.service.exception.NoPermissionException;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface HealthServiceHelper {

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
     * Set the user birth date
     * @param birthDate the birth date of the user
     */
    void setUserBirthDate(Date birthDate);

    /**
     * Save the health data in the local DB
     *
     * @param healthData the new health data
     */
    void saveHealthData(HealthData healthData);

    /**
     * @return true if there are recent (within 1 hour) emergency call, false otherwise
     */
    boolean hasRecentEmergencyCall();

    /**
     * Make an emergency call to the number given by the HealthServiceHelper
     *
     * @return true if successful, false otherwise
     * @throws EmergencyNumberNotFoundException when there is no emergency number available in the actual country
     */
    boolean makeEmergencyCall() throws InterruptedException, ExecutionException, TimeoutException,
            NoPermissionException, EmergencyNumberNotFoundException, GeocoderNotAvailableException;
}
