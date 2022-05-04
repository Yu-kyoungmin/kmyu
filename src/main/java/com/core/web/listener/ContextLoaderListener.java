package com.core.web.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.core.web.listener.adapter.ContextLoaderAdapter;

public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {
	private static Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);
	
	private ContextLoaderAdapter adapter;
	
	public void contextInitialized(ServletContextEvent event) {
		loadContextLoaderAdapter(event);
		
		if(adapter != null) {
			adapter.beforeInitialize(event.getServletContext());
		}
		
		super.contextInitialized(event);
		
		if(adapter != null) {
			adapter.afterInitialize(event.getServletContext());
		}
	}
	
	
	private void loadContextLoaderAdapter(ServletContextEvent event) {
		String adapterClassName = event.getServletContext().getInitParameter("contextLoaderAdapter");
		
		if(StringUtils.isEmpty(adapterClassName)) {
			return;
		}
		
		try {
			Class<?> adapterClass = Class.forName(adapterClassName);
			adapter = (ContextLoaderAdapter)adapterClass.newInstance();
		} catch(ClassNotFoundException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} catch(InstantiationException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		} catch(IllegalAccessException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
		}
	}
}
