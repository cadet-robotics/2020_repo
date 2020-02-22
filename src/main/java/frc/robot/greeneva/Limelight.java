package frc.robot.greeneva;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    private NetworkTableEntry hasTarget;
    private NetworkTableEntry hAng;
    private NetworkTableEntry vAng;

    public Limelight(NetworkTableInstance ntI) {
        NetworkTable lim = ntI.getTable("limelight");
        hasTarget = lim.getEntry("tv");
        hAng = lim.getEntry("tx");
        vAng = lim.getEntry("ty");
    }

    public boolean hasTarget() {
        return hasTarget.getBoolean(false);
    }

    public double getHAngle() {
        return hAng.getDouble(0);
    }

    public double getVAngle() {
        return vAng.getDouble(0);
    }
}