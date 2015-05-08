package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.faces.model.SelectItem;

public class AbstractDate {

	protected static Map<Integer, String> gregorianMonths = new HashMap<Integer, String>(12);
	protected static Map<Integer, String> islamicMonths = new HashMap<Integer, String>(12);
	protected static Map<Integer, String> julianMonths = new HashMap<Integer, String>(12);
	
	static{

		gregorianMonths.put(1, "January (1)");
		gregorianMonths.put(2, "February (2)");
		gregorianMonths.put(3, "March (3)");
		gregorianMonths.put(4, "April (4)");
		gregorianMonths.put(5, "May (5)");
		gregorianMonths.put(6, "June (6)");
		gregorianMonths.put(7, "July (7)");
		gregorianMonths.put(8, "August (8)");
		gregorianMonths.put(9, "September (9)");
		gregorianMonths.put(10, "October (10)");
		gregorianMonths.put(11, "November (11)");
		gregorianMonths.put(12, "December (12)");
		
		islamicMonths.put(1, hex("Muḥarram") + " (1)");
		islamicMonths.put(2, hex("Ṣafar") + " (2)");
		islamicMonths.put(3, hex("Rabīʿ I") + " (3)");
		islamicMonths.put(4, hex("Rabīʿ II") + " (4)");
		islamicMonths.put(5, hex("Jumādỳ I") + " (5)");
		islamicMonths.put(6, hex("Jumādỳ II") + " (6)");
		islamicMonths.put(7, "Rajab (7)");
		islamicMonths.put(8, hex("Šaʿbān") + " (8)");
		islamicMonths.put(9, hex("Ramaḍān") + " (9)");
		islamicMonths.put(10, hex("Šawwāl") + " (10)");
		islamicMonths.put(11, hex("Ḏu al-Qaʿdaẗ") + " (11)");
		islamicMonths.put(12, hex("Ḏu al-Ḥijjaẗ") + " (12)");
		
		julianMonths.put(1, "Ianuarius (1)");
		julianMonths.put(2, "Februarius (2)");
		julianMonths.put(3, "Martius (3)");
		julianMonths.put(4, "Aprilis (4)");
		julianMonths.put(5, "Maius (5)");
		julianMonths.put(6, "Iunius (6)");
		julianMonths.put(7, "Quintilis (Iulius) (7)");
		julianMonths.put(8, "Sextilis (Augustus) (8)");
		julianMonths.put(9, "September (9)");
		julianMonths.put(10, "October (10)");
		julianMonths.put(11, "November (11)");
		julianMonths.put(12, "December (12)");
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
	
	protected boolean isDayInWeek(Integer day){
		if(day == null)
			return false;
		if(day >= 1 && day <= 7){
			return true;
		}
		return false;
	}
	
}
