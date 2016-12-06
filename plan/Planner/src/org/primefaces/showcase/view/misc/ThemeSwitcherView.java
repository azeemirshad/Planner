package org.primefaces.showcase.view.misc; 
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.showcase.domain.Theme;
import org.primefaces.showcase.service.ThemeService;

import com.iac.web.util.FacesUtils;
import com.plan.bll.admin.AdminBll;
import com.plan.dal.dao.ApplicationUsers;
import com.plan.ui.beans.UserBean;
 
@ManagedBean(name = "themeSwitcherView")
@SessionScoped
public class ThemeSwitcherView {
 
    private List<Theme> themes;
    private String selectedTheme;
//    private Theme selTheme;
     
    
    
    
    public ThemeSwitcherView() {
//		this.selectedTheme = "bootstrap";
    	System.out.println("Inside Construct " );
//		 selTheme = new Theme(); 
		 
	}

	@ManagedProperty("#{themeService}")
    private ThemeService service;
 
    @PostConstruct
    public void init() {
    	System.out.println("Inside init @PostConstruct " );
        themes = service.getThemes();
       
        this.selectedTheme = ((UserBean)FacesUtils.getManagedBean("userBean")).getCurrentUser().getTheme();
    }
     
    public void saveTheme(){
    	ApplicationUsers currentUser =  ((UserBean)FacesUtils.getManagedBean("userBean")).getCurrentUser();
    	System.out.println("Saving theme >>>>>>>>>>>>>>>>>>>>>>>>>>> " + this.getSelectedTheme());
//    	System.out.println("Setting theme >>>>>>>>>>>>>>>>>>>>>>>>>>> " + this.getSelTheme().getName());
    	currentUser.setTheme(this.getSelectedTheme());
    	List<ApplicationUsers> userList = new ArrayList<ApplicationUsers>();
    	userList.add(currentUser);
    	new AdminBll().updateUsers(userList);
    }
    
    
    public List<Theme> getThemes() {
        return themes;
    } 
 
    public void setService(ThemeService service) {
        this.service = service;
    }

	public String getSelectedTheme() {
//		System.out.println("Inside selectedTheme getter " + selectedTheme );
//		this.selectedTheme = ((UserBean)FacesUtils.getManagedBean("userBean")).getCurrentUser().getTheme();
		return selectedTheme;
	}

	public void setSelectedTheme(String selectedTheme) {
//		System.out.println("Inside selectedTheme setter " + selectedTheme );
		this.selectedTheme = selectedTheme;
	}

	public ThemeService getService() {
		return service;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

//	public Theme getSelTheme() {
//		System.out.println("Inside Theme getter " + selTheme.getName() );
//		return selTheme;
//	}
//
//	public void setSelTheme(Theme selTheme) {
//		//System.out.println("Inside Theme setter " + selTheme.getDisplayName() );
//		System.out.println("Inside Theme setter " + selTheme.getName() );
//		
//		this.selTheme = selTheme;
//	}
}