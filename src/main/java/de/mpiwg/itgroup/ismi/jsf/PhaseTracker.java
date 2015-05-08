package de.mpiwg.itgroup.ismi.jsf;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;

import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;

public class PhaseTracker implements PhaseListener {

	private static final long serialVersionUID = -1L;

	public void beforePhase(PhaseEvent event) {

		Map<String, String> parameters = event.getFacesContext().getExternalContext().getRequestParameterMap();
		
		String servletPath = event.getFacesContext().getExternalContext().getRequestServletPath();
		
		if(StringUtils.isNotEmpty(servletPath) && event.getPhaseId() == PhaseId.RESTORE_VIEW){
			
			if(StringUtils.endsWith(parameters.get("login"), "true")){
				getSessionBean(event.getFacesContext()).listenerDisplayLoginDialog(null);
			}
			
			if(servletPath.equals("/browse/entityDetails.xhtml")){
				String entityId = parameters.get("eid");
				if(StringUtils.isNotEmpty(entityId)){
					setIdToEntityDetails(event.getFacesContext(), entityId);
				}
			} else if(servletPath.equals("/browse/entityRepository.xhtml")){
				//setEntityRepositoryState(parameters, event.getFacesContext());
				
			} else if(servletPath.equals("/search/simpleSearch.xhtml")){
				//String term = parameters.get("term");
				//String mode = parameters.get("md");
				//getSessionBean(event.getFacesContext()).setSimpleSearchState(term, mode);
			} else if(servletPath.equals("/search/displayAuthor.xhtml")){
				String personId = parameters.get("personId");
				String textId = parameters.get("textId");
				String witnessId = parameters.get("witnessId");
				
				if(StringUtils.isNotEmpty(personId)){
					getSessionBean(event.getFacesContext()).displayAuthorSetPerson(personId);
					if(StringUtils.isNotEmpty(textId)){
						getSessionBean(event.getFacesContext()).displayAuthorSetTitle(textId);
						if(StringUtils.isNotEmpty(witnessId)){
							getSessionBean(event.getFacesContext()).displayAuthorSetWitness(witnessId);
						}
					}
				}else if(StringUtils.isNotEmpty(textId)){
					getSessionBean(event.getFacesContext()).displayAuthorFromTitles(textId);
					if(StringUtils.isNotEmpty(witnessId)){
						getSessionBean(event.getFacesContext()).displayAuthorSetWitness(witnessId);
					}
				}else if(StringUtils.isNotEmpty(witnessId)){
					getSessionBean(event.getFacesContext()).displayAuthorFromWitness(witnessId);
				}
				
				
			}else if(servletPath.equals("/search/displayTitle.xhtml")){
				String textId = parameters.get("textId");
				String witnessId = parameters.get("witnessId");
				if(StringUtils.isNotEmpty(textId)){
					getSessionBean(event.getFacesContext()).displayTitleSetTitle(textId);
					if(StringUtils.isNotEmpty(witnessId)){
						getSessionBean(event.getFacesContext()).displayTitleSetWitness(witnessId);	
					}
				}else if(StringUtils.isNotEmpty(witnessId)){
					getSessionBean(event.getFacesContext()).displayTitleFromWitness(witnessId);
				} 
			}else if(servletPath.equals("/entry/createEntity.xhtml")){
				String entityId = parameters.get("eid");
				String formIndex = parameters.get("formIndex");
				if(StringUtils.isEmpty(entityId) && StringUtils.isNotEmpty(formIndex)){
					//set tabIndex
					getSessionBean(event.getFacesContext()).setSelectedTab(formIndex);
				}else if(StringUtils.isNotEmpty(entityId) && !entityId.equals("0")){
					//set entity to edit
					getSessionBean(event.getFacesContext()).editEntity(entityId);
				}
			}else if(servletPath.equals("/public/dynamicPage.xhtml")){
				String entId = parameters.get("eid");
				if(StringUtils.isNotEmpty(entId)){
					getSessionBean(event.getFacesContext()).getDynamicPage().load(entId);
				}
			
			}else if(servletPath.equals("/public/publicCodexOverview.xhtml")){	
				String entId = parameters.get("eid");
				if(StringUtils.isNotEmpty(entId)){
					getSessionBean(event.getFacesContext()).getPublicCodexView().load(entId);
				}
				
			}/* else if(servletPath.equals("/entry/geonameForm.xhtml")){
			}
				String placeId = parameters.get("placeId");
				if(StringUtils.isNotEmpty(placeId)){
					getSessionBean(event.getFacesContext()).setPlaceInGeoForm(placeId);
				}
			}*/
		}
	}

	
	private SessionBean getSessionBean(FacesContext context){
		SessionBean bean = (SessionBean)context.getExternalContext().getSessionMap().get("Session");
		if(bean == null){
			bean = new SessionBean();
			context.getExternalContext().getSessionMap().put("Session", bean);
		}
		return bean;
	}
	
	private void setIdToEntityDetails(FacesContext context, String id){
		getSessionBean(context).setIdEntityDetails(id);
	}
	
	
	

	public void afterPhase(PhaseEvent event) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
	
	
	class PrettyUrlResponseWrapper extends HttpServletResponseWrapper {
	    public PrettyUrlResponseWrapper(HttpServletResponse response) {   
	        super(response);   
	    } 
	}
}
