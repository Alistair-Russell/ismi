package de.mpiwg.itgroup.dm2e.utils;

import java.net.URLEncoder;

import org.apache.commons.lang.RandomStringUtils;

public class DM2EUtils {

	public static String encodeString(String toURI) throws Exception {
		toURI = toURI.replaceAll("/|:|<|>|,", "_");
		toURI = URLEncoder.encode(toURI, "UTF-8");
		return toURI.replace("+", "_");
	}
	
	public static String generateID(){
		return RandomStringUtils.randomAlphabetic(1) + RandomStringUtils.randomAlphanumeric(6);
	} 
}