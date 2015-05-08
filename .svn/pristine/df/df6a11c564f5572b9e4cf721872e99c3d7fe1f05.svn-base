package de.mpiwg.itgroup.ismi.auxObjects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

//[upgrade]import org.mpi.openmind.repository.bo.Entity;

/**
 * @author jurzua
 * 
 * change [25.06.2010-jurzua] replace results and attributes with a tuple entity-att  
 * 
 * TODO analyze if the attribute is necessary... I think the description should be enough.
 * 
 */
public class ResultSet implements Serializable{
	
	private static final long serialVersionUID = -6799519951054874744L;
	
	private String name;
	private List<Entity> results = new ArrayList<Entity>();
	private Map<Long, Attribute> attMap = new HashMap<Long, Attribute>();
	private Map<Long, String> descriptionMap = new HashMap<Long, String>();
	
	public void setTuple(Entity ent, Attribute att){
		if(!this.results.contains(ent)){
			this.results.add(ent);
		}
		this.attMap.put(ent.getId(), att);
	}
	
	public void setDescription(Long id, String desc){
		this.descriptionMap.put(id, desc);
	}
	public void setDescription(Entity ent, String desc){
		this.descriptionMap.put(ent.getId(), desc);
	}
	
	public ResultSet(String ot) {
		name=ot;
	}
	
	/**
	 * @return Results set als Array of DisplayEntity. 
	 * Array is needed because the DataTable-Object of ICEFaces expects arrays and not Lists.
	 */
	public DisplayEntity[] getResultsArray() {
		DisplayEntity[] retArray = new DisplayEntity[results.size()];
		
		int i = 0;
		for(Entity ent : results){
			retArray[i] = new DisplayEntity(ent);
			i++;
		}
		
		return retArray;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Entity> getResults() {
		return results;
	}

	public void setResults(List<Entity> results) {
		this.results = results;
	}

	public Map<Long, Attribute> getAttMap() {
		return attMap;
	}

	public void setAttMap(Map<Long, Attribute> attMap) {
		this.attMap = attMap;
	}
	
	public Map<Long, String> getDescriptionMap() {
		return descriptionMap;
	}

	public void setDescriptionMap(Map<Long, String> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}
}
