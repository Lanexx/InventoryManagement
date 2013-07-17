package com.fabcoders.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.config.ConfigManager;
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
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

public class StockRDFOperations {

    private static Log log = LogFactory.getLog(StockRDFOperations.class);
    private static String serviceUrl = ConfigManager.SPARQL_URL;

    private static String USERNAME = ConfigManager.ENDPOINT_USERNAME;
    private static String PASSWORD = ConfigManager.ENDPOINT_PASSWORD;
    
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");

    private static final String prefix =
        "PREFIX gr: <http://purl.org/goodrelations/v1#> "
      + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
      + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
      + "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
      + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
      + "PREFIX stock: <http://www.example.com/stock-ns.rdf#> ";
    
    public static int addToStock(String epc)throws InventoryManagementException {

        log.warn("Entering StockRDFOperations.addToStock");
        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("DELETE WHERE { "+stockURI+" stock:removedOn ?val;stock:soldOn ?val2 ;stock:soldTo ?val3 };");
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + "stock:addedOn \""+sdf.format(new Date())+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>.");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, serviceUrl,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while adding to Stock",e);
            throw new InventoryManagementException(e);
        }
        return 1;
    }

    public static int addStockDetails(String productid, String epc)throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.addStockDetails");
        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("DELETE WHERE { "+stockURI+" ?prop ?val};");
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + " rdf:type <http://www.example.com/ns#stock> ;");
        updateString.append("stock:itemType <http://localhost:8080/resource/product/"+productid+">; ");
        updateString.append("stock:hasEpcCode \""+epc+"\"^^<http://www.w3.org/2001/XMLSchema#string>.");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, serviceUrl,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while adding to Stock",e);
            throw new InventoryManagementException(e);
        }
        return 1;
    }
    
    public static int removeFromStock(String epc)throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.removeFromStock");

        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);;
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + " stock:removedOn \""+sdf.format(new Date())+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>.");
        updateString.append("}");
        
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, serviceUrl,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while removing from Stock",e);
            throw new InventoryManagementException(e);
        }
        return 1;
    }

    public static int setStockToSold (String customerName, String epc) throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.setStockToSold");
        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + " stock:soldTo \""+customerName+"\"^^<http://www.w3.org/2001/XMLSchema#string>; ");
        updateString.append(" stock:soldOn \""+sdf.format(new Date())+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>.");
        updateString.append("}");
        
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, serviceUrl,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while selling item",e);
            throw new InventoryManagementException(e);
        }
        return 1;

    }

    public static Set<String> getStockSoldTo(String customerName){
        log.warn("Entering StockRDFOperations.getStockSoldTo");
        Set<String> itemsSold = new HashSet<String>();

        String queryString = prefix +
            "SELECT ?s WHERE { " 
            + " ?s  stock:soldTo \""+customerName+"\"^^<http://www.w3.org/2001/XMLSchema#string> . "
            + "} ";

        try {
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(serviceUrl, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            while(rs.hasNext()){
                QuerySolution sol = rs.nextSolution();
                if(sol.get("s").isResource()){
                    StringBuffer strbuf = new StringBuffer(sol.get("s").asResource().getURI());
                    itemsSold.add(strbuf.substring(strbuf.lastIndexOf("/")+1, strbuf.length()));
                }
            }
            exec.close();
        } catch (Exception e) {
            log.error("Exception Occured while getting sold stock",e);
        }
        return itemsSold;
    }

    public static Set<String> getTagsInStock() {

        log.warn("Entering StockRDFOperations.getTagsInStock");
        Set<String> taglist = new HashSet<String>();

        String queryString = prefix +
            "SELECT ?name " +
            "WHERE { " +
            "    ?stock stock:hasEpcCode ?name . " +
            "    FILTER NOT EXISTS { ?stock stock:soldTo ?someone.} " +
            "    FILTER NOT EXISTS { ?stock stock:removedOn ?someotherdate.}" +
            "    FILTER EXISTS { ?stock stock:addedOn ?somedate.}" +
            "}";

        try {
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(serviceUrl, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            while(rs.hasNext()){
                QuerySolution sol = rs.nextSolution();
                if(sol.get("name").isLiteral()){
                    taglist.add(sol.get("name").asLiteral().getString());
                }
            }
            exec.close();
        } catch (Exception e) {
            log.error("Exception Occured while getting items in stock",e);
        }
        return taglist;
    }

    public static List<Stock> getStockOfProductType(String productid)throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.getStockOfProductType");
        List<Stock> stockSet = getStockForProperty("itemType", new ResourceImpl("<http://localhost:3030/resource/product/"+productid+">"));
        return stockSet;
    }

    public static Stock getStockDetailsForEpc(String epc)throws InventoryManagementException {

        log.warn("Entering StockRDFOperations.getStockDetailsForEpc");
        Model m = ModelFactory.createDefaultModel();
        Literal literal= m.createLiteral("\""+epc+"\"^^<http://www.w3.org/2001/XMLSchema#string>");
        List<Stock> stockSet = getStockForProperty("hasEpcCode",literal );
        if(!stockSet.isEmpty())
            return stockSet.get(0);
        else
            return null;
    }

    private static List<Stock> getStockForProperty(String property, RDFNode value) throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.getStockForProperty");
        List<Stock> stockSet = new ArrayList<Stock>();
        Stock stock;

        String queryString = prefix
            + "SELECT ?stock ?prop ?val " 
            + "WHERE { "
            + "    ?stock rdf:type <http://www.example.com/ns#stock> ."
            + "    ?stock ?prop ?val ."
            + "    ?stock stock:" + property + " " + value + " ." 
            + "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(serviceUrl, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            stock = new Stock();
            String prevObj = "";
            String newObj = "";
            boolean addLastObj = false;
            while (rs.hasNext()) 
            {
                QuerySolution sol = rs.nextSolution();
                newObj = sol.get("stock").asResource().getURI();

                if (!"".equals(prevObj) && !prevObj.equals(newObj)) {
                    stockSet.add(stock);
                    stock = new Stock();
                }

                prevObj = newObj;

                if ("soldOn".equals(sol.get("prop").asResource().getLocalName())) {
                    try {
                        stock.setSold(true);
                        stock.setSoldOn(sdf.parse(sol.get("val").asLiteral().getString()));
                    } catch (ParseException e) {
                    }
                }
                else if ("soldTo".equals(sol.get("prop").asResource().getLocalName())) {
                    stock.setSoldTo(sol.get("val").asLiteral().getString());
                }
                else if ("addedOn".equals(sol.get("prop").asResource().getLocalName())) {
                    try {
                        stock.setAddedOn(sdf.parse(sol.get("val").asLiteral().getString()));
                    } catch (ParseException e) {
                    }
                }
                else if ("hasEpcCode".equals(sol.get("prop").asResource().getLocalName())) {
                    stock.setEpc(sol.get("val").asLiteral().getString());
                }
                else if ("itemType".equals(sol.get("prop").asResource().getLocalName())) {
                    try {
                        StringBuffer buffer =  new StringBuffer(sol.get("val").asResource().getURI());
                        String itemNo = buffer.substring(buffer.lastIndexOf("/")+1, buffer.length());
                        stock.setProduct(ProductRDFOperations.getItemForItemNo(itemNo));
                    } catch (Exception e) {
                    }
                }
                addLastObj = true;   
            }
            if(addLastObj)
                stockSet.add(stock);

            exec.close();
        } catch (Exception e) {
            log.error("Exception Occured while getting details",e);
            throw new InventoryManagementException(e);
        }
        return stockSet;
    }
}