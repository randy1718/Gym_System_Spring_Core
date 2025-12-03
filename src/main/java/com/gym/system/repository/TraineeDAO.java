package com.gym.system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gym.system.model.Trainee;
import com.gym.system.util.PasswordGenerator;
import com.gym.system.util.UsernameDuplicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TraineeDAO {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);
    private Map<String, Trainee> traineeStorage;

    @Autowired
    public TraineeDAO(Map<String, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public void save(Trainee trainee){
        String username = trainee.getFirstName() + "." + trainee.getLastName();
        trainee.setUsername(UsernameDuplicates.checkUsernameDuplicatesTrainees(username, traineeStorage));
        trainee.setPassword(PasswordGenerator.generate());
        if(trainee.getId() == null){
            trainee.setId("u" + (findAll().size() + 1));
        }
        trainee.setIsActive(true);
        logger.debug("Saving trainee in storage {}", trainee.getUsername());
        traineeStorage.put(trainee.getId(), trainee);
    }

    public void update(Trainee trainee){
        logger.debug("Updating trainee {}", trainee.getUsername());
        traineeStorage.put(trainee.getId(), trainee);
    }

    public void delete(String id){
        logger.debug("Deleting trainee {}", id);
        traineeStorage.remove(id);
    }

    public Optional<Trainee> findById(String id){
        logger.debug("Finding trainee {}", id);
        return Optional.ofNullable(traineeStorage.get(id));
    }

    public List<Trainee> findAll(){
        return traineeStorage.values().stream().collect(Collectors.toList());
    }
}
