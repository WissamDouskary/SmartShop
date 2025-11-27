package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.OrderItemRequestDTO;
import com.smartshop.shop.dto.requestDTO.OrderRequestDTO;
import com.smartshop.shop.dto.responseDTO.OrderResponseDTO;
import com.smartshop.shop.enums.CustomerTier;
import com.smartshop.shop.enums.OrderStatus;
import com.smartshop.shop.exception.BusinessException;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.OrderMapper;
import com.smartshop.shop.model.*;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.repository.OrderRepository;
import com.smartshop.shop.repository.ProductRepository;
import com.smartshop.shop.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        Order order = orderMapper.toEntity(dto);

        Client client = clientRepository.findById(dto.getClientId()).orElseThrow(() ->
                new ResourceNotFoundException("Aucun client avec id :" + dto.getClientId())
        );

        double promoCode = 0;
        PromoCode promoc = null;

        if(dto.getPromoCode() != null){
            PromoCode pc = promoCodeRepository.findPromoCodeByCode(dto.getPromoCode()).orElseThrow(() ->
                    new ResourceNotFoundException("Aucun promo code avec code: "+dto.getPromoCode())
            );
            promoc = pc;
            if(pc.isActive()){
                promoCode = pc.getPercentage_remise();
            }else{
                throw new BusinessException("promo code is expired! try another one!");
            }
        }

        double sub_total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequestDTO item : dto.getItems()){
            Product product = productRepository.findById(item.getProductId()).orElseThrow(
                    () -> new ResourceNotFoundException("Aucun produit avec id: "+item.getProductId())
            );

            if(item.getQuantity() > product.getStockDisponible()){
                throw new BusinessException("Quantity est plus élevé que nos stock disponible!");
            }

            double lineTotal = product.getPrixUnitaire() * item.getQuantity();

            sub_total = sub_total + lineTotal;

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .prixUnitaire(product.getPrixUnitaire())
                    .quantity(item.getQuantity())
                    .product(product)
                    .totalLigne(lineTotal)
                    .build();

            orderItem.setProduct(product);

            orderItems.add(orderItem);
            order.setOrderItemList(orderItems);
        }

        decrementStock(order);

        double loyalityDiscount = calculatingLoyalityDiscount(client);

        double finalDiscount = sub_total * ((promoCode / 100.0) + (loyalityDiscount / 100.0));

        double tax_base = sub_total - finalDiscount;

        double tva = tax_base * 0.2;

        double finalTotal = tax_base + tva;

        order.setMontantRestant(finalTotal);
        order.setRemise(finalDiscount);
        order.setPromoCode(promoc);
        order.setTva(tva);
        order.setSousTotal(sub_total);
        order.setClient(client);
        order.setTotal(finalTotal);
        order.setDate(LocalDate.now());

        Order savedOrder =  orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByClient(String clientId) {
        List<Order> orders = orderRepository.findAllByClientId(clientId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void decrementStock(Order order) {
        for (OrderItem item : order.getOrderItemList()) {
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() ->
                 new ResourceNotFoundException("aucun produit avec id :" + item.getProduct().getId())
            );

            if (product.getStockDisponible() < item.getQuantity()) {
                throw new BusinessException("Stock changed! Insufficient stock for: " + product.getNom());
            }

            int newStock = product.getStockDisponible() - item.getQuantity();
            product.setStockDisponible(newStock);

            productRepository.save(product);
        }
    }

    @Transactional
    public double calculatingLoyalityDiscount(Client client){
        if(client.getCustomerTier().equals(CustomerTier.BASIC)){
            return 0;
        }else if(client.getCustomerTier().equals(CustomerTier.SILVER)){
            return 5;
        }else if (client.getCustomerTier().equals(CustomerTier.GOLD)){
            return 10;
        }else if (client.getCustomerTier().equals(CustomerTier.PLATINUM)){
            return 15;
        }
        return 0;
    }

    @Transactional
    public OrderResponseDTO confirmOrderAfterCompletingPayment(String orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("aucun order avec id : "+orderId)
        );

        if(order.getMontantRestant() != 0){
            throw new BusinessException("can't confirme order, because it is not full paid");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.getClient().setTotalOrders(order.getClient().getTotalOrders() + 1);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }
}
