package com.example.terminkalender.repository;

import com.example.terminkalender.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByPrivateKey(String privateKey);
    Optional<Reservation> findByPublicKey(String publicKey);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.room = :room AND r.date = :date AND " +
            "((r.fromTime <= :fromTime AND r.toTime > :fromTime) OR " +
            "(r.fromTime < :toTime AND r.toTime >= :toTime) OR " +
            "(r.fromTime >= :fromTime AND r.toTime <= :toTime))")
    boolean isRoomOccupied(@Param("room") String room,
                           @Param("date") LocalDate date,
                           @Param("fromTime") LocalTime fromTime,
                           @Param("toTime") LocalTime toTime);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.room = :room AND r.date = :date AND r.id != :excludeId AND " +
            "((r.fromTime <= :fromTime AND r.toTime > :fromTime) OR " +
            "(r.fromTime < :toTime AND r.toTime >= :toTime) OR " +
            "(r.fromTime >= :fromTime AND r.toTime <= :toTime))")
    boolean isRoomOccupiedExcludingReservation(@Param("room") String room,
                                               @Param("date") LocalDate date,
                                               @Param("fromTime") LocalTime fromTime,
                                               @Param("toTime") LocalTime toTime,
                                               @Param("excludeId") Long excludeId);
}