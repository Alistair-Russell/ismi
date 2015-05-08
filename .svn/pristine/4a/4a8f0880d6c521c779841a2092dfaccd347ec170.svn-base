package de.mpiwg.itgroup.ismi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HTTPUtils {


	public static Document getDocument(String link){
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(link);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getResponse(String link) throws IOException{
        
		URL url = new URL(link);
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                uc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) 
            sb.append(inputLine);
        in.close();
        
        return null;
	}
	
	
    final static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
        }
        @Override
        public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } };

	public static HttpResponse getHttpSSLResponse(String link) throws IOException, KeyManagementException, NoSuchAlgorithmException{
	    
	    // Install the all-trusting trust manager
	    final SSLContext sslContext = SSLContext.getInstance( "SSL" );
	    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
	    // Create an ssl socket factory with our all-trusting manager
	    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

	    /////
		
		URL url = new URL(link);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		
		( (HttpsURLConnection) httpConn ).setSSLSocketFactory( sslSocketFactory );

        
		InputStream in;
        if (httpConn.getResponseCode() >= 400) {
        	in = httpConn.getErrorStream();
        } else {
        	in = httpConn.getInputStream();
        }        
        
        return new HttpResponse(httpConn.getResponseCode(), in);
	}
    
    
	public static HttpStringResponse getHttpSSLStringResponse(String link) throws IOException, KeyManagementException, NoSuchAlgorithmException{
	    
	    // Install the all-trusting trust manager
	    final SSLContext sslContext = SSLContext.getInstance( "SSL" );
	    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
	    // Create an ssl socket factory with our all-trusting manager
	    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		
		URL url = new URL(link);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		
		( (HttpsURLConnection) httpConn ).setSSLSocketFactory( sslSocketFactory );

        
        BufferedReader in = null;
        if (httpConn.getResponseCode() >= 400) {
        	in = new BufferedReader(
                    new InputStreamReader(
                    		httpConn.getErrorStream()));
        } else {
        	in = new BufferedReader(
                    new InputStreamReader(
                    		httpConn.getInputStream()));
        }
        
        
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) 
        	sb.append(inputLine + "\n");
        in.close();
        
        return new HttpStringResponse(httpConn.getResponseCode(), sb.toString());
	}
	
	public static HttpStringResponse getHttpStringResponse(String link) throws IOException{
        
		//System.out.println(link);
		
		URL url = new URL(link);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        
        BufferedReader in = null;
        if (httpConn.getResponseCode() >= 400) {
        	in = new BufferedReader(
                    new InputStreamReader(
                    		httpConn.getErrorStream()));
        } else {
        	in = new BufferedReader(
                    new InputStreamReader(
                    		httpConn.getInputStream()));
        }
        
        
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) 
        	sb.append(inputLine + "\n");
        in.close();
        
        return new HttpStringResponse(httpConn.getResponseCode(), sb.toString());
	}
	
	public static class HttpStringResponse{
		public int code;
		public String content;
		public HttpStringResponse(int code, String content){
			this.code = code;
			this.content = content;
		}
	}

	public static class HttpResponse{
		public int code;
		public InputStream content;
		public HttpResponse(int code, InputStream content){
			this.code = code;
			this.content = content;
		}
	}
}
