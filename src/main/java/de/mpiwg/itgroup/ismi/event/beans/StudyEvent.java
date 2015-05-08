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

public class StudyEvent extends AbstractEvent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2242991945890055323L;

	public static String OC = "STUDY_EVENT";
	
	private ListenerObject personLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject advisorLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject repositoryLo = new ListenerObject(REPOSITORY, name);
	
	private List<SelectItem> suggestedEngagementOptions = new ArrayList<SelectItem>();
	
	public StudyEvent(){
		super(new Entity(Node.TYPE_ABOX, OC, false));
		refreshEngagementOptions();
	}
	
	public StudyEvent(Entity event){
		super(event);
		refreshEngagementOptions();
	}
	
	public void listenerRefreshEngagementOptions(ActionEvent event){
		this.refreshEngagementOptions();
	}
	
	private void refreshEngagementOptions(){
		this.suggestedEngagementOptions = new ArrayList<SelectItem>();
		suggestedEngagementOptions.add(new SelectItem(null, "--choose--"));
		Attribute binding = getWrapper().getDefAttributeByOwnValue(OC, "options_for_engagement");
		if(binding != null){
			for(String s : binding.getPossibleValuesList()){
				this.suggestedEngagementOptions.add(new SelectItem(s));
			}
		}
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
				if (rel.getOwnValue().equals(was_studied_by)) {
					//EVENT was_studied_by PERSON
					target = getTargetRelation(rel);
					personLo.setEntityAndAttribute0(target);
				}else if (rel.getOwnValue().equals(was_advised_by)) {
					//EVENT was_advised_by PERSON
					target = getTargetRelation(rel);
					advisorLo.setEntityAndAttribute0(target);
					
				} else if (rel.getOwnValue().equals(was_studied_in)) {
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PLACE)){
						//EVENT was_studied_in PLACE
						placeLo.setEntityAndAttribute0(target);	
					}else if(target.getObjectClass().equals(REPOSITORY)){
						//EVENT was_studied_in REPOSITORY
						repositoryLo.setEntityAndAttribute0(target);						
					}
				} else if (rel.getOwnValue().equals(is_a_study_of)) {
					//EVENT study_of WITNESS
					//WITNESS is_exemplar_of TEXT
					this.witness = getTargetRelation(rel);
					if(witness != null && witness.isPersistent()){
						witnessId = witness.getId();
						this.textLo.setEntityAndAttribute0( getTextOfWitness(witness));
						refreshWitnesses(textLo.entity);
					}
				}
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
			
			// EVENT -> was_studied_by -> PERSON
			event.replaceSourceRelation(personLo.entity, PERSON, was_studied_by);

			// EVENT -> was_advised_by -> PERSON
			event.replaceSourceRelation(advisorLo.entity, PERSON, was_advised_by);
						
			// EVENT -> was_studied_in -> REPOSITORY
			event.replaceSourceRelation(repositoryLo.entity, REPOSITORY, was_studied_in);
			//event.replaceSourceRelation(getRepositoryLo().entity, REPOSITORY, was_studied_in);
			
			// EVENT -> was_studied_in -> PLACE
			event.replaceSourceRelation(placeLo.entity, PLACE, was_studied_in);

			// EVENT -> was_studied_by -> WITNESS
			if(witness.isLightweight()){
				witness = getWrapper().getEntityByIdWithContent(witness.getId());
			}
			event.replaceSourceRelation(witness, WITNESS, is_a_study_of);
			
			getWrapper().saveEntity(event, getSessionUserName());
			
			printSuccessSavingEntity();
			
		}catch (Exception e) {
			addGeneralMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		saveEnd();
		return null;
	}
	
	/*
	public void listenerChangePerson(ValueChangeEvent event) {
		this.person = changeListener(event, person, PERSON, name_translit);
	}
	
	public void listenerChangeAdvisor(ValueChangeEvent event) {
		this.advisor = changeListener(event, advisor, PERSON, name_translit);
	}
		
	public void listenerChangeRepository(ValueChangeEvent event) {
		this.repository = changeListener(event, repository, REPOSITORY, name);
	}
	 */	
	@Override
	public void reset(){
		super.reset();
		this.defObjectClass = OC;
		if(personLo != null){
			personLo.reset();
		}else{
			personLo = new ListenerObject(PERSON, name_translit);
		}
		
		if(advisorLo != null){
			advisorLo.reset();
		}else{
			advisorLo = new ListenerObject(PERSON, name_translit);
		}
		
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
		
		if(repositoryLo != null){
			repositoryLo.reset();
		}else{
			repositoryLo = new ListenerObject(REPOSITORY, name);
		}
		
		this.date = new Calendar();
		this.witnessList = new ArrayList<SelectItem>();
		this.refreshEngagementOptions();
	}
	
	public boolean checkConsistency(){
		if(this.witness == null || this.textLo.entity == null || this.repositoryLo.entity == null || this.personLo.entity == null){
			return false;
		}
		return true;
	}
	
	public ListenerObject getPersonLo() {
		return personLo;
	}

	public void setPersonLo(ListenerObject personLo) {
		this.personLo = personLo;
	}

	public ListenerObject getAdvisorLo() {
		return advisorLo;
	}

	public void setAdvisorLo(ListenerObject advisorLo) {
		this.advisorLo = advisorLo;
	}	

	public ListenerObject getRepositoryLo() {
		return repositoryLo;
	}

	public void setRepositoryLo(ListenerObject repositoryLo) {
		this.repositoryLo = repositoryLo;
	}

	public List<SelectItem> getSuggestedEngagementOptions() {
		return suggestedEngagementOptions;
	}

	public void setSuggestedEngagementOptions(
			List<SelectItem> suggestedEngagementOptions) {
		this.suggestedEngagementOptions = suggestedEngagementOptions;
	}
	
}
