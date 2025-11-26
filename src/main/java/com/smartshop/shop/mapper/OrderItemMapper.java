package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.OrderItemRequestDTO;
import com.smartshop.shop.dto.responseDTO.OrderItemResponseDTO;
import com.smartshop.shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.nom", target = "productName")
    OrderItemResponseDTO toResponse(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "prixUnitaire", ignore = true)
    @Mapping(target = "totalLigne", ignore = true)
    OrderItem toEntity(OrderItemRequestDTO dto);
}