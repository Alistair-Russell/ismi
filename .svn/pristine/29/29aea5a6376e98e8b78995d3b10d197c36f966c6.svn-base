package de.mpiwg.itgroup.ismi.utils.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpiwg.itgroup.escidoc.ESciDocHandler;
import org.mpiwg.itgroup.escidoc.bo.Publication;

import de.mpiwg.itgroup.ismi.util.guiComponents.Reference;

public abstract class AbstractTemplate implements Serializable{
	
	private static final long serialVersionUID = -3998660931870870851L;
	
	
	protected List<Entity> refEntityList = new ArrayList<Entity>();
	private List<ReferenceTemplate> referenceList = new ArrayList<AbstractTemplate.ReferenceTemplate>();
	
	protected void loadRefernces(){
		try{
			for(Entity ref : refEntityList){
				this.referenceList.add(new ReferenceTemplate(ref));	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class ReferenceTemplate implements Serializable{
		
		private static final long serialVersionUID = -8257628538319156038L;
		
		private String escidocLabel;
		private String additionalInf;
		private String endnoteId;
		private String endnoteContent;
		
		public ReferenceTemplate(Entity ent){
			
			Attribute att = ent.getAttributeByName(Reference.ESCIDOC_ID);
			try {
				if(att != null && StringUtils.isNotEmpty(att.getValue())){
					Publication pub = ESciDocHandler.getPublicationById(att.getValue());
					if(pub != null){
						this.escidocLabel = pub.getHTMLLabel();
					}	
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			this.additionalInf = (ent.getAttributeByName("additional_information") != null) ? ent.getAttributeByName("additional_information").getValue() : null;
			this.endnoteId = (ent.getAttributeByName("endnote-id") != null) ? ent.getAttributeByName("endnote-id").getValue() : null;
			this.endnoteContent = (ent.getAttributeByName("endnote-content") != null) ? ent.getAttributeByName("endnote-content").getValue() : null;
			
		}
		
		public String getEscidocLabel() {
			return escidocLabel;
		}
		public void setEscidocLabel(String escidocLabel) {
			this.escidocLabel = escidocLabel;
		}
		public String getAdditionalInf() {
			return additionalInf;
		}
		public void setAdditionalInf(String additionalInf) {
			this.additionalInf = additionalInf;
		}
		public String getEndnoteId() {
			return endnoteId;
		}
		public void setEndnoteId(String endnoteId) {
			this.endnoteId = endnoteId;
		}
		public String getEndnoteContent() {
			return endnoteContent;
		}
		public void setEndnoteContent(String endnoteContent) {
			this.endnoteContent = endnoteContent;
		}
	}

	public List<ReferenceTemplate> getReferenceList() {
		return referenceList;
	}

	public void setReferenceList(List<ReferenceTemplate> referenceList) {
		this.referenceList = referenceList;
	}
}
