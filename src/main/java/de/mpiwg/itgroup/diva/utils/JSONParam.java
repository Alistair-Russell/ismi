package de.mpiwg.itgroup.diva.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParam {

	private JSONObject json;
	
    public JSONParam(String string) throws WebApplicationException {
    	
        try {        	
        	json = new JSONObject(string);
        } catch (JSONException e) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
                    .entity("Couldn't parse JSON string: " + e.getMessage())
                    .build());
        }
    }

	public JSONObject getObj() {
		return json;
	}
}
