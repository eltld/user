package net.ememed.user2.exception;

public class MyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4650796247895973405L;

	public MyException() {
		super();
	}

	public MyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MyException(String detailMessage) {
		super(detailMessage);
	}

	public MyException(Throwable throwable) {
		super(throwable);
	}
	
}
