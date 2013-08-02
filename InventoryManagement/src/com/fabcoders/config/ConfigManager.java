/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.fabcoders.persistence.StockHistoryUtil;
import com.fabcoders.reader.gsit.ReaderDllLoader;

/**
 *  This class loads all the configurations and also validates presence of all 
 * necessary support files
 */
public final class ConfigManager {
    
    /**
     * variable for application.properties file
     */
    private static Properties configProp = new Properties();
    
    /**
     * static block to initialize configuration on class load 
     */
    static {
        try {
             configProp.load(ConfigManager.class.getResourceAsStream("/application.properties"));
         } catch (IOException e) {
             e.printStackTrace();
         }
        refreshConfigValue() ;
    }
    
    /**
     * Configurations to initialized
     */
    public static String IMAGE_ACCESS_PATH;
    public static String IMAGE_UPLOAD_PATH;
    public static String HISTORY_LOG_FILE;
    public static String[] ITEMGROUPS;
    public static String[] COLLECTIONS;
    public static String[] COLORS;
    public static String[] SIZE;
    public static String[] GENDER;
    public static String STOCK_SPARQL_URL;
    public static String PRODUCT_SPARQL_URL;
    public static String ENDPOINT_USERNAME;
    public static String ENDPOINT_PASSWORD;
    
    /**
     * This method reads values from configuration
     */
    private static void refreshConfigValue() {
        
        // check configuration from application.properties
        try {

            IMAGE_ACCESS_PATH = configProp.getProperty("image.access.path");
            IMAGE_UPLOAD_PATH = configProp.getProperty("image.upload.path");
            ITEMGROUPS = configProp.getProperty("type.itemgroups","").split(":");
            COLLECTIONS = configProp.getProperty("type.collections","").split(":");
            COLORS = configProp.getProperty("type.colors","").split(":");
            SIZE = configProp.getProperty("type.size","").split(":");
            GENDER = configProp.getProperty("type.gender","").split(":");
            HISTORY_LOG_FILE = configProp.getProperty("history.log.file");
            PRODUCT_SPARQL_URL = configProp.getProperty("product.sparql.endpoint.url");
            STOCK_SPARQL_URL = configProp.getProperty("stock.sparql.endpoint.url");
            ENDPOINT_USERNAME = configProp.getProperty("endpoint.username");
            ENDPOINT_PASSWORD = configProp.getProperty("endpoint.password");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please check the Configuration");
            System.exit(1);
        }
        
        // check if EpcDll file exists
        try {
            new ReaderDllLoader();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please check if EpcDll.dll exists in folder.");
            System.exit(1);
        }
        
        // check if history log file exists
        try {
            new StockHistoryUtil();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  e.getMessage());
            System.exit(1);
        }

        // check if URL to product end point is accessible
        try {
            URL url = new URL(PRODUCT_SPARQL_URL);
            HttpURLConnection httpConn =  (HttpURLConnection)url.openConnection();
            httpConn.setInstanceFollowRedirects( false );
            httpConn.setRequestMethod( "HEAD" ); 
            httpConn.connect();
            httpConn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Product Endpoint is not connected");
            System.exit(1);
        }
    
        // check if URL to company inventory end point is accessible
        try {

            URL url = new URL(STOCK_SPARQL_URL);
            HttpURLConnection httpConn =  (HttpURLConnection)url.openConnection();
            httpConn.setInstanceFollowRedirects( false );
            httpConn.setRequestMethod( "HEAD" ); 
            httpConn.connect();
            httpConn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Stock Endpoint is not connected");
            System.exit(1);
        }
        
        // check is path to upload images is accessible
        try {

            URL url = new URL(IMAGE_UPLOAD_PATH);
            HttpURLConnection httpConn =  (HttpURLConnection)url.openConnection();
            httpConn.setInstanceFollowRedirects( false );
            httpConn.setRequestMethod( "HEAD" ); 
            httpConn.connect();
            httpConn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Image upload path is not connected");
            System.exit(1);
        }
    } 
}