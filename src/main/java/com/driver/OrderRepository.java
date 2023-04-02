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
//        int orders=0;
//        if(pairMap.containsKey(deliveryPartner))
//            orders=pairMap.get(deliveryPartner).size();
//        DeliveryPartner deliveryPartner1=new DeliveryPartner(deliveryPartner,orders);

        deliveryPartnerMap.put(deliveryPartner,new DeliveryPartner(deliveryPartner,0));
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
        return deliveryPartnerMap.get(partnerId).getNumberOfOrders();
        //return pairMap.get(partnerId).size();
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
            count+=pairMap.get(i).size();
        }
        return orderMap.size()-count;
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
        List<String> newList=new ArrayList<>();

        for(String s: orders)
            if(!s.equals(id))
                newList.add(s);

        pairMap.put(partnerId,newList);
    }

    public void deletePartnerById(String id){
        List<String> orders=pairMap.get(id);
        pairMap.remove(id);
        deliveryPartnerMap.remove(id);
        for(String s: orders)
            orderMap.remove(s);
    }

    public String getLastDeliveryTimeByPartnerId(String pid){
        List<String> orders=pairMap.get(pid);
        int max=0;
        for(String time: orders){
            max=Math.max(max,(orderMap.get(time).getDeliveryTime()));
        }
        String mins=""+max%60;
        max-=(max%60);
        String hrs=""+max/60;

        if(mins.length()<2) mins = "0"+mins;
        if(hrs.length()<2) hrs = "0"+hrs;

        return hrs+":"+mins;
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
