package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.PaymentRequestDTO;
import com.smartshop.shop.dto.responseDTO.PaymentResponseDTO;
import com.smartshop.shop.service.PaymentService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> payOrder(
            @Valid @RequestBody PaymentRequestDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        PaymentResponseDTO responseDTO = paymentService.payOrder(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDTO, "Payment is created successfully!"));
    }
}
