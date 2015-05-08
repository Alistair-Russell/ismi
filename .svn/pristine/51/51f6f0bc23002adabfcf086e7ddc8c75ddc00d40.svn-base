package de.mpiwg.itgroup.ismi.utils;

import java.util.Comparator;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

public class SelectItemSort implements Comparator<SelectItem>{

	public int compare(SelectItem o1, SelectItem o2) {
		
		if(StringUtils.isNotEmpty(o1.getLabel())
				&& StringUtils.isNotEmpty(o2.getLabel())){
			return o1.getLabel().compareTo(o2.getLabel());	
		}
		
		if(StringUtils.isNotEmpty(o1.getLabel())){
			return 1;
		}else{
			return -1;
		}
	}
}