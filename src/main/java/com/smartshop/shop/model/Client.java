package com.smartshop.shop.model;

import com.smartshop.shop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nom;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier customerTier = CustomerTier.BASIC;

    private Integer totalOrders = 0;
    private BigDecimal totalSpent = BigDecimal.ZERO;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orderList;
}
