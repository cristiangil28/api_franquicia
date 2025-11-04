package com.api.franchise.application.service;

import com.api.franchise.application.mapper.BranchMapper;
import com.api.franchise.domain.exception.BranchExistException;
import com.api.franchise.domain.exception.BranchNotFoundException;
import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import com.api.franchise.entrypoint.dto.request.BranchRequestDTO;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @InjectMocks
    private BranchService branchService;

    @Test
    void getBranch_ShouldReturnDto_WhenBranchExists() {
        Long id = 1L;
        Branch branch = new Branch();
        branch.setId(id);

        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(id);

        when(branchRepositoryPort.findById(id)).thenReturn(Mono.just(branch));
        when(branchMapper.toResponseDto(branch)).thenReturn(dto);

        StepVerifier.create(branchService.getBranch(id))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void getBranch_ShouldThrow_WhenBranchNotFound() {
        Long id = 1L;
        when(branchRepositoryPort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.getBranch(id))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void saveBranch_ShouldReturnDto_WhenBranchDoesNotExist() {
        BranchRequestDTO requestDTO = new BranchRequestDTO("Poblado Centro",1L);
        requestDTO.setName("Branch 1");
        requestDTO.setFranchiseId(1L);

        Franchise franchise = new Franchise();
        Branch branchEntity = new Branch();
        Branch savedBranch = new Branch();
        savedBranch.setId(10L);

        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(10L);

        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(1L)).thenReturn(Flux.empty());
        when(branchMapper.toEntity(requestDTO)).thenReturn(branchEntity);
        when(branchRepositoryPort.save(branchEntity)).thenReturn(Mono.just(savedBranch));
        when(branchMapper.toResponseDto(savedBranch)).thenReturn(dto);

        StepVerifier.create(branchService.saveBranch(requestDTO))
                .expectNext(dto)
                .verifyComplete();
    }

    void saveBranch_ShouldThrow_WhenFranchiseNotFound() {
        BranchRequestDTO requestDTO = new BranchRequestDTO("Poblado Centro",1L);
        requestDTO.setFranchiseId(1L);

        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.saveBranch(requestDTO))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void saveBranch_ShouldThrow_WhenBranchExists() {
        BranchRequestDTO requestDTO = new BranchRequestDTO("Poblado Centro",1L);
        requestDTO.setName("Branch 1");
        requestDTO.setFranchiseId(1L);

        Franchise franchise = new Franchise();
        Branch existingBranch = new Branch();
        existingBranch.setName("Branch 1");

        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.findByFranchiseId(1L)).thenReturn(Flux.just(existingBranch));

        StepVerifier.create(branchService.saveBranch(requestDTO))
                .expectError(BranchExistException.class)
                .verify();
    }

    @Test
    void updateBranch_ShouldReturnDto_WhenBranchAndFranchiseExist() {
        Long branchId = 1L;
        BranchRequestDTO requestDTO = new BranchRequestDTO("Poblado Centro",1L);
        requestDTO.setName("New Branch");
        requestDTO.setFranchiseId(2L);

        Branch existingBranch = new Branch();
        existingBranch.setId(branchId);

        Franchise franchise = new Franchise();

        Branch updatedBranch = new Branch();
        updatedBranch.setId(branchId);

        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(branchId);

        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.just(existingBranch));
        when(franchiseRepositoryPort.findById(2L)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.save(existingBranch)).thenReturn(Mono.just(updatedBranch));
        when(branchMapper.toResponseDto(updatedBranch)).thenReturn(dto);

        StepVerifier.create(branchService.updateBranch(requestDTO, branchId))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void updateBranch_ShouldThrow_WhenBranchNotFound() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.updateBranch(new BranchRequestDTO("Poblado Centro",1L), 1L))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void updateBranch_ShouldThrow_WhenFranchiseNotFound() {
        Long branchId = 1L;
        BranchRequestDTO requestDTO = new BranchRequestDTO("Poblado Centro",1L);
        requestDTO.setFranchiseId(2L);

        Branch existingBranch = new Branch();

        when(branchRepositoryPort.findById(branchId)).thenReturn(Mono.just(existingBranch));
        when(franchiseRepositoryPort.findById(2L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.updateBranch(requestDTO, branchId))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void deleteBranch_ShouldComplete_WhenBranchExists() {
        Long id = 1L;
        Branch branch = new Branch();

        when(branchRepositoryPort.findById(id)).thenReturn(Mono.just(branch));
        when(branchRepositoryPort.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.deleteBranch(id))
                .verifyComplete();
    }

    @Test
    void deleteBranch_ShouldThrow_WhenBranchNotFound() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.deleteBranch(1L))
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void updateNameBranch_ShouldReturnDto_WhenBranchExists() {
        Long id = 1L;
        String newName = "Updated Name";

        Branch existingBranch = new Branch();
        existingBranch.setId(id);

        Branch updatedBranch = new Branch();
        updatedBranch.setId(id);

        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(id);

        when(branchRepositoryPort.findById(id)).thenReturn(Mono.just(existingBranch));
        when(branchRepositoryPort.save(existingBranch)).thenReturn(Mono.just(updatedBranch));
        when(branchMapper.toResponseDto(updatedBranch)).thenReturn(dto);

        StepVerifier.create(branchService.updateNameBranch(id, newName))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void updateNameBranch_ShouldThrow_WhenBranchNotFound() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.updateNameBranch(1L, "Name"))
                .expectError(BranchNotFoundException.class)
                .verify();
    }
}
