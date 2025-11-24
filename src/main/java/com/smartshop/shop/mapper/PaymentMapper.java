package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.PaymentRequestDTO;
import com.smartshop.shop.dto.responseDTO.PaymentResponseDTO;
import com.smartshop.shop.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponseDTO toResponse(Payment payment);
    Payment toEntity(PaymentRequestDTO dto);
}
