package com.api.franchise.application.mapper;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.entrypoint.dto.request.BranchRequestDTO;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {
    public BranchResponseDTO toResponseDto(Branch branch) {
        BranchResponseDTO response = new BranchResponseDTO();
        response.setId(branch.getId());
        response.setName(branch.getName());
        return response;
    }

    public Branch toEntity(BranchRequestDTO branchRequestDTO){
        Branch branch = new Branch();
        branch.setName(branchRequestDTO.getName());
        branch.setFranchiseId(branchRequestDTO.getFranchiseId());
        return branch;
    }
}
