package frc.robot;

/**
 * Owen doesn't like some parts of java. He 'fixes' them here
 * <p>Javadoc comments lovingly provided by Alex Pickering
 * 
 * @author Owen Avery
 */
public class JavaIsCancerChangeMyMind {
	
	/**
	 * 'Fixed' version of the modulo operator
	 * 
	 * @param n The number to mod
	 * @param m The modulator/divisor
	 * @return The modulod number
	 */
	public static double moduloIsCancer(double n, double m) {
		n = n % m;
		if (n < 0) n = m + n;
		return n;
	}
}