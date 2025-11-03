package com.api.franchise.entrypoint.controller;

import com.api.franchise.application.service.FranchiseService;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    @GetMapping
    public Flux<Franchise> getFranchises(){
        return franchiseService.getFranchies();
    }

    @GetMapping("/{id}")
    public Mono<Franchise> getFranchise(@PathVariable Long id){
        return franchiseService.getFranchise(id);
    }

    @GetMapping("/{id}/branches")
    public Mono<FranchiseResponseDTO> getFranchiseWithBranches(@PathVariable Long id){
        return franchiseService.getFranchiseWithBranches(id);
    }
}
