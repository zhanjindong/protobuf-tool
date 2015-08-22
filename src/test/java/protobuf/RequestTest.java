package protobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


public class RequestTest {
	public static void main(String[] args) throws IOException {
		Request req = new Request();
		req.entryList= new ArrayList<Entry>();
		Entry entry = new Entry();
		entry.key="key";
		entry.value=new byte[]{};
		entry.extinfo = new ArrayList<Entry>();
		
		Entry entry2 = new Entry();
		entry2.key="inner";
		entry2.value=new byte[]{};
		entry.extinfo.add(entry2);
		
		req.entryList.add(entry);
		
		
		Codec<Request> proxy = ProtobufProxy.create(Request.class);
		byte[] bytes = proxy.encode(req);
		
		System.out.println(bytes.length);
		
		Request rst = proxy.decode(bytes);
		System.out.println(rst.entryList.get(0).key);
		System.out.println(rst.entryList.get(0).extinfo.get(0).key);
	}
}
