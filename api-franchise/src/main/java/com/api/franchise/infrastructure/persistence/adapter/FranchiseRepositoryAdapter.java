package com.api.franchise.infrastructure.persistence.adapter;

import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.infrastructure.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll();
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseRepository.findById(id);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return null;
    }
}
