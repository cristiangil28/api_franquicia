package com.api.franchise.entrypoint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockResponseDTO {
    private Long productId;
    private String productName;
    private int stock;
    private Long branchId;
    private String branchName;
}
