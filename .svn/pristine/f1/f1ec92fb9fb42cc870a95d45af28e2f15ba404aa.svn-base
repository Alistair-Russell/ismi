package org.mpiwg.itgroup.escidoc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;


public class SelectedESciDocItems implements Serializable{

	private static final long serialVersionUID = -756497439159524677L;
	private transient List<ESciDocItem> list = new ArrayList<ESciDocItem>();
	private Map<String, Boolean> map = new HashMap<String, Boolean>();

	public boolean addESciDocItem(ESciDocItem pub) {
		if (!map.containsKey(pub.getPublication().getObjid())) {
			this.list.add(pub);
			this.map.put(pub.getPublication().getObjid(), false);
			return true;
		}
		return false;
	}

	public void removeById(String id) {
		ESciDocItem pp = null;
		for (ESciDocItem pub : this.list) {
			if (id.equals(pub.getPublication().getObjid())) {
				pp = pub;
				break;
			}
		}
		this.list.remove(pp);
		this.map.remove(id);
	}

	public List<ESciDocItem> getList() {
		return list;
	}

	public void setList(List<ESciDocItem> list) {
		this.list = list;
	}

	public Map<String, Boolean> getMap() {
		return map;
	}

	public void setMap(Map<String, Boolean> map) {
		this.map = map;
	}

}
