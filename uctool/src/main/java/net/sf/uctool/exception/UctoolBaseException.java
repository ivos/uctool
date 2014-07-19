package net.sf.uctool.exception;

public class UctoolBaseException extends RuntimeException {

	public UctoolBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public UctoolBaseException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
