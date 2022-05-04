package com.core.dataaccess;

import com.core.web.listener.adapter.CommonContextLoaderAdapter;

public class SqlManagerFactory {
	public SqlManagerFactory() {
	}
	
//	public static SqlManager getSqlManager(String dataSourceId) {
//		return (SqlManager)CommonContextLoaderAdapter.getBean(dataSourceId);
//	}
	
	public static SqlManager getSqlManager(String dataSourceId) {
		return (SqlManager)CommonContextLoaderAdapter.getBean(dataSourceId);
	}
}
