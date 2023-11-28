package com.example.devide;

public class Item {
    private String userID, itemID, itemName, itemCount, originalPrice, unitPrice, unit, photo1, photo2;

    private Item() {
    }

    public Item(String userID, String itemID, String itemName, String itemCount, String originalPrice, String unitPrice, String unit, String photo1, String photo2) {
        this.userID = userID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.originalPrice = originalPrice;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.photo1 = photo1;
        this.photo2 = photo2;
    }


    public String getUserID() {
        return userID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }
}
