package com.api.franchise.entrypoint.controller;

import com.api.franchise.application.service.FranchiseService;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.entrypoint.dto.request.FranchiseRequestDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    @GetMapping("/{id}/branches")
    public Mono<FranchiseResponseDTO> getFranchiseWithBranches(@PathVariable Long id){
        return franchiseService.getFranchiseWithBranches(id);
    }
    @PostMapping("/save-franchise")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponseDTO> saveFranchise(@RequestBody FranchiseRequestDTO franchiseDTO){
        return franchiseService.saveFranchise(franchiseDTO);
    }

    @PutMapping("/update-franchise/{id}")
    public Mono<FranchiseResponseDTO> updateFranchise(@RequestBody FranchiseRequestDTO franchiseRequestDTO, @PathVariable Long id){
        return franchiseService.updateFranchise(franchiseRequestDTO,id);
    }

    @DeleteMapping("/delete-franchise/{id}")
    public Mono<Void> deleteFranchise(@PathVariable Long id){
        return franchiseService.deleteFranchise(id);
    }
}
