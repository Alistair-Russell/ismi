package de.mpiwg.itgroup.dm2e;

public class NameSpaces {


    //URL Schemes
    public static String rootDM2E = "http://data.dm2e.eu/data/";
    // collection string right now configured in Testing
    //public static String mpiwgCollection = "harriot";
    public static String mpiwgCollection = "ismi";

    //skos:Concept
    public static String skosConcept = rootDM2E + "concept/mpiwg/" + mpiwgCollection + "/";
    //edm:Agent
    public static String edmAgent = rootDM2E + "agent/mpiwg/" + mpiwgCollection + "/";
    //edm:Event
    public static String edmEvent = rootDM2E + "event/mpiwg/" + mpiwgCollection + "/";
    //edm:ProvidedCHO
    public static String edmProvidedCHO = rootDM2E + "item/mpiwg/" + mpiwgCollection + "/";
    //edm:Place
    public static String edmPlace = rootDM2E + "place/mpiwg/" + mpiwgCollection + "/" ;
    //edm:TimeSpan
    public static String edmTimespan = rootDM2E + "timespan/mpiwg/" + mpiwgCollection + "/";
    //ore:Aggregation
    public static String oreAggregation = rootDM2E + "aggregation/mpiwg/" + mpiwgCollection + "/";
    
    public static String webResource = rootDM2E + "webresource/mpiwg/" + mpiwgCollection + "/";

    //specialized
    public static String oldindexMetaNS = "http://www.mpiwg-berlin.mpg.de/indexMeta/";
    public static String dataProvider = rootDM2E + "agent/mpiwg/" + mpiwgCollection + "/Max%20Planck%20Institute%20For%20The%20History%20Of%20Science";
    public static String provider = rootDM2E + "agent/mpiwg/provider/DM2E";


    // namespaces
    public static String owl = "http://www.w3.org/2002/07/owl#";
    public static String foaf = "http://xmlns.com/foaf/0.1/";
    public static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static String rdfs = "http://www.w3.org/2000/01/rdf-schema#";

    public static String dc = "http://purl.org/dc/elements/1.1/";
    public static String dcterms = "http://purl.org/dc/terms/";
    public static String edm = "http://www.europeana.eu/schemas/edm/";
    public static String ore = "http://www.openarchives.org/ore/terms/";
    public static String skos = "http://www.w3.org/2004/02/skos/core#";
    public static String wgs84 = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static String bibo = "http://purl.org/ontology/bibo/";
    public static String pro = "http://purl.org/spar/pro/";
    public static String dm2e10 = "http://onto.dm2e.eu/schemas/dm2e/";
    public static String dm2e = "http://onto.dm2e.eu/schemas/dm2e/";
    public static String korbo = "http://purl.org/net7/korbo/vocab#";

    // adding all namespaces from owl file
    public static String protege = "http://protege.stanford.edu/plugins/owl/protege#";
    public static String vivo = "http://vivoweb.org/ontology/core#";
    public static String swrla = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#";
    public static String swrlb = "http://www.w3.org/2003/11/swrlb#";
    public static String sqwrl = "http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl#";
    public static String xsd = "http://www.w3.org/2001/XMLSchema#";
    public static String DOLCELite = "http://www.loa-cnr.it/ontologies/DOLCE-Lite.owl#";
    public static String crm = "http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#";
    public static String wgs84_pos = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static String xsp = "http://www.owl-ontologies.com/2005/08/07/xsp.owl#";
    public static String rdfsvoid = "http://rdfs.org/ns/void#";
    public static String rdaGr2 = "http://rdvocab.info/ElementsGr2/";
    public static String fabio = "http://purl.org/spar/fabio/";
    public static String swrl = "http://www.w3.org/2003/11/swrl#";
    public static String cidoc = "http://www.cidoc-crm.org/cidoc-crm/";
	
}
