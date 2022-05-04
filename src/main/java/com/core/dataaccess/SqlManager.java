package com.core.dataaccess;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


/**
 * 쿼리 실행
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
public class SqlManager extends SqlSessionDaoSupport {
	private static Logger logger = LoggerFactory.getLogger(SqlManager.class);
	
	public SqlManager() {
	}
	
	public List<Map<String,Object>> selectList(String sqlId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Map<String,Object>> result = getSqlSession().selectList(sqlId);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sqlId, Map<String,Object> paramMap) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Map<String,Object>> result = getSqlSession().selectList(sqlId, paramMap);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public Object selectOne(String sqlId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = getSqlSession().selectOne(sqlId	);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public Object selectOne(String sqlId, Map<String,Object> paramMap) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = getSqlSession().selectOne(sqlId, paramMap);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public int delete(String sqlId) {
		int deleteCount = 0;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		deleteCount = getSqlSession().delete(sqlId);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return deleteCount;
	}
	
	public int delete(String sqlId, Map<String,Object> paramMap) {
		int deleteCount = 0;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		deleteCount = getSqlSession().delete(sqlId, paramMap);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return deleteCount;
	}
	
	public int update(String sqlId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		int result = getSqlSession().update(sqlId);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public int update(String sqlId, Map<String,Object> paramMap) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		int result = getSqlSession().update(sqlId, paramMap);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public int insert(String sqlId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		int result = getSqlSession().insert(sqlId);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	public int insert(String sqlId, Map<String,Object> paramMap) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		int result = getSqlSession().insert(sqlId, paramMap);
		stopWatch.stop();
		loggingElapsedTime(stopWatch, sqlId);
		return result;
	}
	
	
	private void loggingElapsedTime(StopWatch stopWatch, String sqlId) {
		if(logger.isInfoEnabled()) {
			StringBuffer elapsedTime = new StringBuffer("Sql [");
			elapsedTime.append(sqlId).append("] elapsed time : ");
			elapsedTime.append(stopWatch.getTotalTimeMillis()).append(" ms.");
			logger.info(elapsedTime.toString());
		}
	}
}
