package de.mpiwg.itgroup.diva.jsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.utils.templates.CodexTemplate;
import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;


public class JSPDigitalization extends AbsJSPWrapper{

	private Long digiId;
	private Entity digi;
	
	private String name;
	private String numFiles;
	
	
	private CodexTemplate codex;
	private List<WitnessTemplate> witnessList = new ArrayList<WitnessTemplate>();
	private List<WitnessTemplate> unknownList = new ArrayList<WitnessTemplate>();
	
	public String getDigiLabel(){
		return (digi == null) ? null : digi.getOwnValue();
	}
	
	public String getUserName(){
		if(getSessionBean() != null)
			return getSessionBean().getUsername();
		return null;
	}	
	
	public boolean hasLogin(){
		return !StringUtils.isEmpty(getUserName());
	}
	
	public boolean canEdit(){
		if(getSessionBean() != null)
			return getSessionBean().isCanEdit();
		return false;
	}
	
	@Override
	public void init(){
		try {
			this.loadDigitalization(Long.parseLong(getRequest().getParameter("digiId")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void reset(){
		this.witnessList = new ArrayList<WitnessTemplate>();
		this.unknownList = new ArrayList<WitnessTemplate>();
		this.codex = null;
		this.digi = null;
		this.digiId = null;
		this.name = null;
		this.numFiles = null;
	}
	
	private void loadDigitalization(Long digiId){
		this.reset();
		if(digiId != null){
			this.digiId = digiId;
			this.digi = getWrapper().getEntityByIdWithContent(digiId);
			this.name = digi.getOwnValue();
			this.numFiles = (digi.getAttributeByName("num_files") != null)? digi.getAttributeByName("num_files").getValue() : "";
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(digi, "is_digitalization_of", "CODEX", 1);
			if(list.size() > 0){
				this.codex = new CodexTemplate(list.get(0), getWrapper());
				List<Entity> list0 = getWrapper().getSourcesForTargetRelation(list.get(0), "is_part_of", "WITNESS", -1);
				for(Entity witness : list0){
					WitnessTemplate tmp = new WitnessTemplate(witness, getWrapper(), true);
					this.witnessList.add(tmp);
					if(tmp.isUnknown()){
						this.unknownList.add(tmp);
					}
				}
			}			
		}
		Collections.sort(this.witnessList);
		Collections.sort(this.unknownList);
	}

	public Entity getDigi() {
		return digi;
	}

	public CodexTemplate getCodex() {
		return codex;
	}

	public List<WitnessTemplate> getWitnessList() {
		return this.witnessList;
	}

	public List<WitnessTemplate> getUnknownList() {
		return unknownList;
	}

	public String getName() {
		return name;
	}

	public String getNumFiles() {
		return numFiles;
	}
	
	public Long getDigiId(){
		return this.digiId;
	}
}



