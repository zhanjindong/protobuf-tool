/*
 * Create Author  : chen.chen.9
 * Create Date    : 2014-3-19
 * File Name      : ResourceUtils.java
 */

package com.iflytek.ossp.framework.dt.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 文件路径寻址工具<br />
 * 开发者可以从{@link ResourceConstants}中查找支持的三种路径寻址前缀<br />
 * 举例：ResourceUtils.loadResource("classpath*:log/log4j.xml");
 * <p>
 * 
 * @author chen.chen.9
 * @author jdzhan
 * @date 2014-3-19
 */
public class ResourceUtils {
	/** 加锁工具 */
	private static final byte[] BYTES = new byte[0];

	/** {@link ClassLoader} */
	private static ClassLoader classLoader;

	/**
	 * 从项目，jar或文件系统中读取指定路径的文件<br />
	 * 与loadResources()区别是本方法在有返回值时默认只返回一条记录，其余丢弃
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-19
	 * @param locationPattern
	 * <br/>
	 *            0. 路径寻址前缀请参见{@link ResourceConstants}<br />
	 *            1. 使用file，classpath和classpath*做路径开头<br />
	 *            2. classpath寻址项目中的文件<br />
	 *            3. classpath*既寻址项目，也寻址jar包中的文件<br />
	 *            4. file寻址文件系统中的文件<br />
	 *            5. 默认是classpath 6.
	 *            例如：classpath*:log/log4j.xml;file:/home/ydhl/
	 *            abc.sh;classpath:log/log4j.xml
	 * @return 以URL返回结果
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static URL loadResource(String locationPattern) throws IOException,
			URISyntaxException {
		URL[] urlArray = loadResources(locationPattern);

		return ArrayUtils.isEmpty(urlArray) ? null : urlArray[0];
	}

	/**
	 * 从项目，jar或文件系统中读取指定路径的文件<br />
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-19
	 * @author jdzhan,支持file通配符*寻址
	 * @param locationPattern
	 * <br/>
	 *            0. 路径寻址前缀请参见{@link ResourceConstants}<br />
	 *            1. 使用file，classpath和classpath*做路径开头<br />
	 *            2. classpath寻址项目中的文件<br />
	 *            3. classpath*既寻址项目，也寻址jar包中的文件<br />
	 *            4. file寻址文件系统中的文件<br />
	 *            5. 默认是classpath 6.
	 *            例如：classpath*:log/log4j.xml；file:/home/ydhl/
	 *            abc.sh；classpath:log/log4j.xml
	 * @return 以URL返回结果
	 * @throws IOException
	 * @throws URISyntaxException
	 *             中文路径支持
	 */
	public static URL[] loadResources(String locationPattern)
			throws IOException, URISyntaxException {
		if (locationPattern
				.startsWith(ResourceConstants.CLASSPATH_ALL_URL_PREFIX
						.getValue())) {
			return load1(locationPattern);
		} else if (locationPattern
				.startsWith(ResourceConstants.CLASSPATH_URL_PREFIX.getValue())) {
			return load2(locationPattern);
		} else if (locationPattern.startsWith(ResourceConstants.FILE_URL_PREFIX
				.getValue())) {
			return load3(locationPattern);
		} else {
			// 默认为文件系统路径。
			locationPattern = "file:" + locationPattern;
			return ResourceUtils.loadResources(locationPattern);
		}
	}

	private static URL[] load1(String locationPattern) throws IOException,
			URISyntaxException {
		String location = locationPattern
				.substring(ResourceConstants.CLASSPATH_ALL_URL_PREFIX
						.getValue().length());
		if (location.startsWith(ResourceConstants.FOLDER_SEPARATOR.getValue())) {
			location = location.substring(1);
		}

		Enumeration<URL> resourceUrls = getDefaultClassLoader().getResources(
				location);
		Set<URL> result = new LinkedHashSet<URL>(16);
		while (resourceUrls.hasMoreElements()) {
			URL url = resourceUrls.nextElement();
			result.add(PathUtils.cleanPath(url));
		}
		return result.toArray(new URL[result.size()]);
	}

	private static URL[] load2(String locationPattern)
			throws URISyntaxException, IOException {
		String location = locationPattern
				.substring(ResourceConstants.CLASSPATH_URL_PREFIX.getValue()
						.length());
		if (location.startsWith(ResourceConstants.FOLDER_SEPARATOR.getValue())) {
			location = location.substring(1);
		}

		String cleanPath = PathUtils.cleanPath(location);
		// 只支持文件的通配符匹配，不支持文件夹的通配符匹配
		// 如需实现文件夹的通配符匹配，请参照spring.utils包，较复杂
		if (StringUtils.contains(cleanPath,
				PunctuationConstants.STAR.getValue())
				|| StringUtils.contains(cleanPath,
						PunctuationConstants.QUESTION_MARK.getValue())) {
			String directoryPath = StringUtils.substringBeforeLast(
					locationPattern,
					ResourceConstants.FOLDER_SEPARATOR.getValue());
			File directory = new File(ResourceUtils.loadResource(directoryPath)
					.toURI().getPath());

			String filePattern = StringUtils.substringAfter(cleanPath,
					ResourceConstants.FOLDER_SEPARATOR.getValue());
			while (filePattern.contains(ResourceConstants.FOLDER_SEPARATOR
					.getValue())) {
				filePattern = StringUtils.substringAfter(filePattern,
						ResourceConstants.FOLDER_SEPARATOR.getValue());
			}

			Set<URL> result = new LinkedHashSet<URL>(16);
			Iterator<File> iterator = FileUtils.iterateFiles(directory,
					new WildcardFileFilter(filePattern), null);
			while (iterator.hasNext()) {
				result.add(iterator.next().toURI().toURL());
			}

			return result.toArray(new URL[result.size()]);
		} else {
			// a single resource with the given name
			URL url = getDefaultClassLoader().getResource(cleanPath);
			// if (url == null) {
			// throw new UnsupportedOperationException(cleanPath);
			// }
			return url == null ? null : new URL[] { PathUtils.cleanPath(url) };
		}
	}

	private static URL[] load3(String locationPattern)
			throws MalformedURLException {
		if (StringUtils.contains(locationPattern,
				PunctuationConstants.STAR.getValue())
				|| StringUtils.contains(locationPattern,
						PunctuationConstants.QUESTION_MARK.getValue())) {
			String directoryPath = StringUtils.substringBeforeLast(
					locationPattern,
					ResourceConstants.FOLDER_SEPARATOR.getValue());
			directoryPath = StringUtils.substringAfter(directoryPath,
					ResourceConstants.FILE_URL_PREFIX.getValue());
			File directory = new File(directoryPath);
			String filePattern = StringUtils.substringAfter(locationPattern,
					ResourceConstants.FOLDER_SEPARATOR.getValue());
			while (filePattern.contains(ResourceConstants.FOLDER_SEPARATOR
					.getValue())) {
				filePattern = StringUtils.substringAfter(filePattern,
						ResourceConstants.FOLDER_SEPARATOR.getValue());
			}

			Set<URL> result = new LinkedHashSet<URL>(16);
			Iterator<File> iterator = FileUtils.iterateFiles(directory,
					new WildcardFileFilter(filePattern), null);
			while (iterator.hasNext()) {
				result.add(iterator.next().toURI().toURL());
			}

			return result.toArray(new URL[result.size()]);
		} else {
			// a single resource with the given name
			URL url = new URL(locationPattern);
			return new File(url.getFile()).exists() ? new URL[] { url } : null;
		}
	}

	/**
	 * 获取运行时classloader，首选线程上下文classloader，其次选择类classloader
	 * <p>
	 * 
	 * @author chen.chen.9, 2014-3-20
	 * @return {@link ClassLoader}
	 */
	public static ClassLoader getDefaultClassLoader() {
		if (classLoader != null) {
			return classLoader;
		}

		synchronized (BYTES) {
			if (classLoader == null) {
				ClassLoader tempClassLoader = null;
				try {
					tempClassLoader = Thread.currentThread()
							.getContextClassLoader();
				} catch (Exception ex) {
					// Cannot access thread context ClassLoader - falling back
					// to system class loader...
				}

				if (tempClassLoader == null) {
					// No thread context class loader -> use class loader of
					// this class.
					tempClassLoader = ResourceUtils.class.getClassLoader();
				}

				classLoader = tempClassLoader;
			}
		}

		return classLoader;
	}

	public static void createDirIfNotExist(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static void saveFileBeforeDeleteOld(String fileName, byte[] data) {
		File file = new File(fileName);
		saveFileBeforeDeleteOld(file, data);
	}

	public static void saveFileBeforeDeleteOld(File file, byte[] data) {
		if (file.exists()) {
			file.delete();
		}
		try {
			IOUtils.write(file, data, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveFileBeforeDeleteOld(String fileName, String data) {
		try {
			saveFileBeforeDeleteOld(fileName, data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] readFileIfExist(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		try {
			return IOUtils.read(file);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}