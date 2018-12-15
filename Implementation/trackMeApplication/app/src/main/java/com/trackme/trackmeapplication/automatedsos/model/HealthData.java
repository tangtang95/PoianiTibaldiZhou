package com.trackme.trackmeapplication.automatedsos.model;

import java.time.LocalDateTime;

public class HealthData {

    private LocalDateTime timestamp;
    private Integer heartbeat;
    private Integer pressureMin;
    private Integer pressureMax;
    private Integer bloodOxygenLevel;

    public HealthData() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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
}
