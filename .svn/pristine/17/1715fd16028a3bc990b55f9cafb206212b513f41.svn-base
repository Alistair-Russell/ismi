package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mpiwg.itgroup.ismi.utils.HTTPUtils;
import de.mpiwg.itgroup.ismi.utils.HTTPUtils.HttpStringResponse;

public class JSONDataProxy {

	
	//http://images.rasi.mcgill.ca/data/Majlis_shura_21.json
	
	public static void execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String file = request.getParameter("file");
		
		HttpStringResponse resp = HTTPUtils.getHttpSSLStringResponse("https://images.rasi.mcgill.ca/data/" + file + ".json");
		
		if(resp.code == 200){
			
			PrintWriter out = response.getWriter();
			out.print(resp.content);
		}else{
			response.sendError(resp.code, "Problems loading http://images.rasi.mcgill.ca/data/" + file + ".json");
		}
		
	}
}
