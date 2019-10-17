package frc.robot;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.sensors.SightData;

/**
 * Works with NetworkTables to get vision data
 * <p>Javadoc comments lovingly provided by Alex Pickering
 * 
 * @author Owen Avery
 */
public class UpdateLineManager {
    /**
     * Updates the data for line tracking in the vision system
     * 
     * @param nt NetworkTables instance
     * @param see SightData instance
     */
    public static int startListener(NetworkTableInstance nt, SightData see) {
        NetworkTableEntry dataTable = nt.getTable("ShuffleBoard").getEntry("targets");
        
        return dataTable.addListener((e) -> see.setData(e.getEntry()), EntryListenerFlags.kUpdate + EntryListenerFlags.kNew);
    }
}