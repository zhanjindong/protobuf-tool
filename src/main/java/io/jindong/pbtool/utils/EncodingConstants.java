/*
 * Create Author  : chen.chen.9
 * Create Date    : 2014-3-20
 * File Name      : EncodingConstants.java
 */

package io.jindong.pbtool.utils;

/**
 * 编码类型常量枚举
 * <p>
 * 
 * @author chen.chen.9
 * @date 2014-3-20
 */
public enum EncodingConstants {
	/** UTF-8 */
	UTF8("UTF-8"),

	/** GB2312 */
	GB2312("GB2312"),

	/** GBK */
	GBK("GBK");

	/** value */
	private String value;

	/**
	 * constructor
	 * 
	 * @param value
	 *            value
	 */
	private EncodingConstants(String value) {
		this.value = value;
	}

	/**
	 * getter method
	 * 
	 * @see EncodingConstants#value
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
