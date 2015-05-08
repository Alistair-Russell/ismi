package de.mpiwg.itgroup.ismi.util.guiComponents;

import java.text.MessageFormat;

public class DataPaginator {
	/* This should be parameters or properties stored somewhere */
	private int itemsPerPage = 10;
	private int rewindFastForwardBy = 10;
	
	private int currentPage;
	private int numberOfPages;
	
	private String recordStatus;
	
	public void first(){
		this.currentPage = 0;
	}
	
	public void last(){
		this.currentPage = this.numberOfPages -1;
	}
	
	public void fastRewind () {
		this.rewind(this.rewindFastForwardBy);
	}
	
	private void rewind (int aRewindFastForwardBy) {
		int newPageNumber = currentPage - aRewindFastForwardBy;
		if (newPageNumber < 0) {
			currentPage = 0;
		} else {
			currentPage = newPageNumber;
		}
	}	
	
	public void goToPage(int newPageNumber){
		if (newPageNumber > this.numberOfPages -1) {
			currentPage = this.numberOfPages -1;
		} else if(newPageNumber < 0 ){
			currentPage = 0;
		}else{
			currentPage = newPageNumber;
		}
		
	}
	
	private void forward (int aRewindFastForwardBy) {
		int newPageNumber = currentPage + aRewindFastForwardBy;
		if (newPageNumber > this.numberOfPages - 1) {
			currentPage = this.numberOfPages -1;
		} else {
			currentPage = newPageNumber;
		}
	}	
	
	public void previous () {
		this.rewind(1);		
	}

	public void next () {
		this.forward(1);
	}
	
	public void fastForward () {
		this.forward(this.rewindFastForwardBy);
	}
	
	public void initCount() {
		if (currentPage > numberOfPages) {
			currentPage=numberOfPages;
		}
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	public void resetNumberOfPages(int itemsTotal){
		int numberOfPages = (itemsTotal
				% this.getItemsPerPage() == 0) ?
						itemsTotal
						/ this.getItemsPerPage() :
							(itemsTotal
							/ this.getItemsPerPage()) + 1;
		this.setNumberOfPages(numberOfPages);
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public String getRecordStatus () {
		this.recordStatus = MessageFormat.format("{0} of {1}", 
				new Object []{
				Integer.valueOf(currentPage + 1),
				Integer.valueOf(numberOfPages)
				});
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public int getRewindFastForwardBy() {
		return rewindFastForwardBy;
	}

	public void setRewindFastForwardBy(int rewindFastForwardBy) {
		this.rewindFastForwardBy = rewindFastForwardBy;
	}
}
