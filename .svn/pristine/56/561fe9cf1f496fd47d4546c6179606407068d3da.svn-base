package de.mpiwg.itgroup.dm2e;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

public class URIUtils {
	
	static ValueFactory f;
	
	static public URI ProvidedCHO;
	
	//ore:Aggregation
	static public URI edmAggregatedCHO;
	static public URI edmProvider;
	static public URI edmDataProvider;
	static public URI edmRights;
	static public URI edmHasView;
	static public URI edmObject;
	static public URI dm2eHasAnnotatableVersionAt;
	static public URI dctCreated;
	static public URI dctModified;
	static public URI dm2eHasVersion;
	static public URI dctCreator;
	static public URI dm2eDisplayLevel;
	static public URI dm2eLevelOfHierarchy;
	
	//edm:ProvidedCHO
	static public URI edmType;
	static public URI dcType;
	static public URI dcTitle;
	//static public URI dctTitle;
	static public URI dm2eTitleTransliteration;
	static public URI dm2eSubtitle;
	static public URI dm2eSubtitleTransliteration;
	static public URI dctAlternative;
	static public URI dcDescription;
	//static public URI dctDescription;
	static public URI dcLanguage;
	static public URI dctIssued;
	static public URI dm2ePublishedAt;
	static public URI dcIdentifier;
	static public URI dm2eIsbn;
	static public URI dm2eCallNumber;
	static public URI edmCurrentLocation;
	static public URI edmPlace;
	//TimeSpan
	static public URI edmTimeSpan;
	static public URI edmBegin;
	static public URI edmEnd;
	static public URI cidocBeginQual;
	static public URI cidocEndQual;
	//static public URI edmRights;
	static public URI dcSubject;
	static public URI dctExtent;
	static public URI biboNumPages;
	static public URI biboNumVolumes;
	static public URI biboNumber;
	static public URI dctTableOfContents;
	static public URI dctProvenance;
	static public URI dcFormat;
	static public URI edmIsDerivativeOf;
	static public URI dctHasVersion;
	static public URI dctHasPart;
	static public URI dctIsPartOf;
	static public URI dm2eIsPartOf;
	static public URI dm2eLocatedIn;
	static public URI dctReferences;
	static public URI rdfType;
	static public URI dm2eCondition;
	static public URI dm2eWatermark;
	static public URI dm2eSupport;
	static public URI dm2eRestoration;
	static public URI dm2eReferencedEdition;
	static public URI dm2eReferencedPublication;
	static public URI dm2eRelatedPublication;
	static public URI dm2eLevelOfGenesis;
	static public URI edmHasMet;
	static public URI dm2eIncipit;
	static public URI dm2eExplicit;
	static public URI dm2eColophon;
	static public URI dm2ePageDimension;
	static public URI dm2eWrittenAreaDimensions;
	static public URI dm2eDateOfRemark;
	static public URI dm2eRefersTo;
	
	//personroles
	
	//static public URI dctCreator;
	static public URI dcPublisher;
	static public URI dm2eArtist;
	static public URI dm2eAuthor;
	static public URI dm2eComposer;
	static public URI dm2eContributor;
	static public URI dm2eCopyist;
	static public URI dm2eCopyrightHolder;
	static public URI dm2eEditor;
	static public URI dm2eHonoree;
	static public URI dm2eIllustrator;
	static public URI dm2eLibrary;
	static public URI dm2eMentionedPerson;
	static public URI dm2eMisattributedPerson;
	static public URI dm2ePainter;
	static public URI dm2ePatron;
	static public URI dm2ePortrayedPerson;
	static public URI dm2ePortrayed;
	static public URI dm2ePrinter;
	static public URI dm2ePublisher;
	static public URI dm2eRecipient;
	static public URI dm2eSponsor;
	static public URI dm2eStaff;
	static public URI dm2eSubjectOf;
	static public URI dm2eTranslator;
	static public URI dm2eWriter;
	
	static public URI dcContributor;
	static public URI biboEditor;
	static public URI biboRecipient;
	static public URI proIllustrator;
	static public URI proAuthor;
	static public URI proTranslator;
	
	static public URI skosPrefLabel;
	static public URI skosAltLabel;
	static public URI skosConcept;
	
	static{
		f = ValueFactoryImpl.getInstance();
		
		//ore:Aggregation
		edmAggregatedCHO = f.createURI(NameSpaces.edm + "aggregatedCHO");
		edmProvider = f.createURI(NameSpaces.edm + "provider");
		edmDataProvider = f.createURI(NameSpaces.edm + "dataProvider");
		edmRights = f.createURI(NameSpaces.edm + "rights");
		edmHasView = f.createURI(NameSpaces.edm + "hasView");
		edmObject = f.createURI(NameSpaces.edm + "object");
		
		dm2eHasAnnotatableVersionAt = f.createURI(NameSpaces.dm2e + "hasAnnotatableVersionAt");
		dctCreated = f.createURI(NameSpaces.dcterms + "created");
		dctModified = f.createURI(NameSpaces.dcterms + "modified");
		dm2eHasVersion = f.createURI(NameSpaces.dm2e + "hasVersion");
		dctCreator = f.createURI(NameSpaces.dcterms + "creator");
		dm2eDisplayLevel = f.createURI(NameSpaces.dm2e + "displayLevel");
		dm2eLevelOfHierarchy = f.createURI(NameSpaces.dm2e + "levelOfHierarchy");
		
		//edm:ProvidedCHO
		edmType = f.createURI(NameSpaces.edm + "type");
		dcType = f.createURI(NameSpaces.dc + "type");
		dcTitle = f.createURI(NameSpaces.dc + "title");
		// dctTitle = f.createURI(NameSpaces.dcterms + "title");
		dm2eTitleTransliteration = f.createURI(NameSpaces.dm2e + "titleTransliteration");
		dm2eSubtitle = f.createURI(NameSpaces.dm2e + "subtitle");
		dm2eSubtitleTransliteration = f.createURI(NameSpaces.dm2e + "subtitleTransliteration");
		dctAlternative = f.createURI(NameSpaces.dcterms + "alternative");
		dcDescription = f.createURI(NameSpaces.dc + "description"); 
		//dctDescription = f.createURI(NameSpaces.dcterms + "description");
		dcLanguage = f.createURI(NameSpaces.dc + "language");
		dctIssued = f.createURI(NameSpaces.dcterms + "issued");
		dm2ePublishedAt = f.createURI(NameSpaces.dm2e + "publishedAt");
		dcIdentifier = f.createURI(NameSpaces.dc + "identifier");
		dm2eIsbn = f.createURI(NameSpaces.dm2e + "isbn");
		dm2eCallNumber = f.createURI(NameSpaces.dm2e + "callNumber");
		edmCurrentLocation = f.createURI(NameSpaces.edm + "currentLocation");
		edmPlace = f.createURI(NameSpaces.edm + "Place");
		//TimeSpan
		edmTimeSpan = f.createURI(NameSpaces.edm + "timeSpan");
		edmBegin = f.createURI(NameSpaces.edm + "begin");
		edmEnd = f.createURI(NameSpaces.edm + "end");
		cidocBeginQual = f.createURI(NameSpaces.cidoc + "P79F.beginning_is_qualified_by");
		cidocEndQual = f.createURI(NameSpaces.cidoc + "P80F.end_is_qualified_by"); 
		//edmRights;
		dcSubject = f.createURI(NameSpaces.dc + "subject");
		dctExtent = f.createURI(NameSpaces.dcterms + "extent");
		biboNumPages = f.createURI(NameSpaces.bibo + "numPages");
		biboNumVolumes = f.createURI(NameSpaces.bibo + "numVolumes");
		biboNumber = f.createURI(NameSpaces.bibo + "number");
		dctTableOfContents = f.createURI(NameSpaces.dcterms + "tableOfContents");
		dctProvenance = f.createURI(NameSpaces.dcterms + "provenance");
		dcFormat = f.createURI(NameSpaces.dc + "format");
		edmIsDerivativeOf = f.createURI(NameSpaces.edm + "isDerivativeOf");
		dctHasVersion = f.createURI(NameSpaces.dcterms + "hasVersion");
		dctHasPart = f.createURI(NameSpaces.dcterms + "hasPart");
		dctIsPartOf = f.createURI(NameSpaces.dcterms + "isPartOf");
//		dm2eIsPartOf = f.createURI(NameSpaces.dm2e + "isPartOf");
		dm2eLocatedIn = f.createURI(NameSpaces.dm2e + "locatedIn");
		dctReferences = f.createURI(NameSpaces.dcterms + "references");
		rdfType = f.createURI(NameSpaces.rdf + "type");
		dm2eCondition = f.createURI(NameSpaces.dm2e + "condition");
		dm2eWatermark = f.createURI(NameSpaces.dm2e + "watermark");
		dm2eSupport = f.createURI(NameSpaces.dm2e + "support");
		dm2eRestoration = f.createURI(NameSpaces.dm2e + "restoration");
		dm2eReferencedEdition = f.createURI(NameSpaces.dm2e + "referencedEdition");
		dm2eReferencedPublication = f.createURI(NameSpaces.dm2e + "referencedPublication");
		dm2eRelatedPublication = f.createURI(NameSpaces.dm2e + "relatedPublication");
		dm2eLevelOfGenesis = f.createURI(NameSpaces.dm2e + "levelOfGenesis");
		edmHasMet = f.createURI(NameSpaces.edm + "hasMet");
		dm2eIncipit = f.createURI(NameSpaces.dm2e + "incipit");
		dm2eExplicit = f.createURI(NameSpaces.dm2e + "explicit");
		dm2eExplicit = f.createURI(NameSpaces.dm2e + "explicit");
		dm2ePageDimension = f.createURI(NameSpaces.dm2e + "pageDimension");
		dm2eWrittenAreaDimensions = f.createURI(NameSpaces.dm2e + "writtenAreaDimensions");
		dm2eDateOfRemark = f.createURI(NameSpaces.dm2e + "dateOfRemark");
		dm2eRefersTo = f.createURI(NameSpaces.dm2e + "refersTo");
		
		//personroles
		
		//dctCreator = f.createURI(NameSpaces.dc + "");
		dcPublisher = f.createURI(NameSpaces.dc + "publisher");
		dm2eArtist = f.createURI(NameSpaces.dm2e + "artist");
		dm2eAuthor = f.createURI(NameSpaces.dm2e + "author");
		dm2eComposer = f.createURI(NameSpaces.dm2e + "composer");
		dm2eContributor = f.createURI(NameSpaces.dm2e + "contributor");
		dm2eCopyist = f.createURI(NameSpaces.dm2e + "copyist");
		dm2eCopyrightHolder = f.createURI(NameSpaces.dm2e + "copyist");
		dm2eEditor = f.createURI(NameSpaces.dm2e + "editor");
		dm2eHonoree = f.createURI(NameSpaces.dm2e + "honoree");
		dm2eIllustrator = f.createURI(NameSpaces.dm2e + "illustrator");
		dm2eLibrary = f.createURI(NameSpaces.dm2e + "illustrator");
		dm2eMentionedPerson = f.createURI(NameSpaces.dm2e + "mentionedPerson");
		dm2eMisattributedPerson = f.createURI(NameSpaces.dm2e + "mentionedPerson");
		dm2ePainter = f.createURI(NameSpaces.dm2e + "painter");
		dm2ePatron = f.createURI(NameSpaces.dm2e + "patron");
		dm2ePortrayedPerson = f.createURI(NameSpaces.dm2e + "portrayedPerson");
		dm2ePortrayed = f.createURI(NameSpaces.dm2e + "portrayed");
		dm2ePrinter = f.createURI(NameSpaces.dm2e + "printer");
		dm2ePublisher = f.createURI(NameSpaces.dm2e + "printer");
		dm2eRecipient = f.createURI(NameSpaces.dm2e + "recipient");
		dm2eSponsor = f.createURI(NameSpaces.dm2e + "sponsor");
		dm2eStaff = f.createURI(NameSpaces.dm2e + "staff");
		dm2eSubjectOf = f.createURI(NameSpaces.dm2e + "subjectOf");
		dm2eTranslator = f.createURI(NameSpaces.dm2e + "translator");
		dm2eWriter = f.createURI(NameSpaces.dm2e + "writer");
		
		proAuthor = f.createURI(NameSpaces.pro + "author");
		dcContributor = f.createURI(NameSpaces.dc + "contributor");
		biboEditor = f.createURI(NameSpaces.bibo + "editor");
		biboRecipient = f.createURI(NameSpaces.bibo + "recipient");
		proIllustrator = f.createURI(NameSpaces.pro + "illustrator");
		proTranslator = f.createURI(NameSpaces.pro + "translator");
		
		skosPrefLabel = f.createURI(NameSpaces.skos + "prefLabel");
		skosAltLabel = f.createURI(NameSpaces.skos + "altLabel");
		skosConcept = f.createURI(NameSpaces.skos + "Concept");
	}
	
	
	
	
}
