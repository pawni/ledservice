/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.service;

import java.util.TimerTask;

import de.pawni.ledservice.common.model.LEDColor;
import de.pawni.ledservice.common.model.LEDStatus;
import de.pawni.ledservice.ledcontrol.LEDController;

/**
 * @author Nick Pawlowski
 *
 */
public class ColorChanger extends TimerTask {
	private final LEDStatus status;
	private final boolean flashing;
	private final int flashingDuration;
	private final int interval = 100;
	
	public ColorChanger(LEDStatus status) {
		this.status = status;
		this.flashing = false;
		this.flashingDuration = 0;
	}
	
	public ColorChanger() {
		this.flashing = true;
		this.status = null;
		this.flashingDuration = 10; // seconds
	}
	
	public ColorChanger(int duration) {
		this.flashing = true;
		this.status = null;
		this.flashingDuration = duration; // seconds
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		if(flashing) {
			LEDStatus current = LEDController.getInstance().getStatus();
			LEDStatus flash = new LEDStatus(new LEDColor(255 - current.getRed(),
											255 - current.getGreen(), 255 - current.getBlue()));
			for(int i = 0 ; i < flashingDuration*1000/interval; i++) {
				LEDController.getInstance().setRGB(flash);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
				LEDController.getInstance().setRGB(current);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
		} else {
			LEDController.getInstance().fadeToRGB(status);
		}
	}

}
