package de.mpiwg.itgroup.ismi.utils.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.RomanizationLoC;

import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;

public class TitleTemplate extends AbstractTemplate{
	
	private static Logger logger = Logger.getLogger(TitleTemplate.class);
	
	public String ov;
	public String privacity;
	public String fullTitle;
	public String fullTitleTranslit;
	public String language;
	public String notes;
	public String category;
	public String createIn;
	public String incipit;
	public String explicit;
	public String dedication;
	public String tableOfContents;
	public String author;
	public Long authorId;
	//extra
	public List<String> aliasList;
	public List<String> authorMisattributionList;
        public Map<String,String> referenceEndnoteIdList;
	public String personDedicatedTo;
	public String commentaryOnText;
	public String translationOfText;
	public String versionOfText;
	
	
	public String creationDate;
	
	public TitleTemplate(Entity entity, WrapperService om, boolean useRomanization){
		
		//logger.info("Diplaying " + entity);
		
		this.aliasList = new ArrayList<String>();
		this.authorMisattributionList = new ArrayList<String>();
		
		if(entity != null){
			
			this.privacity = entity.getPrivacity();
			
			Attribute att = null;
			Long entId = entity.getId();
			
			//attributes
			this.fullTitle = om.getAttributeOVByName(entId, "full_title", useRomanization);
			
			this.fullTitleTranslit = om.getAttributeOVByName(entId, "full_title_translit", useRomanization);
			
			att = om.getAttributeByName(entId, "creation_date");
			if(att != null){
				Calendar creationDate = new Calendar(att.getOwnValue());
				this.creationDate = creationDate.getCalendarAsHtml();
			}
			
			this.language = om.getAttributeOVByName(entId, "language", useRomanization);
			
			this.notes = om.getAttributeOVByName(entId, "notes", useRomanization);
			
			this.incipit = om.getAttributeOVByName(entId, "incipit", useRomanization);
			
			this.explicit = om.getAttributeOVByName(entId, "explicit", useRomanization);
			
			this.dedication = om.getAttributeOVByName(entId, "dedication", useRomanization);
			
			this.tableOfContents = om.getAttributeOVByName(entId, "table_of_contents", useRomanization);
			
			List<Entity> tarList =  om.getTargetsForSourceRelation(entId, "has_subject", "SUBJECT", 1);
			this.category = (tarList.size() > 0) ? tarList.get(0).getOwnValue() : null ;
			this.category = (useRomanization) ? RomanizationLoC.convert(category) : category;
			
			tarList =  om.getTargetsForSourceRelation(entity.getId(), "was_created_in", "PLACE", 1);
			this.createIn = (tarList.size() > 0) ? tarList.get(0).getOwnValue() : null ;
			this.createIn = (useRomanization) ? RomanizationLoC.convert(createIn) : createIn;
			
			List<Entity> list0 = om.getSourcesForTargetRelation(entity.getId(), "is_reference_of", "REFERENCE", -1);
			for(Entity ref : list0){
			    this.refEntityList.add(om.getEntityContent(ref));
			}
			
			list0 = om.getTargetsForSourceRelation(entity.getId(), "was_created_by", "PERSON", -1);
			if(list0.size() > 0){
				this.author = list0.get(0).getOwnValue();
				this.author = (useRomanization) ? RomanizationLoC.convert(author) : author;
				this.authorId = list0.get(0).getId();
			}
			
			//extra fields ...
			
			list0 = om.getSourcesForTargetRelation(entity, "is_alias_title_of", "ALIAS", -1);
			for(Entity alias : list0){
				String alias0 = (useRomanization) ? RomanizationLoC.convert(alias.getOwnValue()) : alias.getOwnValue();
				this.aliasList.add(alias0);
			}
			
			list0 = om.getTargetsForSourceRelation(entity, "has_author_misattribution", "MISATTRIBUTION", -1);
			for(Entity misatt : list0){
				List<Entity> authorMissattList = om.getTargetsForSourceRelation(misatt, "misattributed_to", "PERSON", 1);
				if(authorMissattList.size() == 1){
					String authorMiss = authorMissattList.get(0).getOwnValue();
					authorMiss = (useRomanization) ? RomanizationLoC.convert(authorMiss) : authorMiss;
					this.authorMisattributionList.add(authorMiss);
				}
			}
			
			list0 = om.getTargetsForSourceRelation(entity, "was_dedicated_to", "PERSON", 1);
			this.personDedicatedTo = (list0.size() == 1) ? list0.get(0).getOwnValue() : null;
			this.personDedicatedTo = (useRomanization) ? RomanizationLoC.convert(personDedicatedTo) : personDedicatedTo;
			
			list0 = om.getTargetsForSourceRelation(entity, "is_commentary_on", "TEXT", 1);
			this.commentaryOnText = (list0.size() == 1) ? list0.get(0).getOwnValue() : null;
			this.commentaryOnText = (useRomanization) ? RomanizationLoC.convert(commentaryOnText) : commentaryOnText;
			
			list0 = om.getTargetsForSourceRelation(entity, "is_translation_of", "TEXT", 1);
			this.translationOfText = (list0.size() == 1) ? list0.get(0).getOwnValue() : null;
			this.translationOfText = (useRomanization) ? RomanizationLoC.convert(translationOfText) : translationOfText;
			
			list0 = om.getTargetsForSourceRelation(entity, "is_version_of", "TEXT", 1);
			this.versionOfText = (list0.size() == 1) ? list0.get(0).getOwnValue() : null;
			this.versionOfText = (useRomanization) ? RomanizationLoC.convert(versionOfText) : versionOfText;
		}
		
		this.loadRefernces();

		this.referenceEndnoteIdList = new HashMap<String,String>();

			
		

		for  (ReferenceTemplate refTempl : this.getReferenceList()){
			 
			    this.referenceEndnoteIdList.put(refTempl.getEndnoteId(),refTempl.getAdditionalInf());
	
		}


		
		
	}
	
	public String getOv() {
		return ov;
	}
	public String getPrivacity() {
		return privacity;
	}
	public String getFullTitle() {
		return fullTitle;
	}
	public String getFullTitleTranslit() {
		return fullTitleTranslit;
	}
	public String getLanguage() {
		return language;
	}
	public String getNotes() {
		return notes;
	}
	public String getCategory() {
		return category;
	}
	public String getCreateIn() {
		return createIn;
	}
	public String getIncipit() {
		return incipit;
	}
	public String getExplicit() {
		return explicit;
	}
	public String getDedication() {
		return dedication;
	}
	public String getTableOfContents() {
		return tableOfContents;
	}
	public List<String> getAliasList() {
		return aliasList;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public String getAuthor() {
		return author;
	}
	public Long getAuthorId() {
		return authorId;
	}
	
	public boolean getHasAuthor(){
		return this.authorId != null;	
	}

	public List<String> getAuthorMisattributionList() {
		return authorMisattributionList;
	}
}
