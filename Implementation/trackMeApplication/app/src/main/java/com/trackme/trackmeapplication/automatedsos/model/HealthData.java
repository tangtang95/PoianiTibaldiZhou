package com.trackme.trackmeapplication.automatedsos.model;

import java.sql.Timestamp;

public class HealthData {

    private Timestamp timestamp;
    private Integer heartbeat;
    private Integer pressureMin;
    private Integer pressureMax;
    private Integer bloodOxygenLevel;

    public HealthData() {

    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Integer getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(Integer pressureMin) {
        this.pressureMin = pressureMin;
    }

    public Integer getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(Integer pressureMax) {
        this.pressureMax = pressureMax;
    }

    public Integer getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }

    public void setBloodOxygenLevel(Integer bloodOxygenLevel) {
        this.bloodOxygenLevel = bloodOxygenLevel;
    }

    public boolean isValidData(){
        return allFieldNotNull() && isBloodOxygenLevelPercentage() && isPressureMaxGreaterThanPressureMin();
    }

    private boolean allFieldNotNull(){
        return timestamp != null && heartbeat != null && pressureMin != null && pressureMax != null
                && bloodOxygenLevel != null;
    }

    private boolean isBloodOxygenLevelPercentage(){
        return bloodOxygenLevel<=100 && bloodOxygenLevel > 0;
    }

    private boolean isPressureMaxGreaterThanPressureMin(){
        return pressureMax >= pressureMin;
    }

    @Override
    public String toString() {
        return "HealthData{" +
                "timestamp=" + timestamp +
                ", heartbeat=" + heartbeat +
                ", pressureMin=" + pressureMin +
                ", pressureMax=" + pressureMax +
                ", bloodOxygenLevel=" + bloodOxygenLevel +
                '}';
    }
}
