package com.api.franchise.application.service;

import com.api.franchise.application.mapper.ProductMapper;
import com.api.franchise.domain.exception.BranchNotFoundException;
import com.api.franchise.domain.exception.FranchiseNotFoundException;
import com.api.franchise.domain.exception.ProductExistException;
import com.api.franchise.domain.exception.ProductNotFoundException;
import com.api.franchise.domain.model.Product;
import com.api.franchise.domain.port.BranchRepositoryPort;
import com.api.franchise.domain.port.ProductRepositoryPort;
import com.api.franchise.entrypoint.dto.request.ProductRequestDTO;
import com.api.franchise.entrypoint.dto.response.ProductResponseDTO;
import com.api.franchise.entrypoint.dto.response.ProductStockResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryPort productRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductMapper productMapper;

    public Mono<ProductResponseDTO> create(ProductRequestDTO dto) {

        return branchRepositoryPort.findById(dto.getBranchId())
                .switchIfEmpty(Mono.error(new BranchNotFoundException(dto.getBranchId())))
                .flatMap(branch ->
                        productRepositoryPort.findByBranchId(dto.getBranchId())
                                .filter(product -> product.getName().equalsIgnoreCase(dto.getName()))
                                .hasElements()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new ProductExistException(dto.getName()));
                                    }
                                    return productRepositoryPort.save(productMapper.toEntity(dto))
                                            .map(productMapper::toResponseDto);
                                }));
    }

    public Mono<ProductResponseDTO> findById(Long id) {
        return productRepositoryPort.findById(id)
                .map(productMapper::toResponseDto);
    }

    public Flux<ProductResponseDTO> findAll() {
        return productRepositoryPort.findAll()
                .map(productMapper::toResponseDto);
    }

    public Flux<ProductResponseDTO> findByBranch(Long branchId) {
        return productRepositoryPort.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(productMapper::toResponseDto);
    }

    public Mono<ProductResponseDTO> update(Long id, ProductRequestDTO dto) {
        return productRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(existing -> {
                    return branchRepositoryPort.findById(dto.getBranchId())
                                    .switchIfEmpty(Mono.error(new BranchNotFoundException(dto.getBranchId())))
                                            .flatMap(branch -> {
                                                existing.setName(dto.getName());
                                                existing.setStock(dto.getStock());
                                                existing.setBranchId(dto.getBranchId());
                                                productMapper.updateEntityFromDto(dto, existing);
                                                return productRepositoryPort.save(existing)
                                                        .map(productMapper::toResponseDto);
                                            });


                });
    }

    public Mono<Void> delete(Long id) {
        return productRepositoryPort.findById(id)
                        .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                                .flatMap(product ->productRepositoryPort.deleteById(id));
    }

    public Mono<ProductResponseDTO> updateStock(Long id, int stock){
        return productRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setStock(stock);
                    return productRepositoryPort.save(product)
                            .map(productMapper::toResponseDto);
                });
    }

    public Mono<ProductResponseDTO> updateNameProduct(Long id, String name){
        return productRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setName(name);
                    return productRepositoryPort.save(product)
                            .map(productMapper::toResponseDto);
                });
    }

    public Flux<ProductStockResponseDTO> getTopProductPerBranch(Long franchiseId) {
        return productRepositoryPort.findByFranchiseIdOrderByBranchAndStockDesc(franchiseId)
                .flatMap(product -> branchRepositoryPort.findById(product.getBranchId())
                        .map(branch -> new ProductStockResponseDTO(
                                product.getId(),
                                product.getName(),
                                product.getStock(),
                                branch.getId(),
                                branch.getName()
                        ))
                );
    }
}
