package com.api.franchise.infrastructure.mapper;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FranchiseMapper {
    public BranchResponseDTO mapBranchToDto(Branch branch) {
        return new BranchResponseDTO(branch.getId(), branch.getName());
    }

    public FranchiseResponseDTO mapToResponseDto(Franchise franchise, List<BranchResponseDTO> branches) {
        return new FranchiseResponseDTO(franchise.getId(), franchise.getName(), branches);
    }
}
