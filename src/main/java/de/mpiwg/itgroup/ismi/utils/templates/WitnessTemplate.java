package de.mpiwg.itgroup.ismi.utils.templates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.RomanizationLoC;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.utils.NaturalOrderComparator;

public class WitnessTemplate extends AbstractTemplate implements
		Comparable<WitnessTemplate> {
	
	private static final long serialVersionUID = -8829977715724255324L;

	private static Logger logger = Logger.getLogger(WitnessTemplate.class);

	public String privacity;
	public Long id;
	public String ov;
	public String title;
	public Long titleId;
	public String startPage;
	public String endPage;
	public String status;
	public String tableOfContents;
	public String notesOnTitleAuthor;
	public String notesOnCollationAndCorrections;
	public String notesOnOwnership;
	public String notes;
	public String codex;
	public String collection;
	public String repository;
	public String city;
	public String country;
	public String folios;
	public String ahlwardtNo;
	public String colophon;

	public String incipit;
	public String explicit;
	public String subject;

	public List<String> referenceList;
	public Map<String, String> ownedByMap;
	public Map<String, String> readByMap;
	
	//extras fields used by the json interface
	public String titleAsWrittenInWitness;
	public String authorAsWrittenInWitness;
	public String copyist;
	public String placeOfCopying;
    public Map<String,String> referenceEndnoteIdList;
	public String page_dimensions;
	public String written_area_dimensions;
	public String lines_per_page;
	public String page_layout;
	public String script;
	public String writing_surface;
	public List<String> wasStudiedByList;
	public String fullTitle;
	public String fullTitleTranslit;
	public String authorName;
	public String authorNameTranslit;
	public String creationDate;
	
	public boolean useRomanization = false;
	
	public boolean isUnknown() {
		return StringUtils.equals("UNKNOWN", this.title);
	}

	public WitnessTemplate(Entity witness, WrapperService om, boolean useRomanization) {
		this.init(witness, om, false, useRomanization);
	}
	
	public WitnessTemplate(Entity witness, WrapperService om, boolean extended, boolean useRomanization) {
		this.init(witness, om, extended, useRomanization);
	}

	private void init(Entity witness, WrapperService om, boolean extended, boolean useRomanization) {
		// this.referenceList = new ArrayList<String>();
		this.ownedByMap = new HashMap<String, String>();
		this.readByMap = new HashMap<String, String>();
		this.wasStudiedByList = new ArrayList<String>();
		
		this.useRomanization = useRomanization;

		try {
			if (witness != null) {
				this.privacity = witness.getPrivacity();

				this.id = witness.getId();
				this.ov = (useRomanization) ? RomanizationLoC.convert(witness.getOwnValue()) : witness.getOwnValue();

				if (StringUtils.isEmpty(witness.getStatus())) {
					this.status = ApplicationBean.STATUS_NOT_CHECKED;
				} else {
					this.status = witness.getStatus();
				}

				
				this.startPage = om.getAttributeOVByName(id, "start_page", false);
				this.endPage = om.getAttributeOVByName(id, "end_page", false);
				

				this.folios = om.getAttributeOVByName(id, "folios", useRomanization);
				this.ahlwardtNo = om.getAttributeOVByName(id, "ahlwardt_no", useRomanization);
				this.colophon = om.getAttributeOVByName(id, "colophon", useRomanization);
				this.page_dimensions = om.getAttributeOVByName(id, "page_dimensions", useRomanization);
				this.written_area_dimensions = om.getAttributeOVByName(id, "written_area_dimensions", useRomanization);
				this.lines_per_page = om.getAttributeOVByName(id, "lines_per_page", useRomanization);
				this.page_layout = om.getAttributeOVByName(id, "page_layout", useRomanization);
				this.script = om.getAttributeOVByName(id, "script", useRomanization);
				this.writing_surface = om.getAttributeOVByName(id, "writing_surface", useRomanization);
				
				Attribute att0 = om.getAttributeByName(witness.getId(), "creation_date");
				Calendar creationDate0 = AbstractISMIBean.updateCalendar(att0);
				this.creationDate = creationDate0.getCalendarAsHtml();
				
				if(extended){
					this.incipit = om.getAttributeOVByName(id, "incipit", useRomanization);
					this.explicit = om.getAttributeOVByName(id, "explicit", useRomanization);
				}
				
				this.tableOfContents = om.getAttributeOVByName(id, "table_of_contents", useRomanization);				
				this.notesOnTitleAuthor = om.getAttributeOVByName(id, "notes_on_title_author", useRomanization);
				this.notesOnCollationAndCorrections = om.getAttributeOVByName(id, "notes_on_collation_and_corrections", useRomanization);
				this.notesOnOwnership = om.getAttributeOVByName(id, "notes_on_ownership", useRomanization);
				this.notes = om.getAttributeOVByName(id, "notes", useRomanization);

				List<Entity> list0 = om.getTargetsForSourceRelation(witness.getId(), "is_exemplar_of", "TEXT", 1);
				if (list0.size() > 0) {
					this.title = (useRomanization) ? RomanizationLoC.convert(list0.get(0).getOwnValue()) : list0.get(0).getOwnValue();
					this.titleId = list0.get(0).getId();
					att0 = om.getAttributeByName(titleId, "full_title");
					this.fullTitle = (att0 != null) ? att0.getOwnValue() : null;
					this.fullTitle = (useRomanization) ? RomanizationLoC.convert(fullTitle) : fullTitle;
					
					att0 = om.getAttributeByName(titleId, "full_title_translit");
					if(att0 != null){
						this.fullTitleTranslit = (useRomanization) ? RomanizationLoC.convert(att0.getOwnValue()) : att0.getOwnValue();
					}
					
					List<Entity> list1 = om.getTargetsForSourceRelation(this.titleId, "was_created_by", "PERSON", 1);
					if(list1.size() > 0){
						Entity author = list1.get(0);
						att0 = om.getAttributeByName(author.getId(), "name");
						this.authorName = (att0 == null) ? null : att0.getValue();
						this.authorName = (useRomanization) ? RomanizationLoC.convert(authorName) : authorName;
						att0 = om.getAttributeByName(author.getId(), "name_translit");
						if(att0 != null){
							this.authorNameTranslit = (useRomanization) ? RomanizationLoC.convert(att0.getValue()) : att0.getValue();	
						}
					}
					
					if(extended){
						//Attribute subject = om.getAttributeByName(witness.getId(), "subject");
						list0 = om.getTargetsForSourceRelation(this.titleId, "has_subject", "SUBJECT", 1);
						this.subject = (!list0.isEmpty()) ? list0.get(0).getOwnValue() : null;
						this.subject = (useRomanization) ? RomanizationLoC.convert(subject) : subject;
						
					}
				}
				
				list0 = om.getTargetsForSourceRelation(witness, "has_author_written_as", "ALIAS", 1);
				if(list0.size() > 0){
					this.authorAsWrittenInWitness = (useRomanization) ? RomanizationLoC.convert(list0.get(0).getOwnValue()) : list0.get(0).getOwnValue();
				}

				list0 = om.getTargetsForSourceRelation(witness, "has_title_written_as", "ALIAS", 1);
				if(list0.size() > 0){
					this.titleAsWrittenInWitness = (useRomanization) ? RomanizationLoC.convert(list0.get(0).getOwnValue()) : list0.get(0).getOwnValue();
				}
				
				list0 = om.getTargetsForSourceRelation(witness, "was_copied_by", "PERSON", 1);
				if(list0.size() > 0){
					this.copyist = (useRomanization) ? RomanizationLoC.convert(list0.get(0).getOwnValue()) : list0.get(0).getOwnValue();
				}
				
				list0 = om.getTargetsForSourceRelation(witness, "was_copied_in", "PLACE", 1);
				this.placeOfCopying = (list0.size() > 0) ? list0.get(0).getOwnValue() : null;
				
				
				list0 = om.getTargetsForSourceRelation(witness, "was_studied_by", "PERSON", 1);
				for(Entity ent : list0){
					this.wasStudiedByList.add((useRomanization) ? RomanizationLoC.convert(ent.getOwnValue()) : ent.getOwnValue());
				}
				
				//**** Getting the Codices
				List<Entity> list = 
						om.getTargetsForSourceRelation(witness.getId(), "is_part_of", "CODEX", 1);
				if (list.size() > 0) {
					Entity codex = list.get(0);

					Attribute att = om.getAttributeByName(codex.getId(),
							"identifier");
					if (att != null && StringUtils.isNotEmpty(att.getValue())) {
						this.codex = att.getValue();
					}

					for (Relation rel : om.getSourceRelations(codex, "owned_by", "PERSON", -1)) {
						String date = (rel.getAttributeByName("date") != null) ? new Calendar(
								rel.getAttributeByName("date").getOwnValue())
								.getCalendarAsHtml() : "";
						String ov = om.getEntityById(rel.getTargetId()).getOwnValue(); 
						String ownedLabel = (useRomanization) ? RomanizationLoC.convert(ov) : ov; 
						this.ownedByMap.put(ownedLabel, date);
					}

					for (Relation rel : om.getSourceRelations(codex, "read_by",
							"PERSON", -1)) {
						String date = (rel.getAttributeByName("date") != null) ? new Calendar(
								rel.getAttributeByName("date").getOwnValue())
								.getCalendarAsHtml() : "";
						String ov = om.getEntityById(rel.getTargetId()).getOwnValue();
						String readerLabel = (useRomanization) ? RomanizationLoC.convert(ov) : ov;  
						this.readByMap.put(readerLabel, date);
					}

					list = om.getTargetsForSourceRelation(codex.getId(),
							"is_part_of", "COLLECTION", 1);
					if (list.size() > 0) {
						Entity collection = list.get(0);
						this.collection = collection.getOwnValue();

						list = om.getTargetsForSourceRelation(
								collection.getId(), "is_part_of", "REPOSITORY",
								1);

						if (list.size() > 0) {
							Entity repository = list.get(0);
							this.repository = repository.getOwnValue();

							list = om.getTargetsForSourceRelation(
									repository.getId(), "is_in", "PLACE", 1);

							if (list.size() > 0) {
								Entity city = list.get(0);
								this.city = city.getOwnValue();

								list = om.getTargetsForSourceRelation(
										city.getId(), "is_part_of", "PLACE", 1);
								if (list.size() > 0) {
									Entity country = list.get(0);
									this.country = country.getOwnValue();
								}
							}
						}
					}
				}

				list0 = om.getSourcesForTargetRelation(witness,
						"is_reference_of", "REFERENCE", -1);
				for (Entity ref : list0) {
				    //System.out.println("### REF= " + ref.getId());
					this.refEntityList.add(om.getEntityContent(ref));
				}
			}

			this.loadRefernces();
			this.referenceEndnoteIdList = new HashMap<String,String>();

			for  (ReferenceTemplate refTempl : this.getReferenceList()){
			    this.referenceEndnoteIdList.put(refTempl.getEndnoteId(),refTempl.getAdditionalInf());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getPrivacity() {
		return privacity;
	}

	public String getStatus() {
		return status;
	}

	public String getTableOfContents() {
		return tableOfContents;
	}

	public String getNotesOnTitleAuthor() {
		return notesOnTitleAuthor;
	}

	public String getNotesOnCollationAndCorrections() {
		return notesOnCollationAndCorrections;
	}

	public String getNotesOnOwnership() {
		return notesOnOwnership;
	}

	public String getNotes() {
		return notes;
	}

	public String getCodex() {
		return codex;
	}

	public String getCollection() {
		return collection;
	}

	public String getRepository() {
		return repository;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Map<String, String> getOwnedByMap() {
		return ownedByMap;
	}

	public Map<String, String> getReadByMap() {
		return readByMap;
	}

	public Collection<String> getOwnedByList() {
		return ownedByMap.keySet();
	}

	public Collection<String> getReadByList() {
		return readByMap.keySet();
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getStartPage() {
		return startPage;
	}

	public String getEndPage() {
		return endPage;
	}

	public Long getId() {
		return id;
	}

	public String getOv() {
		return ov;
	}

	public String getTitle() {
		return this.title;
	}
	
	public Long getTitleId() {
		return this.titleId;
	}

	public String getFolios() {
		return this.folios;
	}
	
	public boolean hasTitle(){
		return this.titleId != null;
	}

	// 40b-49b
	// 104b-111b
	public int compareTo(WitnessTemplate other) {
		// return this.empId - o.empId ;

		if (other == null)
			return -1;
		if (StringUtils.isEmpty(this.folios)
				&& StringUtils.isEmpty(other.folios))
			return this.title.compareTo(other.title);
		if (StringUtils.isEmpty(this.folios))
			return 1;
		if (StringUtils.isEmpty(other.folios))
			return -1;

		try {
			return NaturalOrderComparator.compare(this.folios, other.folios);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return 0;
	}

}
