package net.sf.uctool.exception;

public class WriterException extends RuntimeException {

	public WriterException(String message, Throwable cause) {
		super(message, cause);
	}

	public WriterException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
