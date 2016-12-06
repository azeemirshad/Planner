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
@Table (name = "wf_track_report")
public class WfTrackReport
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@ManyToOne 
	@JoinColumn (name = "planner_id")
	private WfPlanner plannerId;
	
	@Column(name="activity")
	private String activity;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTIVITY_TIME")
	private Date activityTime;
	
	@ManyToOne 
	@JoinColumn (name = "operator")
	private ApplicationUsers operator;
	
	
	public WfTrackReport() 
	{
		// TODO Auto-generated constructor stub
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	


	public String getActivity() {
		return activity;
	}


	public void setActivity(String activity) {
		this.activity = activity;
	}


	public Date getActivityTime() {
		return activityTime;
	}


	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}


	public ApplicationUsers getOperator() {
		return operator;
	}


	public void setOperator(ApplicationUsers operator) {
		this.operator = operator;
	}


	public WfPlanner getPlannerId() {
		return plannerId;
	}


	public void setPlannerId(WfPlanner plannerId) {
		this.plannerId = plannerId;
	}

}
