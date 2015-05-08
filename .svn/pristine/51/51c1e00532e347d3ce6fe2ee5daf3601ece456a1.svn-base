package de.mpiwg.itgroup.ismi.auxObjects;

//import org.mpi.openmind.repository.bo.Entity;

import org.mpi.openmind.repository.bo.Entity;


/**
 * @author dwinter
 * Hilfsklasse fuer die Ausgabe von Suchresultaten. In der Regel sollte der 
 * Eigenwert eines Objektes angezeigt, werden dieser ist jedoch manchmal leer. 
 * #TODO: Eine entsprechenden Methode sollte in Entity selbst implementiert werden.
 */
public class DisplayEntity  {

	private Entity ent;

	public DisplayEntity(Entity entity) {
		ent=entity;
	}
	
	public String getDisplayValue(){
		
		if (!ent.getOwnValue().equals(""))
			return ent.getOwnValue();

                /*[upgrade]
		if (ent.getObjectClass().equals("PERSON"))
			return ent.getAttribute("name_translit").getValue();
		
		if (ent.getObjectClass().equals("TEXT"))
			return ent.getAttribute("title_translit").getValue();
		*/
		return "";
	}
	public void setEnt(Entity ent) {
		this.ent = ent;
	}

	public Entity getEnt() {
		return ent;
	}
	
	
}
