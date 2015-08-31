package protobuf;

import io.jindong.pbtool.SavedObject;

import org.apache.commons.lang.SerializationUtils;

public class TestSerialized {
	public static void main(String[] args) {
		SavedObject saved = new SavedObject();
		saved.setReqUrl("sssssssssss");
		String test = "teste测试";
		saved.setReqData(test.getBytes());
		byte[] data = SerializationUtils.serialize(saved);
		
		SavedObject newObj = (SavedObject)SerializationUtils.deserialize(data);
		System.out.println(newObj.getReqUrl());
		byte[] data2 = newObj.getReqData();
		System.out.println(new String(data2));
	}
}
