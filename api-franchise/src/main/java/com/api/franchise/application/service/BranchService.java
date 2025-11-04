package com.api.franchise.application.service;

import com.api.franchise.application.mapper.BranchMapper;
import com.api.franchise.domain.exception.BranchExistException;
import com.api.franchise.domain.exception.BranchNotFoundException;
import com.api.franchise.domain.exception.FranchiseHasBranchesException;
import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.entrypoint.dto.request.BranchRequestDTO;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepositoryPort branchRepositoryPort;
    private final BranchMapper branchMapper;
    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public Mono<BranchResponseDTO> getBranch(Long id){
        return branchRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .map(branchMapper::toResponseDto);
    }

    public Mono<BranchResponseDTO> saveBranch(BranchRequestDTO branchRequestDTO) {
        return franchiseRepositoryPort.findById(branchRequestDTO.getFranchiseId())
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(branchRequestDTO.getFranchiseId())))
                .flatMap(franchise ->
                        branchRepositoryPort.findByFranchiseId(branchRequestDTO.getFranchiseId())
                                .filter(branch -> branch.getName().equalsIgnoreCase(branchRequestDTO.getName()))
                                .hasElements()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BranchExistException(branchRequestDTO.getName()));
                                    }
                                    return branchRepositoryPort.save(branchMapper.toEntity(branchRequestDTO))
                                            .map(branchMapper::toResponseDto);
                                })
                );
    }

    public Mono<BranchResponseDTO> updateBranch(BranchRequestDTO branchRequestDTO, Long id) {
        return branchRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(existingBranch -> {
                    return franchiseRepositoryPort.findById(branchRequestDTO.getFranchiseId())
                            .switchIfEmpty(Mono.error(new FranchiseNotFoundException(branchRequestDTO.getFranchiseId())))
                            .flatMap(franchise -> {
                                existingBranch.setName(branchRequestDTO.getName());
                                existingBranch.setFranchiseId(branchRequestDTO.getFranchiseId());
                                return branchRepositoryPort.save(existingBranch)
                                        .map(branchMapper::toResponseDto);
                            });
                });
    }

    public Mono<Void> deleteBranch(long id){
        return branchRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(branch -> branchRepositoryPort.deleteById(id))
                .then();
    }

    public Mono<BranchResponseDTO> updateNameBranch(Long id, String name){
        return branchRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(existingBranch -> {
                                existingBranch.setName(name);
                                return branchRepositoryPort.save(existingBranch);

                }).map(branchMapper::toResponseDto);
    }
}
