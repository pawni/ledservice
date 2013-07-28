/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.ledcontrol;

import de.pawni.ledservice.common.model.LEDStatus;

/**
 * @author Nick Pawlowski
 *
 */
public class LEDController {
	private static LEDController instance;
	
	private LEDStatus status;
	
	private final int pinRed;
	private final int pinGreen;
	private final int pinBlue;
	
	private final ServoPin redPin;
	private final ServoPin greenPin;
	private final ServoPin bluePin;
	
	private LEDController() {
		pinRed = 2;
		pinGreen = 6;
		pinBlue = 5;
		
		redPin = new ServoPin(pinRed);
		greenPin= new ServoPin(pinGreen);
		bluePin = new ServoPin(pinBlue);
		
	}
	
	public static LEDController getInstance() {
		if(instance == null) {
			instance = new LEDController();
		}
		return instance;
	}
	
	public void setRGB(LEDStatus status) {
		setRGB(status.getRed(), status.getGreen(), status.getBlue());
		this.status = status;
	}
	public LEDStatus getStatus() {
		return status;
	}
	
	private void setRGB(int red, int green, int blue) {
		System.out.println("Setting red to "+red);
		redPin.setValue(red);
		System.out.println("Setting red to "+green);
		greenPin.setValue(green);
		System.out.println("Setting red to "+blue);
		bluePin.setValue(blue);
	}
}
