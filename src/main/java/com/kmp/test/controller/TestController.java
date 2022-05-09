package com.kmp.test.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kmp.common.util.CommonUtil;
import com.kmp.test.service.TestService;

/**
 * TEST Controller
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
@Controller
@RequestMapping("/test")
public class TestController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private View jsonView;
	
	@Value("#{common['server.mode']}")
	private String serverMode;
	
	@Autowired
	private TestService service;
	
	@RequestMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("serverMode" , CommonUtil.parseString(serverMode , ""));
		return "/test/test";
	}
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		return new ModelAndView(jsonView, "result", service.test(paramMap));
	}
	
	@RequestMapping(value = "/chang_lang")
	public String changLang(HttpServletRequest request, HttpServletResponse response, Model model) {
		String lang = "EN";
		logger.debug("--- lang : {}", lang);
		Locale locale = new Locale(lang);
		
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		localeResolver.setLocale(request, response, locale);
		return "/test/test";
	}
}
