package org.mpiwg.itgroup.escidoc.bo.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.mpiwg.itgroup.escidoc.bo.Publication;

public class SortPublicationByTitle implements Comparator<Publication> {

	public int compare(Publication e1, Publication e2) {
		if (StringUtils.isNotEmpty(e1.getTitle()) && StringUtils.isNotEmpty(e2.getTitle())) {
			return e1.getTitle().compareTo(e2.getTitle());
		} else if (StringUtils.isNotEmpty(e1.getTitle())) {
			return -1;
		} else if (StringUtils.isNotEmpty(e2.getTitle())) {
			return 1;
		} else {
			return 0;
		}
	}

}
