package com.api.franchise.entrypoint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BranchResponseDTO {
    private Long id;
    private String name;
}
