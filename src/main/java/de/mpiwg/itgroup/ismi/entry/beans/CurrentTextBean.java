package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.NormalizerUtils;
import org.mpi.openmind.repository.utils.RomanizationLoC;
import org.mpi.openmind.repository.utils.TransliterationUtil;
import org.mpi.openmind.security.bo.User;




import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.utils.PrivacityUtils;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.util.guiComponents.EndNoteMisattribution;
import de.mpiwg.itgroup.ismi.util.guiComponents.EndNoteMisattributionTable;
import de.mpiwg.itgroup.ismi.util.guiComponents.EntityList;
import de.mpiwg.itgroup.ismi.util.guiComponents.TargetMisattribution;
import de.mpiwg.itgroup.ismi.util.guiComponents.MisattributionDataTable;

public class CurrentTextBean extends AbstractISMIBean  implements Serializable{
	private static final long serialVersionUID = 1017399812886455381L;

	private static Logger logger = Logger.getLogger(CurrentTextBean.class);

	private String valueShortTitle;
	private Entity shortTitleAlias; // is now an alias of type "prime"
	private String romanizedTitle;
	private String romanizedPrimaAlias;

	private String valueTranslitTitle;
	private Entity translitTitleAlias; // is now an alias of type "prime"

	private ListenerObject authorLo = new ListenerObject(PERSON, "name_translit");
	//private String authorInfo;

	private ListenerObject placeLo = new ListenerObject(PLACE, "name");

	private ListenerObject dedicatedPersonLo = new ListenerObject(PERSON, "name_translit");

	private ListenerObject commentaryLo = new ListenerObject(TEXT, "full_title_translit");

	private ListenerObject translationLo = new ListenerObject(TEXT, "full_title_translit");

	private ListenerObject versionLo = new ListenerObject(TEXT, "full_title_translit");

	private static String PERSON_TYPE_CREATED_BY = "created_by";
	private static String PERSON_TYPE_DEDICATED_TO = "dedicated_to";
	private String personType;

	private Attribute attTitleAlias = new Attribute();
	private Map<Long, Boolean> deletedTitleAliases = new HashMap<Long, Boolean>();
	private String newTitleAlias;

	private Attribute attIncipitAlias = new Attribute();
	private Map<Long, Boolean> deletedIncipitAliases = new HashMap<Long, Boolean>();
	private String newIncipitAlias;

	private Attribute attExplicitAlias = new Attribute();
	private Map<Long, Boolean> deletedExplicitAliases = new HashMap<Long, Boolean>();
	private String newExplicitAlias;
	
	private Long idSubject;
	private Entity subject;

	public static String main_subject = "main_subject";
	public static String sub_subject = "sub_subject";
	public static String type = "type";
	public static String NO_SUBJECT = "NO_SUBJECT";
	
	private boolean searchTextDialogRendered = false;
	private String stringTitle;
	private String stringAuthor;
	private List<SelectItem> textsFound = new ArrayList<SelectItem>();

	private Long idTextSelected;

	private String COMMENTARY_CALLER = "Commentary";
	private String TRANSLATION_CALLER = "Translation";
	private String VERSION_CALLER = "Version";

	private String searchCaller;
	
	private transient Calendar creationDate;
	
	private String textAuthorName;
	private String textAuthorNameTranslit;
	
	private Boolean selectPersonRendered = false;
	private List<SelectItem> personList = new ArrayList<SelectItem>();
	private Long selectedPersonId;
	private boolean restrictedByRole = false;
	
	private EntityList aliasList;
	private EntityList incipitAliasList;
	private EntityList explicitAliasList;

	//private MisattributionDataTable misattDataTable;
	private transient EndNoteMisattributionTable misattTable;
	
	public CurrentTextBean() {
		this.reset();
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, TEXT, false);
		setDefObjectClass(TEXT);
		
		this.aliasList = new EntityList(ALIAS, "alias", getWrapper(), getUserName());
		this.incipitAliasList = new EntityList(ALIAS, "alias", getWrapper(), getUserName());
		this.explicitAliasList = new EntityList(ALIAS, "alias", getWrapper(), getUserName());
		//this.misattDataTable = new MisattributionDataTable(PERSON, "name_translit");
		this.misattTable = new EndNoteMisattributionTable();
		
		this.selectPersonRendered = false;
		this.selectedPersonId = null;
		this.personList = new ArrayList<SelectItem>();
		this.restrictedByRole = false;
		
		this.textAuthorName = new String();
		this.textAuthorNameTranslit = new String();
		
		this.valueShortTitle = null;
		this.shortTitleAlias = null; // is now an alias of type "prime"

		this.valueTranslitTitle = null;
		this.translitTitleAlias = null; // is now an alias of type "prime"

		//this.authorLo = new ListenerObject();
		this.authorLo.reset();
		//this.authorInfo = null;

		//this.placeLo = new ListenerObject();
		this.placeLo.reset();
		
		this.dedicatedPersonLo.reset();

		this.commentaryLo.reset();

		this.translationLo.reset();

		this.versionLo.reset();

		this.attTitleAlias = new Attribute();
		this.deletedTitleAliases = new HashMap<Long, Boolean>();
		this.newTitleAlias = null;

		this.attIncipitAlias = new Attribute();
		this.deletedIncipitAliases = new HashMap<Long, Boolean>();
		this.newIncipitAlias  = null;

		this.attExplicitAlias = new Attribute();
		this.deletedExplicitAliases = new HashMap<Long, Boolean>();
		this.newExplicitAlias = null;
		
		this.idSubject = null;
		this.subject = null;
		
		this.creationDate = new Calendar();
		

		registerChecker(authorLo, "Creator is not valid!");
		registerChecker(placeLo, "Place is not valid!");
		registerChecker(dedicatedPersonLo, "Place is not valid!");
		registerChecker(commentaryLo, "Commentary is not valid!");
		registerChecker(translationLo, "Translation is not valid!");
		registerChecker(versionLo, "Version is not valid!");
		
		//getPopup().setRendered(false);
	}

	public List<SelectItem> getPersonList() {
		return personList;
	}

	public void setPersonList(List<SelectItem> personList) {
		this.personList = personList;
	}

	public Boolean getSelectPersonRendered() {
		return selectPersonRendered;
	}

	public void setSelectPersonRendered(Boolean selectPersonRendered) {
		this.selectPersonRendered = selectPersonRendered;
	}

	/**
	 * Source's Relations # TEXT is_commentary_on TEXT # TEXT is_translation_of
	 * TEXT # TEXT is_version_of TEXT # TEXT was_created_by PERSON # TEXT
	 * was_created_in PLACE # TEXT was_dedicated_to PERSON # TEXT has_subject
	 * SUBJECT
	 * 
	 * Target's Relations # ALIAS is_alias_title_of TEXT # ALIAS
	 * is_alias_incipit_of TEXT
	 * 
	 * @param text
	 */
	@Override
	public void setEntity(Entity text) {
		this.reset();
		this.entity = text;
		if(this.entity.isPersistent()){
			setCurrentId(this.entity.getId().toString());
			if (text.isLightweight()) {
				this.entity = getWrapper().getEntityContent(this.entity);
				this.entity.setLightweight(false);
			}
			
			Attribute attCreationDate = this.entity.getAttributeByName("creation_date");
			if(attCreationDate != null && StringUtils.isNotEmpty(attCreationDate.getOwnValue())){
				this.creationDate = new Calendar(attCreationDate.getOwnValue()); 
			}else{
				this.creationDate = new Calendar();
			}
			
			this.loadAttributes(this.entity);//, getDefinition(this.entity));
			
			for (Relation rel : text.getSourceRelations()) {
				Entity target = null;
				if( rel.getOwnValue().equals(has_subject)){
					this.subject = getTargetRelation(rel);
					this.idSubject = subject.getId();
				} else if (rel.getOwnValue().equals("is_commentary_on")) {
					target = getTargetRelation(rel);
					this.commentaryLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals("is_translation_of")) {
					target = getTargetRelation(rel);
					this.translationLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals("is_version_of")) {
					target = getTargetRelation(rel);
					this.versionLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals("was_created_by")) {
					target = getTargetRelation(rel);
					this.authorLo.setEntityAndAttribute0(target);
					
					if(this.authorLo.attribute != null){
						this.textAuthorNameTranslit = this.authorLo.attribute.getValue();
					}
					
				} else if (rel.getOwnValue().equals("was_dedicated_to")) {
					target = getTargetRelation(rel);
					this.dedicatedPersonLo.setEntityAndAttribute0(target); 
						
				} else if (rel.getOwnValue().equals("was_created_in")) {
					target = getTargetRelation(rel);
					//rich this.placeLo.setEntityAndAttribute(target, "name");
					this.placeLo.setEntityAndAttribute0(target);					
				//} else if (rel.getOwnValue().equals(misattributed_to)) {
				} else if (rel.getOwnValue().equals("has_author_misattribution")) {
					target = getTargetRelation(rel);
					//String refId = (rel.getAttributeByName("reference_id") != null) ? rel.getAttributeByName("reference_id").getValue() : null;
					//String notes = (rel.getAttributeByName("notes") != null) ? rel.getAttributeByName("notes").getValue() : null;
					//target=person
					//this.misattDataTable.add(target, refId, notes);
					//this.misattTable.add(target, ref, rel);
					this.misattTable.load(target);
				}

			}
			for (Relation rel : text.getTargetRelations()) {

				if (rel.getOwnValue().equals("is_alias_title_of")) {
					Entity alias = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					this.aliasList.add(alias); 
				} else if (rel.getOwnValue().equals("is_alias_incipit_of")) {
					Entity alias = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					this.incipitAliasList.add(alias);
				} else if (rel.getOwnValue().equals("is_alias_explicit_of")) {
					Entity alias = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					this.explicitAliasList.add(alias);
				} else if (rel.getOwnValue().equals("is_prime_alias_title_of")) {
					Entity alias = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					this.shortTitleAlias = alias;
					this.valueShortTitle = alias.getAttributeByName("alias").getValue();
				}
			}
			
			this.loadEndNoteRefs();
			this.displayUrl = generateDisplayUrl(authorLo.entity, text, null, getAppBean().getRoot());
		}
	}

	/**
	 * Added by DW it is no used anymore
	 * 
	 * @param event
	 */
	public void translitTitleChangeListener(ValueChangeEvent event) {
		try {
			if (event.getNewValue() == null) {
				return;
			}
			if (event.getNewValue().equals(event.getOldValue())) {
				return;
			}

			String newName = (String) event.getNewValue();

			if (translitTitleAlias == null) {
				translitTitleAlias = new Entity(Node.TYPE_ABOX, "ALIAS", false);
			}

			Attribute attr = this.translitTitleAlias
					.getAttributeByName("alias");
			if (attr == null) {
				this.translitTitleAlias.addAttribute(new Attribute("alias",
						TEXT, newName));
			} else
				attr.setValue(newName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String translitTitleAction() {
		String pn = getAttributes().get("full_title");

		String translit = TransliterationUtil.getTransliteration(pn);
		getAttributes().put("full_title_translit", translit);

		return "translit";

	}

	public void showSearchTextCommentaryDialog(ActionEvent event) {
		this.setSearchTextDialogRendered(true);
		this.searchCaller = COMMENTARY_CALLER;
	}

	public void showSearchTextTranslationDialog(ActionEvent event) {
		this.setSearchTextDialogRendered(true);
		this.searchCaller = TRANSLATION_CALLER;
	}

	public void showSearchTextVersionDialog(ActionEvent event) {
		this.setSearchTextDialogRendered(true);
		this.searchCaller = VERSION_CALLER;
	}

	public void closeSearchTextDialog(ActionEvent event) {
		this.setSearchTextDialogRendered(false);
	}

	public void resetSearchText(ActionEvent event) {
		this.textsFound = new ArrayList<SelectItem>();
		this.stringAuthor = "";
		this.stringTitle = "";
	}

	public void takeVersion(ActionEvent event) {
		if(this.idTextSelected != null){
			Entity ent = getWrapper().getEntityById(idTextSelected);
			if(ent != null){
				if (VERSION_CALLER.equals(this.searchCaller)) {
					this.versionLo.setEntityAndAttribute0(ent);
					
				} else if (COMMENTARY_CALLER.equals(this.searchCaller)) {
					this.commentaryLo.setEntityAndAttribute0(ent);
					
				} else if (TRANSLATION_CALLER.equals(this.searchCaller)) {
					this.translationLo.setEntityAndAttribute0(ent);
				}
				this.searchTextDialogRendered = false;
			}
		}
	}

	public String getStringTitle() {
		return stringTitle;
	}

	public void setStringTitle(String stringTitle) {
		this.stringTitle = stringTitle;
	}

	public String getStringAuthor() {
		return stringAuthor;
	}

	public void setStringAuthor(String stringAuthor) {
		this.stringAuthor = stringAuthor;
	}

	public void searchText(ActionEvent event) {
		this.textsFound = new ArrayList<SelectItem>();
		
		if(StringUtils.isNotEmpty(stringTitle)){
			if (StringUtils.isNotEmpty(this.stringAuthor)) {
				String termAuthor = NormalizerUtils.normalize(stringAuthor);
				for(Entity title : getWrapper().getEntitiesByAtt(TEXT, "full_title_translit", stringTitle, -1, true)){
					for(Entity author : 
						getWrapper().getTargetsForSourceRelation(title, "was_created_by", PERSON, -1)){
						String authorOW = (StringUtils.isNotEmpty(author.getNormalizedOwnValue())) ? author.getNormalizedOwnValue() : "";
						if(authorOW.contains(termAuthor)){
							this.textsFound.add(
									new SelectItem(title.getId(), "Title [" + title.getId() + "]: " + title.getOwnValue()
											+ " - Author: " + author.getOwnValue()));
						}
					}
				}
			}else{
				for(Entity title : getWrapper().getEntitiesByAtt(TEXT, "full_title_translit", stringTitle, -1, true)){
					this.textsFound.add(
							new SelectItem(title.getId(), "Title: " + title.getOwnValue()));
				}
			}
		}else if (StringUtils.isNotEmpty(this.stringAuthor)) {
			String termAuthor = NormalizerUtils.normalize(stringAuthor);
			for(Entity author : getCache().getPersonListByRole("Author")){
				String authorOW = (StringUtils.isNotEmpty(author.getNormalizedOwnValue())) ? author.getNormalizedOwnValue() : "";
				if(authorOW.contains(termAuthor)){
					for(Entity title : 
						getWrapper().getSourcesForTargetRelation(author, "was_created_by", TEXT, -1)){
						this.textsFound.add(
								new SelectItem(title.getId(), "Author[" + author.getId() + "]: " + author.getOwnValue()
								+ " - Title: " + title.getOwnValue()));
					}	
				}
			}	
		}
	}
	
	public void restrictedByRoleChange(ValueChangeEvent event) {
		if (event.getNewValue().equals(event.getOldValue()))
			return;
		Boolean val = (Boolean) event.getNewValue();
		this.setRestrictedByRole(val);
		this.updatePersonList();
	}

	public void listenerRomanizeTitleTranslit(AjaxBehaviorEvent event){
		if(getAttributes().get("full_title_translit") != null)
			this.romanizedTitle = RomanizationLoC.convert(getAttributes().get("full_title_translit"));
	}
	
	public void listenerRomanizePrimaAlias(AjaxBehaviorEvent event){
		if(valueShortTitle != null){
			this.romanizedPrimaAlias = RomanizationLoC.convert(valueShortTitle);
		}
	}
	
	public void listenerShowAllAuthors(ActionEvent event) {
		this.setPersonType(PERSON_TYPE_CREATED_BY);
		this.updatePersonList();
	}
	
	public void listenerShowAllDedicatedToPersons(ActionEvent event) {
		this.setPersonType(PERSON_TYPE_DEDICATED_TO);
		this.updatePersonList();
	}
	
	private void updatePersonList(){
		if (!restrictedByRole)
			this.personList = getCache().getAllPersons();
		else {
			this.personList = getCache().getPersonsByRole("Author");
		}
		this.selectPersonRendered = true;
	}
	
	public void listenerPersonCancel(ActionEvent event) {
		this.selectPersonRendered = false;
	}

	public void listenerSelectPerson(ActionEvent event){
		if(this.selectedPersonId != null){
			Entity selectedPerson = getWrapper().getEntityById(selectedPersonId);
			if(selectedPerson != null){
				if(this.personType.equals(PERSON_TYPE_CREATED_BY)){
					this.authorLo.setEntityAndAttribute0(selectedPerson);
					this.authorLo.entityInfo = "ID = " + authorLo.getEntity().getId();
					
					Attribute attArabicName = getTargetAttribute(selectedPerson, "name");
					if(attArabicName != null){
						this.authorLo.entityInfo += ", Arabic Name = " + attArabicName.getOwnValue();
						this.textAuthorName = attArabicName.getValue();
					}
					
					if(this.authorLo.attribute != null){
						this.textAuthorNameTranslit = this.authorLo.attribute.getValue();
					}
				}else if(this.personType.equals(PERSON_TYPE_DEDICATED_TO)){
					this.dedicatedPersonLo.setEntityAndAttribute0(selectedPerson);
				}
			}
		}
		this.setSelectPersonRendered(false);
	}
	
	public boolean mandatoryEntriesOK(){
		boolean ok = true;
		if(this.authorLo.entity == null || !this.authorLo.entity.isPersistent()){
			addGeneralMsg("<Created by> is a mandatory entry. If you do not know the author of this text, you should select the person <ANONYMOUS>.");
			ok = false;
		}
		return ok;
	}

	@Override
	public String save() {
		super.save();
		try {
			
			if(!mandatoryEntriesOK()){
				addGeneralMsg("The entity could not be saved.");
				return PAGE_EDITOR;
			}
			
			User user = getSessionUser();

			CheckResults cr = getCheckResults();
			if (cr.hasErrors) {
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				return PAGE_EDITOR;
			}

			
			
			getAttributes().put("creation_date", this.creationDate.toJSONString());
			
			this.entity = updateEntityAttributes(this.entity);
			
			this.entity.replaceSourceRelation(commentaryLo.entity, TEXT, "is_commentary_on");
			
			this.entity.replaceSourceRelation(translationLo.entity, TEXT, "is_translation_of");
			
			this.entity.replaceSourceRelation(versionLo.entity, TEXT, "is_version_of");
			
			this.entity.replaceSourceRelation(authorLo.entity, PERSON, "was_created_by");
			
			this.entity.replaceSourceRelation(dedicatedPersonLo.entity, PERSON, "was_dedicated_to");
			
			this.entity.replaceSourceRelation(placeLo.entity, PLACE, "was_created_in");
			
			this.entity.removeAllSourceRelations(has_subject, SUBJECT);
			if(getIdSubject() != null){
				this.subject = getWrapper().getEntityByIdWithContent(getIdSubject());
				this.entity.replaceSourceRelation(subject, SUBJECT, has_subject);
			}

			
			//is_prime_alias_title_of
			if(!StringUtils.isEmpty(valueShortTitle)){
				this.entity.removeAllTargetRelations("is_prime_alias_title_of", ALIAS);
			
				if(this.shortTitleAlias == null){
					//1)create  alias, 2) update value of alias3) save alias, and 4) add to this text.
					shortTitleAlias = new Entity(Node.TYPE_ABOX, ALIAS, false);
					shortTitleAlias.addAttribute(new Attribute("alias", "text", this.valueShortTitle));
					getWrapper().saveEntity(shortTitleAlias, getUserName());					
				}else{
					//1) update value, 2) re-save alias
					this.shortTitleAlias.getAttributeByName("alias").setOwnValue(this.valueShortTitle);
					this.shortTitleAlias.removeAllSourceRelations("is_prime_alias_title_of", TEXT);
					getWrapper().saveEntity(shortTitleAlias, getUserName());
				}
				Relation aliasRel = new Relation(shortTitleAlias, this.entity, "is_prime_alias_title_of");
			}
			
			/*
			if (this.shortTitleAlias == null
					|| (shortTitleAlias.getAttributeByName("alias") != null && StringUtils
							.isEmpty(shortTitleAlias
									.getAttributeByName("alias").getOwnValue()))) {
				this.entity.removeAllTargetRelations("is_prime_alias_title_of",
						ALIAS);
			} else if (this.shortTitleAlias != null) {
				shortTitleAlias = getWrapper().saveEntity(shortTitleAlias, user.getEmail());
				this.entity.setAsUniqueTargetRelation("is_prime_alias_title_of",
						this.shortTitleAlias);
			}
			*/
			
			//ALIAS -> is_alias_title_of -> TEXT
			this.entity.removeAllTargetRelationsByName("is_alias_title_of");
			for(Entity alias : this.aliasList.getEntities()){
				Entity alias0 = getWrapper().getEntityByIdWithContent(alias.getId());
				Relation aliasRel = new Relation(alias0, this.entity, "is_alias_title_of");
			}
			
			//saveAliases2(incipitAliases, deletedIncipitAliases,
			//		"is_alias_incipit_of");
			//deletedIncipitAliases = refreshAliasMap(incipitAliases, deletedIncipitAliases);
			this.entity.removeAllTargetRelationsByName("is_alias_incipit_of");
			for(Entity alias : this.incipitAliasList.getEntities()){
				Entity alias0 = getWrapper().getEntityByIdWithContent(alias.getId());
				Relation aliasRel = new Relation(alias0, this.entity, "is_alias_incipit_of");
			}
			
			/*
			saveAliases2(explicitAliases, deletedExplicitAliases,
					"is_alias_explicit_of");
			deletedExplicitAliases = refreshAliasMap(explicitAliases, deletedExplicitAliases);
			*/
			this.entity.removeAllTargetRelationsByName("is_alias_explicit_of");
			for(Entity alias : this.explicitAliasList.getEntities()){
				Entity alias0 = getWrapper().getEntityByIdWithContent(alias.getId());
				Relation aliasRel = new Relation(alias0, this.entity, "is_alias_explicit_of");
			}
			
			//String lastAction = "";
			
			//REFERENCE -> is_reference_of -> THIS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();
			
			this.entity.removeAllSourceRelations(EndNoteMisattribution.HAS_AUTHOR_MISATT, EndNoteMisattribution.MISATT);
			
			this.entity = getWrapper().saveEntity(this.entity, user.getEmail());
			this.getAppBean().setAllTextsAsDirty();
			getAppBean().getSimpleSearchCache().setMapDirty(true);
			
			this.entity = this.misattTable.saveMisattributions(this.entity);
			
			this.setEntity(this.entity);
			//lastAction = "save text";


			this.generateSecundaryOW(this.entity, getSessionUser().getEmail());

			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();

			//setActionInfo(lastAction);

			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		saveEnd();
		return PAGE_EDITOR;
	}

	public EntityList getIncipitAliasList() {
		return incipitAliasList;
	}

	public void setIncipitAliasList(EntityList incipitAliasList) {
		this.incipitAliasList = incipitAliasList;
	}

	/**
	 * This method saves the text as new entity, and its related alias and
	 * witness.
	 * 
	 * @return
	 */
	public String saveAsNewEntity() {
		
		try {
			User user = getSessionUser();

			CheckResults cr = getCheckResults();
			if (cr.hasErrors) {
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				return "SAVE_ERROR";
			}

			
			this.entity = updateEntityAttributes(this.entity);
			//text.setOwnValue(createOwnValue());
			
			//text.removeSourceRelation("is_commentary_of", commentaryLo.entity);
			//replaceSourceRelation(text, commentaryLo.entity, TEXT, "is_commentary_on");
			this.entity.replaceSourceRelation(commentaryLo.entity, TEXT, "is_commentary_on");

			//text.removeSourceRelation("is_translation_of", translationLo.entity);
			//replaceSourceRelation(text, translationLo.entity, TEXT, "is_translation_of");
			this.entity.replaceSourceRelation(translationLo.entity, TEXT, "is_translation_of");
			
			//text.removeSourceRelation("is_version_of", versionLo.entity);
			//replaceSourceRelation(text, versionLo.entity, TEXT, "is_version_of");
			this.entity.replaceSourceRelation(versionLo.entity, TEXT, "is_version_of");
			
			//text.removeSourceRelation("was_created_by", authorLo.entity);
			//replaceSourceRelation(text, authorLo.entity, PERSON, "was_created_by");
			this.entity.replaceSourceRelation(authorLo.entity, PERSON, "was_created_by");
			
			//text.removeSourceRelation("was_dedicated_to", dedicatedPersonLo.entity);
			//replaceSourceRelation(text, dedicatedPersonLo.entity, PERSON,
			//		"was_dedicated_to");
			this.entity.replaceSourceRelation(dedicatedPersonLo.entity, PERSON, "was_dedicated_to");
			
			//text.removeSourceRelation("was_created_in", placeLo.entity);
			//replaceSourceRelation(text, placeLo.entity, PLACE, "was_created_in");
			this.entity.replaceSourceRelation(placeLo.entity, PLACE, "was_created_in");
			
			//String lastAction = "";

			// removing transiently the target relation, 
			// because they should not be saved as new
			this.entity.removeAllTargetRelations("is_exemplar_of", "WITNESS");
			this.entity.removeAllTargetRelations("is_prime_alias_title_of", "ALIAS");
			this.entity.removeAllTargetRelations("is_alias_title_of", "ALIAS");
			this.entity.removeAllTargetRelations("is_alias_incipit_of", "ALIAS");
			this.entity.removeAllTargetRelations("is_alias_explicit_of", "ALIAS");

			
			this.entity = getWrapper().saveEntityAsNew(this.entity, user.getEmail());
			//((AllTextsBean) this.getSessionBean("AllTexts")).setAllTextsAsDirty();
			this.getAppBean().setAllTextsAsDirty();
			getAppBean().getSimpleSearchCache().setMapDirty(true);

			//lastAction = "save text as new entity";
			getSessionBean().setEditFormCurrentEntId(this.entity.getId());

			logger.info("The entity was saved successfully!!!" + this.entity);
			//this.saveAsNew = false;

			//setActionInfo(lastAction);

			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		return PAGE_EDITOR;
	}

	/**
	 * Refresh aliasMap: delete the tuples, which are ticked to be deleted.
	 * 
	 * @param map
	 * @return
	 */
	private Map<Long, Boolean> refreshAliasMap(List<Entity> list, Map<Long, Boolean> aliasesMap){
		for(Long id : aliasesMap.keySet()){
			if(aliasesMap.get(id)){
				aliasesMap.remove(id);
				Entity alias = getEntityById(list, id);
				if(alias != null){
					list.remove(alias);
				}
			}
		}	
		return aliasesMap;
	}

	public Attribute getAttTitleAlias() {
		return attTitleAlias;
	}

	public void setAttTitleAlias(Attribute attTitleAlias) {
		this.attTitleAlias = attTitleAlias;
	}

	public Attribute getAttIncipitAlias() {
		return attIncipitAlias;
	}

	public void setAttIncipitAlias(Attribute attIncipitAlias) {
		this.attIncipitAlias = attIncipitAlias;
	}

	public Attribute getAttExplicitAlias() {
		return attExplicitAlias;
	}

	public void setAttExplicitAlias(Attribute attExplicitAlias) {
		this.attExplicitAlias = attExplicitAlias;
	}

	public String getValueShortTitle() {
		return valueShortTitle;
	}

	public void setValueShortTitle(String valueShortTitle) {
		this.valueShortTitle = valueShortTitle;
	}

	public Entity getShortTitleAlias() {
		return shortTitleAlias;
	}

	public void setShortTitleAlias(Entity shortTitleAlias) {
		this.shortTitleAlias = shortTitleAlias;
	}

	public String getValueTranslitTitle() {
		return valueTranslitTitle;
	}

	public void setValueTranslitTitle(String valueTranslitTitle) {
		this.valueTranslitTitle = valueTranslitTitle;
	}

	public Entity getTranslitTitleAlias() {
		return translitTitleAlias;
	}

	public void setTranslitTitleAlias(Entity translitTitleAlias) {
		this.translitTitleAlias = translitTitleAlias;
	}

	public void setNewTitleAlias(String newAlias) {
		this.newTitleAlias = newAlias;
	}

	public String getNewTitleAlias() {
		return newTitleAlias;
	}

	/**
	 * <p>This method saves transiently the aliases as new Entities, which are not marked to be deleted 
	 * in aliasesMap.</p>
	 * 
	 * @param aliases list of aliases showing in the interface.
	 * @param aliasesMap map id - boolean, which indicates if the alias[id] should be deleted(true).
	 * @param aliasRelationName name of the relation which links this text whith the given alias list.
	 * @param user
	 */
	/*
	private void saveAliasesAsNew(List<Entity> aliases,
			Map<Long, Boolean> aliasesMap, String aliasRelationName, String user) {
		
		//first step: save the alias, if it is ticked as 'no delete'
		for(Long aliasId : aliasesMap.keySet()){
			if(!aliasesMap.get(aliasId)){
				Entity alias = getEntityById(aliases, new Long(aliasId));
				if(alias != null){
					alias.removeAllSourceRelations(aliasRelationName, TEXT);
					Relation rel = new Relation(alias, this.entity, aliasRelationName);
					getWrapper().saveEntityAsNew(alias, user);	
				}	
			}
		}
	}*/
	
	/**
	 * <p>This method removes transiently the aliases, which are marked to be deleted
	 * and insert to the text the alias, which are not yet contained.</p> 
	 * 
	 * @param aliases list of aliases showing in the interface.
	 * @param aliasesMap map id - boolean, which indicates if the alias[id] should be deleted(true).
	 * @param aliasRelationName name of the relation which links this text whith the given alias list.
	 */
	/*
	private void saveAliases2(List<Entity> aliases,
			Map<Long, Boolean> aliasesMap, String aliasRelationName) {
		
		List<Relation> aliasRelList = this.entity.getTargetRelations(aliasRelationName, "ALIAS");
		
		//first step: delete aliases, which are ticked to be deleted and are contained by the current text
		for(Relation aliasRel : aliasRelList){
			//if the alias Map does not contain the 'relation source' or it is marked to be deleted, 
			//the relation should be removed from the text
			if(!aliasesMap.containsKey(aliasRel.getSourceId()) || aliasesMap.get(aliasRel.getSourceId())){
				text.removeTargetRelation(aliasRel);
			}
		}
		
		//second step: insert alias (as relation) to the text, 
		//if the alias is not contained by the text
		for(Long aliasId : aliasesMap.keySet()){
			if(!aliasesMap.get(aliasId)){
				if(!text.containsTargetRelation(aliasRelationName, new Long(aliasId))){
					Entity alias = getEntityById(aliases, new Long(aliasId));
					if(alias != null){
						Relation rel = new Relation(alias, this.entity, aliasRelationName);
					}
				}	
			}
		}
	}*/
	
	/**
	 * TODO move this method to some util's package
	 * @return
	 */
	private Entity getEntityById(List<Entity> list, Long id){
		for(Entity e : list){
			if(e.getId().compareTo(id) == 0){
				return e;
			}
		}		
		return null;
	}

	public Map<Long, Boolean> getDeletedIncipitAliases() {
		return deletedIncipitAliases;
	}

	public void setDeletedIncipitAliases(
			Map<Long, Boolean> deletedIncipitAliases) {
		this.deletedIncipitAliases = deletedIncipitAliases;
	}

	public Map<Long, Boolean> getDeletedExplicitAliases() {
		return deletedExplicitAliases;
	}

	public void setDeletedExplicitAliases(
			Map<Long, Boolean> deletedExplicitAliases) {
		this.deletedExplicitAliases = deletedExplicitAliases;
	}

	public String getNewIncipitAlias() {
		return newIncipitAlias;
	}

	public void setNewIncipitAlias(String newIncipitAlias) {
		this.newIncipitAlias = newIncipitAlias;
	}

	public String getNewExplicitAlias() {
		return newExplicitAlias;
	}

	public void setNewExplicitAlias(String newExplicitAlias) {
		this.newExplicitAlias = newExplicitAlias;
	}

	public Map<Long, Boolean> getDeletedTitleAliases() {
		return deletedTitleAliases;
	}

	public void setDeletedTitleAliases(Map<Long, Boolean> deletedTitleAliases) {
		this.deletedTitleAliases = deletedTitleAliases;
	}

	public boolean isSearchTextDialogRendered() {
		return searchTextDialogRendered;
	}

	public void setSearchTextDialogRendered(boolean searchTextDialogRendered) {
		this.searchTextDialogRendered = searchTextDialogRendered;
	}

	public void setTextsFound(List<SelectItem> textsFound) {
		this.textsFound = textsFound;
	}

	public List<SelectItem> getTextsFound() {
		return textsFound;
	}

	public Long getIdTextSelected() {
		return idTextSelected;
	}

	public void setIdTextSelected(Long idTextSelected) {
		this.idTextSelected = idTextSelected;
	}

	public String getSearchCaller() {
		return searchCaller;
	}

	public void setSearchCaller(String searchCaller) {
		this.searchCaller = searchCaller;
	}
	public ListenerObject getAuthorLo() {
		return authorLo;
	}

	public void setAuthorLo(ListenerObject authorLo) {
		this.authorLo = authorLo;
	}

	public ListenerObject getPlaceLo() {
		return placeLo;
	}

	public void setPlaceLo(ListenerObject placeLo) {
		this.placeLo = placeLo;
	}

	public ListenerObject getDedicatedPersonLo() {
		return dedicatedPersonLo;
	}

	public void setDedicatedPersonLo(ListenerObject dedicatedPersonLo) {
		this.dedicatedPersonLo = dedicatedPersonLo;
	}

	public ListenerObject getCommentaryLo() {
		return commentaryLo;
	}

	public void setCommentaryLo(ListenerObject commentaryLo) {
		this.commentaryLo = commentaryLo;
	}

	public ListenerObject getTranslationLo() {
		return translationLo;
	}

	public void setTranslationLo(ListenerObject translationLo) {
		this.translationLo = translationLo;
	}

	public ListenerObject getVersionLo() {
		return versionLo;
	}

	public void setVersionLo(ListenerObject versionLo) {
		this.versionLo = versionLo;
	}

	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	public Entity getSubject() {
		return subject;
	}

	public void setSubject(Entity subject) {
		this.subject = subject;
	}

	public String getTextAuthorName() {
		return textAuthorName;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public void setTextAuthorName(String textAuthorName) {
		this.textAuthorName = textAuthorName;
	}

	public String getTextAuthorNameTranslit() {
		return textAuthorNameTranslit;
	}

	public void setTextAuthorNameTranslit(String textAuthorNameTranslit) {
		this.textAuthorNameTranslit = textAuthorNameTranslit;
	}

	public boolean isRestrictedByRole() {
		return restrictedByRole;
	}

	public void setRestrictedByRole(boolean restrictedByRole) {
		this.restrictedByRole = restrictedByRole;
	}
	
	
	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public EntityList getAliasList() {
		return aliasList;
	}

	public void setAliasList(EntityList aliasList) {
		this.aliasList = aliasList;
	}

	public EntityList getExplicitAliasList() {
		return explicitAliasList;
	}

	public void setExplicitAliasList(EntityList explicitAliasList) {
		this.explicitAliasList = explicitAliasList;
	}

	public Long getSelectedPersonId() {
		return selectedPersonId;
	}

	public void setSelectedPersonId(Long selectedPersonId) {
		this.selectedPersonId = selectedPersonId;
	}

	public EndNoteMisattributionTable getMisattTable() {
		return misattTable;
	}

	public void setMisattTable(EndNoteMisattributionTable misattTable) {
		this.misattTable = misattTable;
	}

	public String getRomanizedTitle() {
		return romanizedTitle;
	}

	public void setRomanizedTitle(String romanizedTitle) {
		this.romanizedTitle = romanizedTitle;
	}

	public String getRomanizedPrimaAlias() {
		return romanizedPrimaAlias;
	}

	public void setRomanizedPrimaAlias(String romanizedPrimaAlias) {
		this.romanizedPrimaAlias = romanizedPrimaAlias;
	}
}
