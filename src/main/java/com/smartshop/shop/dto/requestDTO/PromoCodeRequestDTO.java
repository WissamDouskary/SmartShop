package com.smartshop.shop.dto.requestDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeRequestDTO {
    @NotBlank(message = "Code is required")
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Code must follow format PROMO-XXXX (uppercase alphanumeric)")
    private String code;

    @Min(value = 1, message = "Percentage must be at least 1%")
    @Max(value = 100, message = "Percentage cannot exceed 100%")
    private double percentageRemise;

    private boolean active;
}