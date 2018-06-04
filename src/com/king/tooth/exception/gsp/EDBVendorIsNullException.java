package com.king.tooth.exception.gsp;

/**
 * gsp 数据库方言为kong的异常
 * @author DougLei
 */
@SuppressWarnings("serial")
public class EDBVendorIsNullException extends Exception{
	public EDBVendorIsNullException() {
	}
	public EDBVendorIsNullException(String errorMsg) {
		super(errorMsg);
	}
}
