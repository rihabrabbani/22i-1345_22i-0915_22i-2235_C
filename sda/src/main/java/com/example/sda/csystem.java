package com.example.sda;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class csystem {
    private static csystem instance;
    private List<Admin> admins = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Customer> allCustomers = new ArrayList<>();
    private List<orderHistory> allOrders = new ArrayList<>();
    private List<Feedback> allFeedback = new ArrayList<>();
    private Customer currentCustomer;
    private Admin currentAdmin;
    
    public dbService Service;
   
    @Autowired
    public csystem(dbService adminService) {
        this.Service = adminService;
        instance = this; // Assign the instance during initialization
        csystem.getInstance().loadAdmins();
        csystem.getInstance().loadCustomers();
        csystem.getInstance().loadProducts();
        csystem.getInstance().loadFeedback();
        csystem.getInstance().loadOrders();
        
    }

    
    void hello(){
        /*
        orderHistory orderHistory = new orderHistory();
        orderHistory.setCustomerId(101);
        orderHistory.setOrderDate("2021-01-12");
        orderHistory.setTotalOrderPrice(500.0);

        Cart cart = new Cart();
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setPrice(100.0);
        product1.setQuantity(10);
        product1.setCategory("Electronics");
        product1.setDescription("This is a product description");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setPrice(200.0);
        product2.setQuantity(5);
        product2.setCategory("Clothing");
        product2.setDescription("This is a product description");

        cart.addProduct(product1, 2,0); // Product 1 with quantity 2
        cart.addProduct(product2, 1,0); // Product 2 with quantity 1

        orderHistory.setCart(cart);

        // DAO call to insert order history
        Service.addOrderHistory(orderHistory);

    */

    }


    public void loadAdmins() {
        admins = Service.getAllAdmin();
    }

    public void loadOrders() {
        allOrders = Service.getAllOrderHistory();
    }

    public void loadProducts() {
        products = Service.getAllProducts();
    }

    public void loadFeedback() {
        allFeedback = Service.getAllFeedback();
    }

    // Static method to get the singleton instance
    public static csystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SystemClass is not initialized yet!");
        }
        return instance;
    }

    public void addFeedback(int productId, String feedback) {
        Feedback newFeedback = new Feedback(currentCustomer.getId(), productId, feedback);
        allFeedback.add(newFeedback);
        Service.addFeedback(newFeedback);
    }


    public List <Feedback> collectFeedback() {
        for (Admin admin : admins) {
            if (admin.getRole().equals("Marketing Manager")) {
                return allFeedback;
            }
        }
        return null;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }


    public List <orderHistory> getAllOrders() {
        return allOrders;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

        // Method to create a new Customer
    public void createCustomer(String name, String email, String phoneNumber, String password, String Address, List<String> Preferences) {
        for (Admin admin : admins) {
            if (admin.getRole().equals("Store Manager")) {
                Customer customer = admin.addCustomer(name, email, phoneNumber, password, Address, Preferences);
                allCustomers.add(customer);
                Service.addCustomerWithPreferences(customer);
            }
        }
    }

    public void loadCustomers() {
        allCustomers = Service.getAllCustomersWithPreferences();
    }

    // Customer login logic
    public boolean customerLogin(String email, String password) {
        for (Customer customer : allCustomers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                currentCustomer = customer;
                return true;
            }
        }
        return false;
    }

    public Customer viewOwnProfile() {
        return currentCustomer;
    }

    public List<orderHistory> trackPurchaseHistory(int customerId) {
        for (Admin admin : admins) {
            if (admin.getRole().equals("Store Manager")) {
                Customer customer = getCustomerById(customerId);
                 return admin.trackPurchaseHistory(customer);
            }
        }
        return null;
    }

    public List<orderHistory> viewAllOrders() {
        return currentCustomer.getPurchaseHistory();
    }

    // Customer can update their own profile
    public void updateOwnProfile(String newName, String newEmail, String newPhoneNumber , String newPassword, String newAddress, List<String> newPreferences, int newLoyaltyPoints, String newTier) {
            for (Admin admin : admins) {
                if (admin.getRole().equals("Store Manager")) {
                    currentCustomer.update(newName , newEmail, newPhoneNumber, newPassword, newAddress, newPreferences, newLoyaltyPoints, newTier);
                    Service.updateCustomer(currentCustomer);
                }
            }        
    }

    public List<Product> generateProductRecommendations(){
            for (Admin admin : admins) {
                if (admin.getRole().equals("Marketting Manager")) {
                    return admin.generateProductRecommendations(currentCustomer);
                }
            }
            return null;
    }

    // Method to create an Admin (Store Manager, Inventory Manager, etc.)
    public void createAdmin(String name, String email, String password, String role) {
        Admin admin = factoryAdmin.createAdmin(name, email, password, role);
        admins.add(admin);
        Service.addAdmin(admin);
    }



    // Updated Admin login logic in csystem
    public Admin adminLogin(String userRole ,String email, String password) {
        for (Admin admin : admins) {
            if (admin.getRole().equals(userRole) && admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                currentAdmin = admin;
                System.out.println("Logged in as " + admin.getRole());
                return admin;  // Return the logged-in Admin object

            }

        }
        return null;  // Return null if credentials are invalid
    }

    public boolean LoyaltyProgram(int cid){
        for (Admin admin : admins) {
            if (admin.getRole().equals("Loyalty Program Manager")) {
                for (Customer customer : allCustomers) {
                    if (customer.getId() == cid) {
                        admin.settingTier(customer);
                        Service.updateCustomer(customer);
                    }
                }
            }
        }
    return false;
    }


    //admin views profile for customer 
    public Customer viewCustomerProfile(int customerId) {
        Customer customer = getCustomerById(customerId);
        
        if (customer != null) {
            for (Admin admin : admins) {
                if (admin.getRole().equals("Store Manager")) {
                    return customer;
                }
            }
        }
        return null; 
    }

    // Method to update a customer's profile by ID in CSystem
    public boolean updateCustomerProfile(int customerId, String name, String email, String phoneNumber, String password, String address, List<String> preferences, int loyaltyPoints, String tier) {
        Customer customer = getCustomerById(customerId);

        if (customer != null) {
            for (Admin admin : admins) {
                if (admin.getRole().equals("Store Manager")) {
                    customer.update(name, email, phoneNumber, password, address, preferences, loyaltyPoints, tier);
                    Service.updateCustomer(customer);
                    return true;
                }
            }
        }
        return false;
    }

    //loyalty manager adds tier for the customer
    public void setTier(int customerId){
        Customer customer = getCustomerById(customerId);
        if (customer != null) {
            for (Admin admin : admins) {
                if (admin.getRole().equals("Loyalty Program Manager")) {
                    admin.settingTier(customer);       
                    Service.updateCustomer(customer);
                }
            }
        }
    }




    // Add product to the system (products list)
    public void addProduct(String productURL, String name, double price, int quantity, String category, String description) {
        for (Admin admin : admins) {
            if (admin.getRole().equals("Inventory Manager")) {
                Product product = admin.addProduct(productURL, name, price, quantity, category,description);
                products.add(product);
                Service.addProduct(product);
            }
        }
        
    }

    // Add product to the system (products list)
    public void updateStock(int pid, int new_quantity) {
        for (Admin admin : admins) {
            if (admin.getRole().equals("Inventory Manager")) {
                Product product = getProductById(pid);
                admin.updateStock(product, new_quantity);
                Service.updateProduct(product);
            }
        }
    }

    Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    // Add product to a customer's cart
    public void addProductToCart(int product_id, int quantity) {
        if (currentCustomer != null) {
            currentCustomer.addProductToCart(product_id, quantity);
            Product product = getProductById(product_id);
            product.setQuantity(product.getQuantity() - quantity);
            Service.updateProduct(product);
        }
    }

    // Customer checkout process
    public orderHistory checkOut() {
        if (currentCustomer != null) {
            orderHistory order = currentCustomer.checkOut();

            if(currentCustomer.getTier()!=null){
                order.setTotalOrderPrice(order.getTotalOrderPrice() * 0.85);

            if(currentCustomer.getTier().equals("Gold")){
                order.setTotalOrderPrice(order.getTotalOrderPrice() * 0.9);
            }
            else if(currentCustomer.getTier().equals("Silver")){
                order.setTotalOrderPrice(order.getTotalOrderPrice() * 0.95);
            }
            else if(currentCustomer.getTier().equals("Bronze")){
                order.setTotalOrderPrice(order.getTotalOrderPrice() * 0.97);
            }
            else{
                order.setTotalOrderPrice(order.getTotalOrderPrice());
            }
        }
            allOrders.add(order);
            currentCustomer.setLoyaltyPoints(currentCustomer.getLoyaltyPoints() + (int)order.getTotalOrderPrice());
            Service.updateCustomer(currentCustomer);
            Service.addOrderHistory(order);
            return order;
        }
        return null;
    }

    public List<orderHistory> manageChurn(){
        for (Admin admin : admins) {
            if (admin.getRole().equals("Marketting Manager")) {
                return admin.monitorchurn();
            }
        }
        return null;
    }

    public List<Feedback> getAllFeedback() {
        return allFeedback;
    }
    //make purchase integration

    

    // Method to get customer by ID in csystem class
    public Customer getCustomerById(int customerId) {
        for (Customer customer : allCustomers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null; // Return null if the customer is not found
    }

    // Method to get the currently logged-in customer in csystem class
    public Customer getLoggedInCustomer() {
        return this.currentCustomer; // Assuming you have a field 'customer' for the logged-in customer
    }

    // Method to get all customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(allCustomers);  // Return a copy of the customer list
    }  

    public List<Product> getAllProducts() {
        return products;
    }
}