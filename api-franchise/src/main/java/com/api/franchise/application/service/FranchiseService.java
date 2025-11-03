package com.api.franchise.application.service;

import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import com.api.franchise.infrastructure.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final FranchiseMapper franchiseMapper;

    public Flux<Franchise> getFranchies(){
        return franchiseRepositoryPort.findAll();
    }

    public Mono<Franchise> getFranchise(Long id){
        return franchiseRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)));
    }

    public Mono<FranchiseResponseDTO> getFranchiseWithBranches(Long id) {
        return franchiseRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(franchise ->
                        branchRepositoryPort.findByFranchiseId(franchise.getId())
                                .map(franchiseMapper::mapBranchToDto)
                                .collectList()
                                .map(branches ->franchiseMapper.mapToResponseDto(franchise, branches))
                );
    }
}
