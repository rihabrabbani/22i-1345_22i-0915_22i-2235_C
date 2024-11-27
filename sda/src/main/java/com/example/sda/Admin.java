package com.example.sda;

import java.util.*;

public abstract class Admin {
    private static int idCounter = 0;
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;

    public Admin(String name, String email, String password, String role) {
        this.id = ++idCounter;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Customer addCustomer(String name, String email, String phoneNumber, String password, String Address, List<String> Preferences) {
        System.out.println("You do not have permission to add a customer");
        return null;
    }

    public void updateProfile(Customer customer, String name, String email, String phoneNumber, String password, String address, List<String> preferences) {
        //nothing i guess
    }

    public boolean settingTier(Customer customer){
        return false;
        //nothing i guess
    }

    public Customer viewProfile(Customer customer){
        return null;
    }

    public Product addProduct(String productURL, String name, double price, int quantity, String category, String description) {
        System.out.println("You do not have permission to add a product");
        return null;
    }

    public void updateStock(Product product, int newQuantity){
        //nothing
    }

    public List<Product> generateProductRecommendations(Customer customer){
        return new ArrayList<>();
    }

    public List<orderHistory> monitorchurn(){
        return null;
    }

    public List <orderHistory> trackPurchaseHistory(Customer customer){
        return new ArrayList<>();
    }

    public List<Feedback> collectFeedback(List<Feedback> feedbackList){
        return new ArrayList<>();
    }
}