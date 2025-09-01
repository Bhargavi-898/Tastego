package com.svecw.tastego.controller;

import com.svecw.tastego.model.ScannerImage;
import com.svecw.tastego.model.Restaurant;
import com.svecw.tastego.repository.ScannerImageRepository;
import com.svecw.tastego.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Upload UPI screenshot using only restaurant name
    @PostMapping("/upload")
    public ResponseEntity<String> uploadUPIScreenshot(
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("upiScreenshot") MultipartFile upiScreenshotFile) {

        try {
            // Check if restaurant exists
            Restaurant restaurant = restaurantRepository.findByName(restaurantName);
            if (restaurant == null) {
                return ResponseEntity.badRequest().body("Restaurant not found");
            }

            // Save ScannerImage entity
            ScannerImage scannerImage = ScannerImage.builder()
                    .scanner("UPI") // fixed value
                    .name(restaurantName) // store restaurant name
                    .image(upiScreenshotFile.getBytes())
                    .resultText(null)
                    .build();

            scannerImageRepository.save(scannerImage);

            return ResponseEntity.ok("UPI screenshot uploaded successfully for " + restaurantName);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    // View all screenshots uploaded for a restaurant
    @GetMapping("/{restaurantName}")
    public ResponseEntity<List<ScannerImage>> getScreenshotsByRestaurant(@PathVariable String restaurantName) {
        List<ScannerImage> images = scannerImageRepository.findByName(restaurantName);
        return ResponseEntity.ok(images);
    }

    // View first screenshot as byte[]
    @GetMapping("/view/{restaurantName}")
    public ResponseEntity<byte[]> viewScannerImage(@PathVariable String restaurantName) {
        ScannerImage scannerImage = scannerImageRepository.findByName(restaurantName)
                .stream().findFirst().orElse(null);

        if (scannerImage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(scannerImage.getImage());
    }

    // Return Base64 of first screenshot
    @GetMapping("/base64/{restaurantName}")
    public ResponseEntity<Map<String, String>> getScannerImageBase64(@PathVariable String restaurantName) {
        ScannerImage scannerImage = scannerImageRepository.findByName(restaurantName)
                .stream().findFirst().orElse(null);

        if (scannerImage == null) {
            return ResponseEntity.notFound().build();
        }

        String base64 = Base64.getEncoder().encodeToString(scannerImage.getImage());
        Map<String, String> response = new HashMap<>();
        response.put("base64", base64);
        return ResponseEntity.ok(response);
    }
}
