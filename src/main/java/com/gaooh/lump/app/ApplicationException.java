package com.gaooh.lump.app;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode = "";
	
	public ApplicationException(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
