package com.gym.system;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gym.system.Storage.PasswordGenerator;
import com.gym.system.service.GymServices;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    @DisplayName("Storage should be initialized with the sample data from the JSON file")
    public void StorageInitializedCorreclty()
    {
        try (ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymServices facade = applicationContext.getBean(GymServices.class);

            var trainees = facade.findAllTrainees();
            var trainers = facade.findAllTrainers();
            var trainings = facade.findAllTrainings();

            assertAll("Storage should be initialized with sample data",
                () -> assertFalse(trainees.isEmpty(), "Trainees list is empty"),
                () -> assertFalse(trainers.isEmpty(), "Trainers list is empty"),
                () -> assertFalse(trainings.isEmpty(), "Trainings list is empty")
            );
        }
    }

    @Test
    @DisplayName("Facade should create Trainee correctly with username and password")
    void shouldCreateTraineeCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainee trainee = new Trainee();
            trainee.setFirstName("Lucas");
            trainee.setLastName("Diaz");

            facade.createTrainee(trainee);

            var savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getFirstName().equals("Lucas"))
                    .findFirst();
            
            System.out.println("Loaded trainees:-------------");
            facade.findAllTrainees().forEach(System.out::println);
            
            assertAll("New trainee should be created correctly",
                () -> assertTrue(savedTrainee.isPresent(), "Trainee was not saved"),
                () -> assertEquals("Lucas.Diaz", savedTrainee.get().getUsername()),
                () -> assertTrue(savedTrainee.get().iSActive(), "IsActive should be true"),
                () -> assertNotNull(savedTrainee.get().getId(), "Id should be generated"),
                () -> assertNotNull(savedTrainee.get().getPassword(), "Password should be generated"),
                () -> assertEquals(10, savedTrainee.get().getPassword().length(),
                        "Password length must be 10")
            );
        }
    }

    @Test
    @DisplayName("Facade should create Trainer correctly with username and password")
    void shouldCreateTrainerCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainer trainer = new Trainer();
            trainer.setFirstName("Fernando");
            trainer.setLastName("Rosales");

            facade.createTrainer(trainer);

            var savedTrainer = facade.findAllTrainers()
                    .stream()
                    .filter(t -> t.getFirstName().equals("Fernando"))
                    .findFirst();
            
            System.out.println("Loaded trainers:-------------");
            facade.findAllTrainers().forEach(System.out::println);
            
            assertAll("New trainer should be created correctly",
                () -> assertTrue(savedTrainer.isPresent(), "Trainer was not saved"),
                () -> assertEquals("Fernando.Rosales", savedTrainer.get().getUsername()),
                () -> assertTrue(savedTrainer.get().iSActive(), "IsActive should be true"),
                () -> assertNotNull(savedTrainer.get().getId(), "Id should be generated"),
                () -> assertNotNull(savedTrainer.get().getPassword(), "Password should be generated"),
                () -> assertEquals(10, savedTrainer.get().getPassword().length(),
                        "Password length must be 10")
            );
        }
    }

    @Test
    @DisplayName("Facade should create Training correctly")
    void shouldCreateTrainingCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);
            Map<String, TrainingType> trainingTypes = context.getBean("trainingTypeStorage", Map.class);

            Trainer trainer = new Trainer();
            trainer.setFirstName("Fernando");
            trainer.setLastName("Rosales");
            facade.createTrainer(trainer);

            Trainee trainee = new Trainee();
            trainee.setFirstName("Lucas");
            trainee.setLastName("Diaz");
            facade.createTrainee(trainee);

            TrainingType type = new TrainingType();
            type.setName("volumen - masa muscular");
            trainingTypes.put(type.getName(), type);

            var savedTrainer = facade.findAllTrainers()
                    .stream()
                    .filter(t -> t.getFirstName().equals("Fernando"))
                    .findFirst();
            
            var savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getFirstName().equals("Lucas"))
                    .findFirst();        

            Training training = new Training();
            training.setTrainerId(savedTrainer.get().getId());
            training.setTraineeId(savedTrainee.get().getId());
            training.setTrainingName("training volumen - Lucas");
            training.setTrainingType(trainingTypes.get("volumen - masa muscular"));
            training.setDate("2025-11-26 10:00:00");
            training.setDuration(60);

            facade.createTraining(training);

            var savedTraining = facade.findAllTrainings()
                    .stream()
                    .filter(t -> t.getTrainingName().equals("training volumen - Lucas"))
                    .findFirst();
            
            System.out.println("Loaded trainings:-------------");
            facade.findAllTrainings().forEach(System.out::println);
            
            assertAll("New training should be created correctly",
                () -> assertTrue(savedTraining.isPresent(), "Training was not saved"),
                () -> assertEquals(60, savedTraining.get().getDuration()),
                () -> assertEquals("2025-11-26 10:00:00", savedTraining.get().getDate()),
                () -> assertEquals("volumen - masa muscular", savedTraining.get().getTrainingType().getName())
            );
        }
    }
    
    @Test
    @DisplayName("Username generator should append serial number when duplicates exist")
    void shouldGenerateUniqueUsernames() {

        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainer t1 = new Trainer();
            t1.setFirstName("Maria");
            t1.setLastName("Diaz");

            Trainer t2 = new Trainer();
            t2.setFirstName("Maria");
            t2.setLastName("Diaz");

            Trainer t3 = new Trainer();
            t3.setFirstName("Maria");
            t3.setLastName("Diaz");

            facade.createTrainer(t1);
            facade.createTrainer(t2);
            facade.createTrainer(t3);

            assertAll("Usernames must be unique",
                    () -> assertEquals("Maria.Diaz", t1.getUsername()),
                    () -> assertEquals("Maria.Diaz1", t2.getUsername()),
                    () -> assertEquals("Maria.Diaz2", t3.getUsername())
            );
        }
    }
    
    @Test
    @DisplayName("PasswordGenerator should create 10-char random passwords")
    void shouldGenerateValidPassword() {

        String p1 = PasswordGenerator.generate();
        String p2 = PasswordGenerator.generate();

        assertAll("Password properties",
                () -> assertEquals(10, p1.length()),
                () -> assertEquals(10, p2.length()),
                () -> assertNotEquals(p1, p2, "Two generated passwords should be different")
        );
    }

    @Test
    @DisplayName("Delete a trainee should remove the record from the Trainees storage")
    void deleteTrainee_ShouldRemoveExistingTrainee() {
        try (ConfigurableApplicationContext context =
                    new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainee trainee = new Trainee();
            trainee.setFirstName("Isa");
            trainee.setLastName("Romero");
            facade.createTrainee(trainee);

            var savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getFirstName().equals("Isa"))
                    .findFirst();  

            // Act
            facade.deleteTrainee(savedTrainee.get().getId());

            var afterDeletion = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("Isa.Romero"))
                    .findFirst();  

            // Assert
            assertTrue(afterDeletion.isEmpty(), "Trainee should be deleted");
        }
    }

    @Test
    @DisplayName("Facade should retrieve the trainee by using an id.")
    void shouldFindTraineeCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainee trainee = new Trainee();
            trainee.setFirstName("John");
            trainee.setLastName("Swift");
            trainee.setId("u9");

            facade.createTrainee(trainee);

            var savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("John.Swift"))
                    .findFirst();

            Optional<Trainee> foundTrainee = facade.findTraineeById("u9");
            
            assertAll("the trainee should be founded correctly",
                () -> assertTrue(savedTrainee.isPresent(), "Trainee was not saved"),
                () -> assertFalse(foundTrainee.isEmpty(), "the trainee was not found")
            );
        }
    }

    @Test
    @DisplayName("Facade should update Trainee correctly")
    void shouldUpdateTraineeCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainee trainee = new Trainee();
            trainee.setFirstName("Lisa");
            trainee.setLastName("Paz");

            facade.createTrainee(trainee);

            var savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("Lisa.Paz"))
                    .findFirst();

            savedTrainee.get().setAddress("street 8th 5 - 43");
            facade.updateTrainee(savedTrainee.get());

            var updatedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("Lisa.Paz"))
                    .findFirst();
            
            assertAll("Trainee should be updated correctly",
                () -> assertTrue(savedTrainee.isPresent(), "Trainee was not saved"),
                () -> assertEquals("street 8th 5 - 43", updatedTrainee.get().getAddress(),
                        "Address is not updated")
            );
        }
    }
}
