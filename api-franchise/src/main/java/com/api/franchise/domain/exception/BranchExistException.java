package com.api.franchise.domain.exception;

public class BranchExistException extends RuntimeException {
    public BranchExistException(String  name) {
        super("Branch already exists in this franchise with name: "+name);
    }
}
