package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.bo.utils.EntitySortByNormalizedOwnValue;
import org.mpi.openmind.repository.services.utils.AttributeFilter;

import de.mpiwg.itgroup.ismi.auxObjects.ResultSet;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;

public class SimpleSearchBean extends AbstractISMIBean implements Serializable{

	private static final long serialVersionUID = -1363042229412197533L;

	private static Logger logger = Logger.getLogger(SimpleSearchBean.class);
	
	public static String NO_ROLE_PERSON = "Person without role";
	
	private String inputMethod = "latin";
	private String searchTerm;
	
	public static List<SelectItem> modeList = new ArrayList<SelectItem>();
	public static String AUTHORS_TITLES = "People and Titles";
	public static String TITLES = "Titles";
	public static String AUTHORS = "People";
	
	public static List<AttributeFilter> filters = new ArrayList<AttributeFilter>();
	public static List<AttributeFilter> titlesFilters = new ArrayList<AttributeFilter>();
	public static List<AttributeFilter> authorsFilters = new ArrayList<AttributeFilter>();
	private static AttributeFilter filter1 = new AttributeFilter();
	private static AttributeFilter filter2 = new AttributeFilter();
	private static AttributeFilter filter3 = new AttributeFilter();
	private static AttributeFilter filter4 = new AttributeFilter();
	private static AttributeFilter filter5 = new AttributeFilter();
	private static AttributeFilter filter6 = new AttributeFilter();
	private static AttributeFilter filter7 = new AttributeFilter();
	
	private Map<String, ResultSet> resultMap;
	private List<String> resultSetNames;

	public String selectedMode;

	static{
		modeList.add(new SelectItem(AUTHORS));
		modeList.add(new SelectItem(TITLES));
		modeList.add(new SelectItem(AUTHORS_TITLES));
	}
	
	public SimpleSearchBean() {
		this.selectedMode = modeList.get(0).getValue().toString();
	}
	
	
	public void listenerSearch(ActionEvent event){
		try {
			this.simpleSearchAction();	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	public void listenerModeValueChange(ValueChangeEvent event){
		this.selectedMode = (String)event.getNewValue();
	}
	
	public void setInputMethod(String inputMethod) {
		this.inputMethod = inputMethod;
	}
	public String getInputMethod() {
		return inputMethod;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	
	public void simpleSearchAction() throws Exception{
		search0(getSearchTerm(), getSelectedMode());
	}
	
	public List<SelectItem> getModeList() {
		return modeList;
	}

	public String getSelectedMode() {
		return selectedMode;
	}
	public void setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
	}
	
	//######################################################
	//######################################################
	//######################################################
	
	public static Long SEC_05 = new Long(5000);
	public static Long SEC_10 = new Long(10000);
	public static Long SEC_20 = new Long(20000);
	public static Long SEC_30 = new Long(30000);

	private int maxResult = -1	;
	private int counter = 0;
	
	public void search0(String term, String mode) throws Exception{

        if (StringUtils.isNotEmpty(term)) {
        	StringBuilder sb = new StringBuilder();	
        	long start = System.currentTimeMillis();
        	this.resultMap = new HashMap<String, ResultSet>();
        	List<Long> usedIdList = new ArrayList<Long>(); 
        	this.counter = 0;
        	
        	boolean includeTitles = (SimpleSearchBean.TITLES.equals(mode) || SimpleSearchBean.AUTHORS_TITLES.equals(mode)) ? true : false;
        	boolean includeAuthors = (SimpleSearchBean.AUTHORS.equals(mode) || SimpleSearchBean.AUTHORS_TITLES.equals(mode) ? true : false);
        	
        	Map<Entity, Attribute> map = new HashMap<Entity, Attribute>();
        	if(includeAuthors && includeTitles){
        		map = getWrapper().searchEntityByAttributeFilter0(term, filters, -1);
        	}else if(includeAuthors){
        		map = getWrapper().searchEntityByAttributeFilter0(term, authorsFilters, -1);
        	}else if(includeTitles){
        		map = getWrapper().searchEntityByAttributeFilter0(term, titlesFilters, -1);
        	}
        	//System.out.println("getWrapper().searchEntityByAttributeFilter0= " + (System.currentTimeMillis() - start));
        	sb.append("\n\n***********************\n");
        	sb.append("getWrapper().searchEntityByAttributeFilter0= " + (System.currentTimeMillis() - start) + "\n");
        	
        	for(Entity ent : map.keySet()){
        		if(counter == 1000 || (System.currentTimeMillis() - start) > SEC_20){
        			break;
        		}
        		Attribute att = map.get(ent);
        		if(ent.getObjectClass().equals(ALIAS)){
        			AliasStructure aliasStructure = getAliasStructure(ent, att);
        			if(aliasStructure.target != null && !usedIdList.contains(aliasStructure.target.getId())){
        				usedIdList.add(aliasStructure.target.getId());
        				if(includeAuthors && aliasStructure.target.getObjectClass().equals(PERSON)){
        					this.insertPerson(aliasStructure.target, att, aliasStructure.description);
            			}else if(includeTitles && aliasStructure.target.getObjectClass().equals(TEXT)){
            				this.insertTitle(aliasStructure.target, att, aliasStructure.description);
            			}	
        			}
        		}else if(!usedIdList.contains(ent.getId())){
        			usedIdList.add(ent.getId());
        			String d = att.getObjectClass() + "=" + att.getOwnValue();
        			if(ent.getObjectClass().equals(PERSON)){
            			this.insertPerson(ent, att, d);
            		}else if(ent.getObjectClass().equals(TEXT)){
            			this.insertTitle(ent, att, d);
            		}
        		} 
        	}
        	
           	for(String key : resultMap.keySet()){
           		ResultSet rs = resultMap.get(key);
           		//System.out.println(key + "= " + rs.getResults().size());
        		Collections.sort(rs.getResults(), new EntitySortByNormalizedOwnValue());
        	}
        	
           	this.resultSetNames = new ArrayList<String>(getResultMap().keySet());
        	Collections.sort(this.resultSetNames);
        	
        	
        	long end = System.currentTimeMillis();
        	sb.append("Word: " + term + "\n");
        	sb.append("includeTitles: " + includeTitles + "\n");
        	sb.append("includeAuthors: " + includeAuthors + "\n");
        	sb.append("Simple search time execution= " + (end - start) + "\n");
        	sb.append("***********************\n");
        	sb.append("\n");
        	logger.info(sb.toString());
        	
        }
	}
	
	private void insertTitle(Entity ent, Attribute att, String description){
		if(resultMap.get("Title") == null){
			resultMap.put("Title", new ResultSet("Title"));
		}
		resultMap.get("Title").setTuple(ent, att);
		resultMap.get("Title").setDescription(ent, description);
		counter++;
	}
	
	private void insertPerson(Entity ent, Attribute att, String description){
		List<String> roleNameList = getCache().getRoleList(ent.getId());
		for(String roleName : getCache().getRoleList(ent.getId())){
			if(resultMap.get(roleName) == null){
				resultMap.put(roleName, new ResultSet(roleName));
			}
			resultMap.get(roleName).setTuple(ent, att);
			resultMap.get(roleName).setDescription(ent, description);
		}
		if(roleNameList.isEmpty()){
			if(resultMap.get(NO_ROLE_PERSON) == null){
				resultMap.put(NO_ROLE_PERSON, new ResultSet(NO_ROLE_PERSON));
			}
			resultMap.get(NO_ROLE_PERSON).setTuple(ent, att);
			resultMap.get(NO_ROLE_PERSON).setDescription(ent, description);
		}
		counter++;
	}
	
    public List<String> getResultSetNames(){
    	return this.resultSetNames;
    }
    
    /**
     * TODO do it more clever!
     * rules:
     * ----Text
     * is_prime_alias_title_of
     * is_alias_title_of
     * is_alias_incipit_of
     * is_alias_explicit_of
     * ----Person
     * is_prime_alias_name_of
     * is_alias_name_of
     * @param alias
     * @return
     */
    private AliasStructure getAliasStructure(Entity alias, Attribute att) throws Exception{
    	AliasStructure structure = new AliasStructure();
    	structure.aliasAtt = att;
    	List<Relation> list = null;
    	list = getWrapper().getSourceRelations(alias, "is_prime_alias_title_of", "TEXT", 1);
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
    	list = getWrapper().getSourceRelations(alias, "is_alias_title_of", "TEXT", 1);
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
    	list = getWrapper().getSourceRelations(alias, "is_alias_incipit_of", "TEXT", 1);
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
    	list = getWrapper().getSourceRelations(alias, "is_alias_explicit_of", "TEXT", 1);
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
    	list = getWrapper().getSourceRelations(alias, "is_prime_alias_name_of", "PERSON", 1);
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
        list = getWrapper().getSourceRelations(alias, "is_alias_name_of", "PERSON", 1); 
    	if(list.size() > 0)
    		structure.setRelation(list.get(0));
    	return structure;
    }
    
    public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
    public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}	
	
	
	public Map<String, ResultSet> getResultMap() {
		return resultMap;
	}


	public void setResultMap(Map<String, ResultSet> resultMap) {
		this.resultMap = resultMap;
	}
	
	private class AliasStructure{
		public Entity target = null;
		public Entity alias = null;
		public Relation rel = null;
		public String description = "";
		public Attribute aliasAtt = null;
		
		public void setRelation(Relation rel){
			this.rel = rel;
			this.target = rel.getTarget();
			this.alias = rel.getSource();
			this.description = rel.getOwnValue() + " <- ALIAS [alias=" +  aliasAtt.getOwnValue() + "]";
		}
	}
	
	static{
		filter1.setEntObjectClass("PERSON");
    	filter1.setName("name");
    	//filter1.setNormalize(true);
    	
    	filter2.setEntObjectClass("TEXT");
    	filter2.setName("title");
    	//filter2.setNormalize(true);
    	
    	filter3.setEntObjectClass("PERSON");
    	filter3.setName("name_translit");
    	
    	//filter3.setNormalize(true);
    	
    	filter4.setEntObjectClass("TEXT");
    	filter4.setName("title_translit");
    	
    	//filter4.setNormalize(true);
    	
    	filter5.setEntObjectClass("TEXT");
    	filter5.setName("full_title");
    	
    	//filter4.setNormalize(true);   
    	
    	filter6.setEntObjectClass("TEXT");
    	filter6.setName("full_title_translit");
    	
    	
    	//filers for alias
    	filter7.setEntObjectClass("ALIAS");
    	filter7.setName("alias");
    	
    	authorsFilters.add(filter1);
    	authorsFilters.add(filter3);
    	authorsFilters.add(filter7);
    	
    	titlesFilters.add(filter2);
    	titlesFilters.add(filter4);
    	titlesFilters.add(filter5);
    	titlesFilters.add(filter6);
    	titlesFilters.add(filter7);
    	
    	/*
    	filter1.setOwnValue(search);
    	filter2.setOwnValue(search);
    	filter3.setOwnValue(search);
    	filter4.setOwnValue(search);
    	filter5.setOwnValue(search);
    	filter6.setOwnValue(search);
    	filter7.setOwnValue(search);
    	*/
    	
    	filters.add(filter1);
    	filters.add(filter2);
    	filters.add(filter3);
    	filters.add(filter4);
    	filters.add(filter5);
    	filters.add(filter6);
    	filters.add(filter7);
	}
}
