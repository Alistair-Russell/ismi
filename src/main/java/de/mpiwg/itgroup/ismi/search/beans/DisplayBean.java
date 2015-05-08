package de.mpiwg.itgroup.ismi.search.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpiwg.itgroup.escidoc.ESciDocHandler;
import org.mpiwg.itgroup.escidoc.bo.Publication;

import de.mpiwg.itgroup.ismi.auxObjects.SelectItem0;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;
import de.mpiwg.itgroup.ismi.entry.utils.PrivacityUtils;
import de.mpiwg.itgroup.ismi.util.guiComponents.HtmlOption;
import de.mpiwg.itgroup.ismi.utils.templates.TitleTemplate;
import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;

public class DisplayBean  extends AbstractISMIBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1296816397468908012L;

	private static Logger logger = Logger.getLogger(DisplayBean.class);
	
	protected TitleTemplate title;
	protected WitnessTemplate witness;
	
	//Witness elements
	protected List<SelectItem0> witnessItems = new ArrayList<SelectItem0>();
	protected Long selectedWitnessId;
	
	//Title elements
	protected Long selectedTitleId;
	protected List<SelectItem0> titleItems = new ArrayList<SelectItem0>();
	
	protected void resetDisplay(){
		this.witness = null;
		this.selectedWitnessId = null;
		this.title = null;
		this.selectedTitleId = null;
		this.witnessItems = new ArrayList<SelectItem0>();
	}
	
	public void showTitle(Long textId){
		this.selectedTitleId = textId;
		
		this.witness = null;
		this.witnessItems = new ArrayList<SelectItem0>();
		this.selectedWitnessId = null;
		
		long start = System.currentTimeMillis();
		
		Entity entTitle = getWrapper().getEntityById(this.selectedTitleId);
		
		if(this.selectedTitleId != null){
			
			this.titleItems = SelectItem0.valueChange(titleItems, textId);
			this.title = new TitleTemplate(entTitle, getWrapper(), false);
			
			//****** getting witnesses **********************
			long startWitness = System.currentTimeMillis();
			List<Entity> list = getWrapper().getSourcesForTargetRelation(selectedTitleId, is_exemplar_of, WITNESS, -1);
			for(Entity src : list){
				SelectItem0 item = new SelectItem0(src.getId(), src.getOwnValue() + " [" + src.getId() + "]", true);
				this.witnessItems.add(item);
			}
			
			if(list.size() > 0){
				this.showWitness(list.get(0).getId());
			}
			
			long diff = System.currentTimeMillis() - startWitness;
			if(diff > 1000){
				logger.info("Time - listenerShowTitle().getSourcesForTargetRelation(title, 'X', WITNESS, -1) = " + diff + " [ms], Text id= " + this.selectedTitleId);
				
			}
			//************************************************ 
		}
		long diff = System.currentTimeMillis() - start;
		
		if(diff > 1000){
			logger.info("Time - listenerShowTitle() " + diff + " [ms], Text id= " + this.selectedTitleId);
		}
	}
	
	
	public void listenerShowWitness0(ActionEvent event){
		SelectItem0 item = (SelectItem0)getRequestBean("witnessItem");
		if (item != null) {
			this.redirect(null, "?textId=" + selectedTitleId + "&witnessId=" + item.getValue() + "#witnesses");
		}
	}
	
		
	public void showWitness(Long selectedWitnessId) {
		
		this.selectedWitnessId = selectedWitnessId;
		
		if (this.selectedWitnessId != null) {
			this.witnessItems = SelectItem0.valueChange(witnessItems, selectedWitnessId);
			Entity entWitness = getWrapper().getEntityById(selectedWitnessId);
			
			if (entWitness != null) {
				this.witness = new WitnessTemplate(entWitness, getWrapper(), true);
			}	
		}
	}
	
    /**
     * This method is called from the result of the simple search. It will set the title(text) into the currentText to be edited.
     * @return
     */
    public String actionEditTitle() {
    	if(this.selectedTitleId != null){
        	getSessionBean().editEntity(getWrapper().getEntityById(selectedTitleId));
        	return "entry_edit_entity";
    	}
    	return "";
    }
	
	/**
	 * This method is called from the result of the simple search. It will set
	 * the witness into the currentWitness to be edited.
	 * 
	 * @return
	 */
	public String actionEditWitness() {
		if (this.selectedWitnessId != null) {
			getSessionBean().editEntity(getWrapper().getEntityById(selectedWitnessId));
			return "entry_edit_entity";
		}
		return "";
	}
	
	//Privacity titles
	public void changePrivacity4Title(ActionEvent event){
		try {
			if(selectedTitleId != null){
				List<Entity> saveList = PrivacityUtils.changePrivacity4Title(getWrapper().getEntityById(selectedTitleId), null, getWrapper());
				getWrapper().saveEntityListAsNodeWithoutContent(saveList, getUserName());
				this.showTitle(selectedTitleId);
			}			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
	}
	
	//Privacity witnesses
	public void changePrivacity4Witness(ActionEvent event){
		try {
			if(selectedWitnessId != null){
				List<Entity> saveList = PrivacityUtils.changePrivacity4Witness(getWrapper().getEntityById(selectedWitnessId), null, getWrapper());
				getWrapper().saveEntityListAsNodeWithoutContent(saveList, getUserName());
				this.showWitness(selectedWitnessId);
			}	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
		
	}
	
	private void changeAllWitnesses(boolean privacity) throws Exception{
		List<Entity> saveList = new ArrayList<Entity>();
		for(SelectItem item : witnessItems){
			Long id = (Long)item.getValue();
			Entity witness = getWrapper().getEntityById(id);
			saveList.addAll(PrivacityUtils.changePrivacity4Witness(witness, privacity, getWrapper()));
		}
		getWrapper().saveEntityListAsNodeWithoutContent(saveList, getUserName());
		this.showWitness(selectedWitnessId);
	}
	
	public void listenerMakeAllWitnessesPublic(ActionEvent event){
		try {
			this.changeAllWitnesses(true);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
	}
	
	public void listenerMakeAllWitnessesPrivate(ActionEvent event){
		try {
			this.changeAllWitnesses(false);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
		
	}
	
	public int getWitnessItemsSize() {
		if(witnessItems != null)
			return witnessItems.size();
		return 0;
	}

	public TitleTemplate getTitle() {
		return title;
	}

	public WitnessTemplate getWitness() {
		return witness;
	}

	public List<SelectItem0> getWitnessItems() {
		return witnessItems;
	}

	public Long getSelectedWitnessId() {
		return selectedWitnessId;
	}

	public Long getSelectedTitleId() {
		return selectedTitleId;
	}

	public void setSelectedWitnessId(Long selectedWitnessId) {
		this.selectedWitnessId = selectedWitnessId;
	}

	public void setSelectedTitleId(Long selectedTitleId) {
		this.selectedTitleId = selectedTitleId;
	}
}
