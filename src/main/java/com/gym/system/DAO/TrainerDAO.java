package com.gym.system.DAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gym.system.Trainer;


@Repository
public class TrainerDAO {
    private Map<String, Trainer> trainerStorage;

    @Autowired
    public void setTrainerStorage(Map<String, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public void save(Trainer trainer){
        trainerStorage.put(trainer.getId(), trainer);
    }

    public void delete(String id){
        trainerStorage.remove(id);
    }

    public Optional<Trainer> findById(String id){
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public List<Trainer> findAll(){
        return trainerStorage.values().stream().collect(Collectors.toList());
    }
}
