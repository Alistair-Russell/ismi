package de.mpiwg.itgroup.ismi.publicView.pages;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.ViewerAttribute;
import org.mpi.openmind.repository.bo.ViewerPage;
import org.mpi.openmind.repository.utils.OMUtils;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;

public class JSFDynamicPage extends AbstractBean{

	private static final long serialVersionUID = 8946353053956179797L;
	
	private Long currentEntId;
	private ViewerPage page;
	private Map<String, List<String>> attMap;
	private Map<String, String> attMapTextAlign;
	private List<String> labelList;
	
	public void load(Long currentEntId){
		Entity ent = getWrapper().getEntityById(currentEntId);
		this.page = null;
		
		this.attMap = new HashMap<String, List<String>>();
		this.attMapTextAlign = new HashMap<String, String>();
		this.labelList = new ArrayList<String>();
		if(ent != null){
			
			this.page = getWrapper().getViewerPage(ent.getObjectClass());
			if(page != null){
				List<ViewerAttribute> attList = getWrapper().getViewerAttributes(page.getId());
				for(ViewerAttribute att : attList){
					try {
						//TODO show always???
						this.attMapTextAlign.put(att.getLabel(), att.getTextAlign());
						List<String> values = OMUtils.resolveQuery(currentEntId, att.getQuery(), getWrapper(), att.getContentType());
						this.attMap.put(att.getLabel(), values);
						this.labelList.add(att.getLabel());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("");
	}


	public Long getCurrentEntId() {
		return currentEntId;
	}

	public void setCurrentEntId(Long currentEntId) {
		this.currentEntId = currentEntId;
	}

	public ViewerPage getPage() {
		return page;
	}


	public void setPage(ViewerPage page) {
		this.page = page;
	}


	public Map<String, List<String>> getAttMap() {
		return attMap;
	}


	public void setAttMap(Map<String, List<String>> attMap) {
		this.attMap = attMap;
	}
	
	public List<String> getLabels(){
		return this.labelList;
	}

	public Map<String, String> getAttMapTextAlign() {
		return attMapTextAlign;
	}	
	
}
