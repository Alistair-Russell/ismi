package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.security.bo.User;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessAuthorLO;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessTextLO;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.util.guiComponents.EntityList;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;

public class CurrentWitnessBean extends CodexEditorTemplate implements Serializable{
	
	private static final long serialVersionUID = -7645136722251494419L;

	private static Logger logger = Logger.getLogger(CurrentWitnessBean.class);
	
	private Boolean codicesOverviewRendered = false;
	private List <CodexForList> codicesAll = new ArrayList<CodexForList>();
	
	private List<SelectItem> titles_list = new ArrayList<SelectItem>();
	private Long selectedTitleId;
	private Boolean selectTitleRendered = false;
	private String selectPersonType;

	private List<SelectItem> persons_list = new ArrayList<SelectItem>();
	private Long selectedPersonId;
	private Boolean selectPersonRendered = false;
	private Boolean restrictedByRole = true;

	//private Entity witness;
	
	private ListenerObject titleLo = new WitnessTextLO(TEXT, full_title_translit, this);
	
	private String valueTitle = "";
	
	// WITNESS -> has_title_written_as -> ALIAS
	private String valueTextWritten = "";
	private List<SelectItem> suggestedTitlesWritten = new ArrayList<SelectItem>();
	private Long valueTitleSelectedId;
	
	// WITNESS -> has_authorWritten_as -> ALIAS
	private String valueAuthorWritten = "";
	private List<SelectItem> suggestedAuthorsWritten = new ArrayList<SelectItem>();
	private Long valueAuthorSelectedId;
	
	// WITNESS -> is_exemplar_of -> TEXT -> was_created_by -> PERSON
	private ListenerObject authorLo = new WitnessAuthorLO(PERSON, name_translit, this);
	private String valueAuthor = "";
	
	private boolean textUnknown = false;
	private static String UNKNOWN = "UNKNOWN";

	
	// WITENSS -> was_copied_by -> PERSON
	private ListenerObject copyistLo = new ListenerObject(PERSON, name_translit);

	// WITENSS -> was_copied_at -> PLACE
	private ListenerObject copyPlaceLo = new ListenerObject(PLACE, name);

	// WITNESS -> xx -> PERSON
	private ListenerObject patronageLo = new ListenerObject(PERSON, name_translit);

	//private List<SelectItem> citiesWithRepositories;
	private boolean foundCodex;

	private boolean lockValueAuthor = false;
	private boolean lockValueTitle = false;
	
	private boolean createCodexRendered = false;
	private boolean renderShowAllCodexCandidates = false;
	//private Entity witness_old;
	
	private String newIdentifier;

	//private IslamicCalendar islamicCalCopyDate;
	private transient Calendar copyDate;
	
	private EntityList studiedByList;
	private EntityList possibleExamplerOfList;

	public CurrentWitnessBean() {
		super();
		this.entity = new Entity(Node.TYPE_ABOX, WITNESS, false);
		setDefObjectClass(WITNESS);
		
	    registerChecker(copyistLo.statusImage, "Copyist not valid");
	    registerChecker(copyPlaceLo.statusImage, "Copyist not valid");
	    registerChecker(patronageLo.statusImage,"Patron not valid");
	    
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, WITNESS, false);
		this.studiedByList = new EntityList(PERSON, "name_translit", "Studied by");
		this.possibleExamplerOfList = new EntityList(TEXT, "full_title_translit", "Possible titles");
		
		this.codicesOverviewRendered = false;
		this.codicesAll = new ArrayList<CodexForList>();
		
		this.titles_list = new ArrayList<SelectItem>();
		this.selectedTitleId = null;
		this.selectTitleRendered = false;
		this.selectPersonType = "";

		this.persons_list = new ArrayList<SelectItem>();
		this.selectedPersonId = null;
		this.selectPersonRendered = false;
		this.restrictedByRole = true;
		
		if(titleLo != null)
			this.titleLo.reset();
		
		this.valueTitle = "";

		// WITNESS -> has_title_written_as -> ALIAS
		this.valueTextWritten = "";
		this.suggestedTitlesWritten = new ArrayList<SelectItem>();
		this.valueTitleSelectedId = null;
		
		// WITNESS -> has_author_written_as -> ALIAS
		this.valueAuthorWritten = "";
		this.suggestedAuthorsWritten = new ArrayList<SelectItem>();
		this.valueAuthorSelectedId = null;
		
		// WITNESS -> is_exemplar_of -> TEXT -> was_created_by -> PERSON
		if(this.authorLo != null)
			this.authorLo.reset();
		this.valueAuthor = "";
		
		// WITENSS -> was_copied_by -> PERSON
		if(copyistLo != null)
			this.copyistLo.reset();

		// WITENSS -> was_copied_in -> PLACE
		if(this.copyPlaceLo != null)
			this.copyPlaceLo.reset();

		// WITNESS -> xx -> PERSON
		if(patronageLo != null)
			this.patronageLo.reset();
		
		//this.citiesWithRepositories = null;
		this.foundCodex = false;

		this.lockValueAuthor = true;
		this.lockValueTitle = true;
		
		this.createCodexRendered = false;
		this.renderShowAllCodexCandidates = false;
		
		this.newIdentifier = "";
		
		//this.islamicCalCopyDate = new IslamicCalendar();
		this.copyDate = new Calendar();
	}
	
	@Override
	public String save() {
		super.save();
		try {
			
			User user = getSessionUser();

			if(!isCodexIdentifierSet(user.getEmail())){
				return "SAVE_ERROR";
			}
			
			if(!isWitnessConsistentBeforeSave()){
				return "SAVE_ERROR";
			}
			
			//checking if some LO is not OK.
			CheckResults cr = getCheckResults();
			if (cr.hasErrors){
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				this.setSelectedSaveAsNew(false);
				return "SAVE_ERROR";
			}
					
			getAttributes().put("creation_date", this.copyDate.toJSONString());
			
			this.entity = this.updateEntityAttributes(this.entity);

			// WITNESS -> is_part_of -> CODEX
			this.entity.replaceSourceRelation(getCodexLo().entity, CODEX, is_part_of);
			
			// WITNESS -> is_exemplar_of -> TEXT -> was_created_by -> PERSON
			//replaceSourceRelation(this.entity, this.author, PERSON, was_created_by);

			// WITENSS -> was_copied_by -> PERSON
			this.entity.replaceSourceRelation(this.copyistLo.entity, PERSON, rel_was_copied_by);
			
			//WITENSS -> was_copied_in -> PLACE
			this.entity.replaceSourceRelation(this.copyPlaceLo.entity, PLACE, "was_copied_in");
			
			//REFERENCE -> is_reference_of -> WITNESS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();
			
			//WITNESS -> was studied by manyToMany -> PERSON
			this.entity.removeAllSourceRelationsByName(rel_was_studied_by);
			for(Entity target : this.studiedByList.getEntities()){
				Entity target0 = getWrapper().getEntityByIdWithContent(target.getId());
				Relation wasStudiedBy = new Relation(this.entity, target0, rel_was_studied_by);
			}

			// WITNESS -> had_patron -> PERSON
			this.entity.replaceSourceRelation(this.patronageLo.entity, PERSON, rel_had_patron);
			
			
			// WITNESS -> is_exemplar_of -> TEXT
			this.entity.replaceSourceRelation(this.titleLo.entity, TEXT, is_exemplar_of);
			
			this.entity.removeAllSourceRelationsByName(is_possible_exemplar_of);
			this.entity.removeAllSourceRelations(rel_has_title_written_as, ALIAS);
			this.entity.removeAllSourceRelations(rel_has_author_written_as, ALIAS);
			
			if(textUnknown){
				for(Entity target : this.possibleExamplerOfList.getEntities()){
					Entity target0 = getWrapper().getEntityByIdWithContent(target.getId());
					Relation tmp = new Relation(this.entity, target0, is_possible_exemplar_of);
				}
			}else{
				this.saveIndirectedAliases();
			}
			
			// WITNESS -> is_part_of_codex 
			if(this.isSelectedSaveAsNew()){
				this.entity = getWrapper().saveEntityAsNew(this.entity, user.getEmail());
			}else{
				this.entity = getWrapper().saveEntity(this.entity, user.getEmail());
			}
			this.setEntity(this.entity);
			
			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();
			
			this.setSelectedSaveAsNew(false);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		
		saveEnd();
		return PAGE_EDITOR;
	}

	private void saveIndirectedAliases() throws Exception{
		
		User user = getSessionUser();
		
		//WITNESS -> has_title_written_as -> ALIAS
		if(StringUtils.isNotEmpty(valueTextWritten)){
			Entity alias = null;
			if(this.suggestedTitlesWritten != null){
				for(SelectItem item : this.suggestedTitlesWritten){
					Long id = (Long)item.getValue();
					if(id != null){
						Entity candidate = getWrapper().getEntityById(id);
						if(candidate != null && valueTextWritten.equals(candidate.getOwnValue())){
							alias = candidate;
							break;
						}
					}
				}
			}
			
			if(alias == null){
				alias = new Entity(Node.TYPE_ABOX, ALIAS, false);
				alias.addAttribute(new Attribute(ALIAS, "text", valueTextWritten));
				alias = getWrapper().saveEntity(alias, user.getEmail());
				Relation rel = new Relation(alias, this.titleLo.getEntity(), "is_alias_title_of");
				alias = getWrapper().saveEntity(alias, user.getEmail());
			}			
			if(alias.isLightweight()){
				alias = getWrapper().getEntityByIdWithContent(alias.getId());
			}
			this.entity.replaceSourceRelation(alias, ALIAS, rel_has_title_written_as);
		}
				
		
		if(StringUtils.isNotEmpty(this.valueAuthorWritten)){
			Entity alias = null;
			if(suggestedAuthorsWritten != null){
				for(SelectItem item : this.suggestedAuthorsWritten){
					Long id = (Long)item.getValue();
					if(id != null){
						Entity candidate = getWrapper().getEntityById(id);
						if(candidate != null && valueAuthorWritten.equals(candidate.getOwnValue())){
							alias = candidate;
							break;
						}
					}
				}
			}
			
			if(alias == null){
				alias = new Entity(Node.TYPE_ABOX, ALIAS, false);
				alias.addAttribute(new Attribute(ALIAS, "text", valueAuthorWritten));
				alias = getWrapper().saveEntity(alias, user.getEmail());
				Relation rel = new Relation(alias, this.authorLo.getEntity(), "is_alias_name_of");
				alias = getWrapper().saveEntity(alias, user.getEmail());
			}
			if(alias.isLightweight()){
				alias = getWrapper().getEntityByIdWithContent(alias.getId());
			}
			this.entity.replaceSourceRelation(alias, ALIAS, rel_has_author_written_as);
		}
	}
	
	public String saveAsNewEntity() {
		this.setSelectedSaveAsNew(true);
		return save();
	}

	/* rich
	public void patronageChangeListener(ValueChangeEvent event) {
		//patronageLo = changeListener(event, patronageLo, PERSON, "name");
		patronageLo = changeValuePersonByRole(event, patronageLo, "Patron");
	
	}
	
	public void copyistChangeListener(ValueChangeEvent event) {
		copyistLo = changeValuePersonByRole(event, copyistLo, "Copyist");	
	}

	public void copyPlacesChangeListener(ValueChangeEvent event) {
		copyPlaceLo = changeListener(event, copyPlaceLo,"PLACE", "name");
	}*/
	
	public void updateTitle(){
		
		if(titleLo.entity != null && titleLo.entity.isPersistent()){
			
			this.textUnknown = UNKNOWN.equals(titleLo.entity.getOwnValue());
			
			List<Entity> targets = getWrapper().getTargetsForSourceRelation(titleLo.getEntity(), rel_was_created_by, PERSON, 1);
			if(targets.size() > 0){
				this.authorLo.setEntityAndAttribute0(targets.get(0));
				
			}else{
				this.authorLo.reset();
				/* rich
				this.authorLo.setEntityAndAttribute(null, name_translit);
				this.authorLo.statusImage.setStatus(StatusImage.STATUS_UNSET);
				*/
				
			}
			this.suggestedAuthorsWritten = getAuthorAliases(authorLo.entity);
			this.suggestedTitlesWritten = getTitleAliases(titleLo.entity);			
		}
	}

	public void updateAuthor(){
		
		if(authorLo.getEntity() != null && authorLo.getEntity().isPersistent()){
			
			this.titleLo.reset();
			
			List <Entity> sources = getWrapper().getSourcesForTargetRelation(authorLo.getEntity(), rel_was_created_by, TEXT, -1);
			
			ArrayList<SelectItem> options = new ArrayList<SelectItem>();
			for (Entity title : sources){
				options.add(new SelectItem(title.getId(), title.getOwnValue() + " [" + title.getId() + "]"));
			}
			
			titles_list = options;
			this.suggestedAuthorsWritten = getAuthorAliases(authorLo.entity);		
			this.suggestedTitlesWritten = getTitleAliases(null);
		}		
	}
	

	private List<SelectItem> getTitleAliases(Entity title) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		this.valueTextWritten = null;
		if(title != null && title.isPersistent()){
			items.add(new SelectItem(new Long(-1), "-- choose --"));
			List<Entity> aliasTitleList = getWrapper().getSourcesForTargetRelation(title, is_alias_title_of, ALIAS, -1);
			for(Entity alias : aliasTitleList){
				items.add(new SelectItem(alias.getId(), alias.getOwnValue()));
			}
			
			List<Entity> primeAliasTitleList = getWrapper().getSourcesForTargetRelation(title, is_prime_alias_title_of, ALIAS, -1);
			for(Entity alias : primeAliasTitleList){
				items.add(new SelectItem(alias.getId(), alias.getOwnValue()));
			}
		}
		
		return items;
	}
	private List<SelectItem> getAuthorAliases(Entity author) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		this.valueAuthorWritten = null;
		if(author != null && author.isPersistent()){
			//items.add(new SelectItem(null, "-- choose --"));
			List<Entity> aliasAuthorList = getWrapper().getSourcesForTargetRelation(author, is_alias_name_of, ALIAS, -1);
			for(Entity alias : aliasAuthorList){
				items.add(new SelectItem(alias.getId(), alias.getOwnValue()));
			}
			
			List<Entity> primeAliasAuthorList = getWrapper().getSourcesForTargetRelation(author, is_prime_alias_name_of, ALIAS, -1);
			for(Entity alias : primeAliasAuthorList){
				items.add(new SelectItem(alias.getId(), alias.getOwnValue()));
			}
		}
		return items;

	}

	@Override
	public void setEntity(Entity witness) {
		this.reset();
		this.entity = witness;
		
		if(this.entity.isPersistent()){
			if (witness.isLightweight()) {
				witness = getWrapper().getEntityContent(witness);
			}

			Attribute attCreationDate = this.entity.getAttributeByName("creation_date");
			if(attCreationDate != null && StringUtils.isNotEmpty(attCreationDate.getOwnValue())){
				this.copyDate = new Calendar(attCreationDate.getOwnValue()); 
			}else{
				this.copyDate = new Calendar();
			}
			
			// Loading attributes
			this.loadAttributes(this.entity);
			
			// Loading the relations
			// TODO loading target's relations
			for (Relation rel : witness.getSourceRelations()) {
				Entity target = null;
				if (rel.getOwnValue().equals(is_exemplar_of)) {

					// WITNESS -> is_exemplar_of -> TEXT
					target = getTargetRelation(rel);
					titleLo.setEntityAndAttribute0(target);
					this.updateTitle();
				
				} else if (rel.getOwnValue().equals(is_possible_exemplar_of)) {
					// WITNESS -> is_possible_exemplar_of -> TEXT
					target = getTargetRelation(rel);
					possibleExamplerOfList.add(target);					
				} else if (rel.getOwnValue().equals(rel_was_studied_by)) {
					target = getTargetRelation(rel);
					studiedByList.add(target);
				} else if (rel.getOwnValue().equals(rel_was_copied_by)) {
					// WITENSS -> was_copied_by -> PERSON
					target = getTargetRelation(rel);
					copyistLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals("was_copied_in")) {
					// WITENSS -> was_copied_in -> PLACE
					target = getTargetRelation(rel);
					copyPlaceLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals(rel_had_patron)) {
					// WITNESS -> had_patron -> PERSON
					target = getTargetRelation(rel);
					patronageLo.setEntityAndAttribute(target, name_translit);
				} else if (rel.getOwnValue().equals(is_part_of)) {
					// WITNESS -> had_patron -> PERSON		
					if(rel.getTargetObjectClass().equals(CODEX)){
						target = getTargetRelation(rel);
						this.setCodex(target);
					}
				} else if (rel.getOwnValue().equals(rel_has_author_written_as)) {
					//TODO this relation is no in the definitions
					Entity target2 = getTargetRelation(rel);
					Attribute alias = getTargetAttribute(target2, "alias");
					this.valueAuthorWritten = (alias != null) ? alias.getValue() : null;
								
				} else if (rel.getOwnValue().equals(rel_has_title_written_as)) {
					//TODO this relation is no in the definitions
					target = getTargetRelation(rel);
					//this.textWritten = target;
					Attribute alias  = getTargetAttribute(target, "alias");
					this.valueTextWritten = (alias != null) ? alias.getValue() : null;
				}
			}
			
			//this.loadReferences(this.entity);
			this.loadEndNoteRefs();

			this.setCurrentId(this.entity.getId().toString());
			this.checkConsistencyFromCountryToCodex();
			
			this.displayUrl = generateDisplayUrl(authorLo.entity, titleLo.entity, witness, getAppBean().getRoot());
		}
	}
	
	/**
	 * Loads the given codex without reset of the variables, which could cause problem in CurrentWitness Class
	 * @param codex
	 */
	public void setCodex(Entity codex){
		this.getCodexLo().setEntityAndAttribute(codex, "identifier");
		
		if(this.getCodexLo().entity != null && this.getCodexLo().entity.isPersistent()){
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1);
			if(list.size() > 0){
				this.setCollection(list.get(0));
			}
		}
	}

	public String getValueAuthor() {
		return valueAuthor;
	}

	public void setValueAuthor(String valueAuthor) {
		if(!lockValueAuthor)
			this.valueAuthor = valueAuthor;
		this.lockValueAuthor = false;
	}

	public void setValueTitle(String valueTitle) {
		if(!lockValueTitle)
			this.valueTitle = valueTitle;
		this.lockValueTitle = false;
	}

	public String getValueTitle() {
		return valueTitle;
	}

	public List<SelectItem> getTitles_list() {
		return titles_list;
	}

	public void setTitles_list(List<SelectItem> titlesList) {
		titles_list = titlesList;
	}

	public void setFoundCodex(boolean foundCodex) {
		this.foundCodex = foundCodex;
	}

	public boolean isFoundCodex() {
		return foundCodex;
	}
	
	public void listenerCreateTitleAlias(ValueChangeEvent event) {
		this.valueTextWritten = (String)event.getNewValue();
	}
	
	public void listenerChooseTitleAlias(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			Long aliasId = (Long) event.getNewValue();
			if(aliasId != null){
				if(aliasId.equals(-1)){
					this.valueTextWritten = null;
				}else{
					Entity alias = getWrapper().getEntityByIdWithContent(aliasId);
					if(alias != null){
						this.valueTextWritten = alias.getOwnValue();
					}	
				}
			}
		}
	}
	
	public void listenerCreateAuthorAlias(ValueChangeEvent event) {
		this.valueAuthorWritten = (String)event.getNewValue();
	}
	
	public void listenerChooseAuthorAlias(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			Long aliasId = (Long) event.getNewValue();
			if(aliasId != null){
				if(aliasId.equals(-1)){
					this.valueAuthorWritten = null;
				}else{
					Entity alias = getWrapper().getEntityByIdWithContent(aliasId);
					if(alias != null){
						this.valueAuthorWritten = alias.getOwnValue();
					}	
				}				
			}	
		}
	}

	@Override
	public void identifierChangedListener(ValueChangeEvent event) {
		super.identifierChangedListener(event);
		
		if (StringUtils.isEmpty(getCodexLo().attribute.getValue())){
			renderShowAllCodexCandidates = false;
			this.checkConsistencyFromCountryToCodex();
			return;
		}
		
		List<Entity> entities = getWrapper().getEntitiesByAtt(CODEX, getCodexLo().getAttName(), getCodexLo().attribute.getValue(), 10000, false);
		
		if (entities.size()>1){
			if ((getCollectionLo().attribute==null || getCollectionLo().getAttribute().getValue().equals("")) && 
					(getRepositoryLo().getAttribute()==null || getRepositoryLo().getAttribute().getValue().equals(""))){
				showAllCodexCandidates(entities);
				renderShowAllCodexCandidates = true;
			}
			else
				//renderShowAllCodexCandidates = false;
				renderShowAllCodexCandidates = true; // i want to keep the selection field now
		}
		else
			//renderShowAllCodexCandidates = false;
			renderShowAllCodexCandidates = true; //i want to keep the selection field now
	}

	public void searchInCurrentRepository(ActionEvent event) {

		ValueChangeEvent ne = new ValueChangeEvent((UIComponent) event
				.getComponent(), "", getCollectionLo().getAttribute().getOwnValue());
		identifierChangedListener(ne);

	}

	/* rich
	public String insertAuthorAction() {
		return "insert";

	}*/

	public String showOtherTextsByAuthorAction() {
		if (authorLo.entity == null || !authorLo.entity.isPersistent())
			return "";

		List<SelectItem> options = new ArrayList<SelectItem>();
		List<Entity> titles = getWrapper().getSourcesForTargetRelation(authorLo.entity, "was_created_by", TEXT, -1);
		for (Entity title: titles){
			options.add(
				new SelectItem(title.getId(), title.getOwnValue() + " [" + title.getId() + "]"));
		}
		titles_list = options;
		setSelectTitleRendered(true);
		return "";
	}
	
	/**
	 * Gebe moegliche andere Codices aus. Das ist notwendig, weil nach dem jetzigen Stand der Daten ein Codex
	 * mehrfach im Datensatz auftachen kann.
	 * @return
	 */
	public String showAllCodexCandidates(List<Entity> entities){
		if (entities != null) {
			for (Entity entity : entities) {
				CodexForList entForList = getCodexData(entity);
				codicesAll.add(entForList);
			}
		}
		setCodicesOverviewRendered(true);
		return "";
	}

	public String showAllTextsAction() {
		titles_list = new ArrayList<SelectItem>();
		titles_list = getAppBean().getAllTexts(); 
		this.selectTitleRendered = true;
		return "";
	}

	public void listenerSelectTitle(ActionEvent event){
		if(this.selectedTitleId != null){
			Entity title = getWrapper().getEntityById(this.selectedTitleId);
			if(title != null){
				this.titleLo.setEntityAndAttribute0(title);
				this.updateTitle();
				setSelectTitleRendered(false);
			}
		}
	}

	public void listenerCancelPpSelectTitle(ActionEvent event){
		System.out.println("listenerCancelPpSelectTitle");
		this.selectTitleRendered = false;
	}

	public void setSelectTitleRendered(Boolean selectTitleRendered) {
		this.selectTitleRendered = selectTitleRendered;
	}

	public Boolean getSelectTitleRendered() {
		return selectTitleRendered;
	}

	public List<SelectItem> getPersons_list() {
		return persons_list;
	}

	public void setPersons_list(List<SelectItem> personsList) {
		persons_list = personsList;
	}
	
	public Boolean getSelectPersonRendered() {
		return selectPersonRendered;
	}

	public void setSelectPersonRendered(Boolean selectPersonRendered) {
		this.selectPersonRendered = selectPersonRendered;
	}

	public Boolean getRestrictedByRole() {
		return restrictedByRole;
	}

	public void setRestrictedByRole(Boolean restrictedByRole) {
		this.restrictedByRole = restrictedByRole;
	}

	private void updatePersonList(){
		if (!this.restrictedByRole)
			//persons_list = ((AllPersonsBean) this.getBean("AllPersons"))
			//		.getPersonsSelectItems();
			persons_list = getCache().getAllPersons();
		else {
			//persons_list = ((AllPersonsBean) this.getBean("AllPersons"))
			//		.getPersonsSelectItemsByRole(this.selectPersonType);
			persons_list = getCache().getPersonsByRole(selectPersonType);
		}
	}
	
	public String showAllCopyistsAction() {
		this.setSelectPersonType("Copyist");
		this.updatePersonList();
		this.selectPersonRendered = true;
		return "";
	}

	public String showAllPatronsAction() {
		this.setSelectPersonType("Patron");
		this.updatePersonList();
		this.selectPersonRendered = true;
		return "";
	}

	public String showAllAuthorsAction() {
		this.setSelectPersonType("Author");
		this.updatePersonList();
		this.selectPersonRendered = true;
		return "";
	}

	public void listenerCancelPpSelectAuthor(ActionEvent event){
		this.selectPersonRendered = false;
	}
	
	public void listenerSelectPerson(ActionEvent event){
		String role = getSelectPersonType();
		if(this.selectedPersonId != null){
			Entity selectedPerson = getWrapper().getEntityByIdWithContent(selectedPersonId);
			if(selectedPerson != null){
				if (role.equals("Copyist")) {
					copyistLo.setEntityAndAttribute0(selectedPerson);
				} else if (role.equals("Patron")) {
					patronageLo.setEntityAndAttribute0(selectedPerson);
				} else if (role.equals("Author")) {
					authorLo.setEntityAndAttribute0(selectedPerson);
					this.updateAuthor();
				}
				setSelectPersonRendered(false);						
			}			
		}
	}

	public void setSelectPersonType(String selectPersonType) {
		this.selectPersonType = selectPersonType;
	}

	public String getSelectPersonType() {
		return selectPersonType;
	}

	public void restrictedByRoleChange(ValueChangeEvent event) {
		if (event.getNewValue().equals(event.getOldValue()))
			return;
		try{
			Boolean val = (Boolean) event.getNewValue();
			this.setRestrictedByRole(val);
			this.updatePersonList();	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void listenerConvertCreationDate(ActionEvent event) {
		/*
		try{
			
			String newDate = this.islamicCalCopyDate.islamic2Gregorian();
			String newJulianDate = this.islamicCalCopyDate.islamic2Julian();
			
			getAttributes().put("creation_date_ce", newDate);
			getAttributes().put("creation_date_julian", newJulianDate);
			
		}catch(Exception e){
			e.printStackTrace();
			addGeneralMsg("Error converting date!");
			addGeneralMsg("The islamic calendar begins on Friday, July 16th, 622 C.E. in the Julian calendar.");
			addGeneralMsg("The date must be equals or bigger than yawm al-jum'a, 1.Muharram.1 (5.1.1.1)");
		}
		*/
	}
	

	public void selectCodexFromOverviewAction(ActionEvent ae){
		/* rich
		
		HtmlCommandButton cp = (HtmlCommandButton)ae.getComponent();
		Long id = (Long) cp.getValue();
		//juc Entity ent = getOntology().getEntityById(id);
		Entity ent = getWrapper().getEntityById(id);
		this.getCodexLo().setEntityAndAttribute(ent, "identifier");
		//changeValueAttCodex(this.getCodexLo().getAttribute().getOwnValue());
		setCodicesOverviewRendered(false);
		
		*/
	
		
	}
	
	public String closeCodicesOverview(){
		setCodicesOverviewRendered(false);
		return "CLOSE";
		
	}
	
	
	private boolean isCodexIdentifierSet(String user){
		try {

			if(StatusImage.STATUS_OK.equals(getCodexLo().getStatus())){
				return true;
			}
			
			String identifier = getNewIdentifier();
			
			if(!StatusImage.STATUS_OK.equals(getCollectionLo().getStatus()) ||
					StringUtils.isEmpty(identifier)){
				//we can not save the witness
				if(!StatusImage.STATUS_OK.equals(getCollectionLo().getStatus())){
					addErrorMsg("The Collection is not valid.");
				}
				if(StringUtils.isEmpty(identifier)){
					addErrorMsg("The Codex does not have an valid identifier.");
				}
				addErrorMsg("The Witness could not be saved.");
				return false;
			}else {
				//two possibilities: 
				//1. there exist a codex with the same id
				//2. there is no a codex with this id, it should be saved.
				
				if(StringUtils.isNotEmpty(identifier)){
					List<Entity> codexListOfCollection = 
						getWrapper().getSourcesForTargetRelation(getCollectionLo().getEntity(), 
								is_part_of, CODEX, -1);
					for(Entity possibleCodex : codexListOfCollection){
						//juc Attribute att = getWrapper().getAttributeByName(possibleCodex, "identifier");
						Attribute att = getWrapper().getAttributeByName(possibleCodex.getId(), "identifier");
						if(att != null && identifier.equals(att.getValue())){
							getCodexLo().setEntityAndAttribute(possibleCodex, "identifier");
							return true;
						}
					}
				}
			}
			
			//if the Collection Status == OK && the Codex.identifier != null
			Entity newCodex = new Entity(Node.TYPE_ABOX, CODEX, false);
			newCodex.addAttribute(new Attribute("identifier", TEXT, identifier));
			getWrapper().saveEntity(newCodex, user);
			Relation isPartOf = new Relation(newCodex, getCollectionLo().getEntity(), is_part_of);
			getWrapper().saveRelationAsNode(isPartOf, user);
			getCodexLo().setEntityAndAttribute(newCodex, "identifier");
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	public boolean isCreateCodexRendered() {
		return createCodexRendered;
	}

	public void setCreateCodexRendered(boolean createCodexRendered) {
		this.createCodexRendered = createCodexRendered;
	}
	
	@Override
	public String loadCloneEntity(){
		this.setCodex(getCloneEntity());
		this.setRenderUnityCheckerDialog(false);
		this.checkConsistencyFromCountryToCodex();
		return PAGE_EDITOR;
	}

	public List<CodexForList> getCodicesAll() {
		return codicesAll;
	}

	public void setCodicesAll(List<CodexForList> codicesAll) {
		this.codicesAll = codicesAll;
	}

	public void setCodicesOverviewRendered(Boolean codicesOverviewRendered) {
		this.codicesOverviewRendered = codicesOverviewRendered;
	}

	public Boolean getCodicesOverviewRendered() {
		return codicesOverviewRendered;
	}
	
	/**
	 * Teste ob es Codices mit gleichem identifier gibt!
	 * @return
	 */
	public Boolean getRenderShowAllCodexCandidates(){
		return renderShowAllCodexCandidates;
	}

	
	
	public List<SelectItem> getSuggestedTitlesWritten() {
		return suggestedTitlesWritten;
	}

	public void setSuggestedTitlesWritten(List<SelectItem> suggestedTitlesWritten) {
		this.suggestedTitlesWritten = suggestedTitlesWritten;
	}

	public List<SelectItem> getSuggestedAuthorsWritten() {
		return suggestedAuthorsWritten;
	}

	public void setSuggestedAuthorsWritten(List<SelectItem> suggestedAuthorsWritten) {
		this.suggestedAuthorsWritten = suggestedAuthorsWritten;
	}

	public void codexCodexChangeListener(ValueChangeEvent event) {
		if (event.getNewValue() == null) {
			return;
		}
		if (event.getNewValue().equals(event.getOldValue())) {
			return;
		}
		this.getCodexLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		this.getCodexLo().attribute.setValue(event.getNewValue().toString());
	}
	public Long getValueTitleSelectedId() {
		return valueTitleSelectedId;
	}

	public void setValueTitleSelectedId(Long valueTitleSelectedId) {
		this.valueTitleSelectedId = valueTitleSelectedId;
	}

	public ListenerObject getCopyistLo() {
		return copyistLo;
	}

	public void setCopyistLo(ListenerObject copyistLo) {
		this.copyistLo = copyistLo;
	}

	public ListenerObject getCopyPlaceLo() {
		return copyPlaceLo;
	}

	public void setCopyPlaceLo(ListenerObject copyPlaceLo) {
		this.copyPlaceLo = copyPlaceLo;
	}

	public ListenerObject getPatronageLo() {
		return patronageLo;
	}

	public void setPatronageLo(ListenerObject patronageLo) {
		this.patronageLo = patronageLo;
	}

	public ListenerObject getTitleLo() {
		return titleLo;
	}

	public void setTitleLo(ListenerObject titleLo) {
		this.titleLo = titleLo;
	}

	public ListenerObject getAuthorLo() {
		return authorLo;
	}
	
	public String getNewIdentifier() {
		return newIdentifier;
	}

	public void setNewIdentifier(String newIdentifier) {
		this.newIdentifier = newIdentifier;
	}		

	public EntityList getStudiedByList() {
		return studiedByList;
	}

	public Calendar getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(Calendar copyDate) {
		this.copyDate = copyDate;
	}

	public void setStudiedByList(EntityList studiedByList) {
		this.studiedByList = studiedByList;
	}
	public Long getValueAuthorSelectedId() {
		return valueAuthorSelectedId;
	}

	public void setValueAuthorSelectedId(Long valueAuthorSelectedId) {
		this.valueAuthorSelectedId = valueAuthorSelectedId;
	}

	public String getValueTextWritten() {
		return valueTextWritten;
	}

	public void setValueTextWritten(String valueTextWritten) {
		//this.valueTextWritten = valueTextWritten;
	}

	public String getValueAuthorWritten() {
		return valueAuthorWritten;
	}

	public void setValueAuthorWritten(String valueAuthorWritten) {
		//this.valueAuthorWritten = valueAuthorWritten;
	}

	public void setAuthorLo(ListenerObject authorLo) {
		this.authorLo = authorLo;
	}
	
	public Long getSelectedTitleId() {
		return selectedTitleId;
	}

	public void setSelectedTitleId(Long selectedTitleId) {
		this.selectedTitleId = selectedTitleId;
	}
	public Long getSelectedPersonId() {
		return selectedPersonId;
	}

	public void setSelectedPersonId(Long selectedPersonId) {
		this.selectedPersonId = selectedPersonId;
	}

	public boolean isTextUnknown() {
		return textUnknown;
	}

	public void setTextUnknown(boolean textUnknown) {
		this.textUnknown = textUnknown;
	}

	public EntityList getPossibleExamplerOfList() {
		return possibleExamplerOfList;
	}

	public void setPossibleExamplerOfList(EntityList possibleExamplerOfList) {
		this.possibleExamplerOfList = possibleExamplerOfList;
	}
	
}

