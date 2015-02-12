package com.android.volley.toolbox;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

abstract public class MultipartRequest<T> extends Request<T> {

	private static final String PROTOCOL_CHARSET = "utf-8";

	/**
	 * A representation of a MultiPart parameter
	 */
	public static final class MultipartParam {

		public String contentType;
		public String value;

		/**
		 * Initialize a multipart request param with the value and content type
		 *
		 * @param contentType The content type of the param
		 * @param value The value of the param
		 */
		public MultipartParam(String contentType, String value) {
			this.contentType = contentType;
			this.value = value;
		}

	}

	private Response.Listener<T> mListener;
	private Map<String, MultipartParam> mMultipartParams = null;
	private Map<String, String> mFileUploads = null;
	private boolean isFixedStreamingMode;

	/**
	 * Creates a new request with the given method.
	 *
	 * @param method the request {@link Method} to use
	 * @param url URL to fetch the string at
	 * @param listener Listener to receive the String response
	 * @param errorListener Error listener, or null to ignore errors
	 */
	public MultipartRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mMultipartParams = new HashMap<String, MultipartRequest.MultipartParam>();
		mFileUploads = new HashMap<String, String>();
	}

	/**
	 * Add a parameter to be sent in the multipart request
	 *
	 * @param name The name of the paramter
	 * @param contentType The content type of the paramter
	 * @param value the value of the paramter
	 * @return The Multipart request for chaining calls
	 */
	public MultipartRequest<T> addMultipartParam(String name, String contentType, String value) {
		mMultipartParams.put(name, new MultipartParam(contentType, value));
		return this;
	}

	/**
	 * Add a file to be uploaded in the multipart request
	 *
	 * @param name The name of the file key
	 * @param filePath The path to the file. This file MUST exist.
	 * @return The Multipart request for chaining method calls
	 */
	public MultipartRequest<T> addFile(String name, String filePath) {
		mFileUploads.put(name, filePath);
		return this;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	/**
	 * Get all the multipart params for this request
	 *
	 * @return A map of all the multipart params NOT including the file uploads
	 */
	public Map<String, MultipartParam> getMultipartParams() {
		return mMultipartParams;
	}

	/**
	 * Get all the files to be uploaded for this request
	 *
	 * @return A map of all the files to be uploaded for this request
	 */
	public Map<String, String> getFilesToUpload() {
		return mFileUploads;
	}

	public boolean isFixedStreamingMode() {
		return isFixedStreamingMode;
	}

	public void setFixedStreamingMode(boolean isFixedStreamingMode) {
		this.isFixedStreamingMode = isFixedStreamingMode;
	}

	/**
	 * Get the protocol charset
	 */
	public String getProtocolCharset() {
		return PROTOCOL_CHARSET;
	}

}
