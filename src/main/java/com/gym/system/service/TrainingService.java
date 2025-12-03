package com.gym.system.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gym.system.model.Trainee;
import com.gym.system.model.Trainer;
import com.gym.system.model.Training;
import com.gym.system.model.TrainingType;
import com.gym.system.repository.TraineeDAO;
import com.gym.system.repository.TrainerDAO;
import com.gym.system.repository.TrainingDAO;

@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final Map<String, TrainingType> trainingTypeStorage;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO, TrainerDAO trainerDAO, TraineeDAO traineeDAO, Map<String, TrainingType> trainingTypeStorage) { 
        this.trainingDAO = trainingDAO;
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
        this.trainingTypeStorage = trainingTypeStorage;
    }

    public void create(Training t){

        logger.info("Service: Creating new training session for trainee {} with trainer {}",
                t.getTraineeId(), t.getTrainerId());

        logger.debug("Service: Validating trainer with id {}", t.getTrainerId());
        Optional<Trainer> foundTrainer = trainerDAO.findById(t.getTrainerId());
        if (!foundTrainer.isPresent()) {
            logger.error("Service: Trainer {} does not exist", t.getTrainerId());
        }

        logger.debug("Service: Validating trainee with id {}", t.getTraineeId());
        Optional<Trainee> foundTrainee = traineeDAO.findById(t.getTraineeId());
        if (!foundTrainee.isPresent()) {
            logger.error("Service: Trainee {} does not exist", t.getTraineeId());
            throw new IllegalArgumentException("Trainee ID does not exist");
        }

        logger.debug("Service: Validating training type {}", t.getTrainingType().getName());
        if (!trainingTypeStorage.containsKey(t.getTrainingType().getName())) {
            logger.error("Service: Training type {} does not exist", t.getTrainingType().getName());
            throw new IllegalArgumentException("Training type does not exist");
        }

        logger.info("Service: All validations passed. Saving training...");
        trainingDAO.save(t);
    }

    public Optional<Training> findById(String id){
        logger.info("Service: Fetching training with id {}", id);
        return trainingDAO.findById(id);
    }

    public List<Training> findAll(){
        logger.info("Service: Fetching all trainings");
        return trainingDAO.findAll();
    }
}
