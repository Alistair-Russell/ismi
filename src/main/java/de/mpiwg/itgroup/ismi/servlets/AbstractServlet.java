package de.mpiwg.itgroup.ismi.servlets;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mpi.openmind.cache.WrapperService;

import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;


public class AbstractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static String APP_BEAN = "ApplicationBean1";
	
	public ApplicationBean getAppBean(HttpServletRequest request, HttpServletResponse response) {
		ApplicationBean appBean = (ApplicationBean)getApplicationBean(request, response, APP_BEAN);
		
		if(appBean == null){
			appBean = new ApplicationBean();
			getFacesContext(request, response).getCurrentInstance().getExternalContext().getApplicationMap().put(APP_BEAN, appBean);			
		}
		return appBean; 
	}
	
	public Object getApplicationBean(HttpServletRequest request, HttpServletResponse response, String bean) {
		return getFacesContext(request, response).getExternalContext().getApplicationMap().get(bean);
	}

	
	public FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) { 
		ServletContext servletContext = ((HttpServletRequest)request).getSession().getServletContext(); 
		FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY); 
		LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
		Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE); 
		return contextFactory.getFacesContext(servletContext, request, response, lifecycle);
	}
}
