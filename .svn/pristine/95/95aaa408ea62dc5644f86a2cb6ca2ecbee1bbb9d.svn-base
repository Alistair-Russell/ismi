package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.util.guiComponents.EntityList;

/**
 * @author jurzua
 * 
 */
public class CurrentCodexBean extends CodexEditorTemplate  implements Serializable{
	private static final long serialVersionUID = -4242977362183330958L;

	private static Logger logger = Logger.getLogger(CurrentCodexBean.class);

	//private Entity codex;

	private List<Entity> witnessInCurrentCodex;
	private boolean showWitnessInCurrentCodex;
	
	private EntityList ownedByPeople;
	private EntityList readByPeople;
	
	private List<SelectItem> suggestedBindingList = new ArrayList<SelectItem>();

	private CodexEditorTemplate referencedCodexTemplate;
	private Boolean isAlias;
	
	
	public CurrentCodexBean() {
		this.entity = new Entity(Node.TYPE_ABOX, CODEX, false);
		setDefObjectClass(CODEX);
		registerChecker(getCityLo(), "city is not valid");
		registerChecker(getCountryLo(), "country is not valid");
		registerChecker(getRepositoryLo(), "repository is not valid");
		registerChecker(getCollectionLo(), "collection is not valid");
		
		this.refreshBindingList();
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, CODEX, false);
		
		this.ownedByPeople = new EntityList(PERSON, "name_translit", true);
		this.readByPeople = new EntityList(PERSON, "name_translit", true);
		
		this.witnessInCurrentCodex = new ArrayList<Entity>();
		this.showWitnessInCurrentCodex = false;
		this.isAlias = false;
		this.referencedCodexTemplate = new CodexEditorTemplate();
		this.refreshBindingList();
	}
	
	public void listenerRefreshBindingList(ActionEvent event){
		this.refreshBindingList();
	}
	
	public void listenerEditCalendarOwnedBy(ActionEvent event){
		Entity entity = (Entity)getRequestBean("item");
		if(entity != null){
			Calendar cal = this.ownedByPeople.getCalendar(entity.getId());
			getSessionBean().editCalendar(cal, ownedByPeople, entity.getId());
		}
	}
	
	public void listenerEditCalendarReadBy(ActionEvent event){
		Entity entity = (Entity)getRequestBean("item");
		if(entity != null){
			Calendar cal = this.readByPeople.getCalendar(entity.getId());
			getSessionBean().editCalendar(cal, readByPeople, entity.getId());
		}
	}
	
	private void refreshBindingList(){
		this.suggestedBindingList = new ArrayList<SelectItem>();
		suggestedBindingList.add(new SelectItem("", "--choose--"));
		Attribute binding = getWrapper().getDefAttributeByOwnValue(this.entity.getObjectClass(), "binding");
		if(binding != null){
			for(String s : binding.getPossibleValuesList()){
				this.suggestedBindingList.add(new SelectItem(s));
			}
		}
	}

	public String createCollectionAction() {
		getSessionBean().setSelectedTab(SessionBean.COLLECTION_TAB);
		return "edit";
	}

	@Override
	public void setEntity(Entity codex) {
		this.reset();
		this.entity = codex;
		logger.info("Set Codex " + this.entity);
		try {
			if(this.entity != null && this.entity.isPersistent()){	
				if(this.entity.isLightweight()){
					this.entity = getWrapper().getEntityContent(this.entity);
				}
				
				this.loadAttributes(this.entity);
				
				Attribute isAliasAtt = getWrapper().getAttributeByName(this.entity.getId(), "is_alias");
				if(isAlias != null){
					try{
						this.isAlias = new Boolean(isAliasAtt.getOwnValue());
					}catch(Exception ex){
						this.isAlias = false;
					}
				}else{
					this.isAlias = false;
				}
				
				for(Relation rel : getWrapper().getSourceRelations(codex, owned_by, PERSON, -1)) {
					this.ownedByPeople.add(getWrapper().getEntityById(rel.getTargetId()), rel.getAttributeByName("date"));
				}
				
				for(Relation rel : getWrapper().getSourceRelations(codex, "read_by", PERSON, -1)) {
					this.readByPeople.add(getWrapper().getEntityById(rel.getTargetId()), rel.getAttributeByName("date"));
				}
				
				for (Entity target : getWrapper()
						.getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1)) {
					String ct = target.getObjectClass();
					
					if (ct.equals(COLLECTION)) {
						this.setCollection(target);
					}
				}
				
				List<Entity> list = getWrapper().getTargetsForSourceRelation(codex, "is_alias_of", CODEX, 1);
				if(!list.isEmpty()){
					this.setReferencedCodex(list.get(0));
					//this.referencedCodexTemplate.setCodexLo(codexLo);
					//this.referencedCodexLO.setEntityAndAttribute(list.get(0), "identifier");
				}
				
				
				//this.loadReferences(this.entity);
				this.loadEndNoteRefs();
				
				this.setCurrentId(this.entity.getId().toString());
				this.checkConsistencyFromCountryToCodex();
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			printInternalError(e);
		}
	}
	
	public void setReferencedCodex(Entity codex){
		this.referencedCodexTemplate.getCodexLo().setEntityAndAttribute(codex, "identifier");
		
		if(this.referencedCodexTemplate.getCodexLo().entity != null && 
				this.referencedCodexTemplate.getCodexLo().entity.isPersistent()){
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1);
			if(list.size() > 0){
				this.referencedCodexTemplate.setCollection(list.get(0));
			}
		}
	}

	@Override
	public String save() {
		super.save();
		try {
			CheckResults cr = getCheckResults();
			if (cr.hasErrors) {
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				this.setSelectedSaveAsNew(false);
				return PAGE_EDITOR;
			}

			if (this.entity == null) {
				this.entity = new Entity();
				this.entity.setObjectClass("CODEX");
			}
			
			this.entity = this.updateEntityAttributes(this.entity);
			
			if(this.entity.getAttributeByName("is_alias") != null){
				this.entity.getAttributeByName("is_alias").setOwnValue(this.isAlias.toString());
			}else{
				this.entity.addAttribute(new Attribute("is_alias", "boolean", this.isAlias.toString()));
			}
			
			if(this.isAlias){
				if(referencedCodexTemplate.getCodexLo().entity != null){
					// CODEX -> is_part_of -> CODEX
					this.entity.replaceSourceRelation(referencedCodexTemplate.getCodexLo().entity, CODEX, "is_alias_of");	
				}else{
					this.entity.removeAllSourceRelations("is_alias_of", CODEX);
				}			
			}
			
			//checking is the identifier is unique for the selected collecion
			String identifier = (this.entity.getAttributeByName("identifier") != null) ? this.entity.getAttributeByName("identifier").getValue() : "";
			if(StringUtils.isNotEmpty(identifier)){
				if(!checkUnityCodex(identifier, (isSelectedSaveAsNew()) ? null : this.entity.getId(), this.getCollectionLo().entity)){
					this.renderUnityCheckerDialog();
					this.setSelectedSaveAsNew(false);
					return PAGE_EDITOR;
				}
			}else{
				this.addErrorMsg("This entity has not been saved, because its identifier was empty.");
				this.addErrorMsg("You must enter an identifier.");
				return PAGE_EDITOR;
			}
			
			if(!isCodexConsistentBeforeSave()){
				return PAGE_EDITOR;
			}

			this.entity.replaceSourceRelation(this.getCollectionLo().entity, COLLECTION, is_part_of);
			
			// CODEX -> owner_by (manyToMany) -> PERSON
			this.entity.removeAllSourceRelationsByName(owned_by);
			
			// CODEX -> read_by (manyToMany) -> PERSON
			this.entity.removeAllSourceRelationsByName("read_by");
			
			//REFERENCE -> is_reference_of -> THIS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();		
			
			if (this.isSelectedSaveAsNew()) {
				this.entity = getWrapper().saveEntityAsNew(this.entity,getSessionUser().getEmail());
				this.setSelectedSaveAsNew(false);
			} else {
				for(Entity owner : this.ownedByPeople.getEntities()){
					if(owner.isLightweight()){
						owner = getWrapper().getEntityByIdWithContent(owner.getId());
					}
					Relation ownerBy = new Relation(this.entity, owner, owned_by);
					Calendar cal = ownedByPeople.getCalendar(owner.getId());
					if(cal != null){
						ownerBy.addAttribute(new Attribute("date", "date", cal.toJSONString()));
					}
				}
				for(Entity reader : this.readByPeople.getEntities()){
					Relation readBy = new Relation(this.entity, reader, "read_by");
					Calendar cal = readByPeople.getCalendar(reader.getId());
					if(cal != null){
						readBy.addAttribute(new Attribute("date", "date", cal.toJSONString()));
					}
				}
				
				this.entity = getWrapper().saveEntity(this.entity, getSessionUser().getEmail());
			}

			this.generateSecundaryOW(this.entity, getSessionUser().getEmail());
			getSessionBean().setEditFormCurrentEntId(this.entity.getId());
			
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

	public String saveAsNewEntity() {
		this.setSelectedSaveAsNew(true);
		return save();
	}

	public void setLWCodex(Entity codex) {
		this.entity = codex;
	}
	
	public void actionShowWitnessDialog(ActionEvent event){
		if(this.witnessInCurrentCodex.size() == 0 && this.entity.isPersistent()){
			this.witnessInCurrentCodex = 
				getWrapper().getSourcesForTargetRelation(this.entity, "is_part_of", "WITNESS", 50);
		}
		this.setShowWitnessInCurrentCodex(true);
	}
	
	public void actionCloseWitnessDialog(ActionEvent event){
		this.setShowWitnessInCurrentCodex(false);
	}
	
	public void actionEditThisWitness(ActionEvent event){
		this.setShowWitnessInCurrentCodex(false);
		Entity currentWitness = (Entity)getRequestBean("witness");
		if(currentWitness != null && currentWitness.isPersistent()){
			getSessionBean().editEntity(currentWitness);
		}
	}
	
	public List<Entity> getWitnessInCurrentCodex() {
		return witnessInCurrentCodex;
	}

	public void setWitnessInCurrentCodex(List<Entity> witnessInCurrentCodex) {
		this.witnessInCurrentCodex = witnessInCurrentCodex;
	}
	
	public boolean isShowWitnessInCurrentCodex() {
		if(this.entity == null || !this.entity.isPersistent()){
			return false;
		}
		return showWitnessInCurrentCodex;
	}

	public void setShowWitnessInCurrentCodex(boolean showWitnessInCurrentCodex) {
		this.showWitnessInCurrentCodex = showWitnessInCurrentCodex;
	}
	
	public void actionEditThisCollection(ActionEvent event){
		if(this.getCollectionLo().entity != null && getCollectionLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getCollectionLo().entity);
		}
	}
	
	public EntityList getOwnedByPeople() {
		return ownedByPeople;
	}

	public void setOwnedByPeople(EntityList ownedByPeople) {
		this.ownedByPeople = ownedByPeople;
	}

	public List<SelectItem> getSuggestedBindingList() {
		return suggestedBindingList;
	}

	public void setSuggestedBindingList(List<SelectItem> suggestedBindingList) {
		this.suggestedBindingList = suggestedBindingList;
	}

	public CodexEditorTemplate getReferencedCodexTemplate() {
		return referencedCodexTemplate;
	}

	public void setReferencedCodexTemplate(
			CodexEditorTemplate referencedCodexTemplate) {
		this.referencedCodexTemplate = referencedCodexTemplate;
	}

	public Boolean getIsAlias() {
		return isAlias;
	}

	public void setIsAlias(Boolean isAlias) {
		this.isAlias = isAlias;
	}

	public EntityList getReadByPeople() {
		return readByPeople;
	}

	public void setReadByPeople(EntityList readByPeople) {
		this.readByPeople = readByPeople;
	}
	
}
