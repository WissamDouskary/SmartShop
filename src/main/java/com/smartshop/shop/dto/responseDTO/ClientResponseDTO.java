package com.smartshop.shop.dto.responseDTO;

import com.smartshop.shop.enums.CustomerTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    private String id;
    private String nom;
    private String email;
    private CustomerTier customerTier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
    private UserResponseDTO user;
}
