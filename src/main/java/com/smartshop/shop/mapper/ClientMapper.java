package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderMapper.class})
public interface ClientMapper {

    @Mapping(target = "user", source = "user")
    ClientResponseDTO toResponse(Client client);

    Client toEntity(ClientRequestDTO dto);
}
