package org.mpiwg.itgroup.geonames;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mpiwg.itgroup.geonames.bo.Geoname;

public class GeonameUtils {

	public static final String GEONAME_SEARCH = "https://openmind-ismi-dev.mpiwg-berlin.mpg.de/geonames/service?method=search&maxRows=1000&mode=json&q=";
	public static final String GEONAME_GET_BY_ID = 
			"https://openmind-ismi-dev.mpiwg-berlin.mpg.de/geonames/service?method=getGeoname&mode=json&geonameId=";
	private static final int BUFFER_SIZE = 1024;
	
	public static List<Geoname> search(String term) throws Exception{
		List<Geoname> list = new ArrayList<Geoname>();
		
		JSONObject jsonRS = JSONFromURL(GEONAME_SEARCH + term);
		
		JSONArray jsonGeoList = jsonRS.getJSONArray("geonames");
		for(int i=0; i < jsonGeoList.length(); i++){
			list.add(json2Geoname(jsonGeoList.getJSONObject(i)));
		}
		return list;
	}
	
	public static JSONObject getJSONGeoname(Integer id) throws Exception{
		JSONObject jsonRS = JSONFromURL(GEONAME_GET_BY_ID + id);
		
		JSONArray jsonGeoList = jsonRS.getJSONArray("geonames");
		if(jsonGeoList.length() > 0)
			return jsonGeoList.getJSONObject(0);
		return null;
	}
	
	//gets the JSONObject at the specified URL
	public static JSONObject JSONFromURL(String url) throws Exception {
		trustAll();
		URL realURL = new URL(url);
		BufferedReader reader = read(realURL);
		int charsRead;
		char[] copyBuffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();
		while ((charsRead = reader.read(copyBuffer, 0, BUFFER_SIZE)) != -1)
			sb.append(copyBuffer, 0, charsRead);
		JSONObject result = new JSONObject( sb.toString() );
		return result;
	}
	
	public static Geoname json2Geoname(JSONObject json){
		Geoname geo = new Geoname();
		try {
			
			geo.setId(json.getInt("geonameId"));

			if(json.has("lng"))
				geo.setLng(json.getDouble("lng"));
				
			if(json.has("lat"))
				geo.setLat(json.getDouble("lat"));
				
			if(json.has("countryCode"))
				geo.setCountryCode(json.getString("countryCode"));
			
			if(json.has("countryName"))
				geo.setCountryName(json.getString("countryName"));
			
			if(json.has("name"))
				geo.setName(json.getString("name"));
			
			if(json.has("toponymName"))
				geo.setToponymName(json.getString("toponymName"));
			
			if(json.has("population"))
				geo.setPopulation(json.getInt("population"));
			
			if(json.has("alternateNames")){
				JSONArray array = json.getJSONArray("alternateNames");
				for(int i=0; i<array.length(); i++){
					geo.getAlternateNames().add(array.getString(i));
				}
			}
			
			JSONObject jsonCls = json.getJSONObject("class");
			
			if(jsonCls.has("fcl"))
				geo.setfCls(jsonCls.getString("fcl"));			
			if(jsonCls.has("fcode"))
				geo.setfCode(jsonCls.getString("fcode"));
			if(jsonCls.has("description"))
				geo.setClsDescription(jsonCls.getString("description"));
			if(jsonCls.has("name"))
				geo.setClsName(jsonCls.getString("name"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return geo;
	}

	//allows us to access https connections without verifying the certificate
	public static void trustAll() throws Exception {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
		};
		
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	
	//get a BufferedReader for a webpage from its URL
	public static BufferedReader read(URL url) throws Exception {
		return new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
	}
}
