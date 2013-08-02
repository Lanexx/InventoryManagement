/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.domain.Product;
import com.fabcoders.domain.StockHistoryEvent;
import com.fabcoders.exception.InventoryManagementException;
/**
 * This class performs all the operation on history.dat file 
 *
 */
public class StockHistoryUtil {

    private static File historyLogFile;
    
    /**
     * constructor throws exception if history file does not exist
     * @throws InventoryManagementException
     */
    public StockHistoryUtil() throws InventoryManagementException{
        historyLogFile = new File(ConfigManager.HISTORY_LOG_FILE);
        if(!historyLogFile.exists() || !historyLogFile.isFile()){
            throw new InventoryManagementException("History log file does not exists");
        }
    }
  
    /**
     * Add entry to history log file
     * @param event
     * @throws InventoryManagementException
     */
    public static void addToHistoryLog(StockHistoryEvent event) throws InventoryManagementException{
        try {
            String line = event.getEpc()+":"+event.getOperation()+":"+ event.getOperationOn().getTime();
            FileWriter writer = new FileWriter(historyLogFile,true);
            writer.append(line +System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
           throw new InventoryManagementException("Adding to history Failed",e);
        } catch (IOException e) {
            throw new InventoryManagementException("Adding to history Failed",e);
        }
    }
    
    /**
     * Returns all the entries fron history log file
     * @return
     * @throws InventoryManagementException
     */
    public static List<StockHistoryEvent> getAllRecord() throws InventoryManagementException{
        List<StockHistoryEvent> stockList = new ArrayList<StockHistoryEvent>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(historyLogFile));
            String line;
            String[] dataArray;
            StockHistoryEvent stock;
            while((line = reader.readLine())!= null){
                try {
                    stock = new StockHistoryEvent();
                    dataArray = line.split(":");
                   Product product = ProductRDFOperations.getForEPC(dataArray[0]);
                    if(null != product){
                        stock.setProduct(product);
                        stock.setEpc(dataArray[0]);
                        stock.setOperation(dataArray[1]);
                        stock.setOperationOn(new Date(Long.parseLong(dataArray[2])));
                        stockList.add(stock);    
                    }
                } catch (NumberFormatException e) {
                } catch (NullPointerException e) {
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        } catch (IOException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        }
        return stockList; 
    }
    
    /**
     * Clear all the records before thisDay
     * @param thisDay
     * @throws InventoryManagementException
     */
    public static void clearRecordsBefore(Date thisDay)throws InventoryManagementException{
        String tmpFileName = "tmp_try.dat";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(historyLogFile));
            writer = new BufferedWriter(new FileWriter(tmpFileName,true));
            String line;
            String[] dataArray;
            while((line = reader.readLine())!= null){
                dataArray = line.split(":");
                Date operationDate = new Date(Long.parseLong(dataArray[2]));
                if(!operationDate.before(thisDay)){
                    writer.append(line +System.getProperty("line.separator"));
                }
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        } catch (IOException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        } finally{
            try {
                reader.close();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
        historyLogFile.delete();

        // And rename tmp file's name to old file name
        File newFile = new File(tmpFileName);
        newFile.renameTo(historyLogFile);
    }
}
