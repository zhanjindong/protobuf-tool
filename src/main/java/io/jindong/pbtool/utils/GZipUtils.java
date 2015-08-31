package io.jindong.pbtool.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * GZip压缩节压缩工具类。
 * <p>
 * <b>示例：</b>
 * {@link com.iflytek.ossp.framework.example.common.utils.GZipBenchmark}
 * 
 * @author jdzhan,2014-7-29
 * 
 */
public class GZipUtils {

	/**
	 * GZip压缩字节数组
	 * 
	 * @param data
	 *            需要压缩的字节数组
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException {

		ByteArrayOutputStream out = null;
		GZIPOutputStream gzipOut = null;
		try {
			out = new ByteArrayOutputStream();
			gzipOut = new GZIPOutputStream(out);
			gzipOut.write(data);
		} finally {
			IOUtils.close(gzipOut);// 获得输出流的数据之前，需要将gzip流关闭
		}
		return out.toByteArray();
	}

	/**
	 * GZip解压缩字节数组
	 * 
	 * @param data
	 *            需要解压缩的字节数组
	 * @return 解压缩的字节数组
	 * @throws IOException
	 */
	public static byte[] decompress(byte[] data) throws IOException {
		ByteArrayInputStream input = null;
		GZIPInputStream gzipInput = null;
		ByteArrayOutputStream output = null;
		try {
			input = new ByteArrayInputStream(data);
			gzipInput = new GZIPInputStream(input);
			output = new ByteArrayOutputStream();
			IOUtils.copy(gzipInput, output);
		} catch (EOFException ex) {
			if (output.size() > 0) {
				return output.toByteArray();
			} else
				throw ex;
		} finally {
			IOUtils.close(gzipInput);
		}
		return output.toByteArray();
	}
}
