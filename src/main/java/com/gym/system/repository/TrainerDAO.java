package com.gym.system.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gym.system.model.Trainer;
import com.gym.system.util.PasswordGenerator;
import com.gym.system.util.UsernameDuplicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class TrainerDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);
    private Map<String, Trainer> trainerStorage;

    @Autowired
    public TrainerDAO(Map<String, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public void save(Trainer trainer){
        String username = trainer.getFirstName() + "." + trainer.getLastName();
        trainer.setUsername(UsernameDuplicates.checkUsernameDuplicatesTrainers(username, trainerStorage));
        trainer.setPassword(PasswordGenerator.generate());
        if(trainer.getId() == null){
            trainer.setId("t" + (this.findAll().size() + 1));
        }
        trainer.setIsActive(true);
        logger.debug("Saving trainer in storage {}", trainer.getUsername());
        trainerStorage.put(trainer.getId(), trainer);
    }

    public void update(Trainer trainer){
        logger.debug("Updating trainer {}", trainer.getUsername());
        trainerStorage.put(trainer.getId(), trainer);
    }

    public void delete(String id){
        logger.debug("Deleting trainer {}", id);
        trainerStorage.remove(id);
    }

    public Optional<Trainer> findById(String id){
        logger.debug("Finding trainer {}", id);
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public List<Trainer> findAll(){
        return trainerStorage.values().stream().collect(Collectors.toList());
    }
}
