package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.search.utils.ResultEntry;
import org.mpi.openmind.search.utils.SAttribute;
import org.mpi.openmind.search.utils.SAttributeMultipleName;
import org.mpi.openmind.search.utils.SAttributeMultipleValue;
import org.mpi.openmind.search.utils.SAttributeUniqueName;
import org.mpi.openmind.search.utils.SEntity;
import org.mpi.openmind.search.utils.SRelation;
import org.mpi.openmind.search.utils.SRelationMultipleName;
import org.mpi.openmind.search.utils.SRelationUniqueName;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class AdvancedSearchBean extends AbstractISMIBean implements Serializable{
	
	private static Logger logger = Logger.getLogger(AdvancedSearchBean.class);
	
	private static final long serialVersionUID = 1L;
	
	private String currentTemplate;
	private static List<SelectItem> templateList;
	
	private transient Witness4TitleQuery witness4TitleQuery;
	//private SampleSearch06 sample06;
	private SampleSearch07 sample07;
	private Titles4PersonQuery titles4PersonQuery;
	private CodexOwnershipQuery codexOwnershipQuery;
	
	static{
		templateList = new ArrayList<SelectItem>();
		//templateList.add(new SelectItem("sample01"));
		templateList.add(new SelectItem("witness4Title", "Witnesses for a title"));
		//templateList.add(new SelectItem("sample03"));
		//templateList.add(new SelectItem("sample04"));
		//templateList.add(new SelectItem("sample05"));
		templateList.add(new SelectItem("titles4Person", "Titles for a person"));
		templateList.add(new SelectItem("codexOwnership", "Codex ownership"));
		//templateList.add(new SelectItem("sample07"));
		//templateList.add(new SelectItem("sample08"));
		//templateList.add(new SelectItem("sample09"));
	}
	
	public AdvancedSearchBean(){
		//logger.info("AdvancedSearchBean");
		this.reset();
	}
	
	public void reset(){
		//logger.info("AdvancedSearchBean.reset()");
		this.witness4TitleQuery = new Witness4TitleQuery(getAppBean());
		this.titles4PersonQuery = new Titles4PersonQuery(getAppBean());
		this.sample07 = new SampleSearch07(getAppBean());
		this.codexOwnershipQuery = new CodexOwnershipQuery(getAppBean());
		currentTemplate = "witness4Title";
	}
	
	public void listenerChange(ValueChangeEvent event){
		/*
		logger.info(event.getOldValue());
		logger.info(event.getNewValue());
		*/
	}
	
	public String actionSearch(){
		this.search();
		return null;
	}
	
	public SampleSearch07 getSample07() {
		return sample07;
	}

	public CodexOwnershipQuery getCodexOwnershipQuery() {
		return codexOwnershipQuery;
	}

	public Witness4TitleQuery getWitness4TitleQuery() {
		return witness4TitleQuery;
	}

	public void search(){
		long start = System.currentTimeMillis();
		
		List<ResultEntry> rs = sample06();
		
		//printRs(rs);

		long end = System.currentTimeMillis();
		logger.info("execution time [ms] = " + (end - start));
		logger.info("Search resultSet size= " + rs.size());
	}

	
	private List<ResultEntry> test01(){
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		SEntity e1 = new SEntity(0, "PERSON");
		e1.addAtt(new SAttributeUniqueName("name_translit", "abu"));
		entFilters.add(e1);
		
		SEntity e2 = new SEntity(1, "ROLE");
		e2.addAtt(new SAttributeUniqueName("name", "Author"));
		entFilters.add(e2);
		
		SEntity e3 = new SEntity(2, "PLACE");
		e3.addAtt(new SAttributeUniqueName("name", "Spain"));
		entFilters.add(e3);
		
		SEntity e4 = new SEntity(3, "ALIAS");
		e4.addAtt(new SAttributeUniqueName("alias", "Samuel"));
		entFilters.add(e4);
		
		SRelationUniqueName has_role = new SRelationUniqueName(e1.getIndex(), e2.getIndex(), "has_role");
		SRelationUniqueName was_born_in = new SRelationUniqueName(e1.getIndex(), e3.getIndex(), "was_born_in");
		SRelationUniqueName is_prime_alias_name_of = new SRelationUniqueName(e4.getIndex(), e1.getIndex(), "is_prime_alias_name_of");
		relFilters.add(has_role);
		relFilters.add(was_born_in);
		relFilters.add(is_prime_alias_name_of);
		
		return getAppBean().getSS().search(entFilters, relFilters);
		
	}
	
	private List<ResultEntry> test02(){
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		SEntity e2 = new SEntity(1, "ALIAS");
		e2.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(e2);
		
		SEntity e1 = new SEntity(0, "TEXT");
		e1.addAtt(new SAttributeUniqueName("full_title_translit", "abu"));
		entFilters.add(e1);
		
		
		SRelationMultipleName hasAlias = new SRelationMultipleName(e2.getIndex(), e1.getIndex(), 
				"is_prime_alias_title_of", 
				"is_alias_title_of",
				"is_alias_incipit_of",
				"is_alias_explicit_of");
		relFilters.add(hasAlias);
		
		return getAppBean().getSS().search(entFilters, relFilters);
	}
	
	private List<ResultEntry> sample01(){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", "Author"));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		person.addAtt(new SAttributeMultipleName("a", "name_translit", "name"));
		entFilters.add(person);
		
		/*
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(alias);
		*/
		
		SEntity title = new SEntity(2, "TEXT");
		//title.addAtt(new SAttributeMultipleName("abu", "full_title_translit", "full_title"));
		entFilters.add(title);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationUniqueName was_created_by = new SRelationUniqueName(title, person, "was_created_by");
		relFilters.add(was_created_by);
		relFilters.add(has_role);
		
		List<ResultEntry> rs = getAppBean().getSS().search(entFilters, relFilters);
		
		return rs;
	}
	
	/**
	 * 5) List of all people who had other “roles” associated with a title (e.g. copyists , owners, patrons, teachers, students, etc.)
	 * @return
	 */
	private List<ResultEntry> sample05(){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeMultipleValue("name", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		person.addAtt(new SAttributeMultipleName("a", "name_translit", "name"));
		entFilters.add(person);
		
		/*
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(alias);
		*/
		
		SEntity title = new SEntity(2, "TEXT");
		//title.addAtt(new SAttributeMultipleName("abu", "full_title_translit", "full_title"));
		entFilters.add(title);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName title_to_person = new SRelationMultipleName(title, person, "had_patron", "was_dedicated_to", "was_created_by");
		relFilters.add(title_to_person);
		relFilters.add(has_role);
		
		List<ResultEntry> rs = getAppBean().getSS().search(entFilters, relFilters);
		
		return rs;
	}
	
	/**
	 * 6) Bring up people associated with a particular title (not just one huge list of people, but be able to distinguish author, student, teacher)
	 * @return
	 */
	private List<ResultEntry> sample06(){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		//The user can select between: 
		//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", "Annotator"));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		person.addAtt(new SAttributeMultipleName("a", "name_translit", "name"));
		entFilters.add(person);
		
		/*
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(alias);
		*/
		
		SEntity title = new SEntity(2, "TEXT");
		title.addAtt(new SAttributeMultipleName("abu", "full_title_translit", "full_title"));
		entFilters.add(title);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName title_to_person = new SRelationMultipleName(title, person, "had_patron", "was_dedicated_to", "was_created_by");
		relFilters.add(title_to_person);
		relFilters.add(has_role);
		
		List<ResultEntry> rs = getAppBean().getSS().search(entFilters, relFilters);
		
		return rs;
		
	}
	
	/**
	 * 7) Bring up people associated with a particular witness (not just one huge list of people, but be able to distinguish owner, say, from commentator)
	 * @return
	 */
	private List<ResultEntry> sample07(){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		//The user can select between: 
		//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", "Owner"));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		person.addAtt(new SAttributeMultipleName("a", "name_translit", "name"));
		entFilters.add(person);
		
		/*
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(alias);
		*/
		
		SEntity witness = new SEntity(2, "WITNESS");
		//witness.addAtt(new SAttributeMultipleName("abu", "full_title_translit", "full_title"));
		entFilters.add(witness);
		
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationMultipleName witness_to_person = new SRelationMultipleName(witness, person, "had_patron", "was_copied_by", "was_created_by", "was_studied_by");
		relFilters.add(witness_to_person);
		relFilters.add(has_role);
		
		List<ResultEntry> rs = getAppBean().getSS().search(entFilters, relFilters);
		
		return rs;
		
	}
	
	/**
	 * 9) Bring up all owners of a particular codex
	 * @return
	 */
	private List<ResultEntry> sample08(){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		//The user can select between: 
		//"Author", "Annotator", "Copyist", "Corrector", "Dedicatee", "Illuminator", "Illustrator", "Owner", "Patron", "inspector"
		SEntity role = new SEntity(0, "ROLE");
		role.addAtt(new SAttributeUniqueName("name", "Owner"));
		entFilters.add(role);
		
		//alias, "is_prime_alias_name_of", "PERSON"
		//alias, "is_alias_name_of", "PERSON"
		SEntity person = new SEntity(1, "PERSON");
		person.addAtt(new SAttributeMultipleName("a", "name_translit", "name"));
		entFilters.add(person);
		
		/*
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", "abu"));
		entFilters.add(alias);
		*/
		
		SEntity codex = new SEntity(2, "CODEX");
		//witness.addAtt(new SAttributeMultipleName("abu", "full_title_translit", "full_title"));
		entFilters.add(codex);
		
		SRelationUniqueName has_role = new SRelationUniqueName(person, role, "has_role");
		SRelationUniqueName was_owned_by = new SRelationUniqueName(codex, person, "was_owned_by");
		relFilters.add(was_owned_by);
		relFilters.add(has_role);
		
		List<ResultEntry> rs = getAppBean().getSS().search(entFilters, relFilters);
		
		return rs;
		
	}

	/*
	private void printRs(List<ResultEntry> rs){
		logger.info("--------------");		
		for(ResultEntry entry : rs){
			for(Entry<Integer, Long> ent : entry.getEntMap().entrySet()){
				logger.info(ent.getKey() + ") " + getWrapper().getEntityById(ent.getValue()).toSmallString());		
			}
		}
		logger.info("--------------");
	}*/
	
	public String getCurrentTemplate() {
		return currentTemplate;
	}

	public void setCurrentTemplate(String currentTemplate) {
		this.currentTemplate = currentTemplate;
	}

	public List<SelectItem> getTemplateList() {
		return templateList;
	}

	public Titles4PersonQuery getTitles4PersonQuery() {
		return titles4PersonQuery;
	}	
}
