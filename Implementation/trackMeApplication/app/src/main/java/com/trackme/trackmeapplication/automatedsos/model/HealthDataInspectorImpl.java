package com.trackme.trackmeapplication.automatedsos.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HealthDataInspectorImpl implements HealthDataInspector {

    private final Date mBirthDate;
    private ThresholdInteger heartbeatThreshold;
    private final ThresholdInteger pressureMinThreshold;
    private final ThresholdInteger pressureMaxThreshold;
    private final ThresholdInteger bloodOxygenLevelThreshold;

    public HealthDataInspectorImpl(Date birhtDate) {
        mBirthDate = birhtDate;
        heartbeatThreshold = getNewHearthBeatThreshold();
        pressureMinThreshold = new ThresholdInteger(40, 100);
        pressureMaxThreshold = new ThresholdInteger(200, 80);
        bloodOxygenLevelThreshold = new ThresholdInteger(100, 80);
    }

    @Override
    public boolean isGraveCondition(HealthData healthData) {
        heartbeatThreshold = getNewHearthBeatThreshold();
        return !(heartbeatThreshold.contains(healthData.getHeartbeat()) &&
                pressureMinThreshold.contains(healthData.getPressureMin()) &&
                pressureMaxThreshold.contains(healthData.getPressureMax()) &&
                bloodOxygenLevelThreshold.contains(healthData.getBloodOxygenLevel()));
    }

    private ThresholdInteger getNewHearthBeatThreshold(){
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mBirthDate);
        Integer birthYear =  calendar.get(Calendar.YEAR);
        return new ThresholdInteger(220 - (currentYear - birthYear), 30);
    }
}
