package com.smartshop.shop.dto.responseDTO;

import com.smartshop.shop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private String id;
    private LocalDate date;
    private String clientName;
    private OrderStatus status;

    private double sousTotal;
    private double remise;
    private double tva;
    private double total;
    private double montantRestant;

    private List<OrderItemResponseDTO> items;
    private List<PaymentResponseDTO> payments;
}