package io.jindong.pbtool;

import io.jindong.pbtool.utils.IOUtils;
import io.jindong.pbtool.utils.SeparatorUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;

public class ProtoIDLProxy {

	private static final char[] IMPORT = new char[] { 'i', 'm', 'p', 'o', 'r',
			't' };
	private static final char SEMICOLON = ';';
	private static final char QUOTE = '\"';

	public static Map<String, IDLProxyObject> create(File file)
			throws IOException {
		String protoContent = linkProtoFile(file);
		return create(protoContent);
	}
	
	public static Map<String, IDLProxyObject> create(String protoContent)
			throws IOException {
		return ProtobufIDLProxy.create(protoContent, false);
	}

	public static String linkProtoFile(File file) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		FileInputStream is = null;
		InputStreamReader ir = null;
		try {
			is = new FileInputStream(file);
			ir = new InputStreamReader(is, "UTF-8");
			int c;
			int f = 0;
			while ((c = ir.read()) != -1) {
				sb1.append((char) c);
				if (c == IMPORT[f])
					f++;
				else
					f = 0;

				StringBuilder im = new StringBuilder();
				if (f == 6) {
					while ((c = ir.read()) != SEMICOLON) {
						sb1.append((char) c);
						if (c == ' ' || c == QUOTE)
							continue;
						im.append((char) c);
					}
					sb1.append((char) c);
					// handle import
					String tmp = file.getParent()
							+ SeparatorUtils.getFileSeparator() + im.toString();
					sb2.append(IOUtils.read(tmp, "UTF-8"));
					f = 0;
				}
			}

			sb1.append(sb2);
		} catch (Exception e) {
			IOUtils.close(ir);
			IOUtils.close(is);
		}
		return sb1.toString();
	}
	
	public static boolean isEnumReadable(Class<?> type){
		Class<?>[] clazzs = type.getInterfaces();
		for (Class<?> cl : clazzs) {
			if (cl.getSimpleName().equals("EnumReadable")) {
				return true;
			}
		}
		return false;
	}
}
