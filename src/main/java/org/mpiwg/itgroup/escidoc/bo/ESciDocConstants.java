package org.mpiwg.itgroup.escidoc.bo;

import java.io.Serializable;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

public class ESciDocConstants implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7156928908863818464L;
	public static Integer DEBUG = -1;
	public static Integer CREATE = 0;
	public static Integer UPDATE = 1;
	
	
	public static Namespace NS_PERSON = Namespace.getNamespace("person", "http://purl.org/escidoc/metadata/profiles/0.1/person");
	public static Namespace NS_ORGANIZATION = Namespace.getNamespace("organization", "http://purl.org/escidoc/metadata/profiles/0.1/organization");
	
	public static Namespace NS_DC = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
	public static Namespace NS_ETERMS = Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/");
	
	public static Namespace NS_DCTERMS = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");
	
	//xmlns:publication="http://purl.org/escidoc/metadata/profiles/0.1/publication"
	public static Namespace NS_PUBLICATION = Namespace.getNamespace("publication", "http://purl.org/escidoc/metadata/profiles/0.1/publication");
	
	//xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	public static Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	
	//xsi:type="dcterms:W3CDTF"
	//public static Namespace NS_XSI = Namespace.getNamespace("xsi", "dcterms:W3CDTF");
	
	//xmlns:relations="http://www.escidoc.de/schemas/relations/0.3"
	public static Namespace NS_RELATIONS = Namespace.getNamespace("relations", "http://www.escidoc.de/schemas/relations/0.3");
	
	//	xmlns:escidocMetadataRecords="http://www.escidoc.de/schemas/metadatarecords/0.5"
	public static Namespace NS_ESCIDOC_MD_RECORDS = Namespace.getNamespace("escidocMetadataRecords", "http://www.escidoc.de/schemas/metadatarecords/0.5");
	
	//	xmlns:escidocContentStreams="http://www.escidoc.de/schemas/contentstreams/0.7"
	public static Namespace NS_ESCIDOC_CONTENT_STREAMS = Namespace.getNamespace("escidocContentStreams", "http://www.escidoc.de/schemas/contentstreams/0.7");
	
	//	xmlns:escidocComponents="http://www.escidoc.de/schemas/components/0.9"
	public static Namespace NS_ESCIDOC_COMPONENTS = Namespace.getNamespace("escidocComponents", "http://www.escidoc.de/schemas/components/0.9"); 
	
	//	xmlns:version="http://escidoc.de/core/01/properties/version/"
	public static Namespace NS_VERSION = Namespace.getNamespace("version", "http://escidoc.de/core/01/properties/version/");
	
	//	xmlns:release="http://escidoc.de/core/01/properties/release/"
	public static Namespace NS_RELEASE = Namespace.getNamespace("release", "http://escidoc.de/core/01/properties/release/");
	
	//	xmlns:escidocItem="http://www.escidoc.de/schemas/item/0.9"
	public static Namespace NS_ESCIDOC_ITEM = Namespace.getNamespace("escidocItem", "http://www.escidoc.de/schemas/item/0.9");
	
	//	xmlns:prop="http://escidoc.de/core/01/properties/"
	public static Namespace NS_PROP = Namespace.getNamespace("prop", "http://escidoc.de/core/01/properties/");
	
	//	xmlns:srel="http://escidoc.de/core/01/structural-relations/"
	public static Namespace NS_SREL = Namespace.getNamespace("srel", "http://escidoc.de/core/01/structural-relations/");
	
	//	xmlns:xlink="http://www.w3.org/1999/xlink"
	public static Namespace NS_XLINK = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");
	
	//	xml:base="http://escidoc.mpiwg-berlin.mpg.de:8080"
	//public static Namespace NS_BASE = Namespace.getNamespace("base", "http://escidoc.mpiwg-berlin.mpg.de:8080");
	
	//	xlink:type="simple"
	//public static Namespace NS_ = Namespace.getNamespace("", "");

	//	xlink:title="Max Planck Society"
	//public static Namespace NS_ = Namespace.getNamespace("", "");
	
	//	xlink:href="/ir/item/escidoc:127561"
	//public static Namespace NS_ = Namespace.getNamespace("", "");
	
	public Element toElementXMLESciDoc(Integer mode){
		return null;
	}
	
	public Document toXMLESciDoc(Integer mode){
		Document doc = new Document();
		
		doc.setRootElement(this.toElementXMLESciDoc(mode));
		
		return doc;
	}
	
}
