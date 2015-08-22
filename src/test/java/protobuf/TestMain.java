package protobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;
import com.iflytek.ossp.framework.dt.ProtoIDLProxy;

public class TestMain {
	static void test1() throws IOException {
		// 通过 .proto描述文件生成动态解析对象
		// String protoCotent = "package mypackage.test; "
		// +
		// "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";"
		// + "option java_outer_classname = \"StringTypeClass\";  "
		// + "message StringMessage { "
		// +
		// "  required string message = 1; optional Entry entry = 2; message Entry{required string value = 1;}}";

		String protoCotent = FileUtils.readFileToString(new File(
				"d://test.proto"));

		File file = new File("d://test//GetCardContent.proto");
		Map<String, IDLProxyObject> proxies = ProtoIDLProxy.create(file);
		for (Entry<String, IDLProxyObject> p : proxies.entrySet()) {
			System.out.println(p.getKey() + "," + p.getValue());

			Field[] fields = p.getValue().getTarget().getClass().getFields();
			for (Field field : fields) {
				System.out.println(field.getName() + "," + field.getType());
			}
		}

		// IDLProxyObject object = ProtobufIDLProxy.createSingle(protoCotent);
		// // if .proto IDL defines multiple messages use as follow
		// // Map<String, IDLProxyObject> objects =
		// // ProtobufIDLProxy.create(protoCotent);
		// // 动态设置字段值
		// object.put("message", "hello你好");
		// Field[] fields = object.getTarget().getClass().getFields();
		// for (Field field : fields) {
		// System.out.println("field:"+field.getName()+" "+field.getType());
		// }
		// object.put("entry.value", "entry value");
		// // propogation object set
		// // object.put("sub.field", "hello world");
		// // protobuf 序列化
		// byte[] bb = object.encode();
		//
		// // protobuf 反序列化
		// IDLProxyObject result = object.decode(bb);
		// System.out.println(result.get("message"));
		// System.out.println(result.get("entry.value"));
		// // propogation object get
		// // result.get("sub.field")
		//
		// ProtoFile protoFile =
		// ProtoSchemaParser.parse("jprotobuf_autogenerate", protoCotent);
		// List<Service> sers = protoFile.getServices();
		// for (Service service : sers) {
		// System.out.println(service.getName());
		// }
		//
	}

	static void test2() throws IOException {
		File file = new File("d://test//Test.proto");
		Map<String, IDLProxyObject> proxies = ProtoIDLProxy.create(file);
		IDLProxyObject object = proxies.get("Entry");
		object.put("key", "1");
		object.put("key", "2");
		byte[] bb = object.encode();
		IDLProxyObject result = object.decode(bb);
		System.out.println(result.get("key"));
		System.out.println(result.get("key"));

		Field[] fields = object.getTarget().getClass().getFields();
		for (Field field : fields) {
			System.out.println(field.getName() + "," + field.getType());
		}
	}

	public static void main(String[] args) throws Throwable {
//		File file = new File("d://test/Base.proto");
//		Map<String, IDLProxyObject> objs = ProtoIDLProxy.create(file,true);
//		for (Entry<String, IDLProxyObject> entry : objs.entrySet()) {
//			System.out.println(entry.getKey());
//		}

		Map<String, IDLProxyObject> objs = ProtobufIDLProxy.create(new FileInputStream(
				"d://test/Base.proto"),true);
		for (Entry<String, IDLProxyObject> entry : objs.entrySet()) {
			System.out.println(entry.getKey());
		}
		IDLProxyObject obj = objs.get("Entry");
		Field[] fields = obj.getTarget().getClass().getFields();
		for (Field f : fields) {
			System.out.println(f.getName()+","+f.getType());
		}
//		String source = IOUtils.read("D://source.java", "utf-8");
//		String name = "com.iflytek.ossp.framework.performance.servlet.pb.EntryJProtoBufProtoClass";
//		JdkCompiler compiler = new JdkCompiler(Thread.currentThread().getContextClassLoader());
//		Class<?> clazz = compiler.doCompile(name, source, null);
//		
//		Field[] fields = clazz.getFields();
//		System.out.println(clazz);
//		for (Field f : fields) {
//			System.out.println(f.getName()+","+f.getType());
//		}
		
		
	}
}
