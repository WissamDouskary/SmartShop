package com.smartshop.shop.repository;

import com.smartshop.shop.model.Client;
import com.smartshop.shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByClientId(String clientId);
    int countOrderByClient(Client client);
}
