package de.mpiwg.itgroup.ismi.auxObjects.lo;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.CurrentWitnessBean;

public class WitnessAuthorLO extends ListenerObject {
	private static final long serialVersionUID = -6143808296200456713L;
	
	private CurrentWitnessBean witnessBean;
	
	public WitnessAuthorLO(String classObj, String attName, CurrentWitnessBean witnessBean){
		super(classObj, attName);
		this.witnessBean = witnessBean;
	}
	
	
	@Override
	public void actionListenerSelect(ActionEvent event){
		super.actionListenerSelect(event);
		this.witnessBean.updateAuthor();
	}
}
