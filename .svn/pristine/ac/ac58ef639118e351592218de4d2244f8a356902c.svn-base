package de.mpiwg.itgroup.ismi.browse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.services.utils.AttributeFilter;

import de.mpiwg.itgroup.ismi.util.guiComponents.DataPaginator;

public class AbstractEntityRepositoryBean extends AbstractBrowse {

	private static final long serialVersionUID = 3154642100627969159L;

	private static Logger logger = Logger.getLogger(AbstractEntityRepositoryBean.class);
	
	public static String MODE_ADVANCED = "advanced";
	public static String MODE_ALL = "all";
	public static String MODE_NONE = "none";
	

	private String objectClass = null;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> currentEntities = new ArrayList<Entity>();

	private List<SelectItem> definitions = new ArrayList<SelectItem>();
	
	private transient DataPaginator advancedPaginator = new DataPaginator();

	private String ocTerm;
	private String currentTab;
	
	private String textFullTitle;
	private String textFullTitleTranslit;
	private String textShortTitle;

	private String personName;
	private String personNameTranslit;

	private String codexIdentifier;
	
	private String collectionName;

	private String placeName;
	
	private String aliasAlias;
	
	private String repositoryName;
	
	private String witnessFullTitle;
	private String witnessFullTitleTranslit;
	private String witnessAhlwardtNo;

	private boolean advancedSearch = false;
	private String resultMode = MODE_NONE;
	private String resultSummaryMsg;
	
	private String subjectType;
	private List<SelectItem> suggestedSubjectTypes = new ArrayList<SelectItem>();

	private String page;
	private String pageMsg;
	
	public static String main_subject = "main_subject";
	public static String sub_subject = "sub_subject";
	
	public AbstractEntityRepositoryBean(){
		suggestedSubjectTypes.add(new SelectItem(null, "-- choose --"));
		suggestedSubjectTypes.add(new SelectItem(main_subject, main_subject));
		suggestedSubjectTypes.add(new SelectItem(sub_subject, sub_subject));
		this.updateDefinitions(getWrapper().getLWDefinitions());		
	}
	
	private void updateAdvancedEntities() {
		if (StringUtils.isNotEmpty(getObjectClass())) {
			this.advancedPaginator.initCount();
			int startRecord = this.advancedPaginator.getCurrentPage()
					* this.advancedPaginator.getItemsPerPage();
			if((this.advancedPaginator.getCurrentPage() + 1) == this.advancedPaginator.getNumberOfPages()){
				int mod = this.entities.size() % advancedPaginator.getItemsPerPage();
				if(mod == 0){
					this.currentEntities = entities.subList(startRecord, startRecord + this.advancedPaginator.getItemsPerPage());
				}else{
					this.currentEntities = entities.subList(startRecord, startRecord + mod);	
				}
				
			}else{
				this.currentEntities = entities.subList(startRecord, startRecord + this.advancedPaginator.getItemsPerPage());	
			}
			
		} else
			this.currentEntities = new ArrayList<Entity>();
	}
	
	public String advancedFirst() {
		this.advancedPaginator.first();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String advancedLast() {
		this.advancedPaginator.last();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String advancedFastForward() {
		this.advancedPaginator.fastForward();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String advancedFastRewind() {
		this.advancedPaginator.fastRewind();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String advancedPrevious() {
		this.advancedPaginator.previous();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String advancedNext() {
		this.advancedPaginator.next();
		this.updateAdvancedEntities();
		return GOTO_ENTITY_REPOSITORY;
	}
	
	public void reset(){
		this.entities = new ArrayList<Entity>();
		this.currentEntities = new ArrayList<Entity>();
		this.resultSummaryMsg = new String();
	}
	
	public void actionGoToPageAdvancedResult(ActionEvent event){
    	this.pageMsg = "";
    	try{
    		Integer page = new Integer(getPage());
    		this.advancedPaginator.goToPage(page - 1);
    		this.updateAdvancedEntities();
    	}catch(Exception e){
    		this.pageMsg = "page is invalid!";
    	}
    }
	
	public DataPaginator getAdvancedPaginator() {
		return advancedPaginator;
	}

	public void setAdvancedPaginator(DataPaginator advancedPaginator) {
		this.advancedPaginator = advancedPaginator;
	}
	
	public void updateDefinitions(List<Entity> defs) {
		this.definitions = new ArrayList<SelectItem>();
		SelectItem option = null;
		for (Entity def : defs) {
			if (def.getOwnValue() != null) {
				option = new SelectItem(def.getOwnValue());
				this.definitions.add(option);
			}
		}
		this.setEntities(new ArrayList<Entity>());
		if (defs.size() > 0) {
			this.objectClass = defs.get(0).getOwnValue();
		}
	}

	public void actionDisplayAdvancedSearch(ActionEvent event) {
		this.advancedSearch = true;
	}

	public void actionHideAdvancedSearch(ActionEvent event) {
		this.advancedSearch = false;
	}

	public void actionChangeDefinition(ValueChangeEvent event) {
		this.objectClass = (String) event.getNewValue();
	}
	
	public void searchByOwnvalue(){
		this.resultMode = MODE_ADVANCED;
		this.setPage("");
		this.currentEntities = new ArrayList<Entity>();
		this.entities = getWrapper().searchEntityByOwnValue(objectClass, ocTerm);
		this.resultSummaryMsg = "";
		
		if(this.entities.size() > 0){
			
			this.advancedPaginator.setCurrentPage(0);
			int entitiesCount = this.entities.size();
			this.advancedPaginator.resetNumberOfPages(entitiesCount);
			this.updateAdvancedEntities();	
		}else {
			this.resultSummaryMsg = "No items were found!";
		}
	}
	

	public void searchByAttributes() throws Exception{
		this.resultMode = MODE_ADVANCED;
		this.setPage("");
		this.entities = new ArrayList<Entity>();
		this.currentEntities = new ArrayList<Entity>();
		
		List<AttributeFilter> filterList = new ArrayList<AttributeFilter>();

		this.resultSummaryMsg = "";
		Map<Entity, Attribute> resultMap = new HashMap<Entity, Attribute>();
		if (this.objectClass.equals(TEXT)) {
			if (StringUtils.isNotEmpty(textFullTitle)) {
				filterList.add(new AttributeFilter("full_title",
						this.textFullTitle, TEXT));
			}
			if (StringUtils.isNotEmpty(textFullTitleTranslit)) {
				filterList.add(new AttributeFilter("full_title_translit",
						textFullTitleTranslit, TEXT));
			}
			if (StringUtils.isNotEmpty(textShortTitle)) {
				List<Entity> l = getWrapper().searchEntityByAttributeOfTarRelation(TEXT, "is_prime_alias_title_of", ALIAS, "alias", textShortTitle, 20);
				for(Entity e : l){
					this.entities.add(e);
				}
			}
		} else if (this.objectClass.equals(WITNESS)) {
			//witnessAhlwardtNo
			if(StringUtils.isNotEmpty(this.witnessAhlwardtNo)){
				filterList.add(new AttributeFilter("ahlwardt_no", this.witnessAhlwardtNo,
				WITNESS));
			}
		} else if (this.objectClass.equals(SUBJECT)) {
			if(StringUtils.isNotEmpty(this.subjectType)){
				filterList.add(new AttributeFilter("type", this.subjectType,
				SUBJECT));
			}
		} else if (this.objectClass.equals(ALIAS)) {
			if(StringUtils.isNotEmpty(this.aliasAlias)){
				filterList.add(new AttributeFilter("alias", this.aliasAlias,
				ALIAS));
			}
		} else if (this.objectClass.equals(COLLECTION)) {
			if(StringUtils.isNotEmpty(this.collectionName)){
				filterList.add(new AttributeFilter("name", this.collectionName,
				COLLECTION));
			}
		} else if (this.objectClass.equals(REPOSITORY)) {
			if(StringUtils.isNotEmpty(this.repositoryName)){
				filterList.add(new AttributeFilter("name", this.repositoryName,
				REPOSITORY));
			}
		} else if (this.objectClass.equals(PLACE)) {
			if(StringUtils.isNotEmpty(this.placeName)){
				filterList.add(new AttributeFilter("name", this.placeName,
				PLACE));
			}
		} else if (this.objectClass.equals(CODEX)) {
			if(StringUtils.isNotEmpty(this.codexIdentifier)){
				filterList.add(new AttributeFilter("identifier", this.codexIdentifier,
				CODEX));
			}
			
		} else if (this.objectClass.equals(PERSON)) {
			if (StringUtils.isNotEmpty(this.personName)) {
				filterList.add(new AttributeFilter("name", this.personName,
						PERSON));
			}
			if (StringUtils.isNotEmpty(this.personNameTranslit)) {
				filterList.add(new AttributeFilter("name_translit",
						this.personNameTranslit, PERSON));
			}
		}
		if (filterList.size() > 0) {
			resultMap = getWrapper().searchEntityByAttributeFilter(
					filterList, 500);
			for (Entity ent : resultMap.keySet()) {
				this.entities.add(ent);
			}
			Collections.sort(entities);
		}

		if (resultMap.size() > 0) {
			this.resultSummaryMsg = resultMap.size() + " items were found!";
			this.advancedPaginator.setCurrentPage(0);
			int entitiesCount = this.entities.size();
			this.advancedPaginator.resetNumberOfPages(entitiesCount);
			this.updateAdvancedEntities();
		} else {
			this.resultSummaryMsg = "No items were found!";
		}
	}
	
	public boolean isRenderedSearch(){
		if(StringUtils.isNotEmpty(this.objectClass) && (
				objectClass.equals(SUBJECT) ||
				objectClass.equals(REPOSITORY) ||
				objectClass.equals(ALIAS) || 
				objectClass.equals(COLLECTION) ||
				objectClass.equals(TEXT) ||
				objectClass.equals(PERSON) ||
				objectClass.equals(CODEX) ||
				objectClass.equals(WITNESS) ||
				objectClass.equals(PLACE))){
			return true;
		}
		return false;
	}
	
	public String actionSearchByAttributes() {
		try {
			this.searchByAttributes();	
		} catch (Exception e) {
			printInternalError(e);
			logger.error(e.getMessage(), e);
		}
		
		return GOTO_ENTITY_REPOSITORY;
	}
	
	public String actionSearchByOwnvalue(){
		try {
			this.searchByOwnvalue();	
		} catch (Exception e) {
			printInternalError(e);
			logger.error(e.getMessage(), e);
		}
		
		return GOTO_ENTITY_REPOSITORY;
	}

	public boolean isAdvancedSearch() {
		return advancedSearch;
	}

	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}

	public String getTextFullTitle() {
		return textFullTitle;
	}

	public void setTextFullTitle(String textFullTitle) {
		this.textFullTitle = textFullTitle;
	}

	public String getTextFullTitleTranslit() {
		return textFullTitleTranslit;
	}

	public void setTextFullTitleTranslit(String textFullTitleTranslit) {
		this.textFullTitleTranslit = textFullTitleTranslit;
	}

	public String getTextShortTitle() {
		return textShortTitle;
	}

	public void setTextShortTitle(String textShortTitle) {
		this.textShortTitle = textShortTitle;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonNameTranslit() {
		return personNameTranslit;
	}

	public void setPersonNameTranslit(String personNameTranslit) {
		this.personNameTranslit = personNameTranslit;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public List<Entity> getEntities() {
		return this.entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<SelectItem> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<SelectItem> definitions) {
		this.definitions = definitions;
	}

	public String getResultMode() {
		return resultMode;
	}

	public void setResultMode(String resultMode) {
		this.resultMode = resultMode;
	}

	public String getResultSummaryMsg() {
		return resultSummaryMsg;
	}

	public void setResultSummaryMsg(String resultSummaryMsg) {
		this.resultSummaryMsg = resultSummaryMsg;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageMsg() {
		return pageMsg;
	}

	public void setPageMsg(String pageMsg) {
		this.pageMsg = pageMsg;
	}
	public String getCodexIdentifier() {
		return codexIdentifier;
	}
	public void setCodexIdentifier(String codexIdentifier) {
		this.codexIdentifier = codexIdentifier;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getAliasAlias() {
		return aliasAlias;
	}

	public void setAliasAlias(String aliasAlias) {
		this.aliasAlias = aliasAlias;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
    
    public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public List<SelectItem> getSuggestedSubjectTypes() {
		return suggestedSubjectTypes;
	}

	public void setSuggestedSubjectTypes(List<SelectItem> suggestedTypes) {
		this.suggestedSubjectTypes = suggestedTypes;
	}
	public List<Entity> getCurrentEntities() {
		return currentEntities;
	}

	public void setCurrentEntities(List<Entity> currentEntities) {
		this.currentEntities = currentEntities;
	}

	public String getWitnessFullTitle() {
		return witnessFullTitle;
	}

	public void setWitnessFullTitle(String witnessFullTitle) {
		this.witnessFullTitle = witnessFullTitle;
	}

	public String getWitnessFullTitleTranslit() {
		return witnessFullTitleTranslit;
	}

	public void setWitnessFullTitleTranslit(String witnessFullTitleTranslit) {
		this.witnessFullTitleTranslit = witnessFullTitleTranslit;
	}

	public String getWitnessAhlwardtNo() {
		return witnessAhlwardtNo;
	}

	public void setWitnessAhlwardtNo(String witnessAhlwardtNo) {
		this.witnessAhlwardtNo = witnessAhlwardtNo;
	}

	public String getOcTerm() {
		return ocTerm;
	}

	public void setOcTerm(String ocTerm) {
		this.ocTerm = ocTerm;
	}

	public String getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}
	
	
}
