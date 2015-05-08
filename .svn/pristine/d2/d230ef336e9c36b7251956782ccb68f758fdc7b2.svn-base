package org.mpiwg.itgroup.escidoc.utils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.mpiwg.itgroup.escidoc.ESciDocCache;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.MisattributionDataTable;

public class ESciDocItemDataTable extends AbstractBean{
	
	private List<ESciDocItem> items = new ArrayList<ESciDocItem>();
	private String term;
	
	private boolean showDialog = false;
	private AbstractISMIBean entityForm;
	private MisattributionDataTable misattDataTable;
	
	private int sortBy = ESciDocCache.SORT_DEFAULT;
	
	private static List<SelectItem> sortAlternatives;
	
	static{
		sortAlternatives = new ArrayList<SelectItem>();
		sortAlternatives.add(new SelectItem(ESciDocCache.SORT_DEFAULT, "Id"));
		sortAlternatives.add(new SelectItem(ESciDocCache.SORT_TITLE, "Title"));
		sortAlternatives.add(new SelectItem(ESciDocCache.SORT_CREATOR, "Authors"));
	}
	
	public ESciDocItemDataTable(AbstractISMIBean entityForm){
		this.entityForm = entityForm;
		this.reset();
	}
	
	public ESciDocItemDataTable(MisattributionDataTable misattDataTable){
		this.misattDataTable = misattDataTable;
		this.reset();
	}
	
	public void listenerSort(ValueChangeEvent event){
		if(event.getNewValue() != null)
		ESciDocCache.sort(new Integer(event.getNewValue().toString()), items);
	}
	
	public void listenerSelectRefItem(ActionEvent event){
		ESciDocItem item = (ESciDocItem)getRequestBean("refItem");
		if(item != null){
			if(entityForm != null){
				SelectedESciDocItems selectedPubs = this.entityForm.getSelectedItems();
				if(selectedPubs != null){
					if(!selectedPubs.addESciDocItem((ESciDocItem)item.clone())){
						getSessionBean().addGeneralMsg("The reference was already selected.");
					}	
				}	
			}else if(misattDataTable != null){
				misattDataTable.setESciDocItem(item);
			}
			this.showDialog = false;
		}
	}
	
	public void listenerEditItem(ActionEvent event){
		ESciDocItem item = (ESciDocItem)getRequestBean("refItem");
		getSessionBean().geteSciDocForm().setItem(item);
		this.term = null;
		this.items = null;
		this.showDialog = false;
	}
	
	public void listenerCreateReference(){
		getSessionBean().geteSciDocForm().createItem();
		this.close();
	}
	
	public void listenerReset(ActionEvent event){
		this.reset();
	}
	
	public void listenerFilter(ActionEvent event){
		this.filter();
	}
	
	public void listenerOpen(ActionEvent event){
		this.open();
	}
	
	public void open(){
		this.reset();
		this.showDialog = true;
	}
	
	public void listenerClose(ActionEvent event){
		this.close();
	}
	
	public void close(){
		this.term = null;
		this.items = null;
		this.showDialog = false;
	}
	
	public void reset(){
		/*
		this.term = null;
		List<ESciDocItem> list = this.getAppBean().getRefCache().getAllItems(sortBy);
		if(list != null){
			this.items = new ArrayList<ESciDocItem>(list);
		}else{
			this.items = new ArrayList<ESciDocItem>();	
		}*/
	}
	
	public void filter(){
		this.items = this.getAppBean().getRefCache().searchItem(term, sortBy);
	}		
	public int getItemsSize(){
		if(items == null)
			return 0;
		return items.size();
	}
	
	public List<ESciDocItem> getItems() {
		return items;
	}

	public void setItems(List<ESciDocItem> items) {
		this.items = items;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}
	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public List<SelectItem> getSortAlternatives() {
		return sortAlternatives;
	}
}
