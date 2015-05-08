package de.mpiwg.itgroup.ismi.jsf;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;

import com.sun.faces.application.NavigationHandlerImpl;

import de.mpiwg.itgroup.ismi.browse.EntityDetailsBean;
import de.mpiwg.itgroup.ismi.browse.EntityRepositoryBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;
import de.mpiwg.itgroup.ismi.search.beans.SimpleSearchBean;

public class DelegatingNavigationHandlerProxy extends NavigationHandlerImpl {

	NavigationHandler _base;

	public DelegatingNavigationHandlerProxy(NavigationHandler base) {
		super();
		_base = base;
	}

	private EntityRepositoryBean getEntityRepository(FacesContext context) {
		EntityRepositoryBean bean = (EntityRepositoryBean) context
				.getExternalContext().getSessionMap().get("EntityRepository");
		if (bean == null) {
			bean = new EntityRepositoryBean();
			context.getExternalContext().getSessionMap()
					.put("EntityRepository", bean);
		}
		return bean;
	}

	private SessionBean getSessionBean(FacesContext context) {
		SessionBean bean = (SessionBean) context.getExternalContext()
				.getSessionMap().get("Session");
		if (bean == null) {
			bean = new SessionBean();
			context.getExternalContext().getSessionMap().put("Session", bean);
		}
		return bean;
	}

	@Override
	public void handleNavigation(FacesContext facesContext, String fromAction,
			String outcome) {
		if (outcome == null) {
			// stay on current ViewRoot
			return;
		}
		NavigationCase navigationCase = getNavigationCase(facesContext, fromAction, outcome);
		if (navigationCase != null) {
			if (navigationCase.isRedirect()
					/*&& (!PortletUtil.isPortletRequest(facesContext))*/) { 
				ExternalContext externalContext = facesContext
						.getExternalContext();
				ViewHandler viewHandler = facesContext.getApplication()
						.getViewHandler();
				String redirectPath = viewHandler.getActionURL(facesContext,
						navigationCase.getToViewId(facesContext));

				try {
					
					if (outcome.equals("entity_repository")) {
						EntityRepositoryBean bean = getEntityRepository(facesContext);
						if(EntityRepositoryBean.MODE_ALL.equals(bean.getResultMode())){
							redirectPath += "?oc="
							 	+ bean.getObjectClass()
							 	+ "&resultMode="
								+ bean.getResultMode()
								+ "&cpage=";
				
							if(EntityRepositoryBean.MODE_ALL.equals(bean.getResultMode())){
								redirectPath += (bean.getPaginator().getCurrentPage() + 1);
							}else if(EntityRepositoryBean.MODE_ADVANCED.equals(bean.getResultMode())){
								//redirectPath += bean.getCurrentPageAdvancedSearch();
							}else{
								redirectPath += 0;
							}								
						}
			
					} else if(outcome.equals("entity_details")){
						EntityDetailsBean bean = (EntityDetailsBean) facesContext.getExternalContext().getSessionMap().get("EntityDetails");
						if(bean == null){
							bean = new EntityDetailsBean();
							facesContext.getExternalContext().getSessionMap().put("EntityDetails", bean);
						}
						if(StringUtils.isNotEmpty(bean.getCurrentEntityId())){
							redirectPath += "?eid=" + bean.getCurrentEntityId();	
						}
					} else if(outcome.equals("simple_seach")){
						/*
						SimpleSearchBean bean = (SimpleSearchBean) facesContext.getExternalContext().getSessionMap().get("SimpleSearch");
						redirectPath += "?term="
										+ bean.getSearchTerm()
										+ "&md="
										+ bean.getSelectedMode();
										*/
					} else if(outcome.equals("display_title") || outcome.equals("display_author")){
						//Entity bean = (Entity) facesContext.getExternalContext().getRequestMap().get("entity");
						//redirectPath += "?eid=" + bean.getId();
					} else if(outcome.equals("entry_edit_entity")){
						SessionBean session = (SessionBean) facesContext.getExternalContext().getSessionMap().get("Session");
						if(session != null){
							redirectPath += "?formIndex=" + session.getSelectedTab();
							Long entId = session.getEditFormCurrentEntId();
							if(entId != null && entId != 0){
								redirectPath += "&eid=" + entId;
							}
						}
					}
					externalContext.redirect(externalContext.encodeActionURL(redirectPath));

				} catch (IOException e) {
					throw new FacesException(e.getMessage(), e);
				}
			} else {
				ViewHandler viewHandler = facesContext.getApplication()
						.getViewHandler();
				// create new view
				String newViewId = navigationCase.getToViewId(facesContext);
				UIViewRoot viewRoot = viewHandler.createView(facesContext,
						newViewId);
				facesContext.setViewRoot(viewRoot);
				facesContext.renderResponse();
			}
		} else {
			// no navigationcase found, stay on current ViewRoot
		}
	}

	/*
	 * @Override public void handleNavigation(FacesContext fc, String
	 * currentMethod, String currentAction) { /*
	 * System.out.println("currentMethod: " + currentMethod +
	 * "   currentAction: " + currentAction); System.out.println();
	 * System.out.println(); for(String key :
	 * fc.getViewRoot().getAttributes().keySet()){ System.out.println(key + "/t"
	 * + fc.getViewRoot().getAttributes().get(key)); }
	 * System.out.println();System.out.println();
	 * 
	 * if(new String("simple_search").equals(currentAction)){ String path =
	 * getViewId(fc, "", currentAction) + "?chao=chao";
	 * System.out.println("fc.getViewRoot().getViewId(); " +
	 * fc.getViewRoot().getViewId()); //fc.getViewRoot().setViewId(path);
	 * fc.getViewRoot().setViewId("/entry/login.jsp");
	 * 
	 * }else{
	 * 
	 * } _base.handleNavigation(fc, currentMethod, currentAction); }
	 */

	/*
	public String getViewId(FacesContext context, String fromAction,
			String outcome) {
		String view = super.getViewId(context, fromAction, outcome);
		System.out.println("VIEW: " + view);
		return view;

	}*/

}
