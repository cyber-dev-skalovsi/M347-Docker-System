package com.example.terminkalender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/access")
    public String accessReservation(@RequestParam String key, Model model) {
        if (key == null || key.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a valid key.");
            return "index";
        }
        return "redirect:/reservation/view/" + key.trim();
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}