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
							   DRIVE_SPEED_MODIFIER = 1,
							   LIFECAM_3000_VERTICAL_FOV = Math.toRadians(36.93); // found from some searching
	
	/*
	 * Crosshairs Constants
	 */
	public static final boolean CROSSHAIRS_DEBUG = true;
	
	public static final int IMAGE_HEIGHT = 240;    // 240p
	
	public static final double GRAVITY_ACCEL = -9.807,     // From Wikipedia, the free encyclopedia, at e n dot wikipedia dot org
	                           TARGET_HEIGHT = 2.49555,    // From 8 ft 2.25 in (98.25 in)
	                           SHOOTER_Y = 1,
	                           SHOOTER_ANGLE = Math.toRadians(30),
	                           CAMERA_X = 0.01,
	                           CAMERA_Y = 0.9,
	                           CAMERA_ANGLE = Math.toRadians(20);

	// Limelight constants
	public static final double LIMELIGHT_ANGLE = Math.toRadians(55);
	public static final double LIMELIGHT_HEIGHT = 33 * 0.0254;
	public static final double LIMELIGHT_H_OFFSET = 9 * 0.0254;
	public static final double LIMELIGHT_H_OFFSET_P2 = LIMELIGHT_H_OFFSET * LIMELIGHT_H_OFFSET;

	// Generic math constants
	public static final double PI_2 = Math.PI / 2;
}
