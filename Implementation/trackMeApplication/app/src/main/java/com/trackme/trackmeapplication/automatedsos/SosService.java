package com.trackme.trackmeapplication.automatedsos;

import android.app.Service;

import com.trackme.trackmeapplication.automatedsos.exception.EmergencyNumberNotFoundException;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;

import java.util.Date;

public abstract class SosService extends Service {

    /**
     * Retrieves the emergency room number from a json database. If the country does not have
     * any emergency room number, an exception is thrown
     *
     * @return the string emergency number of user own country
     * @throws EmergencyNumberNotFoundException if no numbers are found, then this exception is thrown
     */
    public abstract String getEmergencyRoomNumber() throws EmergencyNumberNotFoundException, NoPermissionException;

    /**
     * Retrieves the birth date of the user of the service
     *
     * @return the java.util.Date of the user
     */
    public abstract Date getUserBirthDate();
}
