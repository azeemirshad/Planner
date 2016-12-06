package com.plan.ui.beans.wf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

import sun.misc.BASE64Decoder;

import com.iac.web.util.FacesUtils;
import com.plan.bll.wf.AddPlannerBll;
import com.plan.dal.dao.WfAttendedBy;
import com.plan.dal.dao.WfPlanner;
import com.plan.ui.beans.PageNavigationBean;
import com.plan.ui.beans.UserBean;
import com.plan.utils.Environment;
import com.plan.utils.MessageConstants;
import com.plan.utils.MessageUtils;
import com.plan.utils.NavigationConstants;
import com.plan.utils.MessageConstants.Constants;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;



@ManagedBean(name="addPlannerBean")
@SessionScoped
public class AddPlannerBean 
{
	
	private WfPlanner toAddPlan;
	private WfPlanner toSearchPlan;
	private List<WfPlanner> plansList;
	private WfPlanner selectedPlan;
	private DualListModel<WfAttendedBy> attendedByList;
	private WfAttendedBy newAttendedBy;
	private List<WfPlanner> filteredPlansList;
	

	
	
	public AddPlannerBean() 
	{
		// TODO Auto-generated constructor stub
		this.toAddPlan = new WfPlanner();
		//this.filteredPlansList = new ArrayList<WfPlanner>();
		this.toSearchPlan = new WfPlanner();
		newAttendedBy = new WfAttendedBy();
		newAttendedBy.setIsDelete(MessageConstants.Constants.NO_STRING);
		this.selectedPlan = new WfPlanner();
		AddPlannerBll bll =new AddPlannerBll();
		List<WfAttendedBy> attendedBySource = bll.getAllAttendedBys();

        List<WfAttendedBy> attendedByTarget = new ArrayList<WfAttendedBy>();
        this.attendedByList = new DualListModel<WfAttendedBy>(attendedBySource, attendedByTarget);
        
		
	}

	
	public String getCurrentCmts()
	{	
		AddPlannerBll bll =new AddPlannerBll();
		this.plansList = bll.getCurrentCmts();
		System.out.println("**************** getCurrentCmts List retreived ... " + plansList.size());
		return "";
	}
	
	public List<String> getAllSections(){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllSections();
		
	}
	
	public void searchCurrentCmts()
	{	
		AddPlannerBll bll =new AddPlannerBll();
		this.plansList = bll.getCurrentCmts();
		System.out.println("**************** searchCurrentCmts List retreived ... " + plansList.size());
		
	}

	public List<String> completeLocations(String query){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllLocations();
		
	}

	public List<String> completeChairedBys(String query){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllChairedBys();
		
	}
	
	public List<String> getChairedBys(){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllChairedBys();
		
	}
	
	public List<String> attendedByList(String query){
		AddPlannerBll bll =new AddPlannerBll();
		return bll.getAllChairedBys();
		
	}
	
	public void addAttendedBy(){
		System.out.println("Adding attended by ... " + newAttendedBy.getName() );
//		WfAttendedBy temp = new WfAttendedBy();
//		temp.setIsDelete(MessageConstants.Constants.NO_STRING);
//		temp.setName(newAttendedBy);
//		System.out.println("attendedByList source before size. " + this.attendedByList.getSource().size() );
		if(!newAttendedBy.getName().trim().equals(""))
			this.attendedByList.getTarget().add(newAttendedBy);
//		System.out.println("attendedByList source size. " + this.attendedByList.getSource().size() );
		
//		newAttendedBy = "";
		newAttendedBy = new WfAttendedBy();
		newAttendedBy.setIsDelete(MessageConstants.Constants.NO_STRING);
	}


	public String addPlan()
	{
		System.out.println("in add planner method");
		try{
			
			toAddPlan.setIsDelete(MessageConstants.Constants.NO_STRING);
			toAddPlan.setAttendedBy(new ArrayList<WfAttendedBy>());
			//WfAttendedBy attendedBy = null;
			this.toAddPlan.setAttendedBy(this.attendedByList.getTarget());
			for (WfAttendedBy target : this.toAddPlan.getAttendedBy()) {
				System.out.println("Target List item :: " + target.getName());
				target.setPlannerId(this.toAddPlan);
//				attendedBy = new WfAttendedBy();
//				attendedBy.setIsDelete(MessageConstants.Constants.NO_STRING);
//				attendedBy.setName(target);
//				attendedBy.setPlannerId(this.toAddPlan);
//				toAddPlan.getAttendedBy().add(attendedBy);
			}
			
			if(new AddPlannerBll().addPlan(toAddPlan))
			{
				this.toAddPlan = new WfPlanner();
				AddPlannerBll bll =new AddPlannerBll();
				List<WfAttendedBy> attendedBySource = bll.getAllAttendedBys();
		        List<WfAttendedBy> attendedByTarget = new ArrayList<WfAttendedBy>();
				this.attendedByList = new DualListModel<WfAttendedBy>(attendedBySource, attendedByTarget);
				
				MessageUtils.info("Data saved/updated successfully");
	//			return NavigationConstants.HOME_NAVIGATION;
			}
			else
			{
				MessageUtils.error("Error saving data");
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.error("Error saving data..." + e.getMessage());
		}
		
		
		return "";
	}
	
	public String updatePlan()
	{
		System.out.println("in update planner method");
		try{
			
			toAddPlan.setIsDelete(MessageConstants.Constants.NO_STRING);
			toAddPlan.setAttendedBy(new ArrayList<WfAttendedBy>());
			//WfAttendedBy attendedBy = null;
			this.toAddPlan.setAttendedBy(this.attendedByList.getTarget());
			for (WfAttendedBy target : this.toAddPlan.getAttendedBy()) {
				System.out.println("Target List item :: " + target.getName());
				target.setPlannerId(this.toAddPlan);
//				attendedBy = new WfAttendedBy();
//				attendedBy.setIsDelete(MessageConstants.Constants.NO_STRING);
//				attendedBy.setName(target);
//				attendedBy.setPlannerId(this.toAddPlan);
//				toAddPlan.getAttendedBy().add(attendedBy);
			}
			
			if(new AddPlannerBll().addPlan(toAddPlan))
			{
				this.toAddPlan = new WfPlanner();
				AddPlannerBll bll =new AddPlannerBll();
				List<WfAttendedBy> attendedBySource = bll.getAllAttendedBys();
		        List<WfAttendedBy> attendedByTarget = new ArrayList<WfAttendedBy>();
				this.attendedByList = new DualListModel<WfAttendedBy>(attendedBySource, attendedByTarget);
				//this.searchCurrentCmts();
				MessageUtils.info("Data saved/updated successfully");
				return (( PageNavigationBean )FacesUtils.getManagedBean( "navBean" ) ).navHomePage();
//				return NavigationConstants.HOME_NAVIGATION;

			}
			else
			{
				MessageUtils.error("Error saving data");
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.error("Error saving data..." + e.getMessage());
		}
		
		
		return "";
	}
	
	
	public String deletePlan()
	{
		System.out.println("in deletePlan method .. " + toAddPlan.getId());
		try{
			
			toAddPlan.setIsDelete(MessageConstants.Constants.YES_STRING);
			
			
			if(new AddPlannerBll().deletePlan(toAddPlan))
			{
				this.toAddPlan = new WfPlanner();
				AddPlannerBll bll =new AddPlannerBll();
//				List<WfAttendedBy> attendedBySource = bll.getAllAttendedBys();
//		        List<WfAttendedBy> attendedByTarget = new ArrayList<WfAttendedBy>();
//				this.attendedByList = new DualListModel<WfAttendedBy>(attendedBySource, attendedByTarget);
				this.searchCurrentCmts();
				MessageUtils.info("IHD/CMT deleted successfully");
				//return (( PageNavigationBean )FacesUtils.getManagedBean( "navBean" ) ).navHomePage();
//				return NavigationConstants.HOME_NAVIGATION;

			}
			else
			{
				MessageUtils.error("Error saving data");
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.error("Error saving data..." + e.getMessage());
		}
		
		
		return "";
	}
	
	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);
        pdf.addTitle("IHDs and CMTs");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String logo = externalContext.getRealPath("") + File.separator + "resources"+ File.separator + "images" + File.separator + "banner.png";
        Image img = Image.getInstance(logo);
        
        img.scalePercent(40);
        pdf.add(img);
    }

	public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
         
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
             
            cell.setCellStyle(cellStyle);
        }
    }
	
	
	public WfPlanner getToAddPlan() {
		return toAddPlan;
	}


	public void setToAddPlan(WfPlanner toAddPlan) {
		this.toAddPlan = toAddPlan;
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







	public WfPlanner getSelectedPlan() {
		return selectedPlan;
	}







	public void setSelectedPlan(WfPlanner selectedPlan) {
		this.selectedPlan = selectedPlan;
	}


	public DualListModel<WfAttendedBy> getAttendedByList() {
		return attendedByList;
	}


	public void setAttendedByList(DualListModel<WfAttendedBy> attendedByList) {
		this.attendedByList = attendedByList;
	}


	public WfAttendedBy getNewAttendedBy() {
		return newAttendedBy;
	}


	public void setNewAttendedBy(WfAttendedBy newAttendedBy) {
		this.newAttendedBy = newAttendedBy;
	}


	public List<WfPlanner> getFilteredPlansList() {
		return filteredPlansList;
	}


	public void setFilteredPlansList(List<WfPlanner> filteredPlansList) {
		this.filteredPlansList = filteredPlansList;
	}
	
	
	
	
	

	/*
	
	public String searchRadiologistClients()
	{	
		RegisterClientBll bll =new RegisterClientBll();
			this.toSearchClient.setId(null);
			this.toSearchClient.setPassportNo("");
			this.toSearchClient.setGamcaSlipNo("");
//			if(toSearchClient==null){
//				this.toSearchClient = new WfClient();
//			}
//			if(this.toSearchClient.getCashPayment()==null || 
//					this.toSearchClient.getCashPayment().getId()==null )
//			{
//				this.toSearchClient.setCashPayment(new WfClientFinance());
				this.toSearchClient.getCashPayment().setCashPaidStatus("Paid");
//			}
//			if(this.toSearchClient.getProgress() == null || 
//					this.toSearchClient.getProgress().getId()==null)
//			{
//				this.toSearchClient.setProgress(new WfClientProgress());
//			}
//			
//			if(this.toSearchClient.getXray() == null || 
//					this.toSearchClient.getXray().getId()==null)
//			{
//				this.toSearchClient.setXray(new WfClientXray());
				this.toSearchClient.getXray().setXrayStatus(Constants.YES_STRING);
//			}
			this.clientsList = bll.searchRadiologistClients(toSearchClient);
			System.out.println("**************** List retreived ... " + clientsList.size());
			
			initializeNullObjs();
		
		
		return "";
	}
	
	
		
	private void initializeNullObjs()
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

	
	

}
