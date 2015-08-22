package com.iflytek.ossp.framework.dt;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;

import com.iflytek.ossp.framework.dt.PBTool.UserData;
import com.iflytek.ossp.framework.dt.utils.ReflectionUtil;

/**
 * 工具类。
 * 
 * @author jdzhan,2015年7月6日
 *
 */
public class PBUtils {
	public static Object dynamicCast(String src, Class<?> targetType) {
		if (StringUtils.isEmpty(src)) {
			return null;
		}
		if (targetType.isAssignableFrom(Integer.class)) {// Integer
			return Integer.parseInt(src);
		}
		if (targetType.isAssignableFrom(Long.class)) {// Long
			return Long.parseLong(src);
		}
		if (targetType.isAssignableFrom(Double.class)) {// Double
			return Double.parseDouble(src);
		}
		if (targetType.isAssignableFrom(Float.class)) {// Float
			return Float.parseFloat(src);
		}
		if (targetType.isAssignableFrom(Boolean.class)) {// Float
			return Boolean.parseBoolean(src);
		}
		if (ReflectionUtil.isByteArray(targetType)) {// byte[]
			return com.iflytek.ossp.framework.dt.utils.StringUtils
					.encodeUTF8(src.toString());
		}
		return src;
	}

	public static String getMsgName(Class<?> type) {
		String name = type.getSimpleName();
		if (name.contains("JProtoBufProtoClass")) {
			name = StringUtils.substringBefore(name, "JProtoBufProtoClass");
		}
		return name;
	}

	public static DefaultMutableTreeNode convertToTreeNode(Object obj) {
		if (obj instanceof DefaultMutableTreeNode) {
			return (DefaultMutableTreeNode) obj;
		}
		throw new ClassCastException();
	}

	public static Class<?> getElementType(String fieldName, Class<?> fieldType,
			Class<?> objectType) {
		try {
			Class<?> elementType = null;
			String methodName = "get" + StringUtils.capitalize(fieldName)
					+ "ElementType";
			Method method = ReflectionUtil.findMethod(objectType, methodName);
			elementType = (Class<?>) method.invoke(objectType.newInstance(),
					null);
			return elementType;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isLeafType(Class<?> type) {
		if (ReflectionUtil.isBaseType(type) || ReflectionUtil.isByteArray(type)
				|| ProtoIDLProxy.isEnumReadable(type)) {
			return true;
		}
		return false;
	}

	public static DefaultMutableTreeNode addChildNode(
			DefaultMutableTreeNode root, UserData userObject) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
				userObject);
		root.add(childNode);
		return childNode;
	}

	public static void reloadTree(JTree tree) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();
		tree.updateUI();
		expandEntireTree(tree);
	}

	public static void expandTreeFromSelected(JTree tree) {
		tree.expandPath(new TreePath(((DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent()).getPath()));
	}

	public static void expandEntireTree(JTree tree) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	public static String getPath(TreeNode[] nodes) {
		String path = "[";
		for (int i = 0; i < nodes.length; i++) {
			if (i == nodes.length - 1) {
				path += nodes[i] + "]";
			} else {
				path += nodes[i].toString() + ", ";
			}
		}
		return path;
	}

	public static String getPath(String parent, String name) {
		parent = parent.replace("]", ", " + name + "]");
		return parent;
	}

	public static String getPath(TreeNode[] nodes, String add) {
		String path = "[";
		for (int i = 0; i < nodes.length; i++) {
			if (i == nodes.length - 1) {
				path += nodes[i] + ", " + add + "]";
			} else {
				path += nodes[i].toString() + ", ";
			}
		}
		return path;
	}

	public static void setNodeUserObjectValue(Object node, Object value) {
		if (!(node instanceof DefaultMutableTreeNode)) {
			return;
		}
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
		Object userObject = treeNode.getUserObject();
		if (!(userObject instanceof UserData)) {
			return;
		}
		UserData data = (UserData) userObject;
		data.setValue(value);
	}

	public static LinkedHashMap<String, Field> getAllFieldsByOrder(
			Class<?> clazz) {
		LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
		if (clazz == null) {
			return fields;
		}
		Field[] arr = clazz.getFields();
		for (Field field : arr) {
			if (field.getType() == clazz
					&& ProtoIDLProxy.isEnumReadable(field.getType())) {
				continue;// 避免枚举类产生的死循环。
			}
			fields.put(field.getName(), field);
		}
		return fields;
	}

	public static LinkedHashMap<String, Field> getAllFieldsByOrder2(
			Class<?> clazz) {
		LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
		if (clazz == null) {
			return fields;
		}

		if (ReflectionUtil.isBaseType(clazz)
				|| ReflectionUtil.isByteArray(clazz)) {
			return fields;
		}

		Field[] arr = clazz.getFields();
		for (Field field : arr) {
			if (field.getType() == clazz
					&& ProtoIDLProxy.isEnumReadable(field.getType())) {
				continue;// 避免枚举类产生的死循环。
			}
			fields.put(field.getName(), field);
		}
		return fields;
	}
}
