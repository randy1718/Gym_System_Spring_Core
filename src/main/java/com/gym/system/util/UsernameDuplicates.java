package com.gym.system.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gym.system.model.Trainee;
import com.gym.system.model.Trainer;

public class UsernameDuplicates {
    public static String checkUsernameDuplicatesTrainees(String baseUsername, Map<String, Trainee> traineeStorage) {

        List<Trainee> trainees = traineeStorage.values().stream().collect(Collectors.toList());
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

    public static String checkUsernameDuplicatesTrainers(String baseUsername, Map<String, Trainer> trainerStorage) {

        List<Trainer> trainers = trainerStorage.values().stream().collect(Collectors.toList());
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
