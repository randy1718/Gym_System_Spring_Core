package com.gym.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gym.system.model.Trainer;
import com.gym.system.repository.TrainerDAO;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO) { 
        this.trainerDAO = trainerDAO;
    }

    public void create(Trainer t){
        logger.info("Service: Creating trainer {} {}", t.getFirstName(), t.getLastName());
        trainerDAO.save(t);
    }

    public void update(Trainer t){
        logger.info("Service: Updating trainer with id {}", t.getId());
        trainerDAO.update(t);
    }

    public Optional<Trainer> findById(String id){
        logger.info("Service: Finding trainer with id {}", id);
        return trainerDAO.findById(id);
    }

    public List<Trainer> findAll(){
        logger.info("Service: Retrieving all trainers");
        return trainerDAO.findAll();
    }
}
