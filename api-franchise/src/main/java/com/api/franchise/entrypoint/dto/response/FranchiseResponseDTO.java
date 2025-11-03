package com.api.franchise.entrypoint.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseResponseDTO {
    private Long id;
    private String name;
    private List<BranchResponseDTO> branches;
}
