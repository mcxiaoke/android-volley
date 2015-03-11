package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class GsonArrayRequest<T> extends GsonRequest<T> {
	private final Gson gson = new Gson();

	private final Type type;

	public GsonArrayRequest(String url, Type type, String requestBody,
		Response.Listener<T> listener, Response.ErrorListener errorListener) {
		this(Method.GET, url, type, requestBody, listener, errorListener);
	}

	public GsonArrayRequest(int method, String url, Type type, String requestBody,
		Response.Listener<T> listener, Response.ErrorListener errorListener) {
		super(method, url, requestBody, listener, errorListener);
		this.type = type;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(
				response.data,
				HttpHeaderParser.parseCharset(response.headers));
			return Response.success(
				(T) gson.fromJson(json, type),
				HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}