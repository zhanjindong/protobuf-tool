package com.iflytek.ossp.framework.dt.utils;

public class EncryptUtils {
	/**
	 * 异或解密加密
	 * 
	 * @param source
	 * @param keyPrefix
	 * @return
	 */
	public static byte[] xOrEncrypt(byte[] source, String keyPrefix) {

		String keyString = keyPrefix + source.length;
		byte[] keyBytes = StringUtils.encodeUTF8(keyString);
		if (source.length < keyBytes.length) {
			return new byte[0];
		}
		for (int i = 0; i < keyBytes.length; i++) {
			source[i] = (byte) (source[i] ^ keyBytes[i]);
		}

		return source;
	}
}
