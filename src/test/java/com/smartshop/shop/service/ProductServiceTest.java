package com.smartshop.shop.service;

import com.smartshop.shop.dto.requestDTO.ProductRequestDTO;
import com.smartshop.shop.dto.responseDTO.ProductResponseDTO;
import com.smartshop.shop.exception.ResourceNotFoundException;
import com.smartshop.shop.mapper.ProductMapper;
import com.smartshop.shop.model.OrderItem;
import com.smartshop.shop.model.Product;
import com.smartshop.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequestDTO productRequest;
    private ProductResponseDTO productResponse;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId("1");
        product.setNom("Test Product");
        product.setPrixUnitaire(100);
        product.setStockDisponible(50);
        product.setDeleted(false);

        productRequest = new ProductRequestDTO();
        productRequest.setNom("Test Product");
        productRequest.setPrixUnitaire(100);
        productRequest.setStockDisponible(50);

        productResponse = new ProductResponseDTO();
        productResponse.setId("1");
        productResponse.setNom("Test Product");
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponseDTO response = productService.createProduct(productRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("1");

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAllByDeletedFalse(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo("1");
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponseDTO result = productService.getProductById("1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findByIdAndDeletedFalse("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById("99");
        });
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponseDTO result = productService.updateProduct("1", productRequest);

        assertThat(result).isNotNull();
        assertThat(product.getNom()).isEqualTo(productRequest.getNom());
        assertThat(product.getPrixUnitaire()).isEqualTo(productRequest.getPrixUnitaire());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct("99", productRequest);
        });
    }

    @Test
    void testDeleteProduct_WhenProductHasNoOrders_ShouldDelete() {
        product.setOrderItems(Collections.emptyList());

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        productService.deleteProduct("1");

        verify(productRepository, times(1)).delete(product);
        verify(productRepository, never()).save(product);
    }

    @Test
    void testDeleteProduct_WhenProductHasOrders_ShouldSoftDelete() {
        product.setOrderItems(List.of(new OrderItem()));

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        productService.deleteProduct("1");

        assertThat(product.getDeleted()).isTrue();
        verify(productRepository, times(1)).save(product);
        verify(productRepository, never()).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct("99");
        });
    }
}
