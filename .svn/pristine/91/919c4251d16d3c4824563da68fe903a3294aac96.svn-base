package de.mpiwg.itgroup.ismi.publicView;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.ViewerPage;
import org.mpi.openmind.repository.utils.RomanizationLoC;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;

public class PublicCodexView extends AbstractBean{
	private static final long serialVersionUID = -3013781647163292966L;
	
	private Entity codex;
	private Entity digi;
	private List<WitnessItem> witnessList;
	private boolean codexPublic = true;
	
	private Long dynamicCodexPageId;
	private Long dynamicWitnessPageId;
	
	public void load(String id){
		try {
			this.load(Long.parseLong(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void load(Long id){
		this.witnessList = new ArrayList<WitnessItem>();
		this.codex = getWrapper().getEntityById(id);
		this.digi = null;
		if(codex != null){
			
			List<Entity> tmp = getWrapper().getSourcesForTargetRelation(codex, "is_digitalization_of", "DIGITALIZATION", 1);
			digi = (tmp.isEmpty()) ? null : tmp.get(0);
			
			tmp = getWrapper().getSourcesForTargetRelation(codex, "is_part_of", "WITNESS", -1);
			
			for(Entity witness : tmp){
				
				List<Entity> list0 = getWrapper().getTargetsForSourceRelation(witness.getId(), "is_exemplar_of", "TEXT", 1);
				//TODO the label should be normalized with the new algorithm of Chantal
				String label =  (list0.isEmpty()) ? witness.getOwnValue() : list0.get(0).getOwnValue();
				
				//calculating the image index for the diva viewer
				//String divaImageIndex = this.getDivaImage(digi.getOwnValue(), getWrapper().getAttributeByName(witness.getId(), "start_page"));
				this.witnessList.add(new WitnessItem(witness.getId(), label/*, divaImageIndex*/));	
			}
			
			
		}
		ViewerPage tmp = getWrapper().getViewerPage("Codex");
		this.dynamicCodexPageId = (tmp == null) ? dynamicCodexPageId : tmp.getId();
		
		tmp = getWrapper().getViewerPage("Witness");
		this.dynamicWitnessPageId = (tmp == null) ? dynamicWitnessPageId : tmp.getId();
	}
	
	public boolean isCodexPublic(){
		return this.codexPublic;
	}
	public Entity getCodex() {
		return codex;
	}
	public void setCodex(Entity codex) {
		this.codex = codex;
	}
	public Entity getDigi() {
		return digi;
	}
	public void setDigi(Entity digi) {
		this.digi = digi;
	}
	public List<WitnessItem> getWitnessList() {
		return witnessList;
	}
	public void setWitnessList(List<WitnessItem> witnessList) {
		this.witnessList = witnessList;
	}
	public void setCodexPublic(boolean codexPublic) {
		this.codexPublic = codexPublic;
	}
	public Long getDynamicCodexPageId() {
		return dynamicCodexPageId;
	}
	public Long getDynamicWitnessPageId() {
		return dynamicWitnessPageId;
	}
	
	public class WitnessItem{
		private String label;
		private Long id;
		
		public WitnessItem(Long id, String label){
			this.id = id;
			this.label = RomanizationLoC.convert(label);
		}
		
		public String getLabel() {
			return label;
		}

		public Long getId() {
			return id;
		}
	}
}
