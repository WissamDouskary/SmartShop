package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.OrderItemRequestDTO;
import com.smartshop.shop.dto.responseDTO.OrderResponseDTO;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.mapper.OrderMapper;
import com.smartshop.shop.model.Order;
import com.smartshop.shop.model.OrderItem;
import com.smartshop.shop.model.Product;
import com.smartshop.shop.repository.OrderRepository;
import com.smartshop.shop.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByClient(String clientId) {
        List<Order> orders = orderRepository.findAllByClientId(clientId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validateStockAvailability(List<OrderItemRequestDTO> items) {
        for (OrderItemRequestDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BusinessException("Product not found"));

            if (product.getStockDisponible() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getNom());
            }
        }
    }

    @Transactional
    public void decrementStock(Order order) {
        for (OrderItem item : order.getOrderItemList()) {
            Product product = item.getProduct();

            if (product.getStockDisponible() < item.getQuantity()) {
                throw new BusinessException("Stock changed! Insufficient stock for: " + product.getNom());
            }

            int newStock = product.getStockDisponible() - item.getQuantity();
            product.setStockDisponible(newStock);

            productRepository.save(product);
        }
    }
}
