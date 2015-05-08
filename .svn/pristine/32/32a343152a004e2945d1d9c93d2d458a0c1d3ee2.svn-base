package de.mpiwg.itgroup.ismi.event.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.auxObjects.lo.EventTextLO;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class TransferEvent  extends AbstractEvent implements Serializable{
	
	private static final long serialVersionUID = -3606229569295260209L;

	public static String OC = "TRANSFER_EVENT";
	
	private boolean selectedPersonFrom;
	private ListenerObject personFromLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject repositoryFromLo = new ListenerObject(REPOSITORY, name);
	
	private boolean selectedPersonTo;
	private ListenerObject personToLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject repositoryToLo = new ListenerObject(REPOSITORY, name);
	
	private boolean selectedPlaceOriginalLocation;
	private ListenerObject placeOriginalLocationLo = new ListenerObject(PLACE, name);
	private ListenerObject repositoryOriginalLocationLo = new ListenerObject(REPOSITORY, name);
	
	private boolean selectedPlaceNewLocation;
	private ListenerObject placeNewLocationLo = new ListenerObject(PLACE, name);
	private ListenerObject repositoryNewLocationLo = new ListenerObject(REPOSITORY, name);
	
	private List<SelectItem> suggestedTransferOptions = new ArrayList<SelectItem>();
	
	public TransferEvent(Entity event) {
		super(event);
		this.refreshTransferOptions();
	}
	
	public TransferEvent(){
		super(new Entity(Node.TYPE_ABOX, OC, false));
		this.refreshTransferOptions();
	}
	
	@Override
	public void setEvent(Entity ev){
		reset();
		event = ev;
		if(event != null && event.isPersistent()){
			if(event.isLightweight()){
				event = getWrapper().getEntityContent(event);
			}
			
			this.loadAttributes(this.event);
			this.date = updateCalendar(this.event.getAttributeByName("date"));
			
			for (Relation rel : event.getSourceRelations()) {
				Entity target = null;
				if (rel.getOwnValue().equals(was_transferred_from)) {
					// EVENT -> was_transferred_from -> PERSON/REPOSITORY
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PERSON)){
						personFromLo.setEntityAndAttribute0(target);
						selectedPersonFrom = true;
					}else{
						repositoryFromLo.setEntityAndAttribute0(target);
						selectedPersonFrom = false;
					}
					
				}else if (rel.getOwnValue().equals(was_transferred_to)) {
					// EVENT -> was_transferred_to -> PERSON/REPOSITORY
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PERSON)){
						personToLo.setEntityAndAttribute0(target);
						selectedPersonTo = true;
					}else{
						repositoryToLo.setEntityAndAttribute0(target);
						selectedPersonTo = false;
					}					
				} else if (rel.getOwnValue().equals(has_original_location)) {
					// EVENT -> has_original_location -> PLACE/REPOSITORY
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PLACE)){
						placeOriginalLocationLo.setEntityAndAttribute0(target);
						selectedPlaceOriginalLocation = true;
					}else{
						repositoryOriginalLocationLo.setEntityAndAttribute0(target);
						selectedPlaceOriginalLocation = false;
					}
				} else if (rel.getOwnValue().equals(has_new_location)) {
					// EVENT -> has_new_location -> PLACE/REPOSITORY
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PLACE)){
						placeNewLocationLo.setEntityAndAttribute0(target);
						selectedPlaceNewLocation = true;
					}else{
						repositoryNewLocationLo.setEntityAndAttribute0(target);
						selectedPlaceNewLocation = false;
					}
				} else if (rel.getOwnValue().equals(is_a_transfer_of)) {
					//EVENT study_of WITNESS
					//WITNESS is_exemplar_of TEXT
					this.witness = getTargetRelation(rel);
					if(witness != null && witness.isPersistent()){
						witnessId = witness.getId();
						this.textLo.setEntityAndAttribute0( getTextOfWitness(witness));
						refreshWitnesses(textLo.entity);
					}
				} else if (rel.getOwnValue().equals(was_transferred_in)) {
					target = getTargetRelation(rel);
					this.placeLo.setEntityAndAttribute0(target);
				}
			}
		}
	}
	
	/*
	public void listenerChangePersonFrom(ValueChangeEvent event) {
		this.personFrom = changeListener(event, personFrom, PERSON, name_translit);
	}
	
	public void listenerChangePersonTo(ValueChangeEvent event) {
		this.personTo = changeListener(event, personTo, PERSON, name_translit);
	}
	
	public void listenerChangeRepositoryFrom(ValueChangeEvent event) {
		this.repositoryFrom = changeListener(event, repositoryFrom, REPOSITORY, name);
	}
	
	public void listenerChangeRepositoryTo(ValueChangeEvent event) {
		this.repositoryTo = changeListener(event, repositoryTo, REPOSITORY, name);
	}*/
	
	public void listenerChangeSelectionFrom(ActionEvent event){
		this.selectedPersonFrom = !selectedPersonFrom;
	}
	
	public void listenerChangeSelectionTo(ActionEvent event){
		this.selectedPersonTo = !selectedPersonTo;
	}
	/*
	public void listenerChangePlaceOriginalLocation(ValueChangeEvent event){
		this.placeOriginalLocation = changeListener(event, placeOriginalLocation, PLACE, name);
	}
	
	public void listenerChangePlaceNewLocation(ValueChangeEvent event){
		this.placeNewLocation = changeListener(event, placeNewLocation, PLACE, name);
	}
	
	public void listenerChangeRepositoryOriginalLocation(ValueChangeEvent event){
		this.repositoryOriginalLocation = changeListener(event, repositoryOriginalLocation, REPOSITORY, name);
	}
	
	public void listenerChangeRepositoryNewLocation(ValueChangeEvent event){
		this.repositoryNewLocation = changeListener(event, repositoryNewLocation, REPOSITORY, name);
	}
	*/
	public void listenerChangeSelectionNewLocation(ActionEvent event){
		this.selectedPlaceNewLocation = !selectedPlaceNewLocation;
	}
	
	public void listenerChangeSelectionOriginalLocation(ActionEvent event){
		this.selectedPlaceOriginalLocation = !selectedPlaceOriginalLocation;
	}
	
	@Override
	public void reset(){
		super.reset();
		this.selectedPersonFrom = true;
		this.selectedPersonTo = true;
		this.selectedPlaceOriginalLocation = true;
		this.selectedPlaceNewLocation = true;
		
		this.defObjectClass = OC;
		this.personFromLo = new ListenerObject(PERSON, name_translit);
		this.repositoryFromLo = new ListenerObject(REPOSITORY, name);
		this.personToLo = new ListenerObject(PERSON, name_translit);
		this.repositoryToLo = new ListenerObject(REPOSITORY, name);
		
		if(textLo != null){
			textLo.reset();
		}else{
			textLo = new EventTextLO(TEXT, full_title_translit, this);
		}
		
		if(placeLo != null){
			placeLo.reset();
		}else{
			placeLo = new ListenerObject(PLACE, name);
		}
		
		
		this.date = new Calendar();
		this.witnessList = new ArrayList<SelectItem>();		
		
		this.placeNewLocationLo = new ListenerObject(PLACE, name);
		this.repositoryNewLocationLo = new ListenerObject(REPOSITORY, name);
		this.personToLo = new ListenerObject(PERSON, name_translit);
		this.repositoryToLo = new ListenerObject(REPOSITORY, name);
		this.placeOriginalLocationLo = new ListenerObject(PLACE, name);
		this.repositoryOriginalLocationLo = new ListenerObject(REPOSITORY, name);
		/*
		this.repositoryFromLo.reset();
		this.repositoryToLo.reset();
		this.placeNewLocationLo.reset();
		this.placeOriginalLocationLo.reset();
		this.repositoryNewLocationLo.reset();
		this.repositoryOriginalLocationLo.reset();
		*/
	}
	
	
	public void listenerRefreshTransferOptions(ActionEvent event){
		this.refreshTransferOptions();
	}
	
	private void refreshTransferOptions(){
		this.suggestedTransferOptions = new ArrayList<SelectItem>();
		suggestedTransferOptions.add(new SelectItem(null, "--choose--"));
		Attribute binding = getWrapper().getDefAttributeByOwnValue(OC, "options_for_transfer");
		if(binding != null){
			for(String s : binding.getPossibleValuesList()){
				this.suggestedTransferOptions.add(new SelectItem(s));
			}
		}
	}
	
	public void listenerSave(ActionEvent event) {
		this.save();
	}
	
	@Override
	public String save(){
		super.save();
		if(!checkConsistency()){
			addGeneralMsg("Either the Witness, the Person or the Place is empty.");
			addGeneralMsg("The event could not be saved.");
			return null;
		}
		
		try{
			getAttributes().put("date", this.date.toJSONString());
			event = updateEntityAttributes(event);
			
			// EVENT -> was_studied_by -> WITNESS
			if(witness.isLightweight()){
				this.witness = getWrapper().getEntityByIdWithContent(witness.getId());
			}
			event.replaceSourceRelation(witness, WITNESS, is_a_transfer_of);
			
			// EVENT -> was_transferred_from -> PERSON/REPOSITORY
			if(isSelectedPersonFrom()){
				event.replaceSourceRelation(personFromLo.entity, PERSON, was_transferred_from);
				event.removeAllSourceRelations(was_transferred_from, REPOSITORY);
			}else{
				event.replaceSourceRelation(repositoryFromLo.entity, REPOSITORY, was_transferred_from);
				event.removeAllSourceRelations(was_transferred_from, PERSON);
			}

			// EVENT -> was_transferred_to -> PERSON/REPOSITORY
			if(isSelectedPersonTo()){
				event.replaceSourceRelation(personToLo.entity, PERSON, was_transferred_to);
				event.removeAllSourceRelations(was_transferred_to, REPOSITORY);
			}else{
				event.replaceSourceRelation(repositoryToLo.entity, REPOSITORY, was_transferred_to);
				event.removeAllSourceRelations(was_transferred_to, PERSON);
			}
			
			// EVENT -> has_original_location -> PLACE/REPOSITORY
			if(isSelectedPlaceOriginalLocation()){
				event.replaceSourceRelation(placeOriginalLocationLo.entity, PLACE, has_original_location);
				event.removeAllSourceRelations(has_original_location, REPOSITORY);
			}else{
				event.replaceSourceRelation(repositoryOriginalLocationLo.entity, REPOSITORY, has_original_location);
				event.removeAllSourceRelations(has_original_location, PLACE);
			}
			
			// EVENT -> has_new_location -> PLACE/REPOSITORY
			if(isSelectedPlaceNewLocation()){
				event.replaceSourceRelation(placeNewLocationLo.entity, PLACE, has_new_location);
				event.removeAllSourceRelations(has_new_location, REPOSITORY);
			}else{
				event.replaceSourceRelation(repositoryNewLocationLo.entity, REPOSITORY, has_new_location);
				event.removeAllSourceRelations(has_new_location, PLACE);
			}
			
			// EVENT -> was_transferred_in -> PLACE
			event.replaceSourceRelation(placeLo.entity, PLACE, was_transferred_in);
			
			getWrapper().saveEntity(event, getSessionUserName());
			
			printSuccessSavingEntity();
			
		}catch (Exception e) {
			addGeneralMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		}	
		saveEnd();
		return null;
	}
	
	public boolean checkConsistency(){
		//TODO
		if(this.witness == null || this.textLo.entity == null ){
			return false;
		}
		return true;
	}
	


	public boolean isSelectedPersonFrom() {
		return selectedPersonFrom;
	}

	public boolean isSelectedPersonTo() {
		return selectedPersonTo;
	}

	public List<SelectItem> getSuggestedTransferOptions() {
		return suggestedTransferOptions;
	}

	public void setSuggestedTransferOptions(
			List<SelectItem> suggestedTransferOptions) {
		this.suggestedTransferOptions = suggestedTransferOptions;
	}

	public boolean isSelectedPlaceOriginalLocation() {
		return selectedPlaceOriginalLocation;
	}


	
	public boolean isSelectedPlaceNewLocation() {
		return selectedPlaceNewLocation;
	}

	public ListenerObject getPersonFromLo() {
		return personFromLo;
	}

	public void setPersonFromLo(ListenerObject personFromLo) {
		this.personFromLo = personFromLo;
	}

	public ListenerObject getRepositoryFromLo() {
		return repositoryFromLo;
	}

	public void setRepositoryFromLo(ListenerObject repositoryFromLo) {
		this.repositoryFromLo = repositoryFromLo;
	}

	public ListenerObject getPersonToLo() {
		return personToLo;
	}

	public void setPersonToLo(ListenerObject personToLo) {
		this.personToLo = personToLo;
	}

	public ListenerObject getRepositoryToLo() {
		return repositoryToLo;
	}

	public void setRepositoryToLo(ListenerObject repositoryToLo) {
		this.repositoryToLo = repositoryToLo;
	}

	public ListenerObject getPlaceOriginalLocationLo() {
		return placeOriginalLocationLo;
	}

	public void setPlaceOriginalLocationLo(ListenerObject placeOriginalLocationLo) {
		this.placeOriginalLocationLo = placeOriginalLocationLo;
	}

	public ListenerObject getRepositoryOriginalLocationLo() {
		return repositoryOriginalLocationLo;
	}

	public void setRepositoryOriginalLocationLo(
			ListenerObject repositoryOriginalLocationLo) {
		this.repositoryOriginalLocationLo = repositoryOriginalLocationLo;
	}

	public ListenerObject getPlaceNewLocationLo() {
		return placeNewLocationLo;
	}

	public void setPlaceNewLocationLo(ListenerObject placeNewLocationLo) {
		this.placeNewLocationLo = placeNewLocationLo;
	}

	public ListenerObject getRepositoryNewLocationLo() {
		return repositoryNewLocationLo;
	}

	public void setRepositoryNewLocationLo(ListenerObject repositoryNewLocationLo) {
		this.repositoryNewLocationLo = repositoryNewLocationLo;
	}

	public void setSelectedPersonFrom(boolean selectedPersonFrom) {
		this.selectedPersonFrom = selectedPersonFrom;
	}

	public void setSelectedPersonTo(boolean selectedPersonTo) {
		this.selectedPersonTo = selectedPersonTo;
	}

	public void setSelectedPlaceOriginalLocation(
			boolean selectedPlaceOriginalLocation) {
		this.selectedPlaceOriginalLocation = selectedPlaceOriginalLocation;
	}

	public void setSelectedPlaceNewLocation(boolean selectedPlaceNewLocation) {
		this.selectedPlaceNewLocation = selectedPlaceNewLocation;
	}

	
	
}
