package de.mpiwg.itgroup.ismi.defs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

public class AbstractDefinitionForm extends AbstractBean  implements Serializable{
	protected static final long serialVersionUID = 1L;
	
	protected List<Entity> defList;
	protected List<SelectItem> defSelectList;
	
	protected Entity selectedLWDefinition;
	protected Entity tmpLWDefinition;
	protected List<SelectableObject<Attribute>> attList;
	protected List<SelectableObject<Relation>> srcRelList;
	protected List<SelectableObject<Relation>> tarRelList;
	
	protected Attribute selectedAttribute;
	
	protected Relation selectedRelation;
	protected List<SelectableObject<Attribute>> selectedRelationAttributes;
	
	protected List<SelectableObject<String>> possibleValuesList = null;
	
	protected boolean isSourceRelation = false;
	
	private static List<SelectItem> contentTypeList = new ArrayList<SelectItem>();
	
	static{
		contentTypeList.add(new SelectItem("", "-- select one --"));
		contentTypeList.add(new SelectItem("arabic"));
		contentTypeList.add(new SelectItem("date"));
		contentTypeList.add(new SelectItem("escidoc-objid"));
		contentTypeList.add(new SelectItem("link"));
		contentTypeList.add(new SelectItem("json"));
		contentTypeList.add(new SelectItem("text"));
		contentTypeList.add(new SelectItem("boolean"));
		contentTypeList.add(new SelectItem("xml"));	
		contentTypeList.add(new SelectItem("geoname-id"));	
	}
	
	public List<SelectItem> getContentTypeList(){
		return contentTypeList;
	}
	
	public boolean isSourceRelation() {
		return isSourceRelation;
	}

	public List<Entity> getDefList() {
		return defList;
	}

	public void setDefList(List<Entity> defList) {
		this.defList = defList;
	}

	public List<SelectItem> getDefSelectList() {
		return defSelectList;
	}

	public void setDefSelectList(List<SelectItem> defSelectList) {
		this.defSelectList = defSelectList;
	}

	public Entity getSelectedLWDefinition() {
		return selectedLWDefinition;
	}

	public void setSelectedLWDefinition(Entity selectedLWDefinition) {
		this.selectedLWDefinition = selectedLWDefinition;
	}

	public List<SelectableObject<Attribute>> getAttList() {
		return attList;
	}

	public void setAttList(List<SelectableObject<Attribute>> attList) {
		this.attList = attList;
	}

	public List<SelectableObject<Relation>> getSrcRelList() {
		return srcRelList;
	}

	public void setSrcRelList(List<SelectableObject<Relation>> srcRelList) {
		this.srcRelList = srcRelList;
	}

	public List<SelectableObject<Relation>> getTarRelList() {
		return tarRelList;
	}

	public void setTarRelList(List<SelectableObject<Relation>> tarRelList) {
		this.tarRelList = tarRelList;
	}
	
	public Attribute getSelectedAttribute() {
		return selectedAttribute;
	}

	public void setSelectedAttribute(Attribute selectedAttribute) {
		this.selectedAttribute = selectedAttribute;
	}

	public Relation getSelectedRelation() {
		return selectedRelation;
	}

	public void setSelectedRelation(Relation selectedRelation) {
		this.selectedRelation = selectedRelation;
	}

	public List<SelectableObject<String>> getPossibleValuesList() {
		return possibleValuesList;
	}

	public void setPossibleValuesList(List<SelectableObject<String>> possibleValuesList) {
		this.possibleValuesList = possibleValuesList;
	}
	
	public List<SelectableObject<Attribute>> getSelectedRelationAttributes() {
		return selectedRelationAttributes;
	}

	public void setSelectedRelationAttributes(
			List<SelectableObject<Attribute>> selectedRelationAttributes) {
		this.selectedRelationAttributes = selectedRelationAttributes;
	}

	protected static String convertAttOrRelOW(String s){
		s = s.toLowerCase();
		s = s.replace(" ", "_");
		return s;
	}
	
	protected static String convertDefOW(String s){
		s = s.toUpperCase();
		s = s.replace(" ", "_");
		return s;
	}
	
	protected static List<Attribute> cloneAttributeList(List<Attribute> list){
		List<Attribute> nList = new ArrayList<Attribute>();
		for(Attribute att : list){
			nList.add((Attribute)att.clone());
		}
		return nList;
	}

	public Entity getTmpLWDefinition() {
		return tmpLWDefinition;
	}

	public void setTmpLWDefinition(Entity tmpLWDefinition) {
		this.tmpLWDefinition = tmpLWDefinition;
	}
}
