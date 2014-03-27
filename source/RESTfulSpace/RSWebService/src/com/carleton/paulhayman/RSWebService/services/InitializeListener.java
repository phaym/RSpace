package com.carleton.paulhayman.RSWebService.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.carleton.paulhayman.RSWebService.dao.ListenerQueue;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;
import com.carleton.paulhayman.RSWebService.dao.ResponseQueue;
import com.carleton.paulhayman.RSWebService.dao.TransactionQueue;

public class InitializeListener implements ServletContextListener {
	
	/*Handle Servlet initialization and shutdown*/
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		Logger.getLogger("RSpace").log(Level.INFO, "RSpace Startup : Initializing Queues...");
		
		ListenerQueue.getInstance();
		ResponseQueue.getInstance();
		TransactionQueue.getInstance();
		MongoDbImpl.getInstance();
	}
}
