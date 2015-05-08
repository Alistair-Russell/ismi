package org.mpiwg.itgroup.escidoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.mpi.openmind.repository.utils.NormalizerUtils;
import org.mpiwg.itgroup.escidoc.bo.Creator;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;
import org.mpiwg.itgroup.escidoc.bo.utils.SortItemByCreator;
import org.mpiwg.itgroup.escidoc.bo.utils.SortItemByTitle;

public class ESciDocCache extends AbstractESciDocCache{
	
	private static final long serialVersionUID = 2087364857910704625L;
	
	private static Logger logger = Logger.getLogger(ESciDocCache.class);
	
	public ESciDocCache(){
		this.synchronizeWithServer();
	}
	
	public ESciDocItem getItemFromServer(String id) throws IOException, JDOMException, ESciDocException, Exception{
		System.out.println("ESciDocCache.getItemFromServer " + id);
		return this.client.getItemX(id);
	}
	
	public ESciDocItem getItem(String id){
		System.out.println("ESciDocCache.getItem " + id);
		return this.getItemMap().get(id);
	}
	
	public void deleteItem(ESciDocItem item, String userName) throws Exception{
		logger.info("Deleting " + item.getObjId());
		
		/*
		if(item.getVersionStatus().equals("released")){
			//only doing a change, the update of the item will change its status to pending,
			//otherwise I do not see a way to move from released to pending (in-revision would also work).
			item.getPublication().setTitle(item.getPublication().getTitle() + ".");
			item = this.client.updateItem(item);
			item.setLastModificationDate(this.client.submitItem(item, userName));
			item.setLastModificationDate(this.client.reviseItem(item, userName));
			
		}*/
		
		if(item.getPublicStatus().equals("pending") || item.getPublicStatus().equals("in-revision")){
			this.client.deleteItem(item.getObjId());
		}else{
			this.client.withdrawItem(item, userName);
		}
		
		
		this.getItemMap().remove(item.getObjId());
	}
	
	public ESciDocItem saveItem(ESciDocItem item, String userName) throws Exception{
		
		logger.info("Saving " + item.getObjId());
		
		if(item != null && StringUtils.isNotEmpty(item.getObjId())){
			
			item = this.client.updateItem(item);	
			
			
		}else if(StringUtils.isEmpty(item.getObjId())){
			
			item = this.client.createItem(item);
			
		}
			
		if(item.getVersionStatus().equals("pending")){
			//Submitting
			item.setLastModificationDate(this.client.submitItem(item, userName));
			item.setLastModificationDate(this.client.releaseItem(item, userName));
		}
		
		if(item.getVersionStatus().equals("submitted")){
			//Releasing
			item.setLastModificationDate(this.client.releaseItem(item, userName));	
		}
		
		this.getItemMap().put(item.getObjId(), item);
			
		return item;
	}
	
	
	public List<ESciDocItem> getAllItems(Integer sortBy){
		List<ESciDocItem> list = new ArrayList<ESciDocItem>(getItemMap().values());
		sort(sortBy, list);
		return list;
	}
	
	public static void sort(Integer sortBy, List<ESciDocItem> list){
		if(sortBy == null || sortBy.equals(SORT_NONE)){
			return;
		}else if(sortBy.equals(SORT_DEFAULT)){
			Collections.sort(list);
		}else if(sortBy.equals(SORT_TITLE)){
			Collections.sort(list, new SortItemByTitle());
		}else if(sortBy.equals(SORT_CREATOR)){
			Collections.sort(list, new SortItemByCreator());
		}
	}
	
	public void listenerSynchronizeWithServer(ActionEvent event){
		this.synchronizeWithServer();
	}
	
	public List<ESciDocItem> searchItem(String term, Integer sortBy){
		List<ESciDocItem> rs = new ArrayList<ESciDocItem>();
		
		if(StringUtils.isNotEmpty(term)){
			term = NormalizerUtils.normalize(term);
			
			for(ESciDocItem item : getItemMap().values()){
				if(itemContainsTerm(item, term)){
					rs.add(item);
				}
			}
			sort(sortBy, rs);
		}
		return rs;
	}
	
	private boolean itemContainsTerm(ESciDocItem item, String term){
		
		if(item.getObjId().contains(term))
			return true;
		
		String secondTerm = item.getPublication().getNorTitle();
		if(secondTerm.contains(term))
			return true;
		
		secondTerm = item.getPublication().getNorSubject();
		if(secondTerm.contains(term))
			return true;
		
		for(String secondTerm0 : item.getPublication().getNorAlternativeList()){
			if(secondTerm0.contains(term))
				return true;
		}
		
		for(Creator creator : item.getPublication().getCreatorList()){
			if(creator.containsTerm(term))
				return true;
		}
		
		return false;
	}
	

}
