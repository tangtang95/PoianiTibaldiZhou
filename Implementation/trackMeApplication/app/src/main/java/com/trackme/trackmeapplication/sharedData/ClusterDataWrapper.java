package com.trackme.trackmeapplication.sharedData;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.localdb.entity.PositionData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster data wrapper object to send a cluster data to the server
 *
 * @author Mattia Tibaldi
 */
public class ClusterDataWrapper implements Serializable {

    //attributes
    private List<HealthDataWrapper> healthDataList;
    private List<PositionDataWrapper> positionDataList;
    private transient List<Integer> healthIDList;
    private transient List<Integer> positionIDList;

    /**
     * Constructor
     */
    public ClusterDataWrapper(){
        healthDataList = new ArrayList<>();
        positionDataList = new ArrayList<>();
        healthIDList = new ArrayList<>();
        positionIDList = new ArrayList<>();
    }

    /**
     * Add new cluster data to the object
     *
     * @param hd health data
     * @param pd position data
     */
    public void addNewClusterData(HealthData hd, PositionData pd){
        HealthDataWrapper healthData = new HealthDataWrapper();
        healthData.setTimestamp(hd.getTimestamp().toString().replace(" ", "T"));
        healthData.setBloodOxygenLevel(hd.getBloodOxygenLevel().toString());
        healthData.setHeartBeat(hd.getHeartbeat().toString());
        healthData.setPressureMax(hd.getPressureMax().toString());
        healthData.setPressureMin(hd.getPressureMin().toString());

        PositionDataWrapper positionData = new PositionDataWrapper();
        positionData.setLatitude(pd.getLatitude().toString());
        positionData.setLongitude(pd.getLongitude().toString());
        positionData.setTimestamp(pd.getTimestamp().toString().replace(" ", "T"));

        healthDataList.add(healthData);
        positionDataList.add(positionData);
        healthIDList.add(hd.getId());
        positionIDList.add(pd.getId());
    }

    //getter methods
    public List<HealthDataWrapper> getHealthDataList() {
        return healthDataList;
    }

    public List<PositionDataWrapper> getPositionDataList() {
        return positionDataList;
    }

    public List<Integer> extractHealthIDList() {
        return healthIDList;
    }

    public List<Integer> extractPositionIDList() {
        return positionIDList;
    }
}
