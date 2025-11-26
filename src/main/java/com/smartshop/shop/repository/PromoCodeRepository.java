package com.smartshop.shop.repository;

import com.smartshop.shop.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    Optional<PromoCode> findPromoCodeByCode(String code);
}
