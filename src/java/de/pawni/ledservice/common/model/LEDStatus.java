/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.common.model;

import java.awt.Color;

/**
 * @author Nick Pawlowski
 *
 */
public class LEDStatus {
	
	private Color color;
	
	public LEDStatus(Color ledColor) {
		color = ledColor;
	}
	
	public LEDStatus(String hex) throws IllegalArgumentException {
		if(hex.length() != 7) {
			throw new IllegalArgumentException("Input Value is no RGB Hex String");
		}
		int red = Integer.valueOf(hex.substring(1,3), 16);
		int blue = Integer.valueOf(hex.substring(3,5), 16);
		int green = Integer.valueOf(hex.substring(5,7), 16);
		
		color = new Color(red, green, blue);
		
	}
	
	public boolean isOn() {
		return color.getRGB() > 0;
	}
	
	public String getHexColor() {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		String hex = "#" + Integer.toString(red, 16).toUpperCase() +
				Integer.toString(green, 16).toUpperCase() +
				Integer.toString(blue, 16).toUpperCase();
		return hex;
	}
	
	public String toString() {
		return isOn() ? "LED on" : "LED off" + "\n" + 
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
