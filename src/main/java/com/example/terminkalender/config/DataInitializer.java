package com.example.terminkalender.config;

import com.example.terminkalender.model.Reservation;
import com.example.terminkalender.model.Participant;
import com.example.terminkalender.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (reservationRepository.count() == 0) {
            createTestReservations();
            System.out.println("Test data created successfully.");
        }
    }

    private void createTestReservations() {
        Reservation reservation1 = new Reservation();
        reservation1.setDate(LocalDate.now().plusDays(7));
        reservation1.setFromTime(LocalTime.of(9, 0));
        reservation1.setToTime(LocalTime.of(11, 0));
        reservation1.setRoom("101");
        reservation1.setComment("Important project meeting for the new system");

        reservation1.addParticipant(new Participant("Max", "Mustermann"));
        reservation1.addParticipant(new Participant("Anna", "Schmidt"));

        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDate(LocalDate.now().plusDays(10));
        reservation2.setFromTime(LocalTime.of(14, 0));
        reservation2.setToTime(LocalTime.of(16, 0));
        reservation2.setRoom("102");
        reservation2.setComment("Training session for new employees");

        reservation2.addParticipant(new Participant("John", "Doe"));
        reservation2.addParticipant(new Participant("Jane", "Smith"));

        reservationRepository.save(reservation2);

        System.out.println("Created 2 test reservations with participants.");
    }
}