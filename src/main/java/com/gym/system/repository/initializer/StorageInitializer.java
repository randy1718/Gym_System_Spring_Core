package com.gym.system.repository.initializer;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.system.model.Trainee;
import com.gym.system.model.Trainer;
import com.gym.system.model.Training;
import com.gym.system.model.TrainingType;
import com.gym.system.util.PasswordGenerator;
import com.gym.system.util.UsernameDuplicates;

@Component
@PropertySource("classpath:application.properties")
public class StorageInitializer implements BeanPostProcessor {

    @Value("${storage.data.path}")
    private String storageDataPath;

    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode rootNode = null;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        if (rootNode == null) {
            rootNode = loadJson();
        }

        if(bean instanceof Map){

            if (beanName.equals("traineeStorage")) {
                logger.info("Initializing storage '{}'", beanName);
                Map<String, Trainee> traineeStorage = (Map<String, Trainee>) bean;

                JsonNode traineeArray = rootNode.get("trainees");
                if (traineeArray != null && traineeArray.isArray()) {
                    for (JsonNode node : traineeArray) {
                        Trainee t = mapper.convertValue(node, Trainee.class);
                        String username = t.getFirstName() + "." + t.getLastName();
                        t.setUsername(UsernameDuplicates.checkUsernameDuplicatesTrainees(username, traineeStorage));
                        t.setPassword(PasswordGenerator.generate());
                        t.setIsActive(true);
                        traineeStorage.put(t.getId(), t);
                    }
                }
            }

            if (beanName.equals("trainerStorage")) {
                logger.info("Initializing storage '{}'", beanName);
                Map<String, Trainer> trainerStorage = (Map<String, Trainer>) bean;

                JsonNode trainerArray = rootNode.get("trainers");
                if (trainerArray != null && trainerArray.isArray()) {
                    for (JsonNode node : trainerArray) {
                        Trainer t = mapper.convertValue(node, Trainer.class);
                        String username = t.getFirstName() + "." + t.getLastName();
                        t.setUsername(UsernameDuplicates.checkUsernameDuplicatesTrainers(username, trainerStorage));
                        t.setPassword(PasswordGenerator.generate());
                        t.setIsActive(true);
                        trainerStorage.put(t.getId(), t);
                    }
                }
            }

            if (beanName.equals("trainingStorage")) {
                logger.info("Initializing storage '{}'", beanName);
                Map<String, Training> trainingStorage = (Map<String, Training>) bean;

                JsonNode trainingArray = rootNode.get("trainings");
                if (trainingArray != null && trainingArray.isArray()) {
                    for (JsonNode node : trainingArray) {
                        Training t = mapper.convertValue(node, Training.class);
                        trainingStorage.put(t.getTraineeId() + "-" + t.getTrainerId() + "-" + t.getDate(), t);
                    }
                }
            }

            if (beanName.equals("trainingTypeStorage")) {
                logger.info("Initializing storage '{}'", beanName);
                Map<String, TrainingType> trainingTypeStorage = (Map<String, TrainingType>) bean;

                JsonNode trainingTypeArray = rootNode.get("trainingTypes");
                if (trainingTypeArray != null && trainingTypeArray.isArray()) {
                    for (JsonNode node : trainingTypeArray) {
                        TrainingType t = mapper.convertValue(node, TrainingType.class);
                        trainingTypeStorage.put(t.getName(), t);
                    }
                }
            }
        }
        return bean;
    }

    private JsonNode loadJson() {
        try {
            String path = storageDataPath.replace("classpath:", "");
            logger.info("Loading JSON storage file from: {}", path);
            try(InputStream in = getClass().getClassLoader().getResourceAsStream(path)){
                if (in == null) {
                    logger.error("JSON file NOT FOUND: {}", storageDataPath);
                    throw new RuntimeException("JSON file not found in classpath: " + storageDataPath);
                }
                logger.info("JSON storage file loaded successfully.");
                return mapper.readTree(in);
            }
        } catch (Exception e) {
            logger.error("Failed to load JSON file: {}", e.getMessage());
            throw new RuntimeException("Failed to load JSON storage file", e);
        }
    }
}
