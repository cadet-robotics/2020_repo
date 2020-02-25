package frc.robot.greeneva;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

import static frc.robot.Constants.*;

public class Limelight implements Sendable {
    private NetworkTableEntry hasTarget;
    private NetworkTableEntry hAng;
    private NetworkTableEntry vAng;
    private NetworkTableEntry camMode;
    private NetworkTableEntry lightMode;

    public Limelight(NetworkTableInstance ntI) {
        NetworkTable lim = ntI.getTable("limelight");
        hasTarget = lim.getEntry("tv");
        hAng = lim.getEntry("tx");
        vAng = lim.getEntry("ty");
        camMode = lim.getEntry("camMode");
        lightMode = lim.getEntry("ledMode");
        SendableRegistry.addLW(this, "limelight-interface");
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
                setLed(true);
                break;
            case Driver:
                camMode.setNumber(1);
                setLed(false);
                break;
        }
    }

    public void setLed(boolean on) {
        lightMode.setNumber(on ? 0 : 1);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("distance", Limelight.this::getDistance, (v) -> {});
        builder.addBooleanProperty("hasTarget", Limelight.this::hasTarget, (v) -> {});
    }
}