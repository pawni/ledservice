/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.pawni.ledservice.common.model.LEDStatus;
import de.pawni.ledservice.live.Calendar;
import de.pawni.ledservice.live.CalendarAccessor;
import de.pawni.ledservice.live.CalendarEvent;

/**
 * @author Nick Pawlowski
 *
 */
public class CalenderScheduler extends TimerTask{
	private CalendarAccessor accessor;
	private int reccurenceRate;
	private static String calId = "YOUR-CALENDAR-ID";
	private static Timer timer;
	
	public CalenderScheduler(int reccurenceRate) {
		accessor = CalendarAccessor.getInstance();
		this.reccurenceRate = reccurenceRate; //minutes
		timer = new Timer(true);
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		Calendar current = accessor.getCalender(calId);
		Date nextRun = new Date((new Date()).getTime() + reccurenceRate*60*1000);
		for(CalendarEvent event : current.getEvents()) {
			if(!event.getNotificationTime().after(nextRun) && event.getNotificationTime().after(new Date())) {
				try {
					LEDStatus status = new LEDStatus(event.getDescription());
					ColorChanger c = new ColorChanger(status);
					timer.schedule(c, event.getNotificationTime());
					System.out.println("Scheduled to set leds to " + status + " at " + event.getNotificationTime());
				} catch(IllegalArgumentException e) {
					ColorChanger c = new ColorChanger();
					timer.schedule(c, event.getNotificationTime());
					System.out.println("Scheduled to flash at " + event.getNotificationTime());
				}
			}
		}
		
	}
}
