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
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetDefs extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			JSONArray defArray = new JSONArray();
			List<Entity> list = ws.getLWDefinitions();
			for(Entity def : list){
				List<Attribute> attList = ws.getAttributeByEntId(def.getId());
				List<Relation> srcList = ws.getDefSourceRelations(def.getOwnValue());
				List<Relation> tarList = ws.getDefTargetRelations(def.getOwnValue());
				defArray.put(JSONUtils.entityToJSON(def, attList, srcList, tarList, false));
			}
			json.put(JSONUtils.R_DEFS, defArray);
			//TODO sort!!! 
						
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