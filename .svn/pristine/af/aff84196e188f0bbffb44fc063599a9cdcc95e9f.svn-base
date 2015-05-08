package de.mpiwg.itgroup.ismi.browse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;
import de.mpiwg.itgroup.ismi.event.beans.CopyEvent;
import de.mpiwg.itgroup.ismi.event.beans.StudyEvent;
import de.mpiwg.itgroup.ismi.event.beans.TransferEvent;

public class EntityDetailsBean extends AbstractBrowse{
	
	private static final long serialVersionUID = 1374652791403248103L;

	private static Logger logger = Logger.getLogger(EntityDetailsBean.class);
	
	private Entity entity;
	private String currentEntityId;

	private List<Attribute> attributeList = new ArrayList<Attribute>();
	private List<String> sourceRelationNames = new ArrayList<String>();
	private List<String> targetRelationNames = new ArrayList<String>();
	
	private Map<String, Long> sourceRelationCount = new HashMap<String, Long>();
	private Map<String, Long> targetRelationCount = new HashMap<String, Long>();
	
	private Map<String, List<Relation>> sourceRelations = new HashMap<String, List<Relation>>();
	private Map<String, List<Relation>> targetRelations = new HashMap<String, List<Relation>>();
	
	private List<Entity> previousVersionsList = new ArrayList<Entity>();
	private Entity selectedPreviousVersion = null;
	private boolean previousVersionDetailsVisible = false;


	public EntityDetailsBean() {
	}

	public void listenerHideDetailsPopup(ActionEvent event){
		setPreviousVersionDetailsVisible(false);
	}
	
	public void listenerHidePreviousVersions(ActionEvent event){
		setPreviousVersionsList(new ArrayList<Entity>());
		setSelectedPreviousVersion(null);
	}
	
	public void listenerUndoUntilThisVersion(ActionEvent event) throws Exception{
		this.selectedPreviousVersion = (Entity)getRequestBean("previousVersion");
		if(this.selectedPreviousVersion != null){
			
			//checking whether the relation reference to existing entities,
			//If the entity referenced does not exist more, the relation will not be considered.
			
			//Checking source's relation
			List<Relation> relToDelete = new ArrayList<Relation>();
			for(Relation rel : selectedPreviousVersion.getSourceRelations()){
				if(getWrapper().getEntityById(rel.getTargetId()) == null){
					relToDelete.add(rel);
				}
			}
			for(Relation rel : relToDelete){
				selectedPreviousVersion.getSourceRelations().remove(rel);
			}
			
			//Checking target's relations
			relToDelete = new ArrayList<Relation>();
			for(Relation rel : selectedPreviousVersion.getTargetRelations()){
				if(getWrapper().getEntityById(rel.getSourceId()) == null){
					relToDelete.add(rel);
				}
			}
			for(Relation rel : relToDelete){
				selectedPreviousVersion.getTargetRelations().remove(rel);
			}
			
			selectedPreviousVersion.setVersion(this.entity.getVersion());
			selectedPreviousVersion.setSystemStatus(Node.SYS_STATUS_CURRENT_VERSION);
			
			getWrapper().saveEntity(selectedPreviousVersion, getUserName());
			
		}
	}
	
	
	
	public void listenerGetPreviousVersions(ActionEvent event){
		this.previousVersionsList = getWrapper().getPreviousEntitiesById(getEntity().getId());
		if(previousVersionsList.size() == 0){
			addGeneralMsg("It had not been found previous versions for this entity.");
		}
	}
	
	public void listenerShowPreviousVersionDetails(ActionEvent event){
		this.selectedPreviousVersion = (Entity)getRequestBean("previousVersion");
		setPreviousVersionDetailsVisible(true);
		System.out.println(this.selectedPreviousVersion);
	}
	
	

	public void listenerShowEntity(ActionEvent event) {
		try {
			Long.parseLong(currentEntityId);
			redirect("/browse/entityDetails.xhtml", "?eid=" + currentEntityId);
			
		} catch (Exception e) {
			addErrorMsg("ID is not valid: " + this.currentEntityId);
		}
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
		if(entity != null){
			
			this.currentEntityId = entity.getId().toString();
			
			this.selectedPreviousVersion = null;
			this.previousVersionsList = new ArrayList<Entity>();
			//this.definition = getWrapper().getDefinition(entity.getObjectClass());
			this.attributeList = getWrapper().getAttributeByEntId(entity.getId());
			this.sourceRelations = new HashMap<String, List<Relation>>();
			this.targetRelations = new HashMap<String, List<Relation>>();
			this.sourceRelationNames = new ArrayList<String>();
			this.targetRelationNames = new ArrayList<String>();
			this.sourceRelationCount = new HashMap<String, Long>();
			this.targetRelationCount = new HashMap<String, Long>();
			
			
			System.out.println("****************");
			List<Relation> srcRels = getWrapper().getDefSourceRelations(this.entity.getObjectClass());
			for(Relation rel : srcRels){
				if(rel.getOwnValue().equals("has_autor_misattribution")){
					System.out.println("src - " + rel.getOwnValue());	
				}
				if(!this.sourceRelationNames.contains(rel.getOwnValue())){
					this.sourceRelationNames.add(rel.getOwnValue());
					this.sourceRelationCount.put(rel.getOwnValue(), getWrapper().getSourceRelationsCount(entity, rel.getOwnValue(), null));
				}
			}
			List<Relation> tarRels = getWrapper().getDefTargetRelations(this.entity.getObjectClass());
			for(Relation rel : tarRels){
				if(!this.targetRelationNames.contains(rel.getOwnValue())){
					this.targetRelationNames.add(rel.getOwnValue());
					this.targetRelationCount.put(rel.getOwnValue(), getWrapper().getTargetRelationsCount(entity, rel.getOwnValue(), null));
				}
					
			}			
		}
	}

	public void listenerShowTargetRelations(ActionEvent event){
		String tarName = (String) getRequestBean("tarName");
		this.targetRelations.put(tarName, getWrapper().getTargetRelations(this.entity, tarName, null, -1));
	}
	
	public String listenerDeleteEntity(){
		try {
			getWrapper().removeCurrentVersionEntity(this.entity);
			EntityRepositoryBean bean = (EntityRepositoryBean)getSessionBean("EntityRepository");
			if(bean != null){
				bean.reset();	
			}
			addGeneralMsg("The entity has been removed.");
		} catch (Exception e) {
			printInternalError(e);
			logger.error(e.getMessage() + " Removing" + entity, e);
			e.printStackTrace();
		}
		
		return GOTO_ENTITY_REPOSITORY;
	}
	
	public void listenerShowSourceRelations(ActionEvent event){
		try {
			String srcName = (String) getRequestBean("srcName");
			this.sourceRelations.put(srcName, getWrapper().getSourceRelations(this.entity, srcName, null, -1));
		} catch (Exception e) {
			printInternalError(e);
			logger.error(e.getMessage(), e);
		}
	}
	public Entity getEntity() {
		return entity;
	}



	/**
	 * This method calls the SessionBean and put in it the current entity to be
	 * modified.
	 * 
	 * @return
	 */
	public String actionEdit() {
		try {
			// getting de.mpiwg.itgroup.ismi.entry.beans.SessionBean
			getSessionBean().editEntity(this.entity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(entity.getObjectClass().equals(StudyEvent.OC) ||
				entity.getObjectClass().equals(CopyEvent.OC) ||
				entity.getObjectClass().equals(TransferEvent.OC)){
			return SessionBean.PAGE_EVENT_FORM;			
		}else{
			return SessionBean.PAGE_ENTRY;
		}
	}

	/**
	 * <p>
	 * Show the details page for the current entity.
	 * </p>
	 */
	public String showTarget() {
		Relation relation = (Relation) getRequestBean("srcRelation");
		setEntity(relation.getTarget());
		return GOTO_ENTITY_DETAILS;
	}

	public String showSource() {
		Relation relation = (Relation) getRequestBean("tarRelation");
		setEntity(relation.getSource());
		return GOTO_ENTITY_DETAILS;
	}

	public Entity getEntityById(Long id) {
		//juc return getOntology().getEntityById(id);
		return getWrapper().getEntityById(id);
	}

	public String getCurrentEntityId() {
		return currentEntityId;
	}

	public void setCurrentEntityId(String cid) {
		this.currentEntityId = cid;
	}

	public List<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}
	public List<String> getSourceRelationNames() {
		return sourceRelationNames;
	}

	public void setSourceRelationNames(List<String> sourceRelationNames) {
		this.sourceRelationNames = sourceRelationNames;
	}

	public List<String> getTargetRelationNames() {
		return targetRelationNames;
	}

	public void setTargetRelationNames(List<String> targetRelationNames) {
		this.targetRelationNames = targetRelationNames;
	}

	public Map<String, Long> getSourceRelationCount() {
		return sourceRelationCount;
	}

	public void setSourceRelationCount(Map<String, Long> sourceRelationCount) {
		this.sourceRelationCount = sourceRelationCount;
	}

	public Map<String, Long> getTargetRelationCount() {
		return targetRelationCount;
	}

	public void setTargetRelationCount(Map<String, Long> targetRelationCount) {
		this.targetRelationCount = targetRelationCount;
	}

	public Map<String, List<Relation>> getSourceRelations() {
		return sourceRelations;
	}

	public void setSourceRelations(Map<String, List<Relation>> sourceRelations) {
		this.sourceRelations = sourceRelations;
	}

	public Map<String, List<Relation>> getTargetRelations() {
		return targetRelations;
	}

	public void setTargetRelations(Map<String, List<Relation>> targetRelations) {
		this.targetRelations = targetRelations;
	}

	public List<Entity> getPreviousVersionsList() {
		return previousVersionsList;
	}

	public void setPreviousVersionsList(List<Entity> previousVersionsList) {
		this.previousVersionsList = previousVersionsList;
	}
	
	public Entity getSelectedPreviousVersion() {
		return selectedPreviousVersion;
	}

	public void setSelectedPreviousVersion(Entity selectedPreviousVersion) {
		this.selectedPreviousVersion = selectedPreviousVersion;
	}
	public boolean isPreviousVersionDetailsVisible() {
		return previousVersionDetailsVisible;
	}

	public void setPreviousVersionDetailsVisible(
			boolean previousVersionDetailsVisible) {
		this.previousVersionDetailsVisible = previousVersionDetailsVisible;
	}
}
