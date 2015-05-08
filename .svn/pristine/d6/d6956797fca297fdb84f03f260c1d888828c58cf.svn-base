package org.mpiwg.itgroup.geonames;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.utils.NormalizerUtils;
import org.mpiwg.itgroup.geonames.bo.Geoname;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;

public class GeonameForm extends AbstractBean{

	private static String GEONAME_ID = "geoname_id";
	
	
	private String searchPlaceTerm;
	private List<Entity> places;
	private Entity selectedPlace;
	
	private String searchGeoTerm;
	private List<Geoname> geonames;
	private Geoname selectedGeoname;
	private JSONObject selectedJSONGeoname;
	
	private boolean showGeonamePopup = false;
	
	public void listenerSelectPlace(ActionEvent event){
		Entity place = (Entity)getRequestBean("place");
		if(place != null){
			this.redirect(null, "?placeId=" + place.getId());
			loadPlace(place);
		}
	}
	
	public void loadPlace(Entity place){
		this.selectedPlace = place;
		if(place != null){
			this.searchGeoTerm = this.selectedPlace.getOwnValue();
			this.searchGeonames(selectedPlace.getOwnValue());	
		}
	}
	
	public void listenerLoadAllPlaces(ActionEvent event){
		this.loadPlaces(null);
	}
	
	public void listenerSearchPlaces(ActionEvent event){
		this.loadPlaces(this.searchPlaceTerm);
	}
	/*
	public void listenerOpenPopup(ActionEvent event){
		this.selectedPlace = (Entity)getRequestBean("place");
		this.searchGeonames(selectedPlace.getOwnValue());
		this.showGeonamePopup = true;
	}*/
	
	public void listenerCloseSearchPopup(ActionEvent event){
		this.closeSearchPopup();
	}
	
	public void listenerSearchGeonames(ActionEvent event){
		this.searchGeonames(this.searchGeoTerm);
	}
	
	public String actionDisplayGeonameMap(){
		this.selectedGeoname = (Geoname)getRequestBean("geoname");
		if(selectedGeoname != null){
			try {
				this.selectedJSONGeoname = GeonameUtils.getJSONGeoname(selectedGeoname.getId());
			} catch (Exception e) {
				addErrorMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		return "geo_map";
	}
	
	public void loadPlaces(String term){
		
		String nTerm = NormalizerUtils.normalize(term);
		
		System.out.println("Loading place in GeonameForm");
		List<Entity> list = getAppBean().getWrapper().getEntitiesByDef("PLACE");
		this.places = new ArrayList<Entity>();
		for(Entity pl : list){
			Attribute att = getAppBean().getWrapper().getAttributeByName(pl.getId(), GEONAME_ID);
			if(att == null || StringUtils.isEmpty(att.getValue())){
				if(StringUtils.isEmpty(nTerm)){
					this.places.add(pl);
				}else if(pl.getNormalizedOwnValue().contains(nTerm)){
					this.places.add(pl);
				}
			}
		}
	}
	
	private void searchGeonames(String term){
		
		try {
			this.searchGeoTerm = term;
			this.geonames = GeonameUtils.search(searchGeoTerm);
		} catch (Exception e) {
			addErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void listenerAssignGeoname2Place(ActionEvent event){
		
		this.selectedGeoname = (Geoname)getRequestBean("geoname");
		
		if(this.selectedGeoname != null){
			try {
				if(selectedPlace.isLightweight()){
					this.selectedPlace = getAppBean().getWrapper().getEntityContent(selectedPlace);
				}
				
				if(this.selectedPlace.containsAttribute(GEONAME_ID)){
					this.selectedPlace.getAttributeByName(GEONAME_ID).setValue(this.selectedGeoname.getId().toString());
				}else{
					this.selectedPlace.addAttribute(new Attribute(GEONAME_ID, "geoname-id", this.selectedGeoname.getId().toString()));
				}
				
				getAppBean().getWrapper().saveEntity(this.selectedPlace, getSessionBean().getUsername());
				
				addGeneralMsg("The geoname " + selectedGeoname.getId() + " has been assigned successfully to the place " + 
						selectedPlace.getOwnValue() + " [ID=" +selectedPlace.getId() + "]");
				
				this.loadPlaces(null);
				this.closeSearchPopup();
			} catch (Exception e) {
				addErrorMsg(e.getMessage());
				e.printStackTrace();
			}
		}else{
			System.err.println("AHHHHH");
		}
	}

	private void closeSearchPopup(){
		this.showGeonamePopup = false;
		this.searchGeoTerm = null;
		this.geonames = null;
		this.selectedPlace = null;
		this.redirect(null, "");
		
	}
	
	public String getSearchGeoTerm() {
		return searchGeoTerm;
	}

	public void setSearchGeoTerm(String searchGeoTerm) {
		this.searchGeoTerm = searchGeoTerm;
	}

	public List<Entity> getPlaces() {
		return places;
	}

	public void setPlaces(List<Entity> places) {
		this.places = places;
	}

	public Entity getSelectedPlace() {
		return selectedPlace;
	}

	public void setSelectedPlace(Entity selectedPlace) {
		this.selectedPlace = selectedPlace;
	}

	public int getGeonamesSize(){
		if(geonames == null)
			return 0;
		return geonames.size();
	}
	
	public List<Geoname> getGeonames() {
		return geonames;
	}

	public void setGeonames(List<Geoname> geonames) {
		this.geonames = geonames;
	}

	public Geoname getSelectedGeoname() {
		return selectedGeoname;
	}

	public void setSelectedGeoname(Geoname selectedGeoname) {
		this.selectedGeoname = selectedGeoname;
	}

	public boolean isShowGeonamePopup() {
		return showGeonamePopup;
	}

	public void setShowGeonamePopup(boolean showGeonamePopup) {
		this.showGeonamePopup = showGeonamePopup;
	}

	public JSONObject getSelectedJSONGeoname() {
		return selectedJSONGeoname;
	}

	public void setSelectedJSONGeoname(JSONObject selectedJSONGeoname) {
		this.selectedJSONGeoname = selectedJSONGeoname;
	}

	public String getSearchPlaceTerm() {
		return searchPlaceTerm;
	}

	public void setSearchPlaceTerm(String searchPlaceTerm) {
		this.searchPlaceTerm = searchPlaceTerm;
	}
	
}
