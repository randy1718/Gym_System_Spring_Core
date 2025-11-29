package com.gym.system.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.system.Trainee;
import com.gym.system.DAO.TraineeDAO;
import com.gym.system.Storage.PasswordGenerator;

@Service
public class TraineeService {

    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO) { 
        this.traineeDAO = traineeDAO;
    }

    public void create(Trainee t){
        String username = t.getFirstName() + "." + t.getLastName();
        t.setUsername(checkUsernameDuplicates(username));
        t.setPassword(PasswordGenerator.generate());
        if(t.getId() == null){
            t.setId("u" + (traineeDAO.findAll().size() + 1));
        }
        t.setIsActive(true);
        traineeDAO.save(t);
    }

    public void update(Trainee t){
        traineeDAO.save(t);
    }

    public void delete(String id){
        traineeDAO.delete(id);
    }

    public Optional<Trainee> findById(String id){
        return traineeDAO.findById(id);
    }

    public List<Trainee> findAll(){
        return traineeDAO.findAll();
    }

    private String checkUsernameDuplicates(String baseUsername) {

        List<Trainee> trainees = traineeDAO.findAll();
        boolean exists = trainees.stream()
                .anyMatch(t -> t.getUsername().equalsIgnoreCase(baseUsername));

        if (!exists) return baseUsername;

        int counter = 1;

        while (true) {
            String newUsername = baseUsername + counter;
            boolean conflict = trainees.stream()
                    .anyMatch(t -> t.getUsername().equalsIgnoreCase(newUsername));

            if (!conflict) return newUsername;

            counter++;
        }
    }
}
