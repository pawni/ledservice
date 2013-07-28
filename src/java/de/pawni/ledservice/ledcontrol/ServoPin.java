/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.ledcontrol;

/**
 * Implementation to encapsule a ServoBlaster pin
 * @author Nick Pawlowski
 *
 */
public class ServoPin {
	private int pin;
	private int value;
	
	public ServoPin(int pin) {
		if(pin > 7 || pin < 0) {
			throw new IllegalArgumentException("Currently we can only handle pins 0-7.");
		}
		this.pin = pin;
	}
	
	public void setValue(int val) {
		if(val < 0) {
			val = 0;
		} else if(val > 249) {
			val = 249;
		}
		this.value = val;
		ServoBlasterHandler.writeToPin(pin, val);
	}
	
	public int getValue() {
		return value;
	}
	
	public void turnOn() {
		setValue(249);
	}
	
	public void turnOff() {
		setValue(0);
	}
}
