package com.smartshop.shop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal prixUnitaire;
    private BigDecimal totalLigne;
}
