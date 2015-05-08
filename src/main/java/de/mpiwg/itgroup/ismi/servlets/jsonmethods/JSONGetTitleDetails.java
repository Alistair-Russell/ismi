package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;
import de.mpiwg.itgroup.ismi.utils.templates.TitleTemplate;

public class JSONGetTitleDetails  extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			JSONObject titleDetails = new JSONObject();
			
			String sid = request.getParameter(JSONUtils.P_ID);
			
			if(StringUtils.isEmpty(sid)){
				throw new Exception("Parameter id can not be null.");
			}
			Long titleId = Long.parseLong(sid);
			Entity title = ws.getEntityByIdWithContent(titleId);
			TitleTemplate template = new TitleTemplate(title, ws, true);
			
			titleDetails.put("ov", template.ov);
			titleDetails.put("author", template.author);
			titleDetails.put("authorId", template.authorId);
			titleDetails.put("category", template.category);
			titleDetails.put("createIn", template.createIn);
			titleDetails.put("creationDate", template.creationDate);
			titleDetails.put("dedication", template.dedication);
			titleDetails.put("explicit", template.explicit);
			titleDetails.put("fullTitle", template.fullTitle);
			titleDetails.put("fullTitleTranslit", template.fullTitleTranslit);
			titleDetails.put("incipit", template.incipit);
			titleDetails.put("language", template.language);
			titleDetails.put("notes", template.notes);
			titleDetails.put("tableOfContents", template.tableOfContents);
			titleDetails.put("personDedicatedTo", template.personDedicatedTo);
			titleDetails.put("commentaryOnText", template.commentaryOnText);
			titleDetails.put("translationOfText", template.translationOfText);
			titleDetails.put("versionOfText", template.versionOfText);
			
			//aliases
			JSONArray aliases = new JSONArray();
			for(String alias : template.aliasList){
				aliases.put(alias);
			}			
			titleDetails.put("aliases", aliases);
			
			//authorMissattributions
			JSONArray missatts = new JSONArray();
			for(String missatt : template.authorMisattributionList){
				missatts.put(missatt);
			}
			titleDetails.put("authorMisattribution", missatts);



			//references
			JSONObject references = new JSONObject();
			for(String reference : template.referenceEndnoteIdList.keySet()){

			    JSONArray array = new JSONArray();
			    array.put(template.referenceEndnoteIdList.get(reference));
			    references.put(reference,array);
			}
			
			titleDetails.put("references", references);
			
			json.put("data", titleDetails);		

			
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