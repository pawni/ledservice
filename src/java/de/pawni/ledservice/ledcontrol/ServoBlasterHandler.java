/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.ledcontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Nick Pawlowski
 *
 */
public class ServoBlasterHandler {
	private static final String servo = "/dev/servoblaster";
	private static final File servoFile = new File(servo);
	
	public static synchronized void writeToPin(int pin, int value) {
		if(pin < 0 || pin > 7) {
			throw new IllegalArgumentException("ServoBlaster only supports pins from 0 to 7.");
		}
		if(value < 0) {
			value = 0;
		} else if(value > 249) {
			value = 249;
		}
		try {
			OutputStream out = new FileOutputStream(servoFile);
			OutputStreamWriter writer = new OutputStreamWriter(out);
			writer.write(pin+"="+value+"\n");
			writer.flush();
			writer.close();
		} catch(IOException e) {
			System.out.println("IOError: "+e.toString());
		}
		
	}
}