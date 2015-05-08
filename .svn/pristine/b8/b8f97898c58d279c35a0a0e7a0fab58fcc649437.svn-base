package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.Serializable;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;

import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;

public class CurrentCollectionBean extends CodexEditorTemplate implements
		Serializable {
	private static final long serialVersionUID = 5723693904746973203L;

	public static int MAX_PLACES = 100;
	public static int MAX_REPOSITORIES = 100;
	private static Logger logger = Logger
			.getLogger(CurrentCollectionBean.class);

	protected Boolean restrictCities = true;
	private Boolean restrictRepositories = true;

	// private Entity collection;

	@Override
	public void reset() {
		super.reset();
		this.entity = new Entity(Node.TYPE_ABOX, COLLECTION, false);

		this.restrictCities = true;
		this.restrictRepositories = true;

		this.entity = new Entity(Node.TYPE_ABOX, COLLECTION, false);
	}

	public CurrentCollectionBean() {
		this.reset();
		// setDefinition(getDefinition("COLLECTION"));
		setDefObjectClass(COLLECTION);
		registerChecker(getCityLo(), "City is not valid!");
		registerChecker(getCountryLo(), "Country is not valid!");
		registerChecker(getRepositoryLo(), "Repository is not valid!");
	}

	public Boolean getRestrictCities() {
		return restrictCities;
	}

	public void setRestrictCities(Boolean restrictCities) {
		this.restrictCities = restrictCities;
	}

	public Boolean getRestrictRepositories() {
		return restrictRepositories;
	}

	public void setRestrictRepositories(Boolean restrictRepositories) {
		this.restrictRepositories = restrictRepositories;
	}

	public void setEntity(Entity collection) {
		this.reset();
		this.entity = collection;

		if (this.entity != null && this.entity.isPersistent()) {
			if (this.entity.isLightweight()) {
				this.entity = getWrapper().getEntityContent(this.entity);
			}

			this.loadAttributes(this.entity);

			for (Entity target : getWrapper().getTargetsForSourceRelation(
					collection, "is_part_of", REPOSITORY, 1)) {
				setRepository(target);
			}

			// this.loadReferences(this.entity);
			this.loadEndNoteRefs();

			this.setCurrentId(this.entity.getId().toString());
			this.checkConsistencyFromCountryToCodex();
		}
	}

	public String saveAsNewEntity() {
		this.setSelectedSaveAsNew(true);
		return save();
	}

	@Override
	public String save() {
		super.save();
		try {

			CheckResults cr = getCheckResults();
			if (cr.hasErrors) {
				getSessionBean().setErrorMessages(cr);
				getSessionBean().setDisplayError(true);
				this.setSelectedSaveAsNew(false);
				return "";
			}

			this.entity = this.updateEntityAttributes(this.entity);
			// checking is the identifier is unique for the selected collecion
			Attribute attName = this.entity.getAttributeByName("name");
			String collectionName = (attName == null) ? null : attName
					.getValue();
			if (StringUtils.isNotEmpty(collectionName)) {
				if (!checkUnityCollection(collectionName,
						(isSelectedSaveAsNew()) ? null : this.entity.getId(),
						this.getRepositoryLo().entity)) {
					this.renderUnityCheckerDialog();
					this.setSelectedSaveAsNew(false);
					return "";
				}
			} else {
				this.addErrorMsg("Ths entity has not been saved, because its name was empty.");
				this.addErrorMsg("You must enter a name.");
				return "";
			}

			if (!isCollectionConsistentBeforeSave()) {
				return "";
			}

			// this.entity.removeSourceRelation(is_part_of
			// ,this.getRepositoryLo().entity);
			// this.replaceSourceRelation(this.entity,
			// this.getRepositoryLo().entity, "REPOSITORY", "is_part_of");
			if (this.getRepositoryLo().entity != null
					&& this.getRepositoryLo().entity.isLightweight()) {
				this.getRepositoryLo().entity = getWrapper()
						.getEntityByIdWithContent(
								this.getRepositoryLo().entity.getId());
			}
			this.entity.replaceSourceRelation(this.getRepositoryLo().entity,
					REPOSITORY, is_part_of);

			// REFERENCE -> is_reference_of -> THIS
			// this.entity = this.prepareReferencesToSave(this.entity);
			this.prepareEndNoteRefs2Save();

			if (isSelectedSaveAsNew()) {
				this.entity.removeAllTargetRelations(is_part_of, CODEX);
				this.entity = getWrapper().saveEntityAsNew(this.entity,
						getSessionUser().getEmail());
			} else {
				this.entity = getWrapper().saveEntity(this.entity,
						getSessionUser().getEmail());
			}

			this.generateSecundaryOW(this.entity, getSessionUser().getEmail());
			this.setCurrentId(this.entity.getId().toString());

			logger.info("Entity saved - Time = "
					+ (System.currentTimeMillis() - start) + ", " + entity);
			this.printSuccessSavingEntity();

			this.setSelectedSaveAsNew(false);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.printInternalError(e);
		}
		saveEnd();

		return PAGE_EDITOR;
	}

	/**
	 * <p>
	 * This method saves the collection without the loading of its contain when
	 * it is not necessary. LW means that the collection keeps light weight or
	 * without attributes and relation.
	 * </p>
	 * <p>
	 * It is normally used by classes (like: CurrentCodex, CurrentWitness) which
	 * inherit of this class.
	 * </p>
	 * 
	 * @param collection
	 */
	public void setLWCollection(Entity collection) {
		this.entity = collection;
	}
}
