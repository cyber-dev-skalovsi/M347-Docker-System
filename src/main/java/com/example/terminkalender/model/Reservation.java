package com.example.terminkalender.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "From time is required")
    private LocalTime fromTime;

    @NotNull(message = "To time is required")
    private LocalTime toTime;

    @NotBlank(message = "Room is required")
    private String room;

    @NotBlank(message = "Comment is required")
    private String comment;

    @Column(unique = true, nullable = false)
    private String publicKey;

    @Column(unique = true, nullable = false)
    private String privateKey;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @PrePersist
    public void generateKeys() {
        if (this.publicKey == null || this.publicKey.isEmpty()) {
            this.publicKey = UUID.randomUUID().toString();
        }
        if (this.privateKey == null || this.privateKey.isEmpty()) {
            this.privateKey = UUID.randomUUID().toString();
        }
    }

    // Helper method to add participant
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setReservation(this);
    }

    // Helper method to remove participant
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setReservation(null);
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getFromTime() { return fromTime; }
    public void setFromTime(LocalTime fromTime) { this.fromTime = fromTime; }

    public LocalTime getToTime() { return toTime; }
    public void setToTime(LocalTime toTime) { this.toTime = toTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    public String getPrivateKey() { return privateKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }

    public List<Participant> getParticipants() { return participants; }
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
        // Ensure bidirectional relationship
        if (participants != null) {
            participants.forEach(p -> p.setReservation(this));
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", room='" + room + '\'' +
                ", comment='" + comment + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", participantCount=" + (participants != null ? participants.size() : 0) +
                '}';
    }
}