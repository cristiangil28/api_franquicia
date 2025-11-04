package com.api.franchise.infrastructure.persistence.adapter;

import com.api.franchise.domain.model.Product;
import com.api.franchise.domain.port.ProductRepositoryPort;
import com.api.franchise.infrastructure.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return productRepository.findByBranchId(branchId);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productRepository.deleteById(id);
    }

    @Override
    public Flux<Product> findByFranchiseIdOrderByBranchAndStockDesc(Long franchiseId) {
        return productRepository.findTopProductPerBranchByFranchise(franchiseId);
    }
}