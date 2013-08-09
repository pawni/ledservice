/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice.rest;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.json.JSONObject;

/**
 * @author Nick Pawlowski
 *
 */
public class RestClient {
	public RestResponse doGet(String url, Map<String, String> header) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        if(header != null) {
	        for(String key : header.keySet() ) {
	        	request.addHeader(key, header.get(key));
	        }
		}
        HttpResponse response = client.execute(request);
        return new RestResponse(response);
	}
	
	public RestResponse doPost(String url, Map<String, String> header, String body) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		if(header != null) {
	        for(String key : header.keySet() ) {
	        	request.addHeader(key, header.get(key));
	        }
		}
        if(body != null && body.trim().length() > 0) {
        	request.setEntity(new StringEntity(body));
        }
        HttpResponse response = client.execute(request);
        return new RestResponse(response);
	}
	
	public RestResponse doPost(String url, Map<String, String> header, JSONObject body) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		if(header != null) {
	        for(String key : header.keySet() ) {
	        	request.addHeader(key, header.get(key));
	        }
		}
        if(body != null && body.length()>0) {
        	request.setEntity(new StringEntity(body.toString()));
        }
        BasicHttpContext context = new BasicHttpContext();
        HttpResponse response = client.execute(request, context);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ||
        		response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY ) {
	        HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute( 
	                ExecutionContext.HTTP_REQUEST);
	        HttpHost currentHost = (HttpHost)  context.getAttribute( 
	                ExecutionContext.HTTP_TARGET_HOST);
	        String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
	        System.out.println(currentUrl);
        }
        
        return new RestResponse(response);
	}
}
