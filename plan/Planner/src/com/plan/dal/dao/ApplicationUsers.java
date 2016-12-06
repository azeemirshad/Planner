package com.plan.dal.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appl_users")
public class ApplicationUsers 
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name="USERNAME")
	private String userName;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="APPOINTMENT")
	private String appointment;
	
	@Column(name="SECTION")
	private String section;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ROLE_ADMIN")
	private String roleAdmin;
	
	@Column(name="ROLE_ADDN")
	private String roleAddn;
	
	@Column(name="ROLE_VIEW")
	private String roleView;
	
	
	@Column(name="PROFILE_STATUS")
	private String profileStatus;
	
	@Column(name="THEME", insertable=false)
	private String theme;
	
	
	
	public ApplicationUsers() 
	{
		// TODO Auto-generated constructor stub
		this.roleAdmin = "N";
		this.roleAddn = "N";
		this.roleView = "N";
		
	}

	
	public String getLoggedUserString()
	 {	 	
	 	return this.appointment+ "-"+this.section+ "-"+this.getName();
	 }
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppointment() {
		return appointment;
	}
	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}


	public String getProfileStatus() {
		return profileStatus;
	}


	public void setProfileStatus(String profileStatus) {
		this.profileStatus = profileStatus;
	}


	public String getRoleAdmin() {
		return roleAdmin;
	}


	public void setRoleAdmin(String roleAdmin) {
		this.roleAdmin = roleAdmin;
	}


	


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}


	public String getSection() {
		return section;
	}


	public void setSection(String section) {
		this.section = section;
	}


	public String getRoleAddn() {
		return roleAddn;
	}


	public void setRoleAddn(String roleAddn) {
		this.roleAddn = roleAddn;
	}


	public String getRoleView() {
		return roleView;
	}


	public void setRoleView(String roleView) {
		this.roleView = roleView;
	}


}
