package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.security.bo.User;

public class CurrentDigitalizationBean extends CodexEditorTemplate implements Serializable{
	
	private static final long serialVersionUID = 4910425502556948216L;
	
	private static Logger logger = Logger.getLogger(CurrentDigitalizationBean.class);
	
	
	public CurrentDigitalizationBean(){
		super();
		this.entity = new Entity(Node.TYPE_ABOX, DIGITALIZATION, false);
		setDefObjectClass(DIGITALIZATION);
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, DIGITALIZATION, false);
	}
	
	@Override
	public void setEntity(Entity digi){
		this.reset();
		this.entity = digi;
		
		if(this.entity.isPersistent()){
			if (entity.isLightweight()) {
				entity = getWrapper().getEntityContent(digi);
			}
		}
		
		this.loadAttributes(this.entity);
		
		List<Entity> list = getWrapper().getTargetsForSourceRelation(entity, is_digitalization_of, CODEX, -1);
		if(list.size() > 0){
			this.setCodex(list.get(0));
		}
	}
	
	
	public void setCodex(Entity codex){
		this.getCodexLo().setEntityAndAttribute(codex, "identifier");
		
		if(this.getCodexLo().entity != null && this.getCodexLo().entity.isPersistent()){
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1);
			if(list.size() > 0){
				this.setCollection(list.get(0));
			}
		}
	}
	
	@Override
	public String save() {
		super.save();
		try {
			User user = getSessionUser();
			
			this.entity = this.updateEntityAttributes(this.entity);

			// DIGITALIZATION -> is_digitalization_of -> CODEX
			this.entity.replaceSourceRelation(getCodexLo().entity, CODEX, is_digitalization_of);
			
			this.entity = getWrapper().saveEntity(this.entity, user.getEmail());
			
			this.setEntity(this.entity);
			
			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			
			this.printSuccessSavingEntity();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		saveEnd();
		return PAGE_EDITOR;
	}			
	

}
