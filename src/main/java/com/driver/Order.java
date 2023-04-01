package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        int hr = Integer.parseInt(""+deliveryTime.charAt(0))*10+Integer.parseInt(""+deliveryTime.charAt(1));
        int min = Integer.parseInt(""+deliveryTime.charAt(3))*10+Integer.parseInt(""+deliveryTime.charAt(4));
        this.deliveryTime = hr*60+min;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
