package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;
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

/**
 * 2) List of all witnesses for a title 
 * (the list should distinguish which witnesses go with a particular title version)
 * @author jurzua
 *
 */
public class Witness4TitleQuery extends AbstractQuery implements Serializable{

	private static final long serialVersionUID = -1460003809678104919L;
	
	private String titleName;
	private List<SRelLongKey> titleWitnessKeyList;
	
	public Witness4TitleQuery(ApplicationBean appBean){
		super(appBean);
		this.exportUrlRoot = ApplicationBean.urlISMIExportServiceTitles;
	}

	@Override
	public void reset(){
		super.reset();
		this.titleName = null;
		this.rs = null;
		this.titleWitnessKeyList = null;
	}
	
	@Override
	protected void search(){
		
		List<ResultEntry> rs0 = this.execute01(titleName);
		List<ResultEntry> rs1 = null;
		if(StringUtils.isNotEmpty(titleName)){
			rs1 = this.execute02(titleName);
		}
		rs = loadResultSet(rs0, rs1);
		
		List<Long> idLongList = new ArrayList<Long>();
		for(Object e : rs){
			ResultEntry02 entry = (ResultEntry02)e;
			if(!idLongList.contains(entry.titleId))
				idLongList.add(entry.titleId);
		}
		this.idList = ApplicationBean.generateIdList(idLongList);
		
		this.exportDirkUrl = ApplicationBean.generateExportURL(exportUrlRoot, idList, "xml");
	}
	
	private List<ResultEntry02> loadResultSet(List<ResultEntry> rs0, List<ResultEntry> rs1){
		List<ResultEntry02> currentRs = new ArrayList<ResultEntry02>();
		titleWitnessKeyList = new ArrayList<SRelLongKey>();
		
		int count = 0;
		
		for(ResultEntry re : rs0){
			Entity title = getOm().getEntityById(re.getEntMap().get(0));
			Entity witness = getOm().getEntityById(re.getEntMap().get(1));
			
			List<Entity> subjectList = getOm().getTargetsForSourceRelation(title.getId(), "has_subject", "SUBJECT", 1);
			String subject = (subjectList.size() == 0) ? null : subjectList.get(0).getOwnValue();
			
			currentRs.add(new ResultEntry02(title, witness, subject));
			putTitleWitnessKeyList(title.getId(), witness.getId());
			count++;
			if(count >= MAX_RS){
				break;
			}
		}
		
		if(rs1 != null && count < MAX_RS){
			for(ResultEntry re : rs1){
				Entity title = getOm().getEntityById(re.getEntMap().get(0));
				Entity witness = getOm().getEntityById(re.getEntMap().get(1));
				if(!containsTitleWitnessKeyList(title.getId(), witness.getId())){
					
					Entity alias = getOm().getEntityById(re.getEntMap().get(1));
					String alias2Person = re.getRel(1, 0);
					List<Entity> subjectList = getOm().getTargetsForSourceRelation(title.getId(), "has_subject", "SUBJECT", 1);
					String subject = (subjectList.size() == 0) ? null : subjectList.get(0).getOwnValue();
					
					currentRs.add(new ResultEntry02(title, witness, subject, alias.getOwnValue(), alias2Person));
					putTitleWitnessKeyList(title.getId(), witness.getId());
					count++;
					if(count >= MAX_RS){
						break;
					}
				}
			}	
		}
		
		Collections.sort(currentRs);
		
		return currentRs;
	}
	
	private void putTitleWitnessKeyList(Long titleId, Long witness){
		titleWitnessKeyList.add(new SRelLongKey(titleId, witness));
	}
	
	private boolean containsTitleWitnessKeyList(Long titleId, Long witnessId){
		return titleWitnessKeyList.contains(new SRelLongKey(titleId, witnessId));
	}
	
	private List<ResultEntry> execute01(String titleName){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		
		SEntity text = new SEntity(0, "TEXT");
		if(StringUtils.isNotEmpty(titleName)){
			text.addAtt(new SAttributeMultipleName(titleName, "full_title_translit", "full_title"));
		}
		entFilters.add(text);
		
		SEntity witness = new SEntity(1, "WITNESS");
		entFilters.add(witness);
		
		SRelationUniqueName is_exemplar_of = new SRelationUniqueName(witness, text, "is_exemplar_of");
		relFilters.add(is_exemplar_of);
		
		return this.appBean.getSS().search(entFilters, relFilters);		
	}
	
	private List<ResultEntry> execute02(String titleName){
		
		List<SEntity> entFilters = new ArrayList<SEntity>();
		List<SRelation> relFilters = new ArrayList<SRelation>();
		
		
		SEntity text = new SEntity(0, "TEXT");
		entFilters.add(text);
		
		SEntity alias = new SEntity(1, "ALIAS");
		alias.addAtt(new SAttributeUniqueName("alias", titleName));
		entFilters.add(alias);
		
		SEntity witness = new SEntity(2, "WITNESS");
		entFilters.add(witness);
		
		SRelationMultipleName is_alias = new SRelationMultipleName(alias, text, 
				"is_prime_alias_title_of", "is_alias_title_of", 
				"is_alias_explicit_of", "is_alias_incipit_of");
		SRelationUniqueName is_exemplar_of = new SRelationUniqueName(witness, text, "is_exemplar_of");
		relFilters.add(is_exemplar_of);
		relFilters.add(is_alias);
		
		return this.appBean.getSS().search(entFilters, relFilters);		
	}
	
	public class ResultEntry02 implements Comparable<ResultEntry02>, Serializable{
		private static final long serialVersionUID = -2672042198162179468L;
		
		public Long titleId;
		public String titleOv;
		public String titleNOv;
		public String alias;
		public String alias2Title;
		public Long witnessId;
		public String witnessOv;
		public String witnessNOv;
		public String subject;
		
		public ResultEntry02(Entity title, Entity witness, String subject){
			this.set(title, witness, subject);
			
		}
		
		public ResultEntry02(Entity title, Entity witness, String subject, String alias, String alias2Title){
			this.set(title, witness, subject);
			this.alias = alias;
			this.alias2Title = alias2Title;
		}
		
		private void set(Entity title, Entity witness, String subject){
			this.subject = subject;
			
			this.titleId = title.getId();
			this.titleOv = title.getOwnValue();
			this.titleNOv = title.getNormalizedOwnValue();
			
			this.witnessId = witness.getId();
			this.witnessOv = witness.getOwnValue();
			this.witnessNOv = witness.getNormalizedOwnValue();
		}
		
		@Override
		public int compareTo(ResultEntry02 o) {
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
				if(comparisonTitle != 0){
					return comparisonTitle;
				}
			}else{
				if(!this.witnessId.equals(o.witnessId)){
					//comparing witness
					int comparisonWitness = NormalizerUtils.normalizedToCompare(titleNOv).compareTo(
							NormalizerUtils.normalizedToCompare(o.titleNOv));
					return comparisonWitness;
				}
			}
			return 0;
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

		public String getAlias() {
			return alias;
		}

		public String getAlias2Title() {
			return alias2Title;
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

		public String getSubject() {
			return subject;
		}
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
}
