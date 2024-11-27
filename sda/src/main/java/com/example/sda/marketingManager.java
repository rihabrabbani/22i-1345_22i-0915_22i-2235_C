package com.example.sda;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class marketingManager extends Admin {
    public marketingManager(String name, String email, String password, String role) {
        super(name, email, password, role);
    }

    @Override
    public List<Product> generateProductRecommendations(Customer customer) {

        if (customer == null) {
            return new ArrayList<>();
        }

        List<String> preferences = customer.getPreferences();
        
        if (preferences == null || preferences.isEmpty()) {
            return new ArrayList<>(); 
        }

        List<Product> recommendedProducts = new ArrayList<>();
        
        List<Product> allProducts = csystem.getInstance().getAllProducts();

        for (Product product : allProducts) {
            for (String preference : preferences) {
                if (product.getCategory().equalsIgnoreCase(preference)) {
                    recommendedProducts.add(product);  
                    if (recommendedProducts.size() == 3) {
                        return recommendedProducts;
                    }
                    break;  
                }
            }
        }
        return recommendedProducts;
    }

    @Override
    public List<orderHistory> monitorchurn() {
        List<orderHistory> allOrders = csystem.getInstance().getAllOrders();
            
        // Ensure the list is not empty.
        if (allOrders.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Sort the orders by order date in ascending order.
        allOrders.sort(Comparator.comparing(orderHistory::getOrderDate));
        
        // Return the first five (or fewer) oldest orders.
        return allOrders.subList(0, Math.min(5, allOrders.size()));
        
    }

    @Override
    public List<Feedback> collectFeedback(List<Feedback> feedbackList) {
        if (feedbackList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return feedbackList;
        }
    }
}