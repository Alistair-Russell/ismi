package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
//rich import com.icesoft.faces.context.effects.Effect;
//rich import com.icesoft.faces.context.effects.Highlight;
import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class IslamicCalendar extends AbstractISMIBean{

	private static List<SelectItem> suggestedDaysOfMonth = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedDaysOfWeek = new ArrayList<SelectItem>();
	private static List<SelectItem> suggestedMonths = new ArrayList<SelectItem>();

	private Integer islamicDayOfMonth;
	private Integer islamicDayOfWeek;
	private Integer islamicMonthOfYear;
	private Integer islamicYear;
	
	private String islamicStringDate;
	private String internalIslamicStringDate;
	
	private String dateType;
	public String internalDateType;
	
	public boolean islamicDateMalFormed;

	static{
		
		suggestedDaysOfWeek = new ArrayList<SelectItem>();
		suggestedDaysOfWeek.add(new SelectItem(new Integer(0), "-- choose --"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(1), "Yawm al-Ithnayn - Monday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(2), "Yawm ath-Thalaathaa' - Tuesday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(3), "Yawm al-Arba'aa' - Wednesday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(4), "Yawm al-Khamis - Thursday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(5), "Yawm al-Jumu'ah - Friday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(6), "Yawm as-Sabt - Saturday"));
		suggestedDaysOfWeek.add(new SelectItem(new Integer(7), "Yawm al-Ahad - Sunday"));
		
		
		suggestedDaysOfMonth = new ArrayList<SelectItem>();
		suggestedDaysOfMonth.add(new SelectItem(new Integer(0), "-- choose --"));
		for(int i=1; i<= 31; i++){
			suggestedDaysOfMonth.add(new SelectItem(new Integer(i), "" + i));
		}
		
		suggestedMonths = new ArrayList<SelectItem>();
		suggestedMonths.add(new SelectItem(new Integer(0), "-- choose --"));
		suggestedMonths.add(new SelectItem(new Integer(1), "1 - Muharram"));
		suggestedMonths.add(new SelectItem(new Integer(2), "2 - Safar"));
		suggestedMonths.add(new SelectItem(new Integer(3), "3 - Rabi' al-Awwal"));
		suggestedMonths.add(new SelectItem(new Integer(4), "4 - Rabi' al-Thani"));
		suggestedMonths.add(new SelectItem(new Integer(5), "5 - Jumada al-Ula"));
		suggestedMonths.add(new SelectItem(new Integer(6), "6 - Jumada al-Thani"));
		suggestedMonths.add(new SelectItem(new Integer(7), "7 - Rajab"));
		suggestedMonths.add(new SelectItem(new Integer(8), "8 - Sha'ban"));
		suggestedMonths.add(new SelectItem(new Integer(9), "9 - Ramadan"));
		suggestedMonths.add(new SelectItem(new Integer(10), "10 - Shawwal"));
		suggestedMonths.add(new SelectItem(new Integer(11), "11 - Dhu al-Qa'da"));
		suggestedMonths.add(new SelectItem(new Integer(12), "12 - Dhu al-Hijja"));
	}
	 
	public IslamicCalendar(){
		this.islamicDayOfWeek = 0;
		this.islamicDayOfMonth = 0;
		this.islamicMonthOfYear = 0;
		this.islamicYear = 1;
	}
	
	public void listenerChangeDateString(ValueChangeEvent event){
		String newValue = (String)event.getNewValue();
		this.internalIslamicStringDate = newValue;
	}
	
	public void listenerChangeDateType(ValueChangeEvent event){
		String newValue = (String)event.getNewValue();
		
		if(ApplicationBean.FORMATED_DATE.equals(newValue)){
			if(this.islamicDateMalFormed = this.setIslamicDate(this.islamicStringDate)){
				this.addGeneralMsg("The date in text format can not be converted.");
				this.internalDateType = ApplicationBean.PLAIN_DATE;
			}else{
				this.internalDateType = ApplicationBean.FORMATED_DATE;
			}
		}else if(ApplicationBean.PLAIN_DATE.equals(newValue)){
			this.internalDateType = ApplicationBean.PLAIN_DATE;
			this.internalIslamicStringDate = getIslamicDateForFormattedWidget();
		}
	}
	
	public void init(String s){
		//this.setIslamicDate(s);
		this.internalIslamicStringDate = s;
		this.islamicStringDate = s;
		this.islamicDateMalFormed = this.setIslamicDate(this.islamicStringDate);
		if(this.islamicDateMalFormed){
			this.internalDateType = ApplicationBean.PLAIN_DATE;
		}else{
			this.internalDateType = ApplicationBean.FORMATED_DATE;
		}
	}
	
	public boolean setIslamicDate(String date){
		try{
			this.islamicStringDate = date;
			this.islamicDayOfMonth = 0;
			this.islamicMonthOfYear = 0;
			this.islamicYear = 1;
			this.islamicDayOfWeek = 0;
			
			if(StringUtils.isNotEmpty(date)){
				String[] list = date.split("\\.");
				if(list.length == 1){
					this.islamicYear = new Integer(list[0]);
				}else if(list.length == 2){
					this.islamicMonthOfYear = new Integer(list[0]);
					this.islamicYear = new Integer(list[1]);
				}else if(list.length == 3){
					this.islamicDayOfMonth = new Integer(list[0]);
					this.islamicMonthOfYear = new Integer(list[1]);
					this.islamicYear = new Integer(list[2]);
				}else if(list.length == 4){
					this.islamicDayOfWeek = new Integer(list[0]);
					this.islamicDayOfMonth = new Integer(list[1]);
					this.islamicMonthOfYear = new Integer(list[2]);
					this.islamicYear = new Integer(list[3]);
				}				
			}
		}catch(Exception e){
			return true;
		}
		return false;
	}
	
	public static int getDayOfWeek(String date){
		if(StringUtils.isNotEmpty(date)){
			String[] list = date.split("\\.");
			if(list.length == 4){
				return new Integer(list[0]);
			}				
		}
		return 0;
	}
	
	public static int getDayOfMonth(String date){
		if(StringUtils.isNotEmpty(date)){
			String[] list = date.split("\\.");
			if(list.length == 3){
				return new Integer(list[0]);
			}				
		}
		return 0;
	}
	
	public static int getMonthOfYear(String date){
		if(StringUtils.isNotEmpty(date)){
			String[] list = date.split("\\.");
			if(list.length == 2){
				return new Integer(list[0]);
			}else if(list.length == 3){
				return new Integer(list[1]);
			}				
		}
		return 0;
	}
	
	public static int getYear(String date){
		if(StringUtils.isNotEmpty(date)){
			String[] list = date.split("\\.");
			if(list.length == 1){
				return new Integer(list[0]);
			}else if(list.length == 2){
				return  new Integer(list[1]);
			}else if(list.length == 3){
				return  new Integer(list[2]);
			}				
		}
		return 0;
	}
	
	public String getIslamicDate(){
		if(ApplicationBean.PLAIN_DATE.equals(this.getDateType())){
			return getIslamicStringDate();
		}
		return getIslamicDateForFormattedWidget();
	}
	
	public String getIslamicDateForFormattedWidget(){
		return this.islamicDayOfWeek + "." + this.islamicDayOfMonth.toString() + "." + this.islamicMonthOfYear + "." + this.islamicYear;
	}
	
	public String islamic2Gregorian(){	
 		return islamic2Gregorian(getIslamicYear(), getIslamicMonthOfYear(), getIslamicDayOfMonth(), getIslamicDayOfWeek());
	}
	
	public String islamic2Julian(){	
		return islamic2Julian(getIslamicYear(), getIslamicMonthOfYear(), getIslamicDayOfMonth(), getIslamicDayOfWeek());
	}
	
	public static String islamic2Gregorian(String date){
		return islamic2Gregorian(getYear(date), getMonthOfYear(date), getDayOfMonth(date), getDayOfWeek(date));		
	}
	
	public static String islamic2Julian(String date){
		return islamic2Julian(getYear(date), getMonthOfYear(date), getDayOfMonth(date), getDayOfWeek(date));		
	}
	
	public static String islamic2Julian(int year, int monthOfYear, int dayOfMonth, int dayOfWeek){
		int validMonthOfYear = (monthOfYear < 1) ? 1 : monthOfYear;
		int validDayOfMonth = (dayOfMonth < 1) ? 1 : dayOfMonth;
		Integer validDayOfWeek = (dayOfWeek >= 1 && dayOfWeek <= 7) ? dayOfWeek : null;
		
		DateTime dtIslamic = 
			new DateTime(year, validMonthOfYear, validDayOfMonth, 0, 0, 0, 0, IslamicChronology.getInstance());
		if(validDayOfWeek != null){
			dtIslamic = dtIslamic.withDayOfWeek(validDayOfWeek);
		}
		
		DateTime dtJulian = dtIslamic.withChronology(JulianChronology.getInstance());
		return  dtJulian.getDayOfWeek() + "." + dtJulian.getDayOfMonth() + "." + dtJulian.getMonthOfYear() + "." + dtJulian.getYear();
	}
	
	public static String islamic2Gregorian(int year, int monthOfYear, int dayOfMonth, int dayOfWeek){
		System.out.println(dayOfWeek + "." + dayOfMonth + "." + monthOfYear + "." + year);
		int validMonthOfYear = (monthOfYear < 1) ? 1 : monthOfYear;
		int validDayOfMonth = (dayOfMonth < 1) ? 1 : dayOfMonth;
		Integer validDayOfWeek = (dayOfWeek >= 1 && dayOfWeek <= 7) ? dayOfWeek : null;
		
		DateTime dtIslamic = 
			new DateTime(year, validMonthOfYear, validDayOfMonth, 0, 0, 0, 0, IslamicChronology.getInstance());
		if(validDayOfWeek != null){
			dtIslamic = dtIslamic.withDayOfWeek(validDayOfWeek);
		}
		
		DateTime dtGregorian = dtIslamic.withChronology(GregorianChronology.getInstance());
		
		return  dtGregorian.getDayOfWeek() + "." + dtGregorian.getDayOfMonth() + "." + dtGregorian.getMonthOfYear() + "." + dtGregorian.getYear();	
	}
	
	public List<SelectItem> getSuggestedDaysOfMonth(){
		return suggestedDaysOfMonth;
	}
	
	public List<SelectItem> getSuggestedMonths(){
		return suggestedMonths;
	}
	public Integer getIslamicDayOfMonth() {
		return islamicDayOfMonth;
	}

	public void setIslamicDayOfMonth(Integer islamicDayOfMonth) {
		this.islamicDayOfMonth = islamicDayOfMonth;
	}

	public Integer getIslamicMonthOfYear() {
		return islamicMonthOfYear;
	}

	public void setIslamicMonthOfYear(Integer islamicMonthOfYear) {
		this.islamicMonthOfYear = islamicMonthOfYear;
	}

	public Integer getIslamicYear() {
		return islamicYear;
	}

	public void setIslamicYear(Integer islamicYear) {
		this.islamicYear = islamicYear;
	}
	
	
	public List<SelectItem> getSuggestedDaysOfWeek() {
		return suggestedDaysOfWeek;
	}

	public Integer getIslamicDayOfWeek() {
		return islamicDayOfWeek;
	}

	public void setIslamicDayOfWeek(Integer islamicDayOfWeek) {
		this.islamicDayOfWeek = islamicDayOfWeek;
	}

	
	public boolean isIslamicDateMalformed() {
		return this.islamicDateMalFormed;
	}

	public String getIslamicStringDate() {
		//return islamicStringDate;
		return internalIslamicStringDate;
	}

	public void setIslamicStringDate(String islamicStringDate) {
		this.islamicStringDate = islamicStringDate;
	}
	public String getDateType() {
		return internalDateType;
		//return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
}
