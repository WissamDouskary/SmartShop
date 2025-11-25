package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.ProductRequestDTO;
import com.smartshop.shop.dto.responseDTO.ProductResponseDTO;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.ProductMapper;
import com.smartshop.shop.model.Product;
import com.smartshop.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        product.setDeleted(false);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable)
                .map(productMapper::toResponse);
    }

    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existingProduct.setNom(dto.getNom());
        existingProduct.setPrixUnitaire(dto.getPrixUnitaire());
        existingProduct.setStockDisponible(dto.getStockDisponible());

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean hasOrders = product.getOrderItems() != null && !product.getOrderItems().isEmpty();

        if (hasOrders) {
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }
    }
}