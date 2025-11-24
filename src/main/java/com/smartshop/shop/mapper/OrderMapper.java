package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.*;
import com.smartshop.shop.dto.requestDTO.OrderRequestDTO;
import com.smartshop.shop.dto.responseDTO.OrderItemResponseDTO;
import com.smartshop.shop.dto.responseDTO.OrderResponseDTO;
import com.smartshop.shop.model.Order;
import com.smartshop.shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface OrderMapper {

    @Mapping(source = "client.nom", target = "clientName")
    @Mapping(source = "orderItemList", target = "items")
    OrderResponseDTO toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Order toEntity(OrderRequestDTO dto);

    @Mapping(source = "product.nom", target = "productName")
    OrderItemResponseDTO toItemResponse(OrderItem orderItem);
}
