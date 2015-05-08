package org.mpiwg.itgroup.escidoc.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpiwg.itgroup.escidoc.bo.Creator;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

public class ESciDocItemForm extends AbstractBean {
	
	private ESciDocItem item;
	private String itemId;
	
	private List<SelectableObject> creatorList = new ArrayList<SelectableObject>();
	private List<SelectableObject> alternativeNameList = new ArrayList<SelectableObject>();
	
	
	private boolean visible;
	
	public ESciDocItemForm(){
		this.reset();
	}
	
	private void reset(){
		this.item = new ESciDocItem();
		this.itemId = null;
		this.creatorList = new ArrayList<SelectableObject>();
		this.alternativeNameList = new ArrayList<SelectableObject>();
	}
	
	public void listenerCleanForm(ActionEvent event){
		this.reset();
	}
	
	public void listenerDelete(ActionEvent event){
		try {
			
			getAppBean().getRefCache().deleteItem(item, getSessionBean().getUsername());
			
			addGeneralMsg("The publication has been deleted successfully.");
			this.visible = false;
			this.item = null;
		} catch (Exception e) {
			addErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void listenerSave(ActionEvent event){
		
		List<String> alternativeNames = new ArrayList<String>();
		for(SelectableObject so : this.alternativeNameList){
			alternativeNames.add((String)so.getObj());
		}
		List<Creator> creators = new ArrayList<Creator>();
		for(SelectableObject so : this.creatorList){
			creators.add((Creator)so.getObj());
		}
		this.item.getPublication().setAlternativeList(alternativeNames);
		this.item.getPublication().setCreatorList(creators);
		try {
			
			this.item = getAppBean().getRefCache().saveItem(item, getSessionBean().getUsername());
			
			this.setItem(getAppBean().getRefCache().getItem(item.getObjId()));
			addGeneralMsg("The publication has been saved successfully.");
		} catch (Exception e) {
			addErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getButtonSaveLabel(){
		if(StringUtils.isEmpty(item.getObjId())){
			return "Create";
		}
		return "Save";
	}
	
	public boolean isDisplayDeleteBtn(){
		if(item == null || StringUtils.isEmpty(item.getObjId())){
			return false;
		}
		return true;
	}
	
	public void listenerReloadItem(ActionEvent event){
		if(StringUtils.isNotEmpty(itemId)){
			this.item = getAppBean().getRefCache().getItem(itemId);
			
			if(item == null){
				addGeneralMsg("No item found " + itemId);
			}else{
				this.item = (ESciDocItem)this.item.clone();
			}
		}else{
			addGeneralMsg("Item id can not be empty");
		}
	}

	public void listenerAddAlternativeName(ActionEvent event){
		this.alternativeNameList.add(new SelectableObject(new String("...")));
	}
	
	public void listenerRemoveAlternativeName(ActionEvent event){
		List<SelectableObject> toDelete = new ArrayList<SelectableObject>();
		for(SelectableObject so : this.alternativeNameList){
			if(so.isSelected()){
				toDelete.add(so);
			}
		}		
		for(SelectableObject so : toDelete){
			this.alternativeNameList.remove(so);
		}
	}
	
	public void listenerAddCreator(ActionEvent event){
		this.creatorList.add(new SelectableObject(new Creator("completeName", "familyName", "givenName")));
	}
	
	public void listenerRemoveCreator(ActionEvent event){
		List<SelectableObject> toDelete = new ArrayList<SelectableObject>();
		for(SelectableObject so : this.creatorList){
			if(so.isSelected()){
				toDelete.add(so);
			}
		}		
		for(SelectableObject so : toDelete){
			this.creatorList.remove(so);
		}
	}
	
	public void listenerClose(ActionEvent event){
		this.visible = false;
		this.item = null;
	}
	
	public ESciDocItem getItem() {
		return item;
	}

	public void createItem(){
		this.reset();
		this.visible = true;
	}
	
	public void setItem(ESciDocItem item) {
		this.reset();
		try {
			
			this.item = getAppBean().getRefCache().getItemFromServer(item.getObjId());
			this.visible = true;
			
			for(String s : this.item.getPublication().getAlternativeList()){
				this.alternativeNameList.add(new SelectableObject(s));
			}
			
			for(Creator creator : this.item.getPublication().getCreatorList()){
				this.creatorList.add(new SelectableObject(creator));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			addGeneralMsg(e.getMessage());
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<SelectableObject> getCreatorList() {
		return creatorList;
	}

	public void setCreatorList(List<SelectableObject> creatorList) {
		this.creatorList = creatorList;
	}

	public List<SelectableObject> getAlternativeNameList() {
		return alternativeNameList;
	}

	public void setAlternativeNameList(List<SelectableObject> alternativeNameList) {
		this.alternativeNameList = alternativeNameList;
	}
	
}
