package com.fabcoders.persistence;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.domain.Product;
import com.fabcoders.domain.Stock;
import com.fabcoders.exception.InventoryManagementException;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

public class ProductRDFOperations {

    private static Log log = LogFactory.getLog(ProductRDFOperations.class);
    private static String serviceUrl = ConfigManager.SPARQL_URL;

    private static String USERNAME = ConfigManager.ENDPOINT_USERNAME;
    private static String PASSWORD = ConfigManager.ENDPOINT_PASSWORD;
    
    private static Map<String, Product> loadedProductMap = new HashMap<String, Product>();

    private static final String prefix =
        "PREFIX gr: <http://purl.org/goodrelations/v1#> "
      + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
      + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
      + "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
      + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
      + "PREFIX product: <http://www.example.com/product-ns.rdf#> ";
    
    
    public static String create(Product product) throws InventoryManagementException {
        log.warn("Entering ProductRDFOperations.create");       
     // query for old item

        String productURI = "<http://localhost:8080/resource/product/"+product.getProductCode()+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("INSERT DATA { ");
        updateString.append(productURI + " rdf:type gr:ProductOrServiceModel ; ");
        if(null != product.getProductName() && !"".equals(product.getProductName()))
            updateString.append(" product:name \""+product.getProductName()+"\"^^xsd:string ;");
        if(null != product.getProductCode() && !"".equals(product.getProductCode()))
            updateString.append(" product:code \""+product.getProductCode()+"\"^^xsd:string ;");
        if(null != product.getProductGroup() && !"".equals(product.getProductGroup()))
            updateString.append(" product:group \""+product.getProductGroup()+"\"^^xsd:string ;");
        if(null != product.getColor() && !"".equals(product.getColor()))
            updateString.append(" product:color \""+product.getColor()+"\"^^xsd:string ;");
        if(null != product.getImage() && !"".equals(product.getImage()))
            updateString.append(" product:image <"+product.getImage()+"> ;");
        if(null != product.getPage() && !"".equals(product.getPage()))
            updateString.append(" product:page <"+product.getPage()+"> ;");
        if(null != product.getCollection() && !"".equals(product.getCollection()))
            updateString.append(" product:collection \""+product.getCollection()+"\"^^xsd:string ;");
        if(null != product.getSex() && !"".equals(product.getSex()))
            updateString.append(" product:sex \""+product.getSex()+"\"^^xsd:string ;");
        if(null != product.getSize() && !"".equals(product.getSize()))
            updateString.append(" product:size \""+product.getSize()+"\"^^xsd:string ;");
        if(null != product.getDescription() && !"".equals(product.getDescription()))
            updateString.append(" product:description \""+product.getDescription()+"\"^^xsd:string ; ");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, serviceUrl,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while Creating Product",e);
            throw new InventoryManagementException(e);
        }
        return productURI;
    }

    public static List<Product> getItemForProperty(String property, RDFNode value) throws InventoryManagementException {
        log.warn("Entering ProductRDFOperations.getItemForProperty");
        List<Product> products = new ArrayList<Product>();
        Product product;

        String queryString = prefix
            + "SELECT ?product ?prop ?val " 
            + "WHERE { " 
            + "    ?product rdf:type gr:ProductOrServiceModel ;?prop ?val ; product:" + property + " " + value + " ." 
            + "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(serviceUrl, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            product = new Product();
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
                else if ("collection".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setCollection(sol.get("val").asLiteral().getString());
                }
                else if ("description".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setDescription(sol.get("val").asLiteral().getString());
                }
                else if ("code".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductCode(sol.get("val").asLiteral().getString());
                }
                else if ("name".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductName(sol.get("val").asLiteral().getString());
                }
                else if ("sex".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setSex(sol.get("val").asLiteral().getString());
                }
                else if ("group".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductGroup(sol.get("val").asLiteral().getString());
                }
                else if ("image".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setImage(sol.get("val").asResource().getURI());
                } 
                else if ("page".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setPage(sol.get("val").asResource().getURI());
                }
                else if ("size".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setSize(sol.get("val").asLiteral().getString());
                }
                addLastObj = true;   
            }
            if(addLastObj)
                products.add(product);
            exec.close();
        } catch (Exception e) {
            log.error("Exception Occured while fetching item details", e);
            throw new InventoryManagementException("Loading item Details Failed", e);
        }
        return products;
    }
    
    public static List<Product> getAllProducts() throws InventoryManagementException {

        log.warn("Entering ProductRDFOperations.getAllProducts");
        List<Product> itemes = new ArrayList<Product>();
        Product product;

        String queryString = prefix +
            "SELECT ?product ?prop ?val " +
            "WHERE {" +
            "    ?product ?prop ?val ." +
            "    ?product rdf:type gr:ProductOrServiceModel . "+
            "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(serviceUrl, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            product = new Product();
            String prevObj = "";
            String newObj = "";
            boolean addLastObj = false;
            while (rs.hasNext()) 
            {
                QuerySolution sol = rs.nextSolution();
                newObj = sol.get("product").asResource().getURI();

                if (!"".equals(prevObj) && !prevObj.equals(newObj)) {
                    itemes.add(product);
                    product = new Product();
                    loadedProductMap.put(product.getProductCode(),product);
                }
                prevObj = newObj;

                if ("color".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setColor(sol.get("val").asLiteral().getString());
                }
                else if ("collection".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setCollection(sol.get("val").asLiteral().getString());
                }
                else if ("description".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setDescription(sol.get("val").asLiteral().getString());
                }
                else if ("code".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductCode(sol.get("val").asLiteral().getString());
                }
                else if ("name".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductName(sol.get("val").asLiteral().getString());
                }
                else if ("sex".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setSex(sol.get("val").asLiteral().getString());
                }
                else if ("group".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setProductGroup(sol.get("val").asLiteral().getString());
                }
                else if ("image".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setImage(sol.get("val").asResource().getURI());
                } 
                else if ("page".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setPage(sol.get("val").asResource().getURI());
                }
                else if ("size".equals(sol.get("prop").asResource().getLocalName())) {
                    product.setSize(sol.get("val").asLiteral().getString());
                }
                addLastObj = true;   
            }
            exec.close();
            if(addLastObj){
                itemes.add(product);
                loadedProductMap.put(product.getProductCode(),product);
            }
                
        } catch (Exception e) {
            log.error("Exception Occured while fetching item details", e);
            throw new InventoryManagementException("Loading item Details Failed", e);
        }

        return itemes;
    }

    public static Product getItemForEPC(String epc) throws InventoryManagementException {
        log.warn("Entering ProductRDFOperations.getItemForEPC");
        Stock stock =  StockRDFOperations.getStockDetailsForEpc(epc);
        if(null != stock){
            Product item =stock.getProduct();
            item.setEpc(epc);
            return item;
        }
        else
            return null;
    }
    
    public static Product getItemForItemNo(String itemNo) throws InventoryManagementException {
        log.warn("Entering ProductRDFOperations.getItemForItemNo");
        if(loadedProductMap.containsKey(itemNo)){
            return loadedProductMap.get(itemNo);
        }
        Model m = ModelFactory.createDefaultModel();
        Literal literal= m.createLiteral("\""+itemNo+"\"^^<http://www.w3.org/2001/XMLSchema#string>");
        List<Product> itemes = getItemForProperty("code", literal);
        if(!itemes.isEmpty()){
            Product product = itemes.get(0);
            loadedProductMap.put(product.getProductCode(),product) ;
            return product;
        }
        else
            return null;
    }
    
    public static BufferedImage getPictureforItemNo(String itemNo) throws InventoryManagementException {
        log.warn("Entering ProductRDFOperations.getPictureforItemNo");
        BufferedImage bf = null;
        String filename = ConfigManager.IMAGE_LOCATION+System.getProperty("file.separator")+ itemNo + ".jpg";
        File file = new File(filename);
        if(file.exists()){
            ImageIcon image = new ImageIcon(file.getPath());
            bf = new BufferedImage(image.getIconWidth(),image.getIconHeight(),BufferedImage.TYPE_INT_RGB);
            image.paintIcon(null, bf.getGraphics(), 0, 0);     
        }
        return bf;
    }
   
 }
