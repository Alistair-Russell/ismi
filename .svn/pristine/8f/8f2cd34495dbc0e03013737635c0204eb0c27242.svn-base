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
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetPublicCodices extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			JSONArray array = new JSONArray();
			
			List<Attribute> attList = ws.getAttributesByDefByAttName("CODEX", "public", "true", -1);
			for(Attribute att : attList){
				Entity ent = ws.getEntityByIdWithContent(att.getSourceId());
				if(ent != null){
					array.put(JSONUtils.entityToJSON(ent, includeRomanization));
				}
			}
			json.put(JSONUtils.R_ENTS, array);
			json.put(JSONUtils.R_ENTS_SIZE, array.length());
			
			
						
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
