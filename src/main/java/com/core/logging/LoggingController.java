package com.core.logging;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Controller Log 출력
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
public class LoggingController {
	private static Logger logger = LoggerFactory.getLogger(LoggingController.class);
	
	public Object printMethodName(ProceedingJoinPoint pjp) throws Throwable {
		Object returnObj = null;
		String sProcMsg  = "OK";
		int iProcCode    = 0;
		
		ServletRequestAttributes sr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		Signature signature = pjp.getSignature();
		String target       = pjp.getTarget().toString();
		long   resultTime   = 0;
		
		String classPath  = target.substring(0, target.lastIndexOf('@'));
		String ctr        = "";
		String serviceFQN = new StringBuffer(classPath).append(".").append(signature.getName()).toString();
		
		if(sr != null) {
			HttpServletRequest curRequest = sr.getRequest();
			ctr = curRequest.getRequestURI();
		}
		
		Map<String,Object> inputParam = new HashMap<String,Object>();
        for (Object obj : pjp.getArgs()) {
            if (obj instanceof Map) {
                inputParam = (Map<String, Object>) obj;
            }
        }
        
        try {
        	logger.info("-------------------------------------------------------------------------");
        	logger.info("---- " + serviceFQN + " START...");
        	logger.info("---- paramMap : " + inputParam.toString());
        	
        	StopWatch stopWatch = new StopWatch();
    		stopWatch.start();
    		
    		returnObj = pjp.proceed();
    		
    		stopWatch.stop();
    		
    		resultTime = stopWatch.getTotalTimeMillis();
    		
    		logger.info("---- [" + serviceFQN + "()] 실행시간(ms) : " + stopWatch.getTotalTimeMillis());
    		logger.info("-------------------------------------------------------------------------");
        } catch (DataAccessException de) {
        	iProcCode = 2;
        	
        	if ((de.getRootCause() == null) || (de.getRootCause() instanceof SQLException)) {
        		sProcMsg = de.toString();
        	} else {
        		if (de.getRootCause() instanceof SQLException) {
        			SQLException se = (SQLException) de.getRootCause();
					sProcMsg = (new StringBuffer(de.toString()).append(se.toString())).toString();
        		} else {
        			sProcMsg = de.toString();
        		}
        	}
        	
        	throw de;
        } catch (Exception e) {
        	boolean isSqlException = false;
			DataAccessException de = null;
			Throwable t = null;
			
			if (e instanceof UncategorizedDataAccessException) {
				do {
					t = e.getCause();
					
					if (t == null) {
						break;
					}
					
					e = (Exception) t;
					if (!(t instanceof DataAccessException)) {
						continue;
					}
					
					de = (DataAccessException) t;
					isSqlException = true;
					break;
				} while (true);
			}
			
			if (isSqlException) {
				iProcCode = 2;
				if (de.getRootCause() != null) {
					SQLException se = (SQLException) de.getRootCause();
					sProcMsg = (new StringBuffer(de.toString()).append(se.toString())).toString();
				} else {
					sProcMsg = de.toString();
				}
			} else {
				iProcCode = 2;
				sProcMsg = e.toString();
			}
			
			throw e;
        } finally {
        	// TODO : 메소드 실행 로그 작성
        	
//        	try {
//        		boolean igMenuFlag = false;
//        		
//        		String menucode = CommonUtil.parseString(inputParam.get("GV_MENU_CD"), "");
//        		
//        		if (!igMenuFlag) {
//        			if("Y".equals(useHistoryFlag)) {
//        				Map<String,Object> logMap  = new HashMap<String,Object>();
//        				Map<String,Object> userMap = (Map<String,Object>)SessionManager.getAttribute(Namespace.SESSION_USERINFO);
//        				String userId = "";
//        				
//        				if(userMap != null) {
//        					userId = CommonUtil.parseString(userMap.get("USER_ID"), "");
//        				} 
//        				
//        				logMap.put("REQ_PARAM"   , inputParam.toString());
//        				logMap.put("CALL_URL"    , ctr);
//        				logMap.put("SES_USER_ID" , userId);
//        				logMap.put("CALL_FUNC_CD", serviceFQN);
//        				logMap.put("SYS_CD"      , "EC");
//        				logMap.put("MENU_CD"     , menucode);
//        				logMap.put("LOGIN_IP"    , inputParam.get("remoteNetworkAddress"));
//        				logMap.put("ERR_TCD"     , Integer.valueOf(iProcCode));
//        				logMap.put("ERR_MSG"     , sProcMsg);
//        				logMap.put("EXECUTE_LT"  , resultTime);
//        				
//        				service.saveLogNewTx(logMap);
//        			}
//        		}
//        		
//        	} catch (Exception e) {
//        		log.error("saveLogNewTx : ", e);
//        	}
        }
		
		return returnObj;
	}
}
