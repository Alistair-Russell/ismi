package de.mpiwg.itgroup.diva.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.diva.utils.JSONArrayParam;
import de.mpiwg.itgroup.diva.utils.JSONEntity;
import de.mpiwg.itgroup.diva.utils.JSONParam;

@Path("/entity")	
public class RestEntity extends RestInterface{

	
	public RestEntity(){
		System.out.println("%%%%%%%%%%%%%% RestWitness %%%%%%%%%%%%%%%%%%%%");
	}
	
	/*
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Track getTrackInJSON() {

		Track track = new Track();
		track.setTitle("Enter Sandman");
		track.setSinger("Metallica");

		return track;

	}*/
	
	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response 
		saveWitnessPagesIndexing(
			 @Context HttpServletRequest request, 
			 @Context HttpServletResponse response,
			 @QueryParam("data") JSONParam jsonParam) throws Exception{

		
		JSONObject json = jsonParam.getObj();
		System.out.println(json.toString());
		
		List<JSONEntity> entityList = JSONEntity.json2EntityList(json);
		
		
		List<Entity> toSave = new ArrayList<Entity>();
		for(JSONEntity jsonEnt : entityList){
			Entity ent = getWrapper(request, response).getEntityByIdWithContent(jsonEnt.id);
			ent = jsonEnt.updateEntity(ent);
			toSave.add(ent);
			//getWrapper(request, response).saveEntity(ent, "jojojo");
			
		}
		
		getWrapper(request, response).saveEntityList(toSave, "test");
		
		
		System.out.println(getSessionBean(request, response).getUsername());
		System.out.println(getSessionBean(request, response).getUser());
		/*
		if(userCanEdit(request, response)){
			
		}else{
			
		}*/
		
		String json0 = "{}";
		return Response.ok(json0, MediaType.APPLICATION_JSON).build();
	}
	
	
}
