package com.example.h_mal.mochee;

import java.util.ArrayList;

/**
 * Created by h_mal on 08/02/2018.
 */

public class Order {

    public String customerName;
    public String customerAddress;
    public String customerCountry;
    public String customerNote;
    public String customerEmail;
    public Payment customerPayment;
    public ArrayList<Item> customerBasket;

    //all parts
    public Order(String customerName, String customerAddress, String customerCountry, String customerNote, String customerEmail, Payment customerPayment, ArrayList<Item> customerBasket) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCountry = customerCountry;
        this.customerNote = customerNote;
        this.customerEmail = customerEmail;
        this.customerPayment = customerPayment;
        this.customerBasket = customerBasket;
    }
}

    class Payment{

    public Price customerPriceBreakdown;
    public PaypalInfo customerPaypalInfo;

    public Payment(Price customerPriceBreakdown, PaypalInfo customerPaypalInfo) {
        this.customerPriceBreakdown = customerPriceBreakdown;
        this.customerPaypalInfo = customerPaypalInfo;
    }
}

class Price{

    public Double deliveryPrice;
    public Double subtotalPrice;
    public Double totalPrice;

    public Price(Double deliveryPrice, Double subtotalPrice, Double totalPrice) {
        this.deliveryPrice = deliveryPrice;
        this.subtotalPrice = subtotalPrice;
        this.totalPrice = totalPrice;
    }
}

class PaypalInfo{

    public String transactionID;
    public String date;
    public String state;

    public PaypalInfo(String transactionID, String date, String state) {
        this.transactionID = transactionID;
        this.date = date;
        this.state = state;
    }
}

class Basket{

    public Item items;

    public Basket(Item items) {
        this.items = items;
    }
}

class Item{

    public Integer itemNumber;
    public String itemName;
    public String itemSize;
    public Integer itemQuantity;

    public Item(Integer itemNumber, String itemName, String itemSize, Integer itemQuantity) {
        this.itemNumber = itemNumber;
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemQuantity = itemQuantity;
    }
}
