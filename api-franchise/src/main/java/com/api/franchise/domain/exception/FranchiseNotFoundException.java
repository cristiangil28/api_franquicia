package com.api.franchise.domain.exception;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(Long id) {
        super("Franchise not found with id: " + id);
    }
}
