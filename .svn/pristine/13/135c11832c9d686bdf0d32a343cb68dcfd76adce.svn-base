package de.mpiwg.itgroup.ismi.entry.beans;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;

public class CurrentRoleBean extends AbstractISMIBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4934098828218979428L;
	private static Logger logger = Logger.getLogger(CurrentRoleBean.class);
	
	public CurrentRoleBean(){
		this.entity = new Entity(Node.TYPE_ABOX, ROLE, false);
		this.setDefObjectClass(ROLE);
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, ROLE, false);
	}
	
	@Override
	public void setEntity(Entity role) {
		this.reset();
		this.entity = role;
		
		if(this.entity != null && this.entity.isPersistent()){
			if (this.entity.isLightweight()) {
				this.entity = getWrapper().getEntityContent(role);
			}
			this.loadAttributes(this.entity);//, getDefinition(this.entity));
		}
	}
	
	@Override
	public String save(){
		super.save();
		try {
			
			if(isConsistent()){
				this.entity = updateEntityAttributes(this.entity);
				this.entity = getWrapper().saveEntity(this.entity, getUserName());
				getAppBean().setRoleListAsDirty();
				
				logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
				this.printSuccessSavingEntity();	
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		
		saveEnd();
		return PAGE_EDITOR;
	}
	
	private boolean isConsistent(){
		
		if(getAttributes().containsKey("name") && 
				StringUtils.isNotEmpty(getAttributes().get("name"))){
			return true;
			
		}else{
			addErrorMsg("The role has not be saved, because its name can not be empty!");
			return false;
		}
		
		
	}
}
