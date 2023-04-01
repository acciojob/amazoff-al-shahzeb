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
    Map<String, List<String>> pairMap = new HashMap<>();


    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String deliveryPartner){
        int orders=0;
        if(pairMap.containsKey(deliveryPartner))
            orders=pairMap.get(deliveryPartner).size();
        DeliveryPartner deliveryPartner1=new DeliveryPartner(deliveryPartner,orders);

        deliveryPartnerMap.put(deliveryPartner,deliveryPartner1);
    }

    public void addOrderPartnerPair(String partnerId, String orderId){
        List<String> orderList = new ArrayList<>();

        if(pairMap.containsKey(partnerId))
            orderList=pairMap.get(partnerId);

        orderList.add(orderId);

        pairMap.put(partnerId,orderList);
    }

    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return pairMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return pairMap.get(partnerId);
    }

    public List<String> getAllOrders(){
        return (List<String>) (orderMap.keySet());
    }

    public int getCountOfUnassignedOrders(){
        int count=0;
        for(int i=0; i<pairMap.size(); i++){
            for(String id : pairMap.get(i)){
                if(!orderMap.containsKey(id))
                    count++;
            }
        }
        return count;
    }

    public void deleteOrderById(String id){
        orderMap.remove(id);
        for(int i=0; i<pairMap.size(); i++) {
            for(int j=0; j<pairMap.get(i).size(); j++) {
                if(pairMap.get(i).get(j).equals(id)){
                    pairMap.get(i).remove(j);
                    return;
                }
            }
        }
    }

    public void deletePartnerById(String id){
        pairMap.remove(id);
        deliveryPartnerMap.remove(id);
    }

    public String getLastDeliveryTimeByPartnerId(String pid){
        String orderId= pairMap.get(pid).get(pairMap.get(pid).size()-1);
        int time = (orderMap.get(orderId).getDeliveryTime());
        return String.valueOf(time);
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String deliveryTime, String pid){
        int hr = Integer.parseInt(""+deliveryTime.charAt(0))*10+Integer.parseInt(""+deliveryTime.charAt(1));
        int min = Integer.parseInt(""+deliveryTime.charAt(3))*10+Integer.parseInt(""+deliveryTime.charAt(4));
        int time = hr*60+min,cnt=0;

         for(String id: pairMap.get(pid)){
             if(orderMap.get(id).getDeliveryTime()>time)
                 cnt++;
         }

        return cnt;
    }
}
