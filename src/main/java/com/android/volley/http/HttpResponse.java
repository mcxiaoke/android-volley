package com.android.volley.http;

import java.util.Map;
import java.util.TreeMap;

public class HttpResponse {
	public static final int SC_MOVED_PERMANENTLY = 301;
	public static final int SC_MOVED_TEMPORARILY = 302;
	public static final int SC_NOT_MODIFIED = 304;
	public static final int SC_FORBIDDEN = 403;
	public static final int SC_UNAUTHORIZED = 401;
	public static final int SC_OK = 200;

	private int responseCode;
	private String responseMessage;
	private HttpEntity entityFromConnection;
	private Map<String, String> httpHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public HttpResponse(int responseCode, String responseMessage) {
		this.setResponseCode(responseCode);
		this.setResponseMessage(responseMessage);
	}

	public void setEntity(HttpEntity entityFromConnection) {
		this.entityFromConnection = entityFromConnection;
	}

	public void addHeader(String key, String value) {
		httpHeaders.put(key, value);
	}

	public Map<String, String> getAllHeaders() {
		return httpHeaders;
	}

	public HttpEntity getEntity() {
		return entityFromConnection;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
