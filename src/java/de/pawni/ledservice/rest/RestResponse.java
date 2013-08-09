/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

/**
 * @author Nick Pawlowski
 *
 */
public class RestResponse {
	private final StatusLine status;
	private final String body;
	
	public RestResponse(HttpResponse response) throws IllegalStateException, IOException {
		status = response.getStatusLine();
		body = readBody(response.getEntity().getContent());
	}
	
	private String readBody(InputStream in) throws IOException {
		String body = "";
		BufferedReader rd = new BufferedReader (new InputStreamReader(in));
        String line = "";
        while ((line = rd.readLine()) != null) {
          body += line;
        }
        return body;
	}
	
	public String getBody() {
		return body;
	}
	
	public int getStatusCode() {
		return status.getStatusCode();
	}
	
	public String getStatus() {
		return status.getStatusCode() + " - " + status.getReasonPhrase();
	}
}
