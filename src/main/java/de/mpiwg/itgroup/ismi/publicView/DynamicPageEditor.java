package de.mpiwg.itgroup.ismi.publicView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.ViewerAttribute;
import org.mpi.openmind.repository.bo.ViewerPage;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

public class DynamicPageEditor extends AbstractBean{
	private static final long serialVersionUID = 223061720960235795L;
	
	private static List<SelectItem> displayModeList = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedTextAlign = new ArrayList<SelectItem>();
	private static List<SelectItem> contentTypeList = new ArrayList<SelectItem>();
	
	private List<SelectItem> definitionList;
	
	static{
		displayModeList.add(new SelectItem(ViewerAttribute.SHOW_ALWAYS, "Show always"));
		displayModeList.add(new SelectItem(ViewerAttribute.SHOW_WITH_CONTENT, "Show only if there is content"));
		displayModeList.add(new SelectItem(ViewerAttribute.HIDE, "Hide"));
		
		suggestedTextAlign.add(new SelectItem(ViewerAttribute.ALIGN_RIGHT));
		suggestedTextAlign.add(new SelectItem(ViewerAttribute.ALIGN_LEFT));
		suggestedTextAlign.add(new SelectItem(ViewerAttribute.ALIGN_CENTER));
		
		contentTypeList.add(new SelectItem(ViewerAttribute.CONTENT_TEXT, "Plain text"));
		contentTypeList.add(new SelectItem(ViewerAttribute.CONTENT_DATE, "Date"));
	}

	private static Logger logger = Logger.getLogger(DynamicPageEditor.class);
	
	private List<SelectItem> pageList;
	
	private List<SelectableObject<ViewerAttribute>> attList;
	
	private Long pageId;
	private ViewerPage page;
	
	
	public DynamicPageEditor(){
		logger.info("Init DynamicPageEditor");
	}
	
	public void load(){
		logger.info("Loading pages!!!");
		Collection<ViewerPage> pages = getWrapper().getViewerPages();
		this.pageList = new ArrayList<SelectItem>();
		this.pageList.add(new SelectItem(new Long(-1), "-- select one --"));
		for(ViewerPage page : pages){
			this.pageList.add(new SelectItem(page.getId(), page.getDefinition()));
		}
	}	
	
	public void listenerCreatePage(ActionEvent event){
		this.page = new ViewerPage();
	}
	
	public void listenerSavePage(ActionEvent event){
		getWrapper().saveViewerPage(page, getSessionBean().getUsername());
		this.load();
	}	
	
	public void listenerAddAtt(ActionEvent event){
		if(attList == null)
			attList = new ArrayList<SelectableObject<ViewerAttribute>>();
		
		ViewerAttribute att = new ViewerAttribute();
		att.setIndex(this.attList.size());
		this.attList.add(new SelectableObject<ViewerAttribute>(att));	
	}
	
	public void listenerSelectPage(AjaxBehaviorEvent event){
		try {
			//System.out.println("AjaxBehaviorEvent");
			if(this.pageId != null && this.pageId != -1){
				this.page = getWrapper().getViewerPage4Edition(pageId);
				List<ViewerAttribute> tmp = getWrapper().getViewerAttributes4Edition(pageId);
				this.attList = new ArrayList<SelectableObject<ViewerAttribute>>();
				for(ViewerAttribute att : tmp){
					this.attList.add(new SelectableObject<ViewerAttribute>(att));
				}
			}else{
				this.page = null;
				this.attList = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			addException(e);
		}
	}
	
	public void listenerRemoveAttributes(ActionEvent event){
		for(SelectableObject<ViewerAttribute> so : new ArrayList<SelectableObject<ViewerAttribute>>(this.attList)){
			if(so.isSelected()){
				if(so.getObj().isPersistent())
					this.getWrapper().removeViewerAnttribute(so.getObj());
				this.attList.remove(so);
			}
		}
		
		for(int i=0; i < this.attList.size(); i++){
			this.attList.get(i).getObj().setIndex(i);
		}
		
	}
	
	public void listenerRemovePage(ActionEvent event){
		if(getWrapper().removeViewerPage(page) > 0){
			getSessionBean().addGeneralMsg("The Page and its attirbutes have been removed successfully!");
		}
	}
	
	public void listenerClean(ActionEvent event){
		this.page = null;
		this.attList = null;
	}
	
	public List<SelectItem> getPageList() {
		if(this.pageList == null){
			this.load();
		}
		return pageList;
	}

	public void setPageList(List<SelectItem> pageList) {
		this.pageList = pageList;
	}

	public ViewerPage getPage() {
		return page;
	}

	public void setPage(ViewerPage page) {
		this.page = page;
	}
	
	public boolean isPagePersistent(){
		return page != null && page.isPersistent();
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public List<SelectableObject<ViewerAttribute>> getAttList() {
		return attList;
	}

	public void setAttList(List<SelectableObject<ViewerAttribute>> attList) {
		this.attList = attList;
	}

	
	public void listenerSaveAll(ActionEvent event){
		try {
			for(int i=0; i<this.attList.size(); i++){
				ViewerAttribute att = this.attList.get(i).getObj();
				att.setIndex(i);
				getWrapper().saveViewerAttribute(page, att, getSessionBean().getUsername());					
			}
			addGeneralMsg("The attributes of the page have been saved successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			addException(e);
		}
	}
	
	public void listenerUp(ActionEvent event){
		logger.info("listenerUp");
		Object obj = getRequestBean("att");
		if(obj != null){
			SelectableObject<ViewerAttribute> currentAtt = (SelectableObject<ViewerAttribute>)obj;
			int index = this.attList.indexOf(currentAtt);
			if(index > 0){
				SelectableObject<ViewerAttribute> previousAtt = this.attList.get(index-1);
				currentAtt.getObj().setIndex(index-1);
				previousAtt.getObj().setIndex(index);
				
				this.attList.set(index-1, currentAtt);
				this.attList.set(index, previousAtt);
			}
		}
	}
	
	public void listenerDown(ActionEvent event){
		logger.info("listenerDown");
		Object obj = getRequestBean("att");
		if(obj != null){
			SelectableObject<ViewerAttribute> currentAtt = (SelectableObject<ViewerAttribute>)obj;
			int index = this.attList.indexOf(currentAtt);
			if(index < this.attList.size()-1){
				SelectableObject<ViewerAttribute> nextAtt = this.attList.get(index+1);
				currentAtt.getObj().setIndex(index+1);
				nextAtt.getObj().setIndex(index);
				
				this.attList.set(index, nextAtt);
				this.attList.set(index+1, currentAtt);
				
			}
		}
		
	}
	
	public List<SelectItem> getDisplayModeList(){
		return displayModeList;
	}
	
	public List<SelectItem> getSuggestedTextAlign(){
		return suggestedTextAlign;
	}
	
	public List<SelectItem> getContentTypeList(){
		return contentTypeList;
	}

	public List<SelectItem> getDefinitionList(){
		if(definitionList == null){
			this.loadDefinitionList();
		}
		return definitionList;
	}
	
	private void loadDefinitionList(){
		this.definitionList = new ArrayList<SelectItem>();
		this.definitionList.add(new SelectItem("-- select one --"));
		for(Entity def : getWrapper().getLWDefinitions()){
			this.definitionList.add(new SelectItem(def.getOwnValue()));
		}
		
	}
	
}
