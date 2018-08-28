package com.sporrong.shoppinglist2;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ShoppingItem {
    public String productName;
    public boolean bought;
    public String pushKey;
    private int price;




    public ShoppingItem() {

    }

    public ShoppingItem(String productName, boolean bought){
        this.bought = bought;
        this.productName = productName;
    }



    public boolean getBought() {
        return bought;
    }


    public String getProductName() {

        return productName;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
