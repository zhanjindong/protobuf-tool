package com.iflytek.ossp.framework.dt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 封装一些常用的正则匹配操作。
 * <p>
 * <b>示例：</b>
 * {@link com.iflytek.ossp.framework.example.common.text.RegexUtilsExample}
 * 
 * @author jdzhan,2014-7-29
 * 
 */
public class RegexUtils {

	/** 手机号码 */
	private static final String CHN_MOBILE_PHONE = "^(1(([35][0-9])|(47)|(70)|(76)|(77)|(78)|[8][012356789]))\\d{8}$";
	/** 邮箱地址 */
	private static final String EMAIL = "(\\w|-)+@(\\w|\\.)+";
	/** IP地址 */
	private static final String IP = "(((1[0-9]{2}|(2[0-4][0-9]|25[0-5]))|[1-9]?[0-9])\\.){3}((1[0-9]{2}|(2[0-4][0-9]|25[0-5]))|[1-9]?[0-9])";
	/** URL */
	private static final String URL = "^[a-zA-z]+:\\/\\/(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\:([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5]))?(\\/\\w*)*(\\.\\w*)?(\\?\\S*)?$/";

	/**
	 * 
	 * 将整个输入串与模式匹配。
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            需要匹配的字符串
	 * @param caseInsensitive
	 *            是否忽略大小写
	 * @return true 或 false
	 */
	public static boolean match(String regex, String input,
			boolean caseInsensitive) {
		if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(input))
			return false;

		Pattern pat = null;
		if (caseInsensitive) {
			pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pat = Pattern.compile(regex);
		}

		Matcher m = pat.matcher(input);
		return m.matches();
	}

	/**
	 * 
	 * 查找输入串中与模式匹配的所有子串。
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            输入的字符串
	 * @param caseInsensitive
	 *            是否忽略大小写
	 * @return 匹配到的子串列表
	 */
	public static List<String> findAll(String regex, String input,
			boolean caseInsensitive) {
		List<String> result = new ArrayList<String>();
		if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(input))
			return result;

		Pattern pat = null;
		if (caseInsensitive) {
			pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pat = Pattern.compile(regex);
		}

		Matcher m = pat.matcher(input);
		while (m.find()) {
			result.add(m.group(0));
		}
		return result;
	}

	/**
	 * 
	 * 用给定的模式去分割输入的字符串。
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            输入字符串
	 * @param caseInsensitive
	 *            是否忽略大小写
	 * @return 分割后的数组
	 */
	public static String[] split(String regex, String input,
			boolean caseInsensitive) {
		if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(input))
			throw new IllegalArgumentException(
					"regex and value can not be null or empty!");

		Pattern pat = null;
		if (caseInsensitive) {
			pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pat = Pattern.compile(regex);
		}

		String[] result = pat.split(input);

		return result;
	}

	/**
	 * 
	 * 替换输入字符串中所有匹配到的子串。
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            输入的字符串
	 * @param replacement
	 *            替换的字符串
	 * @param caseInsensitive
	 *            是否忽略大小写
	 * @return 替换后的字符串
	 */
	public static String replaceAll(String regex, String input,
			String replacement, boolean caseInsensitive) {
		if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(input)
				|| replacement == null)
			throw new IllegalArgumentException(
					"regex,source and replace can not be empty or null!");

		Pattern pat = null;
		if (caseInsensitive) {
			pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		} else {
			pat = Pattern.compile(regex);
		}
		Matcher m = pat.matcher(input);
		return m.replaceAll(replacement);
	}

	/**
	 * 
	 * 验证字符串是否是IPv4格式。
	 * 
	 * @param input
	 *            输入的字符串
	 * @return true 或 false
	 */
	public static boolean isIPv4(String input) {
		return match(IP, input, false);
	}

	public static boolean isURL(String input) {
		return match(URL, input, false);
	}

	/**
	 * 
	 * 验证字符串是否是电子邮箱地址格式。
	 * 
	 * @param input
	 *            输入的字符串
	 * @return true 或 false
	 */
	public static boolean isEmailAddress(String input) {
		return match(EMAIL, input, false);
	}

	/**
	 * 
	 * 验证字符串是否是中国手机号码格式。
	 * 
	 * @param input
	 *            输入的字符串
	 * @return true 或 false
	 */
	public static boolean isCHNMobile(String input) {
		return match(CHN_MOBILE_PHONE, input, false);
	}
}
