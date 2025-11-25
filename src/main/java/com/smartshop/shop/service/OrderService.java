package com.smartshop.shop.service;

import com.smartshop.shop.dto.responseDTO.OrderResponseDTO;
import com.smartshop.shop.mapper.OrderMapper;
import com.smartshop.shop.model.Order;
import com.smartshop.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByClient(String clientId) {
        List<Order> orders = orderRepository.findAllByClientId(clientId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
