package com.example.sda;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

@Repository
public class OrderHistoryDAO {

    private JdbcTemplate jdbcTemplate;

    public OrderHistoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to fetch all order history, including cart and products
    public List<orderHistory> getAllOrderHistory() {
    String sql = "SELECT o.order_id, o.customer_id, o.order_date, o.total_order_price, " +
                 "c.cart_id, p.pid, p.image_url, p.pname, p.unitprice, cp.quantity, " +
                 "cp.total_price, p.category, p.pdescription " +
                 "FROM OrderHistory o " +
                 "LEFT JOIN Cart c ON o.order_id = c.order_id " +
                 "LEFT JOIN Cart_Product cp ON c.cart_id = cp.cart_id " +
                 "LEFT JOIN Product p ON cp.product_id = p.pid";

    return jdbcTemplate.query(sql, new OrderHistoryExtractor());
}

// Custom extractor for handling multiple rows and mapping them correctly.
private class OrderHistoryExtractor implements ResultSetExtractor<List<orderHistory>> {
    @Override
    public List<orderHistory> extractData(ResultSet rs) throws SQLException {
        Map<Integer, orderHistory> ordersMap = new HashMap<>();

        while (rs.next()) {
            int orderId = rs.getInt("order_id");

            // Fetch or create orderHistory
            orderHistory order = ordersMap.computeIfAbsent(orderId, id -> {
                orderHistory newOrder = new orderHistory();
                try {
                    newOrder.setCustomerId(rs.getInt("customer_id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    newOrder.setOrderDate(rs.getString("order_date"));
                } catch (SQLException e) {

                    e.printStackTrace();
                }
                try {
                    newOrder.setTotalOrderPrice(rs.getDouble("total_order_price"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                newOrder.setCart(new Cart());
                return newOrder;
            });

            // Map product details if available
            int productId = rs.getInt("pid");
            if (productId != 0) {
                Product product = new Product();
                product.setId(productId);
                product.setProductURL(rs.getString("image_url"));
                product.setName(rs.getString("pname"));
                product.setPrice(rs.getDouble("unitprice"));
                product.setCategory(rs.getString("category"));
                product.setDescription(rs.getString("pdescription"));

                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");

                order.getCart().addProduct(product, quantity, totalPrice);
            }
        }

        // Return all the orderHistory objects as a list
        return new ArrayList<>(ordersMap.values());
    }
}

    public void addOrderHistory(orderHistory orderHistory) {
        // Insert into OrderHistory
        Date sqlDate = Date.valueOf(orderHistory.getOrderDate());

        String orderSql = "INSERT INTO OrderHistory (order_id, customer_id, order_date, total_order_price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(orderSql, 
            orderHistory.getOrderId(),
            orderHistory.getCustomerId(),
            sqlDate,
            orderHistory.getTotalOrderPrice()
        );

        // Insert into Cart
        String cartSql = "INSERT INTO Cart (cart_id , order_id) VALUES (?,?)";
        jdbcTemplate.update(cartSql,  orderHistory.getOrderId(), orderHistory.getOrderId());

        // Insert into Cart_Product for each product in the cart
        String cartProductSql = "INSERT INTO Cart_Product (cart_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";

        Cart cart = orderHistory.getCart();
        List<Product> products = cart.getProducts();
        List<Integer> quantities = cart.getQuantities();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = quantities.get(i);
            double totalPrice = product.getPrice() * quantity;

            jdbcTemplate.update(cartProductSql, 
                orderHistory.getOrderId(),
                product.getId(),
                quantity,
                totalPrice
            );
        }
    }
}
