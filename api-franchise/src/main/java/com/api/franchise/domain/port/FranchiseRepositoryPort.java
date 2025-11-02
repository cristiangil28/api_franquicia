package com.api.franchise.domain.port;


import com.api.franchise.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryPort {
    Flux<Franchise> findAll();
    Mono<Franchise> findById(Long id);
    Mono<Franchise> save(Franchise franchise);
    Mono<Void> deleteById(Long id);
}
