/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.common.errors;

/**
 * @author Nick Pawlowski
 *
 */
public class EntryNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7497539864608293380L;

	public EntryNotFoundException(String message) {
		super(message);
	}
}
