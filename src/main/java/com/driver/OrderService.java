package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order){
        orderRepository.addOrder(order);
    }

    public void addPartner(String deliveryPartner){
        orderRepository.addPartner(deliveryPartner);
    }

    public void addOrderPartnerPair(String partnerId, String orderId){
        orderRepository.addOrderPartnerPair(partnerId,orderId);
    }

    public Order getOrderById(String orderId){
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }

    public int getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrders();
    }

    public void deleteOrderById(String id){
        orderRepository.deleteOrderById(id);
    }

    public void deletePartnerById(String id){
        orderRepository.deletePartnerById(id);
    }

    public String getLastDeliveryTimeByPartnerId(String pid){
        return orderRepository.getLastDeliveryTimeByPartnerId(pid);
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String deliveryTime, String pid){
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(deliveryTime,pid);
    }
}
