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
            //facade.createTrainer(trainer);

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

            Optional<Trainee> foundTrainee = facade.findTraineeById("u9");
            
            assertAll("Trainee should be found and have correct data",
                () -> assertTrue(foundTrainee.isPresent(), "Trainee was not found"),
                () -> assertEquals("u9", foundTrainee.get().getId(), "ID mismatch"),
                () -> assertEquals("John", foundTrainee.get().getFirstName(), "First name mismatch"),
                () -> assertEquals("Swift", foundTrainee.get().getLastName(), "Last name mismatch")
            );
        }
    }

    @Test
    @DisplayName("Facade should retrieve the trainer by using an id.")
    void shouldFindTrainerCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainer trainer = new Trainer();
            trainer.setFirstName("Alejandro");
            trainer.setLastName("Pereira");
            trainer.setId("t7");

            facade.createTrainer(trainer);

            Optional<Trainer> foundTrainer = facade.findTrainerById("t7");
            
            assertAll("Trainer should be found and have correct data",
                () -> assertTrue(foundTrainer.isPresent(), "Trainer was not found"),
                () -> assertEquals("t7", foundTrainer.get().getId(), "ID mismatch"),
                () -> assertEquals("Alejandro", foundTrainer.get().getFirstName(), "First name mismatch"),
                () -> assertEquals("Pereira", foundTrainer.get().getLastName(), "Last name mismatch")
            );
        }
    }

    @Test
    @DisplayName("Facade should retrieve the training by using an id.")
    void shouldFindTrainingCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainer trainer = new Trainer();
            trainer.setFirstName("Luis");
            trainer.setLastName("Gomez");
            trainer.setId("t5");

            facade.createTrainer(trainer);

            Trainee trainee = new Trainee();
            trainee.setFirstName("Daniela");
            trainee.setLastName("Suazez");
            trainee.setId("u4");

            facade.createTrainee(trainee);

            Training training = new Training();
            training.setTrainerId("t5");
            training.setTraineeId("u4");
            training.setDate("2025-11-29 20:00:00");
            training.setDuration(120);

            facade.createTraining(training);

            Optional<Training> foundTraining = facade.findTrainingById("u4-t5-2025-11-29 20:00:00");
            
            assertAll("Training should be found and have correct data",
                () -> assertTrue(foundTraining.isPresent(), "Training was not found"),
                () -> assertEquals("u4", foundTraining.get().getTraineeId(), "trainee ID mismatch"),
                () -> assertEquals("t5", foundTraining.get().getTrainerId(), "trainer ID mismatch"),
                () -> assertEquals("2025-11-29 20:00:00", foundTraining.get().getDate(), "date mismatch")
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

            Optional<Trainee> savedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("Lisa.Paz"))
                    .findFirst();
            
            assertTrue(savedTrainee.isPresent(), "Trainee was not saved");

            Trainee saved = savedTrainee.get();
            saved.setAddress("street 8th 5 - 43");

            facade.updateTrainee(saved);

            Optional<Trainee> updatedTrainee = facade.findAllTrainees()
                    .stream()
                    .filter(t -> t.getUsername().equals("Lisa.Paz"))
                    .findFirst();
            
            assertAll("Trainee should be updated correctly",
                () -> assertEquals("street 8th 5 - 43",
                        updatedTrainee.get().getAddress(),
                        "Address was not updated"),
                () -> assertEquals("Lisa",
                        updatedTrainee.get().getFirstName(),
                        "First name should remain unchanged"),
                () -> assertEquals("Paz",
                        updatedTrainee.get().getLastName(),
                        "Last name should remain unchanged")
            );
        }
    }

    @Test
    @DisplayName("Facade should update Trainer correctly")
    void shouldUpdateTrainerCorrectly() {
        try (ConfigurableApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymServices facade = context.getBean(GymServices.class);

            Trainer trainer = new Trainer();
            trainer.setFirstName("Julian");
            trainer.setLastName("Sopo");

            facade.createTrainer(trainer);

            Optional<Trainer> savedTrainer = facade.findAllTrainers()
                    .stream()
                    .filter(t -> t.getUsername().equals("Julian.Sopo"))
                    .findFirst();
            
            assertTrue(savedTrainer.isPresent(), "Trainer was not saved");

            Trainer saved = savedTrainer.get();
            saved.setSpecialization("Cross-fit");

            facade.updateTrainer(saved);

            Optional<Trainer> updatedTrainer = facade.findAllTrainers()
                    .stream()
                    .filter(t -> t.getUsername().equals("Julian.Sopo"))
                    .findFirst();
            
            assertAll("Trainer should be updated correctly",
                () -> assertEquals("Cross-fit",
                        updatedTrainer.get().getSpecialization(),
                        "Specialization was not updated"),
                () -> assertEquals("Julian",
                        updatedTrainer.get().getFirstName(),
                        "First name should remain unchanged"),
                () -> assertEquals("Sopo",
                        updatedTrainer.get().getLastName(),
                        "Last name should remain unchanged")
            );
        }
    }
}
