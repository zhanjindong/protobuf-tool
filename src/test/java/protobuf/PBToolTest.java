package protobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;
import com.iflytek.ossp.framework.dt.PBUtils;
import com.iflytek.ossp.framework.dt.ProtoIDLProxy;
import com.iflytek.ossp.framework.dt.utils.IOUtils;
import com.iflytek.ossp.framework.dt.utils.ReflectionUtil;
import com.iflytek.ossp.framework.dt.utils.RegexUtils;

public class PBToolTest {
	public static void main(String[] args) throws IOException,
			IllegalArgumentException, IllegalAccessException {
		System.out.println(RegexUtils.isURL("http://127.0.0.1：8080/ossp-bliserver-service/do?c=1017&v=3.0"));
	}

	// 验证工具生成的数据
	static void test1() throws IOException, IllegalArgumentException,
			IllegalAccessException {
		File file = new File("C://Users/jdzha_000/Documents/Base.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy
				.create(is, false);

		byte[] data = IOUtils.read("D://pbtool.data");

		IDLProxyObject respProxy = proxies.get("Response");
		IDLProxyObject result = respProxy.decode(data);
		System.out.println(result.get("entry"));

		System.out.println(((List<?>) result.get("entry")).size());
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
	}

	// 递归的遍历字段
	static void test2() throws IOException {
		File file = new File("C://Users/jdzha_000/Documents/Base.proto");
		FileInputStream is = new FileInputStream(file);
		Map<String, IDLProxyObject> proxies = ProtobufIDLProxy
				.create(is, false);

		byte[] data = IOUtils.read("D://pbtool.data");

		IDLProxyObject respProxy = proxies.get("Response");
		IDLProxyObject result = respProxy.decode(data);

	}

	static String tab = "—";

	private void walkClass(String fieldName, Class<?> fieldType,
			Object object) {
		if (ReflectionUtil.isCollection(fieldType)) {
			Class<?> elementType = PBUtils.getElementType(fieldName, fieldType,
					object.getClass());
			walkClass(fieldName, elementType, object.getClass());
		} else if (ReflectionUtil.isBaseType(fieldType)
				|| ReflectionUtil.isByteArray(fieldType)
				|| ProtoIDLProxy.isEnumReadable(fieldType)) {
			tab += tab;
			System.out.println(tab + fieldName);
		} else {
			LinkedHashMap<String, Field> fields = getFields(fieldType);
			for (Entry<String, Field> entry : fields.entrySet()) {
				String tmpName = entry.getKey();
				Class<?> tmpType = entry.getValue().getType();
				walkClass(tmpName, tmpType, fieldType);
			}
		}
	}
	
	private static LinkedHashMap<String, Field> getFields(Class<?> type) {
		return PBUtils.getAllFieldsByOrder(type);
	}
}
