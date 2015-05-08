package de.mpiwg.itgroup.ismi.entry.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.utils.EntitySortByNormalizedOwnValue;

public class BiographyBean extends AbstractBean{

	private boolean dirty = true;
	
	private Map<String, List<Biography>> biographyMap;
	private List<String> indexList = null;
	
	public BiographyBean(){
	}
	
	private void load(){
		this.dirty = false;
		this.biographyMap = new HashMap<String, List<Biography>>();
		
		
		
		List<Entity> rs = getAppBean().getWrapper().getEntitiesByAtt("PERSON", "url", "http://islamsci.mcgill.ca/RASI/BEA", -1, true);
		
		for(Entity ent : rs){
			String normalizedOv = ent.getNormalizedOwnValue();
			String index = new String(normalizedOv.charAt(0) + "");
			index = index.toUpperCase();
			
			if(!this.biographyMap.containsKey(index)){
				this.biographyMap.put(index, new ArrayList<BiographyBean.Biography>());
			}
			this.biographyMap.get(index).add(new Biography(ent, getAppBean().getWrapper()));
		}
		
		for(String index : this.biographyMap.keySet()){
			Collections.sort(this.biographyMap.get(index));
		}
		
		this.indexList = new ArrayList<String>(this.biographyMap.keySet());
		Collections.sort(indexList);
	}
	
	public List<String> getIndexList(){
		if(dirty)
			this.load();
		return this.indexList;
	}
	
	public Map<String, List<Biography>> getBiographyMap(){
		if(dirty)
			this.load();
		return this.biographyMap;
	}
	
	public void makeDirty(){
		this.dirty = true;
	}
	
	public class Biography implements Comparable<Biography>{
		private Long personId;
		private String biographyUrl;
		private String label;
		private String normalizedLabel;
		
		public Biography(Entity person, WrapperService wrapper){
			Attribute att = wrapper.getAttributeByName(person.getId(), "url");
			
			this.label = person.getOwnValue();
			this.personId = person.getId();
			this.biographyUrl = att.getOwnValue();
			this.normalizedLabel = person.getNormalizedOwnValue();
		}
		
		public Long getPersonId() {
			return personId;
		}
		public String getBiographyUrl() {
			return biographyUrl;
		}
		public String getLabel() {
			return label;
		}
		
		public int compareTo(Biography o) {
			return this.normalizedLabel.compareTo(o.normalizedLabel);
			
		}
		
		@Override
		public String toString(){
			return "[" + this.personId + "] " + this.label + " - " + this.biographyUrl; 
		}
	}
	
}
