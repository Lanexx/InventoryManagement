/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.domain.Product;
import com.fabcoders.exception.InventoryManagementException;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
/**
 * This is performs all the operation relating persistence of Product related data 
 * data is stored in RDF format and is accessible through SPARQL end point  
 */
public class ProductRDFOperations {

    private static Log log = LogFactory.getLog(ProductRDFOperations.class);

    private static String ENDPOINT_URL = ConfigManager.PRODUCT_SPARQL_URL;
    private static String USERNAME = ConfigManager.ENDPOINT_USERNAME;
    private static String PASSWORD = ConfigManager.ENDPOINT_PASSWORD;
    
    // prefix for all queries    
    private static final String prefix =
        "PREFIX gr: <http://purl.org/goodrelations/v1#> "
      + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
      + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
      + "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
      + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
      + "PREFIX dct: <http://purl.org/dc/terms/> "
      + "PREFIX product: <http://www.example.com/product-ns.rdf#> ";

    //private static Map<String, Product> loadedProductMap = new HashMap<String, Product>();

    public static String create(Product product) throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.create");       

        Product oldProduct = getForProductCode(product.getProductCode());
        
        if(null != oldProduct){
            throw new InventoryManagementException("Product with same code already exists");
        }
        
        String productURI = "<http://localhost:8080/resource/product/"+product.getProductCode()+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("INSERT DATA { ");
        updateString.append(productURI + " rdf:type gr:ProductOrServiceModel ; ");
        if(null != product.getProductName() && !"".equals(product.getProductName()))
            updateString.append(" gr:name \""+product.getProductName()+"\"^^xsd:string ;");
        if(null != product.getProductCode() && !"".equals(product.getProductCode()))
            updateString.append(" gr:serialNumber \""+product.getProductCode()+"\"^^xsd:string ;");
        if(null != product.getProductGroup() && !"".equals(product.getProductGroup()))
            updateString.append(" gr:category \""+product.getProductGroup()+"\"^^xsd:string ;");
        if(null != product.getColor() && !"".equals(product.getColor()))
            updateString.append(" gr:color \""+product.getColor()+"\"^^xsd:string ;");
        if(null != product.getImage() && !"".equals(product.getImage()))
            updateString.append(" foaf:depiction <"+product.getImage()+"> ;");
        if(null != product.getCollection() && !"".equals(product.getCollection()))
            updateString.append(" dct:isPartOf \""+product.getCollection()+"\"^^xsd:string ;");
        if(null != product.getSex() && !"".equals(product.getSex()))
            updateString.append(" product:sex \""+product.getSex()+"\"^^xsd:string ;");
        if(null != product.getSize() && !"".equals(product.getSize()))
            updateString.append(" product:size \""+product.getSize()+"\"^^xsd:string ;");
        if(null != product.getDescription() && !"".equals(product.getDescription()))
            updateString.append(" gr:description \""+product.getDescription()+"\"^^xsd:string ; ");
        if(null != product.getEpc() && !"".equals(product.getEpc()))
            updateString.append(" gr:hasEAN_UCC-13 \""+product.getEpc()+"\"^^xsd:string ; ");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, ENDPOINT_URL, null, null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while Creating Product",e);
            throw new InventoryManagementException(e);
        }
        return productURI;
    }

    public static List<Product> getForProperty(String property, String value) throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.getItemForProperty");
        
        String queryString = prefix
            + "SELECT ?product ?prop ?val " 
            + "WHERE { " 
            + "    ?product rdf:type gr:ProductOrServiceModel ;?prop ?val ; " + property + " " + value + " ." 
            + "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(ENDPOINT_URL, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            List<Product> products = extractProductFrmResultSet(rs);
            exec.close();
            return products;
        } catch (Exception e) {
            log.error("Exception Occured while fetching item details", e);
            throw new InventoryManagementException("Loading item Details Failed", e);
        }
    }

    /**
     * @param rs
     */
    private static List<Product> extractProductFrmResultSet(ResultSet rs) {
        List<Product> products = new ArrayList<Product>();
        Product product = new Product();
        String prevObj = "";
        String newObj = "";
        boolean addLastObj = false;
        while (rs.hasNext()) 
        {
            QuerySolution sol = rs.nextSolution();
            newObj = sol.get("product").asResource().getURI();

            if (!"".equals(prevObj) && !prevObj.equals(newObj)) {
                products.add(product);
                product = new Product();
            }
            prevObj = newObj;

            if ("color".equals(sol.get("prop").asResource().getLocalName())) {
                product.setColor(sol.get("val").asLiteral().getString());
            }
            else if ("isPartOf".equals(sol.get("prop").asResource().getLocalName())) {
                product.setCollection(sol.get("val").asLiteral().getString());
            }
            else if ("description".equals(sol.get("prop").asResource().getLocalName())) {
                product.setDescription(sol.get("val").asLiteral().getString());
            }
            else if ("serialNumber".equals(sol.get("prop").asResource().getLocalName())) {
                product.setProductCode(sol.get("val").asLiteral().getString());
            }
            else if ("name".equals(sol.get("prop").asResource().getLocalName())) {
                product.setProductName(sol.get("val").asLiteral().getString());
            }
            else if ("sex".equals(sol.get("prop").asResource().getLocalName())) {
                product.setSex(sol.get("val").asLiteral().getString());
            }
            else if ("category".equals(sol.get("prop").asResource().getLocalName())) {
                product.setProductGroup(sol.get("val").asLiteral().getString());
            }
            else if ("depiction".equals(sol.get("prop").asResource().getLocalName())) {
                product.setImage(sol.get("val").asResource().getURI());
            } 
            else if ("size".equals(sol.get("prop").asResource().getLocalName())) {
                product.setSize(sol.get("val").asLiteral().getString());
            }
            else if ("hasEAN_UCC-13".equals(sol.get("prop").asResource().getLocalName())) {
                product.setEpc(sol.get("val").asLiteral().getString());
            }
            addLastObj = true;   
        }
        if(addLastObj)
            products.add(product);
        return products;
    }

    /**
     * This method returns all the available products in the system
     * @return
     * @throws InventoryManagementException
     */
    public static List<Product> getAllProducts() throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.getAllProducts");

        String queryString = prefix +
        "SELECT ?product ?prop ?val " +
        "WHERE {" +
        "    ?product ?prop ?val ." +
        "    ?product rdf:type gr:ProductOrServiceModel . "+
        "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(ENDPOINT_URL, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            List<Product> products = extractProductFrmResultSet(rs);
            exec.close();
            return products;
        } catch (Exception e) {
            log.error("Exception Occured while fetching item details", e);
            throw new InventoryManagementException("Loading item Details Failed", e);
        }

    }

    /**
     * This method gets product information by passing EPC code
     * @param epc
     * @return
     * @throws InventoryManagementException
     */
    public static Product getForEPC(String epc) throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.getForEPC");
        String value = "\""+epc+"\"^^<http://www.w3.org/2001/XMLSchema#string>" ;
        List<Product> itemes = getForProperty("gr:hasEAN_UCC-13", value);
        if(!itemes.isEmpty()){
            Product product = itemes.get(0);
            return product;
        }
        else
            return null;
    }
    
    
    /**
     * This method gets product information by passing productCode
     * @param itemNo
     * @return
     * @throws InventoryManagementException
     */
    public static Product getForProductCode(String productCode) throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.getForItemNo");
        //if(loadedProductMap.containsKey(productCode)){
       //     return loadedProductMap.get(productCode);
       // }
        String value = "\""+productCode+"\"^^<http://www.w3.org/2001/XMLSchema#string>" ;
        List<Product> itemes = getForProperty("gr:serialNumber", value);
        if(!itemes.isEmpty()){
            Product product = itemes.get(0);
            //loadedProductMap.put(product.getProductCode(),product) ;
            return product;
        }
        else
            return null;
    }

    /**
     * This method deletes product all product related information 
     * @param productCode
     * @throws InventoryManagementException
     */
    public static void delete(String productCode)throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.delete");       
        // query for old item

        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("Delete Where { ");
        updateString.append(" ?product  ?property ?value ; ");
        updateString.append(" gr:serialNumber \""+productCode+"\"^^xsd:string ;");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, ENDPOINT_URL,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while Creating Product",e);
            throw new InventoryManagementException(e);
        }
    }

    /**
     * this method updates the product information. update is perform by
     * deleting all information and adding new information of product
     * 
     * @param product
     * @throws InventoryManagementException
     */
    public static void update(Product product) throws InventoryManagementException {
        log.info("Entering ProductRDFOperations.update");      
        delete(product.getProductCode());
        create(product);        
    }

 }
