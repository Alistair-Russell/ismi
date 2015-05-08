package de.mpiwg.itgroup.ismi.search.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.search.utils.ResultEntry;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class AbstractQuery extends AbstractBean implements Serializable{
	
	private static final long serialVersionUID = 8640487026954118233L;

	private static Logger logger = Logger.getLogger(AbstractQuery.class);

	protected ApplicationBean appBean;
	private boolean searched = false;
	private Long timeExecution;
	private String exportType = "xml";
	
	protected String exportUrlRoot;
	private String modeExportDirk;
	protected String exportDirkUrl;
	protected String idList;
	
	protected List<?> rs;
	
	protected int MAX_RS = 20000;
	
	private static List<SelectItem> modeExportDirkList;
	
	static{
		modeExportDirkList = new ArrayList<SelectItem>();
		modeExportDirkList.add(new SelectItem("xml"));
		modeExportDirkList.add(new SelectItem("tab"));
		modeExportDirkList.add(new SelectItem("html"));
		//modeExportDirkList.add(new SelectItem("doc"));
	}
	
	public void listenerChangeModeExportDirk(ValueChangeEvent event){
		if(event != null && event.getNewValue() != null){
			this.exportDirkUrl = ApplicationBean.generateExportURL(exportUrlRoot, idList, event.getNewValue().toString());	
		}
	}
	
	public AbstractQuery(ApplicationBean appBean){
		this.appBean = appBean;
		this.modeExportDirk = "xml";
	}
	
	public void listenerReset(ActionEvent event){
		this.reset();
		this.modeExportDirk = "xml";	
	}
	
	public void listenerSearch(ActionEvent event){
		try {
			long start = System.currentTimeMillis();
			this.search();
			long end = System.currentTimeMillis();
			this.searched = true;
			this.timeExecution = end - start;
			logger.info(toString() + " time execution=" + (timeExecution));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			printInternalError(e);
		}

	}
	
	public void reset(){
		this.timeExecution = null;
		this.searched = false;
	}
	
	protected void search() throws Exception{}
	
	protected void printRs(List<ResultEntry> rs, WrapperService ws){
		StringBuilder sb = new StringBuilder();
		sb.append("--------------\n");		
		for(ResultEntry entry : rs){
			for(Entry<Integer, Long> ent : entry.getEntMap().entrySet()){
				sb.append(ent.getKey() + ") " + ws.getEntityById(ent.getValue()).toSmallString());		
			}
			sb.append("\n");
		}
		logger.info("--------------");
		
		logger.info(sb.toString());
		
	}

	public boolean isSearched() {
		return searched;
	}

	public Long getTimeExecution() {
		return timeExecution;
	}
	
	public WrapperService getOm(){
		return this.appBean.getWrapper();
	}
	
	public List<?> getRs() {
		return rs;
	}
	
	public Integer getRsSize(){
		if(rs != null){
			return rs.size();
		}
		return 0;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public List<SelectItem> getModeExportDirkList() {
		return modeExportDirkList;
	}

	public String getModeExportDirk() {
		return modeExportDirk;
	}

	public void setModeExportDirk(String modeExportDirk) {
		this.modeExportDirk = modeExportDirk;
	}

	public String getExportDirkUrl() {
		return exportDirkUrl;
	}

	public void setExportDirkUrl(String exportDirkUrl) {
		this.exportDirkUrl = exportDirkUrl;
	}
}
