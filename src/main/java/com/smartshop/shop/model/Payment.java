package com.smartshop.shop.model;

import com.smartshop.shop.enums.PaymentStatus;
import com.smartshop.shop.enums.TypePayment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer paymentNumber;
    private BigDecimal montant;
    private LocalDate datePayment = LocalDate.now();
    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    private TypePayment typePayment;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
