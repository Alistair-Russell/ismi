package org.mpiwg.itgroup.escidoc.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mpi.openmind.repository.utils.NormalizerUtils;

public class Creator extends ESciDocConstants implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	//completeName familyName givenName
	
	private String completeName;
	private String familyName;
	private String givenName;
	
	private String norCompleteName;
	private String norFamilyName;
	private String norGivenName;
	
	public Creator(String completeName, String familyName, String givenName){
		this.setCompleteName(completeName);
		this.setFamilyName(familyName);
		this.setGivenName(givenName);
	}
	
	public String getCompleteName() {
		return completeName;
	}
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
		if(StringUtils.isEmpty(completeName)){
			this.norCompleteName = new String();
		}else{
			this.norCompleteName = NormalizerUtils.normalize(completeName);
		}
		
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
		if(StringUtils.isEmpty(familyName)){
			this.norFamilyName = new String();
		}else{
			this.norFamilyName = NormalizerUtils.normalize(familyName);
		}
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
		if(StringUtils.isEmpty(givenName)){
			this.norGivenName = new String();
		}else{
			this.norGivenName = NormalizerUtils.normalize(givenName);
		}
	}
	
	public String getNorCompleteName() {
		return norCompleteName;
	}

	public String getNorFamilyName() {
		return norFamilyName;
	}

	public String getNorGivenName() {
		return norGivenName;
	}

	/**
	 * term must be normalized before calling this method
	 * @param term
	 * @return
	 */
	public boolean containsTerm(String term){
		String secondTerm = this.getNorCompleteName();
		
		if(secondTerm.contains(term))
			return true;
		
		secondTerm = this.getNorFamilyName();
		if(secondTerm.contains(term))
			return true;
		
		secondTerm = this.getNorGivenName();
		if(secondTerm.contains(term))
			return true;
		
		return false;
		
	}
	
	@Override
	public Element toElementXMLESciDoc(Integer mode){
		Element root = new Element("person", NS_PERSON);
		
		Element completeName = new Element("complete-name", NS_ETERMS);
		Element familyName = new Element("family-name", NS_ETERMS);
		Element givenName = new Element("given-name", NS_ETERMS);
		
		completeName.addContent(this.completeName);
		familyName.addContent(this.familyName);
		givenName.addContent(this.givenName);
		
		root.addContent(completeName);
		root.addContent(familyName);
		root.addContent(givenName);
				
		Element organization = new Element("organization", NS_ORGANIZATION);
		
		Element orgTitle = new Element("title", NS_DC);
		orgTitle.addContent("Max Planck Society");
		Element orgIdentifier = new Element("identifier", NS_DC);
		
		organization.addContent(orgTitle);
		organization.addContent(orgIdentifier);
		
		root.addContent(organization);
		
		return root;
	}
	
	public static void main(String[] args){
		try{
			Creator c = new Creator("Jorge Urzua", "Urzua", "Jorge");
			XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
			outputter1.output(c.toXMLESciDoc(null), System.out);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String toString(){
		
		return "Creator [completeName="+ completeName +", familyName="+ familyName +", givenName=" + givenName + "]";
	}

}
