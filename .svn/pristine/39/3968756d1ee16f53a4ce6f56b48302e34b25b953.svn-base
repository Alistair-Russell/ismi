package de.mpiwg.itgroup.ismi.jsf.exceptions;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MyExceptionHandler extends ExceptionHandlerWrapper{
	
	private static Logger logger = Logger.getLogger(MyExceptionHandler.class);
	
	private ExceptionHandler wrapped;

    public MyExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    public javax.faces.context.ExceptionHandler getWrapped() {
        return wrapped;
    }

    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        //Iterate through the queued exceptions
        while (events.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            //See if it's an exception I'm interested in
            if (t instanceof ViewExpiredException) {
            	try {

            		String path = getContextPath();
                	logger.info("ViewExpiredException detected. Redirecting to " + path);
                	
                	ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                	ctx.redirect(path);
                	
                } catch (Exception e){
                	logger.error(e.getMessage(), e);
                } finally {
                    //remove it if you processed it
                    events.remove();
                }
            }

            //Let the next ExceptionHandler(s) deal with the others
            getWrapped().handle();
        }
    }
    
    public String getContextPath(){
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String port = (StringUtils.equals(request.getLocalPort() + "", "80")) ? "" : (":" + request.getLocalPort());
		String path = request.getScheme() + "://" + request.getLocalName() + port + request.getContextPath();
		return path;
    }
}
