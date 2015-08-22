package com.iflytek.ossp.framework.dt.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

/**
 * 反射工具类
 * 
 * @author jdzhan
 *
 */
public class ReflectionUtil {

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	/**
	 * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
	 * 
	 * @param clazz
	 *            目标类
	 * @param methodName
	 *            方法名
	 * @param classes
	 *            方法参数类型数组
	 * @return 方法对象
	 * @throws Exception
	 */
	public static Method findMethod(Class clazz, String methodName,
			final Class[] classes) throws Exception {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException ex) {
				if (clazz.getSuperclass() == null) {
					return method;
				} else {
					method = findMethod(clazz.getSuperclass(), methodName,
							classes);
				}
			}
		}
		return method;
	}

	public static Method findMethod(Class targetClass, String methodName)
			throws Exception {
		return findMethod(targetClass, methodName, new Class[] {});
	}

	/**
	 * 
	 * @param obj
	 *            调整方法的对象
	 * @param methodName
	 *            方法名
	 * @param classes
	 *            参数类型数组
	 * @param objects
	 *            参数数组
	 * @return 方法的返回值
	 */
	public static Object invoke(final Object obj, final String methodName,
			final Class[] classes, final Object[] objects) {
		try {
			Method method = findMethod(obj.getClass(), methodName, classes);
			method.setAccessible(true);// 调用private方法的关键一句话
			return method.invoke(obj, objects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getFieldValue(Field field, Object target) {
		try {
			return field.get(target);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object invoke(final Object obj, final String methodName,
			final Class[] classes) {
		return invoke(obj, methodName, classes, new Object[] {});
	}

	public static Object invoke(final Object obj, final String methodName) {
		return invoke(obj, methodName, new Class[] {}, new Object[] {});
	}

	public static boolean isCollection(Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}

	public static boolean isWrapperType(Class<?> type) {
		return WRAPPER_TYPES.contains(type);
	}

	public static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive();
	}

	public static boolean isArray(Class<?> type) {
		return type.isArray();
	}

	public static boolean isByteArray(Class<?> type) {
		return (isArray(type) && byte[].class.isAssignableFrom(type));
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		return ret;
	}

	public static boolean isBaseType(Class<?> type) {
		return isWrapperType(type) || isPrimitive(type)
				|| type.isAssignableFrom(String.class);
	}

	public static LinkedHashMap<String, Field> getAllFieldsByOrder(
			Class<?> clazz) {
		LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
		if (clazz == null) {
			return fields;
		}
		Field[] arr = clazz.getFields();
		for (Field field : arr) {
			fields.put(field.getName(), field);
		}
		return fields;
	}
}