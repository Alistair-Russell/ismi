package de.mpiwg.itgroup.ismi.auxObjects;

import java.util.List;

import javax.faces.model.SelectItem;

public class SelectItem0 extends SelectItem{

	private static final long serialVersionUID = -3590260852460547911L;
	
	private boolean selected = false;
	private static Short needVerification = 0;
	private static Short verificatedByCatalogue = 1;
	private static Short verificatedByWitness = 2;
	private Short verification = needVerification;
	private boolean isWitness = false;
	
	@Override
	public String toString(){
		return "SelectItem0 [" + selected + "] " + getLabel();
	}
	
	public SelectItem0(Object value, String label){
		super(value, label);
	}
	
	public SelectItem0(Object value, String label, boolean isWitness){
		super(value, label);
		this.isWitness = isWitness;
	}
	
	public void select(){
		this.selected = true;
	}
	
	public void deselect(){
		this.selected = false;
	}
	
	public void needVerification(){
		this.verification = needVerification;
	}
	
	public void verificatedByCatalogue(){
		this.verification = verificatedByCatalogue;
	}
	
	public void verificatedByWitness(){
		this.verification = verificatedByWitness;
		
	}
	
	public static List<SelectItem0> valueChange(List<SelectItem0> list, Long id){
		for(SelectItem0 item : list){
			try {
				Long itemId = (Long)item.getValue();
				if(itemId.equals(id)){
					item.select();
				}else{
					item.deselect();
				}
			} catch (Exception e) {
				e.printStackTrace();
				item.deselect();
			}
			
		}
		return list;
	}
	
	public String getStyle() {
		
		if(isWitness){
			if(selected){
				return "background-color: #B8B8B8 ;";
			}else{
				if(verification.equals(needVerification)){
					return "background-color: #FF9999;";
				}else if(verification.equals(verificatedByCatalogue)){
					return "background-color: #CCCCCC;";
				}else if(verification.equals(verificatedByWitness)){
					return null;
				}
			}
		}else{
			if(selected){
				return "background-color: #B8B8B8 ;";
			}
		}
		
		return null;
	}
	
	
}
