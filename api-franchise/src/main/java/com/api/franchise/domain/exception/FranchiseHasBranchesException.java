package com.api.franchise.domain.exception;

public class FranchiseHasBranchesException extends RuntimeException{

    public FranchiseHasBranchesException (Long franchiseId){
        super("Cannot delete franchise with active branches (franchiseId: " + franchiseId + ")");
    }
}
