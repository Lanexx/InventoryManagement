/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * this class renders date in specified format
 */
 public class DateRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private SimpleDateFormat sdfNewValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String valueToString = "";

    @Override
    public void setValue(Object value) {
        if ((value != null)) {
            Date date = (Date)value;
            
            valueToString = sdfNewValue.format(date);
            value = valueToString;
        }
        super.setValue(value);
    }
}