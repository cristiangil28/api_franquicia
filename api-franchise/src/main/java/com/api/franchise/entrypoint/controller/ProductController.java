package com.api.franchise.entrypoint.controller;

import com.api.franchise.application.service.ProductService;
import com.api.franchise.entrypoint.dto.request.ProductRequestDTO;
import com.api.franchise.entrypoint.dto.response.ProductResponseDTO;
import com.api.franchise.entrypoint.dto.response.ProductStockResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save-product")
    public Mono<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) {
        return productService.create(dto);
    }

    @GetMapping
    public Flux<ProductResponseDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ProductResponseDTO> findById(@PathVariable Long id){
        return productService.findById(id);
    }

    @PutMapping("/update-product/{id}")
    public Mono<ProductResponseDTO> updateBranch(@RequestBody ProductRequestDTO dto, @PathVariable Long id){
        return productService.update(id, dto);
    }

    @DeleteMapping("/delete-product/{id}")
    public Mono<Void> deleteBranch(@PathVariable Long id){
        return productService.delete(id)
                .then();
    }

    @PutMapping("/update-stock/{id}")
    public Mono<ProductResponseDTO> updateStock(@PathVariable Long id, @RequestParam int stock){
        return productService.updateStock(id,stock);
    }

    @PutMapping("/update-name-product/{id}")
    public Mono<ProductResponseDTO> updateNameProduct(@PathVariable Long id,@NotBlank @RequestParam String name){
        return productService.updateNameProduct(id,name);
    }

    @GetMapping("/top-stock/{franchiseId}")
    public Flux<ProductStockResponseDTO> getTopProductPerBranch(@PathVariable Long franchiseId){
        return productService.getTopProductPerBranch(franchiseId);
    }
}
