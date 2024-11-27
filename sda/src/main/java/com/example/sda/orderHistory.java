package com.example.sda;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class orderHistory {
    private static int orderCount = 0;
    private int orderId;
    private int customerId;
    private String orderDate;
    private double totalOrderPrice=0;
    private Cart cart;
    

    public orderHistory(int customerId, String orderDate, Cart cart) {
        this.orderId = ++orderCount;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.cart = new Cart();

        // Deep copy the products list
        this.cart.setProducts(new ArrayList<>(cart.getProducts()));

        // Deep copy the quantities list
        this.cart.setQuantities(new ArrayList<>(cart.getQuantities()));

        // Copy the total price (assumes it's immutable, e.g., `double` or `BigDecimal`)
        this.cart.setTotalPrice(cart.getTotalPrice());
  
        for(int i=0;i<cart.getProducts().size();i++){
            totalOrderPrice+=cart.getProducts().get(i).getPrice()*cart.getQuantities().get(i);
        }
        assignRandomOrderDate();
    }

    public void assignRandomOrderDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(2000, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.now().toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.orderDate = randomDate.format(formatter);
    }

    public orderHistory() {
        this.orderId = ++orderCount;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Cart getCart() {
        return cart;
    }

    // Setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}