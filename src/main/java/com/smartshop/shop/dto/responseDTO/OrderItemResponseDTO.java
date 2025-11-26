package com.smartshop.shop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private String id;
    private String productName;
    private Integer quantity;
    private double prixUnitaire;
    private double totalLigne;
}
