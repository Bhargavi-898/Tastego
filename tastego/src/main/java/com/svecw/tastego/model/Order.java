package com.svecw.tastego.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String studentId;
    private String restaurantId;
    private String menuItemId;
    private int quantity;
    private double totalAmount;
    private String paymentScreenshot;
    private String status;
    private String tokenNumber;
    private LocalDate date; // Correct field
    private LocalDateTime orderTime;
    private boolean acknowledged;

    public Order() {}

    // Getters and Setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }
    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentScreenshot() {
        return paymentScreenshot;
    }
    public void setPaymentScreenshot(String paymentScreenshot) {
        this.paymentScreenshot = paymentScreenshot;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }
    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
}
