package com.api.franchise.domain.exception;

public class ProductExistException extends RuntimeException {
    public ProductExistException(String  name) {
        super("Product already exists in this franchise with name: "+name);
    }
}
