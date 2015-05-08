package de.mpiwg.itgroup.ismi.search.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.auxObjects.SelectItem0;
import de.mpiwg.itgroup.ismi.entry.utils.PrivacityUtils;
import de.mpiwg.itgroup.ismi.utils.templates.AuthorTemplate;

public class DisplayAuthorBean extends DisplayBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5003760977919749691L;

	private static Logger logger = Logger.getLogger(DisplayAuthorBean.class);

	private AuthorTemplate author;
	private Long selectedAuthorId;
	
	public void showAuthor(Long authorId) {
		this.resetDisplay();
		
		this.selectedAuthorId = authorId;
		Entity entAuthor = getWrapper().getEntityById(authorId);
		this.author = new AuthorTemplate(entAuthor, getWrapper());
		
		//getting titles
		this.titleItems = new ArrayList<SelectItem0>();
		int count = 0;
		for(Entity title : getWrapper().getSourcesForTargetRelation(entAuthor.getId(), "was_created_by", TEXT, -1)){
			this.titleItems.add(new SelectItem0(title.getId(), title.getOwnValue() + " [" + title.getId() + "]"));
			if(count == 0){
				this.showTitle(title.getId());
			}
			count++;
		}
		
		if(this.titleItems.size() > 0){
			this.selectedTitleId = (Long)this.titleItems.get(0).getValue();
			this.listenerShowTitle0(null);
		}
	}
	
	@Override
	protected void resetDisplay(){
		super.resetDisplay();
		this.titleItems = new ArrayList<SelectItem0>();
	}
	
	
	/*
	public void listenerShowTitle(ValueChangeEvent event) {
		if (event != null && event.getNewValue() != null) {
			this.redirect(null, "?personId=" + selectedAuthorId + "&textId=" + (Long)event.getNewValue() + "#titles");
		}
	}*/
	
	public void listenerShowTitle0(ActionEvent event){
		SelectItem0 item = (SelectItem0)getRequestBean("titleItem");
		if(item != null){
			this.redirect(null, "?personId=" + selectedAuthorId + "&textId=" + item.getValue() + "#titles");
		}
	}
	
	public String actionTest(){
		System.out.println("actionTest");
		return null;
	}
	
	/*
	@Override
	public void listenerShowWitness(ValueChangeEvent event) {	
		if (event != null && event.getNewValue() != null) {
			this.redirect(null, "?personId=" + selectedAuthorId + "&textId=" + selectedTitleId + "&witnessId=" + (Long)event.getNewValue() + "#witnesses");
		}
	}*/
	
	@Override
	public void listenerShowWitness0(ActionEvent event){
		SelectItem0 item = (SelectItem0)getRequestBean("witnessItem");
		if (item != null) {
			this.redirect(null, "?personId=" + selectedAuthorId + "&textId=" + selectedTitleId + "&witnessId=" + item.getValue() + "#witnesses");
		}
	}
	
	public String actionEditAuthor(){
		if(this.selectedAuthorId != null){
			getSessionBean().editEntity(getWrapper().getEntityById(selectedAuthorId));
			return "entry_edit_entity";
		}
		return "";
	}
	
	//Privacity author
	public void changePrivacity4Person(ActionEvent event){
		try {
			if(this.selectedAuthorId != null){
				List<Entity> saveList = PrivacityUtils.changePrivacity4Person(getWrapper().getEntityById(selectedAuthorId), null, getWrapper());
				getWrapper().saveEntityListAsNodeWithoutContent(saveList, getUserName());
			}	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
	}
	
	//Privacity witness
	private void changeAllTitles(boolean privacity) throws Exception{
		List<Entity> saveList = new ArrayList<Entity>();
		for(SelectItem item : titleItems){
			Long id = (Long)item.getValue();
			Entity title = getWrapper().getEntityById(id);
			saveList.addAll(PrivacityUtils.changePrivacity4Title(title, privacity, getWrapper()));
		}	
		getWrapper().saveEntityListAsNodeWithoutContent(saveList, getUserName());
		this.showTitle(selectedTitleId);
	}
	
	public void listenerMakeAllTitlesPublic(ActionEvent event){
		try {
			logger.debug("listenerMakeAllTitlesPublic");
			this.changeAllTitles(true);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
	}
	
	public void listenerMakeAllTitlesPrivate(ActionEvent event){
		try {
			logger.debug("listenerMakeAllTitlesPrivate");
			this.changeAllTitles(false);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}
		
	}
	
	//getters and setters

	public int getTitleItemsSize(){
		if(titleItems != null)
			return titleItems.size();
		return 0;
	}

	public Long getSelectedAuthorId() {
		return selectedAuthorId;
	}

	public AuthorTemplate getAuthor() {
		return author;
	}

	public List<SelectItem0> getTitleItems() {
		return titleItems;
	}	
}
