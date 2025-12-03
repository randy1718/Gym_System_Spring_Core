package com.gym.system.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gym.system.model.Trainee;
import com.gym.system.repository.TraineeDAO;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO) { 
        this.traineeDAO = traineeDAO;
    }

    public void create(Trainee t){
        logger.info("Service: Creating trainee {} {}", t.getFirstName(), t.getLastName());
        traineeDAO.save(t);
    }

    public void update(Trainee t){
        logger.info("Service: Updating trainee with id {}", t.getId());
        traineeDAO.update(t);
    }

    public void delete(String id){
        logger.info("Service: Deleting trainee with id {}", id);
        traineeDAO.delete(id);
    }

    public Optional<Trainee> findById(String id){
        logger.info("Service: Finding trainee with id {}", id);
        return traineeDAO.findById(id);
    }

    public List<Trainee> findAll(){
        logger.info("Service: Retrieving all trainees");
        return traineeDAO.findAll();
    }
}
