package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.json.JSONException;
import org.json.JSONObject;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class Calendar extends AbstractCalendar implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String inputForm;
	private String calendarType;
	
	private Date fromGregorian = new Date();
	private Date untilGregorian = new Date();
	private String additionalInfo;
	
	private Integer currentYear;
	private Date currentFrom = new Date();
	private Date currentUntil = new Date();
	
	private String dateInText;
	private String state;
	
	public Calendar(){
		this.calendarType = TYPE_GREGORIAN;
		this.inputForm = INPUT_FORM_YEAR;
		this.state = STATE_NOT_CHECKED;
	}
	
	public Calendar(String jsonString){
		if(StringUtils.isNotEmpty(jsonString)){
			try {
				JSONObject json = new JSONObject(jsonString);
				this.state = json.getString(STATE);
				
				if(StringUtils.isNotEmpty(state)){
					if(state.equals(STATE_KNOWN)){
						
						this.additionalInfo = json.getString(ADD_INF);
						this.calendarType = json.getString(CALENDAR_TYPE);
						this.inputForm = json.getString(INPUT_FORM);
						
						if(inputForm.equals(INPUT_FORM_YEAR)){
							this.fromGregorian = new Date(json.getJSONObject(FROM));
							this.untilGregorian = new Date(json.getJSONObject(UNTIL));
							this.currentYear = json.getInt(YEAR);
						}else if(inputForm.equals(INPUT_FORM_RANGE)){
							this.fromGregorian = new Date(json.getJSONObject(FROM));
							this.untilGregorian = new Date(json.getJSONObject(UNTIL));
							if(calendarType.equals(TYPE_GREGORIAN)){
								this.currentFrom = new Date(json.getJSONObject(FROM));
								this.currentUntil  = new Date(json.getJSONObject(UNTIL));
							}else if(calendarType.equals(TYPE_ISLAMIC)){
								this.currentFrom = new Date(this.fromGregorian.getIslamicDateTime());
								this.currentUntil = new Date(this.untilGregorian.getIslamicDateTime());
							}else if(calendarType.equals(TYPE_JULIAN)){
								this.currentFrom = new Date(this.fromGregorian.getJulianDateTime());
								this.currentUntil = new Date(this.untilGregorian.getJulianDateTime());
							}
						}else if(inputForm.equals(INPUT_FORM_DATE)){
							this.fromGregorian = new Date(json.getJSONObject(DATE));
							if(calendarType.equals(TYPE_GREGORIAN)){
								this.currentFrom = new Date(json.getJSONObject(DATE));
							}else if(calendarType.equals(TYPE_ISLAMIC)){
								this.currentFrom = new Date(this.fromGregorian.getIslamicDateTime());
							}else if(calendarType.equals(TYPE_JULIAN)){
								this.currentFrom = new Date(this.fromGregorian.getJulianDateTime());
							}
						}	
						
						if(json.has("dayOfWeekFrom")){
							this.currentFrom.setDayOfWeek(json.getInt("dayOfWeekFrom"));
						}
						if(json.has("dayOfWeekUntil")){
							this.currentUntil.setDayOfWeek(json.getInt("dayOfWeekUntil"));
						}
						if(json.has("dayOfWeek")){
							this.currentFrom.setDayOfWeek(json.getInt("dayOfWeek"));
						}
						
					}else if(state.equals(STATE_NOT_CHECKED) || state.equals(STATE_UNKNOWN)){
						if(json.has(DATE_IN_TEXT)){
							this.dateInText = json.getString(DATE_IN_TEXT);	
						}
						this.calendarType = TYPE_GREGORIAN;
						this.inputForm = INPUT_FORM_YEAR;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				this.state = STATE_NOT_CHECKED;
				this.calendarType = TYPE_GREGORIAN;
				this.inputForm = INPUT_FORM_YEAR;
			}	
		}else{
			this.calendarType = TYPE_GREGORIAN;
			this.inputForm = INPUT_FORM_YEAR;
			this.state = STATE_NOT_CHECKED;
		}
	}
	
	public void update(){
		
		if(inputForm.equals(INPUT_FORM_RANGE)){
			if(isUntilOlderThanFrom()){
				if(calendarType.equals(TYPE_GREGORIAN)){
					this.inputGregorianRange();
				}else if(calendarType.equals(TYPE_ISLAMIC)){
					this.inputIslamicRange();
				}else if(calendarType.equals(TYPE_JULIAN)){
					this.inputJulianRange();
				}	
			}else{
				addGeneralMsg("The until date must be older than the from date.");
			}
			
		}else if(inputForm.equals(INPUT_FORM_YEAR)){
			if(calendarType.equals(TYPE_GREGORIAN)){
				this.inputGregorianYear();
			}else if(calendarType.equals(TYPE_ISLAMIC)){
				this.inputIslamicYear();
			}else if(calendarType.equals(TYPE_JULIAN)){
				this.inputJulianYear();
			}
		}else if(inputForm.equals(INPUT_FORM_DATE)){
			if(calendarType.equals(TYPE_GREGORIAN)){
				this.inputGregorianFrom();
			}else if(calendarType.equals(TYPE_ISLAMIC)){
				this.inputIslamicFrom();
			}else if(calendarType.equals(TYPE_JULIAN)){
				this.inputJulianFrom();
			}
		}
	}
	
	public boolean isDeployable(){
		return (!this.state.equals(STATE_NOT_CHECKED) && !this.state.equals(STATE_UNKNOWN));
	}
	
	public String getFormattedRange(){
		if(inputForm != null){
			String from = this.fromGregorian.toString();
			if(this.inputForm.equals(INPUT_FORM_DATE)){
				return "[" + from + "]";
			}else{
				
				String until = this.untilGregorian.toString();
				if(StringUtils.isNotEmpty(from) && StringUtils.isNotEmpty(until)){
					return "[FROM: " + from + " TO: " + until + "]";	
				}	
			}		
		}
		return null;
	}
	
	public String getFormattedIslamicRange(){
		if(inputForm != null){
			String from = this.fromGregorian.toIslamicString();
			if(this.inputForm.equals(INPUT_FORM_DATE)){
				return "[" + from + "]";
			}else{
				String until = this.untilGregorian.toIslamicString();
				if(StringUtils.isNotEmpty(from) && StringUtils.isNotEmpty(until)){
					return "[FROM: " + from + " TO: " + until + "]";	
				}	
			}	
		}	
		return null;
	}
	
	public String getFormattedJulianRange(){
		if(inputForm != null){
			String from = this.fromGregorian.toJulianString();
			if(this.inputForm.equals(INPUT_FORM_DATE)){
				return "[" + from + "]";
			}else{
				String until = this.untilGregorian.toJulianString();
				if(StringUtils.isNotEmpty(from) && StringUtils.isNotEmpty(until)){
					return "[FROM: " + from + " TO: " + until + "]";	
				}	
			}	
		}
		return null;
	}
	
	public void listenerUpdate(ActionEvent e){
		this.update();
	}
	
	
	public String getCalendarAsHtml(){
		StringBuilder sb = new StringBuilder();
		
		if(isDeployable()){
			sb.append("<table align=\"left\">");
			
			sb.append("<tr>");
			sb.append("<th align=\"left\">Gregorian:</th>");
			sb.append("<th align=\"left\">" + this.getFormattedRange() + "</th>");
			sb.append("</tr>");
			
			sb.append("<tr>");
			sb.append("<th align=\"left\">Islamic:</th>");
			sb.append("<th align=\"left\">" + this.getFormattedIslamicRange() + "</th>");
			sb.append("</tr>");
			
			sb.append("<tr>");
			sb.append("<th align=\"left\">Julian:</th>");
			sb.append("<th align=\"left\">" + this.getFormattedJulianRange() + "</th>");
			sb.append("</tr>");
			
			sb.append("</table>");	
		}else if(this.state.equals(STATE_UNKNOWN)){
			sb.append("Unkknown");
		}else{
			sb.append("Not checked");
			if(StringUtils.isNotEmpty(this.dateInText)){
				sb.append("=" + this.dateInText);
			}
		}
		
		return sb.toString();
	}
	
	
	private void inputGregorianRange(){
		this.inputGregorianFrom();
		try{
			DateTime gregrorian = 
				new DateTime(currentUntil.getYear(), currentUntil.getMonth(), currentUntil.getDayOfMonth(), 0, 0, 0, 0, GregorianChronology.getInstance());
			this.untilGregorian = new Date(gregrorian.withChronology(GregorianChronology.getInstance()));	
			if(isDayInWeek(currentUntil.getDayOfWeek())){
				this.untilGregorian.setDayOfWeek(currentUntil.getDayOfWeek());
			}
		}catch (Exception e) {
			this.addGeneralMsg("In until date - " + e.getMessage());
		}
	}
	
	private void inputGregorianFrom(){
		try{
			DateTime gregorian = 
				new DateTime(currentFrom.getYear(), currentFrom.getMonth(), currentFrom.getDayOfMonth(), 0, 0, 0, 0, GregorianChronology.getInstance());
			this.fromGregorian = new Date(gregorian);
			if(isDayInWeek(currentFrom.getDayOfWeek())){
				this.fromGregorian.setDayOfWeek(currentFrom.getDayOfWeek());
			}
		}catch (Exception e) {
			this.addGeneralMsg("In from date - " + e.getMessage());
		}
	}
	
	private void inputIslamicRange(){
		this.inputIslamicFrom();
		try{
			DateTime islamic = 
				new DateTime(currentUntil.getYear(), currentUntil.getMonth(), currentUntil.getDayOfMonth(), 0, 0, 0, 0, IslamicChronology.getInstance());
			DateTime gregorian = islamic.withChronology(GregorianChronology.getInstance());
			this.untilGregorian = new Date(gregorian);
			if(isDayInWeek(currentUntil.getDayOfWeek())){
				this.untilGregorian.setDayOfWeek(gregorian.getDayOfWeek());
			}else{
				this.untilGregorian.setAmbiguity(2);
			}
		}catch (Exception e) {
			this.addGeneralMsg("In until date - " + e.getMessage());
		}	
	}
	
	private void inputIslamicFrom(){
		try{
			DateTime islamic = 
				new DateTime(currentFrom.getYear(), currentFrom.getMonth(), currentFrom.getDayOfMonth(), 0, 0, 0, 0, IslamicChronology.getInstance());
			DateTime gregorian = islamic.withChronology(GregorianChronology.getInstance());
			this.fromGregorian = new Date(gregorian);
			if(isDayInWeek(currentFrom.getDayOfWeek())){
				this.fromGregorian.setDayOfWeek(gregorian.getDayOfWeek());
			}else{
				this.fromGregorian.setAmbiguity(2);
			}
		}catch (Exception e) {
			this.addGeneralMsg("In from date - " + e.getMessage());
		}
	}
	
	
	private void inputJulianRange(){
		this.inputJulianFrom();
		DateTime julian = null;	
		try{
			julian = 
				new DateTime(currentUntil.getYear(), currentUntil.getMonth(), currentUntil.getDayOfMonth(), 0, 0, 0, 0, JulianChronology.getInstance());
			DateTime gregorian = julian.withChronology(GregorianChronology.getInstance());
			this.untilGregorian = new Date(gregorian);	
			if(isDayInWeek(currentUntil.getDayOfWeek())){
				this.untilGregorian.setDayOfWeek(gregorian.getDayOfWeek());
			}
		}catch (Exception e) {
			this.addGeneralMsg("In until date - " + e.getMessage());
		}	
	}
	
	private void inputJulianFrom(){
		DateTime julian = null;
		try{
			julian =
				new DateTime(currentFrom.getYear(), currentFrom.getMonth(), currentFrom.getDayOfMonth(), 0, 0, 0, 0, JulianChronology.getInstance());
			DateTime gregorian = julian.withChronology(GregorianChronology.getInstance());
			this.fromGregorian = new Date(gregorian);
			if(isDayInWeek(currentFrom.getDayOfWeek())){
				this.fromGregorian.setDayOfWeek(gregorian.getDayOfWeek());
			}
		}catch (Exception e) {
			addGeneralMsg("In from date - " + e.getMessage());
		}
	}
	
	public void inputGregorianYear(){
		if(inputForm.equals(INPUT_FORM_YEAR) && currentYear != null){
			this.fromGregorian = new Date(new DateTime(currentYear, 1, 1, 0, 0, 0, 0, GregorianChronology.getInstance()));
			this.untilGregorian = new Date(new DateTime(currentYear, 12, 31, 0, 0, 0, 0, GregorianChronology.getInstance()));
		}
	}
	
	public void inputIslamicYear(){
		if(this.currentYear < 1 ||  this.currentYear > 292271022){
			this.addGeneralMsg("year must be in the range [1,292271022]");
		}else {
			DateTime islamic =
				new DateTime(currentYear, 1, 1, 0, 0, 0, 0, IslamicChronology.getInstance());
			DateTime gregorian = islamic.withChronology(GregorianChronology.getInstance());
			this.fromGregorian = new Date(gregorian);
			this.fromGregorian.setAmbiguity(2);
			this.fromGregorian.setDayOfWeek(null);
			
			islamic =
				new DateTime(currentYear, 12, 29, 0, 0, 0, 0, IslamicChronology.getInstance());
			gregorian = islamic.withChronology(GregorianChronology.getInstance());
			this.untilGregorian = new Date(gregorian);
			this.untilGregorian.setAmbiguity(2);
			this.untilGregorian.setDayOfWeek(null);
		}
	}
	
	public void inputJulianYear(){
		if(this.currentYear < 1){
			this.addGeneralMsg("Value 0 for year is not supported");
		}else{
			DateTime julian =
				new DateTime(currentYear, 1, 1, 0, 0, 0, 0, JulianChronology.getInstance());
			DateTime gregorian = julian.withChronology(GregorianChronology.getInstance());
			this.fromGregorian = new Date(gregorian);
			this.fromGregorian.setDayOfWeek(null);
			
			julian =
				new DateTime(currentYear, 12, 31, 0, 0, 0, 0, JulianChronology.getInstance());
			gregorian = julian.withChronology(GregorianChronology.getInstance());
			this.untilGregorian = new Date(gregorian);
			this.untilGregorian.setDayOfWeek(null);
		}
	}
	
	
	public String toJSONString(){
		this.update();
		JSONObject json = this.toJSON();
		if(json != null){
			return json.toString();
		}
		return "";
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		if(state.equals(STATE_KNOWN)){
			try {
				
				if(inputForm.equals(INPUT_FORM_YEAR)){
					json.put(YEAR, this.currentYear);
					json.put(FROM, this.fromGregorian.toJSON());
					json.put(UNTIL, this.untilGregorian.toJSON());
				}else if(inputForm.equals(INPUT_FORM_RANGE)){
					json.put(FROM, this.fromGregorian.toJSON());
					json.put(UNTIL, this.untilGregorian.toJSON());
					if(isDayInWeek(currentFrom.getDayOfWeek())){
						json.put("dayOfWeekFrom", currentFrom.getDayOfWeek());
					}
					if(isDayInWeek(currentUntil.getDayOfWeek())){
						json.put("dayOfWeekUntil", currentUntil.getDayOfWeek());
					}
				}else if(inputForm.equals(INPUT_FORM_DATE)){
					json.put(DATE, this.fromGregorian.toJSON());
					if(isDayInWeek(currentFrom.getDayOfWeek())){
						json.put("dayOfWeek", currentFrom.getDayOfWeek());
					}
				} 
				
				json.put(STATE, this.state);
				json.put(ADD_INF, additionalInfo);
				json.put(CALENDAR_TYPE, this.calendarType);
				json.put(INPUT_FORM, this.inputForm);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			return json;
		}else if(state.equals(STATE_UNKNOWN)){
			try {
				json.put(STATE, this.state);
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			return json;
		}else if(state.equals(STATE_NOT_CHECKED) && StringUtils.isNotEmpty(dateInText)){
			try {
				json.put(STATE, this.state);
				json.put(DATE_IN_TEXT, this.dateInText);
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			return json;
		}
		return null;
	}
	
	public boolean isUntilOlderThanFrom(){
		if(this.currentFrom.getYear() != null && this.currentUntil.getYear() != null){
			if(this.currentFrom.getYear() < this.currentUntil.getYear()){
				return true;
			}else if(this.currentFrom.getYear().equals(this.currentUntil.getYear())){
				if(this.currentFrom.getMonth() != null && this.currentUntil.getMonth() != null){
					if(this.currentFrom.getMonth() < this.currentUntil.getMonth()){
						return true;
					}else if(this.currentFrom.getMonth().equals(this.currentUntil.getMonth())){
						if(this.currentFrom.getDayOfMonth() != null && this.currentUntil.getDayOfMonth() != null){
							if(this.currentFrom.getDayOfMonth() < this.currentUntil.getDayOfMonth()){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean isDayInWeek(Integer day){
		if(day == null)
			return false;
		if(day >= 1 && day <= 7){
			return true;
		}
		return false;
	}
	
	public Date getFromGregorian() {
		return fromGregorian;
	}
	public void setFromGregorian(Date fromGregorian) {
		this.fromGregorian = fromGregorian;
	}
	public Date getUntilGregorian() {
		return untilGregorian;
	}
	public void setUntilGregorian(Date untilGregorian) {
		this.untilGregorian = untilGregorian;
	}

	public String getInputForm() {
		return inputForm;
	}

	public void setInputForm(String inputForm) {
		this.inputForm = inputForm;
	}

	public String getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Integer getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(Integer currentYear) {
		this.currentYear = currentYear;
	}

	public Date getCurrentFrom() {
		return currentFrom;
	}

	public void setCurrentFrom(Date currentFrom) {
		this.currentFrom = currentFrom;
	}

	public Date getCurrentUntil() {
		return currentUntil;
	}

	public void setCurrentUntil(Date currentUntil) {
		this.currentUntil = currentUntil;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getDateInText() {
		return dateInText;
	}

	public void setDateInText(String dateInText) {
		this.dateInText = dateInText;
	}
}
