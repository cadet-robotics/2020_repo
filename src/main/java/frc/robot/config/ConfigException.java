package frc.robot.config;

/**
 * An exception that gives verbose information on what went wrong
 * during config loading
 * <p>The following may cause this exception: 
 * <ul><li>An incorrect type is read</li>
 * <li>A NullPointerException is thrown during parsing</li></ul></p>
 * @author Alex Pickering
 */
@SuppressWarnings("serial")
public class ConfigException extends RuntimeException {
	String configSection,
		   configElement;
	
	ConfigExceptionType type;
	
	/**
	 * Generally describes the cause of the ConfigException to the exception
	 * 
	 * @author Alex Pickering
	 */
	public enum ConfigExceptionType {
		NULL,
		TYPEERROR,
		NUMFORMAT
	}
	
	/**
	 * 
	 * @param configSection
	 * @param configElement
	 * @param type
	 */
	public ConfigException(String configSection, String configElement, ConfigExceptionType type) {
		super("Failed to load config section: " + configSection + " while parsing element '" + configElement + "'. " + getTypeDescription(type));
		
		this.configSection = configSection;
		this.configElement = configElement;
		this.type = type;
	}
	
	/**
	 * Describes the cause of the ConfigException verbosely
	 * 
	 * @param t The cause or 'type' of the ConfigException
	 * @return A verbose description of the cause
	 */
	private static String getTypeDescription(ConfigExceptionType t) {
		switch(t) {
			case NULL:
				return "A null object was manipulated. Check that any objects that need to be have been initialized (arrays, lists, etc).";
			
			case TYPEERROR:
				return "An incorrect type was read and attempted to be cast. This may be caused by a usually integer value being a malformed string or similar.";
			
			case NUMFORMAT:
				return "A NumberFormatException occurred while parsing the element.";
			
			default:
				return "Something went wrong. We don't know what went wrong, because the provided type lacks a description.";
		}
	}
	
	/**
	 * Gets the section of the config that caused the exception
	 * 
	 * @return The config section
	 */
	public String getConfigSection() {
		return configSection;
	}
	
	/**
	 * Gets the element that caused the exception
	 * 
	 * @return The config element
	 */
	public String getConfigElement() {
		return configElement;
	}
	
	/**
	 * Gets the general cause of the exception
	 * 
	 * @return The cause
	 */
	public ConfigExceptionType getType() {
		return type;
	}
}
