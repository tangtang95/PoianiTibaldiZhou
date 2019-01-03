package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.util.List;

@Dao
public interface HealthDataDao {

    @Query("SELECT * FROM `health-data`")
    List<HealthData> getAll();

    @Insert
    void insert(HealthData healthData);

    @Query("DELETE FROM `health-data`")
    void deleteAll();

    @Query("DELETE FROM `health-data` WHERE id = :elemID")
    void deleteById(int elemID);

    @Query("SELECT * FROM `health-data` ORDER BY timestamp DESC LIMIT 1")
    HealthData getLast();
}
