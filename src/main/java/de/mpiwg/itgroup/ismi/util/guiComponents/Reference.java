package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

public class Reference implements Cloneable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8933033295341938974L;
	public static String ADD_INFORMATION = "additional_information";
	public static String ESCIDOC_ID = "id";
	public static String ENDNOTE_ID = "endnote-id";
	public static String ENDNOTE_CONTENT = "endnote-content";
	
	//public static int EDITION = 0;
	//public static int CREATION = 1;
	
	private Entity ent;
	private String endNoteId;
	private String endNoteContent;
	private String addInformation;
	private String escidocId;
	//private int mode = CREATION;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public boolean equals(Object o){
		
		if(o instanceof Reference){
			Reference other = (Reference)o;
			if(StringUtils.equals(endNoteId, other.endNoteId) &&
					StringUtils.equals(endNoteContent, other.endNoteContent) &&
					StringUtils.equals(addInformation, other.addInformation) &&
					StringUtils.equals(escidocId, other.escidocId)){
				//@TODO compare entities
				return true;
			}
		}
		
		return false;
	}
	
	public Reference(Entity ent){
		this.setEnt(ent);
	}
	
	public void setEnt(Entity ent){
		if(ent != null){
			this.ent = ent;
			//this.mode = EDITION;
			if(ent.containsAttribute(ADD_INFORMATION)){
				this.addInformation = ent.getAttributeByName(ADD_INFORMATION).getValue();
			}
			if(ent.containsAttribute(ESCIDOC_ID)){
				this.escidocId = ent.getAttributeByName(ESCIDOC_ID).getValue();
			}
			if(ent.containsAttribute(ENDNOTE_ID)){
				this.endNoteId = ent.getAttributeByName(ENDNOTE_ID).getValue();
			}
			if(ent.containsAttribute(ENDNOTE_CONTENT)){
				this.endNoteContent = ent.getAttributeByName(ENDNOTE_CONTENT).getValue();
			}
		}else{
			//this.mode = CREATION;
		}
	}	
	
	public boolean isEmpty(){
		if(ent == null &&
				StringUtils.isEmpty(endNoteId) &&
				StringUtils.isEmpty(endNoteContent) &&
				StringUtils.isEmpty(addInformation) &&
				StringUtils.isEmpty(escidocId)){
			return true;
		}
		return false;
	}
	
	public Entity getEnt(){
		//xxxx
		if(this.ent == null){
			this.ent = new Entity(Node.TYPE_ABOX, AbstractISMIBean.REFERENCE, false);			
		}
		if(!ent.containsAttribute(ADD_INFORMATION)){
			ent.addAttribute(new Attribute(ADD_INFORMATION, "text", this.addInformation));
		}
		if(!ent.containsAttribute(ESCIDOC_ID)){
			ent.addAttribute(new Attribute(ESCIDOC_ID, "text", this.escidocId));
		}
		if(!ent.containsAttribute(ENDNOTE_ID)){
			ent.addAttribute(new Attribute(ENDNOTE_ID, "text", this.endNoteId));
		}
		if(!ent.containsAttribute(ENDNOTE_CONTENT)){
			ent.addAttribute(new Attribute(ENDNOTE_CONTENT, "text", this.endNoteContent));
		}
		
		//xxxx
		if(StringUtils.isNotEmpty(addInformation)){
			ent.getAttributeByName(ADD_INFORMATION).setValue(addInformation);
		}
		if(StringUtils.isNotEmpty(escidocId)){
			ent.getAttributeByName(ESCIDOC_ID).setValue(escidocId);
		}
		if(StringUtils.isNotEmpty(endNoteId)){
			ent.getAttributeByName(ENDNOTE_ID).setValue(endNoteId);
		}
		if(StringUtils.isNotEmpty(endNoteContent)){
			ent.getAttributeByName(ENDNOTE_CONTENT).setValue(endNoteContent);
		}
				
		return ent;		
	}

	public String getEndNoteId() {
		return endNoteId;
	}

	public void setEndNoteId(String endNoteId) {
		this.endNoteId = endNoteId;
	}

	public String getEndNoteContent() {
		return endNoteContent;
	}

	public void setEndNoteContent(String endNoteContent) {
		this.endNoteContent = endNoteContent;
	}

	public String getAddInformation() {
		return addInformation;
	}

	public void setAddInformation(String addInformation) {
		this.addInformation = addInformation;
	}

	public String getEscidocId() {
		return escidocId;
	}

	public void setEscidocId(String escidocId) {
		this.escidocId = escidocId;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Reference=");
		if(ent != null){
			sb.append(ent.toString() + "\n");			
		}
		if(StringUtils.isNotEmpty(endNoteId)){
			sb.append(endNoteId + "\n");
		}
		if(StringUtils.isNotEmpty(endNoteContent)){
			sb.append(endNoteContent + "\n");
		}
		if(StringUtils.isNotEmpty(escidocId)){
			sb.append(escidocId);
		}
		return sb.toString();
	}
	
	/*
	public int getMode(){
		return mode;
	}*/
}
