package io.jindong.pbtool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;

/**
 * 
 * 对{@link HttpURLConnection}类的简单封装，提供get和post方法。
 * <p>
 * <b>示例：</b>
 * {@link com.iflytek.ossp.framework.example.UrlConnectionHelperExample}
 * 
 * @author jdzhan,2014-7-29
 * 
 */
public class UrlConnectionUtils {

	/**
	 * 
	 * get请求。
	 * 
	 * @param reqUrl
	 *            请求地址
	 * @param timeout
	 *            超时时间。注意这个值指定了连接超时和读取超时这两个值。
	 * @param encoding
	 *            字符编程
	 * @param result
	 *            请求响应的内容
	 * @return 请求成功返回true
	 * @throws IOException
	 */
	public static boolean get(String url, int timeout, String encoding,
			StringBuilder result) throws IOException {
		return get(url, timeout, encoding, null, result);
	}

	/**
	 * 
	 * get请求{@link UrlConnectionUtils#get}。
	 * 
	 * @param heads
	 *            请求是需要附加的请求头
	 * @return
	 * @throws IOException
	 */
	public static boolean get(String url, int timeout, String encoding,
			Map<String, String> headers, StringBuilder result)
			throws IOException {

		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedReader br = null;

		try {
			urlConnection = cretateConnection("GET", url, timeout, headers);
			is = urlConnection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, encoding));
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				result.append(readLine);
			}
		} finally {
			close(br);
			close(is);
			close(urlConnection);
		}
		return true;
	}

	/**
	 * 
	 * post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param data
	 *            post内容
	 * @param encoding
	 *            字符编码
	 * @param timeout
	 *            超时时间。注意这个值指定了连接超时和读取超时这两个值。
	 * @param result
	 *            响应内容
	 * @return 请求成功返回true
	 * @throws IOException
	 */
	public static boolean post(String url, String data, String encoding,
			int timeout, StringBuilder result) throws IOException {
		return post(url, data, encoding, timeout, null, result);
	}

	/**
	 * 
	 * post请求{@link UrlConnectionUtils#post}。
	 * 
	 * @param headers
	 *            请求时需要附加的请求头
	 * @return
	 * @throws IOException
	 */
	public static boolean post(String url, String data, String encoding,
			int timeout, Map<String, String> headers, StringBuilder result)
			throws IOException {

		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedReader br = null;

		try {
			urlConnection = cretateConnection("POST", url, timeout, headers);
			OutputStream out = urlConnection.getOutputStream(); // send post
			out.write(data.getBytes(encoding));
			out.flush();
			out.close();

			is = urlConnection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				result.append(readLine);
			}
		} finally {
			close(br);
			close(is);
			close(urlConnection);
		}
		return true;
	}

	public static byte[] post(String url, byte[] data, String encoding,
			int timeout, Map<String, String> headers) throws IOException {

		HttpURLConnection urlConnection = null;
		InputStream is = null;
		BufferedReader br = null;

		try {
			urlConnection = cretateConnection("POST", url, timeout, headers);
			OutputStream out = urlConnection.getOutputStream(); // send post
			out.write(data);
			out.flush();
			out.close();

			is = urlConnection.getInputStream();
			return IOUtils.read(is);
		} finally {
			close(br);
			close(is);
			close(urlConnection);
		}
	}

	private static HttpURLConnection cretateConnection(String method,
			String reqString, int timeout, Map<String, String> headers)
			throws IOException {
		URL url = new URL(reqString);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setReadTimeout(timeout);
		urlConnection.setConnectTimeout(timeout);

		fillHeaders(urlConnection, headers);

		urlConnection.setRequestMethod(method); // request method, default
												// GET
		if (method.equals("POST")) {
			urlConnection.setUseCaches(false); // Post can not user cache
			urlConnection.setDoOutput(true); // set output from urlconn
			urlConnection.setDoInput(true); // set input from urlconn
		}
		return urlConnection;
	}

	private static void fillHeaders(HttpURLConnection conn,
			Map<String, String> heads) {
		if (MapUtils.isEmpty(heads)) {
			return;
		}
		for (Entry<String, String> entry : heads.entrySet()) {
			conn.addRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	private static void close(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				// ignored
			}
		}
	}

	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// ignored
			}
		}
	}

	private static void close(HttpURLConnection conn) {
		if (conn != null) {
			conn.disconnect();
		}
	}
}
