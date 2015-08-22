package com.iflytek.ossp.framework.dt;

import java.io.Serializable;
import java.util.Map;

public class SavedObject implements Serializable {
	private static final long serialVersionUID = 5754891160050602318L;
	private String reqUrl;
	private Map<String, String> headers;
	private byte[] reqData;
	private String reqProto;
	private String reqMsg;

	private byte[] respData;
	private String respProto;
	private String respMsg;

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public byte[] getReqData() {
		return reqData;
	}

	public void setReqData(byte[] reqData) {
		this.reqData = reqData;
	}

	public String getReqProto() {
		return reqProto;
	}

	public void setReqProto(String reqProto) {
		this.reqProto = reqProto;
	}

	public String getReqMsg() {
		return reqMsg;
	}

	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}

	public byte[] getRespData() {
		return respData;
	}

	public void setRespData(byte[] respData) {
		this.respData = respData;
	}

	public String getRespProto() {
		return respProto;
	}

	public void setRespProto(String respProto) {
		this.respProto = respProto;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

}

