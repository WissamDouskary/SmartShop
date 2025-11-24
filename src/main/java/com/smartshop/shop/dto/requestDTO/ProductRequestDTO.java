package com.smartshop.shop.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String nom;
    private BigDecimal prixUnitaire;
    private Integer stockDisponible;
}
