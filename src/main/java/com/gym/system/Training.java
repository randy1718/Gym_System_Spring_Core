package com.gym.system;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Training {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime date;
    private int duration;


    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId){
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId){
        this.trainerId = trainerId;
    }

    public String getTrainingName(){
        return trainingName;
    }

    public void setTrainingName(String trainingName){
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType(){
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType){
        this.trainingType = trainingType;
    }

    public String getDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public void setDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse(date, formatter);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id='" + traineeId + "-" + trainerId + "-" + date + '\'' +
                ", traineeId='" + traineeId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", trainingType=" + trainingType +
                ", dateTime=" + date +
                ", duration=" + duration + " min" +
                '}';
    }
}
