package de.mpiwg.itgroup.diva.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

public class JSONEntity {

	public Long id;
	public Map<String, String> attrs = new HashMap<String, String>();
	
	public JSONEntity(JSONObject json, Long id) throws JSONException{
		this.id = id;
		for(String attName : JSONObject.getNames(json)){
			if(!StringUtils.equals(attName, "id")){
				this.attrs.put(attName, json.getString(attName));	
			}
		}
	}
	
	public Entity updateEntity(Entity ent){
		
		for(String attName : this.attrs.keySet()){
			
			if(ent.getAttributeByName(attName) == null){
				//TODO content type ???
				ent.addAttribute(new Attribute(attName, "text", attrs.get(attName)));
			}else{
				ent.getAttributeByName(attName).setValue(attrs.get(attName));
			}
		}
		
		return ent;
	}
	

	public static List<JSONEntity> json2EntityList(JSONObject json) throws JSONException{
		
		List<JSONEntity> rs = new ArrayList<JSONEntity>();
		
		String[] idList = JSONObject.getNames(json);
		
		for(String idString : idList){
			Long id = Long.parseLong(idString);
			rs.add(new JSONEntity(json.getJSONObject(idString), id));
		}
		
		return rs;
		
	}
	
}
