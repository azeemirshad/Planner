package com.plan.bll.wf;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.UploadedFile;

import com.iac.web.util.FacesUtils;
import com.plan.dal.dao.ApplicationUsers;
import com.plan.dal.dao.WfAttendedBy;
import com.plan.dal.dao.WfPlanner;
import com.plan.dal.dao.WfTrackReport;
import com.plan.ui.beans.UserBean;
import com.plan.ui.beans.admin.AdminBean;
import com.plan.ui.beans.admin.CriteriaBean;
import com.plan.utils.Environment;
import com.plan.utils.HibernateUtilsAnnot;
import com.plan.utils.MessageConstants;
import com.plan.utils.ViewScannedFilesUtils;



public class AddPlannerBll 
{	
	private UserBean ub = ((UserBean)FacesUtils.getManagedBean("userBean"));
	private CriteriaBean cb = ((CriteriaBean)FacesUtils.getManagedBean("crit"));
	
	public AddPlannerBll() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public List<WfPlanner> getCurrentCmts()
	{
		Session session = null;
		List<WfPlanner> list = null;
		System.out.println("In getCurrentCmts Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfPlanner.class);
			
			cr.add(Restrictions.ge("eventTime", Calendar.getInstance().getTime()));
			cr.add(Restrictions.eq("isDelete", MessageConstants.Constants.NO_STRING));
			cr.addOrder(Order.asc("eventTime"));
			list = cr.list();
			for (WfPlanner wfPlanner : list) {
				Hibernate.initialize(wfPlanner.getAttendedBy());
				Hibernate.initialize(wfPlanner.getTrackReport());
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
		
		return list;
	}
	
	
	public List<String> getAllSections()
	{
		Session session = null;
		List<String> list = null;
		System.out.println("In getCurrentCmts Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(ApplicationUsers.class);
			cr.setProjection(Projections.distinct(Projections.property("section")));
			cr.add(Restrictions.eq("profileStatus", MessageConstants.Constants.PROFILE_CURRENT));
			cr.add(Restrictions.ne("section","Display"));
			cr.addOrder(Order.asc("section"));
			list = cr.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		System.out.println("Sections List size "+ list.size());
		System.out.println( list);
		
		return list;
	}
	
	public List<String> getAllLocations()
	{
		Session session = null;
		List<String> list = null;
		System.out.println("In getAllLocations Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfPlanner.class);
			cr.setProjection(Projections.distinct(Projections.property("location")));
			cr.add(Restrictions.eq("isDelete", MessageConstants.Constants.NO_STRING));
			cr.addOrder(Order.asc("location"));
			list = cr.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return list;
	}
	
	public List<WfAttendedBy> getAllAttendedBys()
	{
		Session session = null;
		List<String> list = new ArrayList<String>();
		System.out.println("In getAllAttendedBys Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfAttendedBy.class);
			cr.setProjection(Projections.distinct(Projections.property("name")));
			cr.add(Restrictions.eq("isDelete", MessageConstants.Constants.NO_STRING));
			cr.addOrder(Order.asc("name"));
			list = cr.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		List<WfAttendedBy> alist= new ArrayList<WfAttendedBy>();
		WfAttendedBy  attendedBy = null;
		for (String attendedByName : list) {
			attendedBy = new WfAttendedBy();
			attendedBy.setIsDelete(MessageConstants.Constants.NO_STRING);
			attendedBy.setName(attendedByName);
			alist.add(attendedBy);
		}
		
		return alist;
	}
	
	public List<String> getAllChairedBys()
	{
		Session session = null;
		List<String> list = null;
		System.out.println("In getAllChairedBys Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfPlanner.class);
			cr.setProjection(Projections.distinct(Projections.property("chairedBy")));
			cr.add(Restrictions.eq("isDelete", MessageConstants.Constants.NO_STRING));
			cr.addOrder(Order.asc("chairedBy"));
			list = cr.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return list;
	}
	
	public List<String> getAllAppointments()
	{
		Session session = null;
		List<String> list = null;
		System.out.println("In getCurrentCmts Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(ApplicationUsers.class);
			cr.setProjection(Projections.distinct(Projections.property("appointment")));
			cr.add(Restrictions.eq("profileStatus", MessageConstants.Constants.PROFILE_CURRENT));
			cr.addOrder(Order.asc("appointment"));
			list = cr.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return list;
	}
	public boolean addPlan(WfPlanner toAdd) throws Exception
	{
		System.out.println("in add planner bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			ApplicationUsers currentUser = new ApplicationUsers(); 
			currentUser = ub.getCurrentUser();
//			Calendar sDate = Calendar.getInstance(); 
//			sDate.setTime(toAdd.getEventTime());
//			sDate.add(Calendar.MINUTE, -MessageConstants.Constants.MINIMUM_INTERVAL);
//			
//			Calendar eDate = Calendar.getInstance(); 
//			eDate.setTime(toAdd.getEventTime());
//			eDate.add(Calendar.MINUTE, MessageConstants.Constants.MINIMUM_INTERVAL);
//			
//			Criteria cr = session.createCriteria(WfPlanner.class);
//			cr.add(Restrictions.between("eventTime", sDate.getTime(), eDate.getTime()));
//			cr.setProjection(Projections.rowCount());
			
				if(toAdd.getId() == null || toAdd.getId()<1)
				{
//					Integer count =(Integer) cr.uniqueResult();
//					if(count == 0){
					
					toAdd.setInsertBy(currentUser);	
					toAdd.setUpdateBy(currentUser);	
					session.save(toAdd);
					session.flush();
					
					
	//				Adding track report
					saveTrackReport(MessageConstants.Constants.TrackActions.PLAN_SAVED, toAdd, session);
//					}else
//					{
//						throw new Exception("This time slot is not aval, please choose a different one.");
//					}
				}
				else if(toAdd.getId() != null && toAdd.getId()>=1)
				{
					
//					cr.add(Restrictions.ne("id", toAdd.getId()));
//					Integer count =(Integer) cr.uniqueResult();
//					if(count == 0){
	//				Adding track report
						//saveTrackReport(MessageConstants.Constants.TrackActions.PLAN_UPDATED, toAdd, session);
						List<WfAttendedBy> attendees  = session.createCriteria(WfAttendedBy.class).add(Restrictions.eq("plannerId.id", toAdd.getId())).list();
						for (WfAttendedBy object : attendees) {
							session.delete(object);
						}
//						WfTrackReport report = new WfTrackReport();
//						report.setOperator(ub.getCurrentUser());
//						report.setActivity(MessageConstants.Constants.TrackActions.PLAN_UPDATED);
//						report.setPlannerId(toAdd);
//						toAdd.getTrackReport().add(report);
						toAdd.setUpdateBy(currentUser);
						toAdd.setUpdateDate(new Date());
						session.merge(toAdd);
						saveTrackReport(MessageConstants.Constants.TrackActions.PLAN_UPDATED, toAdd, session);
//					}else
//					{
//						throw new Exception("This time slot is not aval, please choose a different one.");
//					}
				}
			
			
			
			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
			throw new Exception(e.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
			throw new Exception(e.getMessage());
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}
	
	
	public boolean deletePlan(WfPlanner toAdd) throws Exception
	{
		System.out.println("in delete planner bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			ApplicationUsers currentUser = new ApplicationUsers(); 
			currentUser = ub.getCurrentUser();
			toAdd.setUpdateBy(currentUser);
			toAdd.setUpdateDate(new Date());
			session.saveOrUpdate(toAdd);
			saveTrackReport(MessageConstants.Constants.TrackActions.PLAN_DELETED, toAdd, session);
			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
			throw new Exception(e.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
			throw new Exception(e.getMessage());
			
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}
	
	
	public void saveTrackReport(String activity, WfPlanner plannerId, Session session)throws HibernateException
	{
		WfTrackReport report = new WfTrackReport();
		report.setOperator(ub.getCurrentUser());
		report.setActivity(activity);
		report.setPlannerId(plannerId);
		
		session.save(report);
	}
	
	
	public List<WfPlanner> advSearchPlans(WfPlanner toSearchPlan, Date dateFrom , Date dateTo)
	{
		Session session = null;
		List<WfPlanner> list = new ArrayList<WfPlanner>();
		System.out.println("In search client Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfPlanner.class);
			if(toSearchPlan!=null)
			{
				if(toSearchPlan.getId()!=null && toSearchPlan.getId()>0)
				{
					cr.add(Restrictions.eq("id", toSearchPlan.getId()));
				}
				if(toSearchPlan.getChairedBy()!=null && toSearchPlan.getChairedBy().trim().length()>0)
				{
					cr.add(Restrictions.ilike("chairedBy", toSearchPlan.getChairedBy()));
				}
				if(toSearchPlan.getTopic()!=null && toSearchPlan.getTopic().trim().length()>0)
				{
					cr.add(Restrictions.ilike("topic", toSearchPlan.getTopic(), MatchMode.ANYWHERE));
				}
				if(toSearchPlan.getLocation()!=null && toSearchPlan.getLocation().trim().length()>0)
				{
					cr.add(Restrictions.eq("location", toSearchPlan.getLocation()));
				}
				cr.add(Restrictions.between("eventTime", dateFrom, dateTo));
			}
			cr.addOrder(Order.asc("eventTime"));
			list = cr.list();
			for(WfPlanner c:list)
			{
				Hibernate.initialize(c.getAttendedBy());
				
				Hibernate.initialize(c.getTrackReport());
				
			}
	
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return list;
	}
	
	
	/*public boolean resetRepeaterList(List<WfPlanner> toUpdateList)
	{
		System.out.println("in update cash bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			ApplicationUsers currentUser = new ApplicationUsers(); 
			currentUser = ub.getCurrentUser();
			for(WfPlanner toUpdate:toUpdateList)
			{
				
				toUpdate.setUpdateBy(currentUser);
				toUpdate.setUpdateDate(new Date());
				toUpdate.setClientStatus(MessageConstants.Constants.ClientStatus.REGISTERED);
				toUpdate.setRepeatStatus(MessageConstants.Constants.RepeatStatus.REPEAT);
				toUpdate.setFinalDeclaredBy(null);
				toUpdate.setFinalDeclaredDate(null);
								
				session.delete(toUpdate.getCashPayment());
				session.delete(toUpdate.getSamples());
				session.delete(toUpdate.getBlood());
				session.delete(toUpdate.getMicro());
				session.delete(toUpdate.getSputum());
				session.delete(toUpdate.getStool());
				session.delete(toUpdate.getUrine());
				
				
				toUpdate.setCashPayment(null);
				toUpdate.setSamples(null);
				toUpdate.setBlood(null);
				toUpdate.setMicro(null);
				toUpdate.setSputum(null);
				toUpdate.setStool(null);
				toUpdate.setUrine(null);
				
				toUpdate.getProgress().setCash(null);
				toUpdate.getProgress().setSample(null);
				toUpdate.getProgress().setPathologist(null);
				
				session.update(toUpdate.getProgress());
				session.update(toUpdate);
				session.flush();
				
//				Adding new cash data
				WfClientFinance fin = new WfClientFinance();
				fin.setClientId(toUpdate);
				fin.setCashPaidStatus(MessageConstants.Constants.CashPaymentStatus.UNPAID);
				session.save(fin);				
				
				
				
				saveTrackReport(MessageConstants.Constants.TrackActions.RESET_REPEATER, toUpdate, session);
			}
			
			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}

	
	
	public List<WfPlanner> searchRadiologistClients(WfPlanner toSearchClient)
	{
		Session session = null;
		List<WfPlanner> list = new ArrayList<WfPlanner>();
		System.out.println("In search client Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(WfPlanner.class);
			
			if(toSearchClient!=null)
			{
				if(toSearchClient.getId()!=null && toSearchClient.getId()>0)
				{
					cr.add(Restrictions.eq("id", toSearchClient.getId()));
				}
				
				
				if(toSearchClient.getGamcaSlipNo()!=null && toSearchClient.getGamcaSlipNo().trim().length()>0)
				{
					cr.add(Restrictions.ilike("gamcaSlipNo", toSearchClient.getGamcaSlipNo()));
				}
				
				if(toSearchClient.getPassportNo()!=null && toSearchClient.getPassportNo().trim().length()>0)
				{
					cr.add(Restrictions.ilike("passportNo", toSearchClient.getPassportNo()));
				}
				
				if(toSearchClient.getClientStatus()!=null && 
						toSearchClient.getClientStatus().contains(MessageConstants.Constants.ClientStatus.REPEATER))
				{
					cr.add(Restrictions.ilike("clientStatus", MessageConstants.Constants.ClientStatus.REPEATER+"%"));
				}
				else if(toSearchClient.getClientStatus()!=null && toSearchClient.getClientStatus().trim().length()>0)
				{
					cr.add(Restrictions.eq("clientStatus", toSearchClient.getClientStatus()));
				}
				else
				{
					
					cr.add(Restrictions.disjunction()
//	                        .add(Restrictions.ilike("clientStatus", MessageConstants.Constants.ClientStatus.REPEATER+"%"))
	                        .add(Restrictions.eq("clientStatus", MessageConstants.Constants.ClientStatus.REGISTERED))
	                        );
				}
				if(toSearchClient.getCashPayment()!=null 
						)
				{
					System.out.println("*************** Adding cash payment");
//					Criteria cr2 = cr.createCriteria("cashPayment",Criteria.LEFT_JOIN);
					cr.createAlias("cashPayment", "cashP");
					
					if(toSearchClient.getCashPayment().getId()!=null && 
							toSearchClient.getCashPayment().getId()>0)
					{
//						cr2.add(Restrictions.eq("id", toSearchClient.getCashPayment().getId()));
						cr.add(Restrictions.eq("cashP.id", toSearchClient.getCashPayment().getId()));
						
					}
					if(toSearchClient.getCashPayment().getCashPaidStatus()!=null &&
							toSearchClient.getCashPayment().getCashPaidStatus().trim().length()>0)
					{
						if(toSearchClient.getCashPayment().getCashPaidStatus().equals(MessageConstants.Constants.CashPaymentStatus.PAID))
						{
//							cr2.add(Restrictions.like("cashPaidStatus", MessageConstants.Constants.CashPaymentStatus.PAID));
							cr.add(Restrictions.like("cashP.cashPaidStatus", MessageConstants.Constants.CashPaymentStatus.PAID));
//							cr2.add(Restrictions.isNotNull("cashAmount"));
							cr.add(Restrictions.isNotNull("cashP.cashAmount"));
						}
						else
						{
//							cr2.add(Restrictions.like("cashPaidStatus", MessageConstants.Constants.CashPaymentStatus.UNPAID));
							cr.add(Restrictions.like("cashP.cashPaidStatus", MessageConstants.Constants.CashPaymentStatus.UNPAID));
						}
						
		                        
					}
					
				}
				
				if(toSearchClient.getProgress()!=null 
						)
				{
					System.out.println("*************** Adding progress");
//					Criteria cr2 = cr.createCriteria("progress",Criteria.LEFT_JOIN);
					cr.createAlias("progress", "pro");
					
					if(toSearchClient.getProgress().getId()!=null && 
							toSearchClient.getProgress().getId()>0)
					{
//						cr2.add(Restrictions.eq("id", toSearchClient.getProgress().getId()));
						cr.add(Restrictions.eq("pro.id", toSearchClient.getProgress().getId()));
					}
					if(toSearchClient.getProgress().getPathologist()!=null &&
							toSearchClient.getProgress().getPathologist().trim().length()>0)
					{
//						cr2.add(Restrictions.ilike("pathologist", toSearchClient.getProgress().getPathologist()+"%"));
						cr.add(Restrictions.ilike("pro.pathologist", toSearchClient.getProgress().getPathologist()+"%"));
					}
					
				}
			}
			cr.createAlias("xray", "x");
			cr.add(Restrictions.or(Restrictions.isNull("x.chest"), Restrictions.ilike("x.chest", "On Hold")));
			//cr.setFetchMode("scannedFiles", FetchMode.LAZY);
			cr.addOrder(Order.desc("id"));
			list = cr.list();
			
			for(WfPlanner c:list)
			{
				Hibernate.initialize(c.getTrackReport());
				
			}
	
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return list;
	}
	
	
	
	public boolean uploadFiles(WfPlanner toUpdate, FileUploadView fileView)
	{
		System.out.println("in upload gamca bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		AdminBean adb = (AdminBean)FacesUtils.getManagedBean("adminBean");
		
		File files = new File(Environment.getScannedFilesStoragePath());
	    if(!files.exists())
	    {
	    	files.mkdirs();
	    }		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
//			Criteria cr = session.createCriteria(WfClient.class)
//					.setFetchMode("scannedFiles", FetchMode.SELECT)
//					.add(Restrictions.eq("id", client.getId()));
//			WfClient toUpdate =(WfClient) cr.uniqueResult();
//			Hibernate.initialize(toUpdate.getScannedFiles());
			
			
			ApplicationUsers currentUser = new ApplicationUsers(); 
			currentUser = ub.getCurrentUser();
			toUpdate.setUpdateBy(currentUser);
			toUpdate.setUpdateDate(new Date());
			String path = Environment.getScannedFilesStoragePath();
			String fileName="";
			// Added by Azeem Irshad to fetch lazy association
			WfClientScannedFiles scanndFiles = (WfClientScannedFiles) session.createQuery(
					"from WfClientScannedFiles where clientId.id = :clientID ")
					.setParameter("clientID", toUpdate.getId())
					
					.uniqueResult();
//			toUpdate.setScannedFiles();
//			WfClientScannedFiles temp = scanndFiles;
//			session.evict(scanndFiles);
//			temp.setClientId(toUpdate);
			toUpdate.setScannedFiles(scanndFiles);
			
			if(toUpdate.getScannedFiles()!=null// && toUpdate.getScannedFiles().getId()!=null 
					&& toUpdate.getScannedFiles().getId()>0)
			{
				System.out.println("inside if .......");
				if(fileView.getFileBinary1()!=null)
				{			
					
					toUpdate.getScannedFiles().setScannedGamca(fileView.getFileBinary1());
					toUpdate.getScannedFiles().setGamcaMime(fileView.getFile1Mime());
					
					fileName=toUpdate.getId().toString()+Environment.getGamcaNameFormat()+fileView.getFile1Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary1());
				}
				if(fileView.getFileBinary2()!=null)
				{
					toUpdate.getScannedFiles().setScannedPassport(fileView.getFileBinary2());
					toUpdate.getScannedFiles().setPassportMime(fileView.getFile2Mime());
					fileName=toUpdate.getId().toString()+Environment.getPassportNameFormat()+fileView.getFile2Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary2());
				}
				if(fileView.getFileBinary3()!=null)
				{
					toUpdate.getScannedFiles().setScannedPhoto(fileView.getFileBinary3());
					toUpdate.getScannedFiles().setPhotoMime(fileView.getFile3Mime());
					fileName=toUpdate.getId().toString()+Environment.getPhotoNameFormat()+fileView.getFile3Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary3());
				}
				System.out.println("exiting if .......");
				
			}
			else
			{
				WfClientScannedFiles scannedFilesObj = new WfClientScannedFiles();
				scannedFilesObj.setClientId(toUpdate);
				if(fileView.getFileBinary1()!=null)
				{
					scannedFilesObj.setScannedGamca(fileView.getFileBinary1());
					scannedFilesObj.setGamcaMime(fileView.getFile1Mime());
					fileName = fileName+Environment.getGamcaNameFormat()+fileView.getFile1Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary1());
				}
				if(fileView.getFileBinary2()!=null)
				{
					scannedFilesObj.setScannedPassport(fileView.getFileBinary2());
					scannedFilesObj.setPassportMime(fileView.getFile2Mime());
					fileName = fileName+Environment.getPassportNameFormat()+fileView.getFile2Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary2());
				}
				if(fileView.getFileBinary3()!=null)
				{
					scannedFilesObj.setScannedPhoto(fileView.getFileBinary3());
					scannedFilesObj.setPhotoMime(fileView.getFile3Mime());
					fileName = fileName+Environment.getPhotoNameFormat()+fileView.getFile3Mime();
					adb.makeFileFromByte(path+fileName, fileView.getFileBinary3());
				}
				session.save(scannedFilesObj);
				
			}
			
			session.update(toUpdate);
			saveTrackReport(MessageConstants.Constants.TrackActions.SCANNED_UPDATED, toUpdate, session);
			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}
	
	
	
	public boolean updateCashStatus(List<WfPlanner> toUpdateList)
	{
		System.out.println("in update cash bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			ApplicationUsers currentUser = new ApplicationUsers(); 
			currentUser = ub.getCurrentUser();
			for(WfPlanner toUpdate:toUpdateList)
			{
				toUpdate.setUpdateBy(currentUser);
				toUpdate.setUpdateDate(new Date());
				if(toUpdate.getCashPayment()==null || toUpdate.getCashPayment().getId()==null
						|| toUpdate.getCashPayment().getId()<1)
				{
					WfClientFinance finObj = new WfClientFinance();
					finObj.setClientId(toUpdate);
					finObj.setCashAmount(toUpdate.getCashPayment().getCashAmount());
					finObj.setCashPaidStatus(toUpdate.getCashPayment().getCashPaidStatus());
					finObj.setCashPaidDate(new Date());
					session.save(finObj);
				}
				else
				{			
					toUpdate.getCashPayment().setCashPaidDate(new Date());
					session.update(toUpdate.getCashPayment());
				}
				
				if(toUpdate.getProgress() == null || toUpdate.getProgress().getId()==null)
				{
					WfClientProgress progress = new WfClientProgress();
					progress.setClientId(toUpdate);
					progress.setCash(toUpdate.getCashPayment().getCashPaidStatus());
					session.save(progress);					
				}
				else
				{
					toUpdate.getProgress().setCash(toUpdate.getCashPayment().getCashPaidStatus());
					session.update(toUpdate.getProgress());
				}
				
				saveTrackReport(MessageConstants.Constants.TrackActions.CASH, toUpdate, session);
			}
			
			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}
	
	public boolean addGpe(WfPlanner toAdd)
	{
		System.out.println("in add client GPE bll method");
		boolean flag = true;
		
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			if(toAdd.getGpe().getId()==null
					|| toAdd.getGpe().getId()<1)
			{
				System.out.println("saving new GPE ");
				toAdd.getGpe().setClientId(toAdd);
				toAdd.getGpe().setInsertDate(new Date());
				session.save(toAdd.getGpe());				
			}
			else
			{
				System.out.println("updating GPE ");
				session.update(toAdd.getGpe());
			}
			//Saving Lungs status
			if(toAdd.getXray()==null || toAdd.getXray().getId()==null)
//				|| toUpdate.getXray().getId()<1)

			{
				WfClientXray xrayObj = new WfClientXray();
				xrayObj.setClientId(toAdd);
				xrayObj.setLungs(toAdd.getXray().getLungs());
				//xrayObj.setXrayInsertDate(new Date());
				session.save(xrayObj);
			}
			else
			{
				//toUpdate.getXray().setXrayInsertDate(new Date());
				session.update(toAdd.getXray());
			}
			
			if(toAdd.getProgress() == null || toAdd.getProgress().getId()==null)
			{
				WfClientProgress progress = new WfClientProgress();
				progress.setClientId(toAdd);
				progress.setGpe(toAdd.getGpe().getMedicalStatus());
				session.save(progress);					
			}
			else
			{
				toAdd.getProgress().setGpe(toAdd.getGpe().getMedicalStatus());
				session.update(toAdd.getProgress());
			}		
			
			saveTrackReport(MessageConstants.Constants.TrackActions.GPE, toAdd, session);			
			tx.commit();
					
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			flag = false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		
		return flag;
	}
	
	
	
	
	
	private void initCashObj(WfClientFinance obj, Session session)throws HibernateException, Exception
	{
		obj.setCashAmount(null);
		obj.setCashPaidDate(null);
		obj.setCashPaidStatus(MessageConstants.Constants.CashPaymentStatus.UNPAID);
		session.update(obj);
	}*/
	
}
