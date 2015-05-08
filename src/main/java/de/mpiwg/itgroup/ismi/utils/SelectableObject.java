package de.mpiwg.itgroup.ismi.utils;

import java.io.Serializable;

public class SelectableObject<N> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean selected;
	private N obj;
	private String label;
	
	public SelectableObject(N obj){
		this.obj = obj;
		this.selected = false;
	}
	
	public SelectableObject(N obj, String label){
		this.obj = obj;
		this.selected = false;
		this.label = label;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public N getObj() {
		return obj;
	}
	public void setObj(N obj) {
		this.obj = obj;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
