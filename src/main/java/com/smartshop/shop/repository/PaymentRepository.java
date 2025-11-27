package com.smartshop.shop.repository;

import com.smartshop.shop.model.Order;
import com.smartshop.shop.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findPaymentsByOrder(Order order);
    int countByOrderId(String orderId);
}
