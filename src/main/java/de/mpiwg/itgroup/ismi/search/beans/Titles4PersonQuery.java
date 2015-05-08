package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.NormalizerUtils;
import org.mpi.openmind.search.utils.ResultEntry;
import org.mpi.openmind.search.utils.SAttributeMultipleName;
import org.mpi.openmind.search.utils.SAttributeUniqueName;
import org.mpi.openmind.search.utils.SEntity;
import org.mpi.openmind.search.utils.SRelLongKey;
import org.mpi.openmind.search.utils.SRelation;
import org.mpi.openmind.search.utils.SRelationMultipleName;
import org.mpi.openmind.search.utils.SRelationUniqueName;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class Titles4PersonQuery extends AbstractQuery implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5342126098161866749L;
	private String personName;
	private String roleName = "Author";
	private static List<SelectItem> roleList;
	private List<SRelLongKey> personTitleKeyList;
	
	private boolean displayCodex = false;
	private boolean displayAlias = false;
	
	public Titles4PersonQuery(ApplicationBean appBean){
		super(appBean);
		this.exportUrlRoot = ApplicationBean.urlISMIExportServiceAuthors;
	}
	
	//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
	static{
		roleList = new ArrayList<SelectItem>();
		roleList.add(new SelectItem("Author"));
		roleList.add(new SelectItem("Annotator"));
		roleList.add(new SelectItem("Copyist"));
		roleList.add(new SelectItem("Corrector"));
		roleList.add(new SelectItem("Dedicatee"));
		roleList.add(new SelectItem("Illuminator"));
		roleList.add(new SelectItem("Illustrator"));
		roleList.add(new SelectItem("Owner"));
		roleList.add(new SelectItem("Patron"));
		roleList.add(new SelectItem("Inspector"));
	}
	
	@Override
	public void reset(){
		super.reset();
		this.personName = null;
		this.roleName = "Author";
		this.rs = null;
		this.personTitleKeyList = null;
		this.displayAlias = false;
		this.displayCodex = false;
	}
	
	@Override
	protected void search(){
		
		this.displayAlias = false;
		this.displayCodex = false;
		
		List<ResultEntry> rs0 = this.execute01(personName, roleName);
		List<ResultEntry> rs1 = null;
		if(StringUtils.isNotEmpty(personName)){
			rs1 = this.execute02(personName, roleName);
		}
		rs = loadResultSet(rs0, rs1);
		
		List<Long> idLongList = new ArrayList<Long>();
		for(Object e : rs){
			Titles4PersonEntry entry = (Titles4PersonEntry)e;
			if(!idLongList.contains(entry.getPersonId()))
				idLongList.add(entry.getPersonId());
		}
		this.idList = ApplicationBean.generateIdList(idLongList);
		
		this.exportDirkUrl = ApplicationBean.generateExportURL(exportUrlRoot, idList, "xml");
	}
	
	
	
	private List<Titles4PersonEntry> loadResultSet(List<ResultEntry> rs0, List<ResultEntry> rs1){
		List<Titles4PersonEntry> currentRS = new ArrayList<Titles4PersonEntry>();
		
		List<Long> personIdList = new ArrayList<Long>();
		
		
		for(ResultEntry re : rs0){
			Entity person = getOm().getEntityById(re.getEntMap().get(1));
			personIdList.add(person.getId());
			currentRS.addAll(getEntryFromPerson(person, null, null, this.roleName));
		}
		
		if(rs1 != null){
			for(ResultEntry re : rs1){
				Entity person = getOm().getEntityById(re.getEntMap().get(1));
				if(!personIdList.contains(person.getId())){
					String alias = getOm().getEntityById(re.getEntMap().get(2)).getOwnValue();
					String alias2Person = re.getRel(2, 1);
					currentRS.addAll(getEntryFromPerson(person, alias, alias2Person, this.roleName));
					this.displayAlias = true;
				}
			}
		}
			
		return currentRS;
	}
	
	
	/**
	 * Possible ways to find a title from a given person
	 * 1- TEXT is_exemplar_of WITNESS is_part_of CODEX owned_by PERSON
	 * 2-
	 * TEXT was_created_by PERSON
	 * TEXT had_patron PERSON
	 * TEXT was_dedicated_to PERSON
	 */
	private List<Titles4PersonEntry> getEntryFromPerson(Entity person, String alias, String alias2Person, String role){
		//rs.add(new Titles4PersonEntry(person, title, title2Person, codex, codex2Person, alias, alias2Person, role, subject, ));
		List<Titles4PersonEntry> rs = new ArrayList<Titles4PersonQuery.Titles4PersonEntry>();
		
		List<Entity> entList = getOm().getSourcesForTargetRelation(person.getId(), "was_created_by", "TEXT", -1);
		for(Entity title : entList){
			String subject = getSubject(title);
			rs.add(new Titles4PersonEntry(person, title, alias, alias2Person, role, subject, "TEXT was_created_by PERSON"));
		}
		
		entList = getOm().getSourcesForTargetRelation(person.getId(), "had_patron", "TEXT", -1);
		for(Entity title : entList){
			String subject = getSubject(title);
			rs.add(new Titles4PersonEntry(person, title, alias, alias2Person, role, subject, "TEXT had_patron PERSON"));
		}
		
		
		entList = getOm().getSourcesForTargetRelation(person.getId(), "was_dedicated_to", "TEXT", -1);
		for(Entity title : entList){
			String subject = getSubject(title);
			rs.add(new Titles4PersonEntry(person, title, alias, alias2Person, role, subject, "TEXT was_dedicated_to PERSON"));
		}		
		
		entList = getOm().getSourcesForTargetRelation(person.getId(), "owned_by", "CODEX", -1);
		for(Entity codex : entList){
			List<Entity> witnessList = getOm().getSourcesForTargetRelation(codex.getId(), "is_part_of", "WITNESS", -1);
			for(Entity witness : witnessList){
				List<Entity> textList = getOm().getTargetsForSourceRelation(witness.getId(), "is_exemplar_of", "TEXT", -1);
				for(Entity title : textList){
					String subject = getSubject(title); 
					rs.add(new Titles4PersonEntry(person, title, codex, witness, alias, alias2Person, role, subject, 
							"TEXT is_exemplar_of WITNESS is_part_of CODEX owned_by PERSON"));
					this.displayCodex = true;
					System.out.println("this.displayCodex = true;");
				}
			}	
		}
		
		return rs;
	}
	
	private String getSubject(Entity title){
		List<Entity> subjectList = getOm().getTargetsForSourceRelation(title.getId(), "has_subject", "SUBJECT", 1);
		return (subjectList.size() == 0) ? null : subjectList.get(0).getOwnValue();
	}
	
	private List<ResultEntry> execute01(String personName, String roleName){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		//The user can select between: 
		//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", roleName));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		if(StringUtils.isNotEmpty(personName)){
			person.addAtt(new SAttributeMultipleName(personName, "name_translit", "name"));
		}
		entFilters.add(person);
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		relFilters.add(has_role);
		
		return this.appBean.getSS().search(entFilters, relFilters);		
	}
	
	private List<ResultEntry> execute02(String personName, String roleName){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		//The user can select between: 
		//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", roleName));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		entFilters.add(person);
		
		SEntity alias = new SEntity(2, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", personName));
		entFilters.add(alias);
		
		SRelationMultipleName is_alias = new SRelationMultipleName(alias, person, "is_alias_name_of", "is_prime_alias_name_of");
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		relFilters.add(has_role);
		relFilters.add(is_alias);
		
		return this.appBean.getSS().search(entFilters, relFilters);		
	}
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<SRelLongKey> getPersonTitleKeyList() {
		return personTitleKeyList;
	}

	public void setPersonTitleKeyList(List<SRelLongKey> personTitleKeyList) {
		this.personTitleKeyList = personTitleKeyList;
	}

	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public boolean isDisplayCodex() {
		return displayCodex;
	}

	public boolean isDisplayAlias() {
		return displayAlias;
	}



	public class Titles4PersonEntry implements Comparable<Titles4PersonEntry>, Serializable{
		private static final long serialVersionUID = 7798695003500406910L;
		
		private Long personId;
		private String personOv;
		private String personNOv;
		
		private Long titleId;
		private String titleOv;
		private String titleNOv;
		
		private Long codexId;
		private String codexOv;
		private String codex2Person;
		
		private Long witnessId;
		private String witnessOv;
		private String witnessNOv;
		
		private String alias;
		private String alias2Person;
		
		private String role;
		//private String title2Person;
		private String subject;
		
		
		
		private String query;
		
		public Titles4PersonEntry(
				Entity person,
				Entity title,
				Entity codex,
				Entity witness,
				String alias,
				String alias2Person,
				String role,
				String subject,
				String query
				){
			
			set(person, title);
			this.alias = alias;
			this.alias2Person = alias2Person;
			this.role = role;
			this.subject = subject;
			this.query = query;
			if(codex != null){
				this.codexId = codex.getId();
				this.codexOv = codex.getOwnValue();
			}
			if(witness != null){
				this.witnessId = witness.getId();
				this.witnessOv = witness.getOwnValue();
				this.witnessNOv = witness.getNormalizedOwnValue();
			}
		}
		
		public Titles4PersonEntry(
				Entity person,
				Entity title,
				String alias,
				String alias2Person,
				String role,
				String subject,
				String query
				){
			
			set(person, title);
			this.alias = alias;
			this.alias2Person = alias2Person;
			this.role = role;
			this.subject = subject;
			this.query = query;
		}
		
		private void set(Entity person, Entity title){
			this.personId = person.getId();
			this.personOv = person.getOwnValue();
			this.personNOv = person.getNormalizedOwnValue();
			
			this.titleId = title.getId();
			this.titleOv = title.getOwnValue();
			this.titleNOv = title.getNormalizedOwnValue();
		}
		
		public Long getPersonId() {
			return personId;
		}

		public String getPersonOv() {
			return personOv;
		}

		public String getPersonNOv() {
			return personNOv;
		}

		public String getAlias() {
			return alias;
		}

		public String getAlias2Person() {
			return alias2Person;
		}

		public Long getTitleId() {
			return titleId;
		}

		public String getTitleOv() {
			return titleOv;
		}

		public String getTitleNOv() {
			return titleNOv;
		}

		public String getRole() {
			return role;
		}

		public String getSubject() {
			return subject;
		}
		
		public Long getCodexId() {
			return codexId;
		}

		public String getCodexOv() {
			return codexOv;
		}

		
		public String getCodex2Person() {
			return codex2Person;
		}
		
		public String getQuery() {
			return query;
		}
		
		public Long getWitnessId() {
			return witnessId;
		}

		public String getWitnessOv() {
			return witnessOv;
		}

		public String getWitnessNOv() {
			return witnessNOv;
		}

		@Override
		public int compareTo(Titles4PersonEntry o) {
			if(!this.personId.equals(o.personId)){
				
				int comparisonPerson = NormalizerUtils.normalizedToCompare(personNOv).compareTo(
						NormalizerUtils.normalizedToCompare(o.personNOv));
				if(comparisonPerson != 0){
					return comparisonPerson;
				}else{
					if(StringUtils.isNotEmpty(role) && StringUtils.isNotEmpty(o.role)){
						int comparisonRole = this.role.compareTo(o.role);
						if(comparisonRole != 0){
							return comparisonRole;
						}
					}else{
						if(StringUtils.isNotEmpty(role)){
							return -1;
						}else if(StringUtils.isNotEmpty(o.role)){
							return 1;
						}	
					}
				}
			}else{
				if(!this.titleId.equals(o.titleId)){
					//comparing subject
					if(StringUtils.isNotEmpty(subject) && StringUtils.isNotEmpty(o.subject)){
						int comparisonSubject = this.subject.compareTo(o.subject);
						if(comparisonSubject != 0){
							return comparisonSubject;
						}
					}else{
						if(StringUtils.isNotEmpty(subject)){
							return -1;
						}else if(StringUtils.isNotEmpty(o.subject)){
							return 1;
						}	
					}	
					//comparing title
					int comparisonTitle = NormalizerUtils.normalizedToCompare(titleNOv).compareTo(
							NormalizerUtils.normalizedToCompare(o.titleNOv));
					return comparisonTitle;
				}
			}
			return 0;
		}
	}
	
}
