package de.mpiwg.itgroup.dm2e;

import java.util.List;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;

import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;

public class AddAgents {

	
	public static void execute(Model g, ValueFactory f, URI providedCHO, WitnessTemplate witness, WrapperService wrapper) throws Exception {
		
		List<Entity> list = wrapper.getTargetsForSourceRelation(witness.titleId, "was_created_by", "PERSON", 1);
		if(list.size() > 0){
			Entity author = list.get(0);
			
			URI uriPerson = f.createURI("http://data.dm2e.eu/data/agent/mpiwg/" + NameSpaces.mpiwgCollection + "/" + author.getId());
			
			g.add(f.createStatement(uriPerson, URIUtils.skosPrefLabel, f.createLiteral(author.getOwnValue())));
    		g.add(f.createStatement(uriPerson, URIUtils.rdfType, f.createURI("http://xmlns.com/foaf/0.1/Person")));
    		g.add(f.createStatement(providedCHO, URIUtils.proAuthor, uriPerson));
			
		}
		
		
		//COPY_EVENT
		list = wrapper.getSourcesForTargetRelation(witness.id, "is_a_copy_of", "COPY_EVENT", -1);
		for(Entity copyEvent : list){
			List<Entity> list0 = wrapper.getTargetsForSourceRelation(copyEvent.getId(), "has_person_copying_text", "PERSON", 1);
			if(list0.size() > 0){
				
				Entity personCopyingText = list0.get(0);
			
				URI uriPerson = f.createURI("http://data.dm2e.eu/data/agent/mpiwg/" + NameSpaces.mpiwgCollection + "/" + personCopyingText.getId());
				
				g.add(f.createStatement(uriPerson, URIUtils.skosPrefLabel, f.createLiteral(personCopyingText.getOwnValue())));
	    		g.add(f.createStatement(uriPerson, URIUtils.rdfType, f.createURI("http://xmlns.com/foaf/0.1/Person")));
	    		g.add(f.createStatement(providedCHO, URIUtils.dm2eCopyist, uriPerson));
			}
		}
		
		
		
	}
}
