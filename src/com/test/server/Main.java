package com.test.server;

import org.apache.log4j.Logger;

import com.test.util.ConfigLoader;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		try {
			new ConfigLoader().loadConfigFile();
			new Thread(new TCPServer(), "TCP-server").start();
		} catch (Exception e) {
			LOGGER.error("Error occured during application startup", e);
		}
	}
}
