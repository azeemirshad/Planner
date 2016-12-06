package com.plan.utils;

import org.hibernate.FlushMode;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtilsAnnot 
{
	
private static final SessionFactory sessionFactory;
	
	static {
		try {
		    //getLicense();
			// Create the SessionFactory
			AnnotationConfiguration configuration = new AnnotationConfiguration();
			sessionFactory = configuration.configure().buildSessionFactory();
		//	SaveOrUpdateEventListenerImpl listenerObj=new SaveOrUpdateEventListenerImpl();
		//	configuration.getSessionEventListenerConfig().setSaveOrUpdateEventListener(listenerObj);
			
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			//System.out.println("Initial SessionFactory creation failed.",
			// ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static final ThreadLocal session = new ThreadLocal();
	private static final ThreadLocal localInterceptor = new ThreadLocal();
	private static final ThreadLocal<Boolean> globalTransaction = new ThreadLocal();

	public static Session currentSession() {
		Session s = (Session) session.get();		
		// Open a new Session, if this Thread has none yet
		if  ( (s==null) || (!s.isOpen()) ) {
			if(localInterceptor.get() == null)
			{
//				localInterceptor.set(new AuditLogInterceptor());
				s = sessionFactory.openSession((Interceptor)localInterceptor.get());
				s.setFlushMode(FlushMode.COMMIT);
			}
			else{
				Interceptor interceptor = (Interceptor)localInterceptor.get();
				s = sessionFactory.openSession(interceptor);
				s.setFlushMode(FlushMode.COMMIT);
			}
			session.set(s);
		}
		return s;
	}

	public static void closeSession() {
		
		Session s = (Session) session.get();
		if (s != null)
			s.close();
		session.set(null);
		
		
//		to prevent multiple sessions for single request
//		Object obj = session.get();
//		if( obj != null )
//		{
//			Session currentSession = ((Session)obj);
//			currentSession.close();
//			session.set( null );
//		}
	}	
	
	public static void terminateSession()
	{
		Object obj = session.get();
		if( obj != null )
		{
			Session currentSession = ((Session)obj);
			currentSession.close();
			session.set( null );
		}
	}
	
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void registerInterceptor(Interceptor interceptor){
    	if(interceptor != null){
    		localInterceptor.set(interceptor);
    	}
	}

}
