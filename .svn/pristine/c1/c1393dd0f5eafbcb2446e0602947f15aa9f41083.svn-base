package de.mpiwg.itgroup.ismi.entry.dataBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.services.ServiceRegistry;

public class SimpleSearchCache extends RoleCache{

	private boolean mapDirty;
	private Map<Attribute, Entity> attResultMap = new HashMap<Attribute, Entity>();
	
	private Map<Long, Attribute> attMap = new HashMap<Long, Attribute>();
	private Map<Long, Entity> entMap = new HashMap<Long, Entity>();
	private Map<String, String> descriptionMap = new HashMap<String, String>();
	private List<Long> ignoredAttIdList = new ArrayList<Long>();

	
	public SimpleSearchCache(ServiceRegistry sr){
		super(sr);
	}	
	
	@Override
	public void reset(){
		super.reset();
		this.attResultMap = new HashMap<Attribute, Entity>();
		this.mapDirty = true;
		this.attMap = new HashMap<Long, Attribute>();
		this.entMap = new HashMap<Long, Entity>();
		//this.ignoredIdList = new ArrayList<Long>();
	}

	public List<SelectItem> getAllPersons(){
		List<SelectItem> list = new ArrayList<SelectItem>();
		for(Entity ent : getWrapper().getEntitiesByDef("PERSON")){
			list.add(
			new SelectItem(ent.getId(), ent.getOwnValue() 
							+" [" + ent.getId() + "]"));
		}
		return list;
	}
	
	public List<SelectItem> getPersonsByRole(String role){
		List<SelectItem> list = new ArrayList<SelectItem>();
		for(Entity ent : getWrapper().getEntitiesByDef("PERSON")){
			if(roleContainsPersonId(role, ent.getId())){
				list.add(
						new SelectItem(
								ent.getId(), 
								ent.getOwnValue() +" [" + ent.getId() + "]"));	
			}
		}
		return list;
	}
	
	public List<Entity> getPersonListByRole(String role){
		List<Entity> list = new ArrayList<Entity>();
		for(Entity ent : getWrapper().getEntitiesByDef("PERSON")){
			if(roleContainsPersonId(role, ent.getId())){
				list.add(ent);	
			}
		}
		return list;
	}
	
	
	public Attribute getAttById(Long id){
		for(Attribute att : attResultMap.keySet()){
			if(att.getId().compareTo(id) == 0)
				return att;
		}
		return null;
	}
	
	public boolean isMapDirty() {
		return mapDirty;
	}

	public void setMapDirty(boolean mapDirty) {
		this.reset();
		this.mapDirty = mapDirty;
	}

	public Map<Attribute, Entity> getAttResultMap() {
		return attResultMap;
	}

	public void setAttResultMap(Map<Attribute, Entity> map) {
		this.reset();
		this.attResultMap = map;
		this.mapDirty = false;
	}
	
	public boolean containsAttribute(Attribute att){
		return this.attMap.containsKey(att.getId());
	}
	
	public void setTuple(Entity ent, Attribute att, String description){
		this.attMap.put(att.getId(), att);
		this.entMap.put(att.getId(), ent);
		this.descriptionMap.put(att.getId() + "-" + ent.getId(), description);
	}
	
	public boolean ignoreAttribute(Attribute att){
		return this.getIgnoredAttIdList().contains(att.getId());
	}
	
	public Map<Long, Attribute> getAttMap() {
		return attMap;
	}

	public void setAttMap(Map<Long, Attribute> attMap) {
		this.attMap = attMap;
	}

	public Map<Long, Entity> getEntMap() {
		return entMap;
	}

	public void setEntMap(Map<Long, Entity> entMap) {
		this.entMap = entMap;
	}	

	public Map<String, String> getDescriptionMap() {
		return descriptionMap;
	}

	public void setDescriptionMap(Map<String, String> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}

	public List<Long> getIgnoredAttIdList() {
		return this.ignoredAttIdList;
	}
}
