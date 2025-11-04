package com.api.franchise.domain.port;

import com.api.franchise.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {

    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Flux<Product> findAll();
    Flux<Product> findByBranchId(Long branchId);
    Mono<Void> deleteById(Long id);
    Flux<Product>findByFranchiseIdOrderByBranchAndStockDesc(Long franchiseId);
}
