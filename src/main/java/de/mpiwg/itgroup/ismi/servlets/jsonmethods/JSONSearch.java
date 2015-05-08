package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONSearch extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			String query = getString(request, "query");
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(StringUtils.isEmpty(query)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "Parameter 'query' is mandatory and should be a number.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'query' is mandatory and should be a number.");
			}else{
				if(query.startsWith("/")){
					query = query.replaceFirst("/", "");
				}
				String[] queryArray = query.split("/");
				
				
				boolean idAtBeginning = true;
				List<Long> idList = getIdListFromString(queryArray[0]);
				if(idList.isEmpty()){
					idList = getIdListFromString(queryArray[queryArray.length-1]);
					idAtBeginning = false;
				}			
				
				
				Deque<String> pathQueue;
				if(idAtBeginning){
					pathQueue = getPathExcludingBeginning(queryArray);
					searchFromLeft(json, idList, pathQueue, false, ws, includeRomanization);
				}else{
					pathQueue = getPathExcludingEnd(queryArray);
					searchFromLeft(json, idList, pathQueue, true, ws, includeRomanization);
				}					
			}		
	
		}catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			e.printStackTrace();
		}finally{
			json = finallyExecution(json, parametersToString(request.getParameterMap()), startExecution);
		}
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		
	}

	
	public static void searchFromLeft(JSONObject jsonResult, List<Long> idList, Deque<String> pathQueue, boolean fromRight, 
				WrapperService ws,
				boolean includeRomanization) throws Exception{
		
		JSONArray rsArray = new JSONArray();
		JSONArray idNoFoundArray = new JSONArray();
		int count = 0;
		for(Long rootId : idList){
			Entity root = ws.getLightweightEntityById(rootId);
			if(root != null){
				
				List<Deque<Entity>> rs = null;
				if(fromRight){
					rs = searchFromRight(new LinkedList<String>(pathQueue), root, ws);
				}else{
					rs = searchFromLeft(new LinkedList<String>(pathQueue), root, ws);	
				}				
				
				for(Deque<Entity> entry : rs){
					JSONArray entryArray = new JSONArray();
					while(!entry.isEmpty()){
						Entity ent = (fromRight) ? entry.removeFirst() : entry.removeLast();
						entryArray.put(JSONUtils.entityToJSON(ent, includeRomanization));
					}				
					rsArray.put(entryArray);
					count++;
				}
			}else{
				System.err.println("JSONGetPath - Entity no found - ID=" + rootId);
				idNoFoundArray.put(rootId);
			}
		}
		jsonResult.put("rs", rsArray);
		jsonResult.put("ids_no_found", idNoFoundArray);
		jsonResult.put("rs_size", count);
	}
	
	public static List<Deque<Entity>> searchFromRight(Deque<String> PathQueue, Entity tar, WrapperService ws){
		List<Deque<Entity>> rs = new ArrayList<Deque<Entity>>();
		
		if(!PathQueue.isEmpty()){
			String relName = PathQueue.pollLast();
			String srcOC = PathQueue.pollLast();
			
			List<Entity> srcs = ws.getSourcesForTargetRelation(tar, relName, srcOC, -1);
			
			if(!srcs.isEmpty()){
				for(Entity src : srcs){
					List<Deque<Entity>> rs0 = searchFromRight(new LinkedList<String>(PathQueue), src, ws);
					for(Deque<Entity> deque : rs0){
						deque.add(tar);
						rs.add(deque);
					}					
				}
			}
			
		}else{
			Deque<Entity> resultEntry = new LinkedList<Entity>();
			resultEntry.add(tar);
			rs.add(resultEntry);
		}
		
		return rs;
	}
	
	public static List<Deque<Entity>> searchFromLeft(Deque<String> pathQueue, Entity src, WrapperService ws){
		
		List<Deque<Entity>> rs = new ArrayList<Deque<Entity>>();
		
		if(!pathQueue.isEmpty()){
			String relName = pathQueue.pollFirst();
			String tarOC = pathQueue.pollFirst();
			
			List<Entity> tars = ws.getTargetsForSourceRelation(src, relName, tarOC, -1);
			
			if(!tars.isEmpty()){
				for(Entity tar : tars){
					List<Deque<Entity>> rs0 = searchFromLeft(new LinkedList<String>(pathQueue), tar, ws);
					for(Deque<Entity> deque : rs0){
						deque.add(src);
						rs.add(deque);
					}
				}	
			}else{
				Deque<Entity> resultEntry = new LinkedList<Entity>();
				resultEntry.add(src);
				rs.add(resultEntry);	
			}						
		}else{
			Deque<Entity> resultEntry = new LinkedList<Entity>();
			resultEntry.add(src);
			rs.add(resultEntry);			
		}
		
		return rs;
	}
	
	private static List<Long> getIdListFromString(String values){
		List<Long> list = new ArrayList<Long>();
		String[] idArray = values.split(",");
		try{
			for(int i=0; i<idArray.length; i++){
				list.add(new Long(idArray[i]));
			}	
		}catch (Exception e) {}
		
		return list;
	}
	
	private static Deque<String> getPathExcludingBeginning(String[] query){
		Deque<String> pathQueue = new LinkedList<String>();
		for(int i=1; i<query.length; i++){
			System.out.print(query[i] + "\t");
			pathQueue.add(query[i]);
		}
		return pathQueue;
	}
	
	private static Deque<String> getPathExcludingEnd(String[] query){
		Deque<String> pathQueue = new LinkedList<String>();
		for(int i=0; i<query.length-1; i++){
			System.out.print(query[i] + "\t");
			pathQueue.add(query[i]);
		}
		return pathQueue;
	}
}
