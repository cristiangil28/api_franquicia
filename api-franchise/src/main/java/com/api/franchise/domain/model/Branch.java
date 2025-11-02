package com.api.franchise.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "branch")
public class Branch {

    @Id
    private Long id;
    private String name;
    @Column("franchise_id")
    private Long franchiseId;
}
