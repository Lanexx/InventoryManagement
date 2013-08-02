/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.exception;

/**
 * This is exception class used for throwing application related exception 
 */
public class InventoryManagementException extends Exception {

    private static final long serialVersionUID = 4793791757345682997L;

    public InventoryManagementException() {
        super();
    }

    public InventoryManagementException(String message, Throwable exception) {
        super(message, exception);
    }

    public InventoryManagementException(String message) {
        super(message);
    }

    public InventoryManagementException(Throwable exception) {
        super(exception);
    }

    
}
