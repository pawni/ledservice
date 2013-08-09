/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.live;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import de.pawni.ledservice.common.model.ISO8601DateFormatter;

/**
 * @author Nick Pawlowski
 *
 */
public class Calendar {
	//private DateFormat format = new ISO8601DateFormatter();
	
	private String id;
	private String name;
	private String description;
	private Date createdTime;
	private Date updatedTime;
	private boolean isDefault;
	private String subscriptionLocation;
	private String permissions;
	// private ...Object from;
	
	private List<CalendarEvent> events;
	
	public Calendar(JSONObject cal, List<CalendarEvent> events) throws JSONException {
		this.id = (String) cal.get("id");
		this.name = (String) cal.get("name");
		this.description = (String) cal.get("description");
		this.permissions = (String) cal.get("permissions");
		if(cal.get("subscription_location") == JSONObject.NULL ) {
			this.subscriptionLocation = null;
		} else {
			this.subscriptionLocation = (String) cal.get("subscription_location");
		}
		this.isDefault = (boolean) cal.get("is_default");
		try {
			this.createdTime = ISO8601DateFormatter.toDate((String) cal.get("created_time"));
			this.updatedTime = ISO8601DateFormatter.toDate((String) cal.get("updated_time"));
		} catch (ParseException e) {
			throw new IllegalArgumentException("could not parse one date: ", e);
		}
		this.events = events;
	}
	
	public String getCalenderId() {
		return id;
	}
	
	public List<CalendarEvent> getEvents() {
		return events;
	}
}
