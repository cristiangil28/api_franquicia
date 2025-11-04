package com.api.franchise.entrypoint.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchRequestDTO {
    private String name;
    private Long franchiseId;
}
