package com.api.franchise.entrypoint.controller;

import com.api.franchise.application.service.BranchService;
import com.api.franchise.entrypoint.dto.request.BranchRequestDTO;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    @GetMapping("/{id}")
    public Mono<BranchResponseDTO> getBranch(@PathVariable Long id){
        return  branchService.getBranch(id);
    }

    @PostMapping("/save-branch")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponseDTO> saveBranch(@RequestBody BranchRequestDTO branchRequestDTO){
        return branchService.saveBranch(branchRequestDTO);
    }

    @PutMapping("/update-branch/{id}")
    public Mono<BranchResponseDTO> updateBranch(@RequestBody BranchRequestDTO branchRequestDTO, @PathVariable Long id){
        return branchService.updateBranch(branchRequestDTO, id);
    }

    @DeleteMapping("delete-branch/{id}")
    public Mono<Void> deleteBranch(@PathVariable Long id){
        return branchService.deleteBranch(id);
    }
}
