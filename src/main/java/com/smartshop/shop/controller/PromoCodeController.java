package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.PromoCodeRequestDTO;
import com.smartshop.shop.dto.responseDTO.PromoCodeResponseDTO;
import com.smartshop.shop.service.PromoCodeService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/promoCode")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<ApiResponse<PromoCodeResponseDTO>> savePromoCode(
            @RequestBody PromoCodeRequestDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        PromoCodeResponseDTO responseDTO = promoCodeService.savePromoCode(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDTO, "PromoCode saved successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PromoCodeResponseDTO>> setPromoCodeInactive(
            @PathVariable("id") String codePromoId,
            HttpServletRequest request
    ){
        AdminChecker.isAdmin(request);

        PromoCodeResponseDTO responseDTO = promoCodeService.MakePromoCodeInActive(codePromoId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDTO, "PromoCode updated successfully!"));
    }
}
