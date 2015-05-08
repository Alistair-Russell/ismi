package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.utils.NormalizerUtils;
import org.mpi.openmind.repository.utils.TransliterationUtil;
import org.mpi.openmind.search.utils.ResultEntry;
import org.mpi.openmind.search.utils.SAttributeMultipleName;
import org.mpi.openmind.search.utils.SAttributeUniqueName;
import org.mpi.openmind.search.utils.SEntity;
import org.mpi.openmind.search.utils.SRelLongKey;
import org.mpi.openmind.search.utils.SRelation;
import org.mpi.openmind.search.utils.SRelationMultipleName;
import org.mpi.openmind.search.utils.SRelationUniqueName;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

/**
 * 7) Bring up people associated with a particular witness 
 * (not just one huge list of people, but be able to distinguish owner, say, from commentator)
 * @author jurzua
 *
 */
public class SampleSearch06 extends AbstractQuery implements Serializable{

	private static final long serialVersionUID = 3749889381908517654L;
	private String personName;
	private String roleName = "Author";
	private static List<SelectItem> roleList;
	
	
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
	
	
	public SampleSearch06(ApplicationBean appBean){
		super(appBean);
		this.exportUrlRoot = ApplicationBean.urlISMIExportServiceAuthors;
	}
	
	@Override
	public void reset(){
		super.reset();
		this.personName = null;
		this.roleName = "Author";
		this.rs = null;
		this.personTitleKeyList = null;
	}
	
	@Override
	protected void search(){
		
		List<ResultEntry> rs0 = this.execute01(personName, roleName);
		List<ResultEntry> rs1 = null;
		if(StringUtils.isNotEmpty(personName)){
			rs1 = this.execute02(personName, roleName);
		}
		rs = loadResultSet(rs0, rs1);
		
		List<Long> idLongList = new ArrayList<Long>();
		for(Object e : rs){
			ResultEntry06 entry = (ResultEntry06)e;
			if(!idLongList.contains(entry.getPersonId()))
				idLongList.add(entry.getPersonId());
		}
		this.idList = ApplicationBean.generateIdList(idLongList);
		
		this.exportDirkUrl = ApplicationBean.generateExportURL(exportUrlRoot, idList, "xml");
	}
	
	private List<ResultEntry06> loadResultSet(List<ResultEntry> rs0, List<ResultEntry> rs1){
		List<ResultEntry06> currentRs = new ArrayList<SampleSearch06.ResultEntry06>();
		personTitleKeyList = new ArrayList<SRelLongKey>();
		
		for(ResultEntry re : rs0){
			
			Entity person = getOm().getEntityById(re.getEntMap().get(1));
			Entity title = getOm().getEntityById(re.getEntMap().get(2));
			
			String alias = null;
			String alias2Person = null;
			String role = this.roleName;
			String title2Person = re.getRel(2, 1);
			List<Entity> subjectList = getOm().getTargetsForSourceRelation(title.getId(), "has_subject", "SUBJECT", 1);
			String subject = (subjectList.size() == 0) ? null : subjectList.get(0).getOwnValue();
			
			currentRs.add(new ResultEntry06(person, title, alias, alias2Person, role, title2Person, subject));
			putPersonTitleKey(person.getId(), title.getId());
		}
		if(rs1 != null){
			for(ResultEntry re : rs1){
				Entity person = getOm().getEntityById(re.getEntMap().get(1));
				Entity title = getOm().getEntityById(re.getEntMap().get(3));
				if(!containsPersonTitleKey(person.getId(), title.getId())){
					
					String alias = getOm().getEntityById(re.getEntMap().get(2)).getOwnValue();
					String alias2Person = re.getRel(2, 1);
					String role = this.roleName;
					String title2Person = re.getRel(3, 1);
					List<Entity> subjectList = getOm().getTargetsForSourceRelation(title.getId(), "has_subject", "SUBJECT", 1);
					String subject = (subjectList.size() == 0) ? null : subjectList.get(0).getOwnValue();
					
					currentRs.add(new ResultEntry06(person, title, alias, alias2Person, role, title2Person, subject));
					putPersonTitleKey(person.getId(), title.getId());	
				}
			}	
		}
		
		Collections.sort(currentRs);
		
		return currentRs;
	}
	
	private List<SRelLongKey> personTitleKeyList;
	private boolean containsPersonTitleKey(Long personId, Long titleId){
		return personTitleKeyList.contains(new SRelLongKey(personId, titleId));
	}
	private void putPersonTitleKey(Long personId, Long titleId){
		personTitleKeyList.add(new SRelLongKey(personId, titleId));
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
		
		SEntity text = new SEntity(2, "TEXT");
		entFilters.add(text);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName text_to_person = new SRelationMultipleName(text, person, "was_created_by", "had_patron", "was_dedicated_to");
		relFilters.add(text_to_person);
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
		
		SEntity text = new SEntity(3, "TEXT");
		entFilters.add(text);
		
		
		SRelationMultipleName is_alias = new SRelationMultipleName(alias, person, "is_alias_name_of", "is_prime_alias_name_of");
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName text_to_person = new SRelationMultipleName(text, person, "was_created_by", "had_patron", "was_dedicated_to");
		relFilters.add(text_to_person);
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
	
	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public class ResultEntry06 implements Comparable<ResultEntry06>, Serializable{
		private static final long serialVersionUID = 3814421582310240565L;
		
		private Long personId;
		private String personOv;
		private String personNOv;
		private String alias;
		private String alias2Person;
		private Long titleId;
		private String titleOv;
		private String titleNOv;
		private String role;
		private String title2Person;
		private String subject;
		
		public ResultEntry06(
				Entity person,
				Entity title,
				String alias,
				String alias2Person,
				String role,
				String title2Person,
				String subject
				){
			
			set(person, title);
			this.alias = alias;
			this.alias2Person = alias2Person;
			this.role = role;
			this.title2Person = title2Person;
			this.subject = subject;
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

		public String getTitle2Person() {
			return title2Person;
		}

		public String getSubject() {
			return subject;
		}

		@Override
		public int compareTo(ResultEntry06 o) {
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
