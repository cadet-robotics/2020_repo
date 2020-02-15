package frc.robot;

/**
 * A place to put utilities
 * 
 * @author Alex Pickering
 */
public class Util {
	
	/**
	 * Maps a value from one range to another
	 * 
	 * @param val The value to map
	 * @param min1
	 * @param max1
	 * @param min2
	 * @param max2
	 * @return The mapped value
	 */
	public static double map(double val, double min1, double max1, double min2, double max2) {
		return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
	}
}
