package com.api.franchise.entrypoint.dto.response;

import com.api.franchise.domain.model.Branch;
import com.api.franchise.domain.model.Franchise;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FranchiseWithBranches {
    private Franchise franchise;
    private List<Branch> branches;
}
