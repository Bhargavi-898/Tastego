package com.svecw.tastego.controller;

import com.svecw.tastego.model.ScannerImage;
import com.svecw.tastego.repository.ScannerImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/scanner")
@CrossOrigin(origins = "*")
public class ScannerImageController {

    @Autowired
    private ScannerImageRepository scannerImageRepository;

    // =========================
    // UPLOAD SCANNER
    // =========================
    @PostMapping("/upload")
    public ResponseEntity<?> uploadScanner(
            @RequestParam("upiScreenshot") MultipartFile file,
            @RequestParam("restaurantName") String restaurantName) {

        try {
            // ✅ Validation
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File is empty"));
            }

            if (restaurantName == null || restaurantName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Restaurant name required"));
            }

            // ✅ Allow only images
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only image files allowed"));
            }

            // ✅ Create upload folder
            Path uploadPath = Paths.get(
                    System.getProperty("user.dir"),
                    "uploads",
                    "scanners"
            );

            Files.createDirectories(uploadPath);

            // ✅ Generate unique filename
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationPath = uploadPath.resolve(fileName);

            // ✅ Save file to disk
            file.transferTo(destinationPath.toFile());

            // ✅ Save metadata to DB (FIXED HERE)
            ScannerImage scannerImage = new ScannerImage();
            scannerImage.setRestaurantName(restaurantName);  // ✅ FIX
            scannerImage.setScannerType("UPI");              // ✅ FIX
            scannerImage.setFileName(fileName);              // ✅ FIX

            scannerImageRepository.save(scannerImage);

            return ResponseEntity.ok(Map.of(
                    "message", "Upload successful",
                    "fileName", fileName
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // =========================
    // GET IMAGES BY RESTAURANT
    // =========================
    @GetMapping("/{restaurantName}")
    public ResponseEntity<List<ScannerImage>> getScreenshotsByRestaurant(
            @PathVariable String restaurantName) {

        List<ScannerImage> images =
                scannerImageRepository.findByRestaurantName(restaurantName); // ✅ FIX

        return ResponseEntity.ok(images);
    }

    // =========================
    // VIEW IMAGE
    // =========================
    @GetMapping("/view/{fileName}")
    public ResponseEntity<?> viewScannerImage(
            @PathVariable String fileName) {

        try {
            Path filePath = Paths.get(
                    System.getProperty("user.dir"),
                    "uploads",
                    "scanners",
                    fileName
            );

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            contentType != null ? contentType : "image/png"
                    ))
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error loading image");
        }
    }

    // =========================
    // BASE64 IMAGE
    // =========================
    @GetMapping("/base64/{fileName}")
    public ResponseEntity<?> getScannerImageBase64(
            @PathVariable String fileName) {

        try {
            Path filePath = Paths.get(
                    System.getProperty("user.dir"),
                    "uploads",
                    "scanners",
                    fileName
            );

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(filePath);

            String base64 = Base64.getEncoder()
                    .encodeToString(imageBytes);

            return ResponseEntity.ok(
                    Map.of("base64", base64)
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error converting image");
        }
    }
}