package frc.robot;

import org.opencv.core.Scalar;

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
	
	/**
	 * Converts from integer comma-separated values to a Scalar
	 * 
	 * @param csv
	 * @return A scalar from the CSV
	 */
	public static Scalar csvToScalar(String csv) {
	    String[] values = csv.split(",");
	    
	    return new Scalar(Integer.parseInt(values[0]),
	                      Integer.parseInt(values[1]),
	                      Integer.parseInt(values[2]));
	}
}
