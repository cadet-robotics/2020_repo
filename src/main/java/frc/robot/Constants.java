/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Scalar;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
	
	public static final double DRIVE_Y_AXIS_MODIFIER = 1,
							   DRIVE_X_AXIS_MODIFIER = 1,
							   DRIVE_SPEED_MODIFIER = 0.25,
							   LIFECAM_3000_VERTICAL_FOV = Math.toRadians(36.93); // found from some searching
	
	public static final boolean CROSSHAIRS_DEBUG = true;
	
	// TESTING CONSTANTS
	public static final double SHOOTER_HEIGHT = 0.9144,    // From 36 inches
	                           CAMERA_HEIGHT = 0.8128,     // From 32 inches
	                           TARGET_HEIGHT = 2.49555,    // From 8 ft 2.25 in (98.25 in)
	                           GRAVITY_ACCEL = -9.807;      // From wikipedia, the free encyclopedia, at en dot wikipedia dot org
	
	public static final int IMAGE_HEIGHT = 240;    // 240p
	
	public static final Scalar CROSSHAIR_CENTER_COLOR = new Scalar(0, 255, 0), // Epic gamer green
	                           CROSSHAIR_A_COLOR = new Scalar(255, 0, 0),      // b l u e
	                           CROSSHAIR_B_COLOR = new Scalar(0, 0, 255);      // Fires of hell red
	                        
}
