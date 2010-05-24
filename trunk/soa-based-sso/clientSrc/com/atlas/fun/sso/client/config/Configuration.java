package com.atlas.fun.sso.client.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuration {
	private static final Log logger = LogFactory.getLog(Configuration.class);

	public static XMLConfiguration _getXMLConfiguration() throws ConfigurationException {
		final String configPath = Configuration.class.getResource("./config.xml").getFile();
		return new XMLConfiguration(configPath);
	}

	public static String getStringValue(String key) {
		try {
			return _getXMLConfiguration().getString(key);
		} catch(Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static int getIntValue(String key){
		try {
			return _getXMLConfiguration().getInt(key);
		} catch(Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public static String getFilePath(String key) {
		try {
			final String path = _getXMLConfiguration().getString(key);
			if(path == null) return null;
			return path.replaceAll("\\\\", "/").replaceAll("/$", "");
		} catch(Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static void main(String[] args) {
		logger.debug(Configuration.getStringValue("estimation-path"));
	}
}
