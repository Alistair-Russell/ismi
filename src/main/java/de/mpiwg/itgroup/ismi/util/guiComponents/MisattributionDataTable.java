package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.mpi.openmind.repository.bo.Entity;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.utils.ESciDocItemDataTable;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

/**
 * TEXT misattributed_to PERSON
 * @author jurzua
 *
 */
public class MisattributionDataTable extends AbstractISMIBean{
	
	private List<TargetMisattribution> misattList = new ArrayList<TargetMisattribution>();
	private Map<Long, Boolean> selectionList = new HashMap<Long, Boolean>();
	
	private ListenerObject lo = new ListenerObject();
	private String attName;
	private String objectClass;
	
	private TargetMisattribution currentMisatt;
	private ESciDocItemDataTable refDataTable;
	
	public MisattributionDataTable(String oc, String attName){
		this.attName = attName;
		this.objectClass = oc;
		this.refDataTable = new ESciDocItemDataTable(this);
	}
	
	public void setESciDocItem(ESciDocItem item){
		if(this.currentMisatt != null){
			this.currentMisatt.setItem(item);
		}
	}
	
	public void listenerCloseRefDialog(ActionEvent event){
		this.currentMisatt = null;
		this.refDataTable.listenerClose(event);
	}
	
	public void listenerOpenRefDialog(ActionEvent event){
		this.currentMisatt = (TargetMisattribution)getRequestBean("misatt");
		this.refDataTable.listenerOpen(event);
	}
	
	
	public void listenerAdd(ActionEvent event){
		this.add(this.lo.entity);
	}
	
	public void listenerRemoveSelection(ActionEvent event){
		for(Entity remove : getSelectedEntities()){
			this.remove(remove.getId());
		}
	}
	
	public void listenerChanged(ValueChangeEvent event) {
		this.lo = changeListener(event, lo, this.objectClass, this.attName);
		if(lo.entity != null && lo.entity.isPersistent()){
			lo.statusImage.setStatus(StatusImage.STATUS_OK);
		}else{
			lo.statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
	}
	
	private List<Entity> getSelectedEntities(){
		List<Entity> list = new ArrayList<Entity>();
		for(TargetMisattribution misatt : this.misattList){
			Entity e = misatt.getPerson();
			if(selectionList.get(e.getId())){
				list.add(e);
			}
		}
		return list;
	}
	
	public void add(Entity e){
		if(e != null && e.isPersistent()){
			if(!selectionList.containsKey(e.getId())){
				this.misattList.add(new TargetMisattribution(e, null, null, getAppBean()));
				this.selectionList.put(e.getId(), false);
				//TODO sort
				//Collections.sort(this.misattList);
			}
		}
	}
	
	public void add(Entity person, String refId, String notes){
		this.misattList.add(new TargetMisattribution(person, refId, notes, getAppBean()));
		this.selectionList.put(person.getId(), false);
	}
	
	public void remove(Long id){
		this.selectionList.remove(id);
		TargetMisattribution toDelete = null;
		for(TargetMisattribution misatt : this.misattList){
			Entity ent = misatt.getPerson();
			if(ent.getId().intValue() == id){
				toDelete = misatt;
				break;
			}
		}
		this.misattList.remove(toDelete);
	}
	
	
	public List<TargetMisattribution> getMisattList() {
		return misattList;
	}

	public void setMisattList(List<TargetMisattribution> misattList) {
		this.misattList = misattList;
	}

	public Map<Long, Boolean> getSelectionList() {
		return selectionList;
	}
	public void setSelectionList(Map<Long, Boolean> selectionList) {
		this.selectionList = selectionList;
	}
	public ListenerObject getLo() {
		return lo;
	}
	public void setLo(ListenerObject lo) {
		this.lo = lo;
	}
	public String getAttName() {
		return attName;
	}
	public void setAttName(String attName) {
		this.attName = attName;
	}
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	public TargetMisattribution getCurrentMisatt() {
		return currentMisatt;
	}
	public void setCurrentMisatt(TargetMisattribution currentMisatt) {
		this.currentMisatt = currentMisatt;
	}

	public ESciDocItemDataTable getRefDataTable() {
		return refDataTable;
	}

	public void setRefDataTable(ESciDocItemDataTable refDataTable) {
		this.refDataTable = refDataTable;
	}
}
