package com.plan.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class MessageUtils 
{
	
	public static final void info(String msg) 
	{
        FacesContext.getCurrentInstance().addMessage(null,
        	new FacesMessage(FacesMessage.SEVERITY_INFO, "Info : ", msg));
    }
	
	public static final void error(String msg) 
	{
        FacesContext.getCurrentInstance().addMessage(null, 
        		new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error! : ", msg));
    }

}
