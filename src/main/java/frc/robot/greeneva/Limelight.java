package frc.robot.greeneva;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import static frc.robot.Constants.*;

public class Limelight {
    private NetworkTableEntry hasTarget;
    private NetworkTableEntry hAng;
    private NetworkTableEntry vAng;
    private NetworkTableEntry camMode;

    public Limelight(NetworkTableInstance ntI) {
        NetworkTable lim = ntI.getTable("limelight");
        hasTarget = lim.getEntry("tv");
        hAng = lim.getEntry("tx");
        vAng = lim.getEntry("ty");
        camMode = lim.getEntry("camMode");
    }

    public boolean hasTarget() {
        return hasTarget.getBoolean(false);
    }

    public double getHAngle() {
        return hAng.getDouble(0);
    }

    public double getHAngleRad() {
        return Math.toRadians(getHAngle());
    }

    public double getVAngle() {
        return vAng.getDouble(0);
    }

    public double getVAngleRad() {
        return Math.toRadians(getVAngle());
    }

    // Uses vertical angle to estimate distance
    // Limelight recommends this, and it does seem like it would work well
    public double getDistance() {
        return (TARGET_HEIGHT - LIMELIGHT_HEIGHT) / Math.tan(getVAngleRad() + LIMELIGHT_ANGLE);
    }

    public enum CamMode {
        Vision,
        Driver
    }

    public void setCamMode(CamMode mode) {
        switch (mode) {
            case Vision:
                camMode.setNumber(0);
                break;
            case Driver:
                camMode.setNumber(1);
                break;
        }
    }
}