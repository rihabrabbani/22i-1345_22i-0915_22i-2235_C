package com.example.sda;
import java.util.*;

public class storeManager extends Admin {
    public storeManager(String name, String email,String password,String role) {
        super(name, email, password,role);
    }
    
    @Override
    public Customer addCustomer(String name, String email, String phoneNumber , String password, String Address, List<String> Preferences) {
        if (doesCustomerExist(email)) {
            System.out.println("A customer with this email already exists. Please choose a different email or modify details.");
            return null;  
        }
        
        Customer customer = new Customer(name, email, phoneNumber , password, Address,Preferences );
        System.out.println("Customer added successfully");
        return customer;
    }

    @Override
    public Customer viewProfile(Customer customer){
        return customer;
    }

    @Override
    public void updateProfile(Customer customer, String name, String phoneNumber,String email, String password, String address, List<String> preferences) {
        if (customer != null) {
            if (name != null && !name.isEmpty()) {
                customer.setName(name);
            }
            if (email != null && !email.isEmpty()) {
                customer.setEmail(email);
            }

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                customer.setPhone(phoneNumber);
            }

            if (password != null && !password.isEmpty()) {
                customer.setPassword(password);
            }
            if (address != null && !address.isEmpty()) {
                customer.setAddress(address);
            }
            if (preferences != null && !preferences.isEmpty()) {
                customer.setPreferences(preferences);
            }
        } 
    }

    @Override
    public List<orderHistory> trackPurchaseHistory(Customer customer){
            List<orderHistory> customerOrders = new ArrayList<>();
            for (orderHistory order : csystem.getInstance().getAllOrders()) {
                if (order.getCustomerId() == customer.getId()) {
                    customerOrders.add(order);
                }
            }
            return customerOrders;
    }

    //helper ftn
    private boolean doesCustomerExist(String email) {
        for (Customer existingCustomer : csystem.getInstance().getAllCustomers()) {
            if (existingCustomer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}