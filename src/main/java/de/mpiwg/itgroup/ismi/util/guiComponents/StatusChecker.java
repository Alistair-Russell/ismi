package de.mpiwg.itgroup.ismi.util.guiComponents;

public interface StatusChecker {

	public String getStatus(); // uergibt den status, soll sein "ok", "false", "unset".
	
	public String getMessage(); // message for display
	
	public void setMessage(String message); // sets the message
}
