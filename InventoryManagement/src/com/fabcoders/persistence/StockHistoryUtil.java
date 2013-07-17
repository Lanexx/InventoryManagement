package com.fabcoders.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.domain.StockHistoryEvent;
import com.fabcoders.exception.InventoryManagementException;

public class StockHistoryUtil {

    private static File historyLogFile;
    
    public StockHistoryUtil() throws InventoryManagementException{
        historyLogFile = new File(ConfigManager.HISTORY_LOG_FILE);
        if(!historyLogFile.exists() || !historyLogFile.isFile()){
            throw new InventoryManagementException("History log file does not exists");
        }
    }
        
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
    
    public static List<StockHistoryEvent> getAllRecord() throws InventoryManagementException{
        List<StockHistoryEvent> stockList = new ArrayList<StockHistoryEvent>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(historyLogFile));
            String line;
            String[] dataArray;
            StockHistoryEvent stock;
            while((line = reader.readLine())!= null){
                stock = new StockHistoryEvent();
                dataArray = line.split(":");
                stock.setProduct(ProductRDFOperations.getItemForEPC(dataArray[0]));
                stock.setOperation(dataArray[1]);
                stock.setOperationOn(new Date(Long.parseLong(dataArray[2])));
                stockList.add(stock);     
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        } catch (IOException e) {
            throw new InventoryManagementException("fetching history Failed",e);
        }
        return stockList; 
    }
}
