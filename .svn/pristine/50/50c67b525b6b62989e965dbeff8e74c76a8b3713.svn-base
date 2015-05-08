package org.mpiwg.itgroup.geonames.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;

public class Geoname  implements Serializable{
	private static final long serialVersionUID = -3256085837090516807L;
	
	private Integer id;
	private List<String> alternateNames = new ArrayList<String>();
	private String countryName;
	private String toponymName;
	private String name;
	private String countryCode;
	private Double lng;
	private Double lat;
	private Integer population;
	
	private String fCls;
	private String fCode;
	private String clsDescription;
	private String clsName;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public List<String> getMoreInfo(){
		
		List<String> rs = new ArrayList<String>();
		rs.add("Lat/Lng: " + lat + "/" + lng);
		rs.add("Population: " + population);
		rs.add("Class name: " + clsName);
		/*
		rs.add("Class description: " + clsDescription);
		rs.add("Class name: " + clsName);
		rs.add("Class name: " + clsName);
		rs.add(fCls + ", " + fCode);
		*/
		/*
		if(StringUtils.isNotEmpty(countryName)){
			if(sb.length() > 0)
				sb.append(", ");
			sb.append("Country: " + countryName);
		}
		if(StringUtils.isNotEmpty(countryCode)){
			if(sb.length() > 0)
				sb.append(", ");
			sb.append("Country code: " + countryCode);
		}*/
		
		return rs;
	}
	
	public List<String> getSomeAlternateName(){
		List<String> rs = new ArrayList<String>();
		
		if(this.alternateNames.size() > 0){
			int counter = 0;
			Queue<String> queue = new LinkedList<String>(this.alternateNames);
			
			int namesPerLine = 3;
			
			while(!queue.isEmpty() && (namesPerLine * 5) > counter){
				
				if(counter % namesPerLine == 0){
					rs.add(queue.poll());
				}else{
					String name =  rs.remove(rs.size() - 1);
					name += ", " + queue.poll();
					rs.add(name);
				}
				counter++;
			}
		}
		
		return rs;
	}
	
	public String getAlternateNamesAsString(){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(String s : this.alternateNames){
			if(count > 0){
				sb.append(", ");
			}
			sb.append(s);
			count++;
		}
		return sb.toString();
	}
	
	public List<String> getAlternateNames() {
		return alternateNames;
	}
	
	public void setAlternateNames(List<String> alternateNames) {
		this.alternateNames = alternateNames;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getToponymName() {
		return toponymName;
	}
	public void setToponymName(String toponymName) {
		this.toponymName = toponymName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Integer getPopulation() {
		return population;
	}
	public void setPopulation(Integer population) {
		this.population = population;
	}
	public String getfCls() {
		return fCls;
	}
	public void setfCls(String fCls) {
		this.fCls = fCls;
	}
	
	public String getfCode() {
		return fCode;
	}
	public void setfCode(String fCode) {
		this.fCode = fCode;
	}
	public String getClsDescription() {
		return clsDescription;
	}
	public void setClsDescription(String clsDescription) {
		this.clsDescription = clsDescription;
	}
	public String getClsName() {
		return clsName;
	}
	public void setClsName(String clsName) {
		this.clsName = clsName;
	}
}
