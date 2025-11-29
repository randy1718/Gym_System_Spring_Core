package com.gym.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.system.Training;
import com.gym.system.DAO.TrainingDAO;

@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO) { 
        this.trainingDAO = trainingDAO;
    }

    public void create(Training t){
        trainingDAO.save(t);
    }

    public Optional<Training> findById(String id){
        return trainingDAO.findById(id);
    }

    public List<Training> findAll(){
        return trainingDAO.findAll();
    }
}
