package com.plan.bll.admin;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.text.StrongTextEncryptor;

import com.plan.dal.AdminDal;
import com.plan.dal.dao.ApplicationUsers;
import com.plan.dal.dao.WfPlanner;
import com.plan.dal.dao.WfPlanner;
import com.plan.utils.Environment;
import com.plan.utils.HibernateUtilsAnnot;
import com.plan.utils.MessageConstants;

public class AdminBll 
{
	
	
	public AdminBll() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public boolean addNewUser(ApplicationUsers toAddUser)
	{
		Session session = null;
		Transaction tx = null;
		System.out.println("In add new User Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(MessageConstants.Constants.PASSWORD_KEY);
			String myEncryptedText = textEncryptor.encrypt(Environment.getDefaultPassword());
			toAddUser.setPassword(myEncryptedText);
			toAddUser.setProfileStatus(MessageConstants.Constants.PROFILE_CURRENT);
			session.save(toAddUser);
			
			tx.commit();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			return false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return true;
	}
	
	public boolean updateUsers(List<ApplicationUsers> usersList)
	{
		Session session = null;
		Transaction tx = null;
		System.out.println("In update User Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			for(ApplicationUsers u:usersList)
			{
				
				session.update(u);
			}
			tx.commit();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			return false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return true;
	}
	
	public List<ApplicationUsers> searchAllUser(ApplicationUsers toSearchUser)
	{
		Session session = null;
		List<ApplicationUsers> list = new ArrayList<ApplicationUsers>();
		System.out.println("In search User Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();			
			Criteria cr = session.createCriteria(ApplicationUsers.class);
			if(toSearchUser!=null)
			{
				if(toSearchUser.getId()!=null && toSearchUser.getId()>0)
				{
					cr.add(Restrictions.eq("id", toSearchUser.getId()));
				}
				if(toSearchUser.getName()!=null && toSearchUser.getName().trim().length()>0)
				{
					cr.add(Restrictions.ilike("name", toSearchUser.getName()));
				}
				if(toSearchUser.getUserName()!=null && toSearchUser.getUserName().trim().length()>0)
				{
					cr.add(Restrictions.ilike("userName", toSearchUser.getUserName()));
				}
				if(toSearchUser.getProfileStatus()!=null && toSearchUser.getProfileStatus().trim().length()>0)
				{
					cr.add(Restrictions.eq("profileStatus", toSearchUser.getProfileStatus()));
				}
			}
			list = cr.list();
	
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
	
	public boolean changePassword(ApplicationUsers user,String newPassword)
	{
		Session session = null;
		Transaction tx = null;
		System.out.println("In change password Method bll");
		try
		{
			session = HibernateUtilsAnnot.currentSession();
			tx = session.beginTransaction();
			
			StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(MessageConstants.Constants.PASSWORD_KEY);
			String myEncryptedText = textEncryptor.encrypt(newPassword);
			user.setPassword(myEncryptedText);
			user.setProfileStatus(MessageConstants.Constants.PROFILE_CURRENT);
			
			session.update(user);
			
			tx.commit();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			tx.rollback();
			return false;
		}
		finally
		{
			HibernateUtilsAnnot.closeSession();
		}
		
		return true;
	}
	

}
