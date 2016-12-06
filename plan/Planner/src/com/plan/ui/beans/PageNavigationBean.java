/**
 * 
 */
package com.plan.ui.beans;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.iac.web.util.FacesUtils;
import com.plan.dal.dao.WfAttendedBy;
import com.plan.ui.beans.admin.CriteriaBean;
import com.plan.ui.beans.wf.AddPlannerBean;
import com.plan.utils.MessageConstants;
import com.plan.utils.NavigationConstants;

/**
 * @author 
 *
 */
@ManagedBean(name = "navBean")
@ViewScoped
public class PageNavigationBean implements Serializable 
{

	private static final long serialVersionUID = 1L;
	private String pageName;
	
	private CriteriaBean cb = (CriteriaBean)FacesUtils.getManagedBean("crit");
	

	@PostConstruct
	public void init() {
		System.out.println("PageNavigationBean init");
		pageName = NavigationConstants.HOME_NAVIGATION;
	}

	public String navHomePage() {
		System.out.println("PageNavigationBean defaultPage.xhtml");
		(( AddPlannerBean )FacesUtils.getManagedBean( "addPlannerBean" ) ).searchCurrentCmts();
		pageName = NavigationConstants.HOME_NAVIGATION;
//		pageTitle = MessageConstants.Constants.PageTitles.HOME_PAGE;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.HOME_PAGE);
		return pageName;
	}
	
	public String navLogOut() {
		System.out.println("PageNavigationBean logout.xhtml");
//		UserBean.KEY_CURRENT_USER = null;
//		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//		pageName = NavigationConstants.LOGOUT_NAVIGATION;
		return pageName;
	}

	public String navAddPlanner() {
		System.out.println("PageNavigationBean navAddPlanner");
		FacesUtils.resetManagedBean("addPlannerBean");
		pageName = NavigationConstants.ADD_PLAN_NAVIGATION;
//		pageTitle = MessageConstants.Constants.PageTitles.REGISTER_CLIENT;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADD_PLAN);
		return pageName;
	}
	
	public String navUpdatePlanner() 
	{
		FacesUtils.resetManagedBean("addPlannerBean");
		pageName = NavigationConstants.UPDATE_CLIENT_NAVIGATION;
//		pageTitle = MessageConstants.Constants.PageTitles.UPDATE_CLIENT;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.UPDATE_PLAN);
		return pageName;
	}
	
	public String navUpdatePlannerDetails() 
	{
		System.out.println("PageNavigationBean .... navUpdatePlannerDetails");
		pageName = NavigationConstants.UPDATE_PLAN_DETAILS_NAVIGATION;
		AddPlannerBean bean = (( AddPlannerBean )FacesUtils.getManagedBean( "addPlannerBean" ) );
		bean.getAttendedByList().setTarget(bean.getToAddPlan().getAttendedBy());
		
//		for (WfAttendedBy attendedBy : bean.getToAddPlan().getAttendedBy()) {
//			System.out.println("Adding in target .... " + attendedBy.getName());
//			bean.getAttendedByList().getTarget().add(attendedBy.getName());
//		}
		
		cb.setPageTitle(MessageConstants.Constants.PageTitles.UPDATE_PLAN);
		return pageName;
	}
	
	
	public String navAdvanceSearchDetails() 
	{
		pageName = NavigationConstants.ADVANCE_SEARCH_DETAILS_NAVIGATION;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADV_SEARCH);
		return pageName;
	}
	
	public String navAdvanceSearch() 
	{
		FacesUtils.resetManagedBean("advSearchBean");
		pageName = NavigationConstants.SEARCH_NAVIGATION;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADV_SEARCH);
		return pageName;
	}
	
	
	

	public String navAdminAddUsersPage() {
		System.out.println("PageNavigationBean admin/addUsers.xhtml");
		FacesUtils.resetManagedBean("adminBean");
		
		pageName = NavigationConstants.ADMIN_ADD_USERS_NAVIGATION;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADMIN);
		return pageName;
	}
	
	public String navAdminManageUsersPage() {
		System.out.println("PageNavigationBean admin/manageUsers.xhtml");
		FacesUtils.resetManagedBean("adminBean");
		pageName = NavigationConstants.ADMIN_MANAGE_USERS_NAVIGATION;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADMIN);
		return pageName;
	}

	public String navAdminChangePassword() 
	{
		FacesUtils.resetManagedBean("adminBean");
		pageName = NavigationConstants.ADMIN_CHANGE_PASSWORD;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADMIN);
		return pageName;
	}
	
	public String navAdminResetClient() 
	{
		FacesUtils.resetManagedBean("resetClientBean");
		pageName = NavigationConstants.ADMIN_RESET_CLIENT;
		cb.setPageTitle(MessageConstants.Constants.PageTitles.ADMIN);
		return pageName;
	}


	
	
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName
	 *            the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}
