package de.mpiwg.itgroup.ismi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;

import de.mpiwg.itgroup.ismi.json.utils.JSONUtils;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONDataProxy;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONExistRel;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetAtts;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetDef;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetDefs;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetEnt;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetEnts;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetEntsSize;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetPublicCodices;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetTitleDetails;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetWitnessDetails;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetWitnesses4Codex;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONSearch;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetRels;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetSrcRels;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetSrcs4TarRel;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetTarRels;
import de.mpiwg.itgroup.ismi.servlets.jsonmethods.JSONGetTars4SrcRel;

public class JSONInterface extends AbstractServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(JSONInterface.class);

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private WrapperService wrapper;
	
	private void initialize(HttpServletRequest request, HttpServletResponse response){

		this.request = request;
		this.response = response;
		this.response.setContentType("application/json; charset=UTF-8");
		//this.response.setContentType("text/html; charset=UTF-8");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doIt(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doIt(request, response);
	}
	
	public void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long start = System.currentTimeMillis();
		this.initialize(request, response);

		String method = request.getParameter(JSONUtils.METHOD);
		
		logger.debug("Starting " + paramsToString(request.getParameterMap()));
		
		if(StringUtils.isNotEmpty(method)){
			try{
				if(method.equals(JSONUtils.M_GET_ENT)){
					JSONGetEnt.execute(getWrapper(), request, response);
				}else if(method.equals("data_proxy")){
					JSONDataProxy.execute(request, response);
				}else if(method.equals(JSONUtils.M_GET_ENTS)){
					JSONGetEnts.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_ENTS_SIZE)){
					JSONGetEntsSize.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_DEF)){
					JSONGetDef.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_DEFS)){
					JSONGetDefs.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_SRC_RELS)){
					JSONGetSrcRels.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_TAR_RELS)){
					JSONGetTarRels.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_ATTS)){
					JSONGetAtts.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_TARS_4_SRC_REL)){
					JSONGetTars4SrcRel.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_SRCS_4_TAR_REL)){
					JSONGetSrcs4TarRel.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_EXIST_RELATION)){
					JSONExistRel.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_RELS)){
					JSONGetRels.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_SEARCH)){
					JSONSearch.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_GET_PUBLIC_CODICES)){
					JSONGetPublicCodices.execute(getWrapper(), request, response);
				}else if(method.equals(JSONUtils.M_WITNESSES_4_CODEX)){
					JSONGetWitnesses4Codex.execute(getWrapper(), request, response);
				}else if(method.equals("get_title_details")){
					JSONGetTitleDetails.execute(getWrapper(), request, response);
				}else if(method.equals("get_witness_details")){
					JSONGetWitnessDetails.execute(getWrapper(), request, response);
				}else{
					PrintWriter out = response.getWriter();
					out.print("{\"responseInfo\": \"Method no valid "+ method +".\"}");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Method no valid "+ method +".");
				}	
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				PrintWriter out = response.getWriter();
				out.print(e.getMessage());
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}else{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter method not found!.");
		}
		long end = System.currentTimeMillis();
		logger.debug("Finish [time=" + (end-start) + "] " + paramsToString(request.getParameterMap()));
	}
	
	private String paramsToString(Map<String, String[]> paramMap){
		StringBuilder sb = new StringBuilder();
		
		for(Entry<String, String[]> entry : paramMap.entrySet()){
			sb.append(entry.getKey() + "=[");
			int count = 0;
			for(String value : entry.getValue()){
				if(count > 0){
					sb.append(", ");
				}
				sb.append(value);
				count++;
			}
			sb.append("] ");
		}
		
		return sb.toString();
	}
	
	
	public WrapperService getWrapper(){
		if(this.wrapper == null){
			this.wrapper = getAppBean(request, response).getWrapper();
		}
		return this.wrapper;
	}
}
