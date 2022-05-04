package com.core.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;


public class DatabaseDrivenMessageSource extends AbstractMessageSource implements ResourceLoaderAware {
	private static Logger logger = LoggerFactory.getLogger(DatabaseDrivenMessageSource.class);
	
	private ResourceLoader resourceLoader;
	
	
	
	public DatabaseDrivenMessageSource() {
		reload();
	}
	
	public DatabaseDrivenMessageSource(MessageResourceService messageResourceService) {
		reload();
	}
	
	public void reload() {
		MessageResourceService.loadTexts();
	}
	
	


	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = getText(code, locale);
		MessageFormat result = createMessageFormat(msg, locale);
		return result;
	}
	
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		return getText(code, locale);
	}

	private String getText(String code, Locale locale) {
		Map<String, Object> localized = MessageResourceService.properties.get(code);
		String textForCurrentLanguage = null;
		if (localized != null) {
			textForCurrentLanguage = (String) localized.get(locale.getLanguage());
			if (textForCurrentLanguage == null) {
				textForCurrentLanguage = (String) localized.get(Locale.KOREAN.getLanguage());
			}
		}
		if (textForCurrentLanguage == null) {
			try {
				textForCurrentLanguage = getParentMessageSource().getMessage(code, null, locale);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return textForCurrentLanguage != null ? textForCurrentLanguage : code;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceloader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}
	
	
}
