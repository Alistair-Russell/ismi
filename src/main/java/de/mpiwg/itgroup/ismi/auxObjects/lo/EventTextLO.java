package de.mpiwg.itgroup.ismi.auxObjects.lo;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.event.beans.AbstractEvent;

public class EventTextLO  extends ListenerObject {
	private static final long serialVersionUID = -2307053338601250211L;
	
	private AbstractEvent event;
	
	public EventTextLO(String classObj, String attName, AbstractEvent event){
		super(classObj, attName);
		this.event = event;
	}
	
	@Override
	public void actionListenerSelect(ActionEvent e){
		super.actionListenerSelect(e);
		event.setWitness(null);
		if(event.getTextLo().entity != null && event.getTextLo().entity.isPersistent()){
			event.refreshWitnesses(event.getTextLo().entity);
		}else{
			event.setWitnessList(new ArrayList<SelectItem>());
		}
	}

}
