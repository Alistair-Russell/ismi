package de.mpiwg.itgroup.ismi.util.guiComponents;

import org.mpi.openmind.repository.bo.Entity;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class TargetMisattribution{
	private Entity person;
	//private Publication pub;
	private ESciDocItem item;
	
	public TargetMisattribution(Entity person, String refId, String notes, ApplicationBean appBean){
		this.person = person;
		
		this.item = appBean.getRefCache().getItem(refId);
		if(this.item == null){
			this.item = new ESciDocItem(refId);
			this.item.setErrorLoading(ESciDocItem.ESCIDOC_ERROR_ID_NO_FOUND);
		}
		this.item.getPublication().setAdditionalInformation(notes);
		
		/*
		try{
			this.pub = ESciDocHandler.getPublicationById(refId);
		}catch (Exception e) {
			System.err.println("The references could not be loaded. The server did not respond.");
		}
		
		if(this.pub == null){
			this.pub = new Publication(refId);
			this.pub.setErrorLoading(Publication.ESCIDOC_ERROR_ID_NO_FOUND);
		}
		this.pub.setAdditionalInformation(notes);
		*/
	}
	
	public Entity getPerson() {
		return person;
	}
	public void setPerson(Entity person) {
		this.person = person;
	}

	public ESciDocItem getItem() {
		return item;
	}

	public void setItem(ESciDocItem item) {
		this.item = item;
	}
}