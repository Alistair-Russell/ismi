package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.RomanizationLoC;
import org.mpi.openmind.repository.utils.TransliterationUtil;



/* rich
import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputHidden;
*/
import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.util.guiComponents.EntityList;

public class CurrentPersonBean extends AbstractISMIBean  implements Serializable{
	private static final long serialVersionUID = -3366510497089165294L;

	private static Logger logger = Logger.getLogger(CurrentPersonBean.class);
	
	//private Entity person;
	private String newAlias;
	
	private String romanizedNameTranslit;
	
	public static String birth_date = "birth_date";
	public static String death_date = "death_date";
	
	private ListenerObject birthPlaceLo = new ListenerObject(PLACE, name);
	private ListenerObject deathPlaceLo = new ListenerObject(PLACE, name);

	private String valueShortName;
	private Entity shortNameAlias; // is now an alias of type "prime"
	
	private transient Calendar calBirthDate;
	private transient Calendar calDeathDate;

	private Map<String, Boolean> deletedAliases = new HashMap<String, Boolean>();
	
	private EntityList livedInPlaces;
	private EntityList aliasList;
	private EntityList roleList;
	private EntityList studentOfList;
	private EntityList floruitList;
	
	@Override
	public void reset(){
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, PERSON, false);
		
		this.newAlias = new String();

		this.livedInPlaces = new EntityList(PLACE, name, true);		
		this.aliasList = new EntityList(ALIAS, "alias", getWrapper(), getUserName());		
		this.roleList = new EntityList(ROLE, "name", getWrapper(), getUserName());
		this.studentOfList = new EntityList(PERSON, "name_translit", "Student of");
		this.floruitList = new EntityList(FLORUIT_DATE, "date", getWrapper(), getUserName(), true);
		
		this.valueShortName = "";
		this.shortNameAlias = null;
		
		this.birthPlaceLo.reset();

		this.deathPlaceLo.reset();

		this.deletedAliases = new HashMap<String, Boolean>();
		
		this.calBirthDate = new Calendar();
		this.calDeathDate = new Calendar();
		
	}

	public EntityList getRoleList() {
		return roleList;
	}

	public void setRoleList(EntityList roleList) {
		this.roleList = roleList;
	}

	public EntityList getAliasList() {
		return aliasList;
	}

	public void setAliasList(EntityList aliasList) {
		this.aliasList = aliasList;
	}

	public CurrentPersonBean() {
		this.reset();
		setDefObjectClass(PERSON);
		registerChecker(birthPlaceLo, "Birth place not valid!");
		registerChecker(deathPlaceLo, "Death place not valid!");
	}
	
	/**
	 * 
	 * @param event
	 */
	public void shortNameChangeListener(ValueChangeEvent event) {
		try {
			if (event.getNewValue() == null) {
				return;
			}
			if (event.getNewValue().equals(event.getOldValue())) {
				return;
			}

			String newShortName = (String) event.getNewValue();

			if (shortNameAlias == null) {
				shortNameAlias = new Entity(org.mpi.openmind.repository.bo.Node.TYPE_ABOX, ALIAS, false);
				
			}

			Attribute attr = this.shortNameAlias.getAttributeByName(alias);
			if (attr == null) {
				this.shortNameAlias.addAttribute(new Attribute(alias, TEXT,
						newShortName));
			} else
				attr.setValue(newShortName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String translitNameAction() {
		String pn = getAttributes().get(name);

		String translit = TransliterationUtil.getTransliteration(pn);
		this.getAttributes().put(name_translit, translit);
		
		return PAGE_EDITOR;
	}

	public Calendar getCalBirthDate() {
		return calBirthDate;
	}

	public void setCalBirthDate(Calendar calBirthDate) {
		this.calBirthDate = calBirthDate;
	}

	public Calendar getCalDeathDate() {
		return calDeathDate;
	}

	public void setCalDeathDate(Calendar calDeathDate) {
		this.calDeathDate = calDeathDate;
	}

	@Override
	public void setEntity(Entity person) {
		this.reset();
		
		this.entity = person;
		
		if(this.entity != null && this.entity.isPersistent()){
			if (this.entity.isLightweight()) {
				this.entity = this.getWrapper().getEntityContent(person);
			}

			this.calDeathDate = updateCalendar(this.entity.getAttributeByName(death_date));
			this.calBirthDate = updateCalendar(this.entity.getAttributeByName(birth_date));
			
			this.setCurrentId(this.entity.getId().toString());
			this.loadAttributes(this.entity);
			
			for (Relation rel : this.entity.getSourceRelations()) {
				Entity target = getWrapper().getEntityById(rel.getTargetId());
				if (rel.getOwnValue().equals("was_born_in")) {
					this.birthPlaceLo.setEntityAndAttribute0(target);
					//this.personWasBornInName = (target == null) ? "" : target.getOwnValue();
				} else if (rel.getOwnValue().equals(lived_in)) {
					this.livedInPlaces.add(target, rel.getAttributeByName("date"));
				} else if (rel.getOwnValue().equals(was_student_of)) {
					this.studentOfList.add(target);
				} else if (rel.getOwnValue().equals(has_role)) {
					this.roleList.add(target);
				} else if (rel.getOwnValue().equals("died_in")) {
					this.deathPlaceLo.setEntityAndAttribute0(target);
				} else if (rel.getOwnValue().equals("has_floruit_date")) {
					Attribute calAtt = getWrapper().getAttributeByName(target.getId(), "date");
					this.floruitList.add(target, calAtt);
				}
			}
			
			for (Relation rel : this.entity.getTargetRelations()) {
				if (rel.getOwnValue().equals("was_created_by") && rel.getSourceObjectClass().equals(TEXT)) {
					//Entity title = getWrapper().getEntityById(rel.getSourceId());
				} else if (rel.getOwnValue().equals("is_alias_name_of")) {
					Entity alias = getWrapper().getEntityById(rel.getSourceId());
					this.aliasList.add(alias);
				} else if (rel.getOwnValue().equals("is_prime_alias_name_of")) {
					Entity alias = getWrapper().getEntityByIdWithContent(rel.getSourceId());
					this.shortNameAlias = alias;
					this.valueShortName = alias.getAttributeByName("alias").getValue();
				}
			}
			
			//this.loadReferences(this.entity);
			this.loadEndNoteRefs();
			this.displayUrl = generateDisplayUrl(person, null, null, getAppBean().getRoot());
				
		}
	}

	/**
	 * Saves the current Person. TODO save the target's relations.
	 * 
	 * @return
	 */
	@Override
	public String save() {
		super.save();
		try {
			
			long start = System.currentTimeMillis();
			
			CheckResults cr = getCheckResults();
			if (cr.hasErrors){
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				return "SAVE_ERROR";
			}
			
			getAttributes().put(birth_date, this.calBirthDate.toJSONString());
			getAttributes().put(death_date, this.calDeathDate.toJSONString());
			
			
			this.entity = updateEntityAttributes(this.entity);
			this.entity.replaceSourceRelation(birthPlaceLo.entity, PLACE, "was_born_in");

			this.entity.replaceSourceRelation(deathPlaceLo.entity, PLACE, "died_in");

			this.entity.removeAllTargetRelationsByName(is_prime_alias_name_of);
			if (shortNameAlias != null) {
				shortNameAlias.setObjectClass(ALIAS);
				shortNameAlias = getWrapper().saveEntity(shortNameAlias, getSessionUser().getEmail());
				Relation aliasRel = new Relation(shortNameAlias, this.entity, is_alias_name_of);
			}
			
			//ALIAS -> is_alias_name_of -> PERSON
			this.entity.removeAllTargetRelationsByName(is_alias_name_of);
			for(Entity alias : this.aliasList.getEntities()){
				Entity alias0 = getWrapper().getEntityByIdWithContent(alias.getId());
				Relation aliasRel = new Relation(alias0, this.entity, is_alias_name_of);
			}
			
			// PERSON -> lived_in manyToMany -> PLACE
			this.entity.removeAllSourceRelationsByName(lived_in);
			for(Entity place : this.livedInPlaces.getEntities()){
				//loadRelation(this.person, place, PLACE, lived_in);
				Entity place0 = getWrapper().getEntityByIdWithContent(place.getId());
				Relation livedIn = new Relation(this.entity, place0, lived_in);
				Calendar cal = livedInPlaces.getCalendar(place.getId());
				if(cal != null){
					livedIn.addAttribute(new Attribute("date", "date", cal.toJSONString()));
				}
			}
			
			// Person -> has_floruit_date -> FLORUIT DATE
			this.entity.removeAllSourceRelationsByName(has_floruit_date);
			for(Entity floruitDate : this.floruitList.getEntities()){
				Entity floruitDate0 = getWrapper().getEntityByIdWithContent(floruitDate.getId());
				Calendar cal = floruitList.getCalendar(floruitDate.getId());
				
				if(floruitDate0.getAttributeByName("date") == null){
					floruitDate0.addAttribute(new Attribute("date", "date", cal.toJSONString()));
				}else{
					floruitDate0.getAttributeByName("date").setValue(cal.toJSONString());
				}
				getWrapper().saveAssertion(floruitDate0, getUserName());
				Relation hasFloruitDate = new Relation(this.entity, floruitDate0, has_floruit_date);
			}
			
			//PERSON -> student of manyToMany -> PERSON
			this.entity.removeAllSourceRelationsByName(was_student_of);
			for(Entity teacher : this.studentOfList.getEntities()){
				Entity teacher0 = getWrapper().getEntityByIdWithContent(teacher.getId()); 
				Relation studentOf = new Relation(this.entity, teacher0, was_student_of);
			}
			
			//PERSON -> has_role -> ROLE
			this.entity.removeAllSourceRelationsByName(has_role);
			for(Entity role : this.roleList.getEntities()){
				Entity role0 = getWrapper().getEntityByIdWithContent(role.getId());
				Relation hasRole = new Relation(this.entity, role0, has_role);
			}
			
			//REFERENCE -> is_reference_of -> THIS
			//this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();
			
			this.entity = getWrapper().saveEntity(this.entity, getSessionUser().getEmail());
			this.setEntity(this.entity);
			this.getCache().setMapDirty(true);
			this.getAppBean().getBiographyBean().makeDirty();
			
			logger.info("Entity saved - Time = " + (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();

			//getPopup().setRendered(false);
			

			//setPopupText("CHANGING");
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}	
		saveEnd();
		return PAGE_EDITOR;
	}

	public void listenerEditCalendarForLivedInPlaces(ActionEvent event){
		Entity entity = (Entity)getRequestBean("item");
		if(entity != null){
			Calendar cal = this.livedInPlaces.getCalendar(entity.getId());
			getSessionBean().editCalendar(cal, livedInPlaces, entity.getId());
		}
	}
	
	public void listenerEditCalendarForFloruitDate(ActionEvent event){
		Entity entity = (Entity)getRequestBean("item");
		if(entity != null){
			Calendar cal = this.floruitList.getCalendar(entity.getId());
			getSessionBean().editCalendar(cal, floruitList, entity.getId());
		}
	}
	
	public void listenerRomanizeNameTranslit(AjaxBehaviorEvent event){
		if(getAttributes().get("name_translit") != null)
			this.romanizedNameTranslit = RomanizationLoC.convert(getAttributes().get("name_translit"));
	}

	public Entity getShortName() {
		return shortNameAlias;
	}

	public void setShortName(Entity shortName) {
		this.shortNameAlias = shortName;
	}

	public String getValueShortName() {
		return valueShortName;
	}

	public void setValueShortName(String valueShortName) {
		this.valueShortName = valueShortName;
	}
	
	public void setToDate(Object x) {
		System.out.println("hello");
	}

	public Converter getToDate() {
		return new myDateTimeConverter();

	}
	
	public Map<String, Boolean> getDeletedAliases() {
		return deletedAliases;
	}

	public void setDeletedAliases(Map<String, Boolean> deletedAliases) {
		this.deletedAliases = deletedAliases;
	}

	public void setNewAlias(String newAlias) {
		this.newAlias = newAlias;
	}

	public String getNewAlias() {
		return newAlias;
	}

	public String editMe(){
		if(this.entity != null){
			getSessionBean().editEntity(this.entity);
			return "entry_edit_entity";
		}
		return "";
	}

	public void setBirthPlaceLo(ListenerObject birthPlaceLo) {
		this.birthPlaceLo = birthPlaceLo;
	}

	public ListenerObject getBirthPlaceLo() {
		return birthPlaceLo;
	}

	public ListenerObject getDeathPlaceLo() {
		return deathPlaceLo;
	}

	public void setDeathPlaceLo(ListenerObject deathPlaceLo) {
		this.deathPlaceLo = deathPlaceLo;
	}

	public EntityList getLivedInPlaces() {
		return livedInPlaces;
	}

	public void setLivedInPlaces(EntityList livedInPlaces) {
		this.livedInPlaces = livedInPlaces;
	}

	public EntityList getStudentOfList() {
		return studentOfList;
	}


	public void setStudentOfList(EntityList studentOfList) {
		this.studentOfList = studentOfList;
	}

	public EntityList getFloruitList() {
		return floruitList;
	}

	public void setFloruitList(EntityList floruitList) {
		this.floruitList = floruitList;
	}

	public String getRomanizedNameTranslit() {
		return romanizedNameTranslit;
	}

	public void setRomanizedNameTranslit(String romanizedNameTranslit) {
		this.romanizedNameTranslit = romanizedNameTranslit;
	}	
}
