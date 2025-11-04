package com.api.franchise.domain.exception;

public class FranchiseNotFoundException extends RuntimeException {

    private final Long id;

    public FranchiseNotFoundException(Long id) {
        super("Franchise not found with id: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
