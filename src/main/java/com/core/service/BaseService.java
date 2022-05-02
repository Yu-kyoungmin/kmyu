package com.core.service;

import com.core.dataaccess.SqlManager;

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
	public BaseService() {
		
	}
	
	public SqlManager getSqlManager() {
		return new SqlManager();
	}
	
	public SqlManager getSqlManager(String dataSourceId) {
		return new SqlManager(dataSourceId);
	}
}
