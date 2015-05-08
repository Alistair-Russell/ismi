package de.mpiwg.itgroup.diva.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.diva.utils.JSONEntity;
import de.mpiwg.itgroup.diva.utils.JSONParam;
import de.mpiwg.itgroup.ismi.entry.beans.CurrentCodexBean;


@Path("/witness")	
public class RestWitness extends RestInterface{

	
	private static Logger logger = Logger.getLogger(RestWitness.class);
	
	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response 
		saveWitnessPagesIndexing(
			 @Context HttpServletRequest request, 
			 @Context HttpServletResponse response,
			 @QueryParam("data") JSONParam jsonParam) throws Exception{

		JSONObject resp = new JSONObject();
		
		try {
			if(getSessionBean(request, response).isCanEdit()){
				JSONObject json = jsonParam.getObj();
		
				logger.info(json.toString());	
							
				if(json.getJSONObject("witness") != null && json.getJSONObject("witness").get("id") != null){
					
					JSONEntity jsonEnt = new JSONEntity(json.getJSONObject("witness"), json.getJSONObject("witness").getLong("id"));
					Long digiId = json.getLong("digi_id");
				
					Entity ent = getWrapper(request, response).getEntityByIdWithContent(jsonEnt.id);
					ent = jsonEnt.updateEntity(ent);
					
					getWrapper(request, response).saveEntity(ent, getSessionBean(request, response).getUsername());	
				}			
			
				resp.put("state", "ok");
			}else{
				resp.put("state", "error");
				
				if(getSessionBean(request, response).getUser() == null){
					resp.put("message", "Action was not executed. You must log in.");
				}else{
					resp.put("message", "Action was not executed. User can not edit.");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.put("state", "error");
			resp.put("message", e.getMessage());
		}
		
		
		return Response.ok(resp.toString(), MediaType.APPLICATION_JSON).build();
	}
}
