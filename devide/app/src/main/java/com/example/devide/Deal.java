package com.example.devide;

public class Deal {
    private String dealID;
    private String itemID;
    private String sellerID;
    private String customerID;
    private String time;
    private String memo;
    private String success;

    public Deal() {
    }

    public Deal(String dealID, String itemID, String sellerID, String customerID, String time, String memo, String success) {
        this.dealID = dealID;
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.customerID = customerID;
        this.time = time;
        this.memo = memo;
        this.success = success;
    }

    public String getDealID() {
        return dealID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getTime() {
        return time;
    }

    public String getMemo() {
        return memo;
    }

    public String getSuccess() {
        return success;
    }

}
