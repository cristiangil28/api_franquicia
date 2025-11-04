package com.api.franchise.application.mapper;

import com.api.franchise.domain.model.Product;
import com.api.franchise.entrypoint.dto.request.ProductRequestDTO;
import com.api.franchise.entrypoint.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setStock(dto.getStock());
        product.setBranchId(dto.getBranchId());
        return product;
    }

    public ProductResponseDTO toResponseDto(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getStock(), product.getBranchId());
    }

    public void updateEntityFromDto(ProductRequestDTO dto, Product product) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getStock() != null) product.setStock(dto.getStock());
        if (dto.getBranchId() != null) product.setBranchId(dto.getBranchId());
    }
}
