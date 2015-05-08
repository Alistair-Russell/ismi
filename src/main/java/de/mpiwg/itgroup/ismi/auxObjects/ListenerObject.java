package de.mpiwg.itgroup.ismi.auxObjects;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;

public class ListenerObject extends AbstractListenerObject{
	private static final long serialVersionUID = 9156066826135642525L;
	
	public String classObj;
	public String attName;
	public String term;
	private boolean renderInfo = false;

	
	public ListenerObject(){}
	
	public ListenerObject(String classObj, String attName){
		this.classObj = classObj;
		this.attName = attName;
	}
	
	/**
	 * When the term is changed in the inputtext, 
	 * this method will be called to change teh suggestion list.
	 * @param event
	 */
	public void valueChangeMethod(ValueChangeEvent event) {
		this.entityInfo =  null;
		changeListener(event, classObj, attName);
		if(classObj.equals("PERSON")){
			/*
			if(authorLo != null && authorLo.entity != null && authorLo.entity.isPersistent()){
			this.authorInfo = "ID = " + authorLo.getEntity().getId();
			Attribute attArabicName = getTargetAttribute(authorLo.entity, "name_translit");
			if(attArabicName != null)
				this.authorInfo += ", Arabic Name = " + attArabicName.getOwnValue();
			}
			 */
		}
	}
	
	public void actionListenerSelect(ActionEvent event){
		
		this.suggestedItems = new ArrayList<SelectItem>();
		SelectItem selectItem = (SelectItem) getRequestBean("item");
		if(selectItem != null){
			Attribute att = (Attribute)selectItem.getValue();
			
			Entity ent = getWrapper().getEntityById(att.getSourceId());
			
			if(ent != null){
				this.setEntityAndAttribute0(ent);
			}
		}
	}
	
	public void onClick(){
        if(!this.equalsTermEntity()){
        	this.suggestedItems = new ArrayList<SelectItem>();
    		this.statusImage = new StatusImage();
    		this.attribute = new Attribute();
    		this.entity = new Entity();
        }
    }
	
	public void reset(){
		this.suggestedItems = new ArrayList<SelectItem>();
		this.statusImage = new StatusImage();
		this.attribute = new Attribute();
		this.entity = new Entity();
		this.term = null;
	}
	
	public String getDisplayUrl(){
		Entity ent = getEntity();
		if(ent.isPersistent()){
			if(ent.getObjectClass().equals("PERSON")){
				return AbstractISMIBean.generateDisplayUrl(ent, null, null, getAppBean().getRoot());
			}
			else if(ent.getObjectClass().equals("TEXT")){
				return AbstractISMIBean.generateDisplayUrl(null, ent, null, getAppBean().getRoot());
			}else if(ent.getObjectClass().equals("WITNESS")){
				return AbstractISMIBean.generateDisplayUrl(null, null, ent, getAppBean().getRoot());
			}
		}
		return null;
	}
	
	public ListenerObject(List<SelectItem> suggestedItemsNew,
			Attribute attributeNew, Entity entityNew) {
		suggestedItems = suggestedItemsNew;
		attribute = attributeNew;
		entity = entityNew;
	}

	public List<SelectItem> getSuggestedItems() {
		return suggestedItems;
	}

	public void setSuggestedItems(List<SelectItem> suggestedItems) {
		this.suggestedItems = suggestedItems;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Entity getEntity() {
		if (entity==null)
			return new Entity();
		return entity;
	}

	public StatusImage getStatusImage() {
		return statusImage;
	}

	public void setStatusImage(StatusImage statusImage) {
		this.statusImage = statusImage;
	}
	
	public void invalidate(){
		if (attribute==null || attribute.getOwnValue().equals(""))
			statusImage.setStatus("unset");
		else
			statusImage.setStatus("false");
	}
	public String getStatus(){
		return statusImage.getStatus();
	}
	
	public void setEntityAndAttribute0(Entity ent){
		this.setEntity(ent);
		if(ent != null && ent.isPersistent()){
			this.attribute = getWrapper().getAttributeByName(ent.getId(), this.attName);
			if(this.attribute != null && StringUtils.isNotEmpty(this.attribute.getOwnValue())){
				term = attribute.getOwnValue() + " [" + entity.getId() + "]";
			}else{
				term = this.entity.getOwnValue() + "[" + entity.getId() + "]";
			}
		}
	}
	
	public void setEntityAndAttribute(Entity ent, String attName){
		this.attName = attName;
		this.setEntityAndAttribute0(ent);
	}
	
	public boolean equalsTermEntity(){
		if(StringUtils.isEmpty(term)){
			return false;
		}		
		if(attribute != null){
			return StringUtils.equals(
					attribute.getOwnValue() + " [" + attribute.getSourceId() + "]", term);
		}else if(entity != null){
			return StringUtils.equals(entity.getOwnValue() + "[" + entity.getId() + "]", term);
		}
		return false;
	}
	
	@Override
	public String toString(){
		String s = new String();
		s += "LO: " + this.statusImage.getStatus();
		s += (this.entity != null) ? "[" + entity.getObjectClass() + ": " + entity.getOwnValue() + "] " : "";
		s += (this.attribute != null) ? "[" + attribute.getName() + ": " + attribute.getValue() + "]" : "";
		return s;
	}
	
	public String getTerm() {
		if(this.entity != null && entity.isPersistent()){
			if(attribute != null && StringUtils.isNotEmpty(attribute.getOwnValue())){
				term = attribute.getOwnValue() + " [" + entity.getId() + "]";
			}else{
				term = this.entity.getOwnValue() + "[" + entity.getId() + "]";
			}
		}
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}

	public String getClassObj() {
		return classObj;
	}

	public void setClassObj(String classObj) {
		this.classObj = classObj;
	}

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public boolean isRenderInfo() {
		if(entity == null || !entity.isPersistent())
			return false;
		return renderInfo;
	}

	public void setRenderInfo(boolean renderInfo) {
		this.renderInfo = renderInfo;
	}
}
