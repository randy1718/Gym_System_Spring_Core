package com.gym.system.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gym.system.Trainee;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TraineeDAO {

    private Map<String, Trainee> traineeStorage;

    @Autowired
    public void setTraineeStorage(Map<String, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public void save(Trainee trainee){
        traineeStorage.put(trainee.getId(), trainee);
    }

    public void delete(String id){
        traineeStorage.remove(id);
    }

    public Optional<Trainee> findById(String id){
        return Optional.ofNullable(traineeStorage.get(id));
    }

    public List<Trainee> findAll(){
        return traineeStorage.values().stream().collect(Collectors.toList());
    }
}
