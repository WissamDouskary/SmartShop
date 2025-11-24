package com.smartshop.shop.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String nom;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Valid
    @NotNull(message = "User account details are required")
    private UserRequestDTO user;
}