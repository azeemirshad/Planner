package com.plan.ui.beans;

import java.io.IOException;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.text.StrongTextEncryptor;

import com.iac.web.util.FacesUtils;
import com.plan.dal.UserDal;
import com.plan.dal.dao.ApplicationUsers;
import com.plan.ui.beans.wf.AddPlannerBean;
import com.plan.utils.Environment;
import com.plan.utils.HibernateUtilsAnnot;
import com.plan.utils.MessageConstants;
import com.plan.utils.MessageUtils;
import com.plan.utils.NavigationConstants;

@ManagedBean (name = "userBean" )
@SessionScoped
public class UserBean 
{
	
	private ApplicationUsers toSearchUser;
	private String loggedUserString ;
	private boolean checkSession ;
	public static String KEY_CURRENT_USER = "currentUser";
	private Date currDate = new Date();
	private String defaultPassword;
	
	public UserBean() 
	{
		// TODO Auto-generated constructor stub
		this.toSearchUser = new ApplicationUsers();
		this.checkSession = true;
		this.defaultPassword = Environment.getDefaultPassword();
	}
	
	
	
	
	public String loginAction() 
	{
		KEY_CURRENT_USER = "currentUser";
		
		this.loggedUserString = "";
		System.out.println("In Login Action, value of username is =" + this.toSearchUser.getUserName());
		String password = this.toSearchUser.getPassword();
		if (password == null || password.trim().length() == 0 ) 
		{
//			FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.INVALID_VALUE);
			MessageUtils.error(MessageConstants.Messages.INVALID_VALUE);
			return NavigationConstants.FAILURE;
		} 
		else 
		{
			try{
			UserDal dal = new UserDal();
			ApplicationUsers currentUser = dal.localLogin(this.toSearchUser.getUserName());
			
			if(currentUser!=null && currentUser.getId()!=null && currentUser.getId()>0)
			{
				StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
//				String myEncryptionPassword = "1";
				textEncryptor.setPassword(MessageConstants.Constants.PASSWORD_KEY);
				String myEncryptedText = currentUser.getPassword();
				String plainText = textEncryptor.decrypt(myEncryptedText);
				if(plainText.equals(this.toSearchUser.getPassword()))
				{
					FacesUtils.putIntoSession(KEY_CURRENT_USER, currentUser);
					System.out.println(KEY_CURRENT_USER);
					System.out.println("Abt to return");
					//(( AddPlannerBean )FacesUtils.getManagedBean( "addPlannerBean" ) ).searchCurrentCmts();
					this.loggedUserString = ((ApplicationUsers) FacesUtils.getFromSession(KEY_CURRENT_USER))
							.getLoggedUserString();
					System.out.println(this.loggedUserString);
					return (( PageNavigationBean )FacesUtils.getManagedBean( "navBean" ) ).navHomePage();
				}
				else 
				{
//					FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.INVALID_PASSWORD);
					MessageUtils.error(MessageConstants.Messages.INVALID_PASSWORD);
					return  "";
				}
				
			}
			else 
			{
//				FacesUtils.addErrorMessage("Login credentials", MessageConstants.Messages.INVALID_USERNAME);
				MessageUtils.error(MessageConstants.Messages.INVALID_USERNAME);
				return  "";
			}
			}catch(Exception e)
			{
				MessageUtils.error("Error!" + e.getMessage());
				return "";
			}

		}

		//return NavigationConstants.HOME_NAVIGATION;
	}
	
	public String searchUser()
	{
		Session session = null;
		
		System.out.println("In search User Method");
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			
			Criteria cr = session.createCriteria(ApplicationUsers.class);
			cr.add(Restrictions.eq("userName", this.toSearchUser.getUserName()));
			ApplicationUsers appUser = (ApplicationUsers)cr.uniqueResult();
			
			
			StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			String myEncryptionPassword = "1";
			textEncryptor.setPassword(myEncryptionPassword);
			String myEncryptedText = appUser.getPassword();
			String plainText = textEncryptor.decrypt(myEncryptedText);
			if(plainText.equals(this.toSearchUser.getPassword()))
			{
				System.out.println("Ok");
			}
			else
			{
				System.out.println("Wrong");
			}
			
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return "";
	}
	
	public ApplicationUsers getCurrentUser() {
		Object obj = FacesUtils.getFromSession(KEY_CURRENT_USER);
		if (obj != null) {
			return (ApplicationUsers) obj;
		}

		return null;
	}
	
	public String logoutUser()  
	{
		System.out.println("Logging out");
		UserBean.KEY_CURRENT_USER = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//		FacesContext.getCurrentInstance().getExternalContext().redirect("logout.xhtml");
		this.checkSession = false;
		//return ((PageNavigationBean)FacesUtils.getManagedBean("navBean")).navLogOut();
		return "/pages/login.xhtml?faces-redirect=true";
		
	}
	
	
	
	public void logout()  
	{
		System.out.println("Logging out");
		UserBean.KEY_CURRENT_USER = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//		FacesContext.getCurrentInstance().getExternalContext().redirect("logout.xhtml");
		this.checkSession = false;
		//return ((PageNavigationBean)FacesUtils.getManagedBean("navBean")).navLogOut();
		//return "/pages/login.xhtml?faces-redirect=true";
		
	}


	public ApplicationUsers getToSearchUser() {
		return toSearchUser;
	}


	public void setToSearchUser(ApplicationUsers toSearchUser) {
		this.toSearchUser = toSearchUser;
	}


	public String getLoggedUserString() {
		return loggedUserString;
	}


	public void setLoggedUserString(String loggedUserString) {
		this.loggedUserString = loggedUserString;
	}


	public boolean isCheckSession() 
	{
		checkSession = true;
		if (this.loggedUserString == null || this.loggedUserString.trim().length() == 0) {
			UserBean.KEY_CURRENT_USER = null;
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("logout.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
			checkSession = false;
		}
		return checkSession;
	}


	public void setCheckSession(boolean checkSession) {
		this.checkSession = checkSession;
	}


	public static String getKEY_CURRENT_USER() {
		return KEY_CURRENT_USER;
	}




	public Date getCurrDate() {
		return currDate;
	}




	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}




	public String getDefaultPassword() {
		return defaultPassword;
	}




	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	

}
