package de.mpiwg.itgroup.ismi.entry.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.utils.templates.DigitalizationTemplate;

public class DigiListBean extends AbstractBean{
	private static final long serialVersionUID = 8982550339613012125L;
	private static Logger logger = Logger.getLogger(DigiListBean.class);
	
	
	private static List<SelectItem> codexFilter;
	
	private static int ALL = 0;
	private static int WITH_CODEX = 1;
	private static int WITHOUT_CODEX = 2;
	private int selectedFilter = ALL;
	
	static{
		codexFilter = new ArrayList<SelectItem>();
		codexFilter.add(new SelectItem(ALL, "All"));
		codexFilter.add(new SelectItem(WITH_CODEX, "With codex"));
		codexFilter.add(new SelectItem(WITHOUT_CODEX, "Without codex"));
	}
	
	private List<DigitalizationTemplate> allList;
	private List<DigitalizationTemplate> list;
	private List<String> suggestionList;
	private String filterTerm;
	
	public DigiListBean(WrapperService om){
		reset(om);
	}
	
	
	/*
	public void listenerDigiFilter(ValueChangeEvent event){
		//System.out.println("listenerDigiFilter");
		//System.out.println(event.getNewValue());
		this.filterTerm = event.getNewValue() + "";
		filter();
	}*/
	
	public void eventFilter(AjaxBehaviorEvent event){
		//System.out.println("AjaxBehaviorEvent: " + this.filterTerm);
		if(StringUtils.isNotEmpty(filterTerm)){
			filter();
		}else{
			reset(getAppBean().getWrapper());
		}
	}
	
	private void filter(){
		//System.out.println(this.allList.size());
		this.list = new ArrayList<DigitalizationTemplate>();
		this.suggestionList = new ArrayList<String>();
		
		for(DigitalizationTemplate digi : this.allList){
			if(StringUtils.containsIgnoreCase(digi.getEntity().getOwnValue(), filterTerm)){
				addDigi(digi);
			}
		}
		//System.out.println(this.list.size());
		Collections.sort(list);
	}
	
	private void reset(WrapperService om){
		//System.out.println("Reset");
		list = new ArrayList<DigitalizationTemplate>();
		allList = new ArrayList<DigitalizationTemplate>();
		suggestionList = new ArrayList<String>();
		filterTerm = null;
		
		long start = System.currentTimeMillis();
		List<Entity> entList = om.getEntitiesByDef("DIGITALIZATION");
		for(Entity digi : entList){
			DigitalizationTemplate d = new DigitalizationTemplate(digi, om);
			d.init();
			if(selectedFilter == ALL){
				//list.add(d);
				this.addDigi(d);
			}else if(selectedFilter == WITH_CODEX && d.isHasCodex()){
				//list.add(d);
				this.addDigi(d);
			}else if(selectedFilter == WITHOUT_CODEX && !d.isHasCodex()){
				//list.add(d);
				this.addDigi(d);
			}
		}
		Collections.sort(list);
		this.allList = new ArrayList<DigitalizationTemplate>(list);
		
		long diff = System.currentTimeMillis() - start;
		logger.info("DigitalizationList Generation - Time[ms] = " + diff);	
	}
	
	private void addDigi(DigitalizationTemplate d){
		this.list.add(d);
		if(!suggestionList.contains(d.getEntity().getOwnValue())){
			this.suggestionList.add(d.getEntity().getOwnValue());
		}
	}
	
	public int getListSize(){
		return this.list.size();
	}
	
	public void listenerRefresh(ActionEvent event){
		System.out.println("listenerRefresh");
		reset(getAppBean().getWrapper());
	}
	
	public String actionEditDigi(){
		DigitalizationTemplate digi = (DigitalizationTemplate)getRequestBean("digi");
		if(digi != null){
			getSessionBean().editEntity(digi.getEntity());
			return "entry_edit_entity";
		}
		return null;	
	}
	
	public String actionEditWitness(){
		Entity entity = (Entity)getRequestBean("witness");
		if(entity != null){
			getSessionBean().editEntity(entity);
			return "entry_edit_entity";
		}
		return null;	
	}
	
	public List<DigitalizationTemplate> getList() {
		return list;
	}

	public int getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(int selectedFilter) {
		this.selectedFilter = selectedFilter;
	}
	
	public List<SelectItem> getCodexFilter(){
		return codexFilter;
	}


	public String getFilterTerm() {
		return filterTerm;
	}


	public void setFilterTerm(String filterTerm) {
		this.filterTerm = filterTerm;
	}

	public List<String> getSuggesstionList(){
		return this.suggestionList;
	}
}
