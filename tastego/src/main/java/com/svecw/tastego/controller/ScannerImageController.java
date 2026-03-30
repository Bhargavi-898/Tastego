package com.svecw.tastego.controller;

import com.svecw.tastego.model.ScannerImage;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.ScannerImageRepository;
import com.svecw.tastego.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/scanner")
@CrossOrigin(origins = "*")
public class ScannerImageController {

    @Autowired
    private ScannerImageRepository scannerImageRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadUPIScreenshot(
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("upiScreenshot") MultipartFile upiScreenshotFile) {

        try {
            Optional<Restaurant> restaurant = restaurantRepository.findByName(restaurantName);

            if (restaurant.isEmpty()) {
                return ResponseEntity.badRequest().body("Restaurant not found");
            }

            ScannerImage scannerImage = new ScannerImage();
            scannerImage.setName(restaurantName);
            scannerImage.setScanner("UPI");
            scannerImage.setImage(upiScreenshotFile.getBytes());
            scannerImage.setResultText(null);

            scannerImageRepository.save(scannerImage);

            return ResponseEntity.ok("UPI screenshot uploaded successfully for " + restaurantName);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{restaurantName}")
    public ResponseEntity<List<ScannerImage>> getScreenshotsByRestaurant(
            @PathVariable String restaurantName) {

        List<ScannerImage> images = scannerImageRepository.findByName(restaurantName);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/view/{restaurantName}")
    public ResponseEntity<byte[]> viewScannerImage(
            @PathVariable String restaurantName) {

        List<ScannerImage> images = scannerImageRepository.findByName(restaurantName);

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ScannerImage scannerImage = images.get(0);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(scannerImage.getImage());
    }

    @GetMapping("/base64/{restaurantName}")
    public ResponseEntity<Map<String, String>> getScannerImageBase64(
            @PathVariable String restaurantName) {

        List<ScannerImage> images = scannerImageRepository.findByName(restaurantName);

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ScannerImage scannerImage = images.get(0);
        String base64 = Base64.getEncoder().encodeToString(scannerImage.getImage());

        Map<String, String> response = new HashMap<>();
        response.put("base64", base64);

        return ResponseEntity.ok(response);
    }
}