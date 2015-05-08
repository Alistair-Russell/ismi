package org.mpiwg.itgroup.escidoc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.mpiwg.itgroup.escidoc.bo.Creator;
import org.mpiwg.itgroup.escidoc.bo.ESciDocConstants;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.Publication;
import org.mpiwg.itgroup.escidoc.bo.Publishing;

public class AbstractClient extends ESciDocConstants implements Serializable{
	
	private static final long serialVersionUID = 8133780898394554218L;
	
	private String user;
	private String password;
	public String eScidocUrl;
	private HttpClient httpclient = null;

	public AbstractClient(String eScidocServer, int eScidocPort, String user,
			String password) {
		this.user = user;
		this.password = password;
		this.eScidocUrl = "http://" + eScidocServer + ":"
				+ String.valueOf(eScidocPort);
	}

	public HttpClient login() throws IOException {
		httpclient = new DefaultHttpClient();

		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		HttpPost httppost = new HttpPost(eScidocUrl + "/aa/login?target=/");

		HttpResponse response = httpclient.execute(httppost);
		// HttpEntity entity = httppost.getRes

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			entity.consumeContent();
		}

		HttpGet httpget = new HttpGet(eScidocUrl
				+ "/aa/j_spring_security_check?j_username=" + user
				+ "&j_password=" + password);

		response = httpclient.execute(httpget);
		// entity = response.getEntity();

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());

		entity = response.getEntity();
		if (entity != null) {
			entity.consumeContent();
		}

		// entity.consumeContent();
		return httpclient;
	}

	public HttpClient getHttpClient() throws IOException {
		//if (httpclient == null) {
		//how can be detected that the timeout of the session???
		this.login();
		//}
		return httpclient;
	}

	public ESciDocItem eScidocPut(String command, InputStream body)
			throws IOException, JDOMException, ESciDocException, Exception {
		//create PUT /ir/item return item
		//update PUT /ir/item/<item-id> return item
		System.out.println("PUT " + eScidocUrl + command);
		HttpPut httpput = new HttpPut(eScidocUrl + command);
		return getItemFromDoc(eScidocRequestBase(httpput, command, body));
	}
	
	public String eScidocPost(String command, InputStream body)
			throws IOException, JDOMException, ESciDocException, Exception {
		///ir/item/<item-id>/submit last-modification-date
		///ir/item/<item-id>/release last-modification-date within XML (result.xsd)
		System.out.println("----------------------");
		System.out.println("POST " + eScidocUrl + command);
		HttpPost httppost = new HttpPost(eScidocUrl + command);
		
		return getLastModificationDate(eScidocRequestBase(httppost, command, body));
	}
	
	public void eScidocDelete(String command, InputStream body)
			throws IOException, JDOMException, ESciDocException, Exception {
		//DELETE /ir/item/<item-id>
		System.out.println("----------------------");
		System.out.println("DELETE " + eScidocUrl + command);
		HttpDelete httppost = new HttpDelete(eScidocUrl + command);
		
		eScidocRequestBase(httppost, command, body);
		return;
	}

	public ESciDocItem eScidocGet(String command) throws IOException,
			JDOMException, ESciDocException, Exception {
		System.out.println("GET " + eScidocUrl + command);
		HttpGet httpget = new HttpGet(eScidocUrl + command);
		Document doc = eScidocRequestBase(httpget, command, null);
		//System.out.println(new XMLOutputter().outputString(doc));
		return getItemFromDoc(doc);
	}

	private Document eScidocRequestBase(HttpRequestBase httpBase, String command,
			InputStream body) throws Exception {

		if (HttpEntityEnclosingRequestBase.class.isInstance(httpBase)) {
			if (body != null) {
				HttpEntity entity = new InputStreamEntity(body, -1);
				((HttpEntityEnclosingRequestBase) httpBase).setEntity(entity);
			}
		}

		HttpResponse status = getHttpClient().execute(httpBase);

		InputStream in = status.getEntity().getContent();
		Document doc = new SAXBuilder().build(in);
		
		//System.out.println("Resonse output for " + command + ":");
		//System.out.println(new XMLOutputter().outputString(doc));

		// the response can be an exception for the cases UPDATE and GET
		// The response can be a Item for GET

		isReponseException(doc);

		return doc;
	}

	public static ESciDocItem getItemFromDoc(Document doc) throws Exception {
		List<ESciDocItem> list = getItemsFromDoc(doc);
		if (!list.isEmpty())
			return list.get(0);
		return null;
	}

	public static List<ESciDocItem> getItemsFromDoc(Document doc)
			throws Exception {
		List<ESciDocItem> list = new ArrayList<ESciDocItem>();

		XPath xp = XPath.newInstance(".//escidocItem:item");
		xp.addNamespace("escidocItem", "http://www.escidoc.de/schemas/item/0.9");

		List<Object> inputList = xp.selectNodes(doc);
		for (Object obj : inputList) {
			if (obj instanceof Element) {
				Element eItem = (Element) obj;

				
				
				ESciDocItem escidocItem = new ESciDocItem();
				// FIXME: sometimes this is not the last version, therefore....
				escidocItem.setLastModificationDate(eItem.getAttributeValue("last-modification-date"));
				addVersionToItem(eItem, escidocItem);
				addPublicStatusItem(eItem, escidocItem);
				addLatestVersionToItem(eItem, escidocItem);
				addLatestRelease(eItem, escidocItem);

				escidocItem.setPublication(getPubFromXMLElement(eItem));
				Attribute objIdAtt = eItem.getAttribute("objid");
				String objId = (objIdAtt != null) ? objIdAtt.getValue() : null;
				
				if(StringUtils.isEmpty(objId)){
					//xlink:href="/ir/item/escidoc:127561"
					objIdAtt = eItem.getAttribute("href", NS_XLINK);
					objId = (objIdAtt != null) ? objIdAtt.getValue() : "";
					objId = objId.replace("/ir/item/", "");
				}
				escidocItem.getPublication().setObjid(objId);
				
				list.add(escidocItem);
			} else {
				System.out.println(obj);
			}
		}

		return list;
	}
	
	public static void addPublicStatusItem(Element eItem, ESciDocItem item){
		Element props = eItem.getChild("properties", NS_ESCIDOC_ITEM);
		Element publicStatus = props.getChild("public-status", NS_PROP);
		
		item.setPublicStatus((publicStatus != null) ? publicStatus.getValue() : null);
	}
	
	public static void addVersionToItem(Element eItem, ESciDocItem item){
		Element props = eItem.getChild("properties", NS_ESCIDOC_ITEM);
		Element version = props.getChild("version", NS_PROP);
		
		if(version != null){
			String value = (version.getChild("number", NS_VERSION) != null) ? version.getChild("number", NS_VERSION).getValue() : null;
			item.setVersionNumber(value);
			
			value = (version.getChild("date", NS_VERSION) != null) ? version.getChild("date", NS_VERSION).getValue() : null;
			item.setVersionDate(value);
			
			value = (version.getChild("status", NS_VERSION) != null) ? version.getChild("status", NS_VERSION).getValue() : null;
			item.setVersionStatus(value);	
		}
	}

	public static void addLatestVersionToItem(Element eItem, ESciDocItem item){
		Element props = eItem.getChild("properties", NS_ESCIDOC_ITEM);
		Element latestVersion = props.getChild("latest-version", NS_PROP);
		
		//Attribute hrefAtt = latestVersion.getAttribute("href", NS_XLINK);
		//putting into item
		item.setLatestVersionNumber(latestVersion.getChild("number", NS_VERSION).getValue());
		item.setLatestVersionDate(latestVersion.getChild("date", NS_VERSION).getValue());
		//item.setLatestVersionHref(hrefAtt.getValue());
	}
	
	
	
	private static void addLatestRelease(Element eItem, ESciDocItem item){
		Element props = eItem.getChild("properties", NS_ESCIDOC_ITEM);
		Element latestRelease = props.getChild("latest-release", NS_PROP);
		
		if(latestRelease != null){
			//Attribute hrefAtt = latestRelease.getAttribute("href", NS_XLINK);
			
			//putting into item
			item.setLatestReleaseNumber(latestRelease.getChild("number", NS_RELEASE).getValue());
			item.setLatestReleaseDate(latestRelease.getChild("date", NS_RELEASE).getValue());
			//item.setLatestReleasePid(latestRelease.getChild("pid", NS_RELEASE).getValue());
			//item.setLatestReleaseHref(hrefAtt.getValue());	
		}
	}
	
	private void isReponseException(Document doc)
			throws JDOMException, IOException, ESciDocException {

		XPath xp = XPath.newInstance(".//exception");
		xp.addNamespace("escidocItem", "http://www.escidoc.de/schemas/item/0.9");

		List<Object> inputList = xp.selectNodes(doc);
		if (!inputList.isEmpty()) {
			throw new ESciDocException((Element) inputList.get(0));
		}
	}
	
	private String getLastModificationDate(Document doc) throws JDOMException{
		
		if(doc.getRootElement().getName().equals("result")){
			Attribute lastModif = doc.getRootElement().getAttribute("last-modification-date");
			if(lastModif != null){
				return lastModif.getValue();
			}
		}
		return null;
	}

	protected static Publication getPubFromXMLElement(Element item) {

		Publication reference = new Publication(getObjId(item.getAttribute(
				"href", NS_XLINK)));

		Element md_records = item.getChild("md-records", NS_ESCIDOC_MD_RECORDS);
		Element md_record = md_records.getChild("md-record", NS_ESCIDOC_MD_RECORDS);
		Element publication = md_record.getChild("publication", NS_PUBLICATION);
		
		List<Object> creatorList = publication.getChildren("creator", NS_ETERMS);
		Element title = publication.getChild("title", NS_DC);
		List<Object> alternativeList = publication.getChildren("alternative", NS_DCTERMS);
		Element publishing_info = publication.getChild("publishing-info", NS_ETERMS);
		Element issued = publication.getChild("issued", NS_DCTERMS);
		Element totalNumberOfPages = publication.getChild("total-number-of-pages", NS_ETERMS);
		
		//<dcterms:subject xmlns:dcterms="http://purl.org/dc/terms/">
		Element subject = publication.getChild("subject", NS_DCTERMS);

		// Filling the reference

		for (Object object : creatorList) {
			if (object instanceof Element) {
				Element creator = (Element) object;
				Element person = creator.getChild("person", NS_PERSON);

				if (person != null) {
					Element complete_name = person.getChild("complete-name",
							NS_ETERMS);
					Element family_name = person.getChild("family-name",
							NS_ETERMS);
					Element given_name = person.getChild("given-name",
							NS_ETERMS);
					Element organization = person.getChild("organization",
							NS_ORGANIZATION);

					reference.addCreator(new Creator(
							(complete_name != null) ? complete_name.getText()
									: "", (family_name != null) ? family_name
									.getText() : "",
							(given_name != null) ? given_name.getText() : ""));
				}
			}
		}

		reference.setTitle((title != null) ? title.getText() : "");

		for (Object object : alternativeList) {
			if (object instanceof Element) {
				Element alternative = (Element) object;
				reference.addAlternative(alternative.getText());
			}
		}

		if (publishing_info != null) {
			Element publisher = publishing_info.getChild("publisher", NS_DC);
			Element place = publishing_info.getChild("place", NS_ETERMS);
			Element edition = publishing_info.getChild("edition", NS_ETERMS);

			reference.setPublishing(new Publishing(
					(publisher != null) ? publisher.getText() : "",
					(place != null) ? place.getText() : "",
					(edition != null) ? edition.getText() : ""));
		}

		reference.setIssued
			((issued != null) ? issued.getText() : "");
		reference.setNumberOfPages
			((totalNumberOfPages != null) ? totalNumberOfPages.getText() : "");
		reference.setSubject
			((subject != null) ? subject.getText() : "");

		return reference;
	}

	private static String getObjId(Attribute att) {
		// new Attribute("href", "/ir/item/"+ publication.getObjid()
		// +"/md-records", NS_XLINK)
		if (att != null) {
			String objId = att.getValue().replace("/ir/item/", "");
			String[] array = objId.split(":");
			if (array.length == 3) {
				return array[0] + ":" + array[1];
			}
			return objId;
		}
		return null;
	}
}
