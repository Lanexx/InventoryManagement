/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.domain;

import java.io.Serializable;

/**
 * This class is Object class. this class represents Product or Item with whom
 * we are dealing with
 */
public class Product implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4450017566946119775L;

    // unique id for product
    private String productCode;
    
    // name for product
    private String productName;
    
    // category to which this belong 
    private String productGroup;

    // product color
    private String color;
    
    // sex to which product belong
    private String sex;

    // size of Product 
    private String size;

    // collection for Product 
    private String collection;
    
    // description of Product 
    private String description;
    
    // photoPath of dress 
    private String image;
    
    // epcCode of Product 
    private String epc;
    
    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productGroup
     */
    public String getProductGroup() {
        return productGroup;
    }

    /**
     * @param productGroup the productGroup to set
     */
    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the collection
     */
    public String getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the EPC Code of the product
     */
    public String getEpc() {
        return epc;
    }

    /**
     * @param epc the EPC code to set for product
     */
    public void setEpc(String epc) {
        this.epc = epc;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((collection == null) ? 0 : collection.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((epc == null) ? 0 : epc.hashCode());
        result = prime * result
                + ((productCode == null) ? 0 : productCode.hashCode());
        result = prime * result
                + ((productGroup == null) ? 0 : productGroup.hashCode());
        result = prime * result
                + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
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
        Product other = (Product) obj;
        if (collection == null) {
            if (other.collection != null)
                return false;
        } else if (!collection.equals(other.collection))
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (epc == null) {
            if (other.epc != null)
                return false;
        } else if (!epc.equals(other.epc))
            return false;
        if (productCode == null) {
            if (other.productCode != null)
                return false;
        } else if (!productCode.equals(other.productCode))
            return false;
        if (productGroup == null) {
            if (other.productGroup != null)
                return false;
        } else if (!productGroup.equals(other.productGroup))
            return false;
        if (productName == null) {
            if (other.productName != null)
                return false;
        } else if (!productName.equals(other.productName))
            return false;
        if (sex == null) {
            if (other.sex != null)
                return false;
        } else if (!sex.equals(other.sex))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Product [collection=" + collection + ", color=" + color
                + ", description=" + description + ", epc=" + epc + ", image="
                + image + ", productCode=" + productCode
                + ", productGroup=" + productGroup + ", productName="
                + productName + ", sex=" + sex + ", size=" + size + "]";
    }
}
