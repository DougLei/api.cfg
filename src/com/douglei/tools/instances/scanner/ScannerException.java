package com.douglei.tools.instances.scanner;

/**
 * 
 * @author DougLei
 */
public class ScannerException extends RuntimeException{
	private static final long serialVersionUID = 6610697534667561161L;

	public ScannerException(String message) {
		super(message);
	}

	public ScannerException(String message, Throwable cause) {
		super(message, cause);
	}
}
