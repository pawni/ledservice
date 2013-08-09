/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.common.model;

import java.io.Serializable;

/**
 * @author Nick Pawlowski
 *
 */
public class LEDStatus implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3112159129465260266L;
	
	private LEDColor color;
	
	public LEDStatus(LEDColor ledColor) {
		color = ledColor;
	}
	
	public LEDStatus(String hex) throws IllegalArgumentException {
		if(hex.length() != 7) {
			throw new IllegalArgumentException("Input Value is no RGB Hex String");
		}
		try {
			int red = Integer.valueOf(hex.substring(1,3), 16);
			int green = Integer.valueOf(hex.substring(3,5), 16);
			int blue = Integer.valueOf(hex.substring(5,7), 16);
			color = new LEDColor(red, green, blue);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("Input Value is no RGB Hex String", e);
		}
		
	}
	
	public boolean isOn() {
		return color.getRed() > 0 || color.getGreen() > 0 || color.getBlue() > 0;
	}
	
	public String getHexColor() {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		String hex = "#" + (red > 16 ? "" : "0") + Integer.toString(red, 16).toUpperCase() +
				(green > 16 ? "" : "0") + Integer.toString(green, 16).toUpperCase() +
				(blue > 16 ? "" : "0") + Integer.toString(blue, 16).toUpperCase();
		return hex;
	}
	
	public String toString() {
		return (isOn() ? "LED on - " : "LED off - ") +
				"Color: " + getHexColor();
	}
	
	public int getRed() {
		return color.getRed();
	}
	
	public int getGreen() {
		return color.getGreen();
	}
	
	public int getBlue() {
		return color.getBlue();
	}
}
