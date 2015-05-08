package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetEntsSize extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			String oc = request.getParameter(JSONUtils.P_OC);
			
			if(StringUtils.isEmpty(oc)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "ObjectClass no valid.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Object Class no valid.");
				
			}else{
				int count = ws.getEntitiesCount(oc);
				json.put(JSONUtils.R_ENTS_SIZE, count);
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
