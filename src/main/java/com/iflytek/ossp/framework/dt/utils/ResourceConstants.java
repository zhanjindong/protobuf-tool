/*
 * Create Author  : chen.chen.9
 * Create Date    : 2014-3-19
 * File Name      : ResourceConstants.java
 */

package com.iflytek.ossp.framework.dt.utils;

/**
 * 与资源寻址相关的常量枚举
 * <p>
 * 
 * @author chen.chen.9
 * @date 2014-3-19
 */
public enum ResourceConstants {
	/** 从项目和jar中读取资源的URL前缀 */
	CLASSPATH_ALL_URL_PREFIX("classpath*:"),

	/** 从项目中读取资源的URL前缀 */
	CLASSPATH_URL_PREFIX("classpath:"),

	/** 从文件系统中读取资源的URL前缀 */
	FILE_URL_PREFIX("file:"),

	/** 上层路径 */
	TOP_PATH(".."),

	/** 当前路径 */
	CURRENT_PATH("."),

	/** linux文件夹分隔符 */
	FOLDER_SEPARATOR("/"),

	/** windows文件分隔符 */
	WINDOWS_FOLDER_SEPARATOR("\\");

	/** value */
	private String value;

	/**
	 * constructor
	 * 
	 * @param value
	 *            value
	 */
	private ResourceConstants(String value) {
		this.value = value;
	}

	/**
	 * getter method
	 * 
	 * @see ResourceConstants#value
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
