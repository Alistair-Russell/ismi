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
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetTars4SrcRel extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			Long entId = getLong(request, JSONUtils.P_ID);
			String relName = getString(request, JSONUtils.P_REL_NAME);
			String tarOc = getString(request, JSONUtils.P_TAR_OC);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(entId == null || StringUtils.isEmpty(relName) || StringUtils.isEmpty(tarOc)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "The parameters 'id', 'rel_name' and 'tar_oc' are mandatory.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The parameters 'id', 'rel_name' and 'tar_oc' are mandatory.");
				
			}else{
				List<Entity> entList = ws.getTargetsForSourceRelation(ws.getEntityByIdReadOnly(entId), relName, tarOc, -1);
				JSONArray array = new JSONArray();
				for(Entity ent : entList){
					array.put(JSONUtils.entityToJSON(ent, includeRomanization));
				}
				json.put(JSONUtils.R_ENTS, array);
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