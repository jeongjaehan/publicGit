package com.paran.bizportal.base;

/**
 * API 익셉션 클래스
 * 
 */
@SuppressWarnings("serial")
public class APIException extends Exception
{
	private int code = 0;
	
	/**
	 * 클래스 생성자 
	 * 
	 * @param  code 결과 코드 
	 */
	public APIException(int code)
	{
		super("ERRORCODE:" + code);
		this.code = code;
	}
	
	/**
	 * 클래스 생성자
	 * 
	 * @param  code 결과 코드
	 * @param  msg 결과 메시지
	 */
	public APIException(int code, String msg)
	{
		super("ERRORCODE:" + code + " [" + msg + "]");
		this.code = code;
	}
	
	/**
	 * Exception 결과 코드를 리턴한다.
	 * 
	 * @return 결과 코드
	 */
	public int getCode()
	{
		return code;
	}
}
