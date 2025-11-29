package com.gym.system.Storage;

import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gym.system.*;

@Configuration
public class InMemoryStorage{

    @Bean
    public  Map<String, Trainer> trainerStorage(){
        return new HashMap<>();
    } 

    @Bean
    public  Map<String, Trainee> traineeStorage(){
        return new HashMap<>();
    } 

    @Bean
    public  Map<String, Training> trainingStorage(){
        return new HashMap<>();
    } 

    @Bean
    public  Map<String, TrainingType> trainingTypeStorage(){
        return new HashMap<>();
    } 
}
