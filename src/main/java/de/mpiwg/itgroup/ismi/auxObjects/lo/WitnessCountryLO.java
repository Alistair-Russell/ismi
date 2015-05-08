package de.mpiwg.itgroup.ismi.auxObjects.lo;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.CodexEditorTemplate;
import de.mpiwg.itgroup.ismi.entry.beans.CurrentWitnessBean;

public class WitnessCountryLO extends ListenerObject{
	
	private static final long serialVersionUID = 7100404791285987664L;
	
	private CodexEditorTemplate witnessBean;
	
	public WitnessCountryLO(String classObj, String attName, CodexEditorTemplate witnessBean){
		super(classObj, attName);
		this.witnessBean = witnessBean;
	}
	
	
	@Override
	public void actionListenerSelect(ActionEvent event){
		super.actionListenerSelect(event);
		
		this.witnessBean.setCitiesInCurrentCountry(new ArrayList<SelectItem>());

		if (this.entity!=null && this.entity.isPersistent()){
			this.witnessBean.setCountry(this.entity);	
		}
		this.witnessBean.checkConsistencyFromCountryToCodex();
	}
	
	@Override
	public void valueChangeMethod(ValueChangeEvent event) {
		this.entityInfo =  null;
		changeListener(event, classObj, attName, "type", "region");
		
	}

}
