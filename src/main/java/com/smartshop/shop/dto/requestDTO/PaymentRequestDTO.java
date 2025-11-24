package com.smartshop.shop.dto.requestDTO;

import com.smartshop.shop.enums.TypePayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private String orderId;
    private BigDecimal montant;
    private TypePayment typePayment;
    private Integer paymentNumber;
    private LocalDate dateEcheance;
    private String reference;
}
