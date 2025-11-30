package com.gym.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.system.Trainer;
import com.gym.system.DAO.TrainerDAO;
import com.gym.system.Storage.PasswordGenerator;

@Service
public class TrainerService {

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO) { 
        this.trainerDAO = trainerDAO;
    }

    public void create(Trainer t){
        String username = t.getFirstName() + "." + t.getLastName();
        t.setUsername(checkUsernameDuplicates(username));
        t.setPassword(PasswordGenerator.generate());
        if(t.getId() == null){
            t.setId("t" + (trainerDAO.findAll().size() + 1));
        }
        t.setIsActive(true);
        trainerDAO.save(t);
    }

    public void update(Trainer t){
        trainerDAO.save(t);
    }

    public Optional<Trainer> findById(String id){
        return trainerDAO.findById(id);
    }

    public List<Trainer> findAll(){
        return trainerDAO.findAll();
    }

    private String checkUsernameDuplicates(String baseUsername) {

        List<Trainer> trainers = trainerDAO.findAll();
        boolean exists = trainers.stream()
                .anyMatch(t -> t.getUsername().equalsIgnoreCase(baseUsername));

        if (!exists) return baseUsername;

        int counter = 1;

        while (true) {
            String newUsername = baseUsername + counter;
            boolean conflict = trainers.stream()
                    .anyMatch(t -> t.getUsername().equalsIgnoreCase(newUsername));

            if (!conflict) return newUsername;

            counter++;
        }
    }
}
