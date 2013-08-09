/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.common.properties;

import java.util.Properties;

import de.pawni.ledservice.common.errors.EntryNotFoundException;

/**
 * @author Nick Pawlowski
 *
 */
public class PropertyHandler {
	private static Properties properties;
	
	// mock implementation to change other code;
	static {
		properties = new Properties();
		properties.put("servo.pins.red", "2");
		properties.put("servo.pins.green", "6");
		properties.put("servo.pins.blue", "5");
		properties.put("live.clientid", "YOUR_CLIENT_ID");
	}
	
	public static String getProperty(String key) throws EntryNotFoundException{
		String val = properties.getProperty(key);
		if(val == null) {
			throw new EntryNotFoundException("Property with key " + key + " not found.");
		}
		return val;
	}
}
