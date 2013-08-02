/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.reader.llrp;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fosstrak.llrp.adaptor.Adaptor;
import org.fosstrak.llrp.adaptor.AdaptorManagement;
import org.fosstrak.llrp.adaptor.Reader;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.MessageHandler;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.LLRPMessageFactory;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.types.LLRPMessage;

import com.fabcoders.exception.InventoryManagementException;
import com.fabcoders.gui.LLRPInventoryTracker;

/**
 * This class provides an interface to work with llrp reader 
 *
 */
public class LLRPReaderImpl implements MessageHandler{
   
    private static AdaptorManagement mgnt;
    private static Reader reader;
    private static Adaptor adaptor;
    private static Log log = LogFactory.getLog(LLRPReaderImpl.class);
    private static String adaptorName;
    private static String readerName;
    
    public LLRPReaderImpl (LLRPInventoryTracker inventoryTracker)throws InventoryManagementException {
        mgnt = AdaptorManagement.getInstance();

        synchronized (mgnt) {
            try {
                mgnt.initialize(null, null, false, inventoryTracker, this);
                mgnt.registerPartialHandler(inventoryTracker, RO_ACCESS_REPORT.class);
            } catch (LLRPRuntimeException e) {
                log.error("Initialising Adaptor Failed", e);
                throw new InventoryManagementException("Initialising Adaptor Failed", e);
            }
        }
    }

    public boolean disconnectReader()throws InventoryManagementException {
        try {

            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.stopROSpecs());
           // sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.disableAccessSpec());
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.disableROSpecs());
           // sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.deleteAccessSpec());
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.deleteROSpecs());
            sleep(2000);
            synchronized (reader) {
                reader.disconnect();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean connectReader(String readerNameInput)throws InventoryManagementException {
        
        try {
            readerName = readerNameInput;
            reader = adaptor.getReader(readerName);
            if(!reader.isConnected()){
                synchronized (reader) {
                    reader.connect(true);
                }
                sleep(1000);    
            }
        } catch (Exception e) {
            log.error("Connecting to Reader Failed", e);
            throw new InventoryManagementException("Connecting to Reader Failed", e);
        }
        
        if(null == reader || null == readerName){
            log.error("Reader is not Initialized");
            return false;
        }
        try {
           // sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.deleteAccessSpec());
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.deleteROSpecs());
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.addROSpec());
           // sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.addAccessSpec());
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.enableROSpec());
           // sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.enableAccessSpec());
            sleep(2000);
            sendLLRPmessage(mgnt, adaptorName, readerName, LLRPMessages.startROSpec());
            
            return true;
        } catch (Exception e) {
            return false;
        }
       
    }
    
    public void disconnectAdaptor() throws InventoryManagementException {
        try {
            mgnt.undefine(adaptorName);
        } catch (Exception e) {
            log.error("Failed to disconnect adaptor",e);
            throw new InventoryManagementException("Failed to disconnect adaptor",e);
        }
    }
    
    public List<String> connectAdaptor(String ip) throws InventoryManagementException {
        if(null == mgnt){
            log.error("Adaptor Management is not Initialized");
            throw new InventoryManagementException("Adaptor Management is not Initialized");
        }
        try {
            adaptorName = mgnt.define("adaptor", ip);
            adaptor = mgnt.getAdaptor(adaptorName);
        } catch (Exception e) {
            log.error("Connecting to Adaptor Failed", e);
            throw new InventoryManagementException("Connecting to Adaptor Failed :"+e.getMessage());
        }
        if(null == adaptor || null == adaptorName){
            log.error("Adaptor is not Initialized");
            throw new InventoryManagementException("Adaptor is not Initialized");
        }

        try {
            return adaptor.getReaderNames();
        } catch (RemoteException e) {
            log.error("Error Ocurred while fetching readres names for aaptor",e);
            return null;
        }
    }
    
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
   
    /**
     * @param mgnt
     * @param adaptorName
     * @param readerName
     * @param byteArray
     * @throws InventoryManagementException
     * @throws InvalidLLRPMessageException
     * @throws LLRPRuntimeException
     */
    private void sendLLRPmessage(AdaptorManagement mgnt, String adaptorName,
            String readerName, byte[] byteArray) throws InventoryManagementException {
        try {
            LLRPMessage llrpMessage = LLRPMessageFactory
                    .createLLRPMessage(byteArray);
            mgnt.enqueueLLRPMessage(adaptorName, readerName, llrpMessage);
        } catch (Exception e) {
            log.error("Sending LLRPMessage Failed", e);
            throw new InventoryManagementException("Sending LLRPMessage Failed", e);
        }
    }
    
    public void readerCleanUp(){
         mgnt.shutdown();
    }
    @Override
    public void handle(String arg0, String arg1, LLRPMessage msg) {
    }
}
