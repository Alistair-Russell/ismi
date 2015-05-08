package de.mpiwg.itgroup.ismi.event.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.auxObjects.lo.EventTextLO;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class AbstractEvent extends AbstractISMIBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6080984991162765794L;

	protected static Logger logger = Logger.getLogger(AbstractEvent.class);
	
	public static String was_studied_by = "was_studied_by";
	public static String was_studied_in = "was_studied_in";
	public static String is_a_study_of = "is_a_study_of";
	public static String was_advised_by = "was_advised_by";	
	public static String was_copied_for = "was_copied_for";
	public static String has_person_copying_text = "has_person_copying_text";
	public static String was_copied_in = "was_copied_in";
	public static String is_a_copy_of = "is_a_copy_of";
	public static String is_a_transfer_of = "is_a_transfer_of";
	public static String was_transferred_from = "was_transferred_from";
	public static String was_transferred_to = "was_transferred_to";
	public static String has_original_location = "has_original_location";
	public static String has_new_location = "has_new_location";
	public static String was_transferred_in = "was_transferred_in";
	
	protected EventTextLO textLo = new EventTextLO(TEXT, full_title_translit, this);
	protected Entity witness;
	protected Long witnessId;
	protected List<SelectItem> witnessList;
	protected Calendar date;
	
	protected ListenerObject placeLo = new ListenerObject(PLACE, name);
	
	public AbstractEvent(Entity event){
		this.setEvent(event);		
	}
	
	protected Entity event;
	/*
	public void listenerChangeText(ValueChangeEvent event) {
		this.text = changeListener(event, text, TEXT, full_title_translit);
		this.witness = null;
		if(text.entity != null && text.entity.isPersistent()){
			refreshWitnesses(text.entity);
		}else{
			this.witnessList = new ArrayList<SelectItem>();
		}
	}*/

	public void refreshWitnesses(Entity text){
		this.witnessList = new ArrayList<SelectItem>();
		List<Entity> list = getWrapper().getSourcesForTargetRelation(text, is_exemplar_of, WITNESS, -1);
		System.out.println("Found " + list.size() + " witnesses");
		if(list.isEmpty()){
			witnessList.add(new SelectItem(null, "This text has no witness associated"));
		}else{
			witnessList.add(new SelectItem(null, "-- choose witness --"));
			for(Entity ent : list){
				witnessList.add(new SelectItem(ent.getId(), ent.getOwnValue() + " ID: " + ent.getId()));
			}
		}
	}
	
	public void listenerChangeWitness(ValueChangeEvent event) {
		if(event.getNewValue() != null){
			try{
				Long id = (Long)event.getNewValue();
				this.witness = getWrapper().getEntityById(id);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.witness = null;
		}
	}
	
	protected Entity getTextOfWitness(Entity witness){
		Entity text = null;
		List<Entity> list = getWrapper().getTargetsForSourceRelation(witness, is_exemplar_of, TEXT, 1);
		if(list.size() > 0){
			text = list.get(0);
		}
		return text;
	}
	/*
	public void listenerChangePlace(ValueChangeEvent event) {
		this.place = changeListener(event, place, PLACE, name);
	}*/
	
	public Entity getEvent() {
		return event;
	}

	public void setEvent(Entity event) {
		this.reset();
		this.event = event;
	}
	
	public String getSessionUserName() {
		SessionBean bean = getSessionBean();
		if(bean != null){
			return bean.getUsername();	
		}
		return null;
	}
	
	public EventTextLO getTextLo() {
		return textLo;
	}

	public void setTextLo(EventTextLO textLo) {
		this.textLo = textLo;
	}

	public Entity getWitness() {
		return witness;
	}

	public void setWitness(Entity witness) {
		this.witness = witness;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public List<SelectItem> getWitnessList() {
		return witnessList;
	}

	public void setWitnessList(List<SelectItem> witnessList) {
		this.witnessList = witnessList;
	}

	public Long getWitnessId() {
		return witnessId;
	}

	public void setWitnessId(Long witnessId) {
		this.witnessId = witnessId;
	}

	
	
	public ListenerObject getPlaceLo() {
		return placeLo;
	}

	public void setPlaceLo(ListenerObject placeLo) {
		this.placeLo = placeLo;
	}

	public void test(AjaxBehaviorEvent e){
		System.out.println("test(ActionEvent e)");
		
	}
	
	@Override
	public String save(){
		logger.info("*************** START Saving "+ this.defObjectClass + " [ID=" + event.getId() +", user"+ getUserName() +"] *********************");
		this.start = System.currentTimeMillis();
		return null;
	}
	
	@Override
	protected void saveEnd(){
		logger.info("*************** END Saving "+ this.defObjectClass + " [ID=" + event.getId() +", user"+ getUserName() +"] *********************\n");
	}
	
	@Override
	protected void printSuccessSavingEntity(){
		this.addGeneralMsg("The entity was successfully saved!");
		this.addGeneralMsg("Its ID is " + this.event.getId());
	}
}
