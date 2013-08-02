/*
 * Developed by Fabcoders 
 * Version 0.1
 */
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
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
/**
 * This is performs all the operation relating persistence of inventory/ Stock related data 
 * data is stored in RDF format and is accessible through SPARQL end point  
 */
public class StockRDFOperations {

    private static Log log = LogFactory.getLog(StockRDFOperations.class);
    
    private static String ENDPOINT_URL = ConfigManager.STOCK_SPARQL_URL;
    private static String USERNAME = ConfigManager.ENDPOINT_USERNAME;
    private static String PASSWORD = ConfigManager.ENDPOINT_PASSWORD;
    
    // format for date storage
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

    // prefix for all queries
    private static final String prefix =
        "PREFIX gr: <http://purl.org/goodrelations/v1#> "
      + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
      + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
      + "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
      + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
      + "PREFIX stock: <http://www.example.com/stock-ns.rdf#> ";
    
    /**
     * this method add stock information to the repository
     * @param epc
     * @return
     * @throws InventoryManagementException
     */
    public static void addToStock(String epc)throws InventoryManagementException {
        log.info("Entering StockRDFOperations.addToStock");
        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("DELETE WHERE { "+stockURI+" ?prop ?val};");
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + " rdf:type <http://www.example.com/ns#stock> ;");
        updateString.append("stock:addedOn \""+sdf.format(new Date())+"\"^^xsd:dateTime;");
        updateString.append("stock:hasEpcCode \""+epc+"\"^^xsd:string.");
        updateString.append("}");
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, ENDPOINT_URL,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while adding to Stock",e);
            throw new InventoryManagementException(e);
        }
    }

    /**
     * This method sets stock removal information  
     * @param epc
     * @return
     * @throws InventoryManagementException
     */
    public static int removeFromStock(String epc)throws InventoryManagementException {
        log.info("Entering StockRDFOperations.removeFromStock");

        String stockURI = "<http://localhost:8080/resource/stock/"+epc+">";
        
        StringBuilder updateString = new StringBuilder(prefix);;
        updateString.append("INSERT DATA { ");
        updateString.append(stockURI + " stock:removedOn \""+sdf.format(new Date())+"\"^^xsd:dateTime.");
        updateString.append("}");
        
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, ENDPOINT_URL,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while removing from Stock",e);
            throw new InventoryManagementException(e);
        }
        return 1;
    }

    /**
     * This method mark stock to sold to customer
     * @param customerName
     * @param epc
     * @return
     * @throws InventoryManagementException
     */
    public static void setStockToSold (String customerName, String[] epcs) throws InventoryManagementException {
        log.info("Entering StockRDFOperations.setStockToSold");
        Date salesdate = new Date();
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("INSERT DATA { ");
        for (String epc : epcs) {
            updateString.append(" <http://localhost:8080/resource/stock/"+epc+"> stock:soldTo \""+customerName+"\"^^xsd:string ; ");
            updateString.append(" stock:soldOn \""+sdf.format(salesdate)+"\"^^xsd:dateTime .");
        }
        updateString.append("}");
        
        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update, ENDPOINT_URL,null,null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while selling item",e);
            throw new InventoryManagementException(e);
        }

    }

    /**
     * returns list of stock items sold to customer in parameter
     * @param customerName
     * @return
     */
    public static List<Stock> getStockSoldTo(String customerName){
        log.info("Entering StockRDFOperations.getStockSoldTo");

        String queryString = prefix +
            "SELECT ?stock ?prop ?val " 
            + " WHERE { " 
            + "    ?stock ?prop ?val ;"
            + "     stock:soldTo \""+customerName+"\"^^xsd:string . "
            + "} ";

        try {
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(ENDPOINT_URL, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
            ResultSet rs = exec.execSelect();
            List<Stock> stockSet = extractStockFromResultSet(rs);
            exec.close();
            return stockSet;
        } catch (Exception e) {
            log.error("Exception Occured while getting sold stock",e);
            return null;
        }
    }

    /**
     * this method returns stock/inventory available with company  
     * @return
     */
    public static Set<String> getStockPresent() {

        log.info("Entering StockRDFOperations.getStockPresent");
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
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(ENDPOINT_URL, query);
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

    /**
     * return stock information for Epc value
     * @param epc
     * @return
     * @throws InventoryManagementException
     */
    public static Stock getForEpc(String epc)throws InventoryManagementException {

        log.info("Entering StockRDFOperations.getForEpc");
        String value = "\""+epc+"\"^^xsd:string";
        List<Stock> stockSet = getForProperty("stock:hasEpcCode",value );
        if(!stockSet.isEmpty())
            return stockSet.get(0);
        else
            return null;
    }

    /**
     * return stock information for property and value
     * @param property
     * @param value
     * @return
     * @throws InventoryManagementException
     */
    private static List<Stock> getForProperty(String property, String value) throws InventoryManagementException {
        log.warn("Entering StockRDFOperations.getForProperty");

        String queryString = prefix
            + "SELECT ?stock ?prop ?val " 
            + "WHERE { "
            + "    ?stock ?prop ?val ;"
            + "    " + property + " " + value + " ." 
            + "}";

        try{
            Query query = QueryFactory.create(queryString);
            QueryEngineHTTP exec = (QueryEngineHTTP)QueryExecutionFactory.sparqlService(ENDPOINT_URL, query);
            exec.setBasicAuthentication(USERNAME, PASSWORD.toCharArray());
           ResultSet rs = exec.execSelect();
           List<Stock> stockSet = extractStockFromResultSet(rs);
            exec.close();
            return stockSet;
        } catch (Exception e) {
            log.error("Exception Occured while getting details",e);
            throw new InventoryManagementException(e);
        }
    }

    /**
     * @param stockSet
     * @param rs
     * @throws ParseException
     */
    private static List<Stock> extractStockFromResultSet(ResultSet rs)
            throws ParseException {
        List<Stock> stockSet = new ArrayList<Stock>();
        
        Stock stock;
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

            if ("addedOn".equals(sol.get("prop").asResource().getLocalName())) {
                try {
                    stock.setAddedOn(sdf.parse(sol.get("val").asLiteral().getString()));
                } catch (ParseException e) {
                }
            }
            else if ("hasEpcCode".equals(sol.get("prop").asResource().getLocalName())) {
                stock.setEpc(sol.get("val").asLiteral().getString());
            }else if ("soldOn".equals(sol.get("prop").asResource().getLocalName())) {
                try {
                    stock.setSold(true);
                    stock.setSoldOn(sdf.parse(sol.get("val").asLiteral().getString()));
                } catch (ParseException e) {
                }
            }
            else if ("soldTo".equals(sol.get("prop").asResource().getLocalName())) {
                stock.setSoldTo(sol.get("val").asLiteral().getString());
            }
            else if ("removedOn".equals(sol.get("prop").asResource().getLocalName())) {
                stock.setRemovedOn(sdf.parse(sol.get("val").asLiteral().getString()));
            }
            addLastObj = true;   
        }
        if(addLastObj)
            stockSet.add(stock);
        return stockSet;
    }
}