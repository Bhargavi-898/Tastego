package com.svecw.tastego.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    // Timing fields

    private String openTime;   // example: 12:00
    private String closeTime;  // example: 14:00

    private String name;

    private LocalDate date;

    private String adminEmail;

    private String code;

    // Default constructor

    public Restaurant() {}

    // Parameterized constructor

    public Restaurant(
            String name,
            LocalDate date,
            String adminEmail,
            String code) {

        this.name = name;
        this.date = date;
        this.adminEmail = adminEmail;
        this.code = code;

    }

    // ========================
    // Getters and Setters
    // ========================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // ========================
    // NEW — Timing Getters/Setters
    // ========================

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

}