package de.mpiwg.itgroup.ismi.entry.utils;

import java.util.ArrayList;
import java.util.List;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

public class PrivacityUtils {

	
	public static List<Entity> changePrivacity4Person(Entity person, Boolean isPublic, WrapperService wrapper){
		List<Entity> saveList = new ArrayList<Entity>();
		
		if(person.isLightweight()){
			person = wrapper.getEntityContent(person);
		}
		
		boolean privacity = false;
		if(isPublic == null){
			privacity = !person.getIsPublic();
		}else{
			privacity = isPublic;
		}
		
		person.setIsPublic(privacity);
		saveList.add(person);
		
		List<Relation> relList = null;
		//loading relations
		if(privacity){
			relList = new ArrayList<Relation>(person.getSourceRelations());
			for (Relation rel : relList) {
				String relName = rel.getOwnValue();
				if (relName.equals("was_born_in") || 
						relName.equals("lived_in") || 
						relName.equals("was_student_of") || 
						relName.equals("has_role") || 
						relName.equals("died_in")) {
					Entity target = wrapper.getEntityById(rel.getTargetId());
					//target = wrapper.getEntityContent(target);
					target.setIsPublic(privacity);
					saveList.add(target);
				}
			}
		}
		
		relList = new ArrayList<Relation>(person.getTargetRelations());
		for (Relation rel : relList) {
			String relName = rel.getOwnValue();
			//title were be not included into this list
			if (relName.equals("is_alias_name_of") || relName.equals("is_prime_alias_name_of")) {
				Entity source = wrapper.getEntityById(rel.getSourceId());
				//source = wrapper.getEntityContent(source);
				source.setIsPublic(privacity);
				saveList.add(source);
			}
		}
		
		return saveList;
	}
	
	public static List<Entity> changePrivacity4Title(Entity title, Boolean isPublic, WrapperService wrapper){
		List<Entity> saveList = new ArrayList<Entity>();
	
		/*
		if(title.isLightweight()){
			title = wrapper.getEntityContent(title);
		}*/
		
		boolean privacity = false;
		if(isPublic == null){
			privacity = !title.getIsPublic();
		}else{
			privacity = isPublic;
		}
		
		title.setIsPublic(privacity);
		saveList.add(title);
		
		if(privacity){
			for(Entity ent : wrapper.getTargetsForSourceRelation(title, "has_subject", "SUBJECT", 1)){
				//if(ent.isLightweight())
				//	ent = wrapper.getEntityContent(ent);
				ent.setIsPublic(privacity);
				saveList.add(ent);
			} 
			
			for(Entity ent : wrapper.getTargetsForSourceRelation(title, "was_created_in", "PLACE", 1)){
				//if(ent.isLightweight())
				//	ent = wrapper.getEntityContent(ent);
				ent.setIsPublic(privacity);
				saveList.add(ent);
			}	
		}

		for(Entity ent : wrapper.getSourcesForTargetRelation(title, "is_alias_title_of", "ALIAS", -1)){
			//if(ent.isLightweight())
			//	ent = wrapper.getEntityContent(ent);
			ent.setIsPublic(privacity);
			saveList.add(ent);
		}
		
		return saveList;
	}
	
	
	/**
	 * 437080
	 * al-Taḏkiraẗ fī ʿilm al-hayʾaẗ 
	 * BIT(1)
	 * create:
	 * ALTER TABLE `openmind`.`node` ADD COLUMN `public` BIT(1) AFTER `possible_value`;
	 * modify:
	 * ALTER TABLE `openmind`.`node` MODIFY COLUMN `public` BIT(1) NOT NULL DEFAULT false;
	 * @param witness
	 * @param isPublic
	 * @param wrapper
	 * @return
	 */
	public static List<Entity> changePrivacity4Witness(Entity witness, Boolean isPublic, WrapperService wrapper){
		List<Entity> saveList = new ArrayList<Entity>();
		/*
		if(witness.isLightweight()){
			witness = wrapper.getEntityContent(witness);
		}
		*/
		boolean privacity = false;
		if(isPublic == null){
			privacity = !witness.getIsPublic();
		}else{
			privacity = isPublic;
		}
		
		witness.setIsPublic(privacity);
		saveList.add(witness);
		
		//changing references
		List<Entity> refEntities = wrapper.getSourcesForTargetRelation(witness.getId(), "is_reference_of", "REFERENCE", -1);
		for(Entity ref : refEntities){
			if(ref.isLightweight()){
				ref = wrapper.getEntityContent(ref);
			}
			ref.setIsPublic(privacity);
			saveList.add(ref);
		}
		
		//only if the witness is done public, the related entities will be changed.
		if(privacity){
			List<Entity> list = 
				wrapper.getTargetsForSourceRelation(witness.getId(),"is_part_of", "CODEX", 1);
			if (list.size() > 0) {
				Entity codex = list.get(0);
				codex.setIsPublic(privacity);
				saveList.add(codex);
				
				list = wrapper.getTargetsForSourceRelation(codex.getId(), "is_part_of", "COLLECTION", 1);
				if (list.size() > 0) {
					Entity collection = list.get(0);
					collection.setIsPublic(privacity);
					saveList.add(collection);
					
					list = wrapper.getTargetsForSourceRelation(collection.getId(), "is_part_of", "REPOSITORY", 1);
					if (list.size() > 0) {
						Entity repository = list.get(0);
						repository.setIsPublic(privacity);
						saveList.add(repository);
						
						list = wrapper.getTargetsForSourceRelation(repository.getId(), "is_in", "PLACE", 1);
						if(list.size() > 0){
							Entity city = list.get(0);
							city.setIsPublic(privacity);
							saveList.add(city);
							
							list = wrapper.getTargetsForSourceRelation(city.getId(), "is_part_of", "PLACE", 1);
							if(list.size() > 0){
								Entity country = list.get(0);
								country.setIsPublic(privacity);
								saveList.add(country);
							}
						}
					}
				}
			}
		}
		
		return saveList;
	}
	
}
