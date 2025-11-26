package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.service.ClientService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        AdminChecker.isAdmin(request);

        ClientResponseDTO newClient = clientService.createClient(dto);

        return ResponseEntity.ok(ApiResponse.success(newClient, "Client created successfully"));
    }

    @PatchMapping("/{id}")
    private ResponseEntity<ApiResponse<ClientResponseDTO>> updateClient(
            @PathVariable("id") String id,
            @RequestBody ClientRequestDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        ClientResponseDTO responseDTO = clientService.updateClient(id, dto);

        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Client updated successfully"));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ApiResponse<ClientResponseDTO>> deleteClient(
            @PathVariable("id") String id,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        clientService.deleteClient(id);

        return ResponseEntity.ok(ApiResponse.success(null, "Client deleted successfully"));
    }
}
