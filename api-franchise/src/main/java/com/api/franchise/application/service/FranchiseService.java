package com.api.franchise.application.service;

import com.api.franchise.domain.model.Franchise;
import com.api.franchise.domain.port.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public Flux<Franchise> getFranchies(){
        return franchiseRepositoryPort.findAll();
    }
}
