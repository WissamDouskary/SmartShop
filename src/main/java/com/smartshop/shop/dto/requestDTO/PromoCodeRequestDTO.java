package com.smartshop.shop.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeRequestDTO {
    private String code;
    private double percentageRemise;
    private boolean active;
}