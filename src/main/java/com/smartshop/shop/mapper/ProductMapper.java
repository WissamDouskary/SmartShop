package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.ProductRequestDTO;
import com.smartshop.shop.dto.responseDTO.ProductResponseDTO;
import com.smartshop.shop.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponse(Product product);
    Product toEntity(ProductRequestDTO dto);
}
