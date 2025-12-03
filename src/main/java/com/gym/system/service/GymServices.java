package com.gym.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gym.system.model.Trainee;
import com.gym.system.model.Trainer;
import com.gym.system.model.Training;

@Component
public class GymServices {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymServices(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void createTrainee(Trainee t){
        traineeService.create(t);
    }

    public void createTrainer(Trainer t){
        trainerService.create(t);
    }

    public void createTraining(Training t){
        trainingService.create(t);
    }

    public void updateTrainee(Trainee t){
        traineeService.update(t);
    }

    public void updateTrainer(Trainer t){
        trainerService.update(t);
    }

    public void deleteTrainee(String id){
        traineeService.delete(id);
    }

    public Optional<Trainee> findTraineeById(String id){
        return traineeService.findById(id);
    }

    public Optional<Trainer> findTrainerById(String id){
        return trainerService.findById(id);
    }

    public Optional<Training> findTrainingById(String id){
        return trainingService.findById(id);
    }


    public List<Trainee> findAllTrainees(){
        return traineeService.findAll();
    }

    public List<Trainer> findAllTrainers(){
        return trainerService.findAll();
    }

    public List<Training> findAllTrainings(){
        return trainingService.findAll();
    }

}
