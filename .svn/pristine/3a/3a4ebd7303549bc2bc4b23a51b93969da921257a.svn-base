package org.mpiwg.itgroup.escidoc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class BootcampClient {

	public static String url = "http://localhost:8080/";
	
	
	public static void testPost() throws JSONException, ClientProtocolException, IOException{
		HttpPost httpBase = new HttpPost("http://localhost:8080/");
		
		JSONObject json = new JSONObject();
		json.put("name", "Jorge hahaha");
		json.put("text", "jhakjhskhjdsahjdsahjkdsa");
		
		
		
		InputStream body = new ByteArrayInputStream(json.toString().getBytes());
		if (HttpEntityEnclosingRequestBase.class.isInstance(httpBase)) {
			if (body != null) {
				HttpEntity entity = new InputStreamEntity(body, -1);
				((HttpEntityEnclosingRequestBase) httpBase).setEntity(entity);
			}
		}
		
		HttpResponse status = (new DefaultHttpClient()).execute(httpBase);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("TESTING BEGINING*******");
			testPost();
			System.out.println("TESTING END************");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
