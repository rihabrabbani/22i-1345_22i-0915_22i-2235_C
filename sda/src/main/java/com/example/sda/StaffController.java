package com.example.sda;

import java.util.*;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class StaffController {
        
    @GetMapping("/")
    public String showLandingPage() {
        return "landing"; // Refers to Signup.html
    }

    @GetMapping("/staffPortal")
    public String showSignUpPage() {
        return "staffPortal"; // Refers to Signup.html
    }

    @GetMapping("/customer_signin")
    public String showCustomerSignInPage() {
        return "customer_signin"; // Refers to Signup.html
    }
    
    @GetMapping("/create_customer")
    public String showCreateCustomerPage() {
        return "customer_signup"; // Refers to Signup.html
    }
    
    @PostMapping("/customer_signin")
    public String handleCustomerSignIn(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        // Process customer sign-in data
        boolean flag = csystem.getInstance().customerLogin(email, password);
        if (flag) {
            return "redirect:/index"; // Redirect to the home page
        }
        else{
            return "customer_signin"; // Stay on the sign-in page
        }

    }

    @PostMapping("/customer_signup")
    public String handleCustomerSignUp(@RequestParam("name") String name,
                                       @RequestParam("password") String password,
                                       @RequestParam("email") String email,
                                       @RequestParam("Phone Number") String phoneNumber,
                                       @RequestParam("Address") String address,
                                       @RequestParam("preferences") List<String> preferences,
                                       Model model) {
        // Process customer sign-up data
        csystem.getInstance().createCustomer(name, email, phoneNumber, password, address, preferences);

        return "redirect:/create_customer"; // Redirect to a success page
    }
    
    @GetMapping("/index")
    public String showIndexPage(Model model) {
        List<Product> recommendedProducts = csystem.getInstance().generateProductRecommendations();
        if(recommendedProducts==null){
            model.addAttribute("Products", csystem.getInstance().getAllProducts());
            return "index";
        }
        model.addAttribute("recommendedProducts", recommendedProducts);
        model.addAttribute("Products", csystem.getInstance().getAllProducts());
        return "index";
    }

    @GetMapping("/log_out")
        public String logOut(){

            return "landing";
        }
    

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") String productId,
                            @RequestParam("quantity") String quantity) {
        // Handle logic to add the product to the cart
        
        csystem.getInstance().addProductToCart(Integer.parseInt(productId), Integer.parseInt(quantity));

        return "redirect:/index"; // Redirect to the home page
    }


    @GetMapping("/check_out")
    public String showCheckOutPage(Model model) {
       orderHistory ord =csystem.getInstance().checkOut();

    List<Map<String, Object>> items = new ArrayList<>();
    for (int i = 0; i < ord.getCart().getProducts().size(); i++) {
        Map<String, Object> item = new HashMap<>();
        Product product = ord.getCart().getProducts().get(i);
        int quantity = ord.getCart().getQuantities().get(i);
        item.put("productId", product.getId());
        item.put("productName", product.getName());
        item.put("category", product.getCategory());
        item.put("unitPrice", product.getPrice());
        item.put("quantity", quantity);
        item.put("totalPrice", product.getPrice() * quantity);
        items.add(item);
    }



    model.addAttribute("order", ord);
    model.addAttribute("items", items);
        
        return "check_out";
    }

    @PostMapping("/submit-checkout")
    public String postMethodName() {
        // Handle logic to process the order
        csystem.getInstance().getCurrentCustomer().getCart().clear();
        return "redirect:/index";
    }

    @GetMapping("/edit_profile")
    public String showEditProfilePage(Model model) {
        Customer cust = csystem.getInstance().getCurrentCustomer();
        model.addAttribute("user", cust);
        return "edit_profile";
    }

    @PostMapping("/update_profile")
        public String updateProfile(@RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("address") String Address){
        // Handle logic to update the customer profile

        csystem.getInstance().updateOwnProfile(name, email, phone, csystem.getInstance().getCurrentCustomer().getPassword() , Address , 
        csystem.getInstance().getCurrentCustomer().getPreferences(), csystem.getInstance().getCurrentCustomer().getLoyaltyPoints(), csystem.getInstance().getCurrentCustomer().getTier());
        System.out.println("Profile updated successfully");
        return "redirect:/index";
    }

    @GetMapping("/product_review")
    public String showProductReviewPage(Model model) {
        List<Product> products = csystem.getInstance().getAllProducts();
        // Add the product data to the model
        model.addAttribute("products", products);
        return "product_review";
    }

    @PostMapping("/submit-review")
    public String submitReview(@RequestParam("productId") String productId,
                               @RequestParam("feedback") String feedback) {
        // Handle logic to submit the product review
        csystem.getInstance().addFeedback(Integer.parseInt(productId), feedback);
        return "redirect:/product_review";
    }


    @GetMapping("/purchase_history")
    public String showPurchaseHistoryPage() {
        return "purchase_history";
    }

    @PostMapping("/stafflogin")
    public String handleLogin(@RequestParam("userRole") String userRole, @RequestParam("email") String email, @RequestParam("password") String password, Model model) {
            Admin adm = csystem.getInstance().adminLogin(userRole,email, password);
            if (adm == null) {
                return "staffPortal";   
            }
            model.addAttribute("userRole", adm.getRole());
            return "home";
    }

    @PostMapping("/staffsignup")
    public String handleSignUp(@RequestParam("userRole") String userRole, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
        csystem.getInstance().createAdmin(username, email, password, userRole);
        return "redirect:/staffPortal";
    }

    @PostMapping("/addProductInventory")
    public String addProductInventory(@RequestParam("productName") String productName,
                                      @RequestParam("productDescribtion") String productDescribtion,
                                      @RequestParam("productPrice") double productPrice,
                                      @RequestParam("productQuantity") int productQuantity,
                                      @RequestParam("category") String category,
                                      @RequestParam("productImage") String productImage) {


        csystem.getInstance().addProduct(productImage,productName , productPrice, productQuantity, category,productDescribtion);

        return "redirect:/add_product";
    }

    @GetMapping("/add_product")
    public String showAddProductPage() {
        return "add_product";
    }

    @GetMapping("/churn")
    public String showChurnPage(Model model) {

        List<Map<String, Object>> customers = new ArrayList<>();
        List<orderHistory> allOrders = csystem.getInstance().manageChurn();

        for (orderHistory order : allOrders) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("id", order.getOrderId());
            Customer cust = csystem.getInstance().getCustomerById(order.getCustomerId());
            customer.put("customerName", cust.getName());
            customer.put("orderDate", order.getOrderDate());
            customers.add(customer);
        }

        // Add the customer data to the model
        model.addAttribute("customers", customers);

        return "churn";
    }

    @GetMapping("/feedback")
    public String showFeedbackPage(Model model) {
        List<Map<String, Object>> feedbacks = new ArrayList<>();
        List<Feedback> allFeedbacks = csystem.getInstance().getAllFeedback();
        for (Feedback feedback : allFeedbacks) {
            Map<String, Object> feedbackData = new HashMap<>();
            feedbackData.put("customerName", csystem.getInstance().getCustomerById(feedback.getCustomerId()).getName());
            feedbackData.put("productName", csystem.getInstance().getProductById(feedback.getProductId()).getName());
            feedbackData.put("feedback", feedback.getFeedbackDescription());
            feedbacks.add(feedbackData);
        }
        // Add the feedback data to the model
        model.addAttribute("feedbacks", feedbacks);

        return "feedback";
    }

    @GetMapping("/Home")
    public String showHomePage(Model model) {
        return "Home";
    }


     @GetMapping("/loyalty_program")
    public String showLoyaltyProgram(Model model) {
        model.addAttribute("customer", null);
        model.addAttribute("notFound", false);
        return "loyalty_program";
    }

    @PostMapping("/findCustomer")
    public String findCustomer(@RequestParam String userId, Model model) {
        int customerIdInt = Integer.parseInt(userId);
        Customer customer = csystem.getInstance().getCustomerById(customerIdInt);

        if (customer != null) {
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("id", customer.getId());
            customerData.put("name", customer.getName());
            customerData.put("email", customer.getEmail());
            customerData.put("phone", customer.getPhone());
            model.addAttribute("customer", customer);
        } else {
            model.addAttribute("notFound", true);
        }
        return "loyalty_program";
    }

    @PostMapping("/submitLoyalty")
    public String submitLoyalty(@RequestParam String loyaltyTier, @RequestParam String id, Model model) {
        // Save or process loyalty tier for the customer
        int customerId = Integer.parseInt(id);
        Customer customer = csystem.getInstance().getCustomerById(customerId);
        csystem.getInstance().LoyaltyProgram(customerId);
        model.addAttribute("successMessage", "Loyalty tier '" + loyaltyTier + "' assigned to " + customer.getName() + "!");
        return "redirect:/loyalty_program";
    }


    @GetMapping("/manage_customer")
    public String manageCustomer(Model model) {
        // Initialize the model with empty values for the form
        model.addAttribute("customerId", "");
        model.addAttribute("currentCustomer", null);
        model.addAttribute("notFound", false); // Default notFound to false
        model.addAttribute("message", null);
        return "manage_customer"; // Thymeleaf template
    }

    // Search for a customer by ID
    @PostMapping("/searchCustomer")
    public String searchCustomer(@RequestParam("customerId") String customerId, Model model) {
        try {
            int customerIdInt = Integer.parseInt(customerId); // Ensure the ID is a valid integer
            Customer customer = csystem.getInstance().getCustomerById(customerIdInt); // Fetch customer details

            if (customer != null) {
                // Populate model with customer details if found
                model.addAttribute("currentCustomer", customer);
                model.addAttribute("notFound", false);
            } else {
                // If not found, show a message
                model.addAttribute("notFound", true);
                model.addAttribute("currentCustomer", null);
            }
        } catch (NumberFormatException e) {
            // Handle invalid customer ID format
            model.addAttribute("notFound", true);
            model.addAttribute("currentCustomer", null);
        }

        model.addAttribute("customerId", customerId);
        return "manage_customer";
    }

    // Save customer changes
    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestParam("id") String id,
                                @RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone,
                                Model model) {
        try {
            int customerId = Integer.parseInt(id); // Ensure the ID is a valid integer
            Customer customer = csystem.getInstance().getCustomerById(customerId);

            if (customer != null) {
                // Update customer details
                csystem.getInstance().updateCustomerProfile(customerId, name, email, phone, customer.getPassword(), customer.getAddress(), customer.getPreferences(), customer.getLoyaltyPoints(), customer.getTier());

                model.addAttribute("message", "Customer details updated successfully.");
                model.addAttribute("currentCustomer", customer);
                model.addAttribute("notFound", false);
            } else {
                // Handle case where customer is not found (shouldn't happen with a valid ID)
                model.addAttribute("message", "Error: Customer not found.");
                model.addAttribute("notFound", true);
            }
        } catch (NumberFormatException e) {
            // Handle invalid ID format
            model.addAttribute("message", "Error: Invalid customer ID.");
            model.addAttribute("notFound", true);
        }

        return "manage_customer";
    }

    @GetMapping("/restock")
    public String showRestockPage() {
        return "restock";
    }
    
        @PostMapping("/restock/find")
        public String findProduct(@RequestParam("productId") String productId, Model model) {
            Product product = csystem.getInstance().getProductById(Integer.parseInt(productId));

            if (product != null) {
                model.addAttribute("productId", product.getId());
                model.addAttribute("productName", product.getName());
                model.addAttribute("productStock", product.getQuantity());
            } else {
                model.addAttribute("error", "Product not found");
            }
            return "restock";
        }
    
        @PostMapping("/restock/update")
        public String updateStock(@RequestParam("productId") String productId, @RequestParam("restockAmount") int restockAmount, Model model) {
            Product product = csystem.getInstance().getProductById(Integer.parseInt(productId));
            if (product != null) {
                int currentStock = (int) product.getQuantity();
                csystem.getInstance().updateStock(Integer.parseInt(productId), currentStock + restockAmount);
                model.addAttribute("success", "Product restocked successfully");
                model.addAttribute("productId", product.getId());
                model.addAttribute("productName", product.getName());
                model.addAttribute("productStock", product.getQuantity());
            } else {
                model.addAttribute("error", "Product not found");
            }
            return "restock";
        }    
        // Other methods...

           @GetMapping("/track_history")
    public String showPurchaseHistory() {
        // Return an HTML page that will load the purchase history dynamically via JavaScript
        return "track_history"; // The page where JS will process the data
    }
    /*
    @PostMapping("/track_history/find")
    public ResponseEntity<Map<String, Object>> findCustomerPurchaseHistory(@RequestParam("userId") String userId) {
        // Simulate fetching customer data (replace with your actual data-fetching logic)
        Map<String, Object> response = new HashMap<>();
        
        if ("1".equals(userId)) {
            String customerName = "John Doe"; // Example customer name
            Map<String, List<Map<String, Object>>> purchases = fetchPurchasesForCustomer(userId); // Replace with your logic

            // Calculate row spans for each order
            Map<String, Integer> rowSpans = new HashMap<>();
            for (String orderId : purchases.keySet()) {
                rowSpans.put(orderId, purchases.get(orderId).size());
            }

            response.put("customerId", userId);
            response.put("customerName", customerName);
            response.put("purchases", purchases);
            response.put("rowSpans", rowSpans);
            response.put("error", null);
        } else {
            response.put("customerId", userId);
            response.put("error", "Customer not found.");
            response.put("customerName", null);
            response.put("purchases", new HashMap<String, List<Map<String, Object>>>());
            response.put("rowSpans", new HashMap<String, Integer>());
        }

        return ResponseEntity.ok(response); // Return JSON response
    }

    private Map<String, List<Map<String, Object>>> fetchPurchasesForCustomer(String userId) {
        // This is where you'd fetch the data from your database
        Map<String, List<Map<String, Object>>> purchaseMap = new HashMap<>();

        // Add dummy purchase data (replace with actual DB data)
        List<Map<String, Object>> order1Products = new ArrayList<>();
        order1Products.add(createProduct("Laptop", 1, 1200));
        order1Products.add(createProduct("Mouse", 1, 25));

        List<Map<String, Object>> order2Products = new ArrayList<>();
        order2Products.add(createProduct("Keyboard", 1, 50));

        purchaseMap.put("1234", order1Products);
        purchaseMap.put("1235", order2Products);

        return purchaseMap;
    }

    private Map<String, Object> createProduct(String name, int quantity, double unitPrice) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("quantity", quantity);
        product.put("unitPrice", unitPrice);
        return product;
    }


*/


@PostMapping("/track_history/find")
public ResponseEntity<Map<String, Object>> findCustomerPurchaseHistory(@RequestParam("userId") String userId) {
    Map<String, Object> response = new HashMap<>();

    // Fetch all orders from the system
    List<orderHistory> allOrders = csystem.getInstance().getAllOrders(); // Replace with your actual system method to fetch all orders
    List<orderHistory> userOrders = allOrders.stream()
                                       .filter(order -> userId.equals(String.valueOf(order.getCustomerId())))
                                       .collect(Collectors.toList());

    if (!userOrders.isEmpty()) {
        // Extract customer name from one of the user's orders (assuming it exists in Order data)
        String customerName = csystem.getInstance().getCustomerById(Integer.parseInt(userId)).getName(); // Replace with your logic if customer data is stored separately

        // Structure purchases and calculate row spans
        Map<String, List<Map<String, Object>>> purchases = new HashMap<>();
        Map<String, Integer> rowSpans = new HashMap<>();

        for (orderHistory order : userOrders) {
            List<Map<String, Object>> products = new ArrayList<>();

            // Iterate through the cart's products and quantities
            List<Product> productList = order.getCart().getProducts();
            List<Integer> quantities = order.getCart().getQuantities();
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                int quantity = quantities.get(i);
                products.add(createProduct(product.getName(), quantity, product.getPrice()));
            }

            purchases.put(String.valueOf(order.getOrderId()), products);
            rowSpans.put(String.valueOf(order.getOrderId()), products.size());
        }

        response.put("customerId", userId);
        response.put("customerName", customerName);

        response.put("purchases", purchases);
        response.put("rowSpans", rowSpans);
        response.put("error", null);
    } else {
        // Customer not found or no orders for the customer
        response.put("customerId", userId);
        response.put("error", "No orders found for the specified customer.");
        response.put("customerName", null);
        response.put("purchases", new HashMap<String, List<Map<String, Object>>>());
        response.put("rowSpans", new HashMap<String, Integer>());
    }

    return ResponseEntity.ok(response); // Return JSON response
}



@PostMapping("/purchase_history/find")
public ResponseEntity<Map<String, Object>> CustomerPurchaseHistory() {
    String userId = String.valueOf(csystem.getInstance().getCurrentCustomer().getId());
    Map<String, Object> response = new HashMap<>();


    // Fetch all orders from the system
    List<orderHistory> allOrders = csystem.getInstance().getAllOrders(); // Replace with your actual system method to fetch all orders
    for (orderHistory order : allOrders) {
        System.out.println(order.getOrderId() + " " + order.getCustomerId());
    }                                   

    List<orderHistory> userOrders = allOrders.stream()
                                       .filter(order -> userId.equals(String.valueOf(order.getCustomerId())))
                                       .collect(Collectors.toList());
                          

    for (orderHistory order : userOrders) {
        System.out.println(order.getOrderId() + " " + order.getCustomerId());
    }                                  

    if (!userOrders.isEmpty()) {
        // Extract customer name from one of the user's orders (assuming it exists in Order data)
        String customerName = csystem.getInstance().getCustomerById(Integer.parseInt(userId)).getName(); // Replace with your logic if customer data is stored separately

        // Structure purchases and calculate row spans
        Map<String, List<Map<String, Object>>> purchases = new HashMap<>();
        Map<String, Integer> rowSpans = new HashMap<>();

        for (orderHistory order : userOrders) {
            List<Map<String, Object>> products = new ArrayList<>();

            // Iterate through the cart's products and quantities
            List<Product> productList = order.getCart().getProducts();
            
            List<Integer> quantities = order.getCart().getQuantities();
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                int quantity = quantities.get(i);
                products.add(createProduct(product.getName(), quantity, product.getPrice()));
            }

            purchases.put(String.valueOf(order.getOrderId()), products);
            rowSpans.put(String.valueOf(order.getOrderId()), products.size());
        }

        response.put("customerId", userId);
        response.put("customerName", customerName);
        
        response.put("purchases", purchases);
        response.put("rowSpans", rowSpans);
        response.put("error", null);
    } else {
        // Customer not found or no orders for the customer
        response.put("customerId", userId);
        response.put("error", "No orders found for the specified customer.");
        response.put("customerName", null);
        response.put("purchases", new HashMap<String, List<Map<String, Object>>>());
        response.put("rowSpans", new HashMap<String, Integer>());
    }

    System.out.println(response);

    return ResponseEntity.ok(response); // Return JSON response
}


private Map<String, Object> createProduct(String name, int quantity, double price) {
    Map<String, Object> product = new HashMap<>();
    product.put("name", name);
    product.put("quantity", quantity);
    product.put("price", price);
    product.put("total", price * quantity);
    return product;
}

}
