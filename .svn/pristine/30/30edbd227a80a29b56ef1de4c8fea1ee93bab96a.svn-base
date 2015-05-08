package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.mpiwg.itgroup.ismi.servlets.AbstractServletMethod;

public class AbstractServletJSONMethod extends AbstractServletMethod{

	protected static String RESPONSE = "response";
	protected static String RESPONSE_INFO = "response_info";
	protected static String OK = "ok";
	protected static String EXCEPTION = "exception";
	protected static String METHOD = "method";
	protected static String RUNTIME = "runtime";
	protected static String PARAMETERS = "parameters";
	
	protected static JSONObject finallyExecution(JSONObject json, JSONObject parameters, long startExecution){
		if(json == null){
			json = new JSONObject();
		}
		
		if(!json.has(RESPONSE)){
			try {
				json.put(RESPONSE, OK);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			json.put(PARAMETERS, parameters);
			json.put(RUNTIME, (System.currentTimeMillis() - startExecution));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject parametersToString(Map<String, ?> map){
		JSONObject json = new JSONObject();
		try {
			for(String key : map.keySet()){
				String[] values = (String[])map.get(key);
				JSONArray array = new JSONArray();
				for(int i=0; i<values.length; i++){
					array.put(values[i]);
				}	
				json.put(key, array);
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}
