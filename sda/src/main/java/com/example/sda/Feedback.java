package com.example.sda;
public class Feedback {
    private static int idCounter = 0;
    private int id;
    private int customerId;
    private int productId;
    private String feedbackDescription;

    public Feedback(int customerId, int productId, String feedbackDescription) {
        this.id = ++idCounter;
        this.customerId = customerId;
        this.productId = productId;
        this.feedbackDescription = feedbackDescription;
    }

    public Feedback() {
        this.id = ++idCounter;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }
}