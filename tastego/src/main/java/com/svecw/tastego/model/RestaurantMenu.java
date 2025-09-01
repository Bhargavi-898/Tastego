package com.svecw.tastego.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurant_menus")
public class RestaurantMenu {

    @Id
    private String id;

    private String restaurantName;

    private List<MenuItem> menuItems;

    private String upiImageUrl; // URL of the UPI payment image
}
