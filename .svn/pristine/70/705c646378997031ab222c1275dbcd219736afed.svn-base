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

public class JSONExistRel  extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			Long srcId = getLong(request, JSONUtils.P_SRC_ID);
			Long tarId = getLong(request, JSONUtils.P_TAR_ID);
			String relName = getString(request, JSONUtils.P_REL_NAME);
			
			if(srcId == null || tarId == null || StringUtils.isEmpty(relName)){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "The parameters 'src_id', 'tar_id', and 'rel_name' are mandatory.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The parameters 'src_id', 'tar_id', and 'rel_name' are mandatory.");
				
			}else{
				
				Boolean existRel = ws.existRelation(srcId, tarId, relName);
				json.put(JSONUtils.R_EXIST_REL, existRel);
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