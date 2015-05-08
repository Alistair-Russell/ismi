package de.mpiwg.itgroup.ismi.merge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

public class GeneralMerge extends AbstractISMIBean implements Serializable{
	
	private static Logger logger = Logger.getLogger(GeneralMerge.class);
	
	private static final long serialVersionUID = 1L;
	public static String FIRST_VALUE = "first value";
	public static String SECOND_VALUE = "second value";
	public static String IGNORE = "ignore";
	public static String TAKE = "take";	

	private boolean showAttributeMapping = false;
	private boolean showSrcRelationMapping = false;
	private boolean showTarRelationMapping = false;

	private boolean entitiesLoaded = false;

	private Map<String, String> firstAttMap = new HashMap<String, String>();
	private Map<String, String> secondAttMap = new HashMap<String, String>();

	private Entity firstEntity;
	private Entity secondEntity;
	
	private String firstId;
	private String secondId;
	
	private Entity entResult;
	private List<Attribute> resultAtts;
	private List<Relation> resultSrcRels;
	private List<Relation> resultTarRels;
	
	private List<String> attLabels;
	private Map<String, String> selectedAtts;

	Map<Long, String> selectedFirstSrcRelations;
	Map<Long, String> selectedSecondSrcRelations;
	Map<Long, String> selectedFirstTarRelations;
	Map<Long, String> selectedSecondTarRelations;
	
	public GeneralMerge(){}
	
	public void loadFirstEntity(ActionEvent event){
		reset();
		try{
			Long id = new Long(this.firstId);
			this.firstEntity = getWrapper().getEntityById(id);
			if(this.firstEntity == null){
				addErrorMsg("Entity no found.");
			}
			else{
				if(this.secondEntity != null){
					if(this.firstEntity.getObjectClass().equals(this.secondEntity.getObjectClass())){
						this.deployDifferences();
					}else{
						addErrorMsg("The class of the first entity is: " + firstEntity.getObjectClass() +
						" and class of the second one is: " + secondEntity.getObjectClass() +". They must be equal!");
					}
				}
			}
		}catch(Exception e){
			addErrorMsg("The first entity could no be loaded.");
		}
	}
	
	public void loadSecondEntity(ActionEvent event){
		reset();
		try{
			Long id = new Long(this.secondId);
			this.secondEntity = getWrapper().getEntityById(id);
			if(this.secondEntity == null){
				addErrorMsg("Entity no found.");
			}
			else{
				if(this.firstEntity != null){
					if(this.firstEntity.getObjectClass().equals(this.secondEntity.getObjectClass())){
						this.deployDifferences();
					}else{
						addErrorMsg("The class of the first entity is: " + firstEntity.getObjectClass() +
						" and class of the second one is: " + secondEntity.getObjectClass() +". They must be equal");
					}
					
				}
			}
		}catch(Exception e){
			addErrorMsg( "The second entity could no be loaded.");
		}
	}
	
	@Override
	public void reset(){
		this.attLabels = new ArrayList<String>();
		this.selectedAtts = new HashMap<String, String>();
		this.selectedFirstSrcRelations = new HashMap<Long, String>();
		this.selectedSecondSrcRelations = new HashMap<Long, String>();
		this.selectedFirstTarRelations = new HashMap<Long, String>();
		this.selectedSecondTarRelations = new HashMap<Long, String>();
		this.entResult = null;
		
		this.entitiesLoaded = false;
		this.showAttributeMapping = false;
		this.showSrcRelationMapping = false;
		this.showTarRelationMapping = false;
	}

	public void listenerExecuteMerge(){
		this.executeMerge();
		getAppBean().getSimpleSearchCache().setMapDirty(true);
	}
	
	private void deployDifferences(){
		this.showAttributeMapping = true;
		this.showSrcRelationMapping = true;
		this.showTarRelationMapping = true;
		this.entitiesLoaded = true;
		if(this.firstEntity != null && this.secondEntity != null){
			if(firstEntity.isLightweight()){
				this.firstEntity = getWrapper().getEntityContent(this.firstEntity);
			}
			if(secondEntity.isLightweight()){
				this.secondEntity = getWrapper().getEntityContent(this.secondEntity);
			}			
			
			this.firstEntity = (Entity)firstEntity.clone();
			this.secondEntity = (Entity) secondEntity.clone();
			
			//attributes
			this.attLabels = new ArrayList<String>();
			this.selectedAtts = new HashMap<String, String>();
			this.firstAttMap = new HashMap<String, String>();
			this.secondAttMap = new HashMap<String, String>();
			
			for(Attribute att : this.firstEntity.getAttributes()){
				firstAttMap.put(att.getName(), att.getValue());
				if(!attLabels.contains(att.getName())){
					attLabels.add(att.getName());
					selectedAtts.put(att.getName(), FIRST_VALUE);
				}
			}
			
			for(Attribute att : this.secondEntity.getAttributes()){
				secondAttMap.put(att.getName(), att.getValue());
				if(!attLabels.contains(att.getName())){
					attLabels.add(att.getName());
					selectedAtts.put(att.getName(), FIRST_VALUE);
				}
			}			
			
			//source relations
			this.selectedFirstSrcRelations = new HashMap<Long, String>();
			this.selectedSecondSrcRelations = new HashMap<Long, String>();
			
			for(Relation rel : this.firstEntity.getSourceRelations()){
				rel.setTarget(getWrapper().getEntityById(rel.getTargetId()));
				selectedFirstSrcRelations.put(rel.getId(), TAKE);
			}
			
			
			for(Relation rel : this.secondEntity.getSourceRelations()){
				rel.setTarget(getWrapper().getEntityById(rel.getTargetId()));
				selectedSecondSrcRelations.put(rel.getId(), TAKE);
			}
			
			//target relations
			this.selectedFirstTarRelations = new HashMap<Long, String>();
			this.selectedSecondTarRelations = new HashMap<Long, String>();
			
			for(Relation rel : this.firstEntity.getTargetRelations()){
				rel.setSource(getWrapper().getEntityById(rel.getSourceId()));
				selectedFirstTarRelations.put(rel.getId(), TAKE);
			}
			
			for(Relation rel : this.secondEntity.getTargetRelations()){
				rel.setSource(getWrapper().getEntityById(rel.getSourceId()));
				selectedSecondTarRelations.put(rel.getId(), TAKE);
			}
			
			
		}
	}
	
	public void preview(ActionEvent event){
		this.generateResultEntity();
	}
	
	private void executeMerge(){
		
		logger.info("Starting merge execution " + firstEntity.getObjectClass() 
				+ " ["+ getUserName() +"]"
				+ "[firstEntity=" + firstEntity.getId() 
				+ ", secondEntity=" + secondEntity.getId() + "]");
		
		try {
			this.generateResultEntity();
			if(this.entResult != null){
				
				this.printMergeInfo(entResult);
				
				this.getWrapper().saveEntity(this.entResult, getSessionUser().getEmail() + "_merge");
				
				this.getWrapper().removeCurrentVersionEntity(this.firstEntity);
				this.getWrapper().removeCurrentVersionEntity(this.secondEntity);
				
				//the old relations should be removed, before...
				this.generateSecundaryOW(this.entResult, getSessionUser().getEmail() + "_merge");
				
				this.printMergeInfo(entResult);
				
				logger.info("Merge execution 'successful' "  + 
						firstEntity.getObjectClass() + 
						" ["+ getUserName() +"]"
						+ "[firstEntity=" + firstEntity.getId() 
						+ ", secondEntity=" + secondEntity.getId() 
						+ ", generatedEntity=" + entResult.getId() + "]");
				
				this.firstEntity = null;
				this.secondEntity = null;
				
				addGeneralMsg("The entities were merged successfully");
				addGeneralMsg("The new entity has the id " + this.entResult.getId());
				this.reset();
			}	
		} catch (Exception e) {
			printInternalError(e);
			logger.error("["+ getUserName() +"] " + e.getMessage(), e);			
		}
	}
	
	private void printMergeInfo(Entity ent){
		StringBuilder sb = new StringBuilder("\n\n");
		
		sb.append("-------------------------------------------");
		sb.append("-----------------------------------------\n");
		sb.append("Merging result [" + getUserName() + "]\n");
		sb.append(ent.toString() + "\n");
		sb.append("Attributes:\n");
		for(Attribute att : ent.getAttributes()){
			sb.append("\t" + att.toString() + "\n");
		}
		
		sb.append("Src Relations:\n");
		for(Relation src : ent.getSourceRelations()){
			sb.append("\t" + src.toString() + "\n");
		}
		
		sb.append("Tar Relations:\n");
		for(Relation tar : ent.getTargetRelations()){
			sb.append("\t" + tar.toString() + "\n");
		}
		
		sb.append("-------------------------------------------");
		sb.append("-----------------------------------------\n");
		logger.info(sb.toString());
	}
	
	public void actionShowTarRelationMapping(ActionEvent event){
		this.showTarRelationMapping = true;
	}
	
	public void actionHideTarRelationMapping(ActionEvent event){
		this.showTarRelationMapping = false;
	}
	
	public void actionShowSrcRelationMapping(ActionEvent event){
		this.showSrcRelationMapping = true;
	}
	
	public void actionHideSrcRelationMapping(ActionEvent event){
		this.showSrcRelationMapping = false;
	}
	
	public void actionShowAttributeMapping(ActionEvent event){
		this.showAttributeMapping = true;
	}
	
	public void actionHideAttributeMapping(ActionEvent event){
		this.showAttributeMapping = false;
	}
	
	private void generateResultEntity(){
		this.entResult = new Entity();
		this.entResult.setLightweight(false);
		this.entResult.setObjectClass(this.firstEntity.getObjectClass());
		
		//generating attributes
		try{
			for(String attName : this.selectedAtts.keySet()){
				String selected = this.selectedAtts.get(attName);
				String value = "";
				if(selected.equals(FIRST_VALUE)){
					value = (firstEntity.getAttributeByName(attName) == null) ? "" : firstEntity.getAttributeByName(attName).getOwnValue();
				}else if(selected.equals(SECOND_VALUE)){
					value = (secondEntity.getAttributeByName(attName) == null) ? "" : secondEntity.getAttributeByName(attName).getOwnValue();
				}
				this.entResult.addAttribute(new Attribute(attName, "text", value));
			}
		}catch(Exception e){
			e.printStackTrace();
			addErrorMsg("Please inform support of this exception: " + e.getMessage());
		}

		
		//generating source relations
		for(Relation rel : firstEntity.getSourceRelations()){
			String selectedValue = this.selectedFirstSrcRelations.get(rel.getId());
			if(StringUtils.isNotEmpty(selectedValue) && selectedValue.equals(TAKE)){
				if(!this.entResult.containsSourceRelation(rel.getOwnValue(), rel.getTargetId())){
					this.entResult.addSourceRelation(generateSrcRelation(rel));	
				}
			}
		}
		
		for(Relation rel : secondEntity.getSourceRelations()){
			String selectedValue = this.selectedSecondSrcRelations.get(rel.getId());
			if(StringUtils.isNotEmpty(selectedValue) && selectedValue.equals(TAKE)){
				if(!this.entResult.containsSourceRelation(rel.getOwnValue(), rel.getTargetId())){
					this.entResult.addSourceRelation(generateSrcRelation(rel));
				}
			}
		}
		
		//generating target relations
		for(Relation rel : firstEntity.getTargetRelations()){
			String selectedValue = this.selectedFirstTarRelations.get(rel.getId());
			if(StringUtils.isNotEmpty(selectedValue) && selectedValue.equals(TAKE)){
				//ensuring that there is no two equals relations.
				if(!this.entResult.containsTargetRelation(rel.getOwnValue(), rel.getSourceId())){
					this.entResult.addTargetRelation(generateTarRelation(rel));
				}
			}
		}
		
		for(Relation rel : secondEntity.getTargetRelations()){
			String selectedValue = this.selectedSecondTarRelations.get(rel.getId());
			if(StringUtils.isNotEmpty(selectedValue) && selectedValue.equals(TAKE)){
				if(!this.entResult.containsTargetRelation(rel.getOwnValue(), rel.getSourceId())){
					this.entResult.addTargetRelation(generateTarRelation(rel));
				}
			}
		}
	}
	
	private Relation generateSrcRelation(Relation rel){
		
		Relation newRel = new Relation();
		newRel.setOwnValue(rel.getOwnValue());
		newRel.setTarget(getWrapper().getEntityById(rel.getTargetId()));
		
		return newRel;
	}
	
	private Relation generateTarRelation(Relation rel){
		Relation newRel = new Relation();
		newRel.setOwnValue(rel.getOwnValue());
		newRel.setSource(getWrapper().getEntityById(rel.getSourceId()));
		return newRel;
	}

    public List<SelectItem> getAttSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(FIRST_VALUE));
        items.add(new SelectItem(SECOND_VALUE));
        items.add(new SelectItem(IGNORE));
        return items;
    }
    
    public List<SelectItem> getRelSelectItems(){
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(TAKE));
        items.add(new SelectItem(IGNORE));
        return items;
    }
    
	public Entity getEntResult() {
		return entResult;
	}

	public void setEntResult(Entity entResult) {
		this.entResult = entResult;
	}

	public List<Attribute> getResultAtts() {
		return resultAtts;
	}

	public void setResultAtts(List<Attribute> resultAtts) {
		this.resultAtts = resultAtts;
	}

	public List<Relation> getResultSrcRels() {
		return resultSrcRels;
	}

	public void setResultSrcRels(List<Relation> resultSrcRels) {
		this.resultSrcRels = resultSrcRels;
	}

	public List<Relation> getResultTarRels() {
		return resultTarRels;
	}

	public void setResultTarRels(List<Relation> resultTarRels) {
		this.resultTarRels = resultTarRels;
	}
	
	public Entity getFirstEntity() {
		return firstEntity;
	}

	public void setFirstEntity(Entity firstEntity) {
		this.firstEntity = firstEntity;
	}

	public Entity getSecondEntity() {
		return secondEntity;
	}

	public void setSecondEntity(Entity secondEntity) {
		this.secondEntity = secondEntity;
	}

	public String getFirstId() {
		return firstId;
	}

	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}

	public String getSecondId() {
		return secondId;
	}

	public void setSecondId(String secondId) {
		this.secondId = secondId;
	}
	
	public List<String> getAttLabels() {
		return attLabels;
	}

	public void setAttLabels(List<String> attLabels) {
		this.attLabels = attLabels;
	}
	
	public Map<String, String> getFirstAttMap() {
		return firstAttMap;
	}

	public void setFirstAttMap(Map<String, String> firstAttMap) {
		this.firstAttMap = firstAttMap;
	}

	public Map<String, String> getSecondAttMap() {
		return secondAttMap;
	}

	public void setSecondAttMap(Map<String, String> secondAttMap) {
		this.secondAttMap = secondAttMap;
	}

	public Map<String, String> getSelectedAtts() {
		return selectedAtts;
	}

	public void setSelectedAtts(Map<String, String> selectedAtts) {
		this.selectedAtts = selectedAtts;
	}
	
	public boolean isShowAttributeMapping() {
		return showAttributeMapping;
	}

	public void setShowAttributeMapping(boolean showAttributeMapping) {
		this.showAttributeMapping = showAttributeMapping;
	}

	public boolean isEntitiesLoaded() {
		return entitiesLoaded;
	}

	public void setEntitiesLoaded(boolean entitiesLoaded) {
		this.entitiesLoaded = entitiesLoaded;
	}
	
	public Map<Long, String> getSelectedFirstSrcRelations() {
		return selectedFirstSrcRelations;
	}

	public void setSelectedFirstSrcRelations(
			Map<Long, String> selectedFirstSrcRelations) {
		this.selectedFirstSrcRelations = selectedFirstSrcRelations;
	}

	public Map<Long, String> getSelectedSecondSrcRelations() {
		return selectedSecondSrcRelations;
	}

	public void setSelectedSecondSrcRelations(
			Map<Long, String> selectedSecondSrcRelations) {
		this.selectedSecondSrcRelations = selectedSecondSrcRelations;
	}
	public boolean isShowSrcRelationMapping() {
		return showSrcRelationMapping;
	}

	public void setShowSrcRelationMapping(boolean showSrcRelationMapping) {
		this.showSrcRelationMapping = showSrcRelationMapping;
	}


	public boolean isShowTarRelationMapping() {
		return showTarRelationMapping;
	}

	public void setShowTarRelationMapping(boolean showTarRelationMapping) {
		this.showTarRelationMapping = showTarRelationMapping;
	}

	public Map<Long, String> getSelectedFirstTarRelations() {
		return selectedFirstTarRelations;
	}

	public void setSelectedFirstTarRelations(
			Map<Long, String> selectedFirstTarRelations) {
		this.selectedFirstTarRelations = selectedFirstTarRelations;
	}

	public Map<Long, String> getSelectedSecondTarRelations() {
		return selectedSecondTarRelations;
	}

	public void setSelectedSecondTarRelations(
			Map<Long, String> selectedSecondTarRelations) {
		this.selectedSecondTarRelations = selectedSecondTarRelations;
	}
	/*
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isShowMsgPopup() {
		return showMsgPopup;
	}

	public void setShowMsgPopup(boolean showMsgPopup) {
		this.showMsgPopup = showMsgPopup;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

	public boolean isShowConfirmPopup() {
		return showConfirmPopup;
	}

	public void setShowConfirmPopup(boolean showConfirmPopup) {
		this.showConfirmPopup = showConfirmPopup;
	}
	*/
}