package com.api.franchise.entrypoint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FranchiseResponseDTO {
    private Long id;
    private String name;
    private List<BranchResponseDTO> branches;
}
