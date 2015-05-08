package de.mpiwg.itgroup.ismi.util.guiComponents;


import java.io.Serializable;

import javax.faces.component.html.HtmlGraphicImage;

//rich import com.icesoft.faces.component.ext.HtmlGraphicImage;

public class StatusImage extends HtmlGraphicImage implements StatusChecker{


	public static String STATUS_OK = "ok";
	public static String STATUS_UNSET = "unset";
	public static String STATUS_FALSE = "false";
	private static String IMAGEDIR = "/resources/images";
	
	private String status = STATUS_UNSET;

	private String message;
	
	public void setStatus(String status) {
		this.status = status;
		
		if (status=="unset") {
				setStatusUnset();
		} else if 
		(status=="ok") {
			setStatusOk();
		} else if (status=="false") {
		setStatusFalse();
		}
	}
	
	public boolean isOk(){
		if(STATUS_OK.equals(status))
			return true;
		return false;
	}

	private void setStatusOk() {
		setAlt("ok");
		
		setUrl(IMAGEDIR+"/face-smile.png");
		
	}

	private void setStatusFalse() {
		setAlt("false");
		setUrl(IMAGEDIR+"/dialog-error.png");
		
	}

	private void setStatusUnset() {
		setAlt("false");
		setUrl(IMAGEDIR+"/dialog-warning.png");
		
	}

	public String getStatus() {
		return status;
	}
	
	public StatusImage(){
		setStatus("unset");
		setHeight("20");
		setWidth("20");
	}

	public String getMessage() {
		
		return message;
	}
	
	public void setMessage(String msg){
		message=msg;
	}
	
	
}
