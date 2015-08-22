/*
 * Create Author  : chen.chen.9
 * Create Date    : 2014-3-21
 * File Name      : PunctuationConstants.java
 */

package com.iflytek.ossp.framework.dt.utils;

/**
 * 标点符号常量枚举
 * <p>
 * 
 * @author chen.chen.9
 * @date 2014-3-21
 */
public enum PunctuationConstants {
	/** 逗号 */
	COMMA(","),

	/** 分号 */
	SEMI_COLON(";"),

	/** 问号 */
	QUESTION_MARK("?"),

	/** 句号 */
	PERIOD("。"),

	/** 点 */
	POINT("."),

	/** 星 */
	STAR("*"),

	/** 冒号 */
	COLON(":");

	/** value */
	private String value;

	/**
	 * constructor
	 * 
	 * @param value
	 *            value
	 */
	private PunctuationConstants(String value) {
		this.value = value;
	}

	/**
	 * getter method
	 * 
	 * @see PunctuationConstants#value
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
