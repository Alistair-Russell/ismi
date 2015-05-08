package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.search.utils.ResultEntry;
import org.mpi.openmind.search.utils.SAttributeMultipleName;
import org.mpi.openmind.search.utils.SAttributeUniqueName;
import org.mpi.openmind.search.utils.SEntity;
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
public class SampleSearch07 extends AbstractQuery implements Serializable{
	private static final long serialVersionUID = 55883896148547918L;

	private String personName;
	private String roleName = "Author";
	private static List<SelectItem> roleList;
	private List<ResultEntry07> rs;
	
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
	
	
	public SampleSearch07(ApplicationBean appBean){
		super(appBean);
	}
	
	@Override
	public void reset(){
		super.reset();
		this.personName = null;
		this.roleName = "Author";
		this.rs = null;
	}
	
	@Override
	protected void search(){
		List<ResultEntry> rs0 = this.execute(personName, roleName);
		//this.printRs(rs, this.appBean.getWrapper());
		
		this.rs = new ArrayList<SampleSearch07.ResultEntry07>();
		
		int count = 0;
		for(ResultEntry re : rs0){
			Entity person = getOm().getEntityById(re.getEntMap().get(1));
			Entity witness = getOm().getEntityById(re.getEntMap().get(2));
			String witness2Person = re.getRel(2, 1);
			rs.add(new ResultEntry07(
					person.getId(), person.getOwnValue(), 
					witness.getId(), witness.getOwnValue(), 
					this.roleName, witness2Person));
			count++;
			if(count >= MAX_RS){
				break;
			}
		}
	}
	
	private List<ResultEntry> execute(String personName, String roleName){
		
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
		
		SEntity witness = new SEntity(2, "WITNESS");
		entFilters.add(witness);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName witness_to_person = new SRelationMultipleName(witness, person, "had_patron", "was_copied_by", "was_created_by", "was_studied_by");
		relFilters.add(witness_to_person);
		relFilters.add(has_role);
		
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
	
	public List<ResultEntry07> getRs() {
		return rs;
	}

	@Override
	public Integer getRsSize(){
		if(rs != null){
			return rs.size();
		}
		return 0;
	}

	public class ResultEntry07 implements Serializable{
		private static final long serialVersionUID = -3582904838999322869L;
		
		private Long personId;
		private String personOv;
		private Long witnessId;
		private String witnessOv;
		private String role;
		private String witness2Person;
		
		public ResultEntry07(
				Long personId, String personOv, 
				Long witnessId, String witnessOv,
				String role, String witness2Person){
			
			this.personId = personId;
			this.personOv = personOv;
			this.witnessId = witnessId;
			this.witnessOv = witnessOv;
			this.role = role;
			this.witness2Person = witness2Person;
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

		public Long getWitnessId() {
			return witnessId;
		}

		public void setWitnessId(Long witnessId) {
			this.witnessId = witnessId;
		}

		public String getWitnessOv() {
			return witnessOv;
		}

		public void setWitnessOv(String witnessOv) {
			this.witnessOv = witnessOv;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getWitness2Person() {
			return witness2Person;
		}

		public void setWitness2Person(String witness2Person) {
			this.witness2Person = witness2Person;
		}
		
		
	}
}
