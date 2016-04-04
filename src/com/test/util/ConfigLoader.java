package com.test.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class loads the proxy.properties & log4j file and populate all
 * key-values in props collection.
 * 
 * @author Irfan
 *
 */
public class ConfigLoader {

	private static final String CONFIG_FILE_NAME = "proxy.properties";
	private static final String LOG_FILE_NAME = "log4j.properties";

	private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class.getName());

	public static final Properties props = new Properties();

	public void loadConfigFile() {
		// Load log4j file
		URL resource = ConfigLoader.class.getClassLoader().getResource(LOG_FILE_NAME);
		PropertyConfigurator.configure(resource);

		URL resource2 = ConfigLoader.class.getClassLoader().getResource(CONFIG_FILE_NAME);
		try (InputStream inputStream = resource2.openConnection().getInputStream();) {
			props.load(inputStream);

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Properties read : " + props);

			LOGGER.info("Loaded properties files successfully");

		} catch (Exception e) {
			LOGGER.error("Error while loading config files :", e);
		}
	}

}
