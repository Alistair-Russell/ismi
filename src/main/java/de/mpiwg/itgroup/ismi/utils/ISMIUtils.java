package de.mpiwg.itgroup.ismi.utils;

import java.util.List;

import de.mpiwg.itgroup.ismi.util.guiComponents.Reference;

public class ISMIUtils {

	public static boolean replaceRef(List<SelectableObject<Reference>> list, Reference old, Reference newRef){
		if(newRef != null){
			for(SelectableObject<Reference> so : list ){
				Reference ref = so.getObj();
				if(ref != null && old.equals(ref)){
					//replacing
					so.setObj(newRef);
					return true;
				}
			}	
		}
		//adding in any case
		list.add(new SelectableObject<Reference>(newRef));		
		return false;
	}
	
	public static boolean listContainsObj(List<SelectableObject<Reference>> list, Object obj){
		if(obj != null){
			for(SelectableObject<Reference> so : list ){
				Reference oInList = so.getObj();
				if(oInList != null && obj.equals(oInList)){
					return true;
				}
			}	
		}
		return false;
	}
	
	public static boolean removeObjFromList(List<SelectableObject<Reference>> list, Reference obj){
		
		if(obj != null){
			for(SelectableObject<Reference> so : list ){
				Reference oInList = so.getObj();
				if(oInList != null && obj.equals(oInList)){
					list.remove(so);
					return true;
				}
			}	
		}
		
		return false;
	}
	
	public static boolean add2List(List<SelectableObject<Reference>> list, Reference obj){
		if(!listContainsObj(list, obj)){
			list.add(new SelectableObject<Reference>(obj));
			return true;
		}
		return false;
	}	
}
