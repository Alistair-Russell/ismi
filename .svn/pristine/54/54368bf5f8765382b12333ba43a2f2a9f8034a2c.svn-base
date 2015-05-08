package de.mpiwg.itgroup.ismi.utils.templates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.search.AbstractSearchService;
import org.mpiwg.itgroup.escidoc.ESciDocHandler;
import org.mpiwg.itgroup.escidoc.bo.Publication;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class AuthorTemplate extends AbstractTemplate{
	
	private static Logger logger = Logger.getLogger(AuthorTemplate.class);
	
	private static String birth_date = "birth_date";
	
	private static String lived_in = "lived_in";
	private static String has_role = "has_role";
	private static String was_student_of = "was_student_of";

	public String privacity;
	public String name;
	public String nameTranslit;
	public String bornInPlace;
	public String diedInPlace;
	public String primeAlias;
	public String notes;
	public String url;
	public List<String> aliasList;
	public List<String> roleList;
	
	public String birthDate;
	public String deathDate;

	public Map<String, String> livedInPlaceMap;
	
	public AuthorTemplate(Entity entity, WrapperService om){
		
		logger.info("Diplaying " + entity);
		
		if (entity.isLightweight()) {
			entity = om.getEntityContent(entity);
		}
		this.aliasList = new ArrayList<String>();
		this.roleList = new ArrayList<String>();
		this.livedInPlaceMap = new HashMap<String, String>();
		this.privacity = entity.getPrivacity();
		
		Attribute attName = entity.getAttributeByName("name");
		Attribute attNameTranslit = entity.getAttributeByName("name_translit");
		Attribute attNotes = entity.getAttributeByName("notes");
		Attribute attUrl = entity.getAttributeByName("url");
		this.name = (attName == null) ? null : attName.getValue();
		this.nameTranslit = (attNameTranslit == null) ? null : attNameTranslit.getValue();
		this.notes = (attNotes == null) ? null : attNotes.getValue();
		this.url = (attUrl == null) ? null : attUrl.getValue();
		
		Calendar calDeathDate = AbstractISMIBean.updateCalendar(entity.getAttributeByName("death_date"));
		this.deathDate = calDeathDate.getCalendarAsHtml();
		
		Calendar calBirthDate = AbstractISMIBean.updateCalendar(entity.getAttributeByName(birth_date));
		this.birthDate = calBirthDate.getCalendarAsHtml();
		
		//this.setCurrentId(entity.getId().toString());
		//this.loadAttributes(entity);
		
		for (Relation rel : entity.getSourceRelations()) {
			if(StringUtils.isEmpty(rel.getOwnValue()) || StringUtils.isEmpty(rel.getTargetObjectClass())){
				try {
					throw new Exception("Relation has no ownValue and/or targetObjCls " + rel);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else{
				Entity target = om.getEntityById(rel.getTargetId());
				if (rel.getOwnValue().equals("was_born_in")) {
					this.bornInPlace = (target == null) ? "" : target.getOwnValue();
				} else if (rel.getOwnValue().equals(lived_in)) {
					String date = (rel.getAttributeByName("date") != null) ? 
							new Calendar(rel.getAttributeByName("date").getOwnValue()).getCalendarAsHtml() : 
							"";
					this.livedInPlaceMap.put(target.getOwnValue(), date);
				} else if (rel.getOwnValue().equals(was_student_of)) {
					//this.studentOfList.add(target);
				} else if (rel.getOwnValue().equals(has_role)) {
					this.roleList.add(target.getOwnValue());
				} else if (rel.getOwnValue().equals("died_in")) {
					this.diedInPlace = (target == null) ? "" : target.getOwnValue();
				}				
			}
		}

		for (Relation rel : entity.getTargetRelations()) {
			
			if(StringUtils.isEmpty(rel.getOwnValue()) || StringUtils.isEmpty(rel.getSourceObjectClass())){
				try {
					throw new Exception("Relation has no ownValue and/or sourceObjCls " + rel);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else{
				if (rel.getOwnValue().equals("was_created_by") && rel.getSourceObjectClass().equals("TEXT")) {
					Entity title = om.getEntityById(rel.getSourceId());
					if(title != null && title.getObjectClass().equals("TEXT")){
						//this.titles.add(title);
						//I remove this line cause the list in this way would not be sorted.
						//this.titleItems.add(new SelectItem(title.getId().toString(), title.getOwnValue() + " [" + title.getId() + "]"));
					}
				} else if (rel.getOwnValue().equals("is_alias_name_of")) {
					Entity alias = om.getEntityById(rel.getSourceId());
					this.aliasList.add(alias.getOwnValue());
				} else if (rel.getOwnValue().equals("is_prime_alias_name_of")) {
					Entity alias = om.getEntityByIdWithContent(rel.getSourceId());
					this.primeAlias = alias.getAttributeByName("alias").getValue();
				} else if (rel.getOwnValue().equals("is_reference_of")) {
					Entity ref = om.getEntityByIdWithContent(rel.getSourceId());
					this.refEntityList.add(ref);
				}	
			}
			
		}
		this.loadRefernces();
	}
	
	public String getPrivacity() {
		return privacity;
	}
	public String getName() {
		return name;
	}
	public String getNameTranslit() {
		return nameTranslit;
	}
	public String getBornInPlace() {
		return bornInPlace;
	}
	public String getDiedInPlace() {
		return diedInPlace;
	}
	public String getPrimeAlias() {
		return primeAlias;
	}
	public String getNotes() {
		return notes;
	}
	public String getUrl() {
		return url;
	}
	public List<String> getAliasList() {
		return aliasList;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	public String getDeathDate() {
		return deathDate;
	}
	public List<String> getRoleList() {
		return roleList;
	}

	public Map<String, String> getLivedInPlaceMap() {
		return livedInPlaceMap;
	}
	
	public Collection<String> getLivedInPlaceList(){
		return livedInPlaceMap.keySet();
	}
}
