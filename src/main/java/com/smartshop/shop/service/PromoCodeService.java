package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.PromoCodeRequestDTO;
import com.smartshop.shop.dto.responseDTO.PromoCodeResponseDTO;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.PromoCodeMapper;
import com.smartshop.shop.model.PromoCode;
import com.smartshop.shop.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    public PromoCodeResponseDTO savePromoCode(PromoCodeRequestDTO dto){
        PromoCode savedPromoCode = promoCodeRepository.save(promoCodeMapper.toEntity(dto));
        return promoCodeMapper.toResponse(savedPromoCode);
    }

    public PromoCodeResponseDTO MakePromoCodeInActive(String promoCodeId){
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId).orElseThrow(
                () -> new ResourceNotFoundException("Aucun Promo Code avec id: "+promoCodeId)
        );
        promoCode.setActive(false);
        return promoCodeMapper.toResponse(promoCode);
    }
}
