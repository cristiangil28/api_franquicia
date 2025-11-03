package com.api.franchise.domain.port;

import com.api.franchise.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepositoryPort {
    Flux<Branch> findAll();
    Mono<Branch> findById(Long id);
    Mono<Branch> save(Branch branch);
    Mono<Void> deleteById(Long id);
    Flux<Branch> findByFranchiseId(Long franchiceId);
}
