package de.mpiwg.itgroup.dm2e;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;

import de.mpiwg.itgroup.dm2e.utils.DM2EUtils;
import de.mpiwg.itgroup.dm2e.utils.EDM;
import de.mpiwg.itgroup.ismi.util.guiComponents.Calendar;
import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;

public class DM2EWitnessConverter {


	public static URI getProvidedCHO(WitnessTemplate witnessTempl,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{
		
		System.out.println("\tgetProvidedCHO witness: " + witnessTempl.id);
		
		URI providedCHO = f.createURI(NameSpaces.edmProvidedCHO, witnessTempl.id.toString());
		
		// uri edm providedcho
		g.add(f.createStatement(providedCHO, URIUtils.rdfType, f.createURI("http://www.europeana.eu/schemas/edm/ProvidedCHO")));
		
		//dc:type -> Specialisation of  edm:PhysicalThing
		g.add(f.createStatement(providedCHO, URIUtils.dcType, DM2E.Manuscript));
		
		g.add(f.createStatement(providedCHO, URIUtils.dm2eLevelOfHierarchy, f.createLiteral(1)));
		
		//edm:type -> Literal
		g.add(f.createStatement(providedCHO, URIUtils.edmType, f.createLiteral("TEXT")));
		
		String description = getDescription(witnessTempl);
		g.add(f.createStatement(providedCHO, URIUtils.dcDescription, f.createLiteral(description)));
		
		//Literal label = (StringUtils.isNotEmpty(title.lang)) ?  f.createLiteral(title.label, title.lang) : f.createLiteral(title.label);
		//TODO language of the title??
		g.add(f.createStatement(providedCHO, URIUtils.dcTitle, f.createLiteral(witnessTempl.title)));
		
		//TODO alternative title, arabic translitareation??
		//g.add(f.createStatement(providedCHO, URIUtils.dctAlternative, label));	
		
		
		if(StringUtils.isNotEmpty(witnessTempl.incipit))
			g.add(f.createStatement(providedCHO, URIUtils.dm2eIncipit, f.createLiteral(witnessTempl.incipit)));
		
		if(StringUtils.isNotEmpty(witnessTempl.explicit))
			g.add(f.createStatement(providedCHO, URIUtils.dm2eExplicit, f.createLiteral(witnessTempl.explicit)));
		
		g.add(f.createStatement(providedCHO, URIUtils.dcLanguage, f.createLiteral("ar")));
		
		//TODO: Places
		
		g.add(f.createStatement(providedCHO, URIUtils.dcIdentifier, f.createLiteral(witnessTempl.id.toString())));
		
		//dm2eCallNumber
		if(StringUtils.isNotEmpty(witnessTempl.codex))
			g.add(f.createStatement(providedCHO, URIUtils.dm2eCallNumber, f.createLiteral(witnessTempl.codex)));
		
		//g.add(f.createStatement(providedCHO, URIUtils.biboNumPages, f.createLiteral(r.meta.bib.numberOfPages)));
		
		//g.add(f.createStatement(providedCHO, URIUtils.biboNumVolumes, f.createLiteral(r.meta.bib.numberOfVolumes)));
		
		//g.add(f.createStatement(providedCHO, URIUtils.dcPublisher, f.createLiteral(publisher)));
		
		
		
		//<http://data.dm2e.eu/data/concept/mpiwg/test/520> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2004/02/skos/core#Concept> .
		//<http://data.dm2e.eu/data/concept/mpiwg/test/520> <http://www.w3.org/2002/07/owl#sameAs> <http://dewey.info/class/520/about> .
		//<http://data.dm2e.eu/data/item/mpiwg/test/MPIWG_WBGMR64C> <http://purl.org/dc/elements/1.1/subject>  <http://data.dm2e.eu/data/concept/mpiwg/test/520> 
		
		
		if(StringUtils.isNotEmpty(witnessTempl.subject)){
			URI uriConcept = f.createURI("http://data.dm2e.eu/data/concept/mpiwg/authority_ismi/"  + witnessTempl.subject);
			g.add(f.createStatement(uriConcept, URIUtils.rdfType, URIUtils.skosConcept));
			g.add(f.createStatement(providedCHO, URIUtils.dcSubject, uriConcept));
			g.add(f.createStatement(uriConcept, URIUtils.skosPrefLabel, f.createLiteral(witnessTempl.subject, "en")));	
		}
		
		
		AddAgents.execute(g, f, providedCHO, witnessTempl, wrapper);
		
		//dm2e:writtenAt
		
		List<Entity> list = wrapper.getTargetsForSourceRelation(witnessTempl.titleId, "was_created_in", "PLACE", 1);
		if(list.size() > 0){
			Entity place = list.get(0);
			URI uriPlace = f.createURI(NameSpaces.edmPlace + witnessTempl.id + "/" + DM2EUtils.generateID() + "_" + DM2EUtils.encodeString(place.getOwnValue()));
			g.add(f.createStatement(uriPlace, f.createURI("http://www.w3.org/2004/02/skos/core#prefLabel"), f.createLiteral(place.getOwnValue(), "en")));     	
			g.add(f.createStatement(providedCHO, DM2E.publishedAt, uriPlace));
			g.add(f.createStatement(uriPlace, URIUtils.rdfType, URIUtils.edmPlace));
		}
		
		
		//TEXT.creation_date -> dcterms:issued
		
		Attribute attCreationDate = wrapper.getAttributeByName(witnessTempl.titleId	, "creation_date");
		if(attCreationDate != null && StringUtils.isNotEmpty(attCreationDate.getOwnValue())){
			Calendar creationDate = new Calendar(attCreationDate.getOwnValue());
			
			if(Calendar.STATE_KNOWN.equals(creationDate.getState())){
				
				//"2000-01-01T00:00:00"
				String begin = null;
				String end = null;
				URI timespanIssued = null;
				
				begin = creationDate.getFromGregorian().getYear() + "-" + 
						((creationDate.getFromGregorian().getMonth() < 10) ? "0" + creationDate.getFromGregorian().getMonth()  : creationDate.getFromGregorian().getMonth()) + "-" + 
						((creationDate.getFromGregorian().getDayOfMonth() < 10) ? "0" + creationDate.getFromGregorian().getDayOfMonth() : creationDate.getFromGregorian().getDayOfMonth()) + "T00:00:00";
				
				if(!Calendar.INPUT_FORM_DATE.equals(creationDate.getInputForm())){
					end = creationDate.getUntilGregorian().getYear() + "-" + 
							((creationDate.getUntilGregorian().getMonth() < 10) ? "0" + creationDate.getUntilGregorian().getMonth() : creationDate.getUntilGregorian().getMonth()) + "-" + 
							((creationDate.getUntilGregorian().getDayOfMonth() < 10) ? "0" + creationDate.getUntilGregorian().getDayOfMonth() : creationDate.getUntilGregorian().getDayOfMonth()) + "T23:59:59";
				

				}
				if(StringUtils.isEmpty(end)){
					timespanIssued = f.createURI(NameSpaces.edmTimespan  + begin.replace(":", "_") + "UG");
				}else{
					timespanIssued = f.createURI(NameSpaces.edmTimespan  + begin.replace(":", "_") + "UG_" + end.replace(":", "_") + "UG");
					g.add(f.createStatement(timespanIssued, URIUtils.edmEnd, f.createLiteral(end, f.createURI("http://www.w3.org/2001/XMLSchema#dateTime"))));
					g.add(f.createStatement(timespanIssued, URIUtils.cidocEndQual, f.createLiteral("uncertainty_granularity")));
				}
				
				g.add(f.createStatement(timespanIssued, URIUtils.rdfType, URIUtils.edmTimeSpan));
				//g.add(f.createStatement(timespanIssued, URIUtils.skosPrefLabel, f.createLiteral(dateString)));
				g.add(f.createStatement(timespanIssued, URIUtils.edmBegin, f.createLiteral(begin, f.createURI("http://www.w3.org/2001/XMLSchema#dateTime"))));
				g.add(f.createStatement(timespanIssued, URIUtils.cidocBeginQual, f.createLiteral("uncertainty_granularity")));
				g.add(f.createStatement(providedCHO, URIUtils.dctIssued, timespanIssued));
			}
		}
		
		return providedCHO;
	}
	
	public static URI getAggregation(WitnessTemplate witness,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{
		//URI aggregation = f.createURI(NameSpaces.oreAggregation, "MPIWG:" + r.indexMetaId);
		URI aggregation = f.createURI(NameSpaces.oreAggregation, witness.id.toString());
		
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
		Entity witnessEnt = wrapper.getEntityById(witness.id);
		Date created = new Date(witnessEnt.getModificationTime());
		
		g.add(f.createStatement(aggregation, URIUtils.dctCreated, 
				f.createLiteral(DM2EConverter.timeFormat.format(created), 
				f.createURI("http://www.w3.org/2001/XMLSchema#dateTime"))));
		
		addIsShownAt(witness, aggregation, wrapper, g, f);
		
		return aggregation;
	}
	
	private static URI addIsShownAt(WitnessTemplate witness, URI agg,
			WrapperService wrapper, Model g, ValueFactory f) throws Exception{

		URI ws = f.createURI(
				"https://openmind-ismi-dev.mpiwg-berlin.mpg.de/om4-ismi/search/displayTitle.xhtml?witnessId=" + witness.id + "#witnesses");

		g.add(f.createStatement(ws, f.createURI("http://purl.org/dc/elements/1.1/format"), f.createLiteral("text/html-named-content")));
		g.add(f.createStatement(ws, URIUtils.dcDescription, f.createLiteral("View of witness " + witness.title)));

		//edm:rights [any URL] mandatory
		g.add(f.createStatement(ws, EDM.rights, f.createLiteral(DM2EConverter.RIGHTS)));

		g.add(f.createStatement(agg, EDM.isShownAt,ws));

		g.add(f.createStatement(ws, URIUtils.rdfType, EDM.WebResource));
		
		return ws;
	}
	
	private static String getDescription(WitnessTemplate witnessTempl){
		//TODO it can be improved!
		return "Manuscript " + witnessTempl.title + " is part of the codex " + witnessTempl.codex + ".";
	}
}
