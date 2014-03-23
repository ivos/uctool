package net.sf.uctool.exception;

public class ReaderException extends RuntimeException {

	public ReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReaderException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
