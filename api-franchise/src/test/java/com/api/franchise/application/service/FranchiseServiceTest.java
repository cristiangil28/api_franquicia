package com.api.franchise.application.service;

import com.api.franchise.application.mapper.FranchiseMapper;
import com.api.franchise.domain.exception.FranchiseHasBranchesException;
import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.entrypoint.dto.request.FranchiseRequestDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private FranchiseMapper franchiseMapper;

    @InjectMocks
    private FranchiseService franchiseService;

    @Test
    void getFranchiseWithBranches_ShouldReturnDto_WhenFranchiseExists() {
        Long franchiseId = 1L;
        Franchise franchise = new Franchise();
        franchise.setId(franchiseId);
        Branch branch = new Branch();
        branch.setId(10L);

        com.api.franchise.entrypoint.dto.response.BranchResponseDTO branchDTO =
                new com.api.franchise.entrypoint.dto.response.BranchResponseDTO();
        FranchiseResponseDTO dto = new FranchiseResponseDTO();
        dto.setId(franchiseId);

        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch));
        when(franchiseMapper.mapBranchToDto(branch)).thenReturn(branchDTO);
        when(franchiseMapper.mapToResponseDto(franchise, List.of(branchDTO))).thenReturn(dto);

        StepVerifier.create(franchiseService.getFranchiseWithBranches(franchiseId))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void getFranchiseWithBranches_ShouldThrow_WhenFranchiseNotFound() {
        Long franchiseId = 1L;
        when(franchiseRepositoryPort.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.getFranchiseWithBranches(franchiseId))
                .expectErrorMatches(e -> e instanceof FranchiseNotFoundException &&
                        ((FranchiseNotFoundException) e).getId().equals(franchiseId))
                .verify();
    }

    @Test
    void saveFranchise_ShouldReturnSavedDto() {
        FranchiseRequestDTO requestDTO = new FranchiseRequestDTO("Poblado");
        requestDTO.setName("Franquicia 1");

        Franchise entity = new Franchise();
        entity.setName("Franquicia 1");

        Franchise savedEntity = new Franchise();
        savedEntity.setId(1L);
        savedEntity.setName("Franquicia 1");

        FranchiseResponseDTO responseDTO = new FranchiseResponseDTO();
        responseDTO.setId(1L);

        when(franchiseMapper.toEntity(requestDTO)).thenReturn(entity);
        when(franchiseRepositoryPort.save(entity)).thenReturn(Mono.just(savedEntity));
        when(franchiseMapper.toResponseDto(savedEntity)).thenReturn(responseDTO);

        StepVerifier.create(franchiseService.saveFranchise(requestDTO))
                .expectNext(responseDTO)
                .verifyComplete();
    }

    @Test
    void updateFranchise_ShouldReturnUpdatedDto_WhenFranchiseExists() {
        Long id = 1L;
        FranchiseRequestDTO requestDTO = new FranchiseRequestDTO("Poblado");
        requestDTO.setName("Nueva Franquicia");

        Franchise existing = new Franchise();
        existing.setId(id);
        existing.setName("Old Name");

        Franchise updated = new Franchise();
        updated.setId(id);
        updated.setName("Nueva Franquicia");

        FranchiseResponseDTO dto = new FranchiseResponseDTO();
        dto.setId(id);
        dto.setName("Nueva Franquicia");

        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.just(existing));
        doNothing().when(franchiseMapper).updateEntityFromDTO(requestDTO, existing);
        when(franchiseRepositoryPort.save(existing)).thenReturn(Mono.just(updated));
        when(franchiseMapper.toResponseDto(updated)).thenReturn(dto);

        StepVerifier.create(franchiseService.updateFranchise(requestDTO, id))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void updateFranchise_ShouldThrow_WhenFranchiseNotFound() {
        Long id = 1L;
        FranchiseRequestDTO requestDTO = new FranchiseRequestDTO("Poblado");
        requestDTO.setName("Nueva Franquicia");

        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.updateFranchise(requestDTO, id))
                .expectErrorMatches(e -> e instanceof FranchiseNotFoundException &&
                        ((FranchiseNotFoundException) e).getId().equals(id))
                .verify();
    }

    @Test
    void updateNameFranchise_ShouldReturnUpdatedDto_WhenFranchiseExists() {
        Long id = 1L;
        String newName = "Nueva Franquicia";

        Franchise existing = new Franchise();
        existing.setId(id);
        existing.setName("Old Name");

        Franchise updated = new Franchise();
        updated.setId(id);
        updated.setName(newName);

        FranchiseResponseDTO dto = new FranchiseResponseDTO();
        dto.setId(id);
        dto.setName(newName);

        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.just(existing));
        when(franchiseRepositoryPort.save(existing)).thenReturn(Mono.just(updated));
        when(franchiseMapper.toResponseDto(updated)).thenReturn(dto);

        StepVerifier.create(franchiseService.updateNameFranchise(id, newName))
                .expectNextMatches(r -> r.getName().equals(newName) && r.getId().equals(id))
                .verifyComplete();
    }

    @Test
    void updateNameFranchise_ShouldThrow_WhenFranchiseNotFound() {
        Long id = 1L;
        String newName = "Nueva Franquicia";

        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.updateNameFranchise(id, newName))
                .expectErrorMatches(e -> e instanceof FranchiseNotFoundException &&
                        ((FranchiseNotFoundException) e).getId().equals(id))
                .verify();
    }

    @Test
    void deleteFranchise_ShouldDelete_WhenNoBranches() {
        Long id = 1L;
        Franchise franchise = new Franchise();
        franchise.setId(id);

        when(branchRepositoryPort.findByFranchiseId(id)).thenReturn(Flux.empty());
        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.just(franchise));
        when(franchiseRepositoryPort.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.deleteFranchise(id))
                .verifyComplete();
    }

    @Test
    void deleteFranchise_ShouldThrow_WhenHasBranches() {
        Long id = 1L;
        Branch branch = new Branch();

        when(branchRepositoryPort.findByFranchiseId(id)).thenReturn(Flux.just(branch));

        StepVerifier.create(franchiseService.deleteFranchise(id))
                .expectError(FranchiseHasBranchesException.class)
                .verify();
    }

    @Test
    void deleteFranchise_ShouldThrow_WhenFranchiseNotFound() {
        Long id = 1L;

        when(branchRepositoryPort.findByFranchiseId(id)).thenReturn(Flux.empty());
        when(franchiseRepositoryPort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.deleteFranchise(id))
                .expectErrorMatches(e -> e instanceof FranchiseNotFoundException &&
                        ((FranchiseNotFoundException) e).getId().equals(id))
                .verify();
    }

}
