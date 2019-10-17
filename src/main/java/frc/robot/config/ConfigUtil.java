package frc.robot.config;

import com.google.gson.JsonElement;

/**
 * Contains some helper methods to reduce repetition
 * 
 * @author Alex Pickering
 */
public class ConfigUtil {
	
	/**
	 * Determines if a config element is filtered out
	 * <p>A filtered element should not be parsed.
	 * <p>This exists to reduce work when adding filtered elements
	 * 
	 * @param element The element name to check
	 * @return Whether or not the element is filtered
	 */
	public static boolean isFiltered(String element) {
		switch(element.toLowerCase()) {
			case "desc": //Element describes the section
			case "placeholder": //Element is a placeholder for a lack of elements
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Gets the given element as an int, where if any exception occurrs it is made into a more verbose ConfigException
	 * <p>This exists to remove the repetition of the try/catch block
	 * <p>This doesn't include the 'throws' clause such that try/catch isn't needed
	 * 
	 * @param element The element to convert
	 * @param section The section in which the element was read
	 * @param name The name of the element
	 * @return The element as an int
	 */
	public static int getAsInt(JsonElement element, String section, String name) {
		try {
			return element.getAsInt();
		} catch(ClassCastException | IllegalStateException e) {
			throw new ConfigException(section, name, ConfigException.ConfigExceptionType.TYPEERROR);
		} catch(NumberFormatException e) {
			throw new ConfigException(section, name, ConfigException.ConfigExceptionType.NUMFORMAT);
		}
	}
}