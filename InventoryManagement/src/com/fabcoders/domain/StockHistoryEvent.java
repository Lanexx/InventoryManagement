/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.domain;

import java.util.Date;
/**
 * This class is object for storing history information 
 * @author Administrator
 */
public class StockHistoryEvent {

    /**
     * epc value of item
     */
    private String epc;
    
    /**
     * item on which operation is done
     */
    private Product product;
    
    /**
     * operation performed
     */
    private String operation;
    
    /**
     * time on which operation is performed
     */
    private Date operationOn;

    /**
     * @return the epc
     */
    public String getEpc() {
        return epc;
    }

    /**
     * @param epc the epc to set
     */
    public void setEpc(String epc) {
        this.epc = epc;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return the operationOn
     */
    public Date getOperationOn() {
        return operationOn;
    }

    /**
     * @param operationOn the operationOn to set
     */
    public void setOperationOn(Date operationOn) {
        this.operationOn = operationOn;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StockHistoryEvent [epc=" + epc + ", operation=" + operation
                + ", operationOn=" + operationOn + "]";
    }

}
