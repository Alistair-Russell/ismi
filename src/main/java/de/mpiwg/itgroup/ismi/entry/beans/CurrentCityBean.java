package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.utils.SelectableObject;


public class CurrentCityBean  extends CodexEditorTemplate  implements Serializable{
	private static final long serialVersionUID = -8609055286714729597L;
	
	private static Logger logger = Logger.getLogger(CurrentCityBean.class);
	
	private List<SelectItem> suggestedTypes;
	
	private List<SelectableObject<Entity>> placesPartOfThis;
	private List<SelectableObject<Entity>> peopleLivedIn;
	
	public CurrentCityBean() {
		this.reset();

	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, PLACE, false);
		this.suggestedTypes = new ArrayList<SelectItem>();
		
		this.placesPartOfThis = new ArrayList<SelectableObject<Entity>>();
		this.peopleLivedIn = new ArrayList<SelectableObject<Entity>>();
		
		//setDefinition(getDefinition(PLACE));
		setDefObjectClass(PLACE);
		String[] types = new String[]{"city","institution", "city_part", "region"};
		fillList(suggestedTypes, types);
		
		registerChecker(getCountryLo(), "Country not valid!");
	}
	
	@Override
	public void setEntity(Entity city) {
		this.reset();
		this.entity = city;
		
		if(this.entity.isPersistent()){
			if(city.isLightweight()){
				this.entity = getWrapper().getEntityContent(this.entity);
			}
			
			this.loadAttributes(this.entity);
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(city, is_part_of, PLACE, -1);
			if(list.size() > 0){
				this.setCountry(list.get(0));
				if(list.size() > 1){
					addErrorMsg("Ontological inconsistency. This place 'is part of' " + list.size() + " other places.");
					addErrorMsg("List:");
					for(Entity place : list){
						addErrorMsg(place.getOwnValue() + " [" + place.getId() + "]");
					}
				}
			}
			
			for(Entity place : getWrapper().getSourcesForTargetRelation(city, is_part_of, PLACE, -1)){
				String label = place.getOwnValue() + " [" + place.getId() + "]";
				this.placesPartOfThis.add(new SelectableObject<Entity>(place, label));
			}
			
			for(Entity person : getWrapper().getSourcesForTargetRelation(city, lived_in, PERSON, -1)){
				String label = person.getOwnValue() + " [" + person.getId() + "]";
				this.peopleLivedIn.add(new SelectableObject<Entity>(person, label));
			}
			
			//this.loadReferences(this.entity);
			this.loadEndNoteRefs();
			
			this.setCurrentId(this.entity.getId().toString());
			this.checkConsistencyFromCountryToCodex();
		}
	}
	
	@Override
	public String save(){
		super.save();
		try {
		
			CheckResults cr = getCheckResults();
			if (cr.hasErrors){
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				this.setSelectedSaveAsNew(false);
				return "SAVE_ERROR";
			}
			
			this.entity = this.updateEntityAttributes(this.entity);
			
			String cityName = (this.entity.getAttributeByName(name) != null) ? this.entity.getAttributeByName(name).getValue() : "";
			if(StringUtils.isNotEmpty(cityName)){
				if(!checkUnityCity(cityName, (isSelectedSaveAsNew()) ? null : this.entity.getId(), this.getCountryLo().entity)){
					this.renderUnityCheckerDialog();
					this.setSelectedSaveAsNew(false);
					return "SAVE_ERROR";
				}
			}else{
				this.addErrorMsg("Ths entity has not been saved, because its name was empty.");
				this.addErrorMsg("You must enter a name.");
				return "SAVE_ERROR";
			}
			
			//this.entity.removeSourceRelation("is_part_of", this.getCountryLo().entity);
			//replaceSourceRelation(this.entity, this.getCountryLo().entity, "PLACE", "is_part_of");
			this.entity.replaceSourceRelation(this.getCountryLo().entity, PLACE, is_part_of);
						
			this.entity.removeAllTargetRelationsByName(is_part_of);
			for(SelectableObject<Entity> so : this.placesPartOfThis){
				Entity place = getWrapper().getEntityByIdWithContent(so.getObj().getId());
				Relation isPartOf = new Relation(place, this.entity, is_part_of);
			}
			
			this.entity.removeAllTargetRelationsByName(lived_in);
			for(SelectableObject<Entity> so : this.peopleLivedIn){
				Entity person = getWrapper().getEntityByIdWithContent(so.getObj().getId());
				Relation livedIn = new Relation(person, this.entity, lived_in);
			}
			
			//REFERENCE -> is_reference_of -> THIS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();		
			
			//String lastAction = "";
			if(this.isSelectedSaveAsNew()){
				this.entity.removeAllTargetRelations(is_in, REPOSITORY);
				this.entity = getWrapper().saveEntityAsNew(this.entity, getSessionUser().getEmail());
				this.setSelectedSaveAsNew(false);
				//lastAction = "Save place as new entity";
			}else{
				this.entity = getWrapper().saveEntity(this.entity, getSessionUser().getEmail());
				//lastAction = "Save place";
			}
			//setActionInfo(lastAction);
			this.generateSecundaryOW(this.entity, getSessionUser().getEmail());
			this.setCurrentId(this.entity.getId().toString());
			
			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();
			
			this.setSelectedSaveAsNew(false);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		saveEnd();
		
		return PAGE_EDITOR;
	}
	
	public void listenerRemovePlacesPartOfThis(){
		for(SelectableObject<Entity> so : new ArrayList<SelectableObject<Entity>>(this.placesPartOfThis)){
			if(so.isSelected()){
				this.placesPartOfThis.remove(so);
			}
		}
	}
	
	public void listenerRemovePeopleLivedIn(){
		for(SelectableObject<Entity> so : new ArrayList<SelectableObject<Entity>>(this.peopleLivedIn)){
			if(so.isSelected()){
				this.peopleLivedIn.remove(so);
			}
		}
	}
	
	public String saveAsNewEntity(){
		this.setSelectedSaveAsNew(true);
		return save();
	}
	public List<SelectItem> getSuggestedTypes() {
		return suggestedTypes;
	}
	public void setSuggestedTypes(List<SelectItem> suggestedTypes) {
		this.suggestedTypes = suggestedTypes;
	}

	public List<SelectableObject<Entity>> getPlacesPartOfThis() {
		return placesPartOfThis;
	}

	public void setPlacesPartOfThis(List<SelectableObject<Entity>> placesPartOfThis) {
		this.placesPartOfThis = placesPartOfThis;
	}

	public List<SelectableObject<Entity>> getPeopleLivedIn() {
		return peopleLivedIn;
	}

	public void setPeopleLivedIn(List<SelectableObject<Entity>> peopleLivedIn) {
		this.peopleLivedIn = peopleLivedIn;
	}
}
