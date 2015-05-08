package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.security.SecurityService;
import org.mpi.openmind.security.bo.User;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.Publication;
import org.mpiwg.itgroup.escidoc.utils.ESciDocItemDataTable;
import org.mpiwg.itgroup.escidoc.utils.SelectedESciDocItems;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.dataBeans.SimpleSearchCache;
import de.mpiwg.itgroup.ismi.util.guiComponents.Reference;
import de.mpiwg.itgroup.ismi.util.guiComponents.ReferenceTable;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusChecker;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

/**
 * 
 * @author jurzua
 */
public class AbstractISMIBean extends AbstractBean implements Serializable{
	
	private static final long serialVersionUID = 9193140306343947209L;

	private static Logger logger = Logger.getLogger(AbstractISMIBean.class);

	public static String WITNESS = "WITNESS";
	public static String TEXT = "TEXT";
	public static String PERSON = "PERSON";
	public static String COLLECTION = "COLLECTION";
	public static String CODEX = "CODEX";
	public static String REPOSITORY = "REPOSITORY";
	public static String ALIAS = "ALIAS";
	public static String PLACE = "PLACE";
	public static String SUBJECT = "SUBJECT";
	public static String REFERENCE = "REFERENCE";
	public static String ROLE = "ROLE";
	public static String DIGITALIZATION = "DIGITALIZATION";
	public static String FLORUIT_DATE = "FLORUIT_DATE";
	
	public static String is_digitalization_of = "is_digitalization_of";
	public static String is_part_of = "is_part_of";
	public static String is_in = "is_in";
	public static String has_subject = "has_subject";
	public static String misattributed_to = "misattributed_to";
	public static String lived_in = "lived_in";
	public static String owned_by = "owned_by";
	public static String has_role = "has_role";
	public static String was_student_of = "was_student_of";
	public static String is_alias_name_of = "is_alias_name_of";
	public static String is_prime_alias_name_of = "is_prime_alias_name_of";
	public static String is_alias_title_of = "is_alias_title_of";
	public static String is_prime_alias_title_of = "is_prime_alias_title_of";
	public static String alias = "alias";
	public static String has_floruit_date = "has_floruit_date";
	
	public static String rel_was_created_by = "was_created_by";
	public static String rel_was_copied_by = "was_copied_by";
	public static String is_exemplar_of = "is_exemplar_of";
	public static String is_possible_exemplar_of = "is_possible_exemplar_of";
	public static String rel_had_patron = "had_patron";
	public static String rel_has_title_written_as = "has_title_written_as";
	public static String rel_has_author_written_as = "has_author_written_as";
	public static String rel_is_reference_of = "is_reference_of";
	
	public static String rel_was_studied_by = "was_studied_by";
	
	public static String PAGE_EDITOR = "entry_edit_entity";
	public static String PAGE_SIMPLE_SEARCH = "simple_search";
	public static String PAGE_MERGE = "general_merge";
	
	//some attributes names
	public static String name = "name";
	public static String name_translit = "name_translit";
	public static String full_title_translit = "full_title_translit";
	
	public static int MAX_SUGGEST = 25;
	
	protected boolean selectedSaveAsNew = false;
	
	private HashSet<StatusChecker> statusChecker = new HashSet<StatusChecker>();
	private boolean warning = false;
	private String warningMessage = "";
	private boolean displayWarning = false;

	//private PanelPopup popup = new PanelPopup();
	//private String popupText = "";
	
	private String currentId;
	private String currentIdMsg;

	// used in the new version
	private Boolean create_error = false;
	private String selectedWitnessID = "0";
	private boolean showWitness;
	private boolean showTitle = false;

	private Map<String, String> attributes = new HashMap<String, String>();
	protected String defObjectClass = null;
	
	protected String displayUrl;
	
	protected Entity entity;
	
	protected long start;
	
	public String save(){
		logger.info("*************** START Saving "+ this.defObjectClass + " [ID=" + entity.getId() +", user"+ getUserName() +"] *********************");
		this.start = System.currentTimeMillis();
		return null;
	}
	
	protected void saveEnd(){
		logger.info("*************** END Saving "+ this.defObjectClass + " [ID=" + entity.getId() +", user"+ getUserName() +"] *********************\n");
	}
	
	public Entity getEntity() {
		if (this.entity == null) {
			this.entity = new Entity();
			this.entity.setObjectClass(defObjectClass);
			this.entity.setLightweight(false);
			this.loadAttributes(this.entity);
		}
		return entity;
	}

	protected void printSuccessSavingEntity(){
		this.addGeneralMsg("The entity was successfully saved!");
		this.addGeneralMsg("Its ID is " + this.entity.getId());
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public String getSaveButtonLabel(){
		if(entity.isPersistent())
			return "Save";
		return "Create";
	}

	public boolean isRenderSaveButton(){
		//#{(CurrentText.entity.id == null and Session.canCreate) || (CurrentText.entity.id != null and Session.canEdit)}
		if((!entity.isPersistent() && getSessionBean().isCanCreate()) ||
				entity.isPersistent() && getSessionBean().isCanEdit())
			return true;
		return false;
	}
	
	public String clearAction(){
		System.out.println("clearAction");
		this.entity = new Entity(Node.TYPE_ABOX, this.defObjectClass, false);
		this.setEntity(this.entity);
		return PAGE_EDITOR;
	}
	
	public String actionReloadEntity(){
		Long id = null;
		try{
			id = new Long(this.getCurrentId());
		}catch (Exception e) {}
		
		if(id != null){
			Entity tmp = getWrapper().getEntityById(id);
			if(tmp != null && tmp.getObjectClass().equals(this.defObjectClass)){
				this.setEntity(tmp);
			}else{
				this.setCurrentIdMsg("Entity no found!");
			}
		}else{
			this.setCurrentIdMsg("ID is not valid!");
		}
		return PAGE_EDITOR;
	}
	
	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}
	
	public static String generateDisplayUrl(Entity author, Entity title, Entity witness, String rootUrl){
		//example: ?personId=414996&textId=36650&witnessId=66802#witnesses
		StringBuilder sb = new StringBuilder();
		if(author != null && author.isPersistent()){
			sb.append(rootUrl + "/search/displayAuthor.xhtml?");
			sb.append("personId=" + author.getId());
			if(title != null && title.isPersistent()){
				sb.append("&textId=" + title.getId());
				if(witness != null && witness.isPersistent()){
					sb.append("&witness=" + witness.getId());
					sb.append("#witnesses");
				}else{
					sb.append("#titles");
				}	
			}
		}else if(title != null && title.isPersistent()){
			sb.append(rootUrl + "/search/displayTitle.xhtml?");
			sb.append("&textId=" + title.getId());
			if(witness != null && witness.isPersistent()){
				sb.append("&witness=" + witness.getId());
				sb.append("#witnesses");
			}else{
				sb.append("#titles");
			}
			
		}		
		return sb.toString();
	}
	
	//********************************
	//**** PUBLICATIONS END NOTE *****
	//********************************
	
	private ReferenceTable endNoteRefTable = new ReferenceTable();
	
	protected void loadEndNoteRefs(){
		this.endNoteRefTable.loadRefs(getEntRefs());
	}
	
	public ReferenceTable getEndNoteRefTable() {
		return endNoteRefTable;
	}

	public void setEndNoteRefTable(ReferenceTable endNoteRefTable) {
		this.endNoteRefTable = endNoteRefTable;
	}

	private List<Entity> getEntRefs(){
		List<Entity> rs = new ArrayList<Entity>();
		for (Relation rel : this.entity.getTargetRelations()) {
			if (rel.getOwnValue().equals(rel_is_reference_of)) {
				rs.add(getWrapper().getEntityByIdWithContent(rel.getSourceId()));
			}
		}
		return rs;
	}
		
	//********************************
	//**** PUBLICATIONS ESciDOC ******
	//********************************
	
	private ESciDocItemDataTable refDataTable = new ESciDocItemDataTable(this);
	protected SelectedESciDocItems selectedItems = new SelectedESciDocItems();
	
	public ESciDocItemDataTable getRefDataTable() {
		return refDataTable;
	}

	public void setRefDataTable(ESciDocItemDataTable refDataTable) {
		this.refDataTable = refDataTable;
	}

	public void listenerRemoveReference(ActionEvent event){
		List<String> pubIdList = new ArrayList<String>();
		for(String pubId : this.selectedItems.getMap().keySet()){
			if(this.selectedItems.getMap().get(pubId)){
				pubIdList.add(pubId);
			}
		}
		for(String pubId : pubIdList){
			this.selectedItems.removeById(pubId);
		}
	}
	
	public void loadReferences(Entity currentEntity){
		this.loadReferences(currentEntity, new ArrayList<String>());
	}
	
	public void loadReferences(Entity currentEntity, List<String> idsNoFound){
		this.selectedItems.setList(new ArrayList<ESciDocItem>());
		boolean escidocRespondOK = true;
		try{
			
			for (Relation rel : currentEntity.getTargetRelations()) {
				if (rel.getOwnValue().equals(rel_is_reference_of)) {
					Entity source = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					ESciDocItem item = getAppBean().getRefCache().getItem(source.getOwnValue());
					if(item == null){
						item = new ESciDocItem(source.getOwnValue());
						item.setErrorLoading(ESciDocItem.ESCIDOC_ERROR_ID_NO_FOUND);
						idsNoFound.add(source.getOwnValue());
					}
					
					/*Publication pub = ESciDocHandler.getPublicationById(source.getOwnValue());
					if(pub == null){
						pub = new Publication(source.getOwnValue());
						pub.setErrorLoading(Publication.ESCIDOC_ERROR_ID_NO_FOUND);
						idsNoFound.add(source.getOwnValue());
					}
					*/
					Attribute att = source.getAttributeByName("additional_information");
					if(att != null){
						item.getPublication().setAdditionalInformation(att.getOwnValue());
					}
					item.getPublication().setEntity(source);
					this.selectedItems.addESciDocItem(item);
				}
			}	
		}catch (Exception e) {
			escidocRespondOK = false;
		}
		this.reportEscidocError(escidocRespondOK, idsNoFound);
	}
	
	
	
	protected void reportEscidocError(boolean escidocRespondOK, List<String> idsNoFound){
		if(!escidocRespondOK){
			addGeneralMsg("The references could not be loaded. The server did not respond.");
			addGeneralMsg("Exception connecting to http://escidoc.mpiwg-berlin.mpg.de:8080 ");
		}
		
		if(!idsNoFound.isEmpty()){
			addGeneralMsg("The following references were not found in Pubman server:");
			StringBuilder sb = new StringBuilder();
			int count = 0;
			for(String id : idsNoFound){
				if(count > 0){
					sb.append(", ");
				}
				sb.append(id);
			}
			addGeneralMsg(sb.toString());
		}
		
	}
	
	protected void prepareEndNoteRefs2Save() throws Exception{
		//REFERENCE -> is_reference_of -> WITNESS
		this.entity.removeAllTargetRelationsByName(rel_is_reference_of);
		for(SelectableObject<Reference> so : this.endNoteRefTable.list){
			Reference ref = so.getObj();

			Entity entRef = ref.getEnt();
			getWrapper().saveAssertion(entRef, getUserName());
			//entity can be no persistent, therefore the assignment of the relation should be done after the save
			entRef.addSourceRelation(rel_is_reference_of, entity);
			//new Relation(entRef, entity, rel_is_reference_of);
		}
	}
	
	protected Entity prepareESciDocRefs2Save(Entity currentEntity) throws Exception{
		//REFERENCE -> is_reference_of -> WITNESS
		currentEntity.removeAllTargetRelationsByName(rel_is_reference_of);
		for(ESciDocItem item : this.selectedItems.getList()){
			Publication pub = item.getPublication();
			if(pub.getEntity() == null){
				Entity pubEntity = new Entity(Node.TYPE_ABOX, REFERENCE, false);
				pubEntity.setOwnValue(pub.getObjid());
				pubEntity.addAttribute(new Attribute("id", "escidoc-objid", pub.getObjid()));
				if(StringUtils.isNotEmpty(pub.getAdditionalInformation())){
					pubEntity.addAttribute(new Attribute("additional_information", "text", pub.getAdditionalInformation()));	
				}
				pubEntity = getWrapper().saveEntity(pubEntity, getUserName());	
				pub.setEntity(pubEntity);
			}else{
				if(pub.getEntity().getAttributeByName("additional_information") != null){
					pub.getEntity().getAttributeByName("additional_information").setOwnValue(pub.getAdditionalInformation());
				}else{
					pub.getEntity().addAttribute(new Attribute("additional_information", "text", pub.getAdditionalInformation()));
				}
				pub.setEntity(getWrapper().saveEntity(pub.getEntity(), getUserName()));	
			}
			Relation rel = new Relation(pub.getEntity(), currentEntity, rel_is_reference_of);
		}
		return currentEntity;
	}

	public static de.mpiwg.itgroup.ismi.util.guiComponents.Calendar updateCalendar(Attribute att){
		de.mpiwg.itgroup.ismi.util.guiComponents.Calendar calendar = null;
		if(att != null && StringUtils.isNotEmpty(att.getOwnValue())){
			calendar = new de.mpiwg.itgroup.ismi.util.guiComponents.Calendar(att.getOwnValue()); 
		}else{
			calendar = new de.mpiwg.itgroup.ismi.util.guiComponents.Calendar();
		}
		return calendar;
	}
	
	protected void loadAttributes(Entity entity) {
		attributes = new HashMap<String, String>();
		if (entity != null) {
			for (Attribute defAtt : getWrapper().getDefAttributes(this.defObjectClass)) {
				Attribute originalAtt = entity.getAttributeByName(defAtt
						.getOwnValue());
				String attValue = (originalAtt != null) ? originalAtt
						.getOwnValue() : "";
				attributes.put(defAtt.getOwnValue(), attValue);
			}
		} else {
			for (Attribute defAtt : getWrapper().getDefAttributes(this.defObjectClass)) {
				attributes.put(defAtt.getOwnValue(), "");
			}
		}
	}

	public void reset(){
		this.setSelectedSaveAsNew(false);
		this.setCurrentId("");
		this.setCurrentIdMsg("");
		this.setAttributes(new HashMap<String, String>());
		this.selectedItems = new SelectedESciDocItems();
		this.displayUrl = null;
		this.endNoteRefTable = new ReferenceTable();
	}
	
	public boolean isSelectedSaveAsNew() {
		return selectedSaveAsNew;
	}

	public void setSelectedSaveAsNew(boolean selectedSaveAsNew) {
		this.selectedSaveAsNew = selectedSaveAsNew;
	}

	/**
	 * <p>
	 * "Entity -> CurrentEntity"
	 * </p>
	 * <p>
	 * This method updates the attributes of the given entity with the
	 * attributes of the attributes Map<String, String>.
	 * </p>
	 * 
	 * @return the input's entity with updated attributes.
	 */
	public Entity updateEntityAttributes(Entity entity) {
		if (entity == null) {
			entity = new Entity(Node.TYPE_ABOX, false);
			//entity.setObjectClass(getDefinition().getOwnValue());
			entity.setObjectClass(this.defObjectClass);
		}
		//for (Attribute defAtt : getDefinition(entity).getAttributes()) {
		for (Attribute defAtt : getWrapper().getDefAttributes(this.defObjectClass)) {
			Attribute att = entity.getAttributeByName(defAtt.getOwnValue());
			if (StringUtils.isNotEmpty(this.attributes
					.get(defAtt.getOwnValue()))) {
				if (att == null) {
					att = new Attribute(defAtt);
					entity.addAttribute(att);
				}
				att.setOwnValue(this.attributes.get(defAtt.getOwnValue()));
				att.setObjectClass(defAtt.getOwnValue());
			} else if (att != null) {
				// the attribute must be deleted.
				entity.getAttributes().remove(att);
			}
		}
		return entity;
	}

	protected List<SelectItem> updateSuggestedPersonByRole(String searchWord, String objClass, String attName, String role){
		List<Attribute> attList = getWrapper().getAttributesByDefByAttName(objClass, attName, searchWord.toString(), -1);
		
		List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
		if (attList == null)
			return suggestedItems;

		int count = 0;
		for (Attribute att : attList) {
			if(getCache().roleContainsPersonId(role, att.getSourceId())){
				SelectItem item = new SelectItem(att, att.getOwnValue() + " [" + att.getSourceId() + "]","description: " + att);
				suggestedItems.add(item);
				count++;
				if(count == MAX_SUGGEST)
					break;
			}
		}
		return suggestedItems;
	}
	
	protected List<SelectItem> updateSuggestedItems(ValueChangeEvent event,
			String objClass, String attName) {
		return updateSuggestedItems((String) event.getNewValue(), objClass,
				attName);
	}

	protected List<SelectItem> updateSuggestedItems(String searchWord,
			String objClass, String attName) {

		List<Attribute> attList = getWrapper().getAttributesByDefByAttName(objClass, attName, searchWord.toString(), MAX_SUGGEST);
		
		List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
		if (attList == null)
			return suggestedItems;

		for (Attribute att : attList) {
			SelectItem item = new SelectItem(att, att.getOwnValue() + " [" + att.getSourceId() + "]",
					"description: " + att);
			suggestedItems.add(item);
		}
		return suggestedItems;
	}

	protected SimpleSearchCache getCache(){
		return getAppBean().getSimpleSearchCache();
	}
	
	protected List<SelectItem> updateSuggestedItems(String objClass,
			String firstName, String firstValue, String secondName,
			String secondValue) {

		List<Attribute> attList = 
			getWrapper().
			searchAttribute(firstName, firstValue, secondName, secondValue, objClass, MAX_SUGGEST);
			
		List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
		if (attList == null)
			return suggestedItems;

		for (Attribute att : attList) {
			SelectItem item = new SelectItem(att, att.getOwnValue() + " [" + att.getSourceId() + "]");
			suggestedItems.add(item);
		}
		return suggestedItems;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * <p>
	 * This method plays the role of a small cache to get quickly the
	 * definitions.
	 * </p>
	 * TODO this method must be implemented into a bean in session context and
	 * research by every beans.
	 * 
	 * @param assertion
	 * @return
	 */
	public Entity getLWDefinition(Entity assertion) {
		return getWrapper().getDefinition(assertion.getObjectClass());
	}
	
	public Entity getLWDefinition(String objClass) {
		return getWrapper().getDefinition(objClass);
	}
	
	/**
	 * <p>
	 * Returns the target's entity of a relation. The returned target is no
	 * light weight.
	 * <p>
	 * 
	 * @param rel
	 * @param target
	 * @return
	 */
	protected Entity getTargetRelation(Relation rel) {
		Entity target = rel.getTarget();
		if (target == null) {
			target = getWrapper().getEntityById(rel.getTargetId());
			rel.setTarget(target);
		} else if (target.isLightweight()) {
			target = getWrapper().getEntityContent(target);
		}
		return target;
	}

	protected Attribute getTargetAttribute(Entity target, String name) {
		Attribute att = getWrapper().getAttributeByName(target.getId(), name);
		
		if (att == null) {
			att = new Attribute();
			att.setOwnValue(target.getOwnValue());
		}
		return att;
	}

	protected WrapperService getWrapper() {
		return getAppBean().getWrapper();
	}
	
	/**
	 * <p>
	 * Return the <code>FacesContext</code> instance for the current request.
	 */
	protected FacesContext context() {
		return (FacesContext.getCurrentInstance());
	}
	
	public void setCreate_error(Boolean create_error) {
		this.create_error = create_error;
	}

	public Boolean getCreate_error() {
		return create_error;
	}

	/**
	 * Eine Liste <code>toBeRestricted</code> wird so eingeschraenkt, dass nur
	 * noch die jenigen uebrig bleiben, die �ber die Relation
	 * <code>relation</code> mit einem der Elemente aus
	 * <code>contraintIds</code> verbunden sind.
	 * 
	 * @param toBeRestricted
	 * @param constraintIds
	 * @param relation
	 * @param mode
	 * @return
	 */
	protected List<SelectItem> restrictList(List<SelectItem> toBeRestricted,
			List<String> constraintIds, String relation, String mode) {

		List<SelectItem> newList = new ArrayList<SelectItem>();

		for (SelectItem s : toBeRestricted) {
			Entity ent = getWrapper().getEntityById(
					Long.valueOf((String) s.getValue()));
			if (s.getValue().equals("0")) {
				newList.add(s);
				continue;
			}

			List<Relation> rels;
			if (mode.equals("target")) {
				rels = ent.getTargetRelations();
			} else if (mode.equals("source")) {
				rels = ent.getSourceRelations();
			} else {
				System.err.println("restrict List - mode not defined:" + mode);
				return null;
			}

			for (Relation r : rels) {
				if (r.getObjectClass().equals(relation)) {
					Long id;
					if (mode.equals("target")) {
						id = r.getSource().getId();
					} else if (mode.equals("source")) {
						id = r.getTarget().getId();
					} else {
						System.err.println("restrict List - mode not defined:"
								+ mode);
						return null;
					}
					if (constraintIds.contains(String.valueOf(id))) {
						newList.add(s);
					}
				}
			}
		}

		return newList;
	}

	public String getSelectedWitnessID() {
		return selectedWitnessID;
	}

	public void setShowWitness(boolean showWitness) {
		this.showWitness = showWitness;
	}

	public boolean isShowWitness() {
		return showWitness;
	}
	
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public boolean isShowTitle() {
		return showTitle;
	}
	/*
	public PanelPopup getPopup() {
		return popup;
	}

	public void setPopup(PanelPopup popup) {
		this.popup = popup;
	}

	public String getPopupText() {
		return popupText;
	}

	public void setPopupText(String popupText) {
		this.popupText = popupText;
	}*/

	
	public String getUserName(){
		User user = getSessionUser(); 
		if(user == null)
			return "";
		else
			return	user.getEmail();
	}
	
	public User getSessionUser() {
		SessionBean bean = getSessionBean();
		if(bean != null){
			return bean.getUser();	
		}else{
			return null;
		}
	}

	public void fillList(List<SelectItem> list, String[] array) {
		list.add(new SelectItem(""));
		for (int i = 0; i < array.length; i++) {
			list.add(new SelectItem(array[i]));
		}

	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setDisplayWarning(boolean displayWarning) {
		this.displayWarning = displayWarning;
	}

	public boolean isDisplayWarning() {
		return displayWarning;
	}

	public void registerChecker(StatusChecker sc) {
		statusChecker.add(sc);
	}

	public void registerChecker(ListenerObject lo, String message) {
		registerChecker(lo.statusImage, message);

	}

	public void registerChecker(StatusChecker sc, String message) {
		sc.setMessage(message);
		statusChecker.add(sc);
	}

	class CheckResults {
		List<String> errors = new ArrayList<String>();
		List<String> warnings = new ArrayList<String>();
		public boolean hasErrors = false;
		public boolean hasWarnings = false;

	}

	public CheckResults getCheckResults() {
		CheckResults cr = new CheckResults();

		for (StatusChecker sc : statusChecker) {
			if (sc.getStatus().equals("false")) {
				cr.hasErrors = true;
				cr.errors.add(sc.getMessage());
			} else if (sc.getStatus().equals("unset")) {
				cr.hasWarnings = true;
				cr.warnings.add(sc.getMessage());
			}
		}
		return cr;
	}

	protected ListenerObject changeValue(String ownvalue, ListenerObject lo, String suggestType, String suggestAttributeName,
			List<SelectItem> restrictedItems) {
		if (restrictedItems != null) {
			lo.suggestedItems = restrictedItems;}

		if (ownvalue == null || ownvalue.equals(""))
			lo.statusImage.setStatus("unset");
		else
			lo.statusImage.setStatus("false");
		lo.entity = null;
		// setze erst mal den Eigenwert auf das eingebene.
		Attribute at = new Attribute();
		at.setOwnValue(ownvalue);
		lo.attribute = at;
		Entity element = null;
		if (lo.suggestedItems != null) // schaue in der  liste der vorgebenen Ereignisse nach.
			for (SelectItem item : lo.suggestedItems) {
				if (item.getLabel().equals(ownvalue)) {
					if (Attribute.class.isInstance(item.getValue())){ // entweder es ist schon ein attribute
						lo.attribute = (Attribute) item.getValue();
						element = getWrapper().getEntityById(
							lo.attribute.getSourceId());
					}else{ //oder ein paar wert / id// = (Attribute) item.getValue();
						element = getWrapper().getEntityById(Long.valueOf((String) item.getValue()));
						lo.setEntityAndAttribute(element, suggestAttributeName);
					}
					if (element != null) {
						if(element.isLightweight()){
							element = getWrapper().getEntityByIdWithContent(element.getId());
						}
						lo.entity = element;
						lo.statusImage.setStatus("ok");
					}
					break;
				}

			} else {
				//juc *** List<Entity> ents = getWrapper().getLightweightConceptsByAttribute(null, suggestType, suggestAttributeName, ownvalue, 1, false);
				List<Entity> ents = getWrapper().getEntitiesByAtt(suggestType, suggestAttributeName, ownvalue, 1, false);
				if (ents.size()==1){
					lo.setEntityAndAttribute(ents.get(0),suggestAttributeName);
					lo.statusImage.setStatus("ok");
			}
				
		}
		return lo;

	}

	

	protected ListenerObject changeValuePersonByRole(ValueChangeEvent event, ListenerObject lo, String role){
		if (event.getNewValue() == null) {
			return lo;
		}else if(StringUtils.isEmpty(event.getNewValue().toString()) && (event.getOldValue() == null || StringUtils.isEmpty(event.getOldValue().toString()))){
			//if the old and new value are empty, then return
			return lo;
		}
		if (event.getNewValue().equals(event.getOldValue())) {
			return lo;
		}

		String ownValue = (String) event.getNewValue();
		
		if(StringUtils.isEmpty(ownValue))
			lo.statusImage.setStatus("unset");
		else
			lo.statusImage.setStatus("false");
		
		lo.entity = null;
		// setze erst mal den Eigenwert auf das eingebene.
		Attribute at = new Attribute();
		at.setOwnValue(ownValue);
		lo.attribute = at;
		for (SelectItem item : lo.suggestedItems) {
			if (StringUtils.isNotEmpty(item.getLabel()) && 
					item.getLabel().equals(ownValue)) {
				//System.out.println("item.getValue()= " + item.getValue());
				lo.attribute = (Attribute) item.getValue();
				Entity element = getWrapper().getEntityById(
						lo.attribute.getSourceId());

				if (element != null) {
					if(element.isLightweight()){
						element = getWrapper().getEntityByIdWithContent(element.getId());
					}
					lo.entity = element;
					lo.statusImage.setStatus(StatusImage.STATUS_OK);
				}
				break;
			}

		}
		
		//juc lo.suggestedItems = updateSuggestedPersonByRole(ownValue, PERSON, "name", role);
		lo.suggestedItems = updateSuggestedPersonByRole(ownValue, PERSON, "name_translit", role);
		
		
		return lo;
	}
	
	protected ListenerObject changeValue(String ownvalue, ListenerObject lo,
			String suggestType, String suggestAttributeName) {

		return changeValue(ownvalue, lo, suggestType, suggestAttributeName,
				null);
	}

	protected ListenerObject changeListener(ValueChangeEvent event,
			ListenerObject lo, String suggestType, String suggestAttributeName,
			String restrictingAttributeName, String restrictingAttributeValue) {
		try {
			if (event.getNewValue() == null) {
				return lo;
			}else if(StringUtils.isEmpty(event.getNewValue().toString()) && (event.getOldValue() == null || StringUtils.isEmpty(event.getOldValue().toString()))){
				//if the old and new value are empty, then return
				return lo;
			}
			if (event.getNewValue().equals(event.getOldValue())) {
				return lo;
			}

			String ownvalue = (String) event.getNewValue();

			if(StringUtils.isEmpty(ownvalue))
				lo.statusImage.setStatus(StatusImage.STATUS_UNSET);
			else
				lo.statusImage.setStatus(StatusImage.STATUS_FALSE);
			
			lo.entity = null;
			// setze erst mal den Eigenwert auf das eingebene.
			Attribute at = new Attribute();
			at.setOwnValue(ownvalue);
			lo.attribute = at;
			for (SelectItem item : lo.suggestedItems) {
				if (StringUtils.isNotEmpty(item.getLabel()) && 
						item.getLabel().equals(ownvalue)) {
					//System.out.println("item.getValue()= " + item.getValue());
					lo.attribute = (Attribute) item.getValue();
					Entity element = getWrapper().getEntityById(
							lo.attribute.getSourceId());

					if (element != null) {
						if(element.isLightweight()){
							element = getWrapper().getEntityByIdWithContent(element.getId());
						}
						lo.entity = element;
						lo.statusImage.setStatus(StatusImage.STATUS_OK);
					}
					break;
				}

			}

			if (restrictingAttributeName == null) {
				lo.suggestedItems = this.updateSuggestedItems(event,
						suggestType, suggestAttributeName);
			} else {
 				lo.suggestedItems = this.updateSuggestedItems(suggestType,
						suggestAttributeName, event.getNewValue().toString(),
						restrictingAttributeName, restrictingAttributeValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lo;
	}
	
	protected void generateSecundaryOW(Entity entity, String user) throws Exception{
		List<Entity> nodeList = new ArrayList<Entity>();
		
		long start = System.currentTimeMillis();
		if(entity.getObjectClass().equals(PLACE)){
			this.generateOW4City(entity, nodeList, user);
			long medium  = System.currentTimeMillis();
			System.out.println("nodeList.size(): " + nodeList.size() + " time: " + (medium - start));
			getWrapper().saveEntityListAsNodeWithoutContent(nodeList, user);
			System.out.println("Save time: " + (System.currentTimeMillis() - medium));
		}else if(entity.getObjectClass().equals(REPOSITORY)){
			this.generateOW4Repository(entity, nodeList, new HashMap<String, String>(), user);
			long medium  = System.currentTimeMillis();
			System.out.println("nodeList.size(): " + nodeList.size() + " time: " + (medium - start));
			getWrapper().saveEntityListAsNodeWithoutContent(nodeList, user);
			System.out.println("Save time: " + (System.currentTimeMillis() - medium));
		}else if(entity.getObjectClass().equals(COLLECTION)){
			this.generateOW4Collection(entity, nodeList, new HashMap<String, String>(), user);
			long medium  = System.currentTimeMillis();
			System.out.println("nodeList.size(): " + nodeList.size() + " time: " + (medium - start));
			getWrapper().saveEntityListAsNodeWithoutContent(nodeList, user);
			System.out.println("Save time: " + (System.currentTimeMillis() - medium));
		}else if(entity.getObjectClass().equals(CODEX)){
			this.generateOW4Codex(entity, nodeList, new HashMap<String, String>(), user);
			long medium  = System.currentTimeMillis();
			System.out.println("nodeList.size(): " + nodeList.size() + " time: " + (medium - start));
			getWrapper().saveEntityListAsNodeWithoutContent(nodeList, user);
			System.out.println("Save time: " + (System.currentTimeMillis() - medium));
		}else if(entity.getObjectClass().equals(TEXT)){
			generateOW4Text(entity, user);
			long medium  = System.currentTimeMillis();
			System.out.println("Saving witnessList time: " + (medium - start));
		}
		//TODO PERSON and TEXT
	}
	
	private void generateOW4City(Entity city, List<Entity> nodeList, String user){
		Map<String, String> map = new HashMap<String, String>();
		map.put(PLACE, city.getOwnValue());
		
		List<Entity> repoList = getWrapper().getSourcesForTargetRelation(city, is_in, REPOSITORY, -1);
		for(Entity repo : repoList){
			this.generateOW4Repository(repo, nodeList, map, user);
		}
	}
	
	private void generateOW4Repository(Entity repository, List<Entity> nodeList, Map<String, String> map, String user){
		if(map.size() == 0){
			List<Entity> placeList = getWrapper().getTargetsForSourceRelation(repository, is_in, PLACE, 1);	
			map.put(PLACE, ((placeList.size() > 0) ? placeList.get(0).getOwnValue() : ""));
		}
		
		map.put(REPOSITORY, repository.getOwnValue());
		List<Entity> collectionList = getWrapper().getSourcesForTargetRelation(repository, is_part_of, COLLECTION, -1);
		for(Entity collection : collectionList){
			this.generateOW4Collection(collection, nodeList, map, user);
		}
	}
	
	private void generateOW4Collection(Entity collection, List<Entity> nodeList, Map<String, String> map, String user){
		if(map.size() == 0){
			List<Entity> repoList = getWrapper().getTargetsForSourceRelation(collection, is_part_of, REPOSITORY, 1);
			map.put(REPOSITORY, (repoList.size() > 0) ? repoList.get(0).getOwnValue() : "");
			if(repoList.size() > 0){
				Entity repository = repoList.get(0);
				List<Entity> placeList = getWrapper().getTargetsForSourceRelation(repository, is_in, PLACE, 1);
				map.put(PLACE, (placeList.size() > 0) ? placeList.get(0).getOwnValue() : "");
			}else{
				map.put(REPOSITORY, "");
				map.put(PLACE, "");
			}
		}
		
		map.put(COLLECTION, collection.getOwnValue());
		List<Entity> list = getWrapper().getSourcesForTargetRelation(collection, is_part_of, CODEX, -1);
		for(Entity codex : list){
			//update ow codex
			Attribute att = getWrapper().getAttributeByName(codex.getId(), "identifier");
			if(att != null){
				codex.setOwnValue(collection.getOwnValue() + "_" + att.getOwnValue());
				nodeList.add(codex);
			}
			this.generateOW4Codex(codex, nodeList, map, user);
		}
	}
	
	private void generateOW4Codex(Entity codex, List<Entity> nodeList, Map<String, String> map, String user){
		if(map.size() == 0){
			List<Entity> collList = getWrapper().getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1);
			map.put(COLLECTION, (collList.size() > 0 ) ? collList.get(0).getOwnValue() : "");
			if(collList.size() > 0){
				Entity collection = collList.get(0);
				List<Entity> repoList = getWrapper().getTargetsForSourceRelation(collection, is_part_of, REPOSITORY, 1);
				map.put(REPOSITORY, (repoList.size() > 0) ? repoList.get(0).getOwnValue() : "");
				if(repoList.size() > 0){
					Entity repository = repoList.get(0);
					List<Entity> placeList = getWrapper().getTargetsForSourceRelation(repository, is_in, PLACE, 1);
					map.put(PLACE, (placeList.size() > 0) ? placeList.get(0).getOwnValue() : "");
				}else{
					map.put(PLACE, "");
				}
			}else{
				map.put(COLLECTION, "");
				map.put(REPOSITORY, "");
				map.put(PLACE, "");
			}
		}
		List<Entity> list = getWrapper().getSourcesForTargetRelation(codex, is_part_of, WITNESS, -1);
		String placeText = (StringUtils.isNotEmpty(map.get(PLACE))) ? "_" + map.get(PLACE) : "";
		String repoText = (StringUtils.isNotEmpty(map.get(REPOSITORY))) ? "_" + map.get(REPOSITORY) : "";
		String codexText = (codex != null) ? "_" + codex.getOwnValue() : "";
		
		for(Entity witness : list){
			List<Entity> textList = getWrapper().getTargetsForSourceRelation(witness, "is_exemplar_of", TEXT, -1);
			String textName = (textList.size() > 0) ? textList.get(0).getOwnValue() : "";
			witness.setOwnValue(textName + placeText + repoText + codexText);     
			nodeList.add(witness);
		}
	}
	
	private void generateOW4Text(Entity text, String user) throws Exception{
		List<Entity> witnessList = getWrapper().getSourcesForTargetRelation(text, "is_exemplar_of", WITNESS, -1);
		List<Entity> listToSave = new ArrayList<Entity>();
		for(Entity witness : witnessList){
			List<Entity> list = getWrapper().getTargetsForSourceRelation(witness, "is_part_of", "CODEX", 1);
			if(list.size() > 0){
				Entity codex = list.get(0);
				list = getWrapper().getTargetsForSourceRelation(codex, "is_part_of", "COLLECTION", 1);
				if(list.size() > 0){
					Entity collection = list.get(0);
					list = getWrapper().getTargetsForSourceRelation(collection, "is_part_of", "REPOSITORY", 1);
					if(list.size() > 0){
						Entity repository = list.get(0);
						list = getWrapper().getTargetsForSourceRelation(repository, "is_in", "PLACE", 1);
						if(list.size() > 0){
							Entity city = list.get(0);
							witness.setOwnValue(text.getOwnValue() + "_" + city.getOwnValue() + "_" + repository.getOwnValue() + "_" + codex.getOwnValue());
							listToSave.add(witness);
						}
					}
				}
			}
			getWrapper().saveEntityListAsNodeWithoutContent(listToSave, user);
		}
	}

	protected SecurityService getSecurityService() {
		return getAppBean().getSecurityService();
	}
	
	protected ListenerObject changeListener(ValueChangeEvent event,
			ListenerObject lo, String suggestType, String suggestAttributeName) {

		return changeListener(event, lo, suggestType, suggestAttributeName,
				null, null);
	}
	
	public String getCurrentId() {
		return currentId;
	}

	public void setCurrentId(String currentId) {
		this.currentId = currentId;
	}
	
	public String getCurrentIdMsg() {
		return currentIdMsg;
	}

	public void setCurrentIdMsg(String currentIdMsg) {
		this.currentIdMsg = currentIdMsg;
	}

	public SelectedESciDocItems getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(SelectedESciDocItems selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getDefObjectClass() {
		return defObjectClass;
	}

	public void setDefObjectClass(String defObjectClass) {
		this.defObjectClass = defObjectClass;
	}
}
