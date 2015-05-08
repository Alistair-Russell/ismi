package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

public class EntityList extends AbstractISMIBean{
	
	private static final long serialVersionUID = -3339006604248018647L;

	private static Logger logger = Logger.getLogger(EntityList.class);
	
	private List<Entity> entities = new ArrayList<Entity>();
	private Map<Long, Boolean> selections = new HashMap<Long, Boolean>();
	private String title;
	
	//some relations have an attribute date
	private boolean useCalendar;
	private Map<Long, Calendar> calendarMap;
	
	private ListenerObject lo;
	
	private String input;
	private Long id;
	
	private WrapperService otg;
	private String user;

	public EntityList(String oc, String attName, String title){
		this.lo = new ListenerObject(oc, attName);
		this.title = title;
	}
	
	public EntityList(String oc, String attName, boolean useCalendar){
		this.lo = new ListenerObject(oc, attName);
		this.useCalendar = useCalendar;
		this.calendarMap = new HashMap<Long, Calendar>();
	}

	public EntityList(String oc, String attName, WrapperService otg, String user){
		this.lo = new ListenerObject(oc, attName);
		this.otg = otg;
		this.user = user;
	}
	
	public EntityList(String oc, String attName, WrapperService otg, String user, boolean useCalendar){
		this.lo = new ListenerObject(oc, attName);
		this.otg = otg;
		this.user = user;
		this.useCalendar = useCalendar;
		this.calendarMap = new HashMap<Long, Calendar>();
	}
	
	
	public void listenerAddRole(ActionEvent event){
		if(otg != null && isUnique(id)){
			Entity role = otg.getEntityById(id);
			if(role != null){
				this.add(role);
			}
		}
	}
	
	public void listenerCreate(ActionEvent event){
		try {
			if(otg != null && isUnique(input)){
				Entity e = new Entity(Node.TYPE_ABOX, lo.getClassObj(), false);
				e.setOwnValue(input);
				e.addAttribute(new Attribute(this.lo.getAttName(), "text", input));
				otg.saveEntity(e, this.user);
				this.add(e);	
			}			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
	}
	
	public void listenerCreateNoUnique(ActionEvent event){
		try {
			if(otg != null ){
				Entity e = new Entity(Node.TYPE_ABOX, lo.getClassObj(), false);
				e.setOwnValue(input);
				e.addAttribute(new Attribute(this.lo.getAttName(), "text", input));
				otg.saveEntity(e, this.user);
				this.add(e);
			}			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
	}

	private boolean isUnique(Long id){
		for(Entity e : entities){
			if(e.getId().compareTo(id) == 0){
				addErrorMsg("This entity was already selected.");
				return false;
			}
		}
		return true;
	}
	
	private boolean isUnique(String input){
		for(Entity e : entities){
			if(e.getOwnValue().equals(input)){
				addErrorMsg("An entity " + lo.getClassObj() + " with name " + input + " exists already.");
				return false;
			}
		}
		return true;
	}
	
	public void listenerChanged(ValueChangeEvent event) {
		this.lo = changeListener(event, lo, this.lo.getClassObj(), this.lo.getAttName());
		if(lo.entity != null && lo.entity.isPersistent()){
			lo.statusImage.setStatus(StatusImage.STATUS_OK);
		}else{
			lo.statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
	}
	
	public void listenerAdd(ActionEvent event){
		this.add(this.lo.entity);
	}
	
	public void listenerRemoveSelection(ActionEvent event){
		for(Entity remove : getSelectedEntities()){
			this.remove(remove.getId());
		}
	}

	public void add(Entity e){
		if(e != null && e.isPersistent()){
			if(!selections.containsKey(e.getId())){
				this.entities.add(e);
				this.selections.put(e.getId(), false);
				Collections.sort(this.entities);
				if(useCalendar){
					Calendar cal = new Calendar();
					this.calendarMap.put(e.getId(), cal);
				}
			}
		}
	}
	
	public void add(Entity e, Attribute calendarAtt){
		if(e != null && e.isPersistent()){
			if(!selections.containsKey(e.getId())){
				this.entities.add(e);
				this.selections.put(e.getId(), false);
				Collections.sort(this.entities);
				if(useCalendar){
					if(calendarAtt == null){
						this.calendarMap.put(e.getId(), new Calendar());
					}else{
						this.calendarMap.put(e.getId(), AbstractISMIBean.updateCalendar(calendarAtt));
					}
				}
			}
		}		
	}
	
	public Calendar getCalendar(Long entId){
		if(calendarMap != null){
			return calendarMap.get(entId);
		}
		return null;
	}
	
	public void addList(List<Entity> list){
		for(Entity e : list){
			this.add(e);
		}
	}

	public void remove(Long id){
		this.selections.remove(id);
		Entity e = null;
		for(Entity ent : entities){
			if(ent.getId().intValue() == id){
				e = ent;
				break;
			}
		}
		this.entities.remove(e);
	}
	

	
	private List<Entity> getSelectedEntities(){
		List<Entity> list = new ArrayList<Entity>();
		for(Entity e : entities){
			if(selections.get(e.getId())){
				list.add(e);
			}
		}
		return list;
	}
	
	
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public Map<Long, Boolean> getSelections() {
		return selections;
	}

	public void setSelections(Map<Long, Boolean> selections) {
		this.selections = selections;
	}

	public ListenerObject getLo() {
		return lo;
	}

	public void setLo(ListenerObject lo) {
		this.lo = lo;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Long, Calendar> getCalendarMap() {
		return calendarMap;
	}

	public void setCalendarMap(Map<Long, Calendar> calendarMap) {
		this.calendarMap = calendarMap;
	}

	public boolean isUseCalendar() {
		return useCalendar;
	}

	public String getTitle() {
		return title;
	}
}
