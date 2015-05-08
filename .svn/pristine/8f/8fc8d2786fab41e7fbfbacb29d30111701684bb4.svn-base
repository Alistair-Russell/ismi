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

public class JSONGetWitnesses4Codex extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			String sid = request.getParameter(JSONUtils.P_ID);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(StringUtils.isEmpty(sid)){
				throw new Exception("Parameter id can not be null.");
			}
			Long codexId = Long.parseLong(sid);
			
			List<Entity> witnessList = ws.getSourcesForTargetRelation(codexId, "is_part_of", "WITNESS", -1);
			
			JSONArray array = new JSONArray();
			for(Entity ent : witnessList){
				array.put(JSONUtils.entityToJSON(ent, includeRomanization));
			}
			json.put(JSONUtils.R_ENTS, array);
			json.put(JSONUtils.R_ENTS_SIZE, witnessList.size());
			
			
			
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
