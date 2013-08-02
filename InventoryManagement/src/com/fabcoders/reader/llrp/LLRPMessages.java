/*
 * Developed by Fabcoders 
 * Version 0.1
 */
package com.fabcoders.reader.llrp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.enumerations.AISpecStopTriggerType;
import org.llrp.ltk.generated.enumerations.AccessReportTriggerType;
import org.llrp.ltk.generated.enumerations.AccessSpecState;
import org.llrp.ltk.generated.enumerations.AccessSpecStopTriggerType;
import org.llrp.ltk.generated.enumerations.AirProtocols;
import org.llrp.ltk.generated.enumerations.ROReportTriggerType;
import org.llrp.ltk.generated.enumerations.ROSpecStartTriggerType;
import org.llrp.ltk.generated.enumerations.ROSpecState;
import org.llrp.ltk.generated.enumerations.ROSpecStopTriggerType;
import org.llrp.ltk.generated.interfaces.AccessCommandOpSpec;
import org.llrp.ltk.generated.messages.ADD_ACCESSSPEC;
import org.llrp.ltk.generated.messages.ADD_ROSPEC;
import org.llrp.ltk.generated.messages.CLOSE_CONNECTION;
import org.llrp.ltk.generated.messages.DELETE_ACCESSSPEC;
import org.llrp.ltk.generated.messages.DELETE_ROSPEC;
import org.llrp.ltk.generated.messages.DISABLE_ACCESSSPEC;
import org.llrp.ltk.generated.messages.DISABLE_ROSPEC;
import org.llrp.ltk.generated.messages.ENABLE_ACCESSSPEC;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC;
import org.llrp.ltk.generated.messages.START_ROSPEC;
import org.llrp.ltk.generated.messages.STOP_ROSPEC;
import org.llrp.ltk.generated.parameters.AISpec;
import org.llrp.ltk.generated.parameters.AISpecStopTrigger;
import org.llrp.ltk.generated.parameters.AccessCommand;
import org.llrp.ltk.generated.parameters.AccessReportSpec;
import org.llrp.ltk.generated.parameters.AccessSpec;
import org.llrp.ltk.generated.parameters.AccessSpecStopTrigger;
import org.llrp.ltk.generated.parameters.C1G2EPCMemorySelector;
import org.llrp.ltk.generated.parameters.C1G2Read;
import org.llrp.ltk.generated.parameters.C1G2TagSpec;
import org.llrp.ltk.generated.parameters.C1G2TargetTag;
import org.llrp.ltk.generated.parameters.C1G2Write;
import org.llrp.ltk.generated.parameters.InventoryParameterSpec;
import org.llrp.ltk.generated.parameters.ROBoundarySpec;
import org.llrp.ltk.generated.parameters.ROReportSpec;
import org.llrp.ltk.generated.parameters.ROSpec;
import org.llrp.ltk.generated.parameters.ROSpecStartTrigger;
import org.llrp.ltk.generated.parameters.ROSpecStopTrigger;
import org.llrp.ltk.generated.parameters.TagReportContentSelector;
import org.llrp.ltk.types.Bit;
import org.llrp.ltk.types.BitArray_HEX;
import org.llrp.ltk.types.TwoBitField;
import org.llrp.ltk.types.UnsignedByte;
import org.llrp.ltk.types.UnsignedInteger;
import org.llrp.ltk.types.UnsignedShort;
import org.llrp.ltk.types.UnsignedShortArray;
import org.llrp.ltk.types.UnsignedShortArray_HEX;

import com.fabcoders.exception.InventoryManagementException;

/**
 * This class contains all the messages send to llrp reader
 * @author windows
 *
 */
public class LLRPMessages {

    
    private static Log log = LogFactory.getLog(LLRPMessages.class);
    private static int rospecId = 111;
    private static int accessSpecID = 222;
    private static String TARGET_DATA = "";
    private static String TAG_MASK =  "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
    /**
     * Add ROSpecs from the reader.
     * 
     * @return
     * @throws InventoryManagementException
     */
    public static byte[] deleteROSpecs() throws InventoryManagementException {
       
        log.debug("Deleting all ROSpecs.");
        DELETE_ROSPEC del = new DELETE_ROSPEC();
        del.setROSpecID(new UnsignedInteger(rospecId));
        try {
            return del.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing DELETE_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing DELETE_ROSPEC Message", e);
        }
    }
    
    /**
     * Add ROSpecs from the reader.
     * 
     * @return
     * @throws InventoryManagementException
     */
    public static byte[] addROSpec() throws InventoryManagementException {
       
        log.debug("Adding ROSpecs.");
        ADD_ROSPEC add = new ADD_ROSPEC();
        add.setROSpec(buildROSpec(rospecId));
        try {
            return add.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing ADD_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing ADD_ROSPEC Message", e);
        }
    }
    
    /**
     * Build the ROSpec.
     * An ROSpec specifies start and stop triggers,
     * tag report fields, antennas, etc.
     * 
     * @param rospecId
     * @return
     */
    private static ROSpec buildROSpec(int rospecId) {
        log.debug("Building ROSpec");
        
        // Create a Reader Operation Spec (ROSpec).
        ROSpec roSpec = new ROSpec();

        roSpec.setPriority(new UnsignedByte(0));
        roSpec.setCurrentState(new ROSpecState(ROSpecState.Disabled));
        roSpec.setROSpecID(new UnsignedInteger(rospecId));

        // Set up the ROBoundarySpec
        // This defines the start and stop triggers.
        ROBoundarySpec roBoundarySpec = new ROBoundarySpec();

        // Set the start trigger to null.
        // This means the ROSpec will start as soon as it is enabled.
        ROSpecStartTrigger startTrig = new ROSpecStartTrigger();
        startTrig.setROSpecStartTriggerType(new ROSpecStartTriggerType(
                ROSpecStartTriggerType.Null));
        roBoundarySpec.setROSpecStartTrigger(startTrig);

        // Set the stop trigger is null. This means the ROSpec
        // will keep running until an STOP_ROSPEC message is sent.
        ROSpecStopTrigger stopTrig = new ROSpecStopTrigger();
        stopTrig.setDurationTriggerValue(new UnsignedInteger(0));
        stopTrig.setROSpecStopTriggerType(new ROSpecStopTriggerType(
                ROSpecStopTriggerType.Null));
        roBoundarySpec.setROSpecStopTrigger(stopTrig);

        roSpec.setROBoundarySpec(roBoundarySpec);

        // Add an Antenna Inventory Spec (AISpec).
        AISpec aispec = new AISpec();

        // Set the AI stop trigger to null. This means that
        // the AI spec will run until the ROSpec stops.
        AISpecStopTrigger aiStopTrigger = new AISpecStopTrigger();
        aiStopTrigger.setAISpecStopTriggerType(new AISpecStopTriggerType(
                AISpecStopTriggerType.Null));
        aiStopTrigger.setDurationTrigger(new UnsignedInteger(0));
        aispec.setAISpecStopTrigger(aiStopTrigger);

        // Select which antenna ports we want to use.
        // Setting this property to zero means all antenna ports.
        UnsignedShortArray antennaIDs = new UnsignedShortArray();
        antennaIDs.add(new UnsignedShort(0));
        aispec.setAntennaIDs(antennaIDs);

        // Tell the reader that we're reading Gen2 tags.
        InventoryParameterSpec inventoryParam = new InventoryParameterSpec();
        inventoryParam.setProtocolID(new AirProtocols(
                AirProtocols.EPCGlobalClass1Gen2));
        inventoryParam.setInventoryParameterSpecID(new UnsignedShort(1));
       
        aispec.addToInventoryParameterSpecList(inventoryParam);
        roSpec.addToSpecParameterList(aispec);

        // Specify what type of tag reports we want
        // to receive and when we want to receive them.
        ROReportSpec roReportSpec = new ROReportSpec();
        // Receive a report every time a tag is read.
        roReportSpec.setROReportTrigger(new ROReportTriggerType(
                ROReportTriggerType.Upon_N_Tags_Or_End_Of_ROSpec));
        roReportSpec.setN(new UnsignedShort(1));
        TagReportContentSelector reportContent = new TagReportContentSelector();
        // Select which fields we want in the report.
        
        reportContent.setEnableAccessSpecID(new Bit(1));
        reportContent.setEnableAntennaID(new Bit(1));
        reportContent.setEnableChannelIndex(new Bit(1));
        reportContent.setEnableFirstSeenTimestamp(new Bit(1));
        reportContent.setEnableInventoryParameterSpecID(new Bit(1));
        reportContent.setEnableLastSeenTimestamp(new Bit(1));
        reportContent.setEnablePeakRSSI(new Bit(1));
        reportContent.setEnableROSpecID(new Bit(1));
        reportContent.setEnableSpecIndex(new Bit(1));
        reportContent.setEnableTagSeenCount(new Bit(1));
        C1G2EPCMemorySelector epcMemSel = new C1G2EPCMemorySelector();
        epcMemSel.setEnableCRC(new Bit(1));
        epcMemSel.setEnablePCBits(new Bit(1));
        
        reportContent.addToAirProtocolEPCMemorySelectorList(epcMemSel);
        roReportSpec.setTagReportContentSelector(reportContent);

        roSpec.setROReportSpec(roReportSpec);

        return roSpec;
    }
    
    // Enable the ROSpec.
    public static byte[] enableROSpec() throws InventoryManagementException {
       
       log.debug("Enabling the ROSpec.");
        ENABLE_ROSPEC enable = new ENABLE_ROSPEC();
        enable.setROSpecID(new UnsignedInteger(rospecId));
        try {
            return enable.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing ENABLE_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing ENABLE_ROSPEC Message", e);
        }
    }

    public static byte[] startROSpec() throws InventoryManagementException {
        log.debug("Starting the ROSpec.");
        START_ROSPEC start = new START_ROSPEC();
        start.setROSpecID(new UnsignedInteger(rospecId));
        try {
            return start.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing START_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing START_ROSPEC Message", e);
        }
    }

    public static byte[] disableROSpecs() throws InventoryManagementException {
        log.debug("Disabling ROSpecs");
        DISABLE_ROSPEC del = new DISABLE_ROSPEC();
        del.setROSpecID(new UnsignedInteger(rospecId));
        try {
            return del.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing DISABLE_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing DISABLE_ROSPEC Message", e);
        }
    }

    public static byte[] stopROSpecs() throws InventoryManagementException {
        log.debug("Stoping ROSpecs");
        STOP_ROSPEC del = new STOP_ROSPEC();
        del.setROSpecID(new UnsignedInteger(rospecId));
        try {
            return del.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing STOP_ROSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing STOP_ROSPEC Message", e);
        }
    }

 // Add the AccessSpec to the reader.
    public static byte[] addAccessSpec(String targetData) throws InventoryManagementException {
        log.debug("Adding AccessSpecs.");
        TARGET_DATA = targetData;
        try {
            ADD_ACCESSSPEC msg = new ADD_ACCESSSPEC();
            msg.setAccessSpec( buildAccessSpec(accessSpecID));
            return msg.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing ADD_ACCESSSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing ADD_ACCESSSPEC Message", e);
        }
    }
    
    // Create an AccessSpec.
    // It will contain our two OpSpecs (read and write).
    private static AccessSpec buildAccessSpec(int accessSpecID) {
        log.debug("Building AccessSpecs.");
        
        AccessSpec accessSpec = new AccessSpec();
        accessSpec.setAccessSpecID(new UnsignedInteger(accessSpecID));

        // Set ROSpec ID to zero.
        // This means that the AccessSpec will apply to all ROSpecs.
        accessSpec.setROSpecID(new UnsignedInteger(0));
        // Antenna ID of zero means all antennas.
        accessSpec.setAntennaID(new UnsignedShort(0));
        accessSpec.setProtocolID(new AirProtocols(
                AirProtocols.EPCGlobalClass1Gen2));
        // AccessSpecs must be disabled when you add them.
        accessSpec
                .setCurrentState(new AccessSpecState(AccessSpecState.Disabled));
        AccessSpecStopTrigger stopTrigger = new AccessSpecStopTrigger();
        // Stop after the operating has been performed a certain number of
        // times.
        // That number is specified by the Operation_Count parameter.
        stopTrigger.setAccessSpecStopTrigger(new AccessSpecStopTriggerType(
                AccessSpecStopTriggerType.Null));
        // OperationCountValue indicate the number of times this Spec is
        // executed before it is deleted. If set to 0, this is equivalent
        // to no stop trigger defined.
        stopTrigger.setOperationCountValue(new UnsignedShort(0));
        accessSpec.setAccessSpecStopTrigger(stopTrigger);

        // Create a new AccessCommand.
        // We use this to specify which tags we want to operate on.
        AccessCommand accessCommand = new AccessCommand();

        // Create a new tag spec.
        C1G2TagSpec tagSpec = new C1G2TagSpec();
        C1G2TargetTag targetTag = new C1G2TargetTag();
        targetTag.setMatch(new Bit(true));
        // We want to check memory bank 1 (the EPC memory bank).
        TwoBitField memBank = new TwoBitField();
        // Clear bit 0 and set bit 1 (bank 1 in binary).
        memBank.clear(0);
        memBank.set(1);
        targetTag.setMB(memBank);
        // The EPC data starts at offset 0x20.
        // Start reading or writing from there.
        targetTag.setPointer(new UnsignedShort(0x20));
        // This is the mask we'll use to compare the EPC.
        // We want to match all bits of the EPC, so all mask bits are set.
        BitArray_HEX tagMask = new BitArray_HEX(TAG_MASK );
        targetTag.setTagMask(tagMask);
        // We only only to operate on tags with this EPC.
        BitArray_HEX tagData = new BitArray_HEX(TARGET_DATA);
        targetTag.setTagData(tagData);

        // Add a list of target tags to the tag spec.
        tagSpec.addToC1G2TargetTagList(targetTag);

        // Add the tag spec to the access command.
        accessCommand.setAirProtocolTagSpec(tagSpec);

        // A list to hold the op specs for this access command.
        List<AccessCommandOpSpec> opSpecList = new ArrayList<AccessCommandOpSpec>();

        // Are we reading or writing to the tag?
        // Add the appropriate op spec to the op spec list.
        // opSpecList.add(buildWriteOpSpec());
        opSpecList.add(buildReadOpSpec());
        
        accessCommand.setAccessCommandOpSpecList(opSpecList);

        // Add access command to access spec.
        accessSpec.setAccessCommand(accessCommand);

        // Add an AccessReportSpec.
        // We want to get notification when the operation occurs.
        // Tell the reader to sent it to us with the ROSpec.
        AccessReportSpec reportSpec = new AccessReportSpec();
        reportSpec.setAccessReportTrigger(new AccessReportTriggerType(
                AccessReportTriggerType.Whenever_ROReport_Is_Generated));

        return accessSpec;
    }
    
    // Create a OpSpec that reads from user memory
    private static C1G2Read buildReadOpSpec() {
        log.debug("Building Read AccessSpecs.");
        
        // Create a new OpSpec.
        // This specifies what operation we want to perform on the
        // tags that match the specifications above.
        // In this case, we want to read from the tag.
        C1G2Read opSpec = new C1G2Read();
        // Set the OpSpecID to a unique number.
        opSpec.setOpSpecID(new UnsignedShort(1));
        opSpec.setAccessPassword(new UnsignedInteger(0));
        // For this demo, we'll read from user memory (bank 3).
        TwoBitField opMemBank = new TwoBitField();
        // Set bits 0 and 1 (bank 3 in binary).
        opMemBank.set(0);
        opMemBank.set(1);
        opSpec.setMB(opMemBank);
        // We'll read from the base of this memory bank (0x00).
        opSpec.setWordPointer(new UnsignedShort(0x00));
        // Read two words.
        opSpec.setWordCount(new UnsignedShort(2));

        return opSpec;
    }

    // Create a OpSpec that writes to user memory
    @SuppressWarnings("unused")
    private static C1G2Write buildWriteOpSpec() {
        log.debug("Building Write AccessSpecs.");
        
        // Create a new OpSpec.
        // This specifies what operation we want to perform on the
        // tags that match the specifications above.
        // In this case, we want to write to the tag.
        C1G2Write opSpec = new C1G2Write();
        // Set the OpSpecID to a unique number.
        opSpec.setOpSpecID(new UnsignedShort(2));
        opSpec.setAccessPassword(new UnsignedInteger(0));
        // For this demo, we'll write to user memory (bank 3).
        TwoBitField opMemBank = new TwoBitField();
        // Set bits 0 and 1 (bank 3 in binary).
        opMemBank.set(0);
        opMemBank.set(1);
        opSpec.setMB(opMemBank);
        // We'll write to the base of this memory bank (0x00).
        opSpec.setWordPointer(new UnsignedShort(0x00));
        UnsignedShortArray_HEX writeData = new UnsignedShortArray_HEX();
        // We'll write 8 bytes or two words.
        writeData.add(new UnsignedShort(0xAABB));
        writeData.add(new UnsignedShort(0xCCDD));
        opSpec.setWriteData(writeData);

        return opSpec;
    }
    
    public static byte[] enableAccessSpec() throws InventoryManagementException {
        log.debug("Enabling AccessSpecs.");
        try {
            ENABLE_ACCESSSPEC msg = new ENABLE_ACCESSSPEC();
            msg.setAccessSpecID(new UnsignedInteger(accessSpecID));
            return msg.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing ENABLE_ACCESSSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing ENABLE_ACCESSSPEC Message", e);
        }
    }
    
    public static byte[] disableAccessSpec() throws InventoryManagementException {
        log.debug("Disabling AccessSpecs.");
        try {
            DISABLE_ACCESSSPEC msg = new DISABLE_ACCESSSPEC();
            msg.setAccessSpecID(new UnsignedInteger(accessSpecID));
            return msg.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing DISABLE_ACCESSSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing DISABLE_ACCESSSPEC Message", e);
        }
    }
    
    public static byte[] deleteAccessSpec() throws InventoryManagementException {
        log.debug("Deleting AccessSpecs.");
        try {
            DELETE_ACCESSSPEC msg = new DELETE_ACCESSSPEC();
            msg.setAccessSpecID(new UnsignedInteger(accessSpecID));
            return msg.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing DELETE_ACCESSSPEC Message", e);
            throw new InventoryManagementException("Exception While Parsing DELETE_ACCESSSPEC Message", e);
        }
    }

    public static byte[] closeConnection() throws InventoryManagementException {
        log.debug("Closing Connection to reader");
        try {
            CLOSE_CONNECTION msg = new CLOSE_CONNECTION();
            return msg.encodeBinary();
        } catch (InvalidLLRPMessageException e) {
            log.error("Exception While Parsing CLOSE_CONNECTION Message", e);
            throw new InventoryManagementException("Exception While Parsing CLOSE_CONNECTION Message", e);
        }
    }
}
