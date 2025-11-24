package com.smartshop.shop.dto.responseDTO;

import com.smartshop.shop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    private BigDecimal sousTotal;
    private BigDecimal remise;
    private BigDecimal tva;
    private BigDecimal total;
    private BigDecimal montantRestant;

    private List<OrderItemResponseDTO> items;
    private List<PaymentResponseDTO> payments;
}