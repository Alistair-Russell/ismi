package de.mpiwg.itgroup.diva.rest;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.mpiwg.itgroup.diva.utils.JSONParam;
import de.mpiwg.itgroup.ismi.utils.HTTPUtils;
import de.mpiwg.itgroup.ismi.utils.HTTPUtils.HttpResponse;
import de.mpiwg.itgroup.ismi.utils.HTTPUtils.HttpStringResponse;

@Path("/diva/proxy")
public class DivaProxy {
	
	private static String IMG_SERVER = "https://images.rasi.mcgill.ca/fcgi-bin/iipsrv.fcgi?FIF=/data7/srv/images/";
	
	@GET
	@Path("/image")
	@Produces("image/jpeg")
	public Response 
		image(
			 @QueryParam("f") String f,
			 @QueryParam("w") String w) throws Exception{
	
		String dirName = getDirName(f);
		
		String url = IMG_SERVER + dirName + "/" + f + "&WID=" + w + "&CVT=JPG";
		
		
		HttpResponse resp = HTTPUtils.getHttpSSLResponse(url);
		
		if(resp.code == 200){
			return Response.ok(resp.content).build();	
		}
		
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	@GET
	@Path("/json/{file}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response 
		json(
			 @PathParam("file") String file) throws Exception{
	
		HttpStringResponse resp = HTTPUtils.getHttpSSLStringResponse("https://images.rasi.mcgill.ca/data/" + file + ".json");
		if(resp.code == 200){
			return Response.ok(resp.content).build();	
		}
		
		return Response.status(Response.Status.NOT_FOUND).build();
		
	}
	
	
	
	private String getDirName(String fileName){
		String[] array = fileName.split("_");
		return fileName.replace("_" + array[array.length-1], "");		
	}
	
	
}
