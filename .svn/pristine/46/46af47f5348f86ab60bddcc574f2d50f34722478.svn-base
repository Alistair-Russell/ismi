package de.mpiwg.itgroup.ismi.profile;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.security.bo.User;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;
import de.mpiwg.itgroup.ismi.entry.beans.SessionBean;

public class ProfileBean extends AbstractISMIBean implements Serializable{
	private static final long serialVersionUID = 8718895052765908194L;
	
	private String password1;
	private String password2;
	private String oldPassword;
	
	
	public String actionChangePassword(){
		boolean valid = true;
		if(StringUtils.isEmpty(password1) || StringUtils.isEmpty(password2) || !password1.equals(password2)){
			addErrorMsg("The new passwords are different.");
			valid = false;
		}
		
		User user = getSecurityService().getUserByPassword(getSessionUser().getEmail(), oldPassword);
		if(user == null){
			addErrorMsg("The old password was not correct.");
			valid = false;
		}
		
		if(valid){
			user.setPassword(password1);
			getSecurityService().saveUser(user);
			getSessionBean().setUser(user);
			
			this.password1 = new String();
			this.password2 = new String();
			this.oldPassword = new String();
			
			return SessionBean.PAGE_SIMPLE_SEARCH;
		}
		return "";
	}
	
	
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	
	
	
}
