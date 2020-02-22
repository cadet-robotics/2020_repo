package frc.robot.greeneva;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import static frc.robot.Constants.*;

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

    // Adjusts the distance because our camera is off center
    public double getAdjustedDistance() {
        double oldDist = getDistance();
        return Math.sqrt(oldDist * oldDist + LIMELIGHT_H_OFFSET_P2 - oldDist * LIMELIGHT_H_OFFSET * Math.cos(getHAngleRad()));
    }

    // Adjusts the horizontal angle because our camera is off center
    public double getAdjustedHAngle() {
        return Math.toDegrees(getAdjustedHAngleRad());
    }

    public double getAdjustedHAngleRad() {
        double hAng = PI_2 - getHAngleRad();
        return Math.asin(Math.sin(hAng) / getAdjustedDistance() * getDistance()) - PI_2;
    }
}