package de.mpiwg.itgroup.ismi.event.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.auxObjects.lo.EventTextLO;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class CopyEvent extends AbstractEvent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1545705063133894571L;

	public static String OC = "COPY_EVENT";
	
	private ListenerObject personCopiedForLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject personCopyingTextLo = new ListenerObject(PERSON, name_translit);
	private ListenerObject repositoryLo = new ListenerObject(REPOSITORY, name);
	
	private Calendar date;
	
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
				if (rel.getOwnValue().equals(was_copied_for)) {
					//EVENT was_studied_by PERSON
					target = getTargetRelation(rel);
					personCopiedForLo.setEntityAndAttribute0(target);
				}else if (rel.getOwnValue().equals(has_person_copying_text)) {
					//EVENT was_advised_by PERSON
					target = getTargetRelation(rel);
					personCopyingTextLo.setEntityAndAttribute0(target);
					
				} else if (rel.getOwnValue().equals(was_copied_in)) {
					target = getTargetRelation(rel);
					if(target.getObjectClass().equals(PLACE)){
						//EVENT was_studied_in PLACE
						placeLo.setEntityAndAttribute0(target);	
					}else if(target.getObjectClass().equals(REPOSITORY)){
						//EVENT was_studied_in REPOSITORY
						repositoryLo.setEntityAndAttribute0(target);						
					}
				} else if (rel.getOwnValue().equals(is_a_copy_of)) {
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
	
	@Override
	public void reset(){
		super.reset();
		this.defObjectClass = OC;
		
		if(textLo != null){
			textLo.reset();
		}else{
			textLo = new EventTextLO(TEXT, full_title_translit, this);
		}
		
		if(personCopiedForLo != null){
			personCopiedForLo.reset();
		}else{
			personCopiedForLo = new ListenerObject(PERSON, name_translit);
		}
		
		if(personCopyingTextLo != null){
			personCopyingTextLo.reset();
		}else{
			personCopyingTextLo = new ListenerObject(PERSON, name_translit);
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
	}
	
	/*
	public void listenerChangePersonCopyingText(ValueChangeEvent event) {
		this.personCopyingText = changeListener(event, personCopyingText, PERSON, name_translit);
	}
	
	public void listenerChangePersonCopiedFor(ValueChangeEvent event) {
		this.personCopiedFor = changeListener(event, personCopiedFor, PERSON, name_translit);
	}
	
	public void listenerChangePlace(ValueChangeEvent event) {
		this.place = changeListener(event, place, PLACE, name);
	}
	
	public void listenerChangeRepository(ValueChangeEvent event) {
		this.repository = changeListener(event, repository, REPOSITORY, name);
	}*/
	
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
			
			// EVENT -> was_copied_for -> PERSON
			event.replaceSourceRelation(personCopiedForLo.entity, PERSON, was_copied_for);

			// EVENT -> has_person_copying_text -> PERSON
			event.replaceSourceRelation(personCopyingTextLo.entity, PERSON, has_person_copying_text);
						
			// EVENT -> was_copied_in -> REPOSITORY
			event.replaceSourceRelation(repositoryLo.entity, REPOSITORY, was_copied_in);
			
			// EVENT -> was_copied_in -> PLACE
			event.replaceSourceRelation(placeLo.entity, PLACE, was_copied_in);

			// EVENT -> is_a_copy_of -> WITNESS
			if(witness.isLightweight()){
				this.witness = getWrapper().getEntityByIdWithContent(witness.getId());
			}
			event.replaceSourceRelation(witness, WITNESS, is_a_copy_of);
			
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
		if(this.witness == null || 
				this.textLo.entity == null || 
				this.repositoryLo.entity == null || 
				this.personCopiedForLo.entity == null || 
				this.personCopyingTextLo.entity == null){
			return false;
		}
		return true;
	}
	
	public CopyEvent(Entity event) {
		super(event);
	}
	
	public CopyEvent(){
		super(new Entity(Node.TYPE_ABOX, OC, false));
	}
	
	public ListenerObject getPersonCopiedForLo() {
		return personCopiedForLo;
	}

	public void setPersonCopiedForLo(ListenerObject personCopiedForLo) {
		this.personCopiedForLo = personCopiedForLo;
	}

	public ListenerObject getPersonCopyingTextLo() {
		return personCopyingTextLo;
	}

	public void setPersonCopyingTextLo(ListenerObject personCopyingTextLo) {
		this.personCopyingTextLo = personCopyingTextLo;
	}

	public ListenerObject getRepositoryLo() {
		return repositoryLo;
	}

	public void setRepositoryLo(ListenerObject repositoryLo) {
		this.repositoryLo = repositoryLo;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}	
}
