package frc.robot;

import frc.robot.io.*;
import frc.robot.sensors.Sensors;
import frc.robot.sensors.SightData;

/**
 * Interface for things containing all subsystems
 * 
 * @author Owen Avery
 */
public interface Nexus {
    public Controls getControls();

    public Drive getDriveSystem();

    public SightData getSightData();

    public Motors getMotors();

    public Sensors getSensors();
    
    public Pneumatics getPneumatics();

    public Elevator getElevator();

    public Robot getRobot();
}