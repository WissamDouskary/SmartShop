package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.ClientRequestDTO;
import com.smartshop.shop.dto.responseDTO.ClientResponseDTO;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.AccessDeniedException;
import com.smartshop.shop.model.Client;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.service.ClientService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientRepository clientRepository;

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
    public ResponseEntity<ApiResponse<ClientResponseDTO>> updateClient(
            @PathVariable("id") String id,
            @RequestBody ClientRequestDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        ClientResponseDTO responseDTO = clientService.updateClient(id, dto);

        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Client updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> deleteClient(
            @PathVariable("id") String id,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        clientService.deleteClient(id);

        return ResponseEntity.ok(ApiResponse.success(null, "Client deleted successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> ClientProfile(
            HttpServletRequest request
    ){

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("You must be logged in.");
        }

        String user_id = (String) session.getAttribute("USER_ID");
        Role user_role = (Role) session.getAttribute("USER_ROLE");

        if(user_role.equals(Role.ADMIN)){
            throw new AccessDeniedException("Just client who can see profiles!");
        }

        Client client = clientRepository.findClientByUser_Id(user_id);
        ClientResponseDTO responseDTO = clientService.clientProfile(client.getId());

        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Your Profile"));
    }
}
