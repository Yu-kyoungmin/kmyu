package com.kmp.common.util;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 공통 Util.
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
public class CommonUtil {
	private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public static Object getBean(String beanName) {
		// ServletContext 객체 가져오기
		ServletContext context = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext();

		// SpringContext 객체 가져오기
		WebApplicationContext wContext = WebApplicationContextUtils.getWebApplicationContext(context);

		return wContext.getBean(beanName);
	}
	
	/**
	 * String 변환
	 * @param obj
	 * @param replacement
	 * @return
	 */
	public static String parseString(Object obj, String replacement) {
		
		String str = "";		
		try {
			if(obj == null) {
				str = replacement;
			} else {
				String strr = String.valueOf(obj);
				if(strr == null) {					
					str = replacement;
				} else {				
					String val = strr.trim().toLowerCase();
					if("".equals(val) || "null".equals(val)) {						
						str = replacement;
					} else {						
						str = strr;
					}
				}
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			str = replacement;
		}
		
		return str;
	}
}
