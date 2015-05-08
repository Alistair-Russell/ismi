package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.security.SecurityService;
import org.mpi.openmind.security.bo.User;
import org.mpiwg.itgroup.escidoc.utils.ESciDocItemDataTable;
import org.mpiwg.itgroup.escidoc.web.ESciDocItemForm;
import org.mpiwg.itgroup.geonames.GeonameForm;
import org.richfaces.event.ItemChangeEvent;

import de.mpiwg.itgroup.ismi.admin.AdminBean;
import de.mpiwg.itgroup.ismi.browse.EntityDetailsBean;
import de.mpiwg.itgroup.ismi.defs.DefinitionForm;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean.CheckResults;
import de.mpiwg.itgroup.ismi.event.beans.CopyEvent;
import de.mpiwg.itgroup.ismi.event.beans.StudyEvent;
import de.mpiwg.itgroup.ismi.event.beans.TransferEvent;
import de.mpiwg.itgroup.ismi.merge.GeneralMerge;
import de.mpiwg.itgroup.ismi.publicView.DynamicPageEditor;
import de.mpiwg.itgroup.ismi.publicView.PublicCodexBean;
import de.mpiwg.itgroup.ismi.publicView.PublicCodexList;
import de.mpiwg.itgroup.ismi.publicView.PublicCodexView;
import de.mpiwg.itgroup.ismi.publicView.pages.DynamicPage;
import de.mpiwg.itgroup.ismi.search.beans.AdvancedSearchBean;
import de.mpiwg.itgroup.ismi.search.beans.DisplayAuthorBean;
import de.mpiwg.itgroup.ismi.search.beans.DisplayTitleBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.util.guiComponents.EntityList;

public class SessionBean extends AbstractBean implements Serializable{
	
	private static Logger logger = Logger.getLogger(SessionBean.class);
	
	private DefinitionForm defForm = new DefinitionForm();
	private StudyEvent studyEventForm = new StudyEvent();
	private CopyEvent copyEventForm = new CopyEvent();
	private TransferEvent transferEventForm = new TransferEvent();
	private transient AdvancedSearchBean advancedSearch = new AdvancedSearchBean();
	private GeonameForm geoForm = new GeonameForm();
	
	private EntityDetailsBean entDetailsForm = new EntityDetailsBean();
	
	
	private DisplayAuthorBean displayAuthor = new DisplayAuthorBean();
	private DisplayTitleBean displayTitle = new DisplayTitleBean();
	
	private ESciDocItemForm eSciDocForm = new ESciDocItemForm();
	
	private ESciDocItemDataTable refDataTable = new ESciDocItemDataTable(null);
	
	private DynamicPageEditor pageEditor = null;
	

	private Calendar currentCalendar = null;
	private EntityList currentEntListForCalendar = null;
	private Long currentEntIdForCalendar;
	
	private boolean displayLoginDialog = false;
	
	private DigiListBean digiList;
	
	//public
	private DynamicPage dynamicPage;
	private PublicCodexView publicCodexView;
	private PublicCodexBean publicCodexBean;
	
	public void editCalendar(Calendar cal, EntityList list, Long entId){
		this.currentCalendar = cal;
		this.currentEntListForCalendar = list;
		this.currentEntIdForCalendar = entId;
	}
	
	public void listenerSaveEditionCalendar(ActionEvent event){
		if(this.currentEntListForCalendar != null && this.currentEntIdForCalendar != null){
			this.currentEntListForCalendar.getCalendarMap().put(currentEntIdForCalendar, currentCalendar);
			this.currentCalendar = null;
			this.currentEntListForCalendar = null;
			this.currentEntIdForCalendar = null;
		}
	}
	
	
	public static String PAGE_ENTITY_REPOSITORY = "entity_repository";
	public static String PAGE_ENTITY_DETAILS = "entity_details";
	public static String PAGE_ENTRY = "entry_edit_entity";
	public static String PAGE_EVENT_FORM = "event_form";
	public static String PAGE_SIMPLE_SEARCH = "simple_search";
	public static String PAGE_PUBLIC_CODICES = "public_codices";
	public static String PAGE_DISPLAY_TITLE = "display_title";
	public static String PAGE_DISPLAY_AUTHOR = "display_author";
	public static String PAGE_GENERAL_MERGE = "general_merge";
	public static String PAGE_GEO_FORM = "geo_form";
	public static String PAGE_ADMIN = "admin";
	public static String PAGE_PROFILE = "profile";
	public static String PAGE_DEF_EDITOR = "defEditor";
	
	private User user = null;
	private String username = "";
	private String password = "";

	//private Entity currentEntity;

	private GeneralMerge generalMerge;

	//private String last_action;
	//private Date time_of_lastAction;
	
	private String remoteAddr;

	private static final long serialVersionUID = 1L;
	
	//Entities tabs
	public static String WITNESS_TAB = "Witness";
	public static String CODEX_TAB = "Codex";
	public static String COLLECTION_TAB = "Collection";
	public static String REPOSITORY_TAB = "Repository";
	public static String CITY_TAB = "Place";
	public static String TEXT_TAB = "Text";
	public static String PERSON_TAB = "Person";
	public static String ALIAS_TAB = "Alias";
	public static String SUBJECT_TAB = "Subject";
	public static String ROLE_TAB = "Role";
	public static String DIGI_TAB = "Digi";

	//Events tabs
	public static String WITNESS_STUDY_EVENT_TAB = "Study";
	public static String COPY_EVENT_TAB = "Copy";
	public static String TRANSFER_EVENT_TAB = "Transfer";
	
	private String selectedTab = WITNESS_TAB;
	private String selectedEventTab = WITNESS_STUDY_EVENT_TAB;
	private String selectedAdvancedSearchTab = "01";
	

	private Long editFormCurrentEntId;

	private boolean displayError = false;
	private ArrayList<String> errorMessages;

	public String getOCByTabIndex(){
		if(selectedTab != null){
			if(selectedTab.equals(WITNESS_TAB)){
				return AbstractISMIBean.WITNESS;
			}else if(selectedTab.equals(CODEX_TAB)){
				return AbstractISMIBean.CODEX;
			}else if(selectedTab.equals(COLLECTION_TAB)){
				return AbstractISMIBean.COLLECTION;
			}else if(selectedTab.equals(REPOSITORY_TAB)){
				return AbstractISMIBean.REPOSITORY;
			}else if(selectedTab.equals(CITY_TAB)){
				return AbstractISMIBean.PLACE;
			}else if(selectedTab.equals(TEXT_TAB)){
				return AbstractISMIBean.TEXT;
			}else if(selectedTab.equals(PERSON_TAB)){
				return AbstractISMIBean.PERSON;
			}else if(selectedTab.equals(ALIAS_TAB)){
				return AbstractISMIBean.ALIAS;
			}else if(selectedTab.equals(SUBJECT_TAB)){
				return AbstractISMIBean.SUBJECT;
			}else if(selectedTab.equals(ROLE_TAB)){
				return AbstractISMIBean.ROLE;
			}else if(selectedTab.equals(DIGI_TAB)){
				return AbstractISMIBean.DIGITALIZATION;
			}
		}
		return null;
	}
	
	public void listenerOpenESciDocForm(ActionEvent event){
		this.refDataTable.open();
	}

	//************************************
	//************************************
	//************************************
	public SessionBean() {
		if(StringUtils.isBlank(this.remoteAddr)){
			this.getRemoteAddr();
		}
		logger.info(" [remoteAddr=" + remoteAddr + "]");
	}
	
	private void autoLogin(){
		/*
		this.username = "jurzua@mpiwg-berlin.mpg.de";
		this.password = "221082";
		this.login();
			*/
	}

	public void getRemoteAddr(){
		try{
			HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
			this.remoteAddr = httpServletRequest.getRemoteAddr();
		}catch (Exception ex) {}
	}


	public void listenerLogin(ActionEvent event) {
		this.login();
	}
	
	public String actionLogin(){
		this.login();
		return new String();
	}
	
	public void listenerDisplayLoginDialog(ActionEvent event) {
		if(user == null){
			this.displayLoginDialog = true;
		}else{
			this.displayLoginDialog = false;
		}
	}
	
	public String actionLoginCancel(){
		this.displayLoginDialog = false;
		return PAGE_SIMPLE_SEARCH;
	}

	private void login(){
		user = null;
		// this.loginMsg = "";

		if (StringUtils.isNotEmpty(username)
				&& StringUtils.isNotEmpty(password)) {
			user = getSecurityService().getUserByPassword(username, password);
		}

		if (user == null) {
			addErrorMsg("The system does not find the account!");
			this.displayLoginDialog = false;
		} else {
			if(StringUtils.isBlank(this.remoteAddr)){
				this.getRemoteAddr();
			}
			logger.info("login " + username + " [remoteAddr=" + remoteAddr + "]");
			this.setUser(user);
			this.generalMerge = new GeneralMerge();
			this.defForm = new DefinitionForm();
			// refresh the editor of Dirk
			addSessionBean("CurrentWitness", new CurrentWitnessBean());
			addSessionBean("CurrentCodex", new CurrentCodexBean());
			addSessionBean("CurrentText", new CurrentTextBean());
			addSessionBean("CurrentPerson", new CurrentPersonBean());
			addSessionBean("CurrentAlias", new CurrentAliasBean());
			addSessionBean("CurrentCollection", new CurrentCollectionBean());
			addSessionBean("CurrentRepository", new CurrentRepositoryBean());
			addSessionBean("CurrentCity", new CurrentCityBean());
			addSessionBean("CurrentDigi", new CurrentDigitalizationBean());
			editFormCurrentEntId = null;
			this.displayLoginDialog = false;
		}
	}
	
	public String logout() {
		if(StringUtils.isBlank(this.remoteAddr)){
			this.getRemoteAddr();
		}
		logger.info("logout " + username + " [remoteAddr=" + remoteAddr + "]");
		this.username = "";
		this.password = "";
		this.user = null;
		this.generalMerge = null;
		this.defForm = null;
		return PAGE_PUBLIC_CODICES;
	}

	public boolean isCanDelete() {
		return isAdmin();
	}

	public boolean isCanMerge() {
		if (getUser() != null) {
			String role = getUser().getRole();
			if (StringUtils.isNotEmpty(role)) {
				if (AdminBean.Administrator.equals(role)
						|| AdminBean.Researcher.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCanCreate() {
		if (getUser() != null) {
			String role = getUser().getRole();
			if (StringUtils.isNotEmpty(role)) {
				if (AdminBean.Administrator.equals(role)
						|| AdminBean.Researcher.equals(role)
						|| AdminBean.Student.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCanEdit() {
		if (getUser() != null) {
			String role = getUser().getRole();
			if (StringUtils.isNotEmpty(role)) {
				if (AdminBean.Administrator.equals(role)
						|| AdminBean.Researcher.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAdmin() {
		if (getUser() != null) {
			String role = getUser().getRole();
			if (StringUtils.isNotEmpty(role)) {
				if (AdminBean.Administrator.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<String> generalMsgList = new ArrayList<String>();
	private List<String> errorMsgList = new ArrayList<String>();

	public List<String> getErrorMsgList() {
		return errorMsgList;
	}

	public void setErrorMsgList(List<String> errorMsgList) {
		this.errorMsgList = errorMsgList;
	}

	public List<String> getGeneralMsgList() {
		return generalMsgList;
	}

	public void setGeneralMsgList(List<String> generalMsgList) {
		this.generalMsgList = generalMsgList;
	}

	public void addErrorMsg(String msg) {
		if (this.errorMsgList == null)
			this.errorMsgList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(msg)) {
			this.errorMsgList.add(msg);
		}
	}

	public void addGeneralMsg(String msg) {
		if (this.generalMsgList == null)
			this.generalMsgList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(msg)) {
			this.generalMsgList.add(msg);
		}
	}

	public void actionCloseErrorMsgDialog(ActionEvent event) {
		this.errorMsgList = new ArrayList<String>();
	}

	public void actionCloseGeneralMsgDialog(ActionEvent event) {
		this.generalMsgList = new ArrayList<String>();
	}
	
	protected SecurityService getSecurityService() {
		return getAppBean().getSecurityService();
	}
	
	public void setSelectedTab(String tab) {
		this.selectedTab = tab;
	}

	public String closeErrorWindowAction() {
		setDisplayError(false);
		return "close_error";
	}

	public void editEntity(Long id) {
		Entity entity = getWrapper().getEntityById(id);
		this.editEntity(entity);
	}
	
	public void editEntity(String id) {
		try{
			Long entityId = Long.parseLong(id);
			this.editEntity(entityId);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public void setPlaceInGeoForm(String id){
		try {
			Long placeId = new Long(id);
			Entity place = getWrapper().getEntityById(placeId);
			if(place != null){
				this.geoForm.loadPlace(place);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	

	public void editEntity(Entity entity) {
		logger.info("[" + this.getUsername() +  "] Edit Entity= " + entity);

		entity = getWrapper().getEntityContent(entity);

		this.editFormCurrentEntId = entity.getId();
		if (entity.getObjectClass().equals("WITNESS")) {
			this.editWitness(entity);
			selectedTab = WITNESS_TAB;
		} else if (entity.getObjectClass().equals("TEXT")) {
			this.editText(entity);
			selectedTab = TEXT_TAB;
		} else if (entity.getObjectClass().equals("REPOSITORY")) {
			this.editRepository(entity);
			selectedTab = REPOSITORY_TAB;
		} else if (entity.getObjectClass().equals("PERSON")) {
			this.editPerson(entity);
			selectedTab = PERSON_TAB;
		} else if (entity.getObjectClass().equals("COLLECTION")) {
			this.editCollection(entity);
			selectedTab = COLLECTION_TAB;
		} else if (entity.getObjectClass().equals("CODEX")) {
			this.editCodex(entity);
			selectedTab = CODEX_TAB;
		} else if (entity.getObjectClass().equals("PLACE")) {
			this.editPlace(entity);
			selectedTab = CITY_TAB;
		} else if (entity.getObjectClass().equals("ALIAS")) {
			this.editAlias(entity);
			selectedTab = ALIAS_TAB;
		} else if (entity.getObjectClass().equals("SUBJECT")) {
			this.editSubject(entity);
			selectedTab = SUBJECT_TAB;
		} else if (entity.getObjectClass().equals("ROLE")) {
			this.editRole(entity);
			selectedTab = ROLE_TAB;
		} else if (entity.getObjectClass().equals("DIGITALIZATION")) {
			this.editDigi(entity);
			selectedTab = DIGI_TAB;			
		} else if (entity.getObjectClass().equals(StudyEvent.OC)) {
			this.editWitnessStudyEvent(entity);
			selectedEventTab = WITNESS_STUDY_EVENT_TAB;
		} else if (entity.getObjectClass().equals(CopyEvent.OC)) {
			this.editCopyEvent(entity);
			selectedEventTab = COPY_EVENT_TAB;
		} else if (entity.getObjectClass().equals(TransferEvent.OC)) {
			this.editTransferEvent(entity);
			selectedEventTab = TRANSFER_EVENT_TAB;
		} else {
			addErrorMsg("Form not implemented for these entities [" + entity.getObjectClass() + "]");
		}
	}

	//***************************
	//***************************
	//***************************
	//***************************
	//***************************
	private void editText(Entity text) {
		CurrentTextBean bean = (CurrentTextBean)getSessionBean(ApplicationBean.CURRENT_TEXT);
		if(bean == null){
			bean = new CurrentTextBean();
			addSessionBean(ApplicationBean.CURRENT_TEXT, bean);	
		}
		try {
			bean.setEntity(text);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editRepository(Entity repository) {
		CurrentRepositoryBean bean = (CurrentRepositoryBean)getSessionBean(ApplicationBean.CURRENT_REPOSITORY);
		if(bean == null){
			bean = new CurrentRepositoryBean();
			addSessionBean(ApplicationBean.CURRENT_REPOSITORY, bean);
		}
		try {
			bean.setEntity(repository);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	private void editPerson(Entity person) {
		CurrentPersonBean bean = (CurrentPersonBean)getSessionBean(ApplicationBean.CURRENT_PERSON);
		if(bean == null){
			bean = new CurrentPersonBean();
			addSessionBean(ApplicationBean.CURRENT_PERSON, bean);
		}
		try {
			bean.setEntity(person);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editCollection(Entity collection) {
		CurrentCollectionBean bean = (CurrentCollectionBean) getSessionBean(ApplicationBean.CURRENT_COLLECTION);
		if (bean == null) {
			bean = new CurrentCollectionBean();
			addSessionBean(ApplicationBean.CURRENT_COLLECTION, bean);
		}
		try {
			bean.setEntity(collection);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editCodex(Entity codex) {
		CurrentCodexBean bean = (CurrentCodexBean) getSessionBean(ApplicationBean.CURRENT_CODEX);
		if (bean == null) {
			bean = new CurrentCodexBean();
			addSessionBean(ApplicationBean.CURRENT_CODEX, bean);
		}
		try {
			bean.setEntity(codex);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editPlace(Entity place) {
		CurrentCityBean bean = (CurrentCityBean) getSessionBean(ApplicationBean.CURRENT_CITY);
		if (bean == null) {
			bean = new CurrentCityBean();
			addSessionBean(ApplicationBean.CURRENT_CITY, bean);
		}
		try {
			bean.setEntity(place);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editAlias(Entity alias) {
		CurrentAliasBean bean = (CurrentAliasBean) getSessionBean(ApplicationBean.CURRENT_ALIAS);
		if (bean == null) {
			bean = new CurrentAliasBean();
			addSessionBean(ApplicationBean.CURRENT_ALIAS, bean);
		}
		try {
			bean.setEntity(alias);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	private void editSubject(Entity subject) {
		CurrentSubjectBean bean = (CurrentSubjectBean) getSessionBean(ApplicationBean.CURRENT_SUBJECT);
		if (bean == null) {
			bean = new CurrentSubjectBean();
			addSessionBean(ApplicationBean.CURRENT_SUBJECT, bean);
		}
		try {
			bean.setEntity(subject);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	private void editRole(Entity role) {
		CurrentRoleBean bean = (CurrentRoleBean) getSessionBean(ApplicationBean.CURRENT_ROLE);
		if (bean == null) {
			bean = new CurrentRoleBean();
			addSessionBean(ApplicationBean.CURRENT_ROLE, bean);
		}
		try {
			bean.setEntity(role);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	
	private void editDigi(Entity digi) {
		CurrentDigitalizationBean bean = (CurrentDigitalizationBean) getSessionBean(ApplicationBean.CURRENT_DIGI);
		if (bean == null) {
			bean = new CurrentDigitalizationBean();
			addSessionBean(ApplicationBean.CURRENT_DIGI, bean);
		}
		try {
			bean.setEntity(digi);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	

	private void editWitness(Entity witness) {
		CurrentWitnessBean bean = (CurrentWitnessBean) getSessionBean(ApplicationBean.CURRENT_WITNESS);
		if (bean == null) {
			bean = new CurrentWitnessBean();
			addSessionBean(ApplicationBean.CURRENT_WITNESS, bean);
		}
		try {
			bean.setEntity(witness);
		} catch (Exception e) {
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	private void editWitnessStudyEvent(Entity event){
		this.studyEventForm = new StudyEvent(event);
	}
	
	public void listenerCreateStudyEvent(ActionEvent event){
		this.studyEventForm = new StudyEvent();
	}

	private void editCopyEvent(Entity event){
		this.copyEventForm = new CopyEvent(event);
	}
	
	public void listenerCreateCopyEvent(ActionEvent event){
		this.copyEventForm = new CopyEvent();
	}
	
	private void editTransferEvent(Entity event){
		this.transferEventForm = new TransferEvent(event);
	}
	
	public void listenerCreateTransferEvent(ActionEvent event){
		this.transferEventForm = new TransferEvent();
	}

		
	public Long getEditFormCurrentEntId() {
		return editFormCurrentEntId;
	}

	public void setEditFormCurrentEntId(Long editFormCurrentEntId) {
		this.editFormCurrentEntId = editFormCurrentEntId;
	}

	
    public void updateCurrent(ItemChangeEvent event) {
    	System.out.println(event.getNewItemName());
    }
	
	public void listenerEntityTabChange(ItemChangeEvent event) {
		String url = "?formIndex=" + this.selectedTab;
		this.redirect(null, url);
	}
	
	public void listenerEventTabChange(ItemChangeEvent event) {
		String url = "?formIndex=" + this.selectedEventTab;
		this.redirect(null, url);
	}
	
	public void listenerAdvancedSearchTabChange(ItemChangeEvent event) {
		String url = "?searchForm=" + this.selectedAdvancedSearchTab;
		this.redirect(null, url);
	}
	
	protected void redirect(String redirectPath, String parameters){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String contextPath = ec.getRequestContextPath();
		if(StringUtils.isEmpty(redirectPath)){
			redirectPath = ec.getRequestServletPath();	
		}
		try {
			ec.redirect(ec.encodeActionURL(contextPath + redirectPath + parameters));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Entity getDefinition(Entity assertion) {
		return getWrapper().getDefinition(assertion.getObjectClass());
	}
	
	public String toProfile(){
		return PAGE_PROFILE;
	}

	public String toSimpleSearch() {
		return PAGE_SIMPLE_SEARCH;
	}

	public String toDefinitionEditor(){
		if(this.defForm != null){
			this.defForm.loadDefinitions();
			return PAGE_DEF_EDITOR;
		}
		return "";
	}
	
	public String toGeoForm(){
		this.geoForm.loadPlaces(null);
		return PAGE_GEO_FORM;
	}

	public String displayByAttribute() {
		Attribute att = (Attribute) getRequestBean("attribute");
		logger.info(att);
		if (att != null) {
			Entity ent = getWrapper().getEntityById(att.getSourceId());
			if (ent.getObjectClass().equals("TEXT")) {
				this.displayTitle.showTitle(ent.getId());
				return PAGE_DISPLAY_TITLE;
			} else if (ent.getObjectClass().equals("PERSON")) {
				this.displayAuthor.showAuthor(ent.getId());
				return PAGE_DISPLAY_AUTHOR;
			}
		}

		return "";
	}
	
	public void displayAuthorSetPerson(String personId){
		try{
			Long id = new Long(personId);
			Entity person = getWrapper().getEntityById(id);
			if(person != null){
				person = getWrapper().getEntityContent(person);
				if (person.getObjectClass().equals("PERSON")) {
					this.displayAuthor.showAuthor(person.getId());
				}	
			}
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayAuthorSetTitle(String textId){
		try{
			Long id = new Long(textId);
			this.displayAuthor.showTitle(id);
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayAuthorSetWitness(String witnessId){
		try{
			Long id = new Long(witnessId);
			this.displayAuthor.showWitness(id);
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayTitleSetTitle(String textId){
		try{
			Long id = new Long(textId);
			Entity ent = getWrapper().getEntityById(id);
			if(ent != null){
				ent = getWrapper().getEntityContent(ent);
				if (ent.getObjectClass().equals("TEXT")) {
					//this.editText(ent);
					this.displayTitle.showTitle(id);
				}	
			}
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayTitleSetWitness(String witnessId){
		try{
			Long id = new Long(witnessId);
			this.displayTitle.showWitness(id);
			
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayTitleFromWitness(String witnessId){
		try{
			Long longWitnessId = new Long(witnessId);
			
			List<Entity> textList = 
				getWrapper().getTargetsForSourceRelation(longWitnessId, "is_exemplar_of", "TEXT", 1);
			if(textList.size() > 0){
				this.displayTitle.showTitle(textList.get(0).getId());
				this.displayTitle.showWitness(longWitnessId);	
			}
			
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayAuthorFromWitness(String witnessId){
		try{
			Long longWitnessId = new Long(witnessId);
			
			List<Entity> titleList = 
				getWrapper().getTargetsForSourceRelation(longWitnessId, "is_exemplar_of", "TEXT", 1);
			if(titleList.size() > 0){
				Entity title = titleList.get(0);
				List<Entity> authorList = 
					getWrapper().getTargetsForSourceRelation(title.getId(), "was_created_by", "PERSON", 1);
				if(authorList.size() > 0){
					this.displayAuthor.showAuthor(authorList.get(0).getId());
					this.displayAuthor.showTitle(title.getId());
					this.displayAuthor.showWitness(longWitnessId);
				}
			}
			
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	
	public void displayAuthorFromTitles(String titleId){
		try{
			Long longTitleId = new Long(titleId);

			List<Entity> authorList = 
				getWrapper().getTargetsForSourceRelation(longTitleId, "was_created_by", "PERSON", 1);
			if(authorList.size() > 0){
				this.displayAuthor.showAuthor(authorList.get(0).getId());
				this.displayAuthor.showTitle(longTitleId);
			}
			
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}
	

	public String editByAttribute() {
		Attribute att = (Attribute) getRequestBean("attribute");
		logger.info(att);
		if (att != null) {
			Entity ent = getWrapper().getEntityById(att.getSourceId());
			if (ent != null) {
				this.editEntity(ent);
				return PAGE_ENTRY;
			}
		}

		return "";
	}
	
	public void setIdEntityDetails(String id){
		try{
			Long idLong = new Long(id);
			Entity e = getWrapper().getEntityById(idLong);
			this.entDetailsForm.setEntity(e);
		}catch(Exception e){
			logger.error("[U=" + this.getUsername() +  "]" + e.getMessage(), e);
		}
	}

	public String editByEntity() {
		Entity ent = (Entity) getRequestBean("entity");
		if (ent != null) {
			if (ent != null) {
				this.editEntity(ent);
				return PAGE_ENTRY;
			}
		}
		return "";
	}

	public void setErrorMessages(CheckResults cr) {

		errorMessages = new ArrayList<String>();
		for (String err : cr.errors) {
			errorMessages.add(err);
		}

	}

	public void setDisplayError(boolean b) {
		displayError = b;

	}

	public boolean getDisplayError() {
		return displayError;
	}

	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	public User getUser() {
		if(user == null){
			autoLogin();
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GeneralMerge getGeneralMerge() {
		return generalMerge;
	}

	public void setGeneralMerge(GeneralMerge generalMerge) {
		this.generalMerge = generalMerge;
	}

	public DefinitionForm getDefForm() {
		return defForm;
	}

	public void setDefForm(DefinitionForm defForm) {
		this.defForm = defForm;
	}

	public StudyEvent getStudyEventForm() {
		return studyEventForm;
	}

	public void setStudyEventForm(StudyEvent studyEventForm) {
		this.studyEventForm = studyEventForm;
	}

	public CopyEvent getCopyEventForm() {
		return copyEventForm;
	}

	public void setCopyEventForm(CopyEvent copyEventForm) {
		this.copyEventForm = copyEventForm;
	}

	public TransferEvent getTransferEventForm() {
		return transferEventForm;
	}

	public void setTransferEventForm(TransferEvent transferEventForm) {
		this.transferEventForm = transferEventForm;
	}

	public String getSelectedEventTab() {
		return selectedEventTab;
	}

	public void setSelectedEventTab(String selectedEventTab) {
		this.selectedEventTab = selectedEventTab;
	}

	public AdvancedSearchBean getAdvancedSearch() {
		return advancedSearch;
	}

	public void setAdvancedSearch(AdvancedSearchBean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}

	public DisplayAuthorBean getDisplayAuthor() {
		return displayAuthor;
	}

	public void setDisplayAuthor(DisplayAuthorBean displayAuthor) {
		this.displayAuthor = displayAuthor;
	}

	public DisplayTitleBean getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(DisplayTitleBean displayTitle) {
		this.displayTitle = displayTitle;
	}

	public Calendar getCurrentCalendar() {
		return currentCalendar;
	}

	public void setCurrentCalendar(Calendar currentCalendar) {
		this.currentCalendar = currentCalendar;
	}

	public EntityList getCurrentEntListForCalendar() {
		return currentEntListForCalendar;
	}

	public void setCurrentEntListForCalendar(EntityList currentEntListForCalendar) {
		this.currentEntListForCalendar = currentEntListForCalendar;
	}

	public Long getCurrentEntIdForCalendar() {
		return currentEntIdForCalendar;
	}

	public void setCurrentEntIdForCalendar(Long currentEntIdForCalendar) {
		this.currentEntIdForCalendar = currentEntIdForCalendar;
	}

	public boolean isDisplayLoginDialog() {
		return displayLoginDialog;
	}
	
	public boolean isDisplayLoginDialog4PrivatePage(){
		return displayLoginDialog || this.user == null;
	}
	
	public boolean isDisplayLoginDialog4PublicPage(){
		return displayLoginDialog;
	}

	public EntityDetailsBean getEntDetailsForm() {
		return entDetailsForm;
	}

	public void setEntDetailsForm(EntityDetailsBean entDetailsForm) {
		this.entDetailsForm = entDetailsForm;
	}

	public ESciDocItemForm geteSciDocForm() {
		return eSciDocForm;
	}

	public void seteSciDocForm(ESciDocItemForm eSciDocForm) {
		this.eSciDocForm = eSciDocForm;
	}

	public ESciDocItemDataTable getRefDataTable() {
		return refDataTable;
	}

	public void setRefDataTable(ESciDocItemDataTable refDataTable) {
		this.refDataTable = refDataTable;
	}

	public GeonameForm getGeoForm() {
		return geoForm;
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public String getSelectedAdvancedSearchTab() {
		return selectedAdvancedSearchTab;
	}

	public void setSelectedAdvancedSearchTab(String selectedAdvancedSearchTab) {
		this.selectedAdvancedSearchTab = selectedAdvancedSearchTab;
	}

	
	public DynamicPageEditor getPageEditor(){
		if(this.pageEditor == null){
			this.pageEditor = new DynamicPageEditor();
		}
		return this.pageEditor;
	}

	public DigiListBean getDigiList() {
		if(digiList == null){
			digiList = new DigiListBean(getWrapper());
		}
		
		return digiList;
	}

	public DynamicPage getDynamicPage() {
		if(dynamicPage == null){
			this.dynamicPage = new DynamicPage();
		}
		return dynamicPage;
	}

	public void setDynamicPage(DynamicPage dynamicPage) {
		this.dynamicPage = dynamicPage;
	}
	
	public PublicCodexView getPublicCodexView(){
		if(publicCodexView == null){
			this.publicCodexView = new PublicCodexView();
		}
		return this.publicCodexView;
	}

	public PublicCodexBean getPublicCodexBean() {
		if(publicCodexBean == null){
			this.publicCodexBean = new PublicCodexBean();
		}
		return publicCodexBean;
	}

	public void setPublicCodexBean(PublicCodexBean publicCodexBean) {
		this.publicCodexBean = publicCodexBean;
	}
	
	
	
}

