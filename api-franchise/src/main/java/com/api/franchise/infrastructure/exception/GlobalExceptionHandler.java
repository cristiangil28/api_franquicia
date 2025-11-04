package com.api.franchise.infrastructure.exception;

import com.api.franchise.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FranchiseNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFranchiseNotFound(FranchiseNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBrancheNotFound(BranchNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FranchiseHasBranchesException.class)
    public ResponseEntity<Map<String, Object>> handleFranchiseHasBranches(FranchiseHasBranchesException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Franchise Has Active Branches",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(BranchExistException.class)
    public ResponseEntity<Map<String,Object>> handleBranchExist(BranchExistException branchExistException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Branch already exists",
                        "message", branchExistException.getMessage()
                ));
    }

    @ExceptionHandler(ProductExistException.class)
    public ResponseEntity<Map<String,Object>> handleProductExist(ProductExistException productExistException){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Product already exists",
                        "message", productExistException.getMessage()
                ));
    }
}
