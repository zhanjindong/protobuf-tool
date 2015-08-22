package protobuf;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Entry {
	
	public Entry(){}
	@Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
	public String key;
	@Protobuf(fieldType = FieldType.BYTES, order = 2, required = false)
	public byte[] value;
	@Protobuf(fieldType = FieldType.OBJECT, order = 3, required = false)
	public List<Entry> extinfo;
}