/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.live;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import de.pawni.ledservice.common.errors.EntryNotFoundException;
import de.pawni.ledservice.common.properties.PropertyHandler;
import de.pawni.ledservice.rest.RestClient;
import de.pawni.ledservice.rest.RestResponse;

/**
 * @author Nick Pawlowski
 *
 */
public class CalendarAccessor {
	private final String APIURL = "https://apis.live.net/v5.0/";
	private final String AUTHURL = "https://login.live.com/oauth20_authorize.srf";
	
	private final String clientId;
	private final String scope = "wl.signin%20wl.basic%20wl.calendars";
	
	private String accessToken;
	private Date expiry;
	
	private CalendarAccessor() {
		try {
			clientId = PropertyHandler.getProperty("live.clientid");
		} catch(EntryNotFoundException e) {
			throw new IllegalStateException("property not found: " + e.toString(), e);
		}
		try {
			renewAccessToken();
		} catch(FailingHttpStatusCodeException e) {
			throw new IllegalStateException("could not get access token", e);
		} catch(MalformedURLException e) {
			throw new IllegalStateException("could not get access token", e);
		} catch(IOException e) {
			throw new IllegalStateException("could not get access token", e);
		}
		
		
	}
	
	private static CalendarAccessor instance = null;
	
	public static synchronized CalendarAccessor getInstance() {
		if (instance == null) {
			instance = new CalendarAccessor();
		}
		return instance;
	}
	
	public void renewAccessToken() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String url = AUTHURL + "?client_id="+clientId+"&scope="+scope+
				"&response_type=token&redirect_uri=http://pawni.de/defaultsite";
		
		WebClient wc = new WebClient();
		HtmlPage p = wc.getPage(url);
		HtmlForm form = p.getFormByName("f1");
		HtmlTextInput login = form.getInputByName("login");
		HtmlPasswordInput passwd = form.getInputByName("passwd");
		login.setValueAttribute("LIVE_USER");
		passwd.setValueAttribute("LIVE_PASSWORD");
		HtmlSubmitInput submit = form.getInputByName("SI");
		HtmlPage p2 = submit.click();
		String accessToken = p2.getUrl().getRef().substring(13);
		accessToken = accessToken.substring(0,accessToken.length()-76);
		this.expiry = new Date(new Date().getTime()+60*60*1000);
		this.accessToken = accessToken;
	}
	
	public Calendar getCalender(String calId) {
		if(expiry.before(new Date()))
		{
			try {
				renewAccessToken();
			} catch(Exception e) {
				throw new IllegalStateException("could not get access token", e);
			}
		}
		RestClient client = new RestClient();
		String url = APIURL + calId + "?access_token="+accessToken;
		try {
			RestResponse resp = client.doGet(url, null);
			JSONObject body = new JSONObject(resp.getBody());
			Calendar cal = new Calendar(body, getEvents(calId));
			return cal;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CalendarEvent> getEvents(String calId) {
		List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		if(expiry.before(new Date()))
		{
			try {
				renewAccessToken();
			} catch(Exception e) {
				throw new IllegalStateException("could not get access token", e);
			}
		}
		RestClient client = new RestClient();
		String url = APIURL + calId + "/events?access_token="+accessToken;
		try {
			RestResponse resp = client.doGet(url, null);
			JSONObject body = new JSONObject(resp.getBody());
			JSONArray jsonEvents = body.getJSONArray("data");
			
			for(int i = 0 ; i<jsonEvents.length(); i++) {
				JSONObject jsonEvent = jsonEvents.getJSONObject(i);
				CalendarEvent event = new CalendarEvent(jsonEvent);
				events.add(event);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return events;
	}
}
