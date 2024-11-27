package com.example.sda;
public class factoryAdmin {

    public static Admin createAdmin(String name, String email, String password , String role) {
        if (role.equals("Marketting Manager")) {
            return new marketingManager(name, email, password, role);
        } else if (role.equals("Inventory Manager")) {
            return new inventoryManager(name, email, password, role);
        } else if (role.equals("Store Manager")) {
            return new storeManager(name, email, password, role);
        }
        else if (role.equals("Loyalty Program Manager")) {
            return new loyaltyManager(name, email, password, role);
        }
        else {
            return null;
        }
    }
    
}