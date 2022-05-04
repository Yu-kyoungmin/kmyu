package com.core.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.core.dataaccess.SqlManager;
import com.kmp.common.util.CommonUtil;


@Service
public class MessageResourceService {
	private static Logger logger = LoggerFactory.getLogger(MessageResourceService.class);
	
	public static List<Map<String,Object>> messageList = new ArrayList<Map<String,Object>>();
	public static final Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>();
	
	private Timer reloadableDatabaseMessageCheckTimer;
	
	private static final long RELOAD_DELAY  = 10000;			// 10초
	private static final long RELOAD_PERIOD = 36000000;			// 10시간
	
	public static String recentLastUpdateDttm     = null;
	
	private SqlManager sqlManager = null;
	
	
	public void setSqlManager(SqlManager sqlManager) {
		
		this.sqlManager = sqlManager;
	}
	
	
	/**
	 * WAS 최초 로딩시 다국어 데이터 조회 한다.
	 */
	@PostConstruct
	public void loadAllMessages() {
		Map<String, Object> param = new HashMap<String, Object>();
		MessageResourceService.messageList = sqlManager.selectList("messageResource.selectMessageAndLabelList", param);
		startReloadableDatabaseMessageCheckTimer();
	}
	
	
	
	/**
	 * 다국어 데이터를 프로퍼티 Map에 셋한다.
	 */
	public static void loadTexts() {
		List<Map<String, Object>> texts = MessageResourceService.messageList;
		
		
		String isoLocale      = null;
		String lastUpdateDttm = null;
		int    idx            = 1;
		Map<String, Object> v = new HashMap<String, Object>();
		
		for (Map<String, Object> map : texts) {
			String msgCd    = CommonUtil.parseString(map.get("MSG_CD"), "");
			int    msgCdCnt = Integer.parseInt(CommonUtil.parseString(map.get("MSG_CD_CNT"), "0"));
			
			isoLocale      = CommonUtil.parseString(map.get("LANG_CD"), "KO").toLowerCase();
			lastUpdateDttm = CommonUtil.parseString(map.get("LAST_UPDATE_DTTM"), "");
			
			
			if(!"".equals(lastUpdateDttm) && checkDate(lastUpdateDttm)) {
				if(MessageResourceService.recentLastUpdateDttm == null) {
					MessageResourceService.recentLastUpdateDttm = lastUpdateDttm;
				}
				
				if(lastUpdateDttm.compareTo(MessageResourceService.recentLastUpdateDttm) > 0) {
					MessageResourceService.recentLastUpdateDttm = lastUpdateDttm;
				}
			}
			
			v.put(isoLocale, map.get("MSG_NM"));
			
			if(idx == msgCdCnt) {
				properties.put(msgCd, v);
				v = new HashMap<String, Object>();
				idx = 1;
			} else {
				idx++;
			}
		}
	}
	
	/**
	 * 날짜 체크
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean checkDate(String str) {
		boolean dateValidity = true;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setLenient(false);
		
		try {
			Date dt = df.parse(str);
		} catch(ParseException pe) {
			dateValidity = false;
		} catch(IllegalArgumentException ae) {
			logger.error("IllegalArgumentException: " + ae);
			dateValidity = false;
		}
		
		return dateValidity;
	}
	
	/**
	 * 다국어 타이머 설정
	 */
	private void startReloadableDatabaseMessageCheckTimer() {
		synchronized(this) {
			if(reloadableDatabaseMessageCheckTimer == null) {
				reloadableDatabaseMessageCheckTimer = new Timer("reloadableDatabaseMessageCheckTimer", true);
				reloadableDatabaseMessageCheckTimer.schedule(new ReloadableDatabaseMessageCheckTimerTask(Thread.currentThread().getContextClassLoader()),RELOAD_DELAY, RELOAD_PERIOD);
			}
		}
	}
	
	
	/**
	 * 다국어 타이머 Class
	 * @author KMYU
	 * @since 2021. 3. 26.
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
	private class ReloadableDatabaseMessageCheckTimerTask extends TimerTask {
		
		private ClassLoader classLoader;
		
		private ReloadableDatabaseMessageCheckTimerTask(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		@Override
		public void run() {
			Thread.currentThread().setContextClassLoader(classLoader);
			
			synchronized(ReloadableDatabaseMessageCheckTimerTask.class) {
				refreshModifiedMessages();
			}
		}
		
		public void refreshModifiedMessages() {
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("LAST_UPDATE_DTTM", recentLastUpdateDttm);
			
			MessageResourceService.messageList = sqlManager.selectList("messageResource.selectMessageAndLabelList", param);
			
			if(MessageResourceService.messageList.size() > 0) {
				loadTexts();
			}
			
		}
	}
}
