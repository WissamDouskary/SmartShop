package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.PromoCodeRequestDTO;
import com.smartshop.shop.dto.responseDTO.PromoCodeResponseDTO;
import com.smartshop.shop.model.PromoCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {
    PromoCodeResponseDTO toResponse(PromoCode promoCode);
    PromoCode toEntity(PromoCodeRequestDTO dto);
}
