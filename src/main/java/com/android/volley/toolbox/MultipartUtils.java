package com.android.volley.toolbox;

import com.android.volley.Request;
import org.apache.http.util.EncodingUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;

public class MultipartUtils {

	public static final String CRLF = "\r\n";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; charset=%s; boundary=%s";
	public static final String BINARY = "binary";
	public static final String EIGHT_BIT = "8bit";
	public static final String FORM_DATA = "form-data; name=\"%s\"";
	public static final String BOUNDARY_PREFIX = "--";
	public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
	public static final String FILENAME = "filename=\"%s\"";
	public static final String COLON_SPACE = ": ";
	public static final String SEMICOLON_SPACE = "; ";

	public static final int CRLF_LENGTH = CRLF.getBytes().length;
	public static final int HEADER_CONTENT_DISPOSITION_LENGTH = HEADER_CONTENT_DISPOSITION.getBytes().length;
	public static final int COLON_SPACE_LENGTH = COLON_SPACE.getBytes().length;
	public static final int HEADER_CONTENT_TYPE_LENGTH = HEADER_CONTENT_TYPE.getBytes().length;
	public static final int CONTENT_TYPE_OCTET_STREAM_LENGTH = CONTENT_TYPE_OCTET_STREAM.getBytes().length;
	public static final int HEADER_CONTENT_TRANSFER_ENCODING_LENGTH = HEADER_CONTENT_TRANSFER_ENCODING.getBytes().length;
	public static final int BINARY_LENGTH = BINARY.getBytes().length;
	public static final int BOUNDARY_PREFIX_LENGTH = BOUNDARY_PREFIX.getBytes().length;

	public static final byte[] CRLF_BYTES = EncodingUtils.getAsciiBytes(CRLF);

	/**
	 * Perform a multipart request on a connection
	 *
	 * @param connection
	 *            The Connection to perform the multi part request
	 * @param request
	 *            The params to add to the Multi Part request
	 *            The files to upload
	 * @throws ProtocolException
	 */
	static void setConnectionParametersForMultipartRequest(HttpURLConnection connection, Request<?> request) throws IOException,
		ProtocolException {
		final String charset = ((MultipartRequest<?>) request).getProtocolCharset();
		final int curTime = (int) (System.currentTimeMillis() / 1000);
		final String boundary = BOUNDARY_PREFIX + curTime;

		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty(HEADER_CONTENT_TYPE, String.format(CONTENT_TYPE_MULTIPART, charset, curTime));

		Map<String, MultipartRequest.MultipartParam> multipartParams = ((MultipartRequest<?>) request).getMultipartParams();
		Map<String, String> filesToUpload = ((MultipartRequest<?>) request).getFilesToUpload();

		if (((MultipartRequest<?>) request).isFixedStreamingMode()) {
			int contentLength = getContentLengthForMultipartRequest(boundary, multipartParams, filesToUpload);

			connection.setFixedLengthStreamingMode(contentLength);
		} else {
			connection.setChunkedStreamingMode(0);
		}

		PrintWriter writer = null;
		try {
			OutputStream out = connection.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(out, charset), true);

			for (String key : multipartParams.keySet()) {
				MultipartRequest.MultipartParam param = multipartParams.get(key);

				writer.append(boundary).append(CRLF).append(String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA, key)).append(CRLF)
					.append(HEADER_CONTENT_TYPE + COLON_SPACE + param.contentType).append(CRLF).append(CRLF).append(param.value).append(CRLF)
					.flush();
			}

			for (String key : filesToUpload.keySet()) {
				File file = new File(filesToUpload.get(key));

				if (!file.exists()) {
					throw new IOException(String.format("File not found: %s", file.getAbsolutePath()));
				}

				if (file.isDirectory()) {
					throw new IOException(String.format("File is a directory: %s", file.getAbsolutePath()));
				}

				writer.append(boundary)
					.append(CRLF)
					.append(String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA + SEMICOLON_SPACE + FILENAME, key, file.getName()))
					.append(CRLF).append(HEADER_CONTENT_TYPE + COLON_SPACE + CONTENT_TYPE_OCTET_STREAM).append(CRLF)
					.append(HEADER_CONTENT_TRANSFER_ENCODING + COLON_SPACE + BINARY).append(CRLF).append(CRLF).flush();

				BufferedInputStream input = null;
				try {
					FileInputStream fis = new FileInputStream(file);
					int transferredBytes = 0;
					int totalSize = (int) file.length();
					input = new BufferedInputStream(fis);
					int bufferLength = 0;

					byte[] buffer = new byte[1024];
					while ((bufferLength = input.read(buffer)) > 0) {
						out.write(buffer, 0, bufferLength);
						transferredBytes += bufferLength;
					}
					out.flush(); // Important! Output cannot be closed. Close of
					// writer will close
					// output as well.
				} finally {
					if (input != null)
						try {
							input.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
				}
				writer.append(CRLF).flush(); // CRLF is important! It indicates
				// end of binary
				// boundary.
			}

			// End of multipart/form-data.
			writer.append(boundary + BOUNDARY_PREFIX).append(CRLF).flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	static int getContentLengthForMultipartRequest(String boundary, Map<String, MultipartRequest.MultipartParam> multipartParams, Map<String, String> filesToUpload) {
		final int boundaryLength = boundary.getBytes().length;
		int contentLength = 0;
		for (String key : multipartParams.keySet()) {
			MultipartRequest.MultipartParam param = multipartParams.get(key);
			int size = boundaryLength +
				CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA, key).getBytes().length +
				CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + param.contentType.getBytes().length +
				CRLF_LENGTH + CRLF_LENGTH + param.value.getBytes().length + CRLF_LENGTH;

			contentLength += size;
		}

		for (String key : filesToUpload.keySet()) {
			File file = new File(filesToUpload.get(key));
			int size = boundaryLength +
				CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA + SEMICOLON_SPACE + FILENAME, key, file.getName()).getBytes().length +
				CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + CONTENT_TYPE_OCTET_STREAM_LENGTH +
				CRLF_LENGTH + HEADER_CONTENT_TRANSFER_ENCODING_LENGTH + COLON_SPACE_LENGTH + BINARY_LENGTH + CRLF_LENGTH + CRLF_LENGTH;

			size += (int) file.length();
			size += CRLF_LENGTH;
			contentLength += size;
		}

		int size = boundaryLength + BOUNDARY_PREFIX_LENGTH + CRLF_LENGTH;
		contentLength += size;

		return contentLength;
	}

}
