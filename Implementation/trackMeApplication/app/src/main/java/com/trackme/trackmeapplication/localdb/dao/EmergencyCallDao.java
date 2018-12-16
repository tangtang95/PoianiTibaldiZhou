package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;

import java.util.List;

@Dao
public interface EmergencyCallDao {

    @Query("SELECT * FROM `emergency-calls`")
    List<EmergencyCall> getAll();

    @Query("SELECT COUNT(*) FROM `emergency-calls` WHERE (julianday('now') - julianday(timestamp)) * 24 * 60 < 60")
    long getNumberOfRecentCalls();

    @Insert
    void insert(EmergencyCall emergencyCall);

    @Query("DELETE FROM `emergency-calls` WHERE (julianday('now') - julianday(timestamp)) * 24 * 60 < 60")
    void deleteAllRecentCalls();

}
