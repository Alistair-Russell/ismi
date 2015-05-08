package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.configuration.ConfigurationService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.services.ServiceRegistry;
import org.mpi.openmind.search.SearchService;
import org.mpi.openmind.security.SecurityService;
import org.mpiwg.itgroup.escidoc.ESciDocCache;

import de.mpiwg.itgroup.ismi.entry.dataBeans.SimpleSearchCache;
import de.mpiwg.itgroup.ismi.publicView.PublicCodexList;

/**
 * <p>
 * Application scope data bean for your application. Create properties here to
 * represent cached data that should be made available to all users and pages in
 * the application.
 * </p>
 * 
 * <p>
 * An instance of this class will be created for you automatically, the first
 * time your application evaluates a value binding expression or method binding
 * expression that references a managed bean using this class.
 * </p>
 * 
 * @version ApplicationBean1.java
 * @version Created on 23.06.2009, 10:58:17
 * @author jurzua
 */

public class ApplicationBean  implements Serializable{
	
	private static final long serialVersionUID = 804932192506497432L;

	private static Logger logger = Logger.getLogger(ApplicationBean.class);
	
	public static String urlISMIExportServiceAuthors = 
		"https://openmind-ismi-dev.mpiwg-berlin.mpg.de/ISMI/database/export/authorsTitlesWitness";
	
	public static String urlISMIExportServiceTitles = 
		"https://openmind-ismi-dev.mpiwg-berlin.mpg.de/ISMI/database/export/titlesWitnesses";
	
	private BiographyBean biographyBean;
	
	private PublicCodexList publicCodexList;
	
	public ESciDocCache refCache = new ESciDocCache();
	
	public static String generateExportURL(String urlRoot, List<Long> list, String mode){
		StringBuilder sb = new StringBuilder(urlRoot);
		
		sb.append("?ids=" + generateIdList(list));
		
		if(StringUtils.isNotEmpty(mode)){
			sb.append("&mode=" + mode);
		}
		
		return sb.toString();
	}
	
	public static String generateIdList(List<Long> list){
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		for(Long id : list){
			
			if(count>0){
				sb.append("%7C");
			}
			sb.append(id);
			count++;
		}
		return sb.toString();
	}
	
	public static String generateExportURL(String urlRoot, String idList, String mode){
		StringBuilder sb = new StringBuilder(urlRoot);
		
		sb.append("?ids=" + idList);
		if(StringUtils.isNotEmpty(mode)){
			sb.append("&mode=" + mode);
		}
		
		return sb.toString();
	}
	
	
	public static String generateExportURL(String urlRoot, Long id, String mode){
		StringBuilder sb = new StringBuilder(urlRoot);
		
		sb.append("?ids=" + id);
		if(StringUtils.isNotEmpty(mode)){
			sb.append("&mode=" + mode);
		}
		
		return sb.toString();
	}
	
	public static String CURRENT_WITNESS = "CurrentWitness";
	public static String CURRENT_CODEX = "CurrentCodex";
	public static String CURRENT_TEXT = "CurrentText";
	public static String CURRENT_PERSON = "CurrentPerson";
	public static String CURRENT_ALIAS = "CurrentAlias";
	public static String CURRENT_COLLECTION = "CurrentCollection";
	public static String CURRENT_REPOSITORY = "CurrentRepository";
	public static String CURRENT_CITY = "CurrentCity";
	public static String CURRENT_SUBJECT = "CurrentSubject";
	public static String CURRENT_ROLE = "CurrentRole";
	public static String CURRENT_DIGI = "CurrentDigi";
	
	private transient SimpleSearchCache simpleSearchCache;
	ServiceRegistry serviceRegistry = null;

	
	private List<SelectItem> roleList = new ArrayList<SelectItem>();
	private List<Entity> entityRoleList = new ArrayList<Entity>();
	private boolean roleListDirty = true;

	private static List<SelectItem> yes_no = new ArrayList<SelectItem>();
	private static List<SelectItem> roles = new ArrayList<SelectItem>();
	
	private static List<SelectItem> suggestedStatus = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedBindings = new ArrayList<SelectItem>();
	private static List<SelectItem> writingSurfaces = new ArrayList<SelectItem>();
	private static List<SelectItem> pageLayout = new ArrayList<SelectItem>();
	private static List<SelectItem> scripts = new ArrayList<SelectItem>();
	private static List<SelectItem> sourcesOfInformation = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedLanguages = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedTextTypes = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedBoolean = new ArrayList<SelectItem>();
	
	
	public static String STATUS_NEED_VERIFICATION = "needs verification";
	public static String STATUS_VERIFIED_BY_CATALOGUE = "verified by catalogue";
	public static String STATUS_VERIFIED_BY_WITNESS = "verified by witness";
	public static String STATUS_NOT_CHECKED = "not checked";
	
	public static String FORMATED_DATE = "formatted date";
	public static String PLAIN_DATE = "plain date";
	private static List<SelectItem> dateTypes = new ArrayList<SelectItem>();
	
	private List<SelectItem> suggestedSubjects = null;
	
	public void resetSuggestedSubjects(){
		this.suggestedSubjects = new ArrayList<SelectItem>();
		this.suggestedSubjects.add(new SelectItem(null, "-- choose --"));
		
		List<Entity> cats = getWrapper().getEntitiesByAtt("SUBJECT", "type", "main_subject", -1, false);
		
		for(Entity cat : cats){
			SelectItem item = new SelectItem(cat.getId(), cat.getOwnValue());
			
			item.setStyle("font-weight: bold; padding-left: 0px; font-size: 14;");
			this.suggestedSubjects.add(item);
			List<Entity> subCats = getWrapper().getSourcesForTargetRelation(cat, "is_part_of", "SUBJECT", -1);
			for(Entity subCat : subCats){
				
				item = new SelectItem(subCat.getId(), subCat.getOwnValue());
				item.setStyle("padding-left: 10px; font-size: 12;");
				this.suggestedSubjects.add(item);
				
				List<Entity> subsubCats = getWrapper().getSourcesForTargetRelation(subCat, "is_part_of", "SUBJECT", -1);
				for(Entity subsubCat : subsubCats){
					if(subsubCat != null){
						item = new SelectItem(subsubCat.getId(), subsubCat.getOwnValue());
						
						this.suggestedSubjects.add(item);
						
					}else{
						
						System.out.println("####################ERROR####################");
						System.out.println("resetSuggestedSubjects");
						System.out.println(" main: " + cat);
						System.out.println(" sub: " + subCat);
					}
				}
			}
		}
	}
	
	public List<SelectItem> getSuggestedSubjects() {
		if(suggestedSubjects == null){
			this.resetSuggestedSubjects();
		}
		return suggestedSubjects;
	}

	public void setSuggestedSubjects(List<SelectItem> suggestedSubjects) {
		this.suggestedSubjects = suggestedSubjects;
	}

	public void listenerSynchronizeESciDocCacheWithServer(){
		this.refCache.synchronizeWithServer();
	}
	
	static{
		
		dateTypes.add(new SelectItem(FORMATED_DATE));
		dateTypes.add(new SelectItem(PLAIN_DATE));
		
		String[] status = new String[]{"needs verification", "verified by catalogue", "verified by witness"};
		Arrays.sort(status);
		
		suggestedStatus.add(new SelectItem("", STATUS_NOT_CHECKED)); 
		for(int i=0; i< status.length; i++){
			suggestedStatus.add(new SelectItem(status[i]));
		}
		
		String[] binding = new String[]{"cardboard", "leather with flap", "leather without flap"};
		Arrays.sort(binding);
		
		suggestedBindings.add(new SelectItem("", "-- choose --")); 
		for(int i=0; i< binding.length; i++){
			suggestedBindings.add(new SelectItem(binding[i]));
		}
		
		
		String[] langs = new String[] { "Arabic", "Persian-Farsi",
				"Greek-classical", "Hebrew", "Latin", "Syriac", "Armenian",
				"Castillian", "Catalan", "Chinese", "French", "German",
				"Greek-Byzantine", "Hindi", "Italian", "Mongolian",
				"Persian-Old", "Persian-Middle", "Russian", "Sanskrit",
				"Turkish (Ottoman pre-1839)", "Turkish (Ottoman 1839-)",
				"Turkish (pre-Ottoman)", "Turkish-Republican", "Urdu", "Uighur" };
		Arrays.sort(langs);
		
		suggestedLanguages.add(new SelectItem("", "-- choose --"));
		for(int i = 0; i < langs.length; i++){
			suggestedLanguages.add(new SelectItem(langs[i]));
		}
		
		String[] tts = new String[] {
				"First-order prose composition (or compilation) [matn]",
				"First-order verse composition (didactic poem)",
				"Second-order composition (Commentary) [sharh]",
				"Third-order composition (Supercommentary)",
				"Fourth-order composition (Gloss)",
				"Fifth-order composition (Supergloss)",
				"Sixth-order composition", "Seventh-order composition",
				"Rearrangement", "Abridgement (epitome)", "Versification",
				"Translation", "Paraphrase", "Supplement" };
		Arrays.sort(tts);
		
		suggestedTextTypes.add(new SelectItem("", "-- choose --"));
		for(int i = 0; i < tts.length; i++){
			suggestedTextTypes.add(new SelectItem(tts[i]));
		}
		
		yes_no.add(new SelectItem("", "-- choose --"));
		yes_no.add(new SelectItem("yes"));
		yes_no.add(new SelectItem("no"));
		
		suggestedBoolean.add(new SelectItem("", "-- choose --"));
		suggestedBoolean.add(new SelectItem("true"));
		suggestedBoolean.add(new SelectItem("false"));

		roles.add(new SelectItem("", "-- choose --"));
		roles.add(new SelectItem("annotator"));
		roles.add(new SelectItem("author"));
		roles.add(new SelectItem("copyist"));
		roles.add(new SelectItem("illuminator"));
		roles.add(new SelectItem("illustrator"));
		roles.add(new SelectItem("owner"));
		roles.add(new SelectItem("patron"));
		roles.add(new SelectItem("reader"));
		roles.add(new SelectItem("student"));
		roles.add(new SelectItem("teacher"));
		roles.add(new SelectItem("translator"));
		
		writingSurfaces.add(new SelectItem("", "-- choose --"));
		writingSurfaces.add(new SelectItem("HARD", "HARD"));
		writingSurfaces.add(new SelectItem("marble", "&nbsp;&nbsp;marble"));
		writingSurfaces.add(new SelectItem("metal instrument", "&nbsp;&nbsp;metal instrument"));
		writingSurfaces.add(new SelectItem("stone", "&nbsp;&nbsp;stone"));
		writingSurfaces.add(new SelectItem("SOFT", "SOFT"));
		writingSurfaces.add(new SelectItem("paper", "&nbsp;&nbsp;paper"));
		writingSurfaces.add(new SelectItem("papyrus", "&nbsp;&nbsp;papyrus"));
		writingSurfaces.add(new SelectItem("parchment", "&nbsp;&nbsp;parchment"));
		
		pageLayout.add(new SelectItem("", "-- choose --"));
		pageLayout.add(new SelectItem("Frame-ruled"));
		pageLayout.add(new SelectItem("Paper"));
	
		scripts.add(new SelectItem("", "-- choose --"));
		scripts.add(new SelectItem("Maghribi"));
		scripts.add(new SelectItem("Naskh"));
		scripts.add(new SelectItem("Nasta'liq"));
		scripts.add(new SelectItem("Riqa'"));
		scripts.add(new SelectItem("Ruq'ah"));
		scripts.add(new SelectItem("Shikastah"));
		scripts.add(new SelectItem("Sudani"));
		scripts.add(new SelectItem("Ta'liq"));
		scripts.add(new SelectItem("Tawqi'"));
		scripts.add(new SelectItem("Other-see Notes"));
		
		
		sourcesOfInformation.add(new SelectItem("", "-- choose --"));
		sourcesOfInformation.add(new SelectItem("Actual Witness"));
		sourcesOfInformation.add(new SelectItem("Microfilm"));
		sourcesOfInformation.add(new SelectItem("Digital Form (DVD/CD)"));
		sourcesOfInformation.add(new SelectItem("Catalogue (Give Full Citation In Notes)"));
	} 
	
	public SearchService getSS() {
		return serviceRegistry.getSearchService();
	}
	
	public WrapperService getWrapper() {
		return serviceRegistry.getWrapper();
	}
	
	public SecurityService getSecurityService() {
		return serviceRegistry.getSecurityService();
	}
	
	public ConfigurationService getConfService(){
		return serviceRegistry.getConfigurationService();
	}

	/**
	 * <p>
	 * Construct a new application data bean instance.
	 * </p>
	 */
	public ApplicationBean() {
		logger.info("Initialize ApplicationBean1");
		this.serviceRegistry = new ServiceRegistry();
		this.simpleSearchCache = new SimpleSearchCache(serviceRegistry);
	}

	public List<SelectItem> getYes_no() {
		return yes_no;
	}
	
	public List<SelectItem> getRoles() {
		return roles;
	}
	
	public List<SelectItem> getWritingSurfaces(){
		return writingSurfaces;
	}
	
	public List<SelectItem> getPageLayout(){
		return pageLayout;
	}
	
	public List<SelectItem> getScripts(){
		return scripts;
	}
	
	public List<SelectItem> getSuggestedStatus(){
		return suggestedStatus;
	}
	
	public List<SelectItem> getSuggestedBindings(){
		return suggestedBindings;
	}
	
	public List<SelectItem> getSourcesOfInformation(){
		return sourcesOfInformation;
	}
	
	public List<SelectItem> getSuggestedLanguages(){
		return suggestedLanguages;
	}
	
	public List<SelectItem> getSuggestedTextTypes(){
		return suggestedTextTypes;
	}
	
	public List<SelectItem> getSuggestedBoolean(){
		return suggestedBoolean;
	}
	
	public List<SelectItem> getDateTypes(){
		return dateTypes;
	}
	
	public SimpleSearchCache getSimpleSearchCache() {
		return simpleSearchCache;
	}

	public void setSimpleSearchCache(SimpleSearchCache simpleSearchCache) {
		this.simpleSearchCache = simpleSearchCache;
	}

	public void setRoleListAsDirty(){
		this.roleListDirty = true;
	}
	
	public String getRoot(){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	public List<SelectItem> getRoleList(){
		if(roleListDirty){
			this.roleList = new ArrayList<SelectItem>();
			this.entityRoleList = getWrapper().getEntitiesByDef("ROLE");
			for(Entity e : this.entityRoleList){
				this.roleList.add(new SelectItem(e.getId(), e.getOwnValue()));
			}
			this.roleListDirty = false;
		}
		
		return this.roleList;
	}
	
	public String getEditImage(){
		return "/resources/images/system/edit_20.png";
	}
	
	public String getImgEdit16(){
		return "/resources/images/system/edit_16.png";
	}
	
	public String getImgEdit32(){
		return "/resources/images/system/edit_32.png";
	}
	
	public String getImgRemove16(){
		return "/resources/images/system/remove_16.png";
	}
	
	public String getImgRemove32(){
		return "/resources/images/system/remove_32.png";
	}
	
	public String getImgVersions16(){
		return "/resources/images/system/versions_16.png";
	}
	
	public String getImgVersions32(){
		return "/resources/images/system/versions_32.png";
	}
	
	public String getImgSave16(){
		return "/resources/images/system/save_16.png";
	}
	
	public String getImgSave32(){
		return "/resources/images/system/save_32.png";
	}
	
	public String getImgClean16(){
		return "/resources/images/system/clean_16.png";
	}
	
	public String getImgClean32(){
		return "/resources/images/system/clean_32.png";
	}

	public String getImgNew16(){
		return "/resources/images/system/new_16.png";
	}
	
	public String getImgNew32(){
		return "/resources/images/system/new_32.png";
	}
	
	public String getImgSeach16(){
		return "/resources/images/system/search_16.png";
	}
	
	public String getImgSearch32(){
		return "/resources/images/system/search_32.png";
	}
	
	public String getDisplayImage(){
		return "/resources/images/display_32.png";
	}
	
	public String getWorldImage(){
		return "/resources/images/icy_earth_32.png";
	}
	
	public String getBookImage(){
		return "/resources/images/book_32.png";
	}
	
	public String getSearchImage(){
		return "/resources/images/search_32.png";
	}
	
	public String getImgNetwork32(){
		return "/resources/images/network_32.png";
	}
	
	public String getImgPerson32(){
		return "/resources/images/person_32.png";
	}
	
	
	public String getImgDown32(){
		return "/resources/images/down_32.png";
	}
	
	public String getImgUp32(){
		return "/resources/images/up_32.png";
	}
	
	///****
	//private List<Entity> texts;
	private List<SelectItem> textsSelectItems;
	private boolean dirty = false;
	
    private void updateListAllTexts(){
    	textsSelectItems = new ArrayList<SelectItem>();
    	for (Entity text : getWrapper().getEntitiesByDef("TEXT")){
    		textsSelectItems.add(new SelectItem(text.getId(), text.getOwnValue() +  " [" +text.getId()+ "]"));
    	}
    }
    
	public List<SelectItem> getAllTexts() {
		if(this.dirty || textsSelectItems == null){
			this.updateListAllTexts();
			this.dirty = false;
		}	
		return textsSelectItems;
	}
	
	public void setAllTextsAsDirty(){
		this.dirty = true;
	}

	public ESciDocCache getRefCache() {
		return refCache;
	}
	
	public String getJSConfirmationSave(){
		return "if(!confirm('Do you really want to save the changes?')){ return false; };";
	}
	
	public String getJSConfirmationLogout(){
		return "if(!confirm('Do you really want to end your session?')){ return false; };";
	}
	
	public String getJSConfirmationDelete(){
		return "if(!confirm('Do you really want to delete this?')){ return false; };";
	}
	
	public String getJSConfirmationSaveAsNew(){
		return "if(!confirm('Do you really want to save the entity as new?')){ return false;};";
	}
	
	public String getJSConfirmationMerge(){
		return "if(!confirm('Do you really want to merge these entities?')){ return false;};";
	}
	
	public String getJSConfirmationCleanForm(){
		return "if(!confirm('Do you really want to clear the form?')){ return false;};";
	}
	
	public String getContextRoot(){
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		
		
		String port = (StringUtils.equals(request.getLocalPort() + "", "80")) ? "" : (":" + request.getLocalPort());
		String path = request.getScheme() + "://" + request.getLocalName() + port + request.getContextPath();
		
		return path;
	}
	
	public BiographyBean getBiographyBean(){
		if(biographyBean == null)
			this.biographyBean = new BiographyBean();
		return this.biographyBean;
	}

	public PublicCodexList getPublicCodexList(){
		if(publicCodexList == null){
			publicCodexList = new PublicCodexList();
		}
		return publicCodexList;
	}
}
