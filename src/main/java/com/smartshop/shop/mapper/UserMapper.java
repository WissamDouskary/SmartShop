package com.smartshop.shop.mapper;

import com.smartshop.shop.dto.requestDTO.UserRequestDTO;
import com.smartshop.shop.dto.responseDTO.UserResponseDTO;
import com.smartshop.shop.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponse(User user);
    User toEntity(UserRequestDTO dto);
}
