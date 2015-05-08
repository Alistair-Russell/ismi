package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

public abstract class AbstractCalendar extends AbstractISMIBean{

	private static final long serialVersionUID = -6494784177169138202L;

	public static String FROM = "from";
	public static String UNTIL = "until";
	public static String YEAR = "year";
	public static String ADD_INF = "additional_info";
	public static String CALENDAR_TYPE = "calendar_type";
	public static String INPUT_FORM = "input_form";
	public static String STATE = "state";
	public static String DATE = "date";
	public static String DATE_IN_TEXT = "date_in_text";
	
	public static String INPUT_FORM_YEAR = "Year";
	public static String INPUT_FORM_DATE = "Date";
	public static String INPUT_FORM_FULL_DATE = "Full date";
	public static String INPUT_FORM_RANGE = "Range";
	public static String INPUT_FORM_FULL_RANGE = "Full range";
	
	public static String TYPE_GREGORIAN = "Gregorian";
	public static String TYPE_ISLAMIC = "Islamic";
	public static String TYPE_JULIAN = "Julian";
	
	public static String STATE_UNKNOWN = "unknown";
	public static String STATE_KNOWN = "known";
	public static String STATE_NOT_CHECKED = "not checked";
	
	public static String FLOUIT = "flouit";
	public static String DISPLAY_FLOUIT = "display_flouit";
	
	
	
	public String getSTATE_UNKNOWN(){
		return STATE_UNKNOWN;
	}

	public String getSTATE_KNOWN(){
		return STATE_KNOWN;
	}
	
	public String getSTATE_NOT_CHECKED(){
		return STATE_NOT_CHECKED;
	}
	
	public List<SelectItem> getStateList(){
		return stateList;
	}
	
	public static List<SelectItem> inputFormList = new ArrayList<SelectItem>();
	public static List<SelectItem> calendarTypeList = new ArrayList<SelectItem>();
	private static List<SelectItem> stateList = new ArrayList<SelectItem>();
	private static List<SelectItem> gregorianMonths = new ArrayList<SelectItem>();
	private static List<SelectItem> islamicMonths = new ArrayList<SelectItem>();
	private static List<SelectItem> julianMonths = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedDaysOfMonth = new ArrayList<SelectItem>();
	
	protected static List<SelectItem> suggestedDaysOfWeek = new ArrayList<SelectItem>(8);
	
	
	static{
		
		suggestedDaysOfWeek.add(new SelectItem(0, "unknown"));
		suggestedDaysOfWeek.add(new SelectItem(1, "Monday"));
		suggestedDaysOfWeek.add(new SelectItem(2, "Tuesday"));
		suggestedDaysOfWeek.add(new SelectItem(3, "Wednesday"));
		suggestedDaysOfWeek.add(new SelectItem(4, "Thursday"));
		suggestedDaysOfWeek.add(new SelectItem(5, "Friday"));
		suggestedDaysOfWeek.add(new SelectItem(6, "Saturday"));
		suggestedDaysOfWeek.add(new SelectItem(7, "Sunday"));
		
		stateList.add(new SelectItem(STATE_NOT_CHECKED));
		stateList.add(new SelectItem(STATE_KNOWN));
		stateList.add(new SelectItem(STATE_UNKNOWN));
		
		inputFormList.add(new SelectItem(INPUT_FORM_YEAR));
		inputFormList.add(new SelectItem(INPUT_FORM_DATE));
		inputFormList.add(new SelectItem(INPUT_FORM_RANGE));
		
		calendarTypeList.add(new SelectItem(TYPE_GREGORIAN));
		calendarTypeList.add(new SelectItem(TYPE_ISLAMIC));
		calendarTypeList.add(new SelectItem(TYPE_JULIAN));
		
		//gregorianMonths.add(new SelectItem(new Integer(0), "-- choose --"));
		gregorianMonths.add(new SelectItem(new Integer(1), "1 - January"));
		gregorianMonths.add(new SelectItem(new Integer(2), "2 - February"));
		gregorianMonths.add(new SelectItem(new Integer(3), "3 - March"));
		gregorianMonths.add(new SelectItem(new Integer(4), "4 - April"));
		gregorianMonths.add(new SelectItem(new Integer(5), "5 - May"));
		gregorianMonths.add(new SelectItem(new Integer(6), "6 - June"));
		gregorianMonths.add(new SelectItem(new Integer(7), "7 - July"));
		gregorianMonths.add(new SelectItem(new Integer(8), "8 - August"));
		gregorianMonths.add(new SelectItem(new Integer(9), "9 - September"));
		gregorianMonths.add(new SelectItem(new Integer(10), "10 - October"));
		gregorianMonths.add(new SelectItem(new Integer(11), "11 - November"));
		gregorianMonths.add(new SelectItem(new Integer(12), "12 - December"));
		
		//islamicMonths.add(new SelectItem(new Integer(0), "-- choose --"));
		
		islamicMonths.add(new SelectItem(new Integer(1), "1 - " + hex("Muḥarram")));
		islamicMonths.add(new SelectItem(new Integer(2), "2 - " + hex("Ṣafar")));
		islamicMonths.add(new SelectItem(new Integer(3), "3 - " + hex("Rabīʿ I")));
		islamicMonths.add(new SelectItem(new Integer(4), "4 - " + hex("Rabīʿ II")));
		islamicMonths.add(new SelectItem(new Integer(5), "5 - " + hex("Jumādỳ I")));
		islamicMonths.add(new SelectItem(new Integer(6), "6 - " + hex("Jumādỳ II")));		
		islamicMonths.add(new SelectItem(new Integer(7), "7 - Rajab"));
		islamicMonths.add(new SelectItem(new Integer(8), "8 - " + hex("Šaʿbān")));
		islamicMonths.add(new SelectItem(new Integer(9), "9 - " + hex("Ramaḍān")));
		islamicMonths.add(new SelectItem(new Integer(10), "10 - " + hex("Šawwāl")));
		islamicMonths.add(new SelectItem(new Integer(11), "11 - " + hex("Ḏu al-Qaʿdaẗ")));
		islamicMonths.add(new SelectItem(new Integer(12), "12 - " + hex("Ḏu al-Ḥijjaẗ")));
		
		//julianMonths.add(new SelectItem(new Integer(0), "-- choose --"));
		julianMonths.add(new SelectItem(new Integer(1), "1 - Ianuarius"));
		julianMonths.add(new SelectItem(new Integer(2), "2 - Februarius"));
		julianMonths.add(new SelectItem(new Integer(3), "3 - Martius"));
		julianMonths.add(new SelectItem(new Integer(4), "4 - Aprilis"));
		julianMonths.add(new SelectItem(new Integer(5), "5 - Maius"));
		julianMonths.add(new SelectItem(new Integer(6), "6 - Iunius"));
		julianMonths.add(new SelectItem(new Integer(7), "7 - Quintilis (Iulius)"));
		julianMonths.add(new SelectItem(new Integer(8), "8 - Sextilis (Augustus)"));
		julianMonths.add(new SelectItem(new Integer(9), "9 - September"));
		julianMonths.add(new SelectItem(new Integer(10), "10 - October"));
		julianMonths.add(new SelectItem(new Integer(11), "11 - November"));
		julianMonths.add(new SelectItem(new Integer(12), "12 - December"));
		
		//suggestedDaysOfMonth.add(new SelectItem(new Integer(0), "-- choose --"));
		for(int i=1; i<= 31; i++){
			suggestedDaysOfMonth.add(new SelectItem(new Integer(i), "" + i));
		}
	}
	
	public static String hex(String s){
		
		Character c = 0x1e25;
		s = s.replace("ḥ", c + "");
		c = 0x1e62;
		s = s.replace("Ṣ", c + "");
		c = 0x12b;
		s = s.replace("ī", c + "");
		c = 0x2bf;
		s = s.replace("ʿ", c + "");
		c = 0x101;
		s = s.replace("ā", c + "");
		c = 0x1ef3;
		s = s.replace("ỳ", c + "");
		c = 0x160;
		s = s.replace("Š", c + "");
		c = 0x1e0d;
		s = s.replace("ḍ", c + "");
		c = 0x1e0e;
		s = s.replace("Ḏ", c + "");
		c = 0x1e24;
		s = s.replace("Ḥ", c + "");
		c = 0x1e97;
		s = s.replace("ẗ", c + "");
		
		return s;
	}
	
	public List<SelectItem> getSuggestedDaysOfMonth(){
		return suggestedDaysOfMonth;
	}
	
	public List<SelectItem> getInputFormList(){
		return inputFormList;
	}
	
	public List<SelectItem> getCalendarTypeList(){
		return calendarTypeList;
	}
	
	public List<SelectItem> getGregorianMonths(){
		return gregorianMonths;
	}
	
	public List<SelectItem> getIslamicMonths(){
		return islamicMonths;
	}
	
	public List<SelectItem> getJulianMonths(){
		return julianMonths;
	}
	
	public List<SelectItem> getSuggestedDaysOfWeek(){
		return suggestedDaysOfWeek;
	}
	
	public String getINPUT_FORM_YEAR(){ return INPUT_FORM_YEAR;}
	public String getINPUT_FORM_DATE(){ return INPUT_FORM_DATE;}
	public String getINPUT_FORM_RANGE(){ return INPUT_FORM_RANGE;}
	public String getINPUT_FORM_FULL_RANGE(){ return INPUT_FORM_FULL_RANGE;}
	public String getTYPE_GREGORIAN(){ return TYPE_GREGORIAN;}
	public String getTYPE_ISLAMIC(){ return TYPE_ISLAMIC;}
	public String getTYPE_JULIAN(){ return TYPE_JULIAN;}
}
