package com.fabcoders.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.persistence.UpdateProcessRemote;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

public class ClearHistoryTask extends TimerTask {

    private static Log log = LogFactory.getLog(ClearHistoryTask.class);
    private static String serviceUrl = ConfigManager.SPARQL_URL;

    private static String USERNAME = ConfigManager.ENDPOINT_USERNAME;
    private static String PASSWORD = ConfigManager.ENDPOINT_PASSWORD;

    private static final String prefix = "PREFIX gr: <http://purl.org/goodrelations/v1#> "
            + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
            + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
            + "PREFIX stock: <http://www.example.com/stock-ns.rdf#> ";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public void run() {
        log.warn("ClearHistoryTask started");

        Calendar prevYear = Calendar.getInstance();
        prevYear.setTime(new Date());
        prevYear.add(Calendar.YEAR, -3);
        
        StringBuilder updateString = new StringBuilder(prefix);
        updateString.append("DELETE WHERE { ");
        updateString.append("?stock rdf:type <http://www.example.com/ns#stock>;");
        updateString.append("       stock:removedOn ?date.");
        updateString.append(" FILTER ( ?date < \""+sdf.format(prevYear.getTime())+"T00:00:00Z\"^^xsd:dateTime ) . ");
        updateString.append("}");

        try {
            UpdateRequest update = UpdateFactory.create(updateString.toString());
            UpdateProcessRemote riStore = new UpdateProcessRemote(update,
                    serviceUrl, null, null);
            riStore.setBasicAuthentication(USERNAME, PASSWORD);
            riStore.execute();
        } catch (Exception e) {
            log.error("Exception Occured while clearing old stock items", e);
        }
        log.warn("ClearHistoryTask ended");
    }
}
