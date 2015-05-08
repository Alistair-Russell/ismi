package org.mpiwg.itgroup.escidoc.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.utils.NormalizerUtils;

public class Publication extends ESciDocConstants implements Comparable<Publication>, Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	private String objid;
	private List<Creator> creatorList = new ArrayList<Creator>();
	private String title;
	private List<String> alternativeList = new ArrayList<String>();
	
	private Publishing publishing = new Publishing(null, null, null);
	private String subject;
	private String issued;
	private String numberOfPages;
	
	
	private String norTitle;
	private List<String> norAlternativeList = new ArrayList<String>();
	private String norSubject;
	
	
	
	private String additionalInformation;
	private Entity entity;
	
	private static String HTML_LABEL = "<span style=\"font-family: Arial;\">" +
			"<font size=\"2\">" +
			"[ID]AUTHORDATE <i>TITLE</i>PUBLISHER" +
			"</font></span>";
	
	@Override 
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String getHTMLLabel(){
		
		String html = HTML_LABEL.replace("TITLE", (StringUtils.isNotEmpty(this.title) ? this.title : ""));
		String creators = this.getCreatorsAsString();
		html = html.replace("AUTHOR", (creators != null) ? " " + creators + "." : "");
		html = html.replace("PUBLISHER", (publishing != null) ? " " + publishing.getPublisher() + "." : "");
		html = html.replace("DATE", (StringUtils.isNotEmpty(this.issued)? " (" + issued + ")." : ""));
		html = html.replace("ID", (StringUtils.isNotEmpty(this.objid)? this.objid : "NO ID"));
		return html;
	}
	
	public String getCreatorsAsString(){
		StringBuilder sb = new StringBuilder();
		for(Creator c : creatorList){
			if(StringUtils.isNotEmpty(sb.toString())){
				sb.append(", ");
			}
			sb.append(c.getFamilyName() + " " + c.getGivenName());
		}
		return sb.toString();
	}
	
	public void addCreator(Creator creator){
		this.creatorList.add(creator);
	}
	
	public Publication(String objid){
		this.objid = objid;
	}
	
	public List<Creator> getCreatorList() {
		return creatorList;
	}

	public void setCreatorList(List<Creator> creatorList) {
		this.creatorList = creatorList;
	}


	public String getObjid() {
		return objid;
	}

	public void setObjid(String objid) {
		this.objid = objid;
	}

	public String getTitle() {
		return title;
	}
	
	public String getNorTitle(){
		return norTitle;
	}
	
	public List<String> getNorAlternativeList() {
		
		return norAlternativeList;
	}


	public String getNorSubject() {
		return norSubject;
	}


	public void setTitle(String title) {
		this.title = title;
		if(StringUtils.isEmpty(title)){
			this.norTitle = new String();
		}else{
			this.norTitle = NormalizerUtils.normalize(title);
		}
	}

	public Publishing getPublishing() {
		return publishing;
	}

	public void setPublishing(Publishing publishing) {
		this.publishing = publishing;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
		if(StringUtils.isEmpty(subject)){
			this.norSubject = new String();
		}else{
			this.norSubject = NormalizerUtils.normalize(subject);
		}
	}
	
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}
	
	public List<String> getAlternativeList() {
		return alternativeList;
	}


	public void setAlternativeList(List<String> alternativeList) {
		this.alternativeList = alternativeList;
		for(String term : alternativeList){
			this.norAlternativeList.add(NormalizerUtils.normalize(term));
		}
	}

	public void addAlternative(String alternative){
		this.alternativeList.add(alternative);
		this.norAlternativeList.add(NormalizerUtils.normalize(alternative));
	}
	
	public String getNumberOfPages() {
		return numberOfPages;
	}


	public void setNumberOfPages(String numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	@Override
	public Element toElementXMLESciDoc(Integer mode){
		Element root = new Element("publication", NS_PUBLICATION);
		root.setAttribute(new Attribute("type", "http://purl.org/eprint/type/Book"));
		
		Element creator = new Element("creator", NS_ETERMS);
		creator.setAttribute(new Attribute("role", "http://www.loc.gov/loc.terms/relators/AUT"));
		for(Creator person : this.creatorList){
			creator.addContent(person.toElementXMLESciDoc(mode));
		}
		root.addContent(creator);
		
		Element title = new Element("title", NS_DC);
		title.addContent(this.title);
		root.addContent(title);
		
		for(String s : this.alternativeList){
			Element alternative = new Element("alternative", NS_DCTERMS);
			alternative.addContent(s);
			root.addContent(alternative);
		}
		
		root.addContent(this.publishing.toElementXMLESciDoc(mode));
		
		
		//<dcterms:issued xmlns:dcterms="http://purl.org/dc/terms/"
		//	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="dcterms:W3CDTF">
		Element issued = new Element("issued", NS_DCTERMS);
		issued.addNamespaceDeclaration(NS_XSI);
		issued.setAttribute(new Attribute("type", "dcterms:W3CDTF", NS_XSI));
		issued.addContent(this.issued);
		root.addContent(issued);
		
		
		Element numberOfPages = new Element("total-number-of-pages", NS_ETERMS);
		numberOfPages.addContent(this.numberOfPages);
		root.addContent(numberOfPages);
		
		Element subject = new Element("subject", NS_DCTERMS);
		subject.addContent(this.subject);
		root.addContent(subject);
		
		return root;
	}
	

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publication)) {
            return false;
        }
        Publication other = (Publication) object;
        
        if(this.objid != null && other.getObjid() != null && this.objid == other.objid)
        	return true;
        
        return false;
    }

	
	
	@Override
	public String toString(){
		return "Publication [objid="+objid+
		", title="+ title +
		", subject="+  subject +
		", " + creatorList + 
		", " + publishing + "]";
	}

	@Override
	public int compareTo(Publication arg0) {
		if(StringUtils.isNotEmpty(this.objid) && StringUtils.isNotEmpty(arg0.objid)){
			return this.objid.compareTo(arg0.objid);
		}else if(StringUtils.isNotEmpty(this.objid)){
			return -1;
		}else if(StringUtils.isNotEmpty(arg0.objid)){
			return 1;
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
			outputter1.output(pub.toXMLESciDoc(null), System.out);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
