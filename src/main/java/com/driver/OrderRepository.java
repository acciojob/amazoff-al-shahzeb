package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap = new HashMap<>();

    Map<String,String> orderPartnerMap = new HashMap<>();  //orderId->partnerId
    Map<String, List<String>> pairMap = new HashMap<>();  //partnerId->List<orderId>


    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String deliveryPartner){
        deliveryPartnerMap.put(deliveryPartner,new DeliveryPartner(deliveryPartner,0));
    }

    public void addOrderPartnerPair(String partnerId, String orderId){
        List<String> orderList = new ArrayList<>();

        if(pairMap.containsKey(partnerId))
            orderList=pairMap.get(partnerId);

        orderList.add(orderId);

        DeliveryPartner dp = deliveryPartnerMap.get(partnerId);
        dp.setNumberOfOrders(orderList.size());
        deliveryPartnerMap.put(partnerId,dp);

        orderPartnerMap.put(orderId,partnerId);

        pairMap.put(partnerId,orderList);
    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        if(deliveryPartnerMap.containsKey(partnerId))
            return deliveryPartnerMap.get(partnerId);
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId){
        int cnt=0;

        if(deliveryPartnerMap.containsKey(partnerId))
            cnt= deliveryPartnerMap.get(partnerId).getNumberOfOrders();

        return cnt;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        if(pairMap.containsKey(partnerId))
            return pairMap.get(partnerId);
        return new ArrayList<String>();
    }

    public List<String> getAllOrders(){
        List<String> orders=new ArrayList<>();
        orders.addAll(orderMap.keySet());
        return orders;
    }

    public int getCountOfUnassignedOrders(){
        return orderMap.size()-orderPartnerMap.size();
    }

    public void deleteOrderById(String id){
        orderMap.remove(id);
        String partnerId="";
        for(Map.Entry<String,List<String>> mp: pairMap.entrySet()){
            for(String order: mp.getValue()){
                if(order.equals(id)){
                    partnerId=mp.getKey();
                    break;
                }

            }
            if(!partnerId.equals(""))
                break;
        }

        if(partnerId.equals(""))
            return;

        List<String> orders = pairMap.get(partnerId);
        List<String> newList = new ArrayList<>();
        for(String s: orders)
            if(!s.equals(id))
                newList.add(s);

        pairMap.put(partnerId,newList);
        orderPartnerMap.remove(id);
    }

    public void deletePartnerById(String id){
        for(Map.Entry<String,String> mp: orderPartnerMap.entrySet())
            if(mp.getValue().equals(id))
                orderPartnerMap.remove(mp.getKey());

        if(pairMap.containsKey(id))
            pairMap.remove(id);
        if(deliveryPartnerMap.containsKey(id))
            deliveryPartnerMap.remove(id);

    }

    public String getLastDeliveryTimeByPartnerId(String pid){
        List<String> orders=pairMap.get(pid);
        int max=0;
        for(String time: orders){
            max=Math.max(max,(orderMap.get(time).getDeliveryTime()));
        }
        int min=max%60;
        String mins=""+min;
        //max-=(max%60);
        int h=max/60;
        String hrs=""+h;

        if(mins.length()<2) mins = "0"+mins;
        if(hrs.length()<2) hrs = "0"+hrs;

        return (hrs+":"+mins);
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String deliveryTime, String pid){
        int hr = Integer.parseInt(""+deliveryTime.charAt(0))*10+Integer.parseInt(""+deliveryTime.charAt(1));
        int min = Integer.parseInt(""+deliveryTime.charAt(3))*10+Integer.parseInt(""+deliveryTime.charAt(4));
        int time = hr*60+min,cnt=0;

        if(pairMap.containsKey(pid)) {
            for (String id : pairMap.get(pid)) {
                if (orderMap.containsKey(id) && orderMap.get(id).getDeliveryTime() > time)
                    cnt++;
            }
        }

        return cnt;
    }
}
