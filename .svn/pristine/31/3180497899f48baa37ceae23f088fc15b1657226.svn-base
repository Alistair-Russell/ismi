package org.mpiwg.itgroup.escidoc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.mpiwg.itgroup.escidoc.bo.Creator;
import org.mpiwg.itgroup.escidoc.bo.ESciDocConstants;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.Publication;
import org.mpiwg.itgroup.escidoc.bo.Publishing;

public class ESciDocClient extends AbstractClient implements Serializable{

	private static final long serialVersionUID = 4982908578268938883L;
	public static String GET_ITEM = "http://escidoc.mpiwg-berlin.mpg.de:8080/ir/item/";
	public static String GET_CONTEXT = "http://escidoc.mpiwg-berlin.mpg.de:8080/ir/context/";
	public static String PUT_ITEM = GET_ITEM;
	public static String CREATE_ITEM = GET_ITEM;

	public static String ISMI_CONTEXT_ID = "escidoc:79281";
	private static String ENCODING = "UTF-8";

	public static String host = 
		"http://escidoc.mpiwg-berlin.mpg.de:8080/srw/search/escidoc_all?operation=searchRetrieve&maximumRecords=MAX_RECORDS&query=";

	public static String queryGetAll = 
		"escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid="
			+ ISMI_CONTEXT_ID;

	public static String queryGetObjectById = "escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid="
			+ ISMI_CONTEXT_ID + " and escidoc.objid=";

	public static String queryByTitle = "escidoc.content-model.objid=escidoc:persistent4 and escidoc.context.objid="
			+ ISMI_CONTEXT_ID + " and escidoc.any-title:=";

	static {
		try {
			// PUT_ITEM = URLEncoder.encode(PUT_ITEM, ENCODING);
			queryGetAll = URLEncoder.encode(queryGetAll, ENCODING);
			queryGetObjectById = URLEncoder
					.encode(queryGetObjectById, ENCODING);
			queryByTitle = URLEncoder.encode(queryByTitle, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public ESciDocClient(String eScidocServer, int eScidocPort, String user,
			String password) {
		super(eScidocServer, eScidocPort, user, password);
	}

	public void deleteItem(String objId) throws Exception {
		eScidocDelete("/ir/item/" + objId, null);
		return;
	}
	
	
	
	public ESciDocItem updateItem(ESciDocItem item) throws Exception {
		String body = new XMLOutputter().outputString(item.toXMLESciDoc(UPDATE));
		byte[] data = body.getBytes("UTF-8");
		return this.eScidocPut("/ir/item/" + item.getObjId(), new ByteArrayInputStream(data));
	}
	
	public ESciDocItem createItem(ESciDocItem item) throws Exception {
		if(item.getObjId() != null){
			throw new Exception("Trying to create a item that has already an object id.");
		}
		String body = new XMLOutputter().outputString(item.toXMLESciDoc(CREATE));
		//System.out.println("----------------- INPUT for CREATE");
		//System.out.println(body);
		//System.out.println("-----------------");
		byte[] data = body.getBytes("UTF-8");
		return this.eScidocPut("/ir/item", new ByteArrayInputStream(data));
	}

	/**
	 * 
	 * @param item
	 * @param comment
	 * @return the modification date of this action that should be updated in the cache
	 * @throws Exception
	 */
	public String releaseItem(ESciDocItem item, String comment) throws Exception {
		return this.executeAction(item, comment, "release");
	}
	
	/**
	 * 
	 * @param item
	 * @param comment
	 * @return the modification date of this action that should be updated in the cache
	 * @throws Exception
	 */
	public String submitItem(ESciDocItem item, String comment) throws Exception {
		return this.executeAction(item, comment, "submit");
	}	
	
	public String reviseItem(ESciDocItem item, String comment) throws Exception {
		return this.executeAction(item, comment, "revise");
	}
	
	public String withdrawItem(ESciDocItem item, String comment) throws Exception {
		return this.executeAction(item, comment, "withdraw");
	}	
	
	private String executeAction(ESciDocItem item, String comment, String action) throws Exception{
		Element param = new Element("param");
		param.setAttribute(new Attribute("last-modification-date", item.getLastModificationDate()));
		Document doc = new Document(param);
		
		Element eComment = new Element("comment");
		if(StringUtils.isNotEmpty(comment)){
			eComment.addContent(comment);
		}
		param.addContent(eComment);
		
		String body = new XMLOutputter().outputString(doc);
		byte[] data = body.getBytes("UTF-8");
		return this.eScidocPost("/ir/item/" + item.getObjId() + "/" + action, new ByteArrayInputStream(data));
	}
	
	public static List<ESciDocItem> getAllItems(Integer maxRecords) throws Exception {
		String host0 = host.replace("MAX_RECORDS", maxRecords.toString());
		System.out.println("getAllPublications=" + host0 + queryGetAll);
		URI uri = new URI(host0 + queryGetAll);
		URL url = uri.toURL();
		InputStream in = url.openStream();
		Document doc = new SAXBuilder().build(in);
		return getItemsFromDoc(doc);
	}

	public ESciDocItem getItemX(String objId) throws IOException, JDOMException, ESciDocException, Exception{
		return eScidocGet("/ir/item/" + objId);
		
	}
	
	public static ESciDocItem getItem(String escidocId) throws Exception {
		return getItemFromDoc(getItem0(escidocId));
	}

	private static Document getItem0(String escidocId) throws Exception {
		URL url = new URL(GET_ITEM + escidocId);
		System.out.println("getItem0= " + GET_ITEM + escidocId);
		
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setRequestMethod("GET");

		InputStreamReader in = new InputStreamReader(httpCon.getInputStream(), ENCODING);

		Document doc = new SAXBuilder().build(in);
		return doc;
	}
	
	public static void testGet() {
		try {
			ESciDocItem item = getItem("escidoc:127561");
			System.out.println(new XMLOutputter().outputString(item
					.toXMLESciDoc(null)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testGetAndSave() {
		try {
			
			//ESciDocItem item = getItem("escidoc:127561");
			
			//System.out.println(new XMLOutputter().outputString(item.toXMLESciDoc(UPDATE)));
			
			ESciDocClient client = new ESciDocClient("escidoc.mpiwg-berlin.mpg.de", 8080, "jurzua", "221082");
			
			ESciDocItem item = client.getItemX("escidoc:127561");
			
			System.out.println(item.getLastModificationDate());
			
			//item.getPublication().setTitle(item.getPublication().getTitle() + " test");
			//item.getPublication().setTitle(item.getPublication().getTitle().replace(" test", ""));
			
			//client.updateItem(item);
			//client.submitItem(item, "test");
			//item = client.getItemX("escidoc:127561");
			client.releaseItem(item, "test");
			
			
			
			//System.out.println(new XMLOutputter().outputString(item0
			//		.toElementXMLESciDoc(UPDATE)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ESciDocClient.testGetAndSave();
	}

}
