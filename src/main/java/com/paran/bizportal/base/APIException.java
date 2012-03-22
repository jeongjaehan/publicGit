package com.paran.bizportal.base;

/**
 * API �ͼ��� Ŭ����
 * 
 */
@SuppressWarnings("serial")
public class APIException extends Exception
{
	private int code = 0;
	
	/**
	 * Ŭ���� ������ 
	 * 
	 * @param  code ��� �ڵ� 
	 */
	public APIException(int code)
	{
		super("ERRORCODE:" + code);
		this.code = code;
	}
	
	/**
	 * Ŭ���� ������
	 * 
	 * @param  code ��� �ڵ�
	 * @param  msg ��� �޽���
	 */
	public APIException(int code, String msg)
	{
		super("ERRORCODE:" + code + " [" + msg + "]");
		this.code = code;
	}
	
	/**
	 * Exception ��� �ڵ带 �����Ѵ�.
	 * 
	 * @return ��� �ڵ�
	 */
	public int getCode()
	{
		return code;
	}
}
