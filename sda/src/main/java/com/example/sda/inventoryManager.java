package com.example.sda;
public class inventoryManager extends Admin {

    public inventoryManager(String name, String email,String password,String role) {
        super(name, email, password,role);
    
    } 
    
    @Override
    public Product addProduct(String productURL, String name, double price, int quantity, String category, String description) {
        if (price < 0 || quantity < 0){
            return null;
        }
        Product product = new Product(productURL, name, price, quantity, category,description);
        System.out.println("Product added successfully");
        return product;
    }
    
    @Override
    public void updateStock(Product product, int newQuantity){
        if (newQuantity >= 0){
            product.setQuantity(newQuantity);
        }
        else {
            return;
        }
    }
}