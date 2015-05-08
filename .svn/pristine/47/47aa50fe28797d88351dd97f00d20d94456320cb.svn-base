package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;


public class CurrentAliasBean extends AbstractISMIBean  implements Serializable{
	private static final long serialVersionUID = 3420778447263527153L;
	
	private static Logger logger = Logger.getLogger(CurrentAliasBean.class);
	
	public CurrentAliasBean(){
		this.entity = new Entity(Node.TYPE_ABOX, ALIAS, false);
		setDefObjectClass(ALIAS);
	}

	@Override
	public void setEntity(Entity alias) {		
		this.entity = alias;
		if(this.entity.isLightweight()){
			this.entity = getWrapper().getEntityContent(this.entity);
		}
		this.loadAttributes(this.entity);	
	}
	
	@Override
	public String save(){
		super.save();
		try {
			this.entity = updateEntityAttributes(this.entity);
			this.entity = getWrapper().saveEntity(this.entity, getSessionUser().getEmail());
			getAppBean().getSimpleSearchCache().setMapDirty(true);
			
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
