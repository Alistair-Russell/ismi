package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.json.JSONException;
import org.json.JSONObject;

public class Date extends AbstractDate implements Serializable{
	private static final long serialVersionUID = -6470414082851873885L;

	public static String AMBIGUITY = "2";
	
	private Integer dayOfMonth;
	private Integer dayOfWeek;
	private Integer dayOfYear;
	private Integer month;
	private Integer year;
	private Integer century;
	private Integer ambiguity;
	
	public Date(){}

	public Date(DateTime dateTime){
		this.setTime(dateTime);
		this.ambiguity = 0;
	}
	
	/*
	public Date(Integer year, Integer month, Integer dayOfMonth){
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}*/
	
	public Date(JSONObject json) {
		try{
			dayOfMonth = json.getInt("dayOfMonth");
			month = json.getInt("month");
			year = json.getInt("year");
			//century = json.getInt("century");
			if(json.has("century")){
				century = json.getInt("century");	
			}
			if(json.has("dayOfWeek")){
				dayOfWeek = json.getInt("dayOfWeek");
			}
			if(json.has("dayOfYear")){
				dayOfYear = json.getInt("dayOfYear");
			}
			if(json.has("ambiguity")){
				ambiguity = json.getInt("ambiguity");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTime(DateTime dateTime){
		//TODO is gregorian chronology
		this.dayOfMonth = dateTime.getDayOfMonth();
		this.dayOfYear = dateTime.getDayOfYear();
		//this.dayOfWeek = dateTime.getDayOfWeek();
		this.month = dateTime.getMonthOfYear();
		this.year = dateTime.getYear();
		this.century = dateTime.getCenturyOfEra();
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("dayOfMonth", dayOfMonth);
			json.put("dayOfYear", dayOfYear);
			json.put("month", month);
			json.put("year", year);
			json.put("century", century);
			json.put("dayOfWeek", dayOfWeek);
			json.put("ambiguity", ambiguity);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String toString(){
		if(year == null || month == null || dayOfMonth == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(this.dayOfMonth + ".");
		sb.append(Date.gregorianMonths.get(this.month) + ".");
		sb.append(this.year);
		if(this.ambiguity != null && this.ambiguity > 0){
			sb.append(" [+-" + this.ambiguity + "]");
		}
		return sb.toString();
	}
	
	public String toIslamicString(){
		try{
			if(year == null || month == null || dayOfMonth == null)
				return null;
				
			DateTime gr =
				new DateTime(this.year, this.month, this.dayOfMonth, 0, 0, 0, 0, GregorianChronology.getInstance());
			
			DateTime islamic = new DateTime(gr.withChronology(IslamicChronology.getInstance()));
			
			StringBuilder sb = new StringBuilder();
			sb.append(islamic.getDayOfMonth() + ".");
			sb.append(Date.islamicMonths.get(islamic.getMonthOfYear()) + ".");
			sb.append(islamic.getYear());
			if(!isDayInWeek(this.dayOfWeek) && !hasAmbiguity()){
				sb.append(" [+-" + AMBIGUITY + "]");
			}
			return sb.toString();	
		}catch (Exception e) {}
		return "no valid";
	}
	
	public boolean hasAmbiguity(){
		if(this.getAmbiguity() == null)
			return false;
		if(this.getAmbiguity() > 0)
			return true;
		return false;
	}
	
	public String toJulianString(){
		try{
			if(year == null || month == null || dayOfMonth == null)
				return null;
			
			DateTime gr =
				new DateTime(this.year, this.month, this.dayOfMonth, 0, 0, 0, 0, GregorianChronology.getInstance());
			
			DateTime julian = new DateTime(gr.withChronology(JulianChronology.getInstance()));
			
			StringBuilder sb = new StringBuilder();
			sb.append(julian.getDayOfMonth() + ".");
			sb.append(Date.julianMonths.get(julian.getMonthOfYear()) + ".");
			sb.append(julian.getYear());
			if(this.ambiguity != null && this.ambiguity > 0){
				sb.append(" [+-" + this.ambiguity + "]");
			}
			return sb.toString();	
		}catch (Exception e) {}
		return "no valid";
	}
	
	public DateTime getIslamicDateTime(){
		if(year == null || month == null || dayOfMonth == null)
			return null;
		try{
			DateTime gr =
				new DateTime(this.year, this.month, this.dayOfMonth, 0, 0, 0, 0, GregorianChronology.getInstance());
			
			return new DateTime(gr.withChronology(IslamicChronology.getInstance()));
		}catch (Exception e) {}
		return null;
	}
	
	public DateTime getJulianDateTime(){
		if(year == null || month == null || dayOfMonth == null)
			return null;
		try{
			DateTime gr =
				new DateTime(this.year, this.month, this.dayOfMonth, 0, 0, 0, 0, GregorianChronology.getInstance());
			
			return new DateTime(gr.withChronology(JulianChronology.getInstance()));
		}catch (Exception e) {}
		return null;
	}
	
	public Integer getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public Integer getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public Integer getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(Integer dayOfYear) {
		this.dayOfYear = dayOfYear;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getCentury() {
		return century;
	}
	public void setCentury(Integer century) {
		this.century = century;
	}
	public Integer getAmbiguity() {
		return ambiguity;
	}
	public void setAmbiguity(Integer ambiguity) {
		this.ambiguity = ambiguity;
	}
}
