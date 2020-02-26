/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    /*
     * Drive Constants
     */
    public static final double DRIVE_Y_AXIS_MODIFIER = 1,
                               DRIVE_X_AXIS_MODIFIER = 1,
                               DRIVE_SPEED_MODIFIER = 1;
    
    /*
     * Winch Constants
     */
    public static final double WINCH_UP_SPEED = 0.75,
                               WINCH_DOWN_SPEED = -0.75;
    
    /*
     * Intake & Magazine Constants
     */
    public static final double INTAKE_SPEED = 0.5,
                               MAGAZINE_SPEED = -0.3,
                               MAGAZINE_COUNT = 30;
    
    /*
     * Crosshairs Constants
     */
    public static final boolean CROSSHAIRS_DEBUG = true;
    
    public static final int IMAGE_HEIGHT = 240;    // 240p
    
    public static final double GRAVITY_ACCEL = -9.807,     // From Wikipedia, the free encyclopedia, at e n dot wikipedia dot org
                               TARGET_HEIGHT = 2.49555,    // From 8 ft 2.25 in (98.25 in)
                               SHOOTER_Y = 0.9398,
                               SHOOTER_ANGLE = Math.toRadians(42),
                               CAMERA_X = -0.1651,
                               CAMERA_Y = 1.1557,
                               CAMERA_ANGLE = Math.toRadians(10),
                               SHOOTER_WHEELS_DIAMETER = 0.1524,
                               LIFECAM_3000_VERTICAL_FOV = Math.toRadians(36.93); // found from some searching
    
    
    // Limelight constants
    public static final double LIMELIGHT_ANGLE = Math.toRadians(50),
                               LIMELIGHT_HEIGHT = 44 * 0.0254;
}
