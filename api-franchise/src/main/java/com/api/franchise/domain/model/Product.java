package com.api.franchise.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "product")
public class Product {

    @Id
    private Long id;
    private String name;
    private Integer stock;
    @Column("branch_id")
    private Long branchId;
}
