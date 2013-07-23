package com.fabcoders.domain;

import java.util.Date;

public class Stock implements Comparable<Stock>{

    private String epc;
    
    private Date addedOn;
    
    private Date removedOn;
    
    private boolean sold;
    
    private String soldTo;
    
    private Date soldOn;

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
     * @return the addedOn
     */
    public Date getAddedOn() {
        return addedOn;
    }

    /**
     * @param addedOn the addedOn to set
     */
    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    /**
     * @return the removedOn
     */
    public Date getRemovedOn() {
        return removedOn;
    }

    /**
     * @param removedOn the removedOn to set
     */
    public void setRemovedOn(Date removedOn) {
        this.removedOn = removedOn;
    }

    /**
     * @return the soldTo
     */
    public String getSoldTo() {
        return soldTo;
    }

    /**
     * @param soldTo the soldTo to set
     */
    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    /**
     * @return the soldOn
     */
    public Date getSoldOn() {
        return soldOn;
    }

    /**
     * @param soldOn the soldOn to set
     */
    public void setSoldOn(Date soldOn) {
        this.soldOn = soldOn;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((epc == null) ? 0 : epc.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stock other = (Stock) obj;
        if (epc == null) {
            if (other.epc != null)
                return false;
        } else if (!epc.equals(other.epc))
            return false;
        return true;
    }

    @Override
    public int compareTo(Stock o) {
        if(removedOn.after(o.removedOn))
            return -1;
        if(removedOn.before(o.removedOn))
            return 1;
        return 0;
    }

    /**
     * @param sold the sold to set
     */
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    /**
     * @return the sold
     */
    public boolean isSold() {
        return sold;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Stock [addedOn=" + addedOn + ", epc=" + epc + ", removedOn=" + removedOn + ", sold=" + sold + ", soldOn="
                + soldOn + ", soldTo=" + soldTo + "]";
    }
    
    
    
}
