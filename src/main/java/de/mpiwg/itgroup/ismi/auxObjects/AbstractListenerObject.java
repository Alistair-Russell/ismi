package de.mpiwg.itgroup.ismi.auxObjects;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;

public abstract class AbstractListenerObject extends AbstractBean{

	private static final long serialVersionUID = -7874914085766225119L;
	private static int MAX_SUGGEST = 25;
	
	public List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
	public transient StatusImage statusImage = new StatusImage();
	public Attribute attribute = new Attribute();
	public Entity entity = new Entity();
	public String entityInfo; 

	protected void changeListener(
			ValueChangeEvent event,
			String suggestType, 
			String suggestAttributeName) {

		changeListener(
			event, 
			suggestType, 
			suggestAttributeName,
			null, 
			null);
	}
	
	protected void changeListener(
			ValueChangeEvent event,
			String suggestType, 
			String suggestAttributeName,
			String restrictingAttributeName, 
			String restrictingAttributeValue) {
		
		try {
			if (event.getNewValue() == null) {
				return;
			}else if(StringUtils.isEmpty(event.getNewValue().toString()) && (event.getOldValue() == null || StringUtils.isEmpty(event.getOldValue().toString()))){
				//if the old and new value are empty, then return
				return;
			}
			if (event.getNewValue().equals(event.getOldValue())) {
				return;
			}

			String ownvalue = (String) event.getNewValue();

			if(StringUtils.isEmpty(ownvalue))
				statusImage.setStatus(StatusImage.STATUS_UNSET);
			else
				statusImage.setStatus(StatusImage.STATUS_FALSE);
			
			this.entity = null;
			// setze erst mal den Eigenwert auf das eingebene.
			Attribute at = new Attribute();
			at.setOwnValue(ownvalue);
			attribute = at;
			for (SelectItem item : this.suggestedItems) {
				if (StringUtils.isNotEmpty(item.getLabel()) && 
						item.getLabel().equals(ownvalue)) {
					//System.out.println("item.getValue()= " + item.getValue());
					this.attribute = (Attribute) item.getValue();
					Entity element = getWrapper().getEntityById(
							this.attribute.getSourceId());

					if (element != null) {
						if(element.isLightweight()){
							element = getWrapper().getEntityByIdWithContent(element.getId());
						}
						entity = element;
						statusImage.setStatus(StatusImage.STATUS_OK);
					}
					break;
				}

			}

			if (restrictingAttributeName == null) {
				suggestedItems = this.updateSuggestedItems(event,
						suggestType, suggestAttributeName);
			} else {
 				suggestedItems = this.updateSuggestedItems(suggestType,
						suggestAttributeName, event.getNewValue().toString(),
						restrictingAttributeName, restrictingAttributeValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	protected List<SelectItem> updateSuggestedItems(String objClass,
			String firstName, String firstValue, String secondName,
			String secondValue) {

		List<Attribute> attList = getWrapper().
			searchAttribute(firstName, firstValue, secondName, secondValue, objClass, MAX_SUGGEST);
			
		List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
		if (attList == null)
			return suggestedItems;

		for (Attribute att : attList) {
			SelectItem item = new SelectItem(att, att.getOwnValue() + " [" + att.getSourceId() + "]");
			suggestedItems.add(item);
		}
		return suggestedItems;
	}
	
	protected List<SelectItem> updateSuggestedItems(ValueChangeEvent event,
			String objClass, String attName) {
		return updateSuggestedItems((String) event.getNewValue(), objClass,
				attName);
	}
	
	protected List<SelectItem> updateSuggestedItems(String searchWord,
			String objClass, String attName) {

		List<Attribute> attList = getWrapper().getAttributesByDefByAttName(objClass, attName, searchWord.toString(), MAX_SUGGEST);
		
		List<SelectItem> suggestedItems = new ArrayList<SelectItem>();
		if (attList == null)
			return suggestedItems;

		for (Attribute att : attList) {
			SelectItem item = new SelectItem(att, att.getOwnValue() + " [" + att.getSourceId() + "]",
					"description: " + att);
			suggestedItems.add(item);
		}
		return suggestedItems;
	}

	public void setEntity(Entity ent) {
		this.entity = ent;
		if(ent != null && ent.isPersistent()){
			if(this.entity.isLightweight()){
				this.entity = getWrapper().getEntityByIdWithContent(this.entity.getId());
			}
			this.setStatus(StatusImage.STATUS_OK);	
			this.entityInfo = "ID = " + this.entity.getId();
			/*
			if(classObj.equals("PERSON")){
				Attribute attArabicName = getTargetAttribute(ent, "name");
				if(attArabicName != null){
					this.authorInfo += ", Arabic Name = " + attArabicName.getOwnValue();
					this.textAuthorName = attArabicName.getValue();
				}
				
				if(this.authorLo.attribute != null){
					this.textAuthorNameTranslit = this.authorLo.attribute.getValue();
				}	
			}
			*/
		}else{
			this.setStatus(StatusImage.STATUS_UNSET);
		}
	}
	
	public void setStatus(String newStatus){
		statusImage.setStatus(newStatus);
	}
	
	protected WrapperService getWrapper() {
		ApplicationBean app = (ApplicationBean) getApplicationBean("ApplicationBean1");
		return app.getWrapper();
	}
}
