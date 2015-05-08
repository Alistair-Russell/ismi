package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

public class EndNoteMisattributionTable extends AbstractISMIBean{
	private static final long serialVersionUID = -2756216426560705499L;

	private List<SelectableObject<EndNoteMisattribution>> list = new ArrayList<SelectableObject<EndNoteMisattribution>>();
	
	private EndNoteMisattribution misatt;
	
	private ListenerObject lo = new ListenerObject(PERSON, "name_translit");
	//private String attName;
	//private String oc;
	
	public EndNoteMisattributionTable(){
		/*
		this.attName = attName;
		this.oc = oc;
		*/
	}
	
	public void listenerRemove(ActionEvent event){		
		List<SelectableObject<EndNoteMisattribution>> toDelete = new ArrayList<SelectableObject<EndNoteMisattribution>>();
		
		for(SelectableObject<EndNoteMisattribution> so : this.list){
			if(so.isSelected()){
				toDelete.add(so);
			}
		}
		for(SelectableObject<EndNoteMisattribution> so : toDelete){
			this.list.remove(so);
		}
	}
	
	public void listenerEditRef(ActionEvent event){
		SelectableObject<EndNoteMisattribution> so = 
			(SelectableObject<EndNoteMisattribution>)getRequestBean("item");
		if(so != null){
			this.misatt = so.getObj();
		}
	}
	
	public void listenerSaveRef(ActionEvent event){
		this.misatt = null;
	}
	
	public void listenerCancel(ActionEvent event){
		//@TODO
	}
	
	public void listenerCreate(ActionEvent event){
		if(this.lo.entity != null & this.lo.entity.isPersistent()){
			this.create(this.lo.entity);	
		}
	}
	
	public void load(Entity misattEntity){
		if(!containsEntity(misattEntity)){
			//TODO sort
			//Collections.sort(this.misattList);
			try {
				this.list.add(new SelectableObject<EndNoteMisattribution>(EndNoteMisattribution.load(misattEntity, getWrapper(), getUserName())));
			} catch (Exception e) {
				addErrorMsg(e.getMessage());
				e.printStackTrace();
			}
		}else{
			addGeneralMsg("This entity has been already inserted!");
		}
	}
	
	public void create(Entity person){
		try {
			this.list.add(new SelectableObject<EndNoteMisattribution>(EndNoteMisattribution.create(person, getWrapper(), getUserName())));
		} catch (Exception e) {
			addErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private boolean containsEntity(Entity ent){
		for(SelectableObject<EndNoteMisattribution> so : this.list){
			if(so.getObj().getPerson() != null && so.getObj().getPerson().getId().equals(ent.getId())){
				return true;
			}
		}
		return false;
	}
	
	/*
	public void listenerChanged(ValueChangeEvent event) {
		this.lo = changeListener(event, lo, this.oc, this.attName);
		if(lo.entity != null && lo.entity.isPersistent()){
			lo.statusImage.setStatus(StatusImage.STATUS_OK);
		}else{
			lo.statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
	}
	*/

	
	public Entity saveMisattributions(Entity text) throws Exception{
		for(EndNoteMisattribution misatt : this.getMisattList()){
			text = misatt.saveAndGetMisattribution(text);
		}
		return text;
	}
	
	
	private List<EndNoteMisattribution> getMisattList(){
		List<EndNoteMisattribution> list = new ArrayList<EndNoteMisattribution>();
		for(SelectableObject<EndNoteMisattribution> so : this.list){
			list.add(so.getObj());
		}
		return list;
	}
	
	public List<SelectableObject<EndNoteMisattribution>> getList() {
		return list;
	}

	public void setList(List<SelectableObject<EndNoteMisattribution>> list) {
		this.list = list;
	}

	public EndNoteMisattribution getMisatt() {
		return misatt;
	}

	public void setMisatt(EndNoteMisattribution misatt) {
		this.misatt = misatt;
	}

	public ListenerObject getLo() {
		return lo;
	}

	public void setLo(ListenerObject lo) {
		this.lo = lo;
	}
}
