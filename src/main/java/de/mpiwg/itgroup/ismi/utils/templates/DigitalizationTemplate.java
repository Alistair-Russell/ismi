package de.mpiwg.itgroup.ismi.utils.templates;

import java.util.List;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;

public class DigitalizationTemplate implements Comparable<DigitalizationTemplate>{
	
	private String codexOv;
	private String codexId;
	private Entity entity;
	private String pages;
	private List<Entity> titlesInCodex;
	
	private WrapperService om;
	private boolean initialized = false;
	
	private boolean hasCodex = false;
	
	public DigitalizationTemplate(Entity digi, WrapperService om){
		this.entity = digi;
		this.om = om;
	}
	
	public void init(){
		this.initialized = true;
		this.pages = om.getAttributeByName(entity.getId(), "num_files").getValue();
		
		List<Entity> list = om.getTargetsForSourceRelation(entity, "is_digitalization_of", "CODEX", 1);
		if(list.size() > 0){
			Entity codex = list.get(0);
			this.codexOv = codex.getOwnValue();
			this.codexId = codex.getId().toString();
			this.titlesInCodex = om.getSourcesForTargetRelation(codex, "is_part_of", "WITNESS", -1);
			this.hasCodex = true;
		}
	}

	public boolean isHasCodex(){
		return this.hasCodex;
	}
	
	public String getCodexId() {
		if(!initialized)
			this.init();
		return codexId;
	}

	public Entity getEntity() {
		return entity;
	}
	
	public String getCodexOv(){
		return this.codexOv;
	}

	public String getPages() {
		if(!initialized)
			this.init();
		return pages;
	}

	public List<Entity> getTitlesInCodex() {
		if(!initialized)
			this.init();
		return titlesInCodex;
	}
	
	public int compareTo(DigitalizationTemplate o) { 
		return this.entity.getOwnValue().compareTo(o.getEntity().getOwnValue()); 
	}
}
