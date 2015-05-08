package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.utils.ISMIUtils;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;


public class ReferenceTable extends AbstractBean{

	private Reference ref;
	private Reference originalRef;
	
	
	public List<SelectableObject<Reference>> list = new ArrayList<SelectableObject<Reference>>();
	
	public ReferenceTable(){}
	
	public void loadRefs(List<Entity> refs){
		for(Entity ent : refs){
			this.list.add(new SelectableObject<Reference>(new Reference(ent)));
		}
	}
	
	public void listenerEditRef(ActionEvent event){
		SelectableObject<Reference> so = (SelectableObject<Reference>)getRequestBean("item");
		if(so != null && so.getObj() != null){
			this.originalRef = (Reference)so.getObj();
			try {
				this.ref = (Reference)this.originalRef.clone();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			addErrorMsg("Problems loading references");
		}
	}
	
	public String actionEditRef(){
		SelectableObject<Reference> so = (SelectableObject<Reference>)getRequestBean("item");
		if(so != null && so.getObj() != null){
			this.originalRef = (Reference)so.getObj();
			try {
				this.ref = (Reference)this.originalRef.clone();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			addErrorMsg("Problems loading references");
		}
		return new String();
	}
	
	public void actionListenerSave(ActionEvent event){
		this.save();
	}
	
	private void save(){
		System.out.println("actionRefSave actionRefSave actionRefSave");
		if(!ref.isEmpty()){
			ref.getEnt();
			if(originalRef != null){
				ISMIUtils.replaceRef(list, originalRef, ref);	
			}else{
				list.add(new SelectableObject<Reference>(ref));
			}
		}
		this.ref = null;
		this.originalRef = null;
	}
	
	public void listenerRefSave(ActionEvent event){
		if(!ref.isEmpty()){
			ref.getEnt();
			if(originalRef != null){
				ISMIUtils.replaceRef(list, originalRef, ref);	
			}else{
				list.add(new SelectableObject<Reference>(ref));
			}
		}
		this.ref = null;
		this.originalRef = null;
	}
	
	public void listenerCreateRef(ActionEvent event){
		this.ref = new Reference(null);		
		this.originalRef = null;
	}
	
	public String actionCreateRef(){
		System.out.println("actionCreateRef");
		this.ref = new Reference(null);		
		this.originalRef = null;
		return new String("");
	}
	
	public String actionCancel(){
		this.ref = null;
		this.originalRef = null;
		return new String();
	}
	
	
	public void listenerCancel(ActionEvent event){
		this.ref = null;
		this.originalRef = null;
	}	
	
	public String actionRemoveRef(){
		System.out.println("actionRemoveRef");
		List<SelectableObject<Reference>> toDelete = new ArrayList<SelectableObject<Reference>>();
		for(SelectableObject<Reference> so : this.list){
			if(so.isSelected()){
				toDelete.add(so);
			}
		}
		for(SelectableObject<Reference> so : toDelete){
			this.list.remove(so);
		}
		return new String("");
	}
	
	public void listenerRemoveRef(ActionEvent event){
		List<SelectableObject<Reference>> toDelete = new ArrayList<SelectableObject<Reference>>();
		for(SelectableObject<Reference> so : this.list){
			if(so.isSelected()){
				toDelete.add(so);
			}
		}
		for(SelectableObject<Reference> so : toDelete){
			this.list.remove(so);
		}
	}

	public Reference getRef() {
		return ref;
	}

	public void setRef(Reference ref) {
		this.ref = ref;
	}

	public Reference getOriginalRef() {
		return originalRef;
	}

	public void setOriginalRef(Reference originalRef) {
		this.originalRef = originalRef;
	}

	public List<SelectableObject<Reference>> getList() {
		return list;
	}

	public void setList(List<SelectableObject<Reference>> list) {
		this.list = list;
	}
	
	public boolean getHasReferences(){
		if(this.list.isEmpty())
			return false;
		return true;
	}
}
