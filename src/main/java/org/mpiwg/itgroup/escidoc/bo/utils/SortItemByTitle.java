package org.mpiwg.itgroup.escidoc.bo.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.Publication;

public class SortItemByTitle implements Comparator<ESciDocItem>{

	public int compare(ESciDocItem e1, ESciDocItem e2) {
		Publication p1 = e1.getPublication();
		Publication p2 = e2.getPublication();
		
		if(p1 != null && p2 != null){
			
			String t1 = replace(p1.getNorTitle());
			String t2 = replace(p2.getNorTitle());
			
			if (StringUtils.isNotEmpty(t1) && StringUtils.isNotEmpty(t2)) {
				return t1.compareTo(t2);
			} else if (StringUtils.isNotEmpty(t1)) {
				return -1;
			} else if (StringUtils.isNotEmpty(t2)) {
				return 1;
			}	
		}
		System.out.println("equals titles");
		return 0;
	}
	
	public static String replace(String s){
		if(StringUtils.isNotEmpty(s)){
			s = s.replace("(", "");
			s = s.replace(")", "");
		}
		return s;
	}
	
}
