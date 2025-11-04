package com.api.franchise.application.service;

import com.api.franchise.domain.exception.FranchiseHasBranchesException;
import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.entrypoint.dto.request.FranchiseRequestDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import com.api.franchise.application.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final FranchiseMapper franchiseMapper;


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

    public Mono<FranchiseResponseDTO> saveFranchise(FranchiseRequestDTO franchiseDTO){
        return franchiseRepositoryPort.save(franchiseMapper.toEntity(franchiseDTO))
                .map(franchiseMapper::toResponseDto);
    }

    public Mono<FranchiseResponseDTO> updateFranchise(FranchiseRequestDTO franchiseDTO, Long id) {
        return franchiseRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(existingFranchise -> {
                    franchiseMapper.updateEntityFromDTO(franchiseDTO, existingFranchise);
                    return franchiseRepositoryPort.save(existingFranchise);
                })
                .map(franchiseMapper::toResponseDto);
    }

    public Mono<Void> deleteFranchise(Long id) {
        return branchRepositoryPort.findByFranchiseId(id)
                .hasElements()
                .flatMap(hasBranches -> {
                    if (hasBranches) {
                        return Mono.error(new FranchiseHasBranchesException(id));
                    }
                    return franchiseRepositoryPort.findById(id)
                            .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                            .flatMap(f -> franchiseRepositoryPort.deleteById(f.getId()));
                });
    }

    public Mono<FranchiseResponseDTO> updateNameFranchise(Long id, String name){
        return franchiseRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(existingFranchise -> {
                    existingFranchise.setName(name);
                    return franchiseRepositoryPort.save(existingFranchise);
                })
                .map(franchiseMapper::toResponseDto);
    }
}
