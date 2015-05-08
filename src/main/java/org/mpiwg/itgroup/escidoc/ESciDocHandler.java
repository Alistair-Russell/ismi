package org.mpiwg.itgroup.escidoc;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.mpiwg.itgroup.escidoc.bo.Creator;
import org.mpiwg.itgroup.escidoc.bo.Publication;
import org.mpiwg.itgroup.escidoc.bo.Publishing;
import org.mpiwg.itgroup.escidoc.bo.utils.SortPublicationByTitle;

public class ESciDocHandler {
	
	public static String ISMI_CONTEXT_ID = "escidoc:79281";
	private static String ENCODING = "UTF-8";
	public static String host = "http://escidoc.mpiwg-berlin.mpg.de:8080/srw/search/escidoc_all?operation=searchRetrieve&maximumRecords=5000&query=";
	public static String queryGetAll = "escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid=" + ISMI_CONTEXT_ID;
	
	public static String queryGetObjectById = "escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid=" + ISMI_CONTEXT_ID + " and escidoc.objid=";
	
	public static String queryByTitle = "escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid=" + ISMI_CONTEXT_ID + " and escidoc.any-title:=";
	
	static{
		try {
			queryGetAll = URLEncoder.encode(queryGetAll, ENCODING);
			queryGetObjectById = URLEncoder.encode(queryGetObjectById, ENCODING);
			queryByTitle = URLEncoder.encode(queryByTitle, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Publication> getAllPublications() throws Exception{
		List<Publication> list = null;
		System.out.println("getAllPublications=" + host + queryGetAll);
		URI uri = new URI(host + queryGetAll);
        URL url = uri.toURL();

        InputStream in = url.openStream();
        list = getPublicationsFromStream(new SAXBuilder().build(in));
            
		Collections.sort(list, new SortPublicationByTitle());
        return list;
	}
	
	public static List<Publication> searchPublicationByTitle(String title) throws Exception{
		List<Publication> list = new ArrayList<Publication>();
		URI uri = new URI(host + queryGetAll);
        URL url = uri.toURL();
        InputStream in = url.openStream();
        List<Publication> pubList = getPublicationsFromStream(new SAXBuilder().build(in));
        
        title = title.toLowerCase();
        
        for(Publication pub : pubList){
        	if(StringUtils.isNotEmpty(pub.getTitle())){
        		String pubTitle = pub.getTitle().toLowerCase();
        		if(pubTitle.contains(title)){
        			list.add(pub);
        		}
        	}
        }      
        return list;
	}
	
	public static List<Publication> searchPublicationByAuthor(String author) throws Exception{
		List<Publication> list = new ArrayList<Publication>();
		
		URI uri = new URI(host + queryGetAll);
        URL url = uri.toURL();
        InputStream in = url.openStream();
        List<Publication> pubList = getPublicationsFromStream(new SAXBuilder().build(in));
        
        //TODO
        /*
        author = author.toLowerCase();
        
        for(Publication pub : pubList){
        	if(pub.getCreator() != null && StringUtils.isNotEmpty(pub.getCreator().getCompleteName())){
        		String pubAuthor = pub.getCreator().getCompleteName().toLowerCase();
        		if(pubAuthor.contains(author)){
        			list.add(pub);
        		}
        	}
        } */            
        return list;
	}
	
	public static Publication getPublicationById(String objectId) throws Exception{
		List<Publication> list = null;
		String urlString = host + queryGetObjectById + objectId;
		System.out.println(urlString);
		URI uri = new URI(urlString);
        URL url = uri.toURL();
        InputStream in = url.openStream();
        list = getPublicationsFromStream(new SAXBuilder().build(in));
        if(list.size() > 0){
        	return list.get(0);
        }
        return null;
	}
	
	private static List<Publication> getPublicationsFromStream(Document doc) throws Exception {
		
		List<Publication> list = new ArrayList<Publication>();
		
		XPath xp = XPath.newInstance(".//escidocItem:item");
		xp.addNamespace("escidocItem", "http://www.escidoc.de/schemas/item/0.9");
		
		List<Object> inputList = xp.selectNodes(doc);
		//System.out.println("inputList.size: " + inputList.size());
		for(Object obj :  inputList){
			if(obj instanceof Element){
				Element item = (Element)obj;
				Publication reference = new Publication(item.getAttributeValue("objid"));
				//System.out.println(reference.getObjid());
				
				Element md_records = item.getChild("md-records", Namespace.getNamespace("escidocMetadataRecords", "http://www.escidoc.de/schemas/metadatarecords/0.5"));
				Element md_record = md_records.getChild("md-record", Namespace.getNamespace("escidocMetadataRecords", "http://www.escidoc.de/schemas/metadatarecords/0.5"));
				Element publication = md_record.getChild("publication", Namespace.getNamespace("publication", "http://purl.org/escidoc/metadata/profiles/0.1/publication"));
				
				
				
				
				Element title = publication.getChild("title", Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/"));
				Element publishing_info = publication.getChild("publishing-info", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
				Element issued = publication.getChild("issued", Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/"));
				if(issued != null){
					reference.setIssued(issued.getText());
				}
				Element subject = publication.getChild("subject", Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/"));
				
				List<Object> creatorList = publication.getChildren("creator", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
				for(Object object : creatorList){
					if(object instanceof Element){
						Element creator = (Element)object;
						Element person = creator.getChild("person", Namespace.getNamespace("person", "http://purl.org/escidoc/metadata/profiles/0.1/person"));
						
						if(person != null){
							Element complete_name = person.getChild("complete-name", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
							Element family_name = person.getChild("family-name", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
							Element given_name = person.getChild("given-name", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
							Element organization = person.getChild("organization", Namespace.getNamespace("organization", "http://purl.org/escidoc/metadata/profiles/0.1/organization"));
							
							reference.addCreator(new Creator(
									(complete_name != null) ? complete_name.getText() : "", 
									(family_name != null) ? family_name.getText() : "", 
									(given_name != null) ? given_name.getText() : ""));	
						}	
					}
				}
				
				
				if(publishing_info != null){
					Element publisher = publishing_info.getChild("publisher", Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/"));
					Element place = publishing_info.getChild("place", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
					Element edition = publishing_info.getChild("edition", Namespace.getNamespace("eterms", "http://purl.org/escidoc/metadata/terms/0.1/"));
					
					reference.setPublishing(
							new Publishing(
									(publisher != null) ? publisher.getText() : "", 
									(place != null) ? place.getText() : "", 
									(edition != null) ? edition.getText() : ""));	
				}
				
				reference.setSubject((subject != null) ? subject.getText() : "");
				reference.setTitle((title != null) ? title.getText() : "");
				//System.out.println(reference.toString());
				list.add(reference);
				
			}else{
				System.out.println(obj);
			}
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		try {
			//ESciDocHandler.getAllPublications();
			
			Publication pub = ESciDocHandler.getPublicationById("escidoc:127561");
			XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
			outputter1.output(pub.toXMLESciDoc(null), System.out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
