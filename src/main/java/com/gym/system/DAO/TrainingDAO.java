package com.gym.system.DAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gym.system.Training;

@Repository
public class TrainingDAO {
    private Map<String, Training> trainingStorage;

    @Autowired
    public void setTrainerStorage(Map<String, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public void save(Training training){
        trainingStorage.put(training.getTraineeId() + "-" + training.getTrainerId() + "-" + training.getDate(), training);
    }

    public void delete(String id){
        trainingStorage.remove(id);
    }

    public Optional<Training> findById(String id){
        return Optional.ofNullable(trainingStorage.get(id));
    }

    public List<Training> findAll(){
        return trainingStorage.values().stream().collect(Collectors.toList());
    }
}
