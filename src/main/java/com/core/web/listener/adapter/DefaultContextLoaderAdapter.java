package com.core.web.listener.adapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public class DefaultContextLoaderAdapter implements ContextLoaderAdapter {
	
	public DefaultContextLoaderAdapter() {
	}
	
	private static WebApplicationContext webApplicationContext;
    private static ApplicationContext applicationContext;

	@Override
	public void beforeInitialize(ServletContext servletcontext) {
		
	}

	@Override
	public void afterInitialize(ServletContext servletcontext) {
		
	}

	@Override
	public void beforeDestroy(ServletContext servletcontext) {
		
	}

	@Override
	public void afterDestroy(ServletContext servletcontext) {
		
	}
	
	@SuppressWarnings("deprecation")
	public static final void setWebApplicationContext(ServletRequest request) {
		webApplicationContext = RequestContextUtils.getWebApplicationContext(request);
	}
	
	public static final void setWebApplicationContext(WebApplicationContext context) {
		webApplicationContext = context;
	}
	
	public static final void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
	
	public static final ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static final WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}
	
	public static final Object getBean(String beanName) {
		
		if(webApplicationContext == null) {
			System.out.println("######################## is null");
			return null;
		} else {
			System.out.println("######################## is not null");
			return webApplicationContext.containsBean(beanName) ? webApplicationContext.getBean(beanName) : null;
		}
	}

}
