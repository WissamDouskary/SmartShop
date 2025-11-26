package com.smartshop.shop.dto.responseDTO;

import com.smartshop.shop.enums.PaymentStatus;
import com.smartshop.shop.enums.TypePayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String id;
    private Integer paymentNumber;
    private double montant;
    private LocalDate datePayment;
    private PaymentStatus status;
    private TypePayment typePayment;
}
