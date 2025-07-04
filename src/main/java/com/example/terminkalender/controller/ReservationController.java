package com.example.terminkalender.controller;

import com.example.terminkalender.model.Reservation;
import com.example.terminkalender.model.Participant;
import com.example.terminkalender.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @InitBinder("reservation")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("participants");
    }

    @GetMapping("/create")
    public String showReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "create-reservation";
    }

    @PostMapping("/create")
    public String createReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult result,
                                    @RequestParam(name = "participants", required = false) String participantsString,
                                    Model model) {

        System.out.println("=== ReservationController.createReservation ===");
        System.out.println("Reservation: " + reservation);
        System.out.println("Participants: " + participantsString);
        System.out.println("Has validation errors: " + result.hasErrors());

        if (result.hasErrors()) {
            System.out.println("Validation errors:");
            result.getAllErrors().forEach(error ->
                    System.out.println("- " + error.getDefaultMessage()));
            return "create-reservation";
        }

        if (participantsString == null || participantsString.trim().isEmpty()) {
            model.addAttribute("error", "At least one participant is required.");
            return "create-reservation";
        }

        if (reservation.getFromTime() != null && reservation.getToTime() != null &&
                !reservation.getFromTime().isBefore(reservation.getToTime())) {
            model.addAttribute("error", "End time must be after start time.");
            return "create-reservation";
        }

        try {
            if (!reservationService.isRoomAvailable(reservation.getRoom(), reservation.getDate(),
                    reservation.getFromTime(), reservation.getToTime())) {
                model.addAttribute("error", "Room is not available at the specified time.");
                return "create-reservation";
            }

            Reservation savedReservation = reservationService.saveReservation(reservation, participantsString);

            System.out.println("Successfully saved reservation with ID: " + savedReservation.getId());
            System.out.println("Public key: " + savedReservation.getPublicKey());
            System.out.println("Private key: " + savedReservation.getPrivateKey());

            model.addAttribute("reservation", savedReservation);
            return "confirmation";

        } catch (Exception e) {
            System.err.println("Error saving reservation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to save reservation: " + e.getMessage());
            return "create-reservation";
        }
    }

    @GetMapping("/view/{key}")
    public String viewReservation(@PathVariable String key, Model model) {
        Optional<Reservation> reservation = reservationService.findByPublicKey(key);

        if (reservation.isEmpty()) {
            reservation = reservationService.findByPrivateKey(key);
        }

        if (reservation.isPresent()) {
            model.addAttribute("reservation", reservation.get());
            model.addAttribute("participants", reservation.get().getParticipants());
            model.addAttribute("isPrivateAccess",
                    key.equals(reservation.get().getPrivateKey()));
            return "view-reservation";
        } else {
            model.addAttribute("error", "Reservation not found.");
            return "error";
        }
    }

    @GetMapping("/edit/{privateKey}")
    public String showEditForm(@PathVariable String privateKey, Model model) {
        Optional<Reservation> reservationOpt = reservationService.findByPrivateKey(privateKey);

        if (reservationOpt.isEmpty()) {
            model.addAttribute("error", "Reservation not found or you don't have permission to edit it.");
            return "error";
        }

        Reservation reservation = reservationOpt.get();
        model.addAttribute("reservation", reservation);

        String participantsString = reservation.getParticipants().stream()
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .collect(Collectors.joining("\n"));
        model.addAttribute("participantsString", participantsString);

        return "edit-reservation";
    }

    @PostMapping("/edit/{privateKey}")
    public String updateReservation(@PathVariable String privateKey,
                                    @Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult result,
                                    @RequestParam(name = "participants", required = false) String participantsString,
                                    Model model) {

        System.out.println("=== ReservationController.updateReservation ===");
        System.out.println("Private Key: " + privateKey);
        System.out.println("Updated Reservation: " + reservation);
        System.out.println("Updated Participants: " + participantsString);

        Optional<Reservation> existingReservationOpt = reservationService.findByPrivateKey(privateKey);
        if (existingReservationOpt.isEmpty()) {
            model.addAttribute("error", "Reservation not found or you don't have permission to edit it.");
            return "error";
        }

        Reservation existingReservation = existingReservationOpt.get();

        if (result.hasErrors()) {
            System.out.println("Validation errors:");
            result.getAllErrors().forEach(error ->
                    System.out.println("- " + error.getDefaultMessage()));
            model.addAttribute("participantsString", participantsString);
            return "edit-reservation";
        }

        if (participantsString == null || participantsString.trim().isEmpty()) {
            model.addAttribute("error", "At least one participant is required.");
            model.addAttribute("participantsString", participantsString);
            return "edit-reservation";
        }

        if (reservation.getFromTime() != null && reservation.getToTime() != null &&
                !reservation.getFromTime().isBefore(reservation.getToTime())) {
            model.addAttribute("error", "End time must be after start time.");
            model.addAttribute("participantsString", participantsString);
            return "edit-reservation";
        }

        try {
            if (!reservationService.isRoomAvailableForUpdate(reservation.getRoom(), reservation.getDate(),
                    reservation.getFromTime(), reservation.getToTime(), existingReservation.getId())) {
                model.addAttribute("error", "Room is not available at the specified time.");
                model.addAttribute("participantsString", participantsString);
                return "edit-reservation";
            }

            existingReservation.setDate(reservation.getDate());
            existingReservation.setFromTime(reservation.getFromTime());
            existingReservation.setToTime(reservation.getToTime());
            existingReservation.setRoom(reservation.getRoom());
            existingReservation.setComment(reservation.getComment());

            Reservation updatedReservation = reservationService.updateReservationWithParticipants(existingReservation, participantsString);

            System.out.println("Successfully updated reservation with ID: " + updatedReservation.getId());

            return "redirect:/reservation/view/" + privateKey;

        } catch (Exception e) {
            System.err.println("Error updating reservation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to update reservation: " + e.getMessage());
            model.addAttribute("participantsString", participantsString);
            return "edit-reservation";
        }
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "confirmation";
    }

    @GetMapping("/list")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "list-reservations";
    }
}