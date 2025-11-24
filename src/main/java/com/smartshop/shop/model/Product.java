package com.smartshop.shop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private BigDecimal prixUnitaire;
    private Integer stockDisponible;

    private Boolean deleted = false;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
}
