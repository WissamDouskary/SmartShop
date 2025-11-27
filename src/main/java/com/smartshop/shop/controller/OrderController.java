package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.OrderRequestDTO;
import com.smartshop.shop.dto.responseDTO.OrderResponseDTO;
import com.smartshop.shop.enums.Role;
import com.smartshop.shop.exception.AccessDeniedException;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.model.Client;
import com.smartshop.shop.repository.ClientRepository;
import com.smartshop.shop.service.OrderService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @RequestBody OrderRequestDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        OrderResponseDTO responseDTO = orderService.createOrder(dto);

        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Order Created successfully"));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getClientHistory(
            @PathVariable String clientId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if(session == null){
            throw new AccessDeniedException("Vous devez être connecté.");
        }

        String currentUserId = (String) session.getAttribute("USER_ID");
        Role currentUserRole = (Role) session.getAttribute("USER_ROLE");

        if (currentUserId == null) {
            throw new AccessDeniedException("Vous devez être connecté.");
        }

        if (currentUserRole == Role.CLIENT) {
            Client targetClient = clientRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

            if (!targetClient.getUser().getId().equals(currentUserId)) {
                throw new AccessDeniedException("Vous n'avez pas le droit de voir les commandes d'un autre client.");
            }
        }

        List<OrderResponseDTO> history = orderService.getOrdersByClient(clientId);

        if(history.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success(history, "Historique des commandes récupéré"));
        }

        return ResponseEntity.ok(ApiResponse.success(history, "Historique des commandes récupéré"));
    }

    @PutMapping("{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> confirmOrderAfterPayment(
            @PathVariable("id") String id,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        OrderResponseDTO responseDTO = orderService.confirmOrderAfterCompletingPayment(id);

        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Order updated successfully!"));
    }
}
