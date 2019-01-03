package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.trackme.trackmeapplication.localdb.entity.PositionData;

import java.util.List;

@Dao
public interface PositionDataDao {

    @Query("SELECT * FROM `position-data`")
    List<PositionData> getAll();

    @Insert
    void insert(PositionData positionData);

    @Query("DELETE FROM `position-data` WHERE id = :elemID")
    void deleteById(int elemID);

    @Query("DELETE FROM `position-data`")
    void deleteAll();

}
