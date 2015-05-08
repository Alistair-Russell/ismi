package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetDef extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			String oc = request.getParameter(JSONUtils.P_OC);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(StringUtils.isEmpty(oc)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "Object class parameter not valid.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
						"Object class parameter not valid.");
				
			}else{
				Entity def = ws.getDefinition(oc);
				List<Attribute> attList = ws.getAttributeByEntId(def.getId());
				List<Relation> srcList = ws.getDefSourceRelations(def.getOwnValue());
				List<Relation> tarList = ws.getDefTargetRelations(def.getOwnValue());
				json.put(JSONUtils.R_DEF, JSONUtils.entityToJSON(def, attList, srcList, tarList, includeRomanization));
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