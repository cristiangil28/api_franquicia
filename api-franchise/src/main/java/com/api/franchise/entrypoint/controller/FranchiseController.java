package com.api.franchise.entrypoint.controller;

import com.api.franchise.application.service.FranchiseService;
import com.api.franchise.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    @GetMapping
    public Flux<Franchise> getFranchies(){
        return franchiseService.getFranchies();
    }
}
