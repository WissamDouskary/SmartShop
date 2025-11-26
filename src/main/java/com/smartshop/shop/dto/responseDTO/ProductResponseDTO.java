package com.smartshop.shop.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String id;
    private String nom;
    private double prixUnitaire;
    private Integer stockDisponible;
    private Boolean deleted;
}
