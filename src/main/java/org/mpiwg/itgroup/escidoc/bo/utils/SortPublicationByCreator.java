package org.mpiwg.itgroup.escidoc.bo.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.mpiwg.itgroup.escidoc.bo.Publication;

public class SortPublicationByCreator  implements Comparator<Publication> {

	
	public int compare(Publication e1, Publication e2) {
		String creators1 = e1.getCreatorsAsString();
		String creators2 = e2.getCreatorsAsString();
		
		if(StringUtils.isNotEmpty(creators1) && StringUtils.isNotEmpty(creators2)){
			return creators1.compareTo(creators2);
		}else if(StringUtils.isNotEmpty(creators1)){
			return -1;
		}else if(StringUtils.isNotEmpty(creators2)){
			return 1;
		}
		return 0;
	}

}