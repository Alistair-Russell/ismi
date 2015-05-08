package org.mpiwg.itgroup.escidoc;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;

public class AbstractESciDocCache implements Serializable{
	
	private static final long serialVersionUID = 2093872792493279298L;

	private static Integer SERVER_STATUS_OK = 0;
	private static Integer SERVER_STATUS_DOWN = 1;
	
	public static int SORT_NONE = -1;
	public static int SORT_DEFAULT = 0;
	public static int SORT_TITLE = 1;
	public static int SORT_CREATOR = 2;
	
	
	private int serverStatus = SERVER_STATUS_OK;
	private Long lastSynchronizationTimeExec;
	private Date lastSynchronization;
	private int lastSynchronizationMaxRecords;
	private final static DateFormat dateformater = new SimpleDateFormat("yyyy.MM.dd-HH:mm");
	
	protected ESciDocClient client = new ESciDocClient("escidoc.mpiwg-berlin.mpg.de", 8080, "jurzua", "221082");
	
	private Map<String,ESciDocItem> itemMap;
	private Integer maxRecords = 50;
	
	public Map<String,ESciDocItem> getItemMap(){
		if(itemMap == null || SERVER_STATUS_DOWN.equals(serverStatus)){
			this.synchronizeWithServer();
		}
		return itemMap;
	}
	
	public int getItemsSize(){
		return this.getItemMap().size();
	}
	
	public void synchronizeWithServer(){
		
		System.out.println("synchronizeWithServer Pubman");
		
		long start = System.currentTimeMillis();
		this.itemMap = new HashMap<String, ESciDocItem>();
		this.lastSynchronizationMaxRecords = this.maxRecords;
		try {
			//List<ESciDocItem> list = new ArrayList<ESciDocItem>(); //ESciDocClient.getAllItems();
			
			List<ESciDocItem> list = new ArrayList<ESciDocItem>();   //ESciDocClient.getAllItems(this.maxRecords);
			for(ESciDocItem item : list){
				this.itemMap.put(item.getPublication().getObjid(), item);
			}
			this.serverStatus = SERVER_STATUS_OK;
		} catch (Exception e) {
			e.printStackTrace();
			this.serverStatus = SERVER_STATUS_DOWN;
		} finally{
			this.lastSynchronization = new Date();
		}
		this.lastSynchronizationTimeExec = System.currentTimeMillis() - start;
	}
	
	public String getServerStatus(){
		if(SERVER_STATUS_OK.equals(serverStatus)){
			return new String("OK");
		}else{
			return new String("Server did not respond.");
		}
	}
	
	public String getLastSynchronization() {
		return dateformater.format(lastSynchronization);
	}

	public boolean isServerStatusOK(){
		if(SERVER_STATUS_OK.equals(SERVER_STATUS_OK))
			return true;
		return false;
	}

	public Long getLastSynchronizationTimeExec() {
		return lastSynchronizationTimeExec;
	}

	public Integer getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(Integer maxRecords) {
		this.maxRecords = maxRecords;
	}

	public int getLastSynchronizationMaxRecords() {
		return lastSynchronizationMaxRecords;
	}
	
}
