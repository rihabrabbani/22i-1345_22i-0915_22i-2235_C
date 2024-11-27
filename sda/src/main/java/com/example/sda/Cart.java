package com.example.sda;
import java.util.*;

public class Cart {
    private List<Product> products;
    private List<Integer> quantities;
    private double totalPrice;

    // Updated default constructor
    public Cart() {
        this.products = new ArrayList<>(); // Initialize the products list
        this.quantities = new ArrayList<>(); // Initialize the quantities list
        this.totalPrice = 0;
    }

    // Parameterized constructor (if needed)
    public Cart(List<Product> products, List<Integer> quantities, double totalPrice) {
        this.products = products;
        this.quantities = quantities;
        this.totalPrice = totalPrice;
    }

    // Add product to cart
    public void addProduct(Product product, int quantity, double totalPrice) {
        products.add(product);
        quantities.add(quantity);
        totalPrice += product.getPrice() * quantity; 
    }

    // Getters and Setters

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Optional clear method to empty the cart
    public void clear() {
        products.clear();
        quantities.clear();
        totalPrice = 0;
    }
}