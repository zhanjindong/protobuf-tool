package com.iflytek.ossp.framework.dt.utils;

import java.io.UnsupportedEncodingException;

public class StringUtils {
	public static byte[] encodeUTF8(String src) {
		try {
			return src.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return src.getBytes();
		}
	}

	public static String decodeUTF8(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(bytes);
		}
	}
}
