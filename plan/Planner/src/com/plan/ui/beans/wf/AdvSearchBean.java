package com.plan.ui.beans.wf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;






import com.plan.bll.wf.AddPlannerBll;
import com.plan.dal.dao.WfPlanner;
import com.plan.utils.MessageConstants;
import com.plan.utils.MessageUtils;


@ManagedBean(name="advSearchBean")
@SessionScoped
public class AdvSearchBean
{
	private WfPlanner toSearchPlan;
	private List<WfPlanner> plansList;
	private WfPlanner selectedClient;
	private Date dateFrom ;
	private Date dateTo;
	
	public AdvSearchBean() 
	{
		// TODO Auto-generated constructor stub
		this.toSearchPlan = new WfPlanner();
		this.selectedClient = new WfPlanner();
		this.plansList = new ArrayList<WfPlanner>();
		Calendar calendar = Calendar.getInstance(); 
		
		this.dateFrom = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		this.dateTo =calendar.getTime();
	}
	
	
	public String advacneSearchClients()
	{		
		AddPlannerBll bll =new AddPlannerBll();
		if(dateTo.after(dateFrom)){
			this.plansList = bll.advSearchPlans(toSearchPlan, dateFrom, dateTo);
			System.out.println("Plans list size :: " + this.plansList.size());
		//	initializeNullObjs();
		}else
		{
			MessageUtils.error(MessageConstants.Messages.INVALID_DATE);
		}
		
	
		return "";
	}
	
	
	
	/*private void initializeNullObjs()
	{
		for(WfPlanner r:this.clientsList)
		{
			if(r.getCashPayment()==null || r.getCashPayment().getId()==null)
			{
				r.setCashPayment(new WfClientFinance());
			}
			if(r.getScannedFiles()==null || r.getScannedFiles().getId()==null)
			{
				r.setScannedFiles(new WfClientScannedFiles());
			}
			if(r.getGpe() == null || r.getGpe().getId()==null)
			{
				r.setGpe(new WfClientGpe());
			}
			if(r.getXray() == null || r.getXray().getId()==null)
			{
				r.setXray(new WfClientXray());
			}
			if(r.getSamples() == null || r.getSamples().getId() == null)
			{
				r.setSamples(new WfClientSamples());
			}
			if(r.getUrine() == null || r.getUrine().getId() == null)
			{
				r.setUrine(new WfLabResultUrine());
			}
			if(r.getMicro() == null || r.getMicro().getId() == null )
			{
				r.setMicro(new WfLabResultMicro());
			}
			if(r.getBlood() == null || r.getBlood().getId() == null )
			{
				r.setBlood(new WfLabResultBlood());
			}
			if(r.getSputum() == null || r.getSputum().getId() == null )
			{
				r.setSputum(new WfLabResultSputum());
			}
			if(r.getStool() == null || r.getStool().getId() == null )
			{
				r.setStool(new WfLabResultStool());
			}
			if(r.getProgress() == null || r.getProgress().getId()==null )
			{
				r.setProgress(new WfClientProgress());
			}

			
		}
		
	}*/
	

	
	/**
	 * @return the selectedClient
	 */
	public WfPlanner getSelectedClient() {
		return selectedClient;
	}


	/**
	 * @param selectedClient the selectedClient to set
	 */
	public void setSelectedClient(WfPlanner selectedClient) {
		this.selectedClient = selectedClient;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}


	public WfPlanner getToSearchPlan() {
		return toSearchPlan;
	}


	public void setToSearchPlan(WfPlanner toSearchPlan) {
		this.toSearchPlan = toSearchPlan;
	}


	public List<WfPlanner> getPlansList() {
		return plansList;
	}


	public void setPlansList(List<WfPlanner> plansList) {
		this.plansList = plansList;
	}
	
	

}
