package com.android.volley.toolbox;

import com.android.volley.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonObjectRequest<T> extends GsonRequest<T> {
	private final Gson gson = new Gson();

	private final Class<T> clazz;

	public GsonObjectRequest(String url, Class<T> clazz, String requestBody,
		Response.Listener<T> listener, Response.ErrorListener errorListener) {
		this(Method.GET, url, clazz, requestBody, listener, errorListener);
	}

	public GsonObjectRequest(int method, String url, Class<T> clazz, String requestBody,
		Response.Listener<T> listener, Response.ErrorListener errorListener) {
		super(method, url, requestBody, listener, errorListener);
		this.clazz = clazz;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(
				response.data,
				HttpHeaderParser.parseCharset(response.headers));
			return Response.success(
				gson.fromJson(json, clazz),
				HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}