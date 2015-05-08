package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.search.utils.ResultEntry;
import org.mpi.openmind.search.utils.SAttributeMultipleName;
import org.mpi.openmind.search.utils.SEntity;
import org.mpi.openmind.search.utils.SRelation;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class CodexOwnershipQuery  extends AbstractQuery{

	private static final long serialVersionUID = 5481872257359678512L;

	private String personName;
	private String codexShelfMark;
	
	public CodexOwnershipQuery(ApplicationBean appBean){
		super(appBean);
		this.exportUrlRoot = ApplicationBean.urlISMIExportServiceAuthors;
	}
	
	@Override
	public void reset(){
		super.reset();
		this.rs = null;
		this.personName = null;
		this.codexShelfMark = null;
	}
	
	@Override
	protected void search() throws Exception{
		
		List<Relation> wasOwnerByList = appBean.getWrapper().getRelation("owned_by", "CODEX", "PERSON");
		List<Long> personCandidates = null;
		List<Long> codexCadidates = null;
		
		if(StringUtils.isNotEmpty(personName)){
			personCandidates = getPersonCadidates(personName);	
		}
		if(StringUtils.isNotEmpty(codexShelfMark)){
			codexCadidates = getCodexCadidates(codexShelfMark);	
		}
		
		rs = this.loadResultSet(wasOwnerByList, personCandidates, codexCadidates);
		
		/*
		 * TODO there is report for it???
		List<Long> idLongList = new ArrayList<Long>();
		for(Object e : rs){
			Titles4PersonEntry entry = (Titles4PersonEntry)e;
			if(!idLongList.contains(entry.getPersonId()))
				idLongList.add(entry.getPersonId());
		}
		this.idList = ApplicationBean.generateIdList(idLongList);
		
		this.exportDirkUrl = ApplicationBean.generateExportURL(exportUrlRoot, idList, "xml");
		*/
	}
	
	private List<CodexOwnershipEntry> loadResultSet(List<Relation> wasOwnerByList, 
			List<Long> personCandidates, 
			List<Long> codexCadidates){
		
		List<CodexOwnershipEntry> list = new ArrayList<CodexOwnershipQuery.CodexOwnershipEntry>();
		
		if(!wasOwnerByList.isEmpty()){
			for(Relation rel : wasOwnerByList){
				if((codexCadidates == null || codexCadidates.contains(rel.getSourceId())) && 
						(personCandidates == null ||personCandidates.contains(rel.getTargetId()))){
					list.add(new CodexOwnershipEntry(
							appBean.getWrapper().getEntityById(rel.getSourceId()), appBean.getWrapper().getEntityById(rel.getTargetId()),
							appBean.getWrapper()));
				}
			}
		}
		
		return list;
	}
	
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getCodexShelfMark() {
		return codexShelfMark;
	}

	public void setCodexShelfMark(String codexShelfMark) {
		this.codexShelfMark = codexShelfMark;
	}

	private List<Long> getPersonCadidates(String personName){
		List<SEntity> entFilters = new ArrayList<SEntity>();
		
		//TODO
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(0, "PERSON");
		if(StringUtils.isNotEmpty(personName)){
			person.addAtt(new SAttributeMultipleName(personName, "name_translit", "name"));
		}
		entFilters.add(person);
		
		List<Long> idList = new ArrayList<Long>();
		for(ResultEntry entry : this.appBean.getSS().search(entFilters, new ArrayList<SRelation>())){
			idList.add(entry.getEntMap().get(0));
		}
		return idList;		
	}

	private List<Long> getCodexCadidates(String shelfMark){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		
		SEntity codex = new SEntity(0, "CODEX");
		if(StringUtils.isNotEmpty(shelfMark)){
			codex.addAtt(new SAttributeMultipleName(shelfMark, "identifier"));
		}
		entFilters.add(codex);
		
		List<Long> idList = new ArrayList<Long>();
		for(ResultEntry entry : this.appBean.getSS().search(entFilters, new ArrayList<SRelation>())){
			idList.add(entry.getEntMap().get(0));
		}
		return idList;		
	}
	
	public class CodexOwnershipEntry implements Serializable{
		
		private static final long serialVersionUID = -6118835259154299870L;
		
		private Long personId;
		private String personOv;
		private Long codexId;
		private String codexOv;
		private String shelfMark;
		private String collection;
		
		public CodexOwnershipEntry(Entity codex, Entity person, WrapperService ws){
			this.personId = person.getId();
			this.personOv = person.getOwnValue();
			this.codexId = codex.getId();
			this.codexOv = codex.getOwnValue();
			//this.shelfMark = (codex.getAttributeByName("identifier") != null) ? codex.getAttributeByName("identifier").getOwnValue() : null;
			try{
				String[] array = this.codexOv.split("_");
				if(array.length > 0){
					this.collection = array[0];
					this.shelfMark = array[1];
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Long getPersonId() {
			return personId;
		}

		public void setPersonId(Long personId) {
			this.personId = personId;
		}

		public String getPersonOv() {
			return personOv;
		}

		public void setPersonOv(String personOv) {
			this.personOv = personOv;
		}

		public Long getCodexId() {
			return codexId;
		}

		public void setCodexId(Long codexId) {
			this.codexId = codexId;
		}

		public String getCodexOv() {
			return codexOv;
		}

		public void setCodexOv(String codexOv) {
			this.codexOv = codexOv;
		}

		public String getShelfMark() {
			return shelfMark;
		}

		public void setShelfMark(String shelfMark) {
			this.shelfMark = shelfMark;
		}

		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}
	}
	
	
}
