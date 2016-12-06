package com.plan.dal;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.plan.dal.dao.ApplicationUsers;
import com.plan.utils.HibernateUtilsAnnot;
import com.plan.utils.MessageConstants;

public class UserDal 
{
	public UserDal() {

	}

	
	public ApplicationUsers localLogin(String userName) throws Exception
	{
		ApplicationUsers obj = new ApplicationUsers();

		Session session = null;
		try {
			session = HibernateUtilsAnnot.currentSession();
			Criteria cr = session.createCriteria(ApplicationUsers.class);
			cr.add(Restrictions.ilike("userName", userName));
			cr.add(Restrictions.eq("profileStatus", MessageConstants.Constants.PROFILE_CURRENT));
			obj = (ApplicationUsers)cr.uniqueResult();
		}

		catch (HibernateException e) 
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
			//return null;
		} finally {
			session.flush();
			HibernateUtilsAnnot.closeSession();
		}

		return obj;
	}

}
