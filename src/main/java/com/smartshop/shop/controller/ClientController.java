package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.AccessDeniedException;
import com.smartshop.shop.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponseDTO>> createClient(
            @RequestBody @Valid ClientRequestDTO dto,
            HttpServletRequest request
    ){
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("USER_ROLE");

        if (userRole != Role.ADMIN) {
            throw new AccessDeniedException("Only Admins can create new clients.");
        }

        ClientResponseDTO newClient = clientService.createClient(dto);

        return ResponseEntity.ok(ApiResponse.success(newClient, "Client created successfully"));
    }
}
