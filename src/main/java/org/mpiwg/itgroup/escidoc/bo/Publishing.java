package org.mpiwg.itgroup.escidoc.bo;

import java.io.Serializable;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Publishing extends ESciDocConstants implements Serializable{
	private static final long serialVersionUID = 1L;

	private String publisher;
	private String place;
	private String edition;
	
	public Publishing(String publisher, String place, String edition){
		this.publisher = publisher;
		this.place = place;
		this.edition = edition;
	}
	
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	@Override
	public Element toElementXMLESciDoc(Integer mode){
		Element root = new Element("publishing-info", NS_ETERMS);
		Element publisher = new Element("publisher", NS_DC);
		Element place = new Element("place", NS_ETERMS);
		Element edition = new Element("edition", NS_ETERMS);
		
		publisher.addContent(this.publisher);
		place.addContent(this.place);
		edition.addContent(this.edition);
		
		root.addContent(publisher);
		root.addContent(place);
		root.addContent(edition);
		
		return root;
	}
	
	@Override
	public String toString(){
		return "Publishing [publisher="+ publisher +", place="+ place +", edition="+ edition +"]";
	}
	
	public static void main(String[] args){
		try{
			Publishing c = new Publishing("Jorge Urzua", "Chile", "JIJI");
			XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
			outputter1.output(c.toXMLESciDoc(null), System.out);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
