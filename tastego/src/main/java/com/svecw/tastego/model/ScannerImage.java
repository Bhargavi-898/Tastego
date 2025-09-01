package com.svecw.tastego.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scanner_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScannerImage {

    @Id
    private String id;

    private byte[] image;

    private String name;

    private String scanner;

    private String resultText;
}
