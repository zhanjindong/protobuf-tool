/*
 * Create Author  : chen.chen.9
 * Create Date    : 2014-3-27
 * File Name      : NormalizationUtils.java
 */

package io.jindong.pbtool.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 标准化工具类
 * <p>
 * 
 * @author chen.chen.9
 * @date 2014-3-27
 */
public class NormalizationUtils {
	/**
	 * 删除换行(LF:\n)
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-27
	 * @param original
	 *            原始字符串
	 * @return 生成字符串
	 */
	public static String removeLinefeed(String original) {
		return StringUtils.remove(original, "\n");
	}

	/**
	 * 删除回车(CR:\r)
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-27
	 * @param original
	 *            原始字符串
	 * @return 生成字符串
	 */
	public static String removeCarriageReturn(String original) {
		return StringUtils.remove(original, "\r");
	}

	/**
	 * 删除水平制表符(HT:\t)
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-27
	 * @param original
	 *            原始字符串
	 * @return 生成字符串
	 */
	public static String removeHorizontalTab(String original) {
		return StringUtils.remove(original, "\t");
	}

	/**
	 * 对json标准化
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-27
	 * @param original
	 *            原始字符串
	 * @return 生成字符串
	 */
	public static String normalizeJson(String original) {
		return StringUtils.replace(NormalizationUtils.removeHorizontalTab(NormalizationUtils
				.removeLinefeed(NormalizationUtils.removeCarriageReturn(original))), " ", "");
	}

	/**
	 * 对xml标准化
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-27
	 * @param original
	 *            原始字符串
	 * @return 生成字符串
	 */
	public static String normalizeXml(String original) {
		return NormalizationUtils.removeHorizontalTab(
				NormalizationUtils.removeLinefeed(NormalizationUtils.removeCarriageReturn(original))).replaceAll(
				">\\s*<", "><");
	}

	public static String normalizeFilePath(String path) {
		return path.replaceAll(":", "").replaceAll("[?]", "").replaceAll(">", "").replaceAll("<", "")
				.replaceAll("\\|", "").replaceAll("\\\\", "").replaceAll("/", "");
	}
}
