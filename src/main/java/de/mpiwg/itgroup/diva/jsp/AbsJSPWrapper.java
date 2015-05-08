package de.mpiwg.itgroup.diva.jsp;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mpi.openmind.cache.WrapperService;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;

public abstract class AbsJSPWrapper {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public static String APP_BEAN = "ApplicationBean1";
	public static String SESSION_BEAN = "Session";
	
	public SessionBean getSessionBean(){
		return (SessionBean)getSessionBean(SESSION_BEAN);
	}
	
	public WrapperService getWrapper(){
		return getAppBean().getWrapper();
	}
	
	public ApplicationBean getAppBean() {
		ApplicationBean appBean = (ApplicationBean)getApplicationBean(APP_BEAN);
		
		if(appBean == null){
			appBean = new ApplicationBean();
			getFacesContext(request, response).getCurrentInstance().getExternalContext().getApplicationMap().put(APP_BEAN, appBean);			
		}
		return appBean; 
	}
	
	private Object getApplicationBean(String bean) {
		return getFacesContext(request, response).getExternalContext().getApplicationMap().get(bean);
	}
	
	private Object getSessionBean(String bean) {
		return getFacesContext(request, response).getExternalContext().getSessionMap().get(bean);
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		System.out.println(response);
		this.response = response;
	}

	
	public void init(){
		System.out.println("THIS METHOD SHOULD BE IMPLEMENTED!!!!");
	}
	
	public FacesContext getFacesContext(HttpServletRequest request,
			HttpServletResponse response) {
		ServletContext servletContext = ((HttpServletRequest) request)
				.getSession().getServletContext();
		FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
				.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
				.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		Lifecycle lifecycle = lifecycleFactory
				.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
		return contextFactory.getFacesContext(servletContext, request,
				response, lifecycle);
	}
}
