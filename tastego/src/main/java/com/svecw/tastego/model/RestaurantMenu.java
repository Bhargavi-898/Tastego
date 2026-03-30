package com.svecw.tastego.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurant_menus")
public class RestaurantMenu {

    @Id
    private String id;

    private String restaurantName;

    private List<MenuItem> menuItems;

    private String upiImageUrl;

    public RestaurantMenu() {}

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

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getUpiImageUrl() {
        return upiImageUrl;
    }

    public void setUpiImageUrl(String upiImageUrl) {
        this.upiImageUrl = upiImageUrl;
    }

}