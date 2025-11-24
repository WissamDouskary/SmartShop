package com.smartshop.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promo_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String code;
    private double percentage_remise;
    private boolean active;
    private LocalDate createdAt = LocalDate.now();

    @OneToMany(mappedBy = "promoCode")
    private List<Order> orderList;
}
