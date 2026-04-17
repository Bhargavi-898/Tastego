package com.svecw.tastego.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "TasteGo Backend is Live 🚀";
    }
}