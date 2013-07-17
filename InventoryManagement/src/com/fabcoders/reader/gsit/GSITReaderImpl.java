package com.fabcoders.reader.gsit;

import java.util.HashMap;
import java.util.Map;

import com.sun.jna.ptr.PointerByReference;

public final class GSITReaderImpl {

    private static PointerByReference hCom = new PointerByReference(); 
    private static PointerByReference sock = new PointerByReference(); 
    private static boolean isSerial = false;
    private static boolean isSocket = false;
    
    /**
     * @return the isSerial
     */
    public boolean isSerial() {
        return isSerial;
    }

    /**
     * @return the isSocket
     */
    public boolean isSocket() {
        return isSocket;
    }

    /**
     * 
     * @param port
     * @param baudRate
     * @return
     */
    public boolean serialConnect(String port, int baudRate) {
        if (EPCLibrary.epc.ComOpenCom(hCom, port, baudRate)) {
            sleep(1000);
            int flag = EPCLibrary.epc.ComStopReadTag(hCom.getValue());// Stop Reader
            sleep(1000);
            if (flag != 1) {
                EPCLibrary.epc.ComCloseCom(hCom);
                sleep(1000);
                return isSerial;
            }
            EPCLibrary.epc.ComStopReadMultiTag(hCom.getValue());// Stop Reader
            sleep(1000);
            isSerial = true;
        }
        else
        {
            isSerial = false;
        }
        return isSerial;
    }
    
    /**
     * 
     * @param ip
     * @param port
     * @return
     */
    public boolean socketConnect(String ip, int port) {
        int intFlag = EPCLibrary.epc.SocketStartClient(sock, ip, port);
        if(intFlag == 0){
            isSocket = true;
            EPCLibrary.epc.SocketStopReadTag(sock);
            EPCLibrary.epc.SocketStopReadMultiTag(sock);
        }
        return isSocket;
    }
    
    private void sleep (long milliseconds){
    try {
        Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }
    
    /**
     * 
     * @return
     */
    public boolean disconnect() {
        if(isSerial){
            EPCLibrary.epc.ComResetReader(hCom.getValue());// Reset reader
            boolean flag =EPCLibrary.epc.ComCloseCom(hCom);
            isSerial = false;
            return flag;
        }
        if(isSocket){
            EPCLibrary.epc.SocketResetReader(sock);// Reset reader
            EPCLibrary.epc.SocketCloseClient(sock);
        }
        return false;
    }
    
    /**
     * 
     */
    public void stopTagRead() {
        if(isSerial){
            EPCLibrary.epc.ComStopReadMultiTag(hCom.getValue());
            EPCLibrary.epc.ComStopReadTag(hCom.getValue());
        }
        if(isSocket){
            EPCLibrary.epc.SocketStopReadMultiTag(sock);
        }
    }
    
    /**
     * 
     * @return
     */
    public  boolean startTagRead() {
        boolean flag = false;
        if(isSerial){
            flag = EPCLibrary.epc.ComStratReadMultiTag(hCom.getValue());
        }
        if(isSocket){
            flag = 0 == EPCLibrary.epc.SocketStartReadMultiTag(sock);
        }
        sleep(1000);
        return flag; 
    }

    /**
     * 
     * @return
     */
    public String getReaderVersion() {
        byte[] recvbuf = new byte[5];
        String ver = null;
        if(isSerial){
            int intFlag = EPCLibrary.epc.ComReadVersion(hCom.getValue(), recvbuf);
            sleep(1000);
            if(0 == intFlag){
                ver = recvbuf[0]+"."+recvbuf[1]+"V";
            }
        }
        if(isSocket){
            int intFlag = EPCLibrary.epc.SocketReadVersion(sock, recvbuf);
            sleep(1000);
            if(0 == intFlag){
                ver = recvbuf[0]+"."+recvbuf[1]+"V";
            }
        }
        return ver; 
    }

    /**
     * 
     * @return
     */
    public static Map<String, String> getTags() {
        byte[] countbuf = new byte[2];
        byte[] recvbuf1 = new byte[800];
        Map<String, String> tagAntennaMap = new HashMap<String, String>();
        boolean boolFlag = false;
        if(isSerial)
        boolFlag = EPCLibrary.epc.ComGetMultiTagBuf(countbuf, recvbuf1);
        if(isSocket)
        boolFlag = EPCLibrary.epc.SocketGetMultiTagBuf(countbuf, recvbuf1);
        if(boolFlag)
        {
            int i;
            String tagid = "";
            String antennaId = "";
            int coutlen = countbuf[0];
            
            for (int j = 0; j < coutlen; j++) {
                tagid = "";
                for (i = 0; i < 12; i++) {
                    tagid += String.format("%02x", recvbuf1[i + j * 13]).toUpperCase();
                }
                antennaId = String.format("%02x", recvbuf1[i + j * 13]).toUpperCase();
            //    System.out.println(tagid+":"+antennaId);
               /* List<Param> params = new ArrayList<Param>();
                params.add(new Param(Param.STRING,tagid));
                params.add(new Param(Param.STRING,antennaId));
                DatabaseUtil.insertSQL("insert into demo (tagid, antennaid) values(?,?)", params);*/
                tagAntennaMap.put(tagid, antennaId);
            }
          //  System.out.println("#################################################");
        }
        return tagAntennaMap;
    }
}
