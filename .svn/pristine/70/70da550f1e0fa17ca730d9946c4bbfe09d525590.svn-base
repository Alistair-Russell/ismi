package de.mpiwg.itgroup.ismi.entry.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.security.bo.User;


public class CurrentSubjectBean extends AbstractISMIBean {
	
	private static final long serialVersionUID = -435164522506185752L;

	private static Logger logger = Logger.getLogger(CurrentSubjectBean.class);
	
	public static String type = "type";
	public static String NO_SUBJECT = "NO_SUBJECT";
	
	public static String has_subject = "has_subject";
	public static String SUBJECT = "SUBJECT";
	public static String main_subject = "main_subject";
	public static String sub_subject = "sub_subject";
	
	private Long idMainSubject;

	private List<SelectItem> suggestedCategories = new ArrayList<SelectItem>();
	private List<SelectItem> suggestedTypes = new ArrayList<SelectItem>();

	public CurrentSubjectBean(){
		suggestedTypes.add(new SelectItem(null, "-- choose --"));
		suggestedTypes.add(new SelectItem(main_subject, main_subject));
		suggestedTypes.add(new SelectItem(sub_subject, sub_subject));
		reset();
	}
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, SUBJECT, false);

		setDefObjectClass(SUBJECT);
		this.idMainSubject = null;
		
		this.suggestedCategories = new ArrayList<SelectItem>();
		this.suggestedCategories.add(new SelectItem(null, "-- choose --"));
		
		List<Entity> cats = getWrapper().getEntitiesByAtt(SUBJECT, type, main_subject, -1, true);
		
		for(Entity cat : cats){
			SelectItem item = new SelectItem(cat.getId(), cat.getOwnValue());
			
			item.setStyle("font-weight: bold; padding-left: 0px; font-size: 14;");
			this.suggestedCategories.add(item);
			List<Entity> subCats = getWrapper().getSourcesForTargetRelation(cat, is_part_of, SUBJECT, -1);
			for(Entity subCat : subCats){
				
				SelectItem subItem = new SelectItem(subCat.getId(), subCat.getOwnValue());
				subItem.setStyle("padding-left: 10px; font-size: 12;");
				
				this.suggestedCategories.add(subItem);
				List<Entity> subsubCats = getWrapper().getSourcesForTargetRelation(subCat, is_part_of, SUBJECT, -1);
				for(Entity subsubCat : subsubCats){
					
					SelectItem subsubItem = new SelectItem(subsubCat.getId(), subsubCat.getOwnValue());
					subsubItem.setStyle("padding-left: 20px; font-size: 10;");	
					this.suggestedCategories.add(subsubItem);
				}
			}
		}
	}
	
	public void setEntity(Entity subject) {
		this.reset();
		
		this.entity = subject;
		if(this.entity.isPersistent()){
			this.setCurrentId(this.entity.getId().toString());
			if (subject.isLightweight()) {
				this.entity = getWrapper().getEntityContent(this.entity);
			}
			this.loadAttributes(this.entity);//, getDefinition(this.entity));
			
			for (Relation rel : this.entity.getSourceRelations()) {
				if(rel.getOwnValue().equals(is_part_of)){
					this.idMainSubject = rel.getTargetId();
				}
			}
		}
	}
	
	@Override
	public String save() {
		super.save();
		try {
			User user = getSessionUser();
			
			this.entity = updateEntityAttributes(this.entity);
			
			this.entity.removeAllSourceRelations(is_part_of, SUBJECT);
			if(getIdMainSubject() != null){
				Entity mainCategory = getWrapper().getEntityById(getIdMainSubject());
				//replaceSourceRelation(this.entity, mainCategory, SUBJECT, is_part_of);
				this.entity.replaceSourceRelation(mainCategory, SUBJECT, is_part_of);
			}
			
			this.entity = getWrapper().saveEntity(this.entity, user.getEmail());
			getSessionBean().setEditFormCurrentEntId(this.entity.getId());
			
			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();

			getAppBean().resetSuggestedSubjects();			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		
		saveEnd();
		return PAGE_EDITOR;

	}

	public List<SelectItem> getSuggestedCategories() {
		return suggestedCategories;
	}

	public void setSuggestedCategories(List<SelectItem> suggestedCategories) {
		this.suggestedCategories = suggestedCategories;
	}
	
	public Long getIdMainSubject() {
		return idMainSubject;
	}

	public void setIdMainSubject(Long idMainSubject) {
		this.idMainSubject = idMainSubject;
	}

	public List<SelectItem> getSuggestedTypes() {
		return suggestedTypes;
	}

	public void setSuggestedTypes(List<SelectItem> suggestedTypes) {
		this.suggestedTypes = suggestedTypes;
	}
}
