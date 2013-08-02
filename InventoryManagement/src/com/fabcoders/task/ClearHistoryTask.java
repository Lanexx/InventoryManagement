/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.task;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.exception.InventoryManagementException;
import com.fabcoders.persistence.StockHistoryUtil;

/**
 * This class runs the task to clear history.dat with older records 
 *
 */
public class ClearHistoryTask extends TimerTask {

    private static Log log = LogFactory.getLog(ClearHistoryTask.class);
   
    @Override
    public void run() {
        log.warn("ClearHistoryTask started");

        // set calendar to 5 years back
        Calendar prevYear = Calendar.getInstance();
        prevYear.setTime(new Date());
        prevYear.add(Calendar.YEAR, -5);
        
        try {
            StockHistoryUtil.clearRecordsBefore(prevYear.getTime());
        } catch (InventoryManagementException e) {
            log.error("Exception Occured while clearing old stock items", e);
        }

        log.warn("ClearHistoryTask ended");
    }
}
