package com.fabcoders.domain;

public class Product {
    
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

    // size of dress 
    private String size;

    // collection of dress 
    private String collection;
    
    // description of dress 
    private String description;
    
    // photoPath of dress 
    private String image;
    
    // photoPath of dress 
    private String page;

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
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Product [collection=" + collection + ", color=" + color
                + ", description=" + description + ", image=" + image
                + ", page=" + page + ", productCode=" + productCode
                + ", productGroup=" + productGroup + ", productName="
                + productName + ", sex=" + sex + ", size=" + size + "]";
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getEpc() {
        return epc;
    }

    
}
