package com.example.sda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

@Service
public class dbService {

    @Autowired
    public dbService(OrderHistoryDAO orderHistoryDAO) {
        this.orderHistoryDAO = orderHistoryDAO;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderHistoryDAO orderHistoryDAO;

    public List<orderHistory> getAllOrderHistory() {
        return orderHistoryDAO.getAllOrderHistory();
    }

    public void addOrderHistory(orderHistory orderHistory) {
        orderHistoryDAO.addOrderHistory(orderHistory);
    }

    // Get all users from the database
    public List<Admin> getAllAdmin() {
        String sql = "SELECT aid, aname, aemail, apassword, role FROM Admin";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Admin admin = factoryAdmin.createAdmin(rs.getString("aname"), rs.getString("aemail"), rs.getString("apassword"), rs.getString("role"));
            return admin;
        });
    }
    
    public List<Customer> getAllCustomersWithPreferences() {
    // SQL to fetch all customers with their preferences
    String sql = "SELECT c.cid, c.cname, c.cemail, c.cpass, c.number, c.address, c.loyaltypoints, c.tier, p.preferences " +
                 "FROM Customer c " +
                 "LEFT JOIN Preferences p ON c.cid = p.cid " +
                 "ORDER BY c.cid";

    // Fetch and group the data
    return jdbcTemplate.query(sql, rs -> {
        Map<Integer, Customer> customerMap = new HashMap<>();
    
        try {
            while (rs.next()) {
                int customerId = rs.getInt("cid");
    
                // Check if this customer is already mapped
                Customer customer = customerMap.computeIfAbsent(customerId, id -> {
                    Customer newCustomer = new Customer();
                    try {
                        newCustomer.setName(rs.getString("cname"));
                        newCustomer.setEmail(rs.getString("cemail"));
                        newCustomer.setPassword(rs.getString("cpass"));
                        newCustomer.setPhone(rs.getString("number"));
                        newCustomer.setAddress(rs.getString("address"));
                        newCustomer.setLoyaltyPoints(rs.getInt("loyaltypoints"));
                        newCustomer.setTier(rs.getString("tier"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    newCustomer.setPreferences(new ArrayList<>());
                    return newCustomer;
                });
    
                // Add preference if it exists
                String preference = rs.getString("preferences");
                if (preference != null) {
                    customer.getPreferences().add(preference);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    
        return new ArrayList<>(customerMap.values());
    });
}

    // Add a new user to the database
    public void addAdmin(Admin admin) {
        String sql = "INSERT INTO Admin (aid , aname, aemail,apassword,role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, admin.getId(), admin.getName(), admin.getEmail(),admin.getPassword(),admin.getRole());
    }

    public void addCustomerWithPreferences(Customer customer) {
        // SQL to insert into Customer table
        String customerSql = "INSERT INTO Customer (cid , cname, cemail, cpass, number, address, loyaltypoints, tier) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING cid";
    
        // Insert customer and retrieve the generated customer ID (cid)
        Integer customerId = jdbcTemplate.queryForObject(
            customerSql, 
            new Object[] {
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getLoyaltyPoints(),
                customer.getTier()
            },
            Integer.class
        );
    
        // SQL to insert into Preferences table
        String preferencesSql = "INSERT INTO Preferences (preferences, cid) VALUES (?, ?)";
    
        // Batch update preferences for the customer
        jdbcTemplate.batchUpdate(preferencesSql, customer.getPreferences(), customer.getPreferences().size(), (ps, preference) -> {
            ps.setString(1, preference);
            ps.setInt(2, customerId);
        });
    }
    
    public List<Product> getAllProducts() {
        String sql = "SELECT pid, pname, pdescription, image_url, stock, unitprice, category FROM Product";
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
        
            return new Product(
                rs.getString("image_url"),
                rs.getString("pname"),
                rs.getBigDecimal("unitprice").doubleValue(),
                rs.getInt("stock"),
                rs.getString("category"),
                rs.getString("pdescription") 
            );
        });
        
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO Product (pid, pname, pdescription, image_url, stock, unitprice, category) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        jdbcTemplate.update(sql,
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getProductURL(),
            product.getQuantity(),
            product.getPrice(),
            product.getCategory()
        );
    }
    
    public void addFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback (feedback_id, cid, pid, feedback_desc) " +
                     "VALUES (?, ?, ?, ?)";
    
        jdbcTemplate.update(sql,
            feedback.getId(),
            feedback.getCustomerId(),
            feedback.getProductId(),
            feedback.getFeedbackDescription()
        );
    }
    
    public List<Feedback> getAllFeedback() {
        String sql = "SELECT feedback_id, cid, pid, feedback_desc FROM Feedback";
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Feedback(
                rs.getInt("cid"),
                rs.getInt("pid"),
                rs.getString("feedback_desc")
            );
        });
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer " +
                     "SET cname = ?, cemail = ?, cpass = ?, number = ?, address = ?, " +
                     "loyaltypoints = ?, tier = ? " +
                     "WHERE cid = ?";
    
        jdbcTemplate.update(sql,
            customer.getName(),         // cname
            customer.getEmail(),        // cemail
            customer.getPassword(),     // cpass
            customer.getPhone(),        // number
            customer.getAddress(),      // address
            customer.getLoyaltyPoints(),// loyaltypoints
            customer.getTier(),         // tier
            customer.getId()            // cid
        );
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Product " +
                     "SET pname = ?, pdescription = ?, image_url = ?, stock = ?, unitprice = ?, category = ? " +
                     "WHERE pid = ?";
    
        jdbcTemplate.update(sql,
            product.getName(),        // pname
            product.getDescription(), // pdescription
            product.getProductURL(),    // image_url
            product.getQuantity(),       // stock
            product.getPrice(),   // unitprice
            product.getCategory(),    // category
            product.getId()           // pid
        );
    }
    
    
}
