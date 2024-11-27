package com.example.sda;
import java.util.*;

public class Customer {
    private static int idCounter = 0;
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int LoyaltyPoints = 0;
    private String tier;
    private Cart cart = new Cart();
    String Address;
    List<String> Preferences = new ArrayList<String>();
    List<orderHistory> purchaseHistory;

    public Customer(String name, String email, String phoneNumber,String password, String Address,List<String> Preferences) {
        this.id = ++idCounter;
        this.name = name;
        this.phone = phoneNumber;
        this.email = email;
        this.password = password;
        this.Address = Address;
        this.Preferences = Preferences;
    }

    public Customer() {
        this.id = ++idCounter;
    }

    public Cart getCart(){
        return cart;
    }


    /*public void setId (int id){
        this.id = id;
    }*/

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
    }


    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setLoyaltyPoints(int LoyaltyPoints){
        this.LoyaltyPoints = LoyaltyPoints;
    }

    public int getLoyaltyPoints(){
        return LoyaltyPoints;
    }

    public void setTier(String tier){
        this.tier = tier;
    }

    public String getTier(){
        return tier;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public List<String> getPreferences() {
        return Preferences;
    }

    public void setPreferences(List<String> Preferences) {
        this.Preferences = Preferences;
    }

    public void update(String newName, String newEmail,String newPhoneNumber, String newPassword, String newAddress, List<String> newPreferences, int newLoyaltyPoints, String newTier){
        this.name = newName;
        this.email = newEmail;
        this.password = newPassword;
        this.Address = newAddress;
        this.phone = newPhoneNumber;
        this.Preferences = newPreferences;
        this.LoyaltyPoints = newLoyaltyPoints;
        this.tier = newTier;
    }

    public void viewProfile(){
        //do implementation rabbani
    }
    
    public void addProductToCart(int product_id,int quantity){
        Product product = csystem.getInstance().getProductById(product_id);
        for (int i = 0; i < cart.getProducts().size(); i++) {
            if (cart.getProducts().get(i).getId() == product_id) {
                cart.getQuantities().set(i, cart.getQuantities().get(i) + quantity);
                return;
            }
        }
        cart.addProduct(product, quantity,0);
    }

    public orderHistory checkOut(){
        Date date = new Date();
        String currentDate = date.toString();
        orderHistory order = new orderHistory(id,currentDate,cart);
        return order;
    }

    public void purchaseItem() {
        if (cart.getProducts().isEmpty()) {
            return;
        }
    
        for (int i = 0; i < cart.getProducts().size(); i++) {
            Product product = cart.getProducts().get(i);
            int quantityToPurchase = cart.getQuantities().get(i);
    
            if (product.getQuantity() < quantityToPurchase) {
                return;
            }
        }
    
        for (int i = 0; i < cart.getProducts().size(); i++) {
            Product product = cart.getProducts().get(i);
            int quantityToPurchase = cart.getQuantities().get(i);
    
            product.setQuantity(product.getQuantity() - quantityToPurchase);
        }
    
        orderHistory order = checkOut();
    }

    public void makePurchase(String orderDate, Cart cart) {
        orderHistory newOrder = new orderHistory(this.id, orderDate, cart);
        addToPurchaseHistory(newOrder);
    }

    public void viewPurchaseHistory() {
        if (hasPurchaseHistory()) {
            getPurchaseHistory();
        } 
    }

    public boolean hasPurchaseHistory() {
        return !purchaseHistory.isEmpty();
    }

    public List<orderHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addToPurchaseHistory(orderHistory orderHistory) {
        purchaseHistory.add(orderHistory);
    }

    public List<orderHistory> getOrdersSortedByDate() {
        Collections.sort(purchaseHistory, new Comparator<orderHistory>() {
            @Override
            public int compare(orderHistory order1, orderHistory order2) {
                return order1.getOrderDate().compareTo(order2.getOrderDate());
            }
        });
        return purchaseHistory;
    }

    /*public orderHistory getOldestOrder() {
        if (purchaseHistory.isEmpty()) {
            return null;
        }

        orderHistory oldestOrder = purchaseHistory.get(0);
        for (orderHistory order : purchaseHistory) {
            if (order.isOlderThan(oldestOrder)) {
                oldestOrder = order;
            }
        }
        return oldestOrder;
    }
*/
    public void clientRequestsOrderHistory() {
        viewPurchaseHistory();  
    }

}