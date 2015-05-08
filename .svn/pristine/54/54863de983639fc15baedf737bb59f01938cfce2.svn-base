package de.mpiwg.itgroup.ismi.publicView.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;

public class CodexDynamicPage extends JSPDynamicPage{

	private List<WitnessTemplate> witnessList;
	private List<WitnessTemplate> unknownList;
	private Integer startPage;
	private Boolean imageFullscreen;
    private HashMap<String,String> references;

	@Override
	public void load(Long codexId){
		super.load(codexId);
		
		witnessList = new ArrayList<WitnessTemplate>();
		unknownList = new ArrayList<WitnessTemplate>();
		
		//loading the digitalization object
		List<Entity> list0 = getWrapper().getSourcesForTargetRelation(codexId, "is_digitalization_of", "DIGITALIZATION", 1);
		this.digi = (list0.size() == 0) ? null : list0.get(0);
		
		//loading the witnesses
		list0 = getWrapper().getSourcesForTargetRelation(codexId, "is_part_of", "WITNESS", -1);
		for(Entity witness : list0){
			WitnessTemplate tmp = new WitnessTemplate(witness, getWrapper(), true);
			this.witnessList.add(tmp);
			if(tmp.isUnknown()){
				this.unknownList.add(tmp);
			}
		}
		Collections.sort(this.witnessList);
		Collections.sort(this.unknownList);


		//load the references
		this.references=new HashMap<String,String>();

		list0 = getWrapper().getSourcesForTargetRelation(codexId, "is_reference_of", "REFERENCE", 1);
		for(Entity reference:list0){
		    //this.references.add(reference.getOwnValue());
		    this.references.put(reference.getOwnValue(),getWrapper().getAttributeOVByName(reference.getId(), "additional_information", false));
		}
		
	}
	
	public void init(){
		this.startPage = 0;
		this.imageFullscreen = false;
		try {
			
			this.load(Long.parseLong(getRequest().getParameter("eid")));
			
			try {
				this.startPage = Integer.parseInt(getRequest().getParameter("startPage"));
				this.imageFullscreen = 
						(getRequest().getParameter("imgFullscreen") != null) ? Boolean.parseBoolean(getRequest().getParameter("imgFullscreen")) : false;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer getStartPage() {
		return startPage;
	}

	public Boolean getImageFullscreen() {
		return imageFullscreen;
	}

	public List<WitnessTemplate> getWitnessList() {
		return witnessList;
	}

	public void setWitnessList(List<WitnessTemplate> witnessList) {
		this.witnessList = witnessList;
	}

	public List<WitnessTemplate> getUnknownList() {
		return unknownList;
	}

	public void setUnknownList(List<WitnessTemplate> unknownList) {
		this.unknownList = unknownList;
	}

    public HashMap<String,String> getReferenceList(){
	return references;

    }
	
}
