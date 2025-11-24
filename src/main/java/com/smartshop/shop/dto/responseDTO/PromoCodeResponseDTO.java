package com.smartshop.shop.dto.responseDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeResponseDTO {
    private String id;
    private String code;
    private double percentageRemise;
    private boolean active;
}
