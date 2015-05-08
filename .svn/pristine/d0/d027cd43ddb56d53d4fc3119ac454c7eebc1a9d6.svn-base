package de.mpiwg.itgroup.ismi.servlets.jsonmethods;

import java.io.IOException;
import java.io.PrintWriter;

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
import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;

public class JSONGetWitnessDetails   extends AbstractServletJSONMethod{
	
	public static void execute(WrapperService ws, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		long startExecution = System.currentTimeMillis();
		
		try{
			
			JSONObject witnessDetails = new JSONObject();
			
			String sid = request.getParameter(JSONUtils.P_ID);
			Boolean useRomanization = getBoolean(request, JSONUtils.P_INCL_ROMANIZATION);
			
			
			if(StringUtils.isEmpty(sid)){
				throw new Exception("Parameter id can not be null.");
			}
			Long witnessId = Long.parseLong(sid);
			
			Entity witness = ws.getEntityByIdWithContent(witnessId);
			WitnessTemplate template = new WitnessTemplate(witness, ws, true, useRomanization);
			
			
			witnessDetails.put("ov", template.ov);
			witnessDetails.put("title", template.title);
			witnessDetails.put("startPage", template.startPage);
			witnessDetails.put("endPage", template.endPage);
			witnessDetails.put("status", template.status);
			witnessDetails.put("tableOfContents", template.tableOfContents);
			witnessDetails.put("notesOnTitleAuthor", template.notesOnTitleAuthor);
			witnessDetails.put("notesOnCollationAndCorrections", template.notesOnCollationAndCorrections);
			witnessDetails.put("notesOnOwnership", template.notesOnOwnership);
			witnessDetails.put("notes", template.notes);
			witnessDetails.put("codex", template.codex);
			witnessDetails.put("collection", template.collection);
			witnessDetails.put("repository", template.repository);
			witnessDetails.put("city", template.city);
			witnessDetails.put("country", template.country);
			witnessDetails.put("folios", template.folios);
			witnessDetails.put("incipit", template.incipit);
			witnessDetails.put("explicit", template.explicit);
			witnessDetails.put("subject", template.subject);
			witnessDetails.put("ahlwardtNo", template.ahlwardtNo);
			witnessDetails.put("titleAsWrittenInWitness", template.titleAsWrittenInWitness);
			witnessDetails.put("authorAsWrittenInWitness", template.authorAsWrittenInWitness);
			witnessDetails.put("copyist", template.copyist);
			witnessDetails.put("placeOfCopying", template.placeOfCopying);
			witnessDetails.put("colophon", template.colophon);
			
			witnessDetails.put("pageDimensions", template.page_dimensions);
			witnessDetails.put("writtenAreaDimensions", template.written_area_dimensions);
			witnessDetails.put("linesPerPage", template.lines_per_page);
			witnessDetails.put("pageLayout", template.page_layout);
			witnessDetails.put("script", template.script);
			witnessDetails.put("writingSurface", template.writing_surface);
			witnessDetails.put("fullTitle", template.fullTitle);
			witnessDetails.put("fullTitleTranslit", template.fullTitleTranslit);
			witnessDetails.put("creationDate", template.creationDate);
			
			witnessDetails.put("authorName", template.authorName);
			witnessDetails.put("authorNameTranslit", template.authorNameTranslit);
			
			//references
			JSONObject references = new JSONObject();
			for(String reference : template.referenceEndnoteIdList.keySet()){

			    JSONArray array = new JSONArray();
			    array.put(template.referenceEndnoteIdList.get(reference));
			    references.put(reference,array);
			}
			witnessDetails.put("references", references);
			
			
			JSONArray array = new JSONArray();
			for(String reader : template.wasStudiedByList){
				array.put(reader);
			}
			
			witnessDetails.put("readers", array);
			
			
			json.put("data", witnessDetails);			
			
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