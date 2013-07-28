/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.common.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * @author Nick Pawlowski
 *
 */
public class LEDColor extends Color implements Serializable {

	/**
	 * @param r
	 * @param g
	 * @param b
	 */
	public LEDColor(int r, int g, int b) {
		super(r, g, b);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3231185976901103536L;

}
