package com.plan.dal.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table (name = "wf_attended_by")
public class WfAttendedBy
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@ManyToOne 
	@JoinColumn (name = "planner_id")
	private WfPlanner plannerId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="IS_DELETE")
	private String isDelete;
	
	
	
	
	public WfAttendedBy() 
	{
		// TODO Auto-generated constructor stub
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public WfPlanner getPlannerId() {
		return plannerId;
	}


	public void setPlannerId(WfPlanner plannerId) {
		this.plannerId = plannerId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIsDelete() {
		return isDelete;
	}


	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}


	
}
