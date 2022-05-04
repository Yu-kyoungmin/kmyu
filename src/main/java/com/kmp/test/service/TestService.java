package com.kmp.test.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.core.service.BaseService;

/**
 * 테스트 Service.
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
@Service
public class TestService extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(TestService.class);
	
	public Map<String,Object> test(Map<String,Object> paramMap) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		logger.debug("----- test Service");
		resultMap.put("AAA", "AAAA");
		resultMap.put("list", getSqlManager("sqlManagerOra").selectList("test.selectTest"));
		
		return resultMap;
	}
}
