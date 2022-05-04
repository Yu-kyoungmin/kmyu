package com.core.web.listener.adapter;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class CommonContextLoaderAdapter extends DefaultContextLoaderAdapter {
	public CommonContextLoaderAdapter() {
	}
	
	public void afterInitialize(ServletContext context) {
		setWebApplicationContext(WebApplicationContextUtils.getWebApplicationContext(context));
	}
	
	public void beforeDestroy(ServletContext context) {
		super.beforeDestroy(context);
	}
}
