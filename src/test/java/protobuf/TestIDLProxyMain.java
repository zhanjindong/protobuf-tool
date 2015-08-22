package protobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.iflytek.ossp.framework.dt.ProtoIDLProxy;
import com.iflytek.ossp.framework.dt.utils.ReflectionUtil;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoSchemaParser;
import com.squareup.protoparser.Type;

public class TestIDLProxyMain {
	public static void main(String[] args) throws IOException,
			DescriptorValidationException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		test6();
	}

	static class Test {
		public Test() {
		}

		public String str;
		public List<String> list;

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}

	}

	// 1、解析proto文件：然用户选择作为请求和响应的message，直接正则匹配其实也可以
	static void test1() throws IOException {
		File file = new File("C://Users/jdzha_000/Desktop/Downres.proto");
		FileInputStream is = new FileInputStream(file);
		ProtoFile protoFile = ProtoSchemaParser.parseUtf8(
				"jprotobuf_autogenerate", is);
		List<Type> types = protoFile.getTypes();
		for (Type type : types) {
			System.out.println(type.getFullyQualifiedName());
		}
	}

	// 2、解析字段：用户选择后，进行其字段的解析
	static void test2() throws Exception {
		File file = new File("d://test//Test.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy.create(is);
		IDLProxyObject object2 = proxies.get("Msg");
		System.out.println(object2.getTarget().getClass());
		Field[] fields = object2.getTarget().getClass().getFields();
		for (Field field : fields) {
			System.out.println(field.getName() + "," + field.getType());
			if (field.getType().getName().equals("java.util.List")) {
				Method[] ms = object2.getTarget().getClass().getMethods();
				Method method = ReflectionUtil.findMethod(object2.getTarget()
						.getClass(),
						"get" + StringUtils.capitalize(field.getName())
								+ "ElementType");
				Class<?> class1 = (Class<?>) method.invoke(object2.getTarget(),
						null);
				System.out.println(class1);
				Field[] fs = class1.getFields();
				for (Field field2 : fs) {
					System.out.println(field2.getName() + ","
							+ field2.getType());
				}
			}
		}
	}

	// 3、设置和读取字段的值
	static void test3() throws IOException, DescriptorValidationException,
			IllegalArgumentException, IllegalAccessException {
		File file = new File("d://test//Test.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy.create(is);
		IDLProxyObject entryObj = proxies.get("Entry");
		entryObj.put("str", "ddaaa");
		List<Object> list1 = new ArrayList<Object>();
		list1.add("1dad");
		list1.add("123");
		entryObj.put("key", list1);
		byte[] bb = entryObj.encode();
		IDLProxyObject result = entryObj.decode(bb);
		System.out.println("str=" + result.get("str"));
		System.out.println("key=" + result.get("key"));

		IDLProxyObject msgObj = proxies.get("Msg");
		List<Object> list2 = new ArrayList<Object>();
		msgObj.put("ival", new Integer(12313));
		msgObj.put("sval", "sasas");
		list2.add(entryObj.getTarget());
		list2.add(entryObj.getTarget());
		msgObj.put("entry", list2);
		// 枚举应该可以选择
		msgObj.put("action", "DIALING");
		bb = msgObj.encode();
		result = msgObj.decode(bb);

		// printFields(result.getTarget());

		System.out.println("ival=" + result.get("ival"));
		System.out.println("sval=" + result.get("sval"));
		System.out.println("entry=" + result.get("entry"));
		System.out.println("action=" + result.get("action"));
	}

	// 嵌套内部类测试
	static void test4() throws IOException, IllegalArgumentException,
			IllegalAccessException {
		File file = new File("d://test//Base.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy
				.create(is, false);
		IDLProxyObject request = proxies.get("Request");
		IDLProxyObject entry = proxies.get("Entry");
		entry.put("key", "outer key");
		entry.put("value", new byte[] {});

		List<Object> entryList = new ArrayList<Object>();
		IDLProxyObject inner = proxies.get("Entry");
		inner = inner.newInstnace();// 一定要重新new一个
		inner.put("key", "inner key");
		inner.put("value", new byte[] {});
		entryList.add(inner.getTarget());

		entry.put("extinfo", entryList);// 这里entry跟inner是一个引用，否则会造成死循环。

		List<Object> reqList = new ArrayList<Object>();
		reqList.add(entry.getTarget());
		reqList.add(entry.getTarget());

		request.put("entry", reqList);

		byte[] bytes = request.encode();
		System.out.println(bytes.length);
		IDLProxyObject result = request.decode(bytes);
		System.out.println(result.get("entry"));
		Object object = ((List<?>) result.get("entry")).get(0);
		Field[] fields = object.getClass().getFields();
		for (Field field : fields) {
			System.out.println(field.getName() + ":" + field.get(object));
			if (field.getType().getName().equals("java.util.List")) {
				List<?> list = ((List<?>) field.get(object));
				if (list == null || list.size() == 0) {
					continue;
				}
				Object obj = ((List<?>) field.get(object)).get(0);
				Field[] fs = obj.getClass().getFields();
				for (Field field2 : fs) {
					System.out
							.println(field2.getName() + ":" + field2.get(obj));
				}
			}
		}

		// 同过protoc编译生成的Java类可以正确的解析。
		// BaseProtos.Request req = BaseProtos.Request.parseFrom(bytes);
		// System.out.println(req.getEntryList().get(0).getExtinfoList().get(0).getKey());
	}
	
	//枚举值设置问题
	static void test5() throws IOException{
		File file = new File("d://test//Base.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy
				.create(is, false);
		IDLProxyObject entry = proxies.get("Entry");
		entry.put("action", "NO_ACTION");//枚举暂不支持repeated
	}
	
	static void test6() throws IOException{
		Map<String, IDLProxyObject> map = ProtoIDLProxy.create(new File("C://Users/jdzha_000/Desktop/Downres.proto"));
		for (Entry<String, IDLProxyObject> entry : map.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

	static void printFields(Object obj) {
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			System.out.println(field.getName() + "," + field.getType());
		}
	}

}
