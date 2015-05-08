package de.mpiwg.itgroup.diva.rest;

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

public abstract class RestInterface {
	
	public static String APP_BEAN = "ApplicationBean1";
	public static String SESSION_BEAN = "Session";
	
	
	protected boolean userCanEdit(HttpServletRequest request, HttpServletResponse response){
		SessionBean session = getSessionBean(request, response);
		return session.isCanEdit();
		
	}
	
	public SessionBean getSessionBean(HttpServletRequest request,
			HttpServletResponse response){
		return (SessionBean)getSessionBean(SESSION_BEAN, request, response);
	}
	
	public WrapperService getWrapper(HttpServletRequest request,
			HttpServletResponse response){
		return getAppBean(request, response).getWrapper();
	}
	
	public ApplicationBean getAppBean(HttpServletRequest request, HttpServletResponse response) {
		ApplicationBean appBean = (ApplicationBean)getApplicationBean(APP_BEAN, request, response);
		
		if(appBean == null){
			appBean = new ApplicationBean();
			getFacesContext(request, response).getCurrentInstance().getExternalContext().getApplicationMap().put(APP_BEAN, appBean);			
		}
		return appBean; 
	}	
	
	private Object getApplicationBean(String bean, HttpServletRequest request,
			HttpServletResponse response) {
		return getFacesContext(request, response).getExternalContext().getApplicationMap().get(bean);
	}
	
	private Object getSessionBean(String bean, HttpServletRequest request, HttpServletResponse response) {
		return getFacesContext(request, response).getExternalContext().getSessionMap().get(bean);
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
