package com.core.service;

import org.springframework.util.StringUtils;

import com.core.dataaccess.SqlManager;
import com.core.dataaccess.SqlManagerFactory;
import com.core.web.listener.adapter.CommonContextLoaderAdapter;

/**
 * Service 에서 상속 받아 DB 연결 함.
 * @author KMYU
 * @since 2022. 5. 2
 * @version 1.0 
 * @see 
 * 
 * <pre> 
 * << 개정이력(Modification Information) >> 
 * 
 * 수정일                       수정자                 수정내용 
 * -------     --------  --------------------------- 
 *  
 * </pre>
 */
public class BaseService {
//	public BaseService() {
//		
//	}
//	
//	public SqlManager getSqlManager() {
//		return new SqlManager();
//	}
//	
//	public SqlManager getSqlManager(String dataSourceId) {
//		return new SqlManager(dataSourceId);
//	}
	
	public BaseService() {
	}
	
	public SqlManager getSqlManager(String dataSourceId) {
		return SqlManagerFactory.getSqlManager(dataSourceId);
	}
	
	protected BaseService getThis() {
		String fqn = getClass().getName();
		return (BaseService)CommonContextLoaderAdapter.getBean(StringUtils.uncapitalize(fqn.substring(fqn.lastIndexOf(".") + 1)));
	}
}
