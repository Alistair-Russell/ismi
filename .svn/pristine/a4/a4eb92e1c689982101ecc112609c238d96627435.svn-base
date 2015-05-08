package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
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

public class JSONGetEnts extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			String oc = request.getParameter(JSONUtils.P_OC);
			String ids = request.getParameter(JSONUtils.P_IDS);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(StringUtils.isEmpty(oc) && StringUtils.isEmpty(ids)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "Object class parameter for the entities not valid.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
						"Object class parameter for the entities not valid.");
				
			}else if(StringUtils.isNotEmpty(oc)){
				List<Entity> list = ws.getEntitiesByDef(oc);
				JSONArray array = new JSONArray();
				for(Entity ent : list){
					array.put(JSONUtils.entityToJSON(ent, includeRomanization));
				}
				json.put(JSONUtils.R_ENTS, array);
				json.put(JSONUtils.R_ENTS_SIZE, list.size());
			}else{
				String[] idArray = ids.split(",");
				JSONArray array = new JSONArray();
				for(String sid : idArray){
					try {
						Long id = Long.parseLong(sid);
						Entity ent =  ws.getEntityByIdWithContent(id);
						if(ent != null){
							array.put(JSONUtils.entityToJSON(ent, includeRomanization));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				json.put(JSONUtils.R_ENTS, array);
				json.put(JSONUtils.R_ENTS_SIZE, array.length());
				
			}
						
		}catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}finally{
			json = finallyExecution(json, parametersToString(request.getParameterMap()), startExecution);
		}
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		
	}
}
