package de.mpiwg.itgroup.dm2e;

import java.net.URLEncoder;

import org.apache.commons.lang.RandomStringUtils;

public class DM2EUtils {
	
	public static String encodeString(String toURI) throws Exception {
		toURI = toURI.replaceAll("/|:|<|>|,","_");
		toURI = URLEncoder.encode(toURI,"UTF-8");
		toURI = toURI.replace("+", "_");
		toURI = toURI.replace("__", "_");
		
		return toURI;
	}
	
	public static String generateID(){
		return RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.randomAlphanumeric(6);
	} 
}
