package de.mpiwg.itgroup.ismi.auxObjects.lo;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.CodexEditorTemplate;
import de.mpiwg.itgroup.ismi.entry.beans.CurrentWitnessBean;

public class WitnessCityLO extends ListenerObject{

	private static final long serialVersionUID = -1576005707417850556L;

	private CodexEditorTemplate witnessBean;
	
	public WitnessCityLO(String classObj, String attName, CodexEditorTemplate witnessBean){
		super(classObj, attName);
		this.witnessBean = witnessBean;
	}
	
	@Override
	public void actionListenerSelect(ActionEvent event){
		super.actionListenerSelect(event);
		
		this.witnessBean.setRepositoriesInCurrentCity(new ArrayList<SelectItem>());
		
		if (this.entity!=null && this.entity.isPersistent()){
			this.witnessBean.setCity(this.entity);
		}
		this.witnessBean.checkConsistencyFromCountryToCodex();
		
	}
	
	@Override
	public void valueChangeMethod(ValueChangeEvent event) {
		this.entityInfo =  null;
		changeListener(event, classObj, attName, "type", "city");
		
	}

}
