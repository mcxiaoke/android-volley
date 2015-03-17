package com.android.volley.toolbox;

import com.android.volley.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class GsonRequest<T> extends Request<T> {
	private static final String PROTOCOL_CHARSET = "utf-8";

	private static final String PROTOCOL_CONTENT_TYPE =
		String.format("application/json; charset=%s", PROTOCOL_CHARSET);

	private final Response.Listener<T> mListener;
	private final String mRequestBody;

	public GsonRequest(String url, String requestBody, Response.Listener<T> listener,
		Response.ErrorListener errorListener) {
		this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
	}

	public GsonRequest(int method, String url, String requestBody, Response.Listener<T> listener,
		Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mRequestBody = requestBody;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	@Override
	public String getPostBodyContentType() {
		return getBodyContentType();
	}

	@Override
	public byte[] getPostBody() {
		return getBody();
	}

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody() {
		try {
			return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
				mRequestBody, PROTOCOL_CHARSET);
			return null;
		}
	}

}