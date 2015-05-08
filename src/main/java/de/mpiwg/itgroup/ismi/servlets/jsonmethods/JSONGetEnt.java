package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetEnt extends AbstractServletJSONMethod{
	
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			Long entId = getLong(request, JSONUtils.P_ID);
			String mpiwgId = getString(request, JSONUtils.P_MPIWGID);
			Boolean includeContent = getBoolean(request, JSONUtils.P_INCLUDE);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			Integer depth = getInteger(request, JSONUtils.P_DEPTH);
			depth = (depth == null) ? 0 : depth;
			depth = (depth > 2) ? 2 : depth;
			
			if(entId == null && StringUtils.isEmpty(mpiwgId)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "Neither parameter 'id' nor 'mpiwg_id' have been found in the parameter list.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'id' is mandatory and should be a number.");
				
			}else if(StringUtils.isNotEmpty(mpiwgId)){
				
				List<Attribute> attList = ws.getAttributesByExactValue("mpiwg_id", mpiwgId);
				if(!attList.isEmpty()){
					entId = attList.get(0).getSourceId();
				}	
			}
			
			if(entId != null){
				loadEntity(ws, json, entId, depth, includeContent, includeRomanization);
			}else{
				json.put(RESPONSE_INFO, "Entity no found.");
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
	
	private static void loadEntity(WrapperService ws, JSONObject json, Long entId, Integer depth, Boolean includeContent, Boolean includeRomanization) throws JSONException{
		Entity ent = null;
		if(depth > 0 || (includeContent != null && includeContent)){
			ent = ws.getEntityByIdWithContent(entId);
		}else{
			ent = ws.getLightweightEntityById(entId);
		}
		if(ent != null){
			JSONObject jsonEntity = null;
			if(depth > 0){
				jsonEntity = JSONUtils.entityToJSON(ent, depth, ws, includeRomanization);
			}else{
				jsonEntity = JSONUtils.entityToJSON(ent, includeRomanization);	
			}
			json.put(JSONUtils.ENT, jsonEntity);
		}else{
			json.put(RESPONSE_INFO, "Entity no found.");
		}
	}
}