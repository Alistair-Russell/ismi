package de.mpiwg.itgroup.dm2e;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.services.ServiceRegistry;
import org.mpi.openmind.repository.utils.OMUtils;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;

import de.mpiwg.itgroup.dm2e.utils.EDM;
import de.mpiwg.itgroup.ismi.utils.templates.CodexTemplate;

public class DM2ECodexConverter {

	
	public static URI getProvidedCHO(CodexTemplate codexTempl,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{
	
		String identifier = (StringUtils.isNotEmpty(codexTempl.mpiwgId)) ? codexTempl.mpiwgId : codexTempl.id.toString();
		
		URI providedCHO = f.createURI(NameSpaces.edmProvidedCHO, identifier);
		
		//
		Literal title = f.createLiteral(codexTempl.ov, "en");
		g.add(f.createStatement(providedCHO, URIUtils.dcTitle, title));
		
		// uri edm providedcho
		g.add(f.createStatement(providedCHO, URIUtils.rdfType, f.createURI("http://www.europeana.eu/schemas/edm/ProvidedCHO")));
		
		//dc:type -> Specialisation of  edm:PhysicalThing
		g.add(f.createStatement(providedCHO, URIUtils.dcType, DM2E.Manuscript));
		
		g.add(f.createStatement(providedCHO, URIUtils.dm2eLevelOfHierarchy, f.createLiteral(1)));
		
		//edm:type -> Literal
		g.add(f.createStatement(providedCHO, URIUtils.edmType, f.createLiteral("TEXT")));
		
		String description = getDescription(codexTempl);
		g.add(f.createStatement(providedCHO, URIUtils.dcDescription, f.createLiteral(description)));
		
		g.add(f.createStatement(providedCHO, URIUtils.dcLanguage, f.createLiteral("ar")));
		
		//TODO: Places
		
		g.add(f.createStatement(providedCHO, URIUtils.dcIdentifier, f.createLiteral(identifier)));
		
		//dm2eCallNumber
		g.add(f.createStatement(providedCHO, URIUtils.dm2eCallNumber, f.createLiteral(codexTempl.identifier)));
		
		
		for(String subject : OMUtils.resolveQuery(codexTempl.id, "CODEX|target:is_part_of|WITNESS|source:is_exemplar_of|TEXT|source:has_subject|SUBJECT", wrapper, null)){
			URI uriConcept = f.createURI("http://data.dm2e.eu/data/concept/mpiwg/authority_ismi/"  +  DM2EUtils.encodeString(subject));
			g.add(f.createStatement(uriConcept, URIUtils.rdfType, URIUtils.skosConcept));
			g.add(f.createStatement(providedCHO, URIUtils.dcSubject, uriConcept));
			g.add(f.createStatement(uriConcept, URIUtils.skosPrefLabel, f.createLiteral(subject, "en")));
		}
		
		//AddAgents.execute(g, f, providedCHO, witnessTempl, wrapper);
		
		//dm2e:writtenAt
		for(String placeWrittenAt : OMUtils.resolveQuery(codexTempl.id, "CODEX|target:is_part_of|WITNESS|source:was_created_in|PLACE", wrapper, null)){
			URI uriPlace = f.createURI(NameSpaces.edmPlace + codexTempl.id + "/" + DM2EUtils.generateID() + "_" + DM2EUtils.encodeString(placeWrittenAt));
			g.add(f.createStatement(uriPlace, f.createURI("http://www.w3.org/2004/02/skos/core#prefLabel"), f.createLiteral(placeWrittenAt, "en")));     	
			g.add(f.createStatement(providedCHO, DM2E.writtenAt, uriPlace));
			g.add(f.createStatement(uriPlace, URIUtils.rdfType, URIUtils.edmPlace));
		}
		
		
		//pro:author
		for(String personId : OMUtils.resolveQuery(codexTempl.id, "CODEX|target:is_part_of|WITNESS|source:is_exemplar_of|TEXT|source:was_created_by|PERSON:id", wrapper, null)){
    		
			Entity person = wrapper.getEntityById(Long.parseLong(personId));
			
			URI uriPerson = f.createURI(
    				"http://data.dm2e.eu/data/agent/mpiwg/" + NameSpaces.mpiwgCollection + "/" + codexTempl.identifier + "/" + DM2EUtils.generateID() + "_" + personId);
    		g.add(f.createStatement(uriPerson, URIUtils.skosPrefLabel, f.createLiteral(person.getOwnValue())));
    		g.add(f.createStatement(uriPerson, URIUtils.rdfType, f.createURI("http://xmlns.com/foaf/0.1/Person")));
    		g.add(f.createStatement(providedCHO, URIUtils.proAuthor, uriPerson));
		}
		
		return providedCHO;
	}
	
	public static URI getAggregation(CodexTemplate codexTemp,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{
		
		URI aggregation = f.createURI(NameSpaces.oreAggregation, codexTemp.mpiwgId);
		
		//edm:provider
		URI provider = f.createURI("http://data.dm2e.eu/data/agent/mpiwg/" + NameSpaces.mpiwgCollection + "/DM2E");
		g.add(f.createStatement(aggregation, URIUtils.rdfType, f.createURI("http://www.openarchives.org/ore/terms/Aggregation")));
		g.add(f.createStatement(aggregation, URIUtils.edmProvider, provider));
		g.add(f.createStatement(provider, URIUtils.rdfType, f.createURI("http://xmlns.com/foaf/0.1/Organization")));
		g.add(f.createStatement(provider, URIUtils.skosPrefLabel, f.createLiteral("DM2E", "en")));
		g.add(f.createStatement(provider, URIUtils.skosAltLabel, f.createLiteral("Digitised Manuscripts to Europeana", "en")));
		
		//data provider
		URI dataProvider = f.createURI("http://data.dm2e.eu/data/agent/mpiwg/" + NameSpaces.mpiwgCollection + "/MPIWG");
		g.add(f.createStatement(aggregation, URIUtils.edmDataProvider, dataProvider));
		g.add(f.createStatement(dataProvider, URIUtils.skosPrefLabel, f.createLiteral("Max Planck Institute for the History of Science", "en")));
		g.add(f.createStatement(dataProvider, URIUtils.skosPrefLabel, f.createLiteral("Max-Planck-Institut für Wissenschaftsgeschichte", "de")));
		g.add(f.createStatement(dataProvider, URIUtils.rdfType, f.createURI("http://xmlns.com/foaf/0.1/Organization")));
		
		//edm:rights  
		g.add(f.createStatement(aggregation, URIUtils.edmRights, f.createURI(DM2EConverter.RIGHTS)));
		
		//dm2e:displayLevel mandatory
		g.add(f.createStatement(aggregation, URIUtils.dm2eDisplayLevel, f.createLiteral(true)));
		
		//dcterms:created
		Entity witnessEnt = wrapper.getEntityById(codexTemp.id);
		Date created = new Date(witnessEnt.getModificationTime());
		
		g.add(f.createStatement(aggregation, URIUtils.dctCreated, 
				f.createLiteral(DM2EConverter.timeFormat.format(created), 
				f.createURI("http://www.w3.org/2001/XMLSchema#dateTime"))));
		
		addIsShownAt(codexTemp, aggregation, wrapper, g, f);
		addLandingPageObject(codexTemp, aggregation, wrapper, g, f);
		
		return aggregation;
	}
	
	private static URI addLandingPageObject(CodexTemplate codexTemp, URI agg,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{
		
		String indexmetaDir = codexTemp.indexmetaFolder.replace("/mpiwg/online", "");
		

		URI ws =  f.createURI("http://digilib.mpiwg-berlin.mpg.de/digitallibrary/servlet/Scaler?dw=50&fn=" + indexmetaDir + "/pageimg");
		
		g.add(f.createStatement(ws, f.createURI("http://purl.org/dc/elements/1.1/format"), f.createLiteral("image/jpeg")));
		g.add(f.createStatement(ws, URIUtils.dcDescription, f.createLiteral("Landing page of codex: " + codexTemp.ov)));

		//edm:rights [any URL] mandatory
		g.add(f.createStatement(ws, EDM.rights, f.createLiteral(DM2EConverter.RIGHTS)));

		g.add(f.createStatement(agg, EDM.object, ws));
		// KT added type
		g.add(f.createStatement(ws, URIUtils.rdfType, EDM.WebResource));

		return ws;
	}
	
	private static URI addIsShownAt(CodexTemplate codexTemp, URI agg,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{

		URI ws = f.createURI(
				"http://echo.mpiwg-berlin.mpg.de/" + codexTemp.mpiwgId);

		g.add(f.createStatement(ws, f.createURI("http://purl.org/dc/elements/1.1/format"), f.createLiteral("text/html-named-content")));
		g.add(f.createStatement(ws, URIUtils.dcDescription, f.createLiteral("ECHO View of codex: " + codexTemp.ov)));

		//edm:rights [any URL] mandatory
		g.add(f.createStatement(ws, EDM.rights, f.createLiteral(DM2EConverter.RIGHTS)));

		g.add(f.createStatement(agg, EDM.isShownAt,ws));

		g.add(f.createStatement(ws, URIUtils.rdfType, EDM.WebResource));
		
		return ws;
	}
	
	
	private static String getDescription(CodexTemplate temp){
		//TODO it can be improved!
		return "Codex: " + temp.ov;
	}
	
	public static void main(String[] args){
		
		ServiceRegistry sr = new ServiceRegistry();
		
		 try {
			//List<String> rs = OMUtils.resolveQuery(new Long(4202), "CODEX|target:is_part_of|WITNESS|source:is_exemplar_of|TEXT|source:has_subject|SUBJECT", sr.getWrapper());
			 //List<String> rs = OMUtils.resolveQuery(new Long(159098), "CODEX|target:is_part_of|WITNESS|source:is_exemplar_of|TEXT|source:was_created_in|PLACE", sr.getWrapper());

			List<String> rs = OMUtils.resolveQuery(new Long(159098), "CODEX|target:is_part_of|WITNESS|source:is_exemplar_of|TEXT|source:was_created_by|PERSON:id", sr.getWrapper(), null);
			
			for(String s : rs){
				System.out.println(s);
			}
			System.out.println(rs.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 System.exit(0);
		
	}
}
