package com.api.franchise.infrastructure.persistence.adapter;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.infrastructure.persistence.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private final BranchRepository branchRepository;

    @Override
    public Flux<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return branchRepository.findById(id);
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return branchRepository.deleteById(id);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId);
    }
}
