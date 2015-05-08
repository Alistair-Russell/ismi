package de.mpiwg.itgroup.ismi.utils.templates;

import java.util.List;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

public class CodexTemplate {

	public Long id;
	public String ov;
	public String identifier;
	public String mpiwgId;
	public String indexmetaFolder;
	public String collection;
	public String repository;
	public String city;
	public String country;
	
	public CodexTemplate(Entity entity, WrapperService om) {
		this.id = entity.getId();
		this.ov = entity.getOwnValue();
		
		Attribute tmp = om.getAttributeByName(entity.getId(), "identifier");
		this.identifier = (tmp != null) ? tmp.getValue() : null;
		
		tmp = om.getAttributeByName(entity.getId(), "mpiwg_id");
		this.mpiwgId = (tmp != null) ? tmp.getValue() : null;
		
		tmp = om.getAttributeByName(entity.getId(), "indexmeta_folder");
		this.indexmetaFolder = (tmp != null) ? tmp.getValue() : null;
		
		List<Entity> list =  om.getTargetsForSourceRelation(entity.getId(), "is_part_of", "COLLECTION", 1);
		if(list.size() > 0){
			this.collection = list.get(0).getOwnValue();
			list =  om.getTargetsForSourceRelation(list.get(0).getId(), "is_part_of", "REPOSITORY", 1);
			if(list.size() > 0){
				this.repository = list.get(0).getOwnValue();
				list =  om.getTargetsForSourceRelation(list.get(0).getId(), "is_in", "PLACE", 1);
				if(list.size() > 0){
					this.city  = list.get(0).getOwnValue();
					list =  om.getTargetsForSourceRelation(list.get(0).getId(), "is_part_of", "PLACE", 1);
					if(list.size() > 0){
						this.country  = list.get(0).getOwnValue();	
					}
				}
			}
		}
	}
	
	public Long getId(){
		return this.id;
	}
	
	public String getOv(){
		return this.ov;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getCollection() {
		return collection;
	}

	public String getRepository() {
		return repository;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}
	
	
}
