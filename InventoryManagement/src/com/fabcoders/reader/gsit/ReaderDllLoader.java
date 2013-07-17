package com.fabcoders.reader.gsit;

import java.net.URL;

public class ReaderDllLoader {

    private static String epcDllPath ;
    
    public ReaderDllLoader (){
        URL url = ReaderDllLoader.class.getClassLoader().getResource("./EpcDll.dll");
        epcDllPath = url.getPath().substring(1, url.getPath().length());
    }
    
    /**
     * @return the epcDllPath
     */
    public static String getEpcDllPath() {
        return epcDllPath;
    }
}
