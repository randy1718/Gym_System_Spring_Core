package com.gym.system.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.gym.system.AppConfig;
import com.gym.system.Trainee;
import com.gym.system.Trainer;
import com.gym.system.Training;
import com.gym.system.TrainingType;
import com.gym.system.DAO.TrainingDAO;
import com.gym.system.DAO.TrainerDAO;
import com.gym.system.DAO.TraineeDAO;

@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private final Map<String, TrainingType> trainingTypeStorage;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO, TrainerDAO trainerDAO, TraineeDAO traineeDAO, Map<String, TrainingType> trainingTypeStorage) { 
        this.trainingDAO = trainingDAO;
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
        this.trainingTypeStorage = trainingTypeStorage;
    }

    public void create(Training t){

        Optional<Trainer> foundTrainer = trainerDAO.findById(t.getTrainerId());
        if (!foundTrainer.isPresent()) {
            throw new IllegalArgumentException("Trainer ID does not exist");
        }

        Optional<Trainee> foundTrainee = traineeDAO.findById(t.getTraineeId());
        if (!foundTrainee.isPresent()) {
            throw new IllegalArgumentException("Trainee ID does not exist");
        }

        if (!trainingTypeStorage.containsKey(t.getTrainingType().getName())) {
            throw new IllegalArgumentException("Training type does not exist");
        }

        trainingDAO.save(t);
    }

    public Optional<Training> findById(String id){
        return trainingDAO.findById(id);
    }

    public List<Training> findAll(){
        return trainingDAO.findAll();
    }
}
