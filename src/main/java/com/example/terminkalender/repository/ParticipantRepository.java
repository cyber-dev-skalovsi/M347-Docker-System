package com.example.terminkalender.repository;

import com.example.terminkalender.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByReservationId(Long reservationId);
    void deleteByReservationId(Long reservationId);
}