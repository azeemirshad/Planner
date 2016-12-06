package com.plan.ui.beans.admin;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;






import com.iac.web.util.FacesUtils;
import com.plan.bll.admin.AdminBll;
import com.plan.bll.wf.AddPlannerBll;
import com.plan.dal.dao.ApplicationUsers;
import com.plan.dal.dao.WfPlanner;
import com.plan.ui.beans.UserBean;
import com.plan.utils.Environment;
import com.plan.utils.MessageConstants;
import com.plan.utils.MessageUtils;
import com.plan.utils.NavigationConstants;

@ManagedBean(name="adminBean")
@SessionScoped
public class AdminBean 
{
	
	private ApplicationUsers toAddUser;
	private ApplicationUsers toSearchUser;
	private List<ApplicationUsers> usersList;
	private ApplicationUsers currentUser;
	private String newPassword = "";
	private String newPasswordAgain = "";
	
	private WfPlanner selectedClient;
	private String scannedFileType;
	
	
	
	public AdminBean() 
	{		
		// TODO Auto-generated constructor stub
		toAddUser = new ApplicationUsers();
		toSearchUser = new ApplicationUsers();
		this.usersList = new ArrayList<ApplicationUsers>();
		this.selectedClient = new WfPlanner();
	}
	
	public String addNewUser()
	{
		
		AdminBll bll =new AdminBll();
		if(bll.addNewUser(toAddUser))
		{
//			FacesUtils.addInfoMessage("Login credentials", MessageConstants.Messages.SAVE_SUCCESS);
			MessageUtils.info(MessageConstants.Messages.SAVE_SUCCESS);
			this.toAddUser = new ApplicationUsers();
		}
		else
		{
//			FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.SAVE_FAILURE);
			MessageUtils.error(MessageConstants.Messages.SAVE_FAILURE);
		}
		
		return "";
	}

	public String searchUsers()
	{
//		if(this.usersList.size()==0)
		{
			AdminBll bll =new AdminBll();
			this.usersList = bll.searchAllUser(toSearchUser);			
		}
		return "";
	}
	
	public String updateUsers()
	{

		AdminBll bll =new AdminBll();
		if(usersList.size()>0 &&  bll.updateUsers(usersList))
		{
//			FacesUtils.addInfoMessage(MessageConstants.Messages.UPDATE_SUCCESS);
			MessageUtils.info(MessageConstants.Messages.UPDATE_SUCCESS);
			this.usersList = bll.searchAllUser(toSearchUser);			
		}
		else
		{
//			FacesUtils.addErrorMessage(MessageConstants.Messages.UPDATE_FAILURE);
			MessageUtils.error(MessageConstants.Messages.UPDATE_FAILURE);
		}

		return "";
	}
	
	public String resetPassword()
	{
		AdminBll bll =new AdminBll();
		newPassword = Environment.getDefaultPassword();
		if(bll.changePassword(toSearchUser,newPassword))
		{
//			FacesUtils.addInfoMessage("Login credentials", MessageConstants.Messages.UPDATE_SUCCESS);
			MessageUtils.info(MessageConstants.Messages.UPDATE_SUCCESS);
			return NavigationConstants.ADMIN_MANAGE_USERS_NAVIGATION;			
		}
		else
		{
//			FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.UPDATE_FAILURE);
			MessageUtils.error(MessageConstants.Messages.UPDATE_FAILURE);
		}

		return "";
	}
	
	public String changePassword()
	{
		currentUser = ((UserBean)FacesUtils.getManagedBean("userBean")).getCurrentUser();
		AdminBll bll =new AdminBll();
		
		if(newPassword==null || newPasswordAgain==null || 
				newPassword.trim().length()==0 	|| newPasswordAgain.trim().length()==0
				)
		{
//			FacesUtils.addErrorMessage("Login credentials", "Invalid Password");
			MessageUtils.error(MessageConstants.Messages.INVALID_PASSWORD);
			return "";
		}
		
		if(!newPassword.equals(newPasswordAgain))
		{
//			FacesUtils.addErrorMessage("Login credentials", "Passwords do not match");
			MessageUtils.error("Passwords do not match");
			return "";
		}
		
		if(bll.changePassword(currentUser,newPassword))
		{
//			FacesUtils.addInfoMessage("Login credentials", MessageConstants.Messages.UPDATE_SUCCESS);
			MessageUtils.info(MessageConstants.Messages.UPDATE_SUCCESS);
			return NavigationConstants.HOME_NAVIGATION;			
		}
		else
		{
//			FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.UPDATE_FAILURE);
			MessageUtils.error(MessageConstants.Messages.UPDATE_FAILURE);
		}

		return "";
	}
	
	
	public List<String> completeSections(String query){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllSections();
		
	}
	
	public List<String> completeAppointments(String query){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllAppointments();
		
	}
	
	public ApplicationUsers getToAddUser() {
		return toAddUser;
	}

	public void setToAddUser(ApplicationUsers toAddUser) {
		this.toAddUser = toAddUser;
	}

	public List<ApplicationUsers> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<ApplicationUsers> usersList) {
		this.usersList = usersList;
	}

	public ApplicationUsers getToSearchUser() {
		return toSearchUser;
	}

	public void setToSearchUser(ApplicationUsers toSearchUser) {
		this.toSearchUser = toSearchUser;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordAgain() {
		return newPasswordAgain;
	}

	public void setNewPasswordAgain(String newPasswordAgain) {
		this.newPasswordAgain = newPasswordAgain;
	}
	public String getScannedFileType() {
		return scannedFileType;
	}

	public void setScannedFileType(String scannedFileType) {
		this.scannedFileType = scannedFileType;
	}

	public WfPlanner getSelectedClient() {
		return selectedClient;
	}

	public void setSelectedClient(WfPlanner selectedClient) {
		this.selectedClient = selectedClient;
	}

}
