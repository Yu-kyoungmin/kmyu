package com.core.web.listener.adapter;

import javax.servlet.ServletContext;

public interface ContextLoaderAdapter {
	public abstract void beforeInitialize(ServletContext servletcontext);
	public abstract void afterInitialize(ServletContext servletcontext);
	public abstract void beforeDestroy(ServletContext servletcontext);
	public abstract void afterDestroy(ServletContext servletcontext);
}
