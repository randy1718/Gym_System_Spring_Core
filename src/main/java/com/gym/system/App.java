package com.gym.system;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gym.system.Storage.StorageInitializer;
import com.gym.system.service.GymServices;

public class App {
    public static void main( String[] args ){

        Logger logger = LoggerFactory.getLogger(StorageInitializer.class);
        logger.info("Initializing GYM System...");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GymServices facade = context.getBean(GymServices.class);
        Map<String, TrainingType> trainingTypes = context.getBean("trainingTypeStorage", Map.class);
        List<TrainingType> trainingTypeList = trainingTypes.values().stream().collect(Collectors.toList());
        
        System.out.println("Loaded trainees:-------------");
        facade.findAllTrainees().forEach(System.out::println);
        System.out.println("Loaded trainers:-------------");
        facade.findAllTrainers().forEach(System.out::println);
        System.out.println("Loaded trainings:-------------");
        facade.findAllTrainings().forEach(System.out::println);
        System.out.println("Loaded training Types:-------------");
        trainingTypeList.forEach(System.out::println);
    }
}
