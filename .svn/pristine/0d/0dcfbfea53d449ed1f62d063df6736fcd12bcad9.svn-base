package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;

public class CurrentRepositoryBean extends CodexEditorTemplate  implements Serializable{
	private static final long serialVersionUID = 2926289436446091260L;
	
	private static Logger logger = Logger.getLogger(CurrentRepositoryBean.class);
	
	public static int MAX_PLACES = 100;
	//private Entity repository;	
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, REPOSITORY, false);
	}
	
	public CurrentRepositoryBean(){
		this.reset();
		setDefObjectClass(REPOSITORY);
		registerChecker(getCityLo().statusImage, "City not valid!");
		registerChecker(getCountryLo().statusImage, "Country is not valid!");
	}

	
	@Override
	public void setEntity(Entity repository) {
		this.reset();
		this.entity = repository;
		
		if(this.entity != null && this.entity.isPersistent()){
			if(this.entity.isLightweight()){
				this.entity = getWrapper().getEntityContent(this.entity);
			}
			
			this.setCurrentId(this.entity.getId().toString());
			this.loadAttributes(this.entity);//, getDefinition(this.entity));
			
			//this.loadReferences(this.entity);
			this.loadEndNoteRefs();
			
			for(Entity city : getWrapper().getTargetsForSourceRelation(this.entity, "is_in", PLACE, 1)){
				this.setCity(city);
			}			
		}
	}

	public String saveAsNewEntity(){
		this.setSelectedSaveAsNew(true);
		return save();
	}
	
	
	@Override
	public String save(){
		super.save();
		try {
			CheckResults cr = getCheckResults();
			if (cr.hasErrors){
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				this.setSelectedSaveAsNew(false);
				return PAGE_EDITOR;
			}
			
			this.entity = this.updateEntityAttributes(this.entity);
			
			
			Attribute attName = this.entity.getAttributeByName("name");
			String repositoryName = (attName == null) ? null : attName.getValue();
			if(StringUtils.isNotEmpty(repositoryName)){
				if(!checkUnityRepository(repositoryName, (isSelectedSaveAsNew()) ? null : this.entity.getId(), this.getCityLo().entity)){
					this.renderUnityCheckerDialog();
					this.setSelectedSaveAsNew(false);
					return PAGE_EDITOR;
				}
			}else{
				this.addErrorMsg("Ths entity has not been saved, because its name was empty.");
				this.addErrorMsg("You must enter a name.");
				return PAGE_EDITOR;
			}
			
			if(!isRepositoryConsistentBeforeSave()){
				return PAGE_EDITOR;
			}
			
			this.entity.replaceSourceRelation(this.getCityLo().getEntity(), PLACE, is_in);
			
			//REFERENCE -> is_reference_of -> THIS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();
			
			if(isSelectedSaveAsNew()){
				this.entity.removeAllTargetRelations(is_part_of, COLLECTION);
				this.entity = getWrapper().saveEntityAsNew(this.entity, getSessionUser().getEmail());
			}else{
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
}
