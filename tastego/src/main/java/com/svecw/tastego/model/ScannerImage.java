package com.svecw.tastego.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scanner_images")
public class ScannerImage {

    @Id
    private String id;

    private String restaurantName;   // ✅ renamed clearly
    private String scannerType;      // ✅ better naming
    private String fileName;         // ✅ stored file name only

    public ScannerImage() {}

    // ===== Getters & Setters =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getScannerType() {
        return scannerType;
    }

    public void setScannerType(String scannerType) {
        this.scannerType = scannerType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}