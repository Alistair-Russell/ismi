package de.mpiwg.itgroup.ismi.auxObjects.lo;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.CodexEditorTemplate;
import de.mpiwg.itgroup.ismi.entry.beans.CurrentWitnessBean;

public class WitnessRepositoryLO  extends ListenerObject{
	
	private static final long serialVersionUID = 8313432163139959772L;
	
	private CodexEditorTemplate witnessBean;
	
	public WitnessRepositoryLO(String classObj, String attName, CodexEditorTemplate witnessBean){
		super(classObj, attName);
		this.witnessBean = witnessBean;
	}
	
	
	@Override
	public void actionListenerSelect(ActionEvent event){
		super.actionListenerSelect(event);
				
		this.witnessBean.setCollectionsInCurrentRepository(new ArrayList<SelectItem>());
		this.witnessBean.setShelfMarksInCurrentCollection(new ArrayList<SelectItem>());

		if (this.entity!= null && this.entity.isPersistent()){
			this.witnessBean.setRepository(this.entity);
		}
		this.witnessBean.checkConsistencyFromCountryToCodex();		
	}
}
