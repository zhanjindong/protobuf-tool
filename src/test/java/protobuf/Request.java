package protobuf;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Request {
	
	public  Request() {
	}
	@Protobuf(fieldType = FieldType.OBJECT, order = 3, required = false)
	public List<Entry> entryList;
	
}