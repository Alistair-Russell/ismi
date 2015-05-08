package de.mpiwg.itgroup.ismi.entry.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;

public abstract class AbstractBean implements Serializable{
	
	private static final long serialVersionUID = -8646299519985691771L;
	private static String BEAN_APP = "ApplicationBean1";
	private static String BEAN_SESSION = "Session";
	
	public Object getRequestBean(String name){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	}

	public Object getSessionBean(String name){
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
	}
	
	public Object getApplicationBean(String name){
		return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(name);
	}
	
	public void addSessionBean(String name, Object bean){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, bean);
	}
	
	public ApplicationBean getAppBean(){
		ApplicationBean app = (ApplicationBean)getApplicationBean(BEAN_APP);
		if(app == null){
			app = new ApplicationBean();
			FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(BEAN_APP, app);	
		}
		return app;
	}
	
	protected void printInternalError(Exception e){
		addErrorMsg("Internal Error, this action could not be executed correctly.");
		addErrorMsg(e.getMessage());
		addErrorMsg("Server time: " + DateFormat.getDateTimeInstance().format(new Date()));
		addErrorMsg("Please inform support: jurzua@mpiwg-berlin.mpg.de");
	}
	
	protected SessionBean getSessionBean() {
		SessionBean session = 
			((SessionBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(BEAN_SESSION));
		if(session == null){
			session = new SessionBean();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(BEAN_SESSION, session);
		}
		return session;
	}
	
	public void addGeneralMsg(String msg){
		System.out.println(msg);
		this.getSessionBean().addGeneralMsg(msg);
	}
	
	public void addErrorMsg(String msg){
		this.getSessionBean().addErrorMsg(msg);
	}
	
	public void addException(Exception e){
		this.getSessionBean().addErrorMsg("Internal error: " + e.getMessage());
	}

	protected void redirect(String redirectPath, String parameters){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String contextPath = ec.getRequestContextPath();
		if(StringUtils.isEmpty(redirectPath)){
			redirectPath = ec.getRequestServletPath();	
		}
		try {
			//System.out.println(contextPath + redirectPath + parameters);
			ec.redirect(ec.encodeActionURL(contextPath + redirectPath + parameters));
			
			//ec.redirect(contextPath + redirectPath + parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected WrapperService getWrapper() {
		return getAppBean().getWrapper();
	}
}
