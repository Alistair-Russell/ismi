package de.mpiwg.itgroup.ismi.entry.beans;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

public class UnityChecker extends AbstractISMIBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5727416233789954800L;
	private Entity cloneEntity;
	private String unityCheckerMsg;
	private boolean renderUnityCheckerDialog;

	@Override
	public void reset(){
		super.reset();
		this.renderUnityCheckerDialog = false;
	}

	private boolean checkUnity(String unityName, Long id, String objectClass, Entity target, String relationName, String attName){
		this.cloneEntity = null;
		unityName = unityName.toLowerCase();
		
		if(target != null && target.isPersistent()){
			for (Entity src : getWrapper().getSourcesForTargetRelation(target, relationName, objectClass, -1)) {
				if(id == null || id.longValue() != src.getId().longValue()){
					if(StringUtils.isEmpty(attName)){
						if(unityName.equals(src.getOwnValue().toLowerCase())){
							this.cloneEntity = src;
							return false;
						}
					}else{
						Attribute att = getWrapper().getAttributeByName(src.getId(), attName);
						if(att != null && unityName.equals(att.getValue().toLowerCase())){
							this.cloneEntity = src;
							return false;
						}
					}	
				}
			}				
		}
		
		return true;
	}
	
	public String hideUnityCheckerDialog(){
		this.renderUnityCheckerDialog = false;
		return PAGE_EDITOR;
	}
	
	public void renderUnityCheckerDialog(){
		if(cloneEntity != null && cloneEntity.isPersistent()){
			if(cloneEntity.getObjectClass().equals(CODEX)){
				this.unityCheckerMsg = "There is already an object with the same identifier. ";
			}else{
				this.unityCheckerMsg = "There is already an object with the same name. ";
			}
			
			this.unityCheckerMsg += "The ID of the " + cloneEntity.getObjectClass().toLowerCase() +
			" found is " + cloneEntity.getId() + ".";
			
			this.unityCheckerMsg += 
				"\nDo you want to load the existing " 
				+ cloneEntity.getObjectClass().toLowerCase() + "?";
			this.renderUnityCheckerDialog = true;
		}		
	}
	
	public String loadCloneEntity(){
		if(cloneEntity != null && cloneEntity.isPersistent()){
			getSessionBean().editEntity(cloneEntity);
		}
		return PAGE_EDITOR;
	}
	
	public boolean checkUnityCollection(String collectionName, Long id, Entity repository){
		return this.checkUnity(collectionName, id, COLLECTION, repository, is_part_of, null);
	}
	
	public boolean checkUnityRepository(String repositoryName, Long id, Entity city){
		return this.checkUnity(repositoryName, id, REPOSITORY, city, is_in, null);
	}
	
	public boolean checkUnityCity(String cityName, Long id, Entity country){
		return this.checkUnity(cityName, id, PLACE, country, is_part_of, null);
	}
	
	public boolean checkUnityCodex(String identifier, Long id, Entity collection){
		return this.checkUnity(identifier, id, CODEX, collection, is_part_of, "identifier");
	}
	
	public Entity getCloneEntity() {
		return cloneEntity;
	}

	public void setCloneEntity(Entity cloneEntity) {
		this.cloneEntity = cloneEntity;
	}
	public String getUnityCheckerMsg() {
		return unityCheckerMsg;
	}

	public void setUnityCheckerMsg(String unityCheckerMsg) {
		this.unityCheckerMsg = unityCheckerMsg;
	}
	public boolean isRenderUnityCheckerDialog() {
		return renderUnityCheckerDialog;
	}

	public void setRenderUnityCheckerDialog(boolean renderUnityCheckerDialog) {
		this.renderUnityCheckerDialog = renderUnityCheckerDialog;
	}
}
