package de.mpiwg.itgroup.ismi.entry.dataBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.services.ServiceRegistry;

public class RoleCache extends AbstractCache{
	Map<String, List<Long>> roleMap = null;
	
	public RoleCache(ServiceRegistry sr) {
		super(sr);
	}
	
	@Override
	public void reset(){
		super.reset();
		loadRoleMap();
	}
	
	private void loadRoleMap(){
		roleMap = new HashMap<String, List<Long>>();
		//List<Entity> roles = getOtl().getLightweightAssertions("ROLE", null, -1);
		List<Entity> roles = getWrapper().getEntitiesByDef("ROLE");
		for(Entity role : roles){
			if(StringUtils.isNotEmpty(role.getOwnValue())){
				String roleLowCase = role.getOwnValue().toLowerCase();
				getRoleMap().put(roleLowCase, new ArrayList<Long>());
				List<Relation> relList = getWrapper().getTargetRelations(role, "has_role", "PERSON", -1);
				for(Relation hasRole : relList){
					getRoleMap().get(roleLowCase).add(hasRole.getSourceId());
				}	
			}else{
				System.err.println("****************");
				System.err.println("Entity Role without Label: " + role);
				System.err.println("****************");
			}
		}
	}
	
	public List<String> getRoleList(Long id){
		List<String> list = new ArrayList<String>();
		for(String roleName : this.getRoleMap().keySet()){
			if(this.getRoleMap().get(roleName).contains(id)){
				list.add(roleName);
				
			}
		}
		return list;
	}
	
	public Map<String, List<Long>> getRoleMap() {
		if(roleMap == null)
			loadRoleMap();
		return roleMap;
	}

	public boolean roleContainsPersonId(String role, Long id){
		if(StringUtils.isEmpty(role) || this.getRoleMap().get(role.toLowerCase()) == null)
			return false;
		
		return this.getRoleMap().get(role.toLowerCase()).contains(id);
	}
}
