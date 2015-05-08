package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;

public class JSONGetRels extends AbstractServletJSONMethod{
	
	private static Logger logger = Logger.getLogger(JSONGetRels.class);
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			String name = getString(request, JSONUtils.P_REL_NAME);
			String srcOC = getString(request, JSONUtils.P_SRC_OC);
			String tarOC = getString(request, JSONUtils.P_TAR_OC);
			Boolean inclEnts = getBoolean(request, JSONUtils.P_INCL_ENTS);
			Boolean includeRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			
			if(StringUtils.isNotEmpty(name)){
				List<Relation> list = ws.getRelation(name, srcOC, tarOC);
				JSONArray array = new JSONArray();
				if(inclEnts != null && inclEnts.equals(true)){
					for(Relation rel : list){
						Entity src = ws.getEntityById(rel.getSourceId());
						Entity tar = ws.getEntityById(rel.getTargetId());
						if(src != null && tar != null){
							List<Attribute> srcAtts = ws.getAttributeByEntId(src.getId());
							List<Attribute> tarAtts = ws.getAttributeByEntId(tar.getId());
							if(srcAtts != null && tarAtts != null){
								array.put(JSONUtils.relationToJSON(rel, src, tar, srcAtts, tarAtts, includeRomanization));	
							}	
						}else{
							System.err.println("Lost src or/and tar fot rhis relation: " + rel.toString());
						}
					}
				}else{
					for(Relation rel : list){
						array.put(JSONUtils.relationToJSON(rel, includeRomanization));
					}
				}
				
				json.put(JSONUtils.R_RELS, array);
				json.put(JSONUtils.R_RELS_SIZE, list.size());
			}else{
				PrintWriter out = response.getWriter();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The parameters '" + JSONUtils.P_REL_NAME + "' is mandatory.");
			}	
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error, please take a look at log file.");
			//e.printStackTrace();
		}finally{
			json = finallyExecution(json, parametersToString(request.getParameterMap()), startExecution);
		}
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		
	}
}
