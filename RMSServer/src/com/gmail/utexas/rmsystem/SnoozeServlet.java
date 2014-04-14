package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.RMSUser;
import com.gmail.utexas.rmsystem.models.Settings;
import com.googlecode.objectify.ObjectifyService;

public class SnoozeServlet  extends HttpServlet{

	Logger log = Logger.getLogger("Snooze");
	static {
        ObjectifyService.register(Settings.class);
        ObjectifyService.register(RMSUser.class);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	RMSUser user = ofy().load().type(RMSUser.class).id(LogMessageHandler.G_APP_DEBUG).get();
    	BufferedReader reader = req.getReader();
		boolean status = false;
		try {			
			String line = reader.readLine();			
			status = Boolean.parseBoolean(line);			
		} finally {
			reader.close();
		}
    	user.setSnooze(status);  	
    	ofy().save().entity(user).now();    	
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String type = req.getParameter("type");
    	if(type != null && type.equals("duration")){
    		Settings settings = ofy().load().type(Settings.class).id(LogMessageHandler.G_APP_DEBUG).get();    
    		resp.getWriter().println(settings.snooze);    		
    	} else { 
    		RMSUser user = ofy().load().type(RMSUser.class).id(LogMessageHandler.G_APP_DEBUG).get();
    		resp.getWriter().println(user.isSnooze());
    		if(user.isSnooze()){
    			user.setSnooze(false);
    	    	ofy().save().entity(user).now();    	    			
    		}
    	}
    }
}