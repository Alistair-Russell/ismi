package de.mpiwg.itgroup.ismi.servlets.testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;


public class Performance {
	
	private static final int BUFFER_SIZE = 1024;
	
	public static void executeWitnessTest(Long id){
		
		try {			
			getText(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void getText(Long textId) throws Exception {
		
		//System.out.println(json.getString("ent"));
		
		Entity text = getEnt(textId);
		
		System.out.println(text.toString());
		
		getWitness(text.getId());
	}
	
	private static void getWitness(Long textId) throws Exception {
		
		List<Entity> witnessList = getSrcs4TarRel(textId, "is_exemplar_of", "WITNESS");
		for(Entity w : witnessList){
			//System.out.println(w.toString());
			getCodex(w.getId());
		}
	}
	
	private static void getCodex(Long witnessId) throws Exception {
		//get_tars_4_src_rel&id=witnessId&rel_name=is_part_of&tar_oc=CODEX
		
		List<Entity> codexList = getTars4SrcRel(witnessId, "is_part_of", "CODEX");
		for(Entity codex : codexList){
			//System.out.println(codex.toString());
			getCollection(codex.getId());
		}
	}
	
	private static void getCollection(Long codexId) throws Exception {
		//get_tars_4_src_rel&id=codexId&rel_name=is_part_of&tar_oc=COLLECTION
		
		List<Entity> collList = getTars4SrcRel(codexId, "is_part_of", "COLLECTION");
		for(Entity coll : collList){
			//System.out.println(coll.toString());
			getRepository(coll.getId());
		}
		
	}
	
	private static void getRepository(Long collectionId) throws Exception {
		//get_tars_4_src_rel&id=collectionId&rel_name=is_part_of&tar_oc=REPOSITORY
		
		List<Entity> repList = getTars4SrcRel(collectionId, "is_part_of", "REPOSITORY");
		for(Entity rep : repList){
			System.out.println(rep.toString());
		}
	}
	
	private static void getPlace(Long repositoryId) throws Exception {
		//get_tars_4_src_rel&id=repositoryId&rel_name=is_in&tar_oc=PLACE
	}
	
	private static Entity getEnt(Long id) throws Exception{
		JSONObject json = jsonFromURL("get_ent&id=" + id + "&include_content=true");
		return JSONUtils.jsonToEntity(json.getJSONObject("ent")); 
	}
	
	private static List<Entity> getTars4SrcRel(Long id, String relName, String tarOC) throws Exception{
		List<Entity> rs = new ArrayList<Entity>();
		JSONObject json = jsonFromURL("get_tars_4_src_rel&id=" + id + "&rel_name=" + relName + "&tar_oc=" + tarOC);
		
		JSONArray jsonArray = json.getJSONArray("ents");
		for(int i =0; i<jsonArray.length(); i++){
			rs.add(JSONUtils.jsonToEntity(jsonArray.getJSONObject(i)));
		}
		return rs;
	}
	
	private static List<Entity> getSrcs4TarRel(Long id, String relName, String srcOC) throws Exception{
		List<Entity> rs = new ArrayList<Entity>();
		JSONObject json = jsonFromURL("get_srcs_4_tar_rel&id=" + id + "&rel_name=" + relName + "&src_oc=" + srcOC);
		
		JSONArray jsonArray = json.getJSONArray("ents");
		for(int i =0; i<jsonArray.length(); i++){
			rs.add(JSONUtils.jsonToEntity(jsonArray.getJSONObject(i)));
		}
		return rs;
	}
	
	
	//get a BufferedReader for a webpage from its URL
	public static BufferedReader read(URL url) throws Exception {
		return new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
	}
	
	public static JSONObject jsonFromURL(String s) throws Exception {
		String url = "https://openmind-ismi-dev.mpiwg-berlin.mpg.de/om4-ismi/jsonInterface?method=" + s;
		
		System.out.println(url);
		trustAll();
		URL realURL = new URL(url);
		BufferedReader reader = read(realURL);
		int charsRead;
		char[] copyBuffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();
		while ((charsRead = reader.read(copyBuffer, 0, BUFFER_SIZE)) != -1)
			sb.append(copyBuffer, 0, charsRead);
		
		return new JSONObject(sb.toString());
	}
	//allows us to access https connections without verifying the certificate
	public static void trustAll() throws Exception {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
		};
		
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Performance.executeWitnessTest(new Long(444596));

	}

}
