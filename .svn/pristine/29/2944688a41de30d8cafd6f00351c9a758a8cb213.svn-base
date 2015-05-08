package de.mpiwg.itgroup.ismi.entry.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.auxObjects.ListenerObject;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessCityLO;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessCollectionLO;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessCountryLO;
import de.mpiwg.itgroup.ismi.auxObjects.lo.WitnessRepositoryLO;
import de.mpiwg.itgroup.ismi.util.guiComponents.StatusImage;
import de.mpiwg.itgroup.ismi.utils.SelectItemSort;


/**
 * CODEX -> is_part_of -> COLLECTION
 * COLLECTION -> is_part_of -> REPOSITORY
 * REPOSITORY -> is_in -> PLACE[city]
 * PLACE[city] -> is_part_of -> PLACE[country]
 * @author jurzua
 *
 */
public class CodexEditorTemplate extends UnityChecker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7580021280544772497L;
	private static Logger logger = Logger.getLogger(CodexEditorTemplate.class);
	public static int MAX_CODICES = 1000;
	public static int MAX_REPOSITORIES = 100;
	public static int MAX_COLLECTIONS = 100;
	public static int MAX_PLACES = 100;
	
	private ListenerObject countryLo;
	private ListenerObject cityLo;
	private ListenerObject repositoryLo;
	private ListenerObject collectionLo;
	private ListenerObject codexLo;
	
	private List<SelectItem> citiesInCurrentCountry;
	private List<SelectItem> repositoriesInCurrentCity;
	private List<SelectItem> collectionsInCurrentRepository;
	private List<SelectItem> shelfMarksInCurrentCollection;

	private boolean thereExistMoreCodices = false;
	
	public CodexEditorTemplate(){
		logger.debug("Init: " + this.getClass().getName());
		this.reset();
	}
	
	@Override
	public void reset(){
		super.reset();
		this.countryLo = new WitnessCountryLO(PLACE, "name", this);
		this.cityLo = new WitnessCityLO(PLACE, "name", this);
		this.repositoryLo = new WitnessRepositoryLO(REPOSITORY, "name", this);
		this.collectionLo = new WitnessCollectionLO(COLLECTION, "name", this);
		this.codexLo = new ListenerObject(CODEX, "identifier");
		
		this.citiesInCurrentCountry = new ArrayList<SelectItem>();
		this.repositoriesInCurrentCity = new ArrayList<SelectItem>();
		this.collectionsInCurrentRepository = new ArrayList<SelectItem>();
		this.shelfMarksInCurrentCollection = new ArrayList<SelectItem>();
		
		this.thereExistMoreCodices = false;
	}
	
	public void setCountry(Entity country){
		this.getCountryLo().setEntityAndAttribute0(country);
		this.restrictByCountry(country);
	}
	
	public void setCity(Entity city) {
		this.getCityLo().setEntityAndAttribute0(city);
		this.restrictByCity(city);
		
		List<Entity> list = getWrapper().getTargetsForSourceRelation(city, is_part_of, PLACE, 1);
		
		if(list.size() > 0){
			this.setCountry(list.get(0));
		}
	}

	public void setRepository(Entity repository) {
		this.getRepositoryLo().setEntityAndAttribute0(repository);
		this.restrictByRepository(repository);
		
		List<Entity> list = getWrapper().getTargetsForSourceRelation(repository, is_in, PLACE, 5);
		
		if(list.size() > 0){
			this.setCity(list.get(0));
		}
	}
	
	public void setCollection(Entity collection) {
		this.getCollectionLo().setEntityAndAttribute0(collection);
		this.restrictByCollection(collection);
		
		List<Entity> repoList = getWrapper().getTargetsForSourceRelation(collection, is_part_of, REPOSITORY, 1);
		
		if(repoList.size() > 0){
			this.setRepository(repoList.get(0));
		}
	}
	
	/**
	 * Listen to a change of the country where the codex is located. Restrict
	 * possible collections/repositories/codices to object in this city.
	 * 
	 * @param event
	 */
	/* rich
	public void countryChangeListener(ValueChangeEvent event) {
		
		this.setCitiesInCurrentCountry(new ArrayList<SelectItem>());

		this.setCountryLo(changeListener(event, getCountryLo(), PLACE, "name","type","region"));
		if (getCountryLo().entity!=null && getCountryLo().entity.isPersistent()){
			this.setCountry(getCountryLo().entity);	
		}
		this.checkConsistencyFromCountryToCodex();
	}*/
	
	/**
	 * Listen to a change of the city where the codex is located. Restrict
	 * possible collections/repositories/codices to object in this city.
	 * 
	 * @param event
	 */
	/* rich
	public void cityChangeListener(ValueChangeEvent event) {
		
		this.setRepositoriesInCurrentCity(new ArrayList<SelectItem>());
		
		this.setCityLo(changeListener(event, getCityLo(), PLACE, "name", "type", "city"));
		
		if (this.getCityLo().entity!=null && this.getCityLo().entity.isPersistent()){
			this.setCity(this.getCityLo().entity);
		}
		this.checkConsistencyFromCountryToCodex();
		
	}
	*/
	/*
	public void repositoryChangeListener(ValueChangeEvent event) {
		
		this.setCollectionsInCurrentRepository(new ArrayList<SelectItem>());
		this.setShelfMarksInCurrentCollection(new ArrayList<SelectItem>());

		this.setRepositoryLo(changeListener(event, this.getRepositoryLo(), REPOSITORY, "name"));
		
		if (this.getRepositoryLo().entity!= null && this.getRepositoryLo().entity.isPersistent()){
			this.setRepository(this.getRepositoryLo().entity);
		}
		this.checkConsistencyFromCountryToCodex();
		
	}*/
	

	/**
	 * <p>When the collection is changed by the user this method will be called.</p>
	 * <p>This modification means that the COLLECTION of the relation CODEX->
	 * is_part_of-> COLLECTION is replaced.</p>
	 * @param newEvent
	 */
	/* rich
	public void collectionChangeListener(ValueChangeEvent event) {
		
		this.setShelfMarksInCurrentCollection(new ArrayList<SelectItem>());
		
		this.setCollectionLo(this.changeListener(event, this.getCollectionLo(), COLLECTION, "name"));
		
		Entity currentCollection = this.getCollectionLo().entity;
		if(currentCollection != null && currentCollection.isPersistent()){
			this.setCollection(currentCollection);
		}	
		
		this.checkConsistencyFromCountryToCodex();
		
	}*/
	
	public void inCurrentCountryListener(ValueChangeEvent event) {
		inCurrentListener(getCitiesInCurrentCountry(), event);
	}

	public void inCurrentCityListener(ValueChangeEvent event) {
		inCurrentListener(getRepositoriesInCurrentCity(), event);
	}

	public void inCurrentRepositoryListener(ValueChangeEvent event) {
		inCurrentListener(getCollectionsInCurrentRepository(), event);
	}

	public void inCurrentListener(List<SelectItem> items, ValueChangeEvent event) {
		if (event.getNewValue() == null) {
			return;
		}
		if (event.getNewValue().equals(event.getOldValue())) {
			return;
		}
		String newValue = (String) event.getNewValue();
		for (SelectItem item : items) {
			if (item.getValue().equals(newValue)) {
				//System.out.println(item.getValue());
				Entity ent = 
					getWrapper().getEntityById(Long.valueOf((String) item.getValue()));
				
				if(ent != null){
					String ct = ent.getObjectClass();
					if (ct.equals(CODEX)) {
						this.getCodexLo().setEntityAndAttribute0(ent);
						Attribute identifier = getWrapper().getAttributeByName(ent.getId(), "identifier");
						
						if(identifier != null){
							this.getCodexLo().statusImage.setStatus(StatusImage.STATUS_OK);
						}
						//changeValueAttCodex(this.getCodexLo().getAttribute().getOwnValue());
						break;
						
					} else if (ct.equals(REPOSITORY)) {
						this.setRepository(ent);
						break;
					} else if (ct.equals(PLACE)) {
						this.setCity(ent);
						break;

					} else if (ct.equals(COLLECTION)) {
						setCollection(ent);
						break;
					} else {
						break;
					}	
				}
			}else{
				logger.error("Entity no found " + item.getValue() + ", however it should exist.");
			}
		}
		this.checkConsistencyFromCountryToCodex();	
	}
	
	public void checkConsistencyFromCountryToCodex(ActionEvent event){
		this.checkConsistencyFromCountryToCodex();
	}
	
	public CodexForList getCodexData(Entity entity) {
		Entity currentRepository = null;

		CodexForList entForList = new CodexForList();
		entForList.id = entity.getId();
		if (entity.isLightweight()){
			entity = getWrapper().getEntityContent(entity);
		}	
		Attribute identAttr = entity.getAttributeByName("identifier");
		if (identAttr != null)
			entForList.ident = identAttr.getValue();
		List<Relation> rels = null;
		try {
			if (entity.isLightweight()){
				entity = getWrapper().getEntityContent(entity);
			}
				
			rels = entity.getSourceRelations();
		} catch (Exception e) {
			e.printStackTrace();
			rels = null;
		}
		if (rels != null) {
			for (Relation rel : rels) {

				Entity coll = getWrapper().getEntityById(rel.getTargetId());
				coll = getWrapper().getEntityContent(coll);

				if (rel.getOwnValue().equals(is_part_of)
						&& coll.getObjectClass().equals(COLLECTION)) {
					Attribute name = coll.getAttributeByName("name");
					if (name != null)
						entForList.collection = name.getValue();
					try {
						Relation rel2 = coll
								.getSourceRelationByOwnValue(is_part_of);
						if (rel2 != null) {
							currentRepository = getWrapper().getEntityById(rel2.getTargetId());
							Attribute nameRep = currentRepository
									.getAttributeByName("name");
							if (nameRep != null)
								entForList.repository = nameRep.getValue();

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (rel.getOwnValue().equals(is_part_of)
						&& coll.getObjectClass().equals(REPOSITORY)) {
					Attribute name = coll.getAttributeByName("name");
					if (name != null)
						entForList.repository = name.getValue();
					currentRepository = coll;
				} else if (rel.getOwnValue().equals(is_in)
						&& coll.getObjectClass().equals(PLACE)) {
					Attribute name = coll.getAttributeByName("name");
					// TODO: ersetze �ber all den String f�r das Attribute
					// zur
					// Anzeige durch eine Konstante bzw. durch
					// getAttRepository.getName, dann kann es leichter
					// geaendert
					// werden, bzw. ducrch eine dynamischen getOwnValue.
					if (name != null)
						entForList.place = name.getValue();
				}

			}
		}

		if (currentRepository != null) {
			Relation rel = null;
			try {
				if (currentRepository.isLightweight())
					entity = getWrapper().getEntityContent(currentRepository);

				rel = currentRepository.getSourceRelationByOwnValue(is_in);
			} catch (Exception e) {
				e.printStackTrace();
				rel = null;
			}
			if (rel != null) {
				Entity coll = getWrapper().getEntityById(rel.getSourceId());
				if (coll.getObjectClass().equals(PLACE)) {
					Attribute name = coll.getAttributeByName("name");
					// TODO: ersetze ueber all den String fuer das Attribute
					// zur
					// Anzeige durch eine Konstante bzw. durch
					// getAttRepository.getName, dann kann es leichter
					// geaendert
					// werden, bzw. ducrch eine dynamischen getOwnValue.
					if (name != null)
						entForList.place = name.getValue();
				}

			}
		}
		return entForList;
	}
	
	/**
	 * Restrict all list for the select menu, to the place selected
	 * TODO @Dirk this method seeks relations, which does not exist more, it implicates a bad performance.
	 * @param country
	 */
	protected void restrictByCountry(Entity country) {
		
		this.setCitiesInCurrentCountry(new ArrayList<SelectItem>());
		
		for (Entity src : getWrapper().getSourcesForTargetRelation(country, is_part_of, PLACE, MAX_PLACES)) {
			getCitiesInCurrentCountry().add(new SelectItem(String.valueOf(src
					.getId()), src.getOwnValue()));
		}
	}
	
	/**
	 * Restrict all list for the select menu, to the place selected
	 * 
	 * @param place
	 */
	protected void restrictByCity(Entity place) {
		
		this.setRepositoriesInCurrentCity(new ArrayList<SelectItem>());
		
		for (Entity src : getWrapper().getSourcesForTargetRelation(place, is_in, "REPOSITORY",MAX_REPOSITORIES)) {
			this.getRepositoriesInCurrentCity().add(new SelectItem(String.valueOf(src
					.getId()), src.getOwnValue()));
		}
	}
	
	/**
	 * Restrict all list for the select menu, to the place selected
	 * 
	 * @param repository
	 */
	public void restrictByRepository(Entity repository) {
	
		this.setCollectionsInCurrentRepository(new ArrayList<SelectItem>());
		for (Entity src : getWrapper().getSourcesForTargetRelation(repository, is_part_of, COLLECTION, MAX_COLLECTIONS)) {
			this.getCollectionsInCurrentRepository().add(new SelectItem(String
					.valueOf(src.getId()), src.getOwnValue()));
		}
	}
	
	protected void restrictByCollection(Entity collection) {
		this.setShelfMarksInCurrentCollection(restrictShelfMarks(collection));
	}
	
	protected List<SelectItem> restrictShelfMarks(Entity collection) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		int countCodex = 0;
		for (Entity src : getWrapper().getSourcesForTargetRelation(collection, is_part_of, CODEX, MAX_CODICES)) {
			countCodex++;
			items.add(new SelectItem(String.valueOf(src.getId()), src
					.getOwnValue()
					+ "(" + src.getObjectClass() + ")"));
		}
		Collections.sort(items, new SelectItemSort());
		this.thereExistMoreCodices = (countCodex == MAX_CODICES) ? true : false;
		return items;
	}
	
	/*
	 * 	########################
	 *  Country -> Codex
	 *  ######################## 
	 */
	
	public void checkConsistencyFromCountryToCodex(){
		boolean consistent = false;
		
		Entity country = this.getCountryLo().entity;
		if(country != null && country.isPersistent() && StringUtils.isNotEmpty(getCountryLo().getAttribute().getValue())){
			
			Attribute att = getWrapper().getAttributeByName(country.getId(), "name");
			
			if(att != null && getCountryLo().getAttribute().getValue().equals(att.getValue())){
				this.getCountryLo().statusImage.setStatus(StatusImage.STATUS_OK);
				consistent = true;
			}else{
				this.getCountryLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			}
		}else{
			this.getCountryLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(consistent){
			this.checkConsistencyFromCityToCodex(country);
		}else{
			this.labelStatesAsFalseOrUnset(true, true, true, true, false);
		}
	}
	
	private void checkConsistencyFromCityToCodex(Entity country){
		boolean consistent = false;
		
		Entity city = this.getCityLo().entity;
		if(city != null && city.isPersistent() && StringUtils.isNotEmpty(getCityLo().getAttribute().getValue())){
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(city, is_part_of, PLACE, 1);
			
			if(list.size() > 0 && list.get(0).getId().longValue() == country.getId().longValue()){
				this.getCityLo().statusImage.setStatus(StatusImage.STATUS_OK);
				consistent = true;
			}else{
				this.getCityLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			}
		}else{
			this.getCityLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(consistent){
			this.checkConsistencyFromRepositoryToCodex(city);
		}else{
			this.labelStatesAsFalseOrUnset(true, true, true, false, false);
		}
	}
	
	private void checkConsistencyFromRepositoryToCodex(Entity city){
		boolean consistent = false;
		
		Entity repository = this.getRepositoryLo().entity;
		if(repository != null && repository.isPersistent() && StringUtils.isNotEmpty(getRepositoryLo().getAttribute().getValue())){
			//juc List<Entity> list = getSearchServ().getTargetsForSourceRelation(repository, is_in, PLACE, 1);
			List<Entity> list = getWrapper().getTargetsForSourceRelation(repository, is_in, PLACE, 1);
			
			if(list.size() > 0 && list.get(0).getId().longValue() == city.getId().longValue()){
				this.getRepositoryLo().statusImage.setStatus(StatusImage.STATUS_OK);
				consistent = true;
			}else{
				this.getRepositoryLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			}
		}else{
			this.getRepositoryLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(consistent){
			this.checkConsistencyFromCollectionToCodex(repository);
		}else{
			this.labelStatesAsFalseOrUnset(true, true, false, false, false);
		}
	}
	
	private void checkConsistencyFromCollectionToCodex(Entity repository){
		boolean consistent = false;
		
		Entity collection = this.getCollectionLo().entity;
		if(collection != null && collection.isPersistent() && StringUtils.isNotEmpty(getCollectionLo().getAttribute().getValue())){
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(collection, is_part_of, REPOSITORY, 1);
			
			if(list.size() > 0 && list.get(0).getId().longValue() == repository.getId().longValue()){
				this.getCollectionLo().statusImage.setStatus(StatusImage.STATUS_OK);
				consistent = true;
			}else{
				this.getCollectionLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			}
		}else{
			this.getCollectionLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(consistent){
			this.checkConsistencyOfCodex(collection);
		}else{
			this.labelStatesAsFalseOrUnset(true, false, false, false, false);
		}
	}
	
	private void checkConsistencyOfCodex(Entity collection){
		
		Entity codex = this.getCodexLo().entity;
		if(codex != null && codex.isPersistent() && StringUtils.isNotEmpty(getCodexLo().getAttribute().getValue())){
			
			Attribute identifier = getWrapper().getAttributeByName(codex.getId(), "identifier");
			
			List<Entity> list = getWrapper().getTargetsForSourceRelation(codex, is_part_of, COLLECTION, 1);
			
			if(list.size() > 0 && list.get(0).getId().longValue() == collection.getId().longValue() &&
					identifier != null && this.getCodexLo().getAttribute().getValue().equals(identifier.getValue())){
				this.getCodexLo().setStatus(StatusImage.STATUS_OK);
			}else{
				this.getCodexLo().setStatus(StatusImage.STATUS_FALSE);
			}
		}else{
			this.getCodexLo().setStatus(StatusImage.STATUS_UNSET);
		}
	}
	
	/**
	 * If a previous method detects that some elements (a subset of {CODEX,...,COUNTRY}) 
	 * of the cycle can not be 'OK' (see: StatusImage states), 
	 * this method will label the mentioned elements as either 'FALSE' or 'UNSET'
	 * 
	 * @param bCodex true if you are sure that it can not be 'ok'.
	 * @param bCollection true if you are sure that it can not be 'ok'.
	 * @param bRepository true if you are sure that it can not be 'ok'.
	 * @param bCity true if you are sure that it can not be 'ok'.
	 * @param bCountry true if you are sure that it can not be 'ok'.
	 */
	public void labelStatesAsFalseOrUnset(boolean bCodex, boolean bCollection, boolean bRepository, boolean bCity, boolean bCountry){
		
		if(bCodex){
			Entity codex = this.getCodexLo().entity;
			if(codex != null && codex.isPersistent() && StringUtils.isNotEmpty(getCodexLo().getAttribute().getValue()))
				this.getCodexLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			else
				this.getCodexLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(bCollection){
			Entity collection = this.getCollectionLo().entity;
			if(collection != null && collection.isPersistent() && StringUtils.isNotEmpty(this.getCollectionLo().getAttribute().getValue()))
				this.getCollectionLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			else
				this.getCollectionLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(bRepository){
			Entity repository = this.getRepositoryLo().entity;
			if(repository != null && repository.isPersistent() && StringUtils.isNotEmpty(this.getRepositoryLo().getAttribute().getValue()))
				this.getRepositoryLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			else
				this.getRepositoryLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(bCity){
			Entity city = this.getCityLo().entity;
			if(city != null && city.isPersistent() && StringUtils.isNotEmpty(this.getCityLo().getAttribute().getValue()))
				this.getCityLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			else
				this.getCityLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
		if(bCountry){
			Entity country = this.getCountryLo().entity;
			if(country != null && country.isPersistent() && StringUtils.isNotEmpty(this.getCountryLo().getAttribute().getValue()))
				this.getCountryLo().statusImage.setStatus(StatusImage.STATUS_FALSE);
			else
				this.getCountryLo().statusImage.setStatus(StatusImage.STATUS_UNSET);
		}
		
	}
	
	public ListenerObject getCountryLo() {
		return countryLo;
	}
	public void setCountryLo(ListenerObject countryLo) {
		this.countryLo = countryLo;
	}
	public ListenerObject getCityLo() {
		return cityLo;
	}
	public void setCityLo(ListenerObject cityLo) {
		this.cityLo = cityLo;
	}
	public ListenerObject getRepositoryLo() {
		return repositoryLo;
	}
	public void setRepositoryLo(ListenerObject repositoryLo) {
		this.repositoryLo = repositoryLo;
	}
	public ListenerObject getCollectionLo() {
		return collectionLo;
	}
	public void setCollectionLo(ListenerObject collectionLo) {
		this.collectionLo = collectionLo;
	}
	public ListenerObject getCodexLo() {
		return codexLo;
	}
	public void setCodexLo(ListenerObject codexLo) {
		this.codexLo = codexLo;
	}
	
	public void setCitiesInCurrentCountry(List<SelectItem> citiesInCurrentCountry) {
		this.citiesInCurrentCountry = citiesInCurrentCountry;
	}

	public void setRepositoriesInCurrentCity(
			List<SelectItem> repositoriesInCurrentCity) {
		this.repositoriesInCurrentCity = repositoriesInCurrentCity;
	}
	
	public void setCollectionsInCurrentRepository(
			List<SelectItem> collectionsInCurrentRepository) {
		this.collectionsInCurrentRepository = collectionsInCurrentRepository;
	}
	
	public void setShelfMarksInCurrentCollection(
			List<SelectItem> shelfMarksInCurrentCollection) {
		this.shelfMarksInCurrentCollection = shelfMarksInCurrentCollection;
	}
	
	public boolean isThereExistMoreCodices() {
		return thereExistMoreCodices;
	}

	public void setThereExistMoreCodices(boolean thereExistMoreCodices) {
		this.thereExistMoreCodices = thereExistMoreCodices;
	}

	public List<SelectItem> getCitiesInCurrentCountry() {
		if(this.citiesInCurrentCountry != null && 
				this.citiesInCurrentCountry.size() > 0 &&
				(this.citiesInCurrentCountry.get(0).getLabel() == null ||
				!this.citiesInCurrentCountry.get(0).getLabel().equals("-- choose --"))){
			SelectItem chooser = new SelectItem("", "-- choose --");
			this.citiesInCurrentCountry.add(0, chooser);
		}
		return citiesInCurrentCountry;
	}
	
	public List<SelectItem> getRepositoriesInCurrentCity() {
		if(this.repositoriesInCurrentCity != null && 
				this.repositoriesInCurrentCity.size() > 0 &&
				(this.repositoriesInCurrentCity.get(0).getLabel() == null || 
				!this.repositoriesInCurrentCity.get(0).getLabel().equals("-- choose --"))){
			SelectItem chooser = new SelectItem("", "-- choose --");
			this.repositoriesInCurrentCity.add(0, chooser);
		}
		return repositoriesInCurrentCity;
	}
	
	public List<SelectItem> getCollectionsInCurrentRepository() {
		if(this.collectionsInCurrentRepository != null && 
				this.collectionsInCurrentRepository.size() > 0 &&
				(this.collectionsInCurrentRepository.get(0).getLabel() == null ||
				!this.collectionsInCurrentRepository.get(0).getLabel().equals("-- choose --"))){
			SelectItem chooser = new SelectItem("", "-- choose --");
			this.collectionsInCurrentRepository.add(0, chooser);
		}
		return collectionsInCurrentRepository;
	}
	
	public List<SelectItem> getShelfMarksInCurrentCollection() {
		if(shelfMarksInCurrentCollection != null &&
				this.shelfMarksInCurrentCollection.size() > 0 &&
				(this.shelfMarksInCurrentCollection.get(0).getLabel() == null ||
				!this.shelfMarksInCurrentCollection.get(0).getLabel().equals("-- choose --"))){
			SelectItem chooser = new SelectItem("", "-- choose --");
			this.shelfMarksInCurrentCollection.add(0, chooser);
		}
		return shelfMarksInCurrentCollection;
	}
	public class CodexForList {

		public String ident;
		public String repository;
		public String collection;
		public String place;
		public String city;
		public Long id;

		public String getIdent() {
			return ident;
		}

		public void setIdent(String ident) {
			this.ident = ident;
		}

		public String getRepository() {
			return repository;
		}

		public void setRepository(String repository) {
			this.repository = repository;
		}

		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String toString() {
			String str = "";
			if (ident != null && !ident.equals(""))
				str += ident;

			if (collection != null && !collection.equals(""))
				str += "_" + collection;
			if (repository != null && !repository.equals(""))
				str += "_" + repository;
			if (city != null && !city.equals(""))
				str += city;

			return str;

		}
	}
	
	
	protected boolean isRepositoryConsistentBeforeSave(){
		this.checkConsistencyFromCountryToCodex();
		
		if(!getCountryLo().getStatus().equals(StatusImage.STATUS_OK)){
			this.addErrorMsg("The country is marked as: " + getCountryLo().getStatus() + ". The entity could not be saved.");
			return false;
		}
		
		if(!getCityLo().getStatus().equals(StatusImage.STATUS_OK)){
			this.addErrorMsg("The city is marked as: " + getCityLo().getStatus() + ". The entity could not be saved.");
			return false;
		}
		
		return true;
	}
	
	protected boolean isCollectionConsistentBeforeSave(){
		
		if(!isRepositoryConsistentBeforeSave()){
			return false;
		}
		
		if(!getRepositoryLo().getStatus().equals(StatusImage.STATUS_OK)){
			this.addErrorMsg("The repository is marked as: " + getRepositoryLo().getStatus() + ". The entity could not be saved.");
			return false;
		}
		
		return true;
	}
	
	protected boolean isCodexConsistentBeforeSave(){
		
		if(!isCollectionConsistentBeforeSave()){
			return false;
		}
		
		if(!getCollectionLo().getStatus().equals(StatusImage.STATUS_OK)){
			this.addErrorMsg("The collection is marked as: " + getCollectionLo().getStatus() + ". The entity could not be saved.");
			return false;
		}
		
		return true;
	}
	
	protected boolean isWitnessConsistentBeforeSave(){
		
		if(!isCodexConsistentBeforeSave()){
			return false;
		}
		
		if(!getCodexLo().getStatus().equals(StatusImage.STATUS_OK)){
			this.addErrorMsg("The codex is marked as: " + getCodexLo().getStatus() + ". The entity could not be saved.");
			return false;
		}
		
		return true;
	}
	
	
	public void editThisCountryAction(ActionEvent event){
		if(this.getCountryLo().entity != null && this.getCountryLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getCountryLo().entity);
		}
	}
	
	public void editThisCityAction(ActionEvent event){
		if(this.getCityLo().entity != null && this.getCityLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getCityLo().entity);
		}
	}
	
	public void editThisRepositoryAction(ActionEvent event){
		if(this.getRepositoryLo().entity != null && this.getRepositoryLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getRepositoryLo().entity);
		}
	}
	
	public void editThisCollectionAction(ActionEvent event){
		if(this.getCollectionLo().entity != null && this.getCollectionLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getCollectionLo().entity);
		}
	}
	
	public void editThisCodexAction(ActionEvent event){
		if(this.getCodexLo().entity != null && this.getCodexLo().entity.isPersistent()){
			getSessionBean().editEntity(this.getCodexLo().entity);
		}
	}
	
	public void identifierChangedListener(ValueChangeEvent event) {
		if(event.getNewValue() != null){
			inCurrentListener(getShelfMarksInCurrentCollection(), event);
		}else{
			getCodexLo().setEntity(null);
			getCodexLo().setAttribute(new Attribute("identifier", TEXT, ""));
		}
	}
}
