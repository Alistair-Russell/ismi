package de.mpiwg.itgroup.ismi.defs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.browse.EntityRepositoryBean;
import de.mpiwg.itgroup.ismi.utils.SelectableObject;

public class DefinitionForm extends AbstractDefinitionForm implements
		Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(DefinitionForm.class);

	public void createDefinition(ActionEvent event) {
		this.tmpLWDefinition = new Entity(Node.TYPE_TBOX, Node.TYPE_TBOX, false);
	}

	public void cancelCreationDefinition(ActionEvent event) {
		this.tmpLWDefinition = null;
		this.selectedLWDefinition = null;
	}

	public void saveLWDefinition(ActionEvent event) {
		try {
			if (tmpLWDefinition != null
					&& StringUtils.isNotBlank(tmpLWDefinition.getOwnValue())) {

				String ov = tmpLWDefinition.getOwnValue();
				tmpLWDefinition.setOwnValue(convertDefOW(ov));

				this.selectedLWDefinition = this.getWrapper().saveLWDefinition(
						tmpLWDefinition, getSessionBean().getUsername());
				this.tmpLWDefinition = null;
				this.loadDefinition();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}

	}

	public DefinitionForm() {
		this.loadDefinitions();
	}

	public void listenerCancelEdition(ActionEvent event) {
		this.loadDefinitions();
	}

	public void loadDefinitions() {
		this.defList = getWrapper().getLWDefinitions();
		this.defSelectList = new ArrayList<SelectItem>();

		this.selectedLWDefinition = null;
		for (Entity def : this.defList) {
			this.defSelectList.add(new SelectItem(def.getId(), def
					.getOwnValue()));
		}
		EntityRepositoryBean bean = (EntityRepositoryBean) getSessionBean("EntityRepository");
		if (bean != null) {
			bean.updateDefinitions(this.defList);
		}
	}

	public void loadDefinition() {
		this.attList = new ArrayList<SelectableObject<Attribute>>();
		for (Attribute att : getWrapper().getDefAttributes(
				this.selectedLWDefinition.getOwnValue())) {
			this.attList.add(new SelectableObject<Attribute>(att));
		}
		this.srcRelList = new ArrayList<SelectableObject<Relation>>();
		for (Relation rel : getWrapper().getDefSourceRelations(
				this.selectedLWDefinition.getOwnValue())) {
			rel.setSource(selectedLWDefinition);
			rel.setTarget(getWrapper().getDefinitionById(rel.getTargetId()));
			srcRelList.add(new SelectableObject<Relation>(rel));
		}
		this.tarRelList = new ArrayList<SelectableObject<Relation>>();
		for (Relation rel : getWrapper().getDefTargetRelations(
				this.selectedLWDefinition.getOwnValue())) {
			rel.setSource(getWrapper().getDefinitionById(rel.getSourceId()));
			rel.setTarget(selectedLWDefinition);
			tarRelList.add(new SelectableObject<Relation>(rel));
		}
	}

	public void listenerShowDefinition(ActionEvent event) {
		this.selectedLWDefinition = (Entity) getRequestBean("def");
		if (this.selectedLWDefinition != null) {
			this.loadDefinition();
		} else {
			addGeneralMsg("The definition could not be loaded.");
		}
	}

	// ********************* Possible Values for attributes***********
	public void listenerAddPossibleValue(ActionEvent event) {
		this.possibleValuesList.add(new SelectableObject<String>(new String()));
	}

	public void listenerDeletePossibleValues(ActionEvent event) {
		List<SelectableObject<String>> toDelete = new ArrayList<SelectableObject<String>>();
		for (SelectableObject<String> so : this.possibleValuesList) {
			if (so.isSelected()) {
				toDelete.add(so);
			}
		}
		for (SelectableObject<String> so : toDelete) {
			this.possibleValuesList.remove(so);
		}
	}

	// ***************************************************************

	// **************************** Relations ************************
	public void listenerDeleteSourceRelations(ActionEvent event) {
		try {
			List<SelectableObject<Relation>> toDelete = new ArrayList<SelectableObject<Relation>>();
			for (SelectableObject<Relation> so : this.srcRelList) {
				if (so.isSelected()) {
					toDelete.add(so);
				}
			}
			for (SelectableObject<Relation> so : toDelete) {
				this.getWrapper().removeDefRelation((Relation) so.getObj());
			}
			this.loadDefinition();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}
	}

	public void listenerDeleteTargetRelations(ActionEvent event) {
		try {
			List<SelectableObject<Relation>> toDelete = new ArrayList<SelectableObject<Relation>>();
			for (SelectableObject<Relation> so : this.tarRelList) {
				if (so.isSelected()) {
					toDelete.add(so);
				}
			}
			for (SelectableObject<Relation> so : toDelete) {
				this.getWrapper().removeDefRelation((Relation) so.getObj());
			}
			this.loadDefinition();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}
	}

	public void listenerEditSourceRelation(ActionEvent event) {
		SelectableObject<Relation> so = (SelectableObject<Relation>) getRequestBean("relObj");
		if (so != null && so.getObj() != null
				&& so.getObj() instanceof Relation) {
			Relation rel = so.getObj();
			this.selectedRelation = (Relation) rel.clone();

			this.selectedRelationAttributes = new ArrayList<SelectableObject<Attribute>>();
			for (Attribute att : getWrapper().getDefRelationAttributes(
					rel.getId())) {
				this.selectedRelationAttributes
						.add(new SelectableObject<Attribute>(att));
			}
		}
		this.isSourceRelation = true;
	}

	public void listenerEditTargetRelation(ActionEvent event) {
		SelectableObject<Relation> so = (SelectableObject<Relation>) getRequestBean("relObj");
		if (so != null && so.getObj() != null
				&& so.getObj() instanceof Relation) {
			Relation rel = so.getObj();
			this.selectedRelation = (Relation) rel.clone();
		}
		this.isSourceRelation = false;
	}

	public void listenerCancelEditionOfRelation(ActionEvent event) {
		this.selectedRelation = null;
	}

	public void listenerSaveRelation(ActionEvent event) {
		try {
			if (!this.isRelationUnique(selectedRelation)) {
				addGeneralMsg("The relation could not be saved. There is already a relation with same source, target and label.");
				return;
			}
			if (!this.isOKRelationAttributes()) {
				addGeneralMsg("The relation could not be saved. There are some attributes for this relation with same name or with empty name.");
				return;
			}

			this.selectedRelation.setSourceModif(this.selectedLWDefinition
					.getModificationTime());
			this.selectedRelation
					.setSourceObjectClass(this.selectedLWDefinition
							.getObjectClass());
			this.getWrapper().saveDefRelation(selectedRelation, getSessionBean().getUsername());
			for (SelectableObject<Attribute> so : this.selectedRelationAttributes) {
				Attribute att = so.getObj();
				att.setSourceId(this.selectedRelation.getId());
				att.setSourceModif(this.selectedRelation.getModificationTime());
				att.setSourceObjectClass(this.selectedRelation.getObjectClass());
				this.getWrapper().saveDefAttribute(att, getSessionBean().getUsername());
			}
			this.selectedRelation = null;
			this.loadDefinition();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}
	}

	public void listenerCreateSourceRelation(ActionEvent event) {
		this.selectedRelation = new Relation();
		this.selectedRelation.setSource(selectedLWDefinition);
		this.isSourceRelation = true;
		this.selectedRelationAttributes = new ArrayList<SelectableObject<Attribute>>();
	}

	public void listenerCreateTargetRelation(ActionEvent event) {
		this.selectedRelation = new Relation();
		this.selectedRelation.setTarget(selectedLWDefinition);
		this.isSourceRelation = false;
		this.selectedRelationAttributes = new ArrayList<SelectableObject<Attribute>>();
	}

	// ******************** Attributes for relation *****************

	public void listenerCreateAttributeForRelation(ActionEvent event) {
		Attribute att = new Attribute(Node.TYPE_TBOX, "text", "");
		att.setSourceId(this.selectedRelation.getId());
		att.setSourceObjectClass(Node.TYPE_TBOX);
		att.setSourceModif(this.selectedRelation.getModificationTime());
		att.setSystemStatus(Node.SYS_STATUS_CURRENT_VERSION);
		if (selectedRelationAttributes == null) {
			this.selectedRelationAttributes = new ArrayList<SelectableObject<Attribute>>();
		}
		this.selectedRelationAttributes
				.add(new SelectableObject<Attribute>(att));
	}

	public void listenerDeleteAttributesForRelation(ActionEvent event) {
		try {
			List<SelectableObject<Attribute>> toDelete = new ArrayList<SelectableObject<Attribute>>();
			for (SelectableObject<Attribute> so : this.selectedRelationAttributes) {
				if (so.isSelected()) {
					toDelete.add(so);
				}
			}
			for (SelectableObject<Attribute> so : toDelete) {
				this.selectedRelationAttributes.remove(so);
				Attribute att = (Attribute) so.getObj();
				if (att.getId() != null) {
					this.getWrapper().removeDefAttribute(att);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}

	}

	// ***************************************************************
	// **************************** Attributes ***********************

	public void listenerCreateAttribute(ActionEvent event) {
		this.selectedAttribute = new Attribute(Node.TYPE_TBOX, "text", "");
		this.selectedAttribute.setSourceId(this.selectedLWDefinition.getId());
		this.selectedAttribute.setSourceObjectClass(Node.TYPE_TBOX);
		this.selectedAttribute.setSourceModif(this.selectedLWDefinition
				.getModificationTime());
		this.selectedAttribute.setSystemStatus(Node.SYS_STATUS_CURRENT_VERSION);

		this.possibleValuesList = new ArrayList<SelectableObject<String>>();
		if (possibleValuesList.size() == 0) {
			this.possibleValuesList.add(new SelectableObject<String>(
					new String()));
		}
	}

	public void listenerEditAttribute(ActionEvent event) {
		SelectableObject<Attribute> so = (SelectableObject<Attribute>) getRequestBean("attObj");
		if (so != null && so.getObj() != null
				&& so.getObj() instanceof Attribute) {
			Attribute att = (Attribute) so.getObj();
			this.selectedAttribute = (Attribute) att.clone();

			this.possibleValuesList = new ArrayList<SelectableObject<String>>();
			for (String possibleValue : selectedAttribute
					.getPossibleValuesList()) {
				this.possibleValuesList.add(new SelectableObject<String>(
						possibleValue));
			}
			if (possibleValuesList.size() == 0) {
				this.possibleValuesList.add(new SelectableObject<String>(
						new String()));
			}
		}
	}

	public void listenerSaveAttribute(ActionEvent event) {
		try {
			this.selectedAttribute
					.setOwnValue(convertAttOrRelOW(selectedAttribute
							.getOwnValue()));

			if (StringUtils.isBlank(selectedAttribute.getOwnValue())) {
				addGeneralMsg("The attribute could not be saved."
						+ " The attribute must have a name.");
				return;
			}

			if (!isAttributeNameUnique(selectedAttribute)) {
				addGeneralMsg("The attribute could not be saved."
						+ " There is already a attribute with the same name.");
				return;
			}
			this.savePossibleValues();
			getWrapper().saveDefAttribute(selectedAttribute, getSessionBean().getUsername());
			this.selectedAttribute = null;
			this.loadDefinition();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}
	}

	private void savePossibleValues() {
		List<String> list = new ArrayList<String>();
		for (SelectableObject<String> so : this.possibleValuesList) {
			String value = so.getObj();
			if (StringUtils.isNotBlank(value)) {
				if (list.contains(value)) {
					addGeneralMsg("There are two or more values equals.");
					return;
				} else {
					list.add(value);
				}
			}
		}

		Collections.sort(list);
		this.selectedAttribute.setPossibleValuesList(list);
	}

	private boolean isRelationUnique(Relation editingRel) {
		for (SelectableObject<Relation> so : this.srcRelList) {
			Relation rel = so.getObj();
			if (!rel.getId().equals(editingRel.getId())
					&& rel.getSourceId().equals(editingRel.getSourceId())
					&& rel.getTargetId().equals(editingRel.getTargetId())
					&& rel.getOwnValue().equals(editingRel.getOwnValue())) {
				return false;
			}
		}
		for (SelectableObject<Relation> so : this.tarRelList) {
			Relation rel = so.getObj();
			if (!rel.getId().equals(editingRel.getId())
					&& rel.getSourceId().equals(editingRel.getSourceId())
					&& rel.getTargetId().equals(editingRel.getTargetId())
					&& rel.getOwnValue().equals(editingRel.getOwnValue())) {
				return false;
			}
		}
		return true;
	}

	private boolean isOKRelationAttributes() {
		List<String> testList = new ArrayList<String>();
		if (selectedRelationAttributes != null) {
			for (SelectableObject<Attribute> so : this.selectedRelationAttributes) {
				Attribute att = so.getObj();
				if (StringUtils.isBlank(att.getOwnValue())) {
					return false;
				}
				att.setOwnValue(convertAttOrRelOW(att.getOwnValue()));
				if (testList.contains(att.getOwnValue())) {
					return false;
				} else {
					testList.add(att.getOwnValue());
				}
			}
		}
		return true;
	}

	private boolean isAttributeNameUnique(Attribute editingAtt) {
		for (SelectableObject<Attribute> so : this.attList) {
			Attribute att = so.getObj();
			if (!att.getId().equals(editingAtt.getId())
					&& att.getOwnValue().equals(editingAtt.getOwnValue())) {
				return false;
			}
		}
		return true;
	}

	public void listenerCancelEditionOfAttribute(ActionEvent event) {
		this.selectedAttribute = null;
		this.possibleValuesList = null;
	}

	public void listenerDeleteAttributes(ActionEvent event) {
		try {
			List<SelectableObject<Attribute>> toDelete = new ArrayList<SelectableObject<Attribute>>();
			for (SelectableObject<Attribute> so : this.attList) {
				if (so.isSelected()) {
					toDelete.add(so);
				}
			}
			for (SelectableObject<Attribute> so : toDelete) {
				this.getWrapper().removeDefAttribute(so.getObj());
			}
			this.loadDefinition();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			this.printInternalError(e);
		}
	}
	// ***************************************************************
	// ***************************************************************
}
