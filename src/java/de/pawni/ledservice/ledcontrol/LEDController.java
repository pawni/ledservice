/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.ledcontrol;

import java.util.Date;

import de.pawni.ledservice.common.errors.EntryNotFoundException;
import de.pawni.ledservice.common.model.LEDColor;
import de.pawni.ledservice.common.model.LEDStatus;
import de.pawni.ledservice.common.properties.PropertyHandler;

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
		try{
			pinRed = Integer.valueOf(PropertyHandler.getProperty("servo.pins.red"));
			pinGreen = Integer.valueOf(PropertyHandler.getProperty("servo.pins.green"));
			pinBlue = Integer.valueOf(PropertyHandler.getProperty("servo.pins.blue"));
		} catch(EntryNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
		
		
		redPin = new ServoPin(pinRed);
		greenPin= new ServoPin(pinGreen);
		bluePin = new ServoPin(pinBlue);
		
		this.status = new LEDStatus("#000000");
		setRGB(0, 0, 0);
	}
	
	public static LEDController getInstance() {
		if(instance == null) {
			instance = new LEDController();
		}
		return instance;
	}
	
	public void setRGB(LEDStatus status) {
		setRGB(status.getRed(), status.getGreen(), status.getBlue());
	}
	public LEDStatus getStatus() {
		return status;
	}
	
	private void setRGB(int red, int green, int blue) {
		System.out.println("Setting red to "+red);
		redPin.setValue(red);
		System.out.println("Setting green to "+green);
		greenPin.setValue(green);
		System.out.println("Setting blue to "+blue);
		bluePin.setValue(blue);
		
		this.status = new LEDStatus(new LEDColor(red, green, blue));
	}
	
	public void fadeToRGB(LEDStatus ledStatus) {
		LEDStatus old = this.status;
		int redDiff = ledStatus.getRed() - status.getRed();
		System.out.println("Difference of red = "+redDiff);
		int greenDdiff = ledStatus.getGreen() - status.getGreen();
		System.out.println("Difference of green = "+greenDdiff);
		int blueDiff = ledStatus.getBlue() - status.getBlue();
		System.out.println("Difference of blue = "+blueDiff);
		
		
		int maxDiff = Math.max(redDiff, Math.max(blueDiff, greenDdiff));
		
		for(int i = 1; i <= maxDiff; i++) {
			System.out.println("Time: "+(new Date()).toString());
			System.out.println("Fading from "+status+" to "+ledStatus+" - step "+i+" of "+maxDiff);
			setRGB(Math.round(old.getRed() + (float)redDiff/maxDiff*i),
					Math.round(old.getGreen() + (float)greenDdiff/maxDiff*i),
					Math.round(old.getBlue() + (float)blueDiff/maxDiff*i));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//what happened?
			}
			
		}
		setRGB(ledStatus);
	}
}
