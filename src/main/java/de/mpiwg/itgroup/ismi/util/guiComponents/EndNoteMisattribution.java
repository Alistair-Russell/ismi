package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.io.Serializable;
import java.util.List;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Node;
import org.mpi.openmind.repository.bo.Relation;

public class EndNoteMisattribution implements Serializable {
	private static final long serialVersionUID = -1840193000833171154L;

	public static String MISATT = "MISATTRIBUTION";
	public static String MISATTRIBUTED_TO = "misattributed_to";
	public static String IS_REFERENCE_OF = "is_reference_of";
	public static String HAS_AUTHOR_MISATT = "has_author_misattribution"; 
	
	private Entity misatt;
	private Entity person;
	private Reference ref;
	private WrapperService ot;
	private String userName;
	
	public EndNoteMisattribution(WrapperService ot, String userName){
		this.ot = ot;
		this.userName = userName;
	}
	
	public Entity saveAndGetMisattribution(Entity text) throws Exception{
		
		if(misatt == null){
			misatt = new Entity(Node.TYPE_ABOX, MISATT, false);
		}
		
		Entity entityRef = this.ref.getEnt();
		ot.saveAssertion(entityRef, userName);
		
		this.misatt.removeAllSourceRelations(MISATTRIBUTED_TO, "PERSON");
		Relation rel0 = new Relation(misatt, person, MISATTRIBUTED_TO);
		
		this.misatt.removeAllTargetRelations(IS_REFERENCE_OF, "REFERENCE");
		Relation rel1 = new Relation(entityRef, misatt, IS_REFERENCE_OF);
		
		this.misatt.removeAllTargetRelations(HAS_AUTHOR_MISATT, "TEXT");
		Relation rel2 = new Relation(text, misatt, HAS_AUTHOR_MISATT);
		
		
		ot.saveAssertion(misatt, userName);
		
		return text;
	}
	
	public static EndNoteMisattribution create(Entity person, WrapperService ot, String userName) {
		EndNoteMisattribution obj = new EndNoteMisattribution(ot, userName);
		
		obj.setPerson(person);
		obj.setRef(new Reference(null));
		
		return obj;
	}
	
	public static EndNoteMisattribution load(Entity misatt, WrapperService ot, String userName) throws Exception{
		
		EndNoteMisattribution obj = new EndNoteMisattribution(ot, userName); 
		
		if(misatt.isLightweight()){
			obj.setMisatt(ot.getEntityByIdWithContent(misatt.getId()));
		}
		//loading person. Person can be Light Weight
		List<Entity> tmpList = ot.getTargetsForSourceRelation(misatt, MISATTRIBUTED_TO, "PERSON", -1);
		if(tmpList.size() > 1){
			throw new Exception("Misattribution (entity) can not has more than one person associated. " + misatt.toString());
		}else if(tmpList.size() > 0){
			obj.setPerson(tmpList.get(0));
		}		
		
		tmpList = ot.getSourcesForTargetRelation(misatt, IS_REFERENCE_OF, "REFERENCE", -1);
		if(tmpList.size() > 0){
			Entity ref = tmpList.get(0);
			if(ref.isLightweight()){
				ref = ot.getEntityByIdWithContent(ref.getId());
			}
			obj.setRef(new Reference(ref));
		}		
		
		//this.person = person;
		//this.ref = new Reference(ref);
		return obj;
	}
	
	public Entity getPerson() {
		return person;
	}
	public void setPerson(Entity person) {
		this.person = person;
	}
	public Reference getRef() {
		return ref;
	}
	public void setRef(Reference ref) {
		this.ref = ref;
	}
	public Entity getMisatt() {
		return misatt;
	}
	public void setMisatt(Entity misatt) {
		this.misatt = misatt;
	}
	
}
