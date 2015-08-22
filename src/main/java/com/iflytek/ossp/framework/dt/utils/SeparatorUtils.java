package com.iflytek.ossp.framework.dt.utils;

import java.util.Properties;

public class SeparatorUtils {

	/* system properties to get separators */
	static final Properties PROPERTIES = new Properties(System.getProperties());

	/**
	 * get line separator on current platform
	 * 
	 * @return line separator
	 */
	public static String getLineSeparator() {
		return PROPERTIES.getProperty("line.separator");
	}

	/**
	 * get path separator on current platform
	 * 
	 * @return path separator
	 */
	public static String getPathSeparator() {
		return PROPERTIES.getProperty("path.separator");
	}

	/**
	 * get file separator on current platform
	 * 
	 * @return path separator
	 */
	public static String getFileSeparator() {
		return PROPERTIES.getProperty("file.separator");
	}

}

