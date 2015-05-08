package de.mpiwg.itgroup.ismi.publicView;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.utils.NormalizerUtils;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;

public class PublicCodexBean extends AbstractBean{
	private static final long serialVersionUID = 6992265932188217438L;
	
	private static Logger logger = Logger.getLogger(PublicCodexBean.class);

	private List<Entity> currentCodexList;
	private String term;
	
	public PublicCodexBean(){
		logger.info("Init PublicCodexBean");
	}

	private void load(){
		this.currentCodexList = new ArrayList<Entity>(getAppBean().getPublicCodexList().getCodexList());
	}
	
	public void listenerSearch(ActionEvent event){
		this.search();
	}
	
	public void listenerReset(ActionEvent event){
		this.load();
	}
	
	private void search(){
		if(StringUtils.isEmpty(term)){
			this.load();
		}else{
			this.currentCodexList = new ArrayList<Entity>();
			String term0 = NormalizerUtils.normalize(term);
			for(Entity ent : getAppBean().getPublicCodexList().getCodexList()){
				
				if(StringUtils.isNotEmpty(ent.getNormalizedOwnValue()) && 
						ent.getNormalizedOwnValue().contains(term0)){
					this.currentCodexList.add(ent);
				}
			}	
		}
	}
	
	public List<Entity> getCurrentCodexList() {
		if(currentCodexList == null){
			this.load();
		}
		return currentCodexList;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
