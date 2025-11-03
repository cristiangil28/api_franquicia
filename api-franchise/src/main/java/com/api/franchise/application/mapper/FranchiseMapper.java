package com.api.franchise.application.mapper;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import com.api.franchise.entrypoint.dto.request.FranchiseRequestDTO;
import com.api.franchise.entrypoint.dto.response.BranchResponseDTO;
import com.api.franchise.entrypoint.dto.response.FranchiseResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class FranchiseMapper {
    public BranchResponseDTO mapBranchToDto(Branch branch) {
        return new BranchResponseDTO(branch.getId(), branch.getName());
    }

    public FranchiseResponseDTO mapToResponseDto(Franchise franchise, List<BranchResponseDTO> branches) {
        return new FranchiseResponseDTO(franchise.getId(), franchise.getName(), branches);
    }

    public Franchise toEntity(FranchiseRequestDTO franchiseDTO){
        Franchise franchise = new Franchise();
        franchise.setName(franchiseDTO.getName());
        return franchise;
    }

    public FranchiseResponseDTO toResponseDto(Franchise franchise) {
        FranchiseResponseDTO response = new FranchiseResponseDTO();
        response.setId(franchise.getId());
        response.setName(franchise.getName());
        return response;
    }

    public void updateEntityFromDTO(FranchiseRequestDTO franchiseRequestDTO, Franchise franchise){
        if(!Objects.isNull(franchiseRequestDTO) && !Objects.isNull(franchiseRequestDTO.getName())){
            franchise.setName(franchiseRequestDTO.getName());
        }
    }
}
