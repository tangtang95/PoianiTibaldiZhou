package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.repository.HealthDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.PositionDataRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SendDataServiceImpl implements SendDataService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthDataRepository healthDataRepository;

    @Autowired
    private PositionDataRepository positionDataRepository;

    @Override
    public HealthData sendHealthData(String userId, HealthData healthData) {
        return userRepository.findById(userId).map(user -> {
            healthData.setUser(user);
            return healthDataRepository.save(healthData);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public PositionData sendPosition(String userId, PositionData positionData) {
        return userRepository.findById(userId).map(user ->{
            positionData.setUser(user);
            return positionDataRepository.save(positionData);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public DataWrapper sendClusterOfData(String userId, DataWrapper data) {
        return userRepository.findById(userId).map(user -> {
            long healthDataRowAffected = data.getHealthDataList().stream().map(healthData -> {
                healthData.setUser(user);
                return healthDataRepository.save(healthData);
            }).count();
            long positionDataRowAffected = data.getPositionDataList().stream().map(positionData -> {
                positionData.setUser(user);
                return positionDataRepository.save(positionData);
            }).count();
            return data;
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
