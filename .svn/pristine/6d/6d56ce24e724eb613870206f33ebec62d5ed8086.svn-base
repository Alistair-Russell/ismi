package org.mpiwg.itgroup.escidoc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;

public class Test {

	//private String eScidocServer;
	//private int eScidocPort;
	private String user;
	private String password;
	public String eScidocUrl;
	private HttpClient httpclient = null;

	public Test(String eScidocServer, int eScidocPort, String user,
			String password) {
		// this.eScidocServer=eScidocServer;
		// this.eScidocPort=eScidocPort;
		this.user = user;
		this.password = password;
		this.eScidocUrl = "http://" + eScidocServer + ":" + String.valueOf(eScidocPort);
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

	public HttpResponse eScidocPut(String command, InputStream body)
			throws IOException {
		HttpPut httpput = new HttpPut(eScidocUrl + command);
		return eScidocRequestBase(httpput, command, body);
	}

	private HttpResponse eScidocRequestBase(HttpRequestBase httpBase, String command, InputStream body) throws IOException {
		if (httpclient == null)
			login();

		if (HttpEntityEnclosingRequestBase.class.isInstance(httpBase)) {
			if (body != null) {
				HttpEntity entity = new InputStreamEntity(body, -1);
				((HttpEntityEnclosingRequestBase) httpBase).setEntity(entity);
			}
		}
		
		HttpResponse status = httpclient.execute(httpBase);
		return status;
	}
	
	public static Document testGetAndSave(){
		try {
			ESciDocItem item = ESciDocClient.getItem("escidoc:127561");
			//System.out.println(new XMLOutputter().outputString(item.toXMLESciDoc(null)));
			
			return item.toXMLESciDoc(ESciDocClient.UPDATE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	public static void main(String[] args) throws IOException {
		/*
		Document doc = Test.testGetAndSave();
		
		String body = new XMLOutputter().outputString(doc);
		byte[] data = body.getBytes("UTF-8");
		
		Test test = new Test("escidoc-test.mpiwg-berlin.mpg.de", 8080, "jurzua", "221082");
		test.eScidocPut("/ir/item/escidoc:127561", new ByteArrayInputStream(data));
		*/
		
		String s = new String("CODEX:distinguishing_features&CODEX&notes_on_ownership&CODEX:notes");
		String[] array = s.split("&");
		System.out.println(array.length);
	}

}
