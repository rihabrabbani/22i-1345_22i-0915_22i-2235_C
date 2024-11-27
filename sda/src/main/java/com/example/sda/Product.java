package com.example.sda;
public class Product {
    private static int idCounter = 0;
    private String productURL;
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String category;
    private String description;

    // Constructor
    public Product(String productURL, String name, double price, int quantity, String category, String description) {
        this.productURL = productURL;
        this.id = ++idCounter;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
    }

    Product(){

    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters
    public String getProductURL() {
        return productURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}