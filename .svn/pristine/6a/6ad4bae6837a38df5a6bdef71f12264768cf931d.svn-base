package de.mpiwg.itgroup.diva.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONArrayParam {
	
	private JSONArray json;
	
    public JSONArrayParam(String string) throws WebApplicationException {
    	
        try {        	
        	json = new JSONArray(string);
        } catch (JSONException e) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
                    .entity("Couldn't parse JSON string: " + e.getMessage())
                    .build());
        }
    }

	public JSONArray getArray() {
		return json;
	}
}
