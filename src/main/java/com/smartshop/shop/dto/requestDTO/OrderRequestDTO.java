package com.smartshop.shop.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String clientId;
    private String promoCode;
    private List<OrderItemRequestDTO> items;
}
