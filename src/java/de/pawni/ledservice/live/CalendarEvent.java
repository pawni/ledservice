/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.live;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import de.pawni.ledservice.common.model.ISO8601DateFormatter;

/**
 * @author Nick Pawlowski
 *
 */
public class CalendarEvent {
	//private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
	
	private String id;
	private String name;
	private String description;
	private Date createdTime;
	private Date updatedTime;
	private Date start;
	private Date end;
	private String calendar_id;
	private String location;
	private boolean isAllDay;
	private boolean isRecurrent;
	private String recurrence = null;
	private int reminderTime = -1;
	private String availibility;
	private String visibility;
	//private ... from;
	
	public CalendarEvent(JSONObject event) throws JSONException {
		this.id = (String) event.get("id");
		this.name = (String) event.get("name");
		this.description = (String) event.get("description");
		try {
			createdTime = ISO8601DateFormatter.toDate((String) event.get("created_time"));
			updatedTime = ISO8601DateFormatter.toDate((String) event.get("updated_time"));
			start = ISO8601DateFormatter.toDate((String) event.get("start_time"));
			end = ISO8601DateFormatter.toDate((String) event.get("end_time"));
		} catch (ParseException e) {
			throw new IllegalArgumentException("could not parse one date: ", e);
		}
		this.calendar_id = (String) event.get("calendar_id");
		this.location = (String) event.get("location");
		this.availibility = (String) event.get("availability");
		this.visibility = (String) event.get("visibility");
		this.isAllDay = (boolean)event.get("is_all_day_event");
		this.isRecurrent = (boolean) event.get("is_recurrent");
		if(this.isRecurrent) {
			this.recurrence = (String) event.get("recurrence");
		}
		if(event.get("reminder_time") != null) {
			this.reminderTime = (int) event.get("reminder_time");
		}
		System.out.println("Created Event "+ name + " with id "+id+" with notificationTime "+getNotificationTime());
	}
	
	public Date getNotificationTime() {
		return (reminderTime < 0 ) ? start : new Date(start.getTime() - reminderTime * 1000 * 60);
	}
	
	public String getDescription() {
		return description;
	}
}
