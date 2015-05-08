package de.mpiwg.itgroup.ismi.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class AbstractServletMethod {

	public static Long getLong(HttpServletRequest request, String name){
		Long value = null;
		try{
			String s = request.getParameter(name);
			value = new Long(s);
		}catch (Exception e) {
		}
		return value;
	}
	
	public static String getString(HttpServletRequest request, String name){
		String value = null;
		try{
			value = request.getParameter(name);
		}catch (Exception e) {
		}
		return value;
	}
	
	public static Boolean getBoolean(HttpServletRequest request, String name){
		Boolean value = false;
		try{
			String s = request.getParameter(name);
			value = new Boolean(s);
		}catch (Exception e) {
		}
		return value;
	}
	
	public static Integer getInteger(HttpServletRequest request, String name){
		Integer value = null;
		try{
			String s = request.getParameter(name);
			value = new Integer(s);
		}catch (Exception e) {
		}
		return value;
	}
	
	public static List<Long> getLongList(HttpServletRequest request, String name){
		List<Long> list = new ArrayList<Long>();
		String s = request.getParameter(name);
		String[] array = s.split("[|]");
		for(String sID : array){
			try{
				Long id = new Long(sID);
				list.add(id);
			}catch (Exception e) {}
		}
		return list;
	}
}
