package com.svecw.tastego.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;
    private LocalDate date;  // Use LocalDate here for consistency
    private String adminEmail;
    private String code;

    // Default constructor
    public Restaurant() {}

    // Parameterized constructor
    public Restaurant(String name, LocalDate date, String adminEmail, String code) {
        this.name = name;
        this.date = date;
        this.adminEmail = adminEmail;
        this.code = code;
    }

    // Getters and Setters
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
}
