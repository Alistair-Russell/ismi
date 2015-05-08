package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetAtts extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			Long entId = getLong(request, JSONUtils.P_ID);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			if(entId == null){
				json.put(RESPONSE, EXCEPTION);
				json.put(RESPONSE_INFO, "Parameter 'id' is mandatory and should be a number.");
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'id' is mandatory and should be a number.");
				
			}else{
				List<Attribute> attList = ws.getAttributeByEntId(entId);
				JSONArray array = new JSONArray();
				for(Attribute att : attList){
					array.put(JSONUtils.attributeToJSON(att, includeRomanization));
				}
				json.put(JSONUtils.R_ATTS, array);
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