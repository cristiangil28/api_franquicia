package com.api.franchise.application.service;

import com.api.franchise.application.mapper.ProductMapper;
import com.api.franchise.domain.exception.BranchNotFoundException;
import com.api.franchise.domain.exception.ProductExistException;
import com.api.franchise.domain.exception.ProductNotFoundException;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Product;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.ProductRepositoryPort;
import com.api.franchise.entrypoint.dto.request.ProductRequestDTO;
import com.api.franchise.entrypoint.dto.response.ProductResponseDTO;
import com.api.franchise.entrypoint.dto.response.ProductStockResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    // ----------------- create -----------------
    @Test
    void create_ShouldReturnDto_WhenProductDoesNotExist() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Product A");
        dto.setBranchId(1L);
        dto.setStock(10);

        Branch branch = new Branch();
        Product productEntity = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(100L);

        ProductResponseDTO responseDto = new ProductResponseDTO();
        responseDto.setId(100L);

        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.findByBranchId(1L)).thenReturn(Flux.empty());
        when(productMapper.toEntity(dto)).thenReturn(productEntity);
        when(productRepositoryPort.save(productEntity)).thenReturn(Mono.just(savedProduct));
        when(productMapper.toResponseDto(savedProduct)).thenReturn(responseDto);

        StepVerifier.create(productService.create(dto))
                .expectNext(responseDto)
                .verifyComplete();
    }

    @Test
    void create_ShouldThrow_WhenBranchNotFound() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setBranchId(1L);

        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.create(dto))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void create_ShouldThrow_WhenProductExists() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setBranchId(1L);
        dto.setName("Existing Product");

        Branch branch = new Branch();
        Product existingProduct = new Product();
        existingProduct.setName("Existing Product");

        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.findByBranchId(1L)).thenReturn(Flux.just(existingProduct));

        StepVerifier.create(productService.create(dto))
                .expectError(ProductExistException.class)
                .verify();
    }

    // ----------------- findById -----------------
    @Test
    void findById_ShouldReturnDto_WhenProductExists() {
        Product product = new Product();
        product.setId(1L);

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(1L);

        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productMapper.toResponseDto(product)).thenReturn(dto);

        StepVerifier.create(productService.findById(1L))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void findById_ShouldComplete_WhenProductNotFound() {
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.findById(1L))
                .verifyComplete(); // No lanza excepción, retorna Mono vacío
    }

    // ----------------- findAll -----------------
    @Test
    void findAll_ShouldReturnListOfDtos() {
        Product p1 = new Product();
        Product p2 = new Product();

        ProductResponseDTO dto1 = new ProductResponseDTO();
        ProductResponseDTO dto2 = new ProductResponseDTO();

        when(productRepositoryPort.findAll()).thenReturn(Flux.just(p1, p2));
        when(productMapper.toResponseDto(p1)).thenReturn(dto1);
        when(productMapper.toResponseDto(p2)).thenReturn(dto2);

        StepVerifier.create(productService.findAll())
                .expectNext(dto1, dto2)
                .verifyComplete();
    }

    // ----------------- findByBranch -----------------
    @Test
    void findByBranch_ShouldReturnList_WhenBranchExists() {
        Product p1 = new Product();
        ProductResponseDTO dto1 = new ProductResponseDTO();

        when(productRepositoryPort.findByBranchId(1L)).thenReturn(Flux.just(p1));
        when(productMapper.toResponseDto(p1)).thenReturn(dto1);

        StepVerifier.create(productService.findByBranch(1L))
                .expectNext(dto1)
                .verifyComplete();
    }

    @Test
    void findByBranch_ShouldThrow_WhenBranchNotFound() {
        when(productRepositoryPort.findByBranchId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(productService.findByBranch(1L))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    // ----------------- update -----------------
    @Test
    void update_ShouldReturnDto_WhenProductAndBranchExist() {
        Long productId = 1L;
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setBranchId(2L);
        dto.setName("Updated Product");
        dto.setStock(20);

        Product existing = new Product();
        Product updated = new Product();
        updated.setId(productId);
        ProductResponseDTO responseDto = new ProductResponseDTO();
        responseDto.setId(productId);

        Branch branch = new Branch();

        when(productRepositoryPort.findById(productId)).thenReturn(Mono.just(existing));
        when(branchRepositoryPort.findById(2L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.save(existing)).thenReturn(Mono.just(updated));
        when(productMapper.toResponseDto(updated)).thenReturn(responseDto);

        StepVerifier.create(productService.update(productId, dto))
                .expectNext(responseDto)
                .verifyComplete();
    }

    @Test
    void update_ShouldThrow_WhenProductNotFound() {
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.update(1L, new ProductRequestDTO()))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void update_ShouldThrow_WhenBranchNotFound() {
        Product existing = new Product();
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(existing));
        when(branchRepositoryPort.findById(2L)).thenReturn(Mono.empty());

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setBranchId(2L);

        StepVerifier.create(productService.update(1L, dto))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    // ----------------- delete -----------------
    @Test
    void delete_ShouldComplete_WhenProductExists() {
        Product product = new Product();
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.delete(1L))
                .verifyComplete();
    }

    @Test
    void delete_ShouldThrow_WhenProductNotFound() {
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.delete(1L))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    // ----------------- updateStock -----------------
    @Test
    void updateStock_ShouldReturnDto_WhenProductExists() {
        Product product = new Product();
        Product updated = new Product();
        ProductResponseDTO dto = new ProductResponseDTO();

        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.save(product)).thenReturn(Mono.just(updated));
        when(productMapper.toResponseDto(updated)).thenReturn(dto);

        StepVerifier.create(productService.updateStock(1L, 50))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void updateStock_ShouldThrow_WhenProductNotFound() {
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateStock(1L, 50))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    // ----------------- updateNameProduct -----------------
    @Test
    void updateNameProduct_ShouldReturnDto_WhenProductExists() {
        Product product = new Product();
        Product updated = new Product();
        ProductResponseDTO dto = new ProductResponseDTO();

        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.save(product)).thenReturn(Mono.just(updated));
        when(productMapper.toResponseDto(updated)).thenReturn(dto);

        StepVerifier.create(productService.updateNameProduct(1L, "New Name"))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void updateNameProduct_ShouldThrow_WhenProductNotFound() {
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateNameProduct(1L, "New Name"))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    // ----------------- getTopProductPerBranch -----------------
    @Test
    void getTopProductPerBranch_ShouldReturnList() {
        Long franchiseId = 1L;
        Product product = new Product();
        product.setId(10L);
        product.setName("Prod A");
        product.setStock(100);
        product.setBranchId(5L);

        Branch branch = new Branch();
        branch.setId(5L);
        branch.setName("Branch A");

        ProductStockResponseDTO dto = new ProductStockResponseDTO();
        dto.setProductId(10L);

        when(productRepositoryPort.findByFranchiseIdOrderByBranchAndStockDesc(franchiseId))
                .thenReturn(Flux.just(product));
        when(branchRepositoryPort.findById(5L)).thenReturn(Mono.just(branch));

        StepVerifier.create(productService.getTopProductPerBranch(franchiseId))
                .expectNextMatches(ps -> ps.getProductId().equals(10L) && ps.getBranchName().equals("Branch A"))
                .verifyComplete();
    }
}
