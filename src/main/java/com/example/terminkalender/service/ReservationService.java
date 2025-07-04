package com.example.terminkalender.service;

import com.example.terminkalender.model.Reservation;
import com.example.terminkalender.model.Participant;
import com.example.terminkalender.repository.ReservationRepository;
import com.example.terminkalender.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ParticipantRepository participantRepository) {
        this.reservationRepository = reservationRepository;
        this.participantRepository = participantRepository;
    }

    public boolean isRoomAvailable(String room, LocalDate date, LocalTime fromTime, LocalTime toTime) {
        return !reservationRepository.isRoomOccupied(room, date, fromTime, toTime);
    }

    public boolean isRoomAvailableForUpdate(String room, LocalDate date, LocalTime fromTime, LocalTime toTime, Long excludeReservationId) {
        return !reservationRepository.isRoomOccupiedExcludingReservation(room, date, fromTime, toTime, excludeReservationId);
    }

    @Transactional
    public Reservation saveReservation(Reservation reservation, String participantNames) {
        System.out.println("=== ReservationService.saveReservation ===");
        System.out.println("Input reservation: " + reservation);
        System.out.println("Input participants: " + participantNames);

        if (!isRoomAvailable(reservation.getRoom(), reservation.getDate(),
                reservation.getFromTime(), reservation.getToTime())) {
            throw new IllegalStateException("Room is not available at the specified time");
        }

        reservation.getParticipants().clear();

        List<Participant> participants = parseParticipants(participantNames);

        for (Participant participant : participants) {
            reservation.addParticipant(participant);
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        System.out.println("Saved reservation: " + savedReservation);
        System.out.println("Saved participants count: " + savedReservation.getParticipants().size());

        return savedReservation;
    }

    @Transactional
    public Reservation updateReservationWithParticipants(Reservation reservation, String participantNames) {
        System.out.println("=== ReservationService.updateReservationWithParticipants ===");
        System.out.println("Updating reservation: " + reservation);
        System.out.println("New participants: " + participantNames);

        reservation.getParticipants().clear();

        List<Participant> participants = parseParticipants(participantNames);

        for (Participant participant : participants) {
            reservation.addParticipant(participant);
        }

        Reservation updatedReservation = reservationRepository.save(reservation);

        System.out.println("Updated reservation: " + updatedReservation);
        System.out.println("Updated participants count: " + updatedReservation.getParticipants().size());

        return updatedReservation;
    }

    private List<Participant> parseParticipants(String participantNames) {
        List<Participant> participants = new ArrayList<>();

        if (participantNames == null || participantNames.trim().isEmpty()) {
            return participants;
        }

        String[] names = participantNames.split("[,;\\n\\r]+");

        for (String name : names) {
            name = name.trim();
            if (!name.isEmpty()) {
                String[] nameParts = name.split("\\s+");

                if (nameParts.length >= 2) {
                    String firstName = nameParts[0].trim();
                    String lastName = String.join(" ",
                            java.util.Arrays.copyOfRange(nameParts, 1, nameParts.length)).trim();

                    firstName = cleanName(firstName);
                    lastName = cleanName(lastName);

                    if (!firstName.isEmpty() && !lastName.isEmpty()) {
                        participants.add(new Participant(firstName, lastName));
                        System.out.println("Parsed participant: " + firstName + " " + lastName);
                    }
                } else if (nameParts.length == 1) {
                    String firstName = cleanName(nameParts[0]);
                    if (!firstName.isEmpty()) {
                        participants.add(new Participant(firstName, "Unknown"));
                        System.out.println("Parsed participant: " + firstName + " Unknown");
                    }
                }
            }
        }

        return participants;
    }

    private String cleanName(String name) {
        if (name == null) return "";
        return name.replaceAll("[^a-zA-ZÀ-ÿ\\s'-]", "").trim();
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findByPrivateKey(String privateKey) {
        return reservationRepository.findByPrivateKey(privateKey);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findByPublicKey(String publicKey) {
        return reservationRepository.findByPublicKey(publicKey);
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}