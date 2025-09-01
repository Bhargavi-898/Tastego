package com.svecw.tastego.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menuItems")
public class MenuItem {

    @Id
    private String id;

    private String restaurantId; // Link to specific restaurant
    private String name;
    private double price;
    private String description;
    private String category;
    private String upiImageUrl; // Optional image URL
}
