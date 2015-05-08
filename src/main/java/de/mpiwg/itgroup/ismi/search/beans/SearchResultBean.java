package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.bo.utils.EntitySortByNormalizedOwnValue;
import org.mpi.openmind.repository.services.utils.AttributeFilter;
import org.mpi.openmind.repository.utils.NormalizerUtils;

import de.mpiwg.itgroup.ismi.auxObjects.ResultSet;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;


public class SearchResultBean extends AbstractISMIBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7072264955252613769L;
	public static Long SEC_05 = new Long(5000);
	public static Long SEC_10 = new Long(10000);
	public static Long SEC_20 = new Long(20000);
	public static Long SEC_30 = new Long(30000);

	private List<ResultSet> resultSetList = new ArrayList<ResultSet>();

	private int maxResult = -1	;
	private int counter = 0;

    public void searchAttributes(String term, String mode){
    	long start = System.currentTimeMillis();
    	this.resultSetList = new ArrayList<ResultSet>();
    	this.counter = 0;
    	try{
            if (StringUtils.isNotEmpty(term)) {
            	
            	boolean includeTitles = (SimpleSearchBean.TITLES.equals(mode) || SimpleSearchBean.AUTHORS_TITLES.equals(mode)) ? true : false;
            	boolean includeAuthors = (SimpleSearchBean.AUTHORS.equals(mode) || SimpleSearchBean.AUTHORS_TITLES.equals(mode) ? true : false);
            	
            	List<AttributeFilter> filters = new ArrayList<AttributeFilter>();
            	
            	AttributeFilter filter1 = new AttributeFilter();
            	filter1.setEntObjectClass("PERSON");
            	filter1.setName("name");
            	//filter1.setNormalize(true);
            	
            	AttributeFilter filter2 = new AttributeFilter();
            	filter2.setEntObjectClass("TEXT");
            	filter2.setName("title");
            	//filter2.setNormalize(true);
            	
            	AttributeFilter filter3 = new AttributeFilter();
            	filter3.setEntObjectClass("PERSON");
            	filter3.setName("name_translit");
            	
            	//filter3.setNormalize(true);
            	
            	AttributeFilter filter4 = new AttributeFilter();
            	filter4.setEntObjectClass("TEXT");
            	filter4.setName("title_translit");
            	
            	//filter4.setNormalize(true);
            	
            	AttributeFilter filter5 = new AttributeFilter();
            	filter5.setEntObjectClass("TEXT");
            	filter5.setName("full_title");
            	
            	//filter4.setNormalize(true);   
            	
            	
            	AttributeFilter filter6 = new AttributeFilter();
            	filter6.setEntObjectClass("TEXT");
            	filter6.setName("full_title_translit");
            	
            	
            	//filers for alias
            	AttributeFilter filter7 = new AttributeFilter();
            	filter7.setEntObjectClass("ALIAS");
            	filter7.setName("alias");
            	
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
            	
            	long startQuery = System.currentTimeMillis();
            	Map<Attribute, Entity> map;
            	
            	if(getCache().isMapDirty()){
            		map = getWrapper().searchAttEntityByAttributeFilter(filters, getMaxResult());
            		System.out.println();
            		System.out.println("###########################################################");
            		System.out.println();
            		System.out.println("MAP SEARCH SIZEx= " + map.size() + " max result " + getMaxResult());
            		/*
            		for(Attribute a : map.keySet()){
            			System.out.println(a);
            		}*/
            		
            		getCache().setAttResultMap(map);
            	}else{
            		map = getCache().getAttResultMap();
            	}
            	
            	long endQuery = System.currentTimeMillis();
            	String normalizedString = NormalizerUtils.normalize(term);
            	
            	System.out.println("");
            	System.out.println("***********************");
            	System.out.println("Word: " + term);
            	System.out.println("Normalized: " + normalizedString);
            	System.out.println("Query Execution= " + (endQuery - startQuery));
            	
            	ResultSet nameResult = new ResultSet("Name");
            	ResultSet titleResult = new ResultSet("Title");

            	List<Long> idList = new ArrayList<Long>();
            	
            	int countCached = 0;
            	int countNoCached = 0;
            	int noCachedAlias = 0;
            	int noCachedTextPerson = 0;
            	int putInCache = 0;
            	int putInCacheError = 0;
            	for(Attribute att : map.keySet()){
            		
            		//limiting the execution time.
            		if((System.currentTimeMillis() - start) > SEC_20 || counter >= 1000)
            			break;
            		
            		//Attribute att = map.get(ent);
            		Entity ent = map.get(att);
            		
            		if(StringUtils.isNotEmpty(att.getNormalizedOwnValue()) && !getCache().ignoreAttribute(att) && att.getNormalizedOwnValue().contains(normalizedString)){	
            			if(getCache().containsAttribute(att)){
            				countCached++;
            				//if the attributed was saved in the cache
            				Entity ee = getCache().getEntMap().get(att.getId());
            				String description = getCache().getDescriptionMap().get(att.getId() + "-" + ee.getId());
            				if(includeAuthors && ee.getObjectClass().equals("PERSON") && !idList.contains(ee.getId())){
                    			nameResult.setTuple(ee, att);
                    			nameResult.setDescription(ee, description);
                    			//nameResult.setDescription(ee, att.getObjectClass() + "=" + att.getOwnValue());
                    			counter++;
                    			idList.add(ee.getId());
            				}else if(includeTitles &&  ee.getObjectClass().equals("TEXT")	 && !idList.contains(ee.getId())){
                    			titleResult.setTuple(ee, att);
                    			titleResult.setDescription(ee, description);
                    			//titleResult.setDescription(ee, att.getObjectClass() + "=" + att.getOwnValue());
                    			counter++;
                    			idList.add(ee.getId());
            				}
            			}else{
            				
            				countNoCached++;
                    		if(includeAuthors && !idList.contains(ent.getId()) && att.getName().contains("name") && att.getSourceObjectClass().equals("PERSON")){
                    			//OC: PERSON
                    			String d = att.getObjectClass() + "=" + att.getOwnValue();
                    			nameResult.setDescription(ent, d);
                    			nameResult.setTuple(ent, att);
                    			getCache().setTuple(ent, att, d);
                    			counter++;
                    			noCachedTextPerson++;
                    			idList.add(ent.getId());
                    		}else if(includeTitles && !idList.contains(ent.getId()) && (att.getName().contains("title") || att.getName().contains("full_title"))){
                    			//OC: TEXT
                    			String d = att.getObjectClass() + "=" + att.getOwnValue();
                    			titleResult.setDescription(ent, d);
                    			titleResult.setTuple(ent, att);
                    			getCache().setTuple(ent, att, d);
                    			counter++;
                    			noCachedTextPerson++;
                    			idList.add(ent.getId());
                    		}else if(att.getSourceObjectClass().equals("ALIAS")){
                    			noCachedAlias++;
                    			//OC: ALIAS
                    			AliasStructure aliasStructure = getAliasStructure(ent, att);
                    			if(aliasStructure.target != null){
                    				getCache().setTuple(aliasStructure.target, att, aliasStructure.description);
                    				putInCache++;
                    				if(!idList.contains(aliasStructure.target.getId())){
                        				if(includeAuthors && aliasStructure.target.getObjectClass().equals("PERSON")){
                        					nameResult.setTuple(aliasStructure.target, att);
                        					nameResult.setDescription(aliasStructure.target, aliasStructure.description);
                        					counter++;
                        				}else if(includeTitles && aliasStructure.target.getObjectClass().equals("TEXT")){
                        					titleResult.setTuple(aliasStructure.target, att);
                        					titleResult.setDescription(aliasStructure.target, aliasStructure.description);
                        					counter++;
                        				}
                        				idList.add(aliasStructure.target.getId());
                        			}
                    			}else{
                    				putInCacheError++;
                    				this.getCache().getIgnoredAttIdList().add(att.getId());
                    				System.out.println("cacheError++ : " + att);
                    			}   			
                    		} 
                    		   				
            			}
            		}
            	}
            	
            	System.out.println();
            	System.out.println("countCached: " + countCached + " - countNoCached: " + countNoCached + " - total: " + (countCached + countNoCached));
            	System.out.println("noCachedTextPerson: " + noCachedTextPerson++ + " - noCachedAlias: " + noCachedAlias);
            	System.out.println("putInCacheError: " + putInCacheError + " - putInCache: " + putInCache);
            	System.out.println("nameResult: " + nameResult.getResults().size());
            	System.out.println("titleResult: " + titleResult.getResults().size());
            	
            	if(nameResult.getResults().size() > 0){
            		Collections.sort(nameResult.getResults(), new EntitySortByNormalizedOwnValue());
            		this.resultSetList.add(nameResult);
            	}
            	if(titleResult.getResults().size() > 0){
            		Collections.sort(titleResult.getResults(), new EntitySortByNormalizedOwnValue());
            		this.resultSetList.add(titleResult);
            	}
            }    
            
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	long end = System.currentTimeMillis();
    	System.out.println("Simple search time execution= " + (end - start));
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

	public List<ResultSet> getResultSetList() {
		return resultSetList;
	}

	public void setResultSetList(List<ResultSet> resultSetList) {
		this.resultSetList = resultSetList;
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
	private class AliasStructure implements Serializable{
		private static final long serialVersionUID = -833933447985472058L;
		
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
}
