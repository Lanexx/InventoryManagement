package com.fabcoders.exception;

public class InventoryManagementException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4793791757345682997L;

    public InventoryManagementException() {
        super();
    }

    public InventoryManagementException(String message, Throwable error) {
        super(message, error);
    }

    public InventoryManagementException(String message) {
        super(message);
    }

    public InventoryManagementException(Throwable error) {
        super(error);
    }

    
}
