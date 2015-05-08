package de.mpiwg.itgroup.ismi.browse;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;
import de.mpiwg.itgroup.ismi.event.beans.CopyEvent;
import de.mpiwg.itgroup.ismi.event.beans.StudyEvent;
import de.mpiwg.itgroup.ismi.event.beans.TransferEvent;
import de.mpiwg.itgroup.ismi.util.guiComponents.DataPaginator;

public class EntityRepositoryBean extends AbstractEntityRepositoryBean {
	private static final long serialVersionUID = -2380877853539157567L;
	
	private transient DataPaginator paginator = new DataPaginator();
	
	public EntityRepositoryBean(){
		super();
	}
	
	@Override
	public void reset(){
		super.reset();
		this.paginator = new DataPaginator();
	}


	private void updateEntities() {
		if (StringUtils.isNotEmpty(getObjectClass())) {
			this.paginator.initCount();
			int startRecord = this.paginator.getCurrentPage()
					* this.paginator.getItemsPerPage();
			
			if(this.paginator.getNumberOfPages() == 0){
				this.setEntities(new ArrayList<Entity>());
			}else{
				int mod = getWrapper().getEntitiesCount(getObjectClass()) % paginator.getItemsPerPage();
				if((paginator.getCurrentPage() + 1) == paginator.getNumberOfPages() && mod != 0){
					this.setEntities(
							getWrapper().getEntityByDefSubList(getObjectClass(), startRecord, startRecord + mod)
					);	
				}else{
					this.setEntities(
							getWrapper().getEntityByDefSubList(getObjectClass(), startRecord, startRecord + paginator.getItemsPerPage())
					);	
				}
			}
		} else {
			this.setEntities(new ArrayList<Entity>());
		}
	}

	public String first() {
		this.paginator.first();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String last() {
		this.paginator.last();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String fastForward() {
		this.paginator.fastForward();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String fastRewind() {
		this.paginator.fastRewind();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String previous() {
		this.paginator.previous();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public String next() {
		this.paginator.next();
		this.updateEntities();
		return GOTO_ENTITY_REPOSITORY;
	}

	public void listenerGoToPage(ActionEvent event) {
		try {
			this.setPageMsg("");
			Integer intPage = new Integer(this.getPage());
			if (intPage != null) {
				intPage--;
				this.paginator.goToPage(intPage);
				this.updateEntities();
				
			}
		} catch (Exception e) {
			this.setPageMsg("page is invalid!");
		}
	}

	public String actionShowAll() {
		
		this.setResultMode(MODE_ALL);
		this.setResultSummaryMsg("");
		this.setPage("");
		this.paginator.setCurrentPage(0);
		int entitiesCount = getWrapper().getEntitiesCount(getObjectClass());
		this.paginator.resetNumberOfPages(entitiesCount);
		this.updateEntities();
		
		setResultSummaryMsg(entitiesCount + " items were found!");
		
		return GOTO_ENTITY_REPOSITORY;
	}
	
	

	public String details() {
		Entity selectedEntity = (Entity) getRequestBean("entity");
		EntityDetailsBean bean = (EntityDetailsBean) getRequestBean(SESSION_BEAN_ENTITY_DETAILS);
		bean.setEntity(selectedEntity);
		
		return GOTO_ENTITY_DETAILS;
	}

	public String actionEdit() {
		Entity entity = (Entity) getRequestBean("entity");
		getSessionBean().editEntity(entity);
		//return SessionBean.PAGE_ENTRY;
		
		if(entity.getObjectClass().equals(StudyEvent.OC) ||
				entity.getObjectClass().equals(CopyEvent.OC) ||
				entity.getObjectClass().equals(TransferEvent.OC)){
			return SessionBean.PAGE_EVENT_FORM;			
		}else{
			return SessionBean.PAGE_ENTRY;
		}
	}

	public DataPaginator getPaginator() {
		return paginator;
	}

	public void setPaginator(DataPaginator paginator) {
		this.paginator = paginator;
	}
}
