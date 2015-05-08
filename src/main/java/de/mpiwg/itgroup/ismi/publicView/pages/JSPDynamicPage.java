package de.mpiwg.itgroup.ismi.publicView.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.ViewerAttribute;
import org.mpi.openmind.repository.bo.ViewerPage;
import org.mpi.openmind.repository.utils.OMUtils;
import org.mpi.openmind.repository.utils.RomanizationLoC;

import de.mpiwg.itgroup.diva.jsp.AbsJSPWrapper;

public class JSPDynamicPage extends AbsJSPWrapper{
	
	private Long currentEntId;
	private ViewerPage page;
	private Map<String, List<String>> attMap;
	private Map<String, String> attMapTextAlign;
	private List<String> labelList;
	
	private boolean errorLoading;
	
	protected Entity digi;
	
	
	public void load(String currentEntId){
		try {
			this.load(Long.parseLong(currentEntId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load(Long newEntId){
		this.currentEntId = newEntId;
		Entity ent = getWrapper().getEntityById(currentEntId);
		
		this.errorLoading = (ent != null) ? true : false;
		if(ent == null){
			this.getSessionBean().addGeneralMsg("The entity " + currentEntId + " was not found.");
		}
		
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
						
						if(att.getDisplayMode() != ViewerAttribute.HIDE){
							this.attMapTextAlign.put(att.getLabel(), att.getTextAlign());
							List<String> values = OMUtils.resolveQuery(currentEntId, att.getQuery(), getWrapper(), att.getContentType());
							values = romanizeList(values);
							if(values.size() > 0 || att.getDisplayMode() == ViewerAttribute.SHOW_ALWAYS){
								this.attMap.put(att.getLabel(), values);
								this.labelList.add(att.getLabel());	
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("");
	}
	
	public List<String> romanizeList(List<String> list){
		List<String> rs = new ArrayList<String>();
		for(String s : list){
			rs.add(RomanizationLoC.convert(s));
		}
		return rs;
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

	public String getDigiLabel(){
		return (digi == null) ? null : digi.getOwnValue();
	}
	
	public Long getDigiId(){
		return (digi == null) ? null : digi.getId();
	}
	
	public Entity getDigi() {
		return digi;
	}

	public void setDigi(Entity digi) {
		this.digi = digi;
	}

	public Map<String, String> getAttMapTextAlign() {
		return attMapTextAlign;
	}
	
	public boolean isErrorLoading(){
		return this.errorLoading;
	}
}
