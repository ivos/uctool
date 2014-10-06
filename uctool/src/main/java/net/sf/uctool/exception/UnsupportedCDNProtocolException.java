package net.sf.uctool.exception;

public class UnsupportedCDNProtocolException extends UctoolBaseException {

	public UnsupportedCDNProtocolException(String protocol) {
		super("This protocol is not supported: " + protocol
				+ ". Use only http, https or empty value.");
	}

	private static final long serialVersionUID = 1L;

}
