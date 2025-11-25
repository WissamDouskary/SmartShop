package com.smartshop.shop.controller;

import com.smartshop.shop.dto.ApiResponse;
import com.smartshop.shop.dto.requestDTO.ProductRequestDTO;
import com.smartshop.shop.dto.responseDTO.ProductResponseDTO;
import com.smartshop.shop.service.ProductService;
import com.smartshop.shop.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> products = productService.getAllProducts(pageable);
        if(products.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success(products, "Aucun Produits"));
        }
        return ResponseEntity.ok(ApiResponse.success(products, "Product list retrieved"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProduct(@PathVariable String id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Product details retrieved"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductRequestDTO dto,
            HttpServletRequest request
    ) {
        AdminChecker.checkAdminAccess(request);
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.ok(ApiResponse.success(created, "Product created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequestDTO dto,
            HttpServletRequest request
    ) {
        AdminChecker.checkAdminAccess(request);
        ProductResponseDTO updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String id,
            HttpServletRequest request
    ) {
        AdminChecker.checkAdminAccess(request);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
}