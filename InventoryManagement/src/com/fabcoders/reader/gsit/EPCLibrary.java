package com.fabcoders.reader.gsit;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * This are the function allowed from EPCDll.dll file
 */
public interface EPCLibrary extends Library {
    
    EPCLibrary epc = (EPCLibrary) Native.loadLibrary(ReaderDllLoader
            .getEpcDllPath(), EPCLibrary.class);
    
    // Serial port function
    public Byte CharToHex(byte ch);

    // Hit serial
    public boolean ComOpenCom(PointerByReference hCom, String ComPort, int BaudRate);

    // close the serial port
    public boolean ComCloseCom(PointerByReference hCom);

    // read version
    public int ComReadVersion(Pointer hCom, byte[] RecvBuf);

    // single tag identification
    public int ComTagIdentify(Pointer hCom, int TagType, byte[] Recvbuf);

    // read the contents of the tag inside
    public int ComReadTagContent(Pointer hCom, int MemBank, int Address,
            int length, byte[] RecvBuf);

    // write
    public int ComTagWriteWord(Pointer hCom, int WriteMode, byte MemBank,
            byte Address, byte Length, byte[] Data);

    // lock card
    public int ComTagLock(Pointer hCom, int LockType);

    // destroyed card
    public int ComTagKill(Pointer hCom, byte[] PassWord);

    // initialize card
    public int ComInitTag(Pointer hCom);

    // reset reader
    public int ComResetReader(Pointer hCom);

    // stop the reader
    public int ComStopReadTag(Pointer hCom);

    // start reader
    public int ComStartReadTag(Pointer hCom);

    // re-identification card (card is valid)
    public int ComAfreshTag(Pointer hCom);

    // re-obtain data
    public int ComAfreshGetData(Pointer hCom);

    // set the baud rate
    public int ComSetBaudRate(Pointer hCom, int BaudRateType);

    // reader stopped working
    public int ComStopWork(Pointer hCom);

    // query multiple parameters
    public int ComQueryMultiParameter(Pointer hCom, int Length, int Lsb,
            byte[] RecvBuf);

    // query a single parameter
    public int ComQuerySingleParameter(Pointer hCom, int Lsb,
            byte[] RecvBuf);

    // set multiple parameters
    public int ComSetMultiParameter(Pointer hCom, int Length, int Lsb,
            byte[] WriteData);

    // set individual parameters
    public int ComSetSingleParameter(Pointer hCom, int Lsb,
            byte WriteData);

    // Start reading cards
    public boolean ComStratReadMultiTag(Pointer hCom);

    // Get read multi-card data
    public boolean ComGetMultiTagBuf(byte[] Count, byte[] IdBuffer);

    // stop reading multiple cards
    public void ComStopReadMultiTag(Pointer hCom);

    // query password
    public int ComCheckPassWorld(Pointer hCom, int Type, byte[] RecvBuf);

    // cryptographic operations
    public int ComOperationPassWorld(Pointer hCom, int Type,
            byte[] RecvBuf);

    // import authorization ID
    public int ComLoadAuthoId(Pointer hCom, byte[] SendData);

    // delete all authorized ID
    public int ComDeleAuthoId(Pointer hCom);

    // Network port

    public int SocketStartClient(PointerByReference sockClient, String ClientIp,
            int ClientPort);

    public boolean SocketCloseClient(PointerByReference sockClient);

    public int SocketStartServer(PointerByReference SerSock, int SerPort);

    public boolean SocketCloseServer(PointerByReference SerSock);

    public boolean SocketOutTime(Pointer sockClient, long Wtime, long Etime,
            boolean Openflag);

    @Deprecated
    public int SocketClearBuffer(Pointer sockClient);

    // read the version number
    public int SocketReadVersion(PointerByReference sockClient, byte[] recvBuf);

    // power to stop
    public int SocketStopWorkSetting(PointerByReference sockClient);

    // reset
    public int SocketResetReader(PointerByReference sockClient);

    // query the reader a single parameter
    public int SocketQuerySingleParameter(PointerByReference sockClient, Byte lsb,
            byte[] recvBuf);

    // query the reader more parameters
    public int SocketQueryMultiParameter(PointerByReference sockClient,
            byte length, byte lsb, byte[] recvBuf);

    // set the reader a single parameter
    public int SocketSetSingleParameter(PointerByReference sockClient, byte Lsb,
            byte Data);

    // set multiple parameters of the reader
    public int SocketSetMultiParameter(PointerByReference sockClient, byte Length,
            byte Lsb, byte[] writeData);

    // tag identification
    public int SocketTagIdentify(PointerByReference sockClient, byte tagType,
            byte[] recvBuf);

    // Initialize tab
    public int SocketInitTag(PointerByReference sockClient);

    // Lock card
    public int SocketTagLock(PointerByReference sockClient, byte LockType);

    // Destruction of label
    public int SocketTagKill(PointerByReference sockClient, byte[] PassData);

    // Write content to an area of the card
    public int SocketTagWriteWord(PointerByReference sockClient, byte WriteMode,
            byte Membank, byte Address, byte Length, byte[] Writedata);

    // Read the contents of the card in a domain
    public int SocketReadTagContent(PointerByReference sockClient, byte Membank,
            byte Address, int Length, byte[] recvBuf);

    // Re-identify the c
    public int SocketAfreshIdentifyTag(PointerByReference sockClient);

    // Start tag read
    public int SocketStratReadTag(PointerByReference sockClient);

    // Stop tag read
    public int SocketStopReadTag(PointerByReference sockClient);

    // Start reading cards
    public int SocketStartReadMultiTag(PointerByReference sockClient);

    // Stop reading multi-card
    public boolean SocketStopReadMultiTag(PointerByReference sockClient);

    // Get read multi-card data
    public boolean SocketGetMultiTagBuf(byte[] Count, byte[] IdBuffer);

    // Inquiry Password
    public int SocketCheckPassWorld(PointerByReference sockClient, byte Type,
            byte[] RecvBuf);

    // Cryptographic operations
    public int SocketOperationPassWorld(PointerByReference sockClient, byte Type,
            byte[] RecvBuf);

    // Import authorization ID
    public int SocketLoadAuthoId(PointerByReference sockClient, byte[] SendData);

    // Delete all authorization ID
    public int SocketDeleAuthoId(PointerByReference sockClient);

    // Heartbeat packets
    public boolean SocketHeartbeatpacket(PointerByReference sockClient);
}
