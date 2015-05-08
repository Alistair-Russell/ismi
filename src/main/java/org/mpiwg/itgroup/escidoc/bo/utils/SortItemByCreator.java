package org.mpiwg.itgroup.escidoc.bo.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.Publication;

public class SortItemByCreator   implements Comparator<ESciDocItem> {

	
	public int compare(ESciDocItem e1, ESciDocItem e2) {
		Publication p1 = e1.getPublication();
		Publication p2 = e2.getPublication();
		
		if(p1 != null && p2 != null){
			String c1 = replace(p1.getCreatorsAsString());
			String c2 = replace(p2.getCreatorsAsString());
			
			if(StringUtils.isNotEmpty(c1) && StringUtils.isNotEmpty(c2)){
				return c1.compareTo(c2);
			}else if(StringUtils.isNotEmpty(c1)){
				return -1;
			}else if(StringUtils.isNotEmpty(c2)){
				return 1;
			}	
		}
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
