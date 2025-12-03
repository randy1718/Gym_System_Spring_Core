package com.gym.system.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gym.system.model.Training;

@Repository
public class TrainingDAO {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);
    private Map<String, Training> trainingStorage;

    @Autowired
    public TrainingDAO(Map<String, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public void save(Training training){
        logger.debug("Saving training in storage {}", training.getTrainingName());
        trainingStorage.put(training.getTraineeId() + "-" + training.getTrainerId() + "-" + training.getDate(), training);
    }

    public void delete(String id){
        logger.debug("Deleting training {}", id);
        trainingStorage.remove(id);
    }

    public Optional<Training> findById(String id){
        logger.debug("Finding training {}", id);
        return Optional.ofNullable(trainingStorage.get(id));
    }

    public List<Training> findAll(){
        return trainingStorage.values().stream().collect(Collectors.toList());
    }
}
