package org.soap.ws.client.utils;

public final class SoapMessageUtils {

	private final String heading;
	private final StringBuilder address;
	private final StringBuilder encoding;
	private final StringBuilder httpMethod;
	private final StringBuilder header;
	private final StringBuilder message;
	private final StringBuilder messageLength;
	private final StringBuilder messageLoad;
	private final StringBuilder responseCode;
	private final StringBuilder contentType;

	public SoapMessageUtils(String h) {
		heading = h;

		address = new StringBuilder();
		encoding = new StringBuilder();
		httpMethod = new StringBuilder();
		header = new StringBuilder();
		message = new StringBuilder();
		messageLoad = new StringBuilder();
		responseCode = new StringBuilder();
		messageLength = new StringBuilder();
		contentType = new StringBuilder();
	}

	public StringBuilder getAddress() {
		return address;
	}

	public StringBuilder getEncoding() {
		return encoding;
	}

	public StringBuilder getHeader() {
		return header;
	}

	public StringBuilder getHttpMethod() {
		return httpMethod;
	}

	public StringBuilder getMessage() {
		return message;
	}

	public StringBuilder getMessageLength() {
		return messageLength;
	}

	public StringBuilder getMessageLoad() {
		return messageLoad;
	}

	public StringBuilder getResponseCode() {
		return responseCode;
	}

	public StringBuilder getContentType() {
		return contentType;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(heading);
		if (address.length() > 0) {
			buffer.append("\nAddress: ");
			buffer.append(address);
		}
		if (responseCode.length() > 0) {
			buffer.append("\nResponse-Code: ");
			buffer.append(responseCode);
		}
		if (encoding.length() > 0) {
			buffer.append("\nEncoding: ");
			buffer.append(encoding);
		}
		if (contentType.length() > 0) {
			buffer.append("\nContentType: ");
			buffer.append(contentType);
		}
		if (httpMethod.length() > 0) {
			buffer.append("\nHttp-Method: ");
			buffer.append(httpMethod);
		}
		buffer.append("\nHeaders: ");
		buffer.append(header);
		if (message.length() > 0) {
			buffer.append("\nMessages: ");
			buffer.append(message);
		}
		if (messageLength.length() > 0) {
			buffer.append("\nMessageLength: ");
			buffer.append(messageLength);
		}
		if (messageLoad.length() > 0) {
			buffer.append("\nMessageLoad:");
			buffer.append(messageLoad);
		}
		buffer.append("\n--------------------------------------");
		return buffer.toString();
	}

}