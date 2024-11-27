package com.example.sda;
public class loyaltyManager extends Admin {    
    public loyaltyManager(String name, String email,String password,String role) {
        super(name, email, password,role);
    }
    
    @Override
    public boolean settingTier(Customer customer){
        if (customer.getLoyaltyPoints() >= 1000){
            customer.setTier("Gold");
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - 1000);
        }
        else if (customer.getLoyaltyPoints() >= 500){
            customer.setTier("Silver");
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - 500);
        }
        else if (customer.getLoyaltyPoints() >= 100){
            customer.setTier("Bronze");
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - 100);
        }
        else{
            return false;
        }
        return true;
    }
}