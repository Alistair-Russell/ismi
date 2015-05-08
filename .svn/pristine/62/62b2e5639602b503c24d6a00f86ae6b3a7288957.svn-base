package de.mpiwg.itgroup.ismi.admin;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.security.bo.User;
import org.mpiwg.itgroup.escidoc.bo.ESciDocItem;


import de.mpiwg.itgroup.ismi.entry.beans.AbstractISMIBean;

public class AdminBean  extends AbstractISMIBean{

	public static String Administrator = "Administrator";
	public static String Researcher = "Researcher";
	public static String Student = "Student";
	
	private static List<SelectItem> roleList = new ArrayList<SelectItem>();
	static{
		roleList.add(new SelectItem(Administrator));
		roleList.add(new SelectItem(Researcher));
		roleList.add(new SelectItem(Student));
	}
	
	public AdminBean(){
		this.reset();
	}
	
	private User user;
	private List<User> userList;
	
	private String userEmail;
	private String userName;

	private String password1;
	private String password2;
	private boolean changePassword;
	
	private boolean showESciDocPanelControl = false;
	
	public void listenerOpenESciDocPanelControl(ActionEvent event){
		this.showESciDocPanelControl = true;
	}
	
	public void listenerCloseESciDocPanelControl(ActionEvent event){
		this.showESciDocPanelControl = false;
	}
	
	public boolean isShowESciDocPanelControl() {
		return showESciDocPanelControl & getSessionBean().isAdmin();
	}

	public void reset(){
		this.user = null;
		this.password1 = new String();
		this.password2 = new String();
		this.changePassword = false;
		this.userEmail = new String();
		this.userName = new String();
	}

	public void actionCreateUser(ActionEvent event){
		this.user = new User();
		this.user.setRole(Researcher);
		this.changePassword = true;
		this.userList = new ArrayList<User>();
	}
	
	public void actionEditUser(ActionEvent event){
		this.user = (User)getRequestBean("item");
		this.changePassword = false;
		this.userList = new ArrayList<User>();
	}
	
	public void actionRemoveUser(ActionEvent event){
		User user0 = (User)getRequestBean("item");
		getSecurityService().deleteUser(user0);
		this.userList = getSecurityService().getAllUsers();
	}
	
	public void actionReset(ActionEvent event){
		reset();
	}
	
	public void actionGetAllUsers(ActionEvent event){
		this.userList = getSecurityService().getAllUsers();
		this.user = null;

	}
	
	public void actionSaveUser(ActionEvent event){
		boolean valid = true;
		if(changePassword && (StringUtils.isEmpty(password1) || 
				StringUtils.isEmpty(password2) || 
				!password1.equals(password2))){
			valid = false;
			addErrorMsg("The passwords are different or at least one is empty");
		}
		
		User otherUser = getSecurityService().getUserByEmail(user.getEmail());
		
		//TODO check email
		
		if(!user.isPersistent() && otherUser != null){
			addErrorMsg("The email is the key and should be unique. A user already exists with this email.");
			valid = false;
		}else if(user.isPersistent() && otherUser != null && !otherUser.getId().equals(user.getId())){
			addErrorMsg("Other user is using this email.");
			valid = false;
		}
		
		if(valid){
			if(!user.isPersistent()){
				user.setPassword(password1);
			}
			getSecurityService().saveUser(user);
			this.changePassword = false;
			this.user = null;
			this.userList = getSecurityService().getAllUsers();
		}
		
	}
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public List<SelectItem> getRoleList(){
		return roleList;
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
	
	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
