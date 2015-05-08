package org.mpiwg.itgroup.escidoc.bo;

import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ESciDocItem extends ESciDocConstants implements Cloneable, Comparable<ESciDocItem>{

	public static int ESCIDOC_ERROR_ID_NO_FOUND = 0;
	public static int ESCIDOC_ERROR_SERVER_NO_RESPONDED = 1;
	
	private Integer errorLoading;
	
	private Publication publication = null;
	
	private String lastModificationDate;
	
	//Properties TODO: this should be configurable
	private static String CONTEXT_ID = "escidoc:79281";
	private static String CONTENT_MODEL = "escidoc:persistent4";
	
	private String publicStatus;

	//item/properties/latest-version
	//private String latestVersionHref;
	private String latestVersionNumber;
	private String latestVersionDate;
	
	//item/properties/latest-release
	private String latestReleaseNumber;
	private String latestReleaseDate;
	//private String latestReleasePid;
	
	//item/properties/version
	private String versionNumber;
	private String versionDate;
	private String versionStatus;
	
	
	public ESciDocItem(String id){
		publication = new Publication(id);
	}
	
	public ESciDocItem(){
		publication = new Publication(null);
	}
	
	@Override 
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Element toElementXMLESciDoc(Integer mode){
		Element root = new Element("item", NS_ESCIDOC_ITEM);
		
		root.addNamespaceDeclaration(NS_RELATIONS);
		root.addNamespaceDeclaration(NS_ESCIDOC_MD_RECORDS);
		root.addNamespaceDeclaration(NS_ESCIDOC_CONTENT_STREAMS);
		root.addNamespaceDeclaration(NS_ESCIDOC_COMPONENTS);
		root.addNamespaceDeclaration(NS_VERSION);
		root.addNamespaceDeclaration(NS_RELEASE);
		root.addNamespaceDeclaration(NS_PROP);
		root.addNamespaceDeclaration(NS_SREL);
		root.addNamespaceDeclaration(NS_XLINK);
		//root.addNamespaceDeclaration(NS_BASE);

		if(UPDATE.equals(mode)){
			// discarded for UPDATE and CREATE
			root.setAttribute(new Attribute("type", "simple", NS_XLINK));
			root.setAttribute(new Attribute("title", "Max Planck Society", NS_XLINK));
			root.setAttribute(new Attribute("href", "/ir/item/" + publication.getObjid(), NS_XLINK));
			
			root.setAttribute(new Attribute("last-modification-date", this.lastModificationDate));
		}
		
		// CREATE=required, UPDATE=optional
		//if(CREATE.equals(mode))
		root.addContent(getESciDocItemProperties(mode));
		
		root.addContent(getESciDocMDRecords(mode));
		
		//item/components UPDATE, CREATE = required
		Element components = new Element("components", NS_ESCIDOC_COMPONENTS);
		if(mode == null){
			components.setAttribute(new Attribute("type", "simple", NS_XLINK));
			components.setAttribute(new Attribute("title", "Components of Item " + publication.getObjid(), NS_XLINK));
			components.setAttribute(new Attribute("href", "/ir/item/"+ publication.getObjid() +"/components", NS_XLINK));	
		}
		
		root.addContent(components);
		
		if(UPDATE.equals(mode)){
			//item/relations UPDATE, CREATE = optional		
			Element relations = new Element("relations", NS_RELATIONS);
			relations.setAttribute(new Attribute("type", "simple", NS_XLINK));
			relations.setAttribute(new Attribute("title", "Relations of Item", NS_XLINK));
			relations.setAttribute(new Attribute("href", "/ir/item/"+this.publication.getObjid()+"/relations", NS_XLINK));
			
			//item/resources UPDATE, CREATE = discarded
			Element resources = new Element("resources", NS_ESCIDOC_ITEM);
			resources.setAttribute(new Attribute("type", "simple", NS_XLINK));
			resources.setAttribute(new Attribute("title", "Resources", NS_XLINK));
			resources.setAttribute(new Attribute("href", "/ir/item/"+this.publication.getObjid()+"/resources", NS_XLINK));	
		}
		
		return root;
	}
	
	private Element getESciDocItemProperties(Integer mode){
		
		Element root = new Element("properties", NS_ESCIDOC_ITEM);
		
		//CREATE, UPDATE discard = xlink:type, xlink:title, xlink:href, xml:base
		//UPDATE requests = last-modification-date
		
		//item/properties
		if(UPDATE.equals(mode)){
			root.setAttribute(new Attribute("type", "simple", NS_XLINK));
			root.setAttribute(new Attribute("title", "Properties", NS_XLINK));
			root.setAttribute(new Attribute("href", "/ir/item/" + this.publication.getObjid() + "/properties", NS_XLINK));
		}
		
		
		//item/properties/context
		Element context = new Element("context", NS_SREL);
		context.setAttribute(new Attribute("type", "simple", NS_XLINK));
		context.setAttribute(new Attribute("title", "ISMI", NS_XLINK));
		context.setAttribute(new Attribute("href", "/ir/context/" + CONTEXT_ID, NS_XLINK));
		root.addContent(context);
		
		//item/properties/content-model
		Element contentModel = new Element("content-model", NS_SREL);
		if(UPDATE.equals(mode)){
			contentModel.setAttribute(new Attribute("type", "simple", NS_XLINK));
			contentModel.setAttribute(new Attribute("title", "ct", NS_XLINK));
		}
		contentModel.setAttribute(new Attribute("href", "/cmm/content-model/" + CONTENT_MODEL, NS_XLINK));
		root.addContent(contentModel);
		
		
		//item/properties/latest-version
		if(mode == null){
			Element latestVersion = new Element("latest-version", NS_PROP);
			//latestVersion.setAttribute(new Attribute("href", this.latestVersionHref, NS_XLINK));
			
			Element eVersionNumber = new Element("number", NS_VERSION);
			Element eVersionDate = new Element("date", NS_VERSION);
			
			eVersionNumber.addContent(this.latestVersionNumber);
			eVersionDate.addContent(this.latestVersionDate);
			
			latestVersion.addContent(eVersionNumber);
			latestVersion.addContent(eVersionDate);
			
			root.addContent(latestVersion);
		}
		
		//item/properties/latest-release
		if(mode == null){
			Element latestRelease = new Element("latest-release", NS_PROP);
			//latestRelease.setAttribute(new Attribute("href", this.latestReleaseHref, NS_XLINK));
			
			Element eReleaseNumber = new Element("number", NS_RELEASE);
			Element eReleaseDate = new Element("date", NS_RELEASE);
			//Element eReleasePid = new Element("pid", NS_RELEASE);
			
			eReleaseNumber.addContent(this.latestReleaseNumber);
			eReleaseDate.addContent(this.latestReleaseDate);
			//eReleasePid.addContent(this.latestReleasePid);
			
			latestRelease.addContent(eReleaseNumber);
			latestRelease.addContent(eReleaseDate);
			//latestRelease.addContent(eReleasePid);
			
			root.addContent(latestRelease);
		}
		
		return root;
	}

	private Element getESciDocMDRecords(Integer mode){
		Element root = new Element("md-records", NS_ESCIDOC_MD_RECORDS);
		
		if(UPDATE.equals(mode)){
			root.setAttribute(new Attribute("type", "simple", NS_XLINK));
			root.setAttribute(new Attribute("title", "Metadata Records of Item " + publication.getObjid(), NS_XLINK));
			root.setAttribute(new Attribute("href", "/ir/item/"+ publication.getObjid() +"/md-records", NS_XLINK));
		}
		if(UPDATE.equals(mode)){
			//TODO last-modification-date
		}
		
		Element record = new Element("md-record", NS_ESCIDOC_MD_RECORDS);
		record.setAttribute(new Attribute("name", "escidoc"));
		
		if(UPDATE.equals(mode)){
			record.setAttribute(new Attribute("type", "simple", NS_XLINK));
			record.setAttribute(new Attribute("title", "escidoc", NS_XLINK));
			record.setAttribute(new Attribute("href", "/ir/item/"+ 	this.publication.getObjid() +"/md-records/md-record/escidoc", NS_XLINK));	
		}
		
		record.addContent(this.publication.toElementXMLESciDoc(mode));
		
		root.addContent(record);
		
		return root;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public String getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
	
	public Integer getErrorLoading() {
		return errorLoading;
	}

	public void setErrorLoading(Integer errorLoading) {
		this.errorLoading = errorLoading;
	}
	
	public String getObjId(){
		if(publication != null)
			return this.publication.getObjid();
		return null;
	}
	
	public String getLatestVersionNumber() {
		return latestVersionNumber;
	}

	public void setLatestVersionNumber(String latestVersionNumber) {
		this.latestVersionNumber = latestVersionNumber;
	}

	public String getLatestVersionDate() {
		return latestVersionDate;
	}

	public void setLatestVersionDate(String latestVersionDate) {
		this.latestVersionDate = latestVersionDate;
	}

	public String getLatestReleaseNumber() {
		return latestReleaseNumber;
	}

	public void setLatestReleaseNumber(String latestReleaseNumber) {
		this.latestReleaseNumber = latestReleaseNumber;
	}

	public String getLatestReleaseDate() {
		return latestReleaseDate;
	}

	public void setLatestReleaseDate(String latestReleaseDate) {
		this.latestReleaseDate = latestReleaseDate;
	}

	@Override
	public int compareTo(ESciDocItem arg0) {
		if(this.publication != null && arg0.publication != null){
			if(StringUtils.isNotEmpty(this.publication.getObjid()) && StringUtils.isNotEmpty(arg0.publication.getObjid())){
				return this.getPublication().getObjid().compareTo(arg0.publication.getObjid());
			}else if(StringUtils.isNotEmpty(this.publication.getObjid())){
				return -1;
			}else if(StringUtils.isNotEmpty(arg0.publication.getObjid())){
				return 1;
			}	
		}
		return 0;
	}

	public static void main(String[] args){
		try{
			
			Publication pub = new Publication(null);
			
			Publishing publishing = new Publishing("Jorge Urzua", "Chile", "JIJI");
			pub.setPublishing(publishing);
			
			Creator creator0 = new Creator("Jorge Urzua", "Urzua", "Jorge");
			Creator creator1 = new Creator("Valeria Mouzas", "Mouzas", "Valeria");
			pub.getCreatorList().add(creator0);
			pub.getCreatorList().add(creator1);
			
			pub.setIssued("1982");
			pub.getAlternativeList().add("alternative01");
			pub.getAlternativeList().add("alternative02");
			pub.getAlternativeList().add("alternative03");
			
			pub.setTitle("Por Dios...");
			
			XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
			outputter1.output(pub.toXMLESciDoc(CREATE), System.out);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

	public String getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(String versionStatus) {
		this.versionStatus = versionStatus;
	}
	
	public String getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
	}

	public String getInfo(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("publicStatus=" + this.publicStatus);
		sb.append(", versionNumber=" + this.versionNumber);
		sb.append(", versionStatus=" + this.versionStatus);
		sb.append(", versionDate=" + this.versionDate);
		sb.append(", releaseNumber=" + this.latestReleaseNumber);
		sb.append(", releaseDate=" + this.latestReleaseDate);
		
		return sb.toString();
		
	}
}
