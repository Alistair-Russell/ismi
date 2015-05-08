package de.mpiwg.itgroup.dm2e;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * http://onto.dm2e.eu/schemas/dm2e/1.0/
 * 
 * @author jurzua
 *
 */
public class DM2E {

	static ValueFactory f;
	
	//edm:PhysicalThing
	static public URI dm2eOnto10;
	static public URI Archive; //Superclass: foaf:Organization
	static public URI Collection; //Superclass: edm:NonInformationResource
	static public URI Document; //Subclass: dm2e:Manuscript
	static public URI File; //Superclasses: edm:PhysicalThing
	static public URI Manuscript; //Superclass: dm2e:Document
	static public URI Paragraph; //Superclass: skos:Concept
	static public URI Photo; //Superclasses: edm:PhysicalThing, foaf:Image
	static public URI Publication; //Superclass: skos:Concept
	static public URI Work; //Superclass: skos:Concept
	
	static public URI Page;
	static public URI Book;
	
	
	//Properties
	static public URI artist;
	static public URI callNumber;
	static public URI composer;
	static public URI condition;
	static public URI contributor;
	static public URI copyist;
	static public URI explicit;
	static public URI hasPartCHO;
	static public URI hasPartPlace;
	static public URI hasPartTimeSpan;
	static public URI hasPartWebResource;
	static public URI honoree;
	static public URI illustration;
	static public URI incipit;
	static public URI influencedBy;
	static public URI isDerivativeOfCHO;
	static public URI isDerivativeOfWebResource;
	static public URI isPartOfCHO;
	static public URI isPartOfPlace;
	static public URI isPartOfTimeSpan;
	static public URI isPartOfWebResource;
	static public URI levelOfGenesis;
	static public URI mentioned;
	static public URI misattributed;
	static public URI owner;
	static public URI pageDimension;
	static public URI painter;
	static public URI patron;
	static public URI portrayed;
	static public URI previousOwner;
	static public URI principal;
	static public URI printedAt;
	static public URI publishedAt;
	static public URI refersTo;
	static public URI restoration;
	static public URI scopeNote;
	static public URI shelfmarkLocation;
	static public URI sponsor;
	static public URI studentOf;
	static public URI subtitle;
	static public URI subtitleTransliteration;
	static public URI titleTransliteration;
	static public URI wasStudiedBy;
	static public URI wasTaughtBy;
	static public URI watermark;
	static public URI writer;
	static public URI writtenAt;
	static public URI writtenAreaDimension;
	
	static {
		f = ValueFactoryImpl.getInstance();
		
		//http://onto.dm2e.eu/schemas/dm2e/1.0/#Manuscript
		dm2eOnto10 = f.createURI("http://onto.dm2e.eu/schemas/dm2e/");
		Archive = f.createURI(dm2eOnto10 + "Collection");
		Collection = f.createURI(dm2eOnto10 + "Collection");
		Document = f.createURI(dm2eOnto10 + "Document");
		File = f.createURI(dm2eOnto10 + "File");
		Manuscript = f.createURI(dm2eOnto10 + "Manuscript");
		Paragraph = f.createURI(dm2eOnto10 + "Paragraph");
		Photo = f.createURI(dm2eOnto10 + "Photo");
		Publication = f.createURI(dm2eOnto10 + "Publication");
		Work = f.createURI(dm2eOnto10 + "Publication");
		
		
		//Page = f.createURI("http://purl.org/spar/fabio/Page");
		// Page now served by dm2e
		Page = f.createURI(dm2eOnto10 + "Page");
		Book = f.createURI("http://purl.org/ontology/bibo/Book");
		
		//Properties
		
		artist = f.createURI(dm2eOnto10 + "artist");
		callNumber = f.createURI(dm2eOnto10 + "callNumber");
		composer = f.createURI(dm2eOnto10 + "composer");
		condition = f.createURI(dm2eOnto10 + "condition");
		contributor = f.createURI(dm2eOnto10 + "contributor");
		copyist = f.createURI(dm2eOnto10 + "copyist");
		explicit = f.createURI(dm2eOnto10 + "explicit");
		hasPartCHO = f.createURI(dm2eOnto10 + "hasPartCHO");
		hasPartPlace = f.createURI(dm2eOnto10 + "hasPartPlace");
		hasPartTimeSpan = f.createURI(dm2eOnto10 + "hasPartTimeSpan");
		hasPartWebResource = f.createURI(dm2eOnto10 + "hasPartWebResource");
		honoree = f.createURI(dm2eOnto10 + "honoree");
		illustration = f.createURI(dm2eOnto10 + "illustration");
		incipit = f.createURI(dm2eOnto10 + "incipit");
		influencedBy = f.createURI(dm2eOnto10 + "influencedBy");
		isDerivativeOfCHO = f.createURI(dm2eOnto10 + "isDerivativeOfCHO");
		isDerivativeOfWebResource = f.createURI(dm2eOnto10 + "isDerivativeOfWebResource");
		isPartOfCHO = f.createURI(dm2eOnto10 + "isPartOfCHO");
		isPartOfPlace = f.createURI(dm2eOnto10 + "isPartOfPlace");
		isPartOfTimeSpan = f.createURI(dm2eOnto10 + "isPartOfTimeSpan");
		isPartOfWebResource = f.createURI(dm2eOnto10 + "isPartOfWebResource");
		levelOfGenesis = f.createURI(dm2eOnto10 + "levelOfGenesis");
		mentioned = f.createURI(dm2eOnto10 + "mentioned");
		misattributed = f.createURI(dm2eOnto10 + "misattributed");
		owner = f.createURI(dm2eOnto10 + "owner");
		pageDimension = f.createURI(dm2eOnto10 + "pageDimension");
		painter = f.createURI(dm2eOnto10 + "painter");
		patron = f.createURI(dm2eOnto10 + "patron");
		portrayed = f.createURI(dm2eOnto10 + "portrayed");
		previousOwner = f.createURI(dm2eOnto10 + "previousOwner");
		principal = f.createURI(dm2eOnto10 + "principal");
		printedAt = f.createURI(dm2eOnto10 + "printedAt");
		publishedAt = f.createURI(dm2eOnto10 + "publishedAt");
		refersTo = f.createURI(dm2eOnto10 + "refersTo");
		restoration = f.createURI(dm2eOnto10 + "restoration");
		scopeNote = f.createURI(dm2eOnto10 + "scopeNote");
		shelfmarkLocation = f.createURI(dm2eOnto10 + "shelfmarkLocation");
		sponsor = f.createURI(dm2eOnto10 + "sponsor");
		studentOf = f.createURI(dm2eOnto10 + "studentOf");
		subtitle = f.createURI(dm2eOnto10 + "subtitle");
		subtitleTransliteration = f.createURI(dm2eOnto10 + "subtitleTransliteration");
		titleTransliteration = f.createURI(dm2eOnto10 + "titleTransliteration");
		wasStudiedBy = f.createURI(dm2eOnto10 + "wasStudiedBy");
		wasTaughtBy = f.createURI(dm2eOnto10 + "wasTaughtBy");
		watermark = f.createURI(dm2eOnto10 + "watermark");
		writer = f.createURI(dm2eOnto10 + "writer");
		writtenAreaDimension = f.createURI(dm2eOnto10 + "writtenAreaDimension");
		writtenAt = f.createURI(dm2eOnto10 + "writtenAt");
		
	}
}
