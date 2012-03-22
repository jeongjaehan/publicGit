package com.kakaruto.blog.base;

/**
 * API ��� �ڵ� ���� �������̽�
 * 
 * @author ��ȫ��
 *
 */
public interface APIResult 
{
	/**
	 * ����
	 */
	public static final int SUCCESS = 0;
	
	/**
	 * �Ķ���� ��ȿ�� üũ ����
	 */
	public static final int PARAMETER_VALIDATION_CHECK_FAIL = -8001;
	
	/**
	 * �Ķ� ��Ű �̻� (�������� ����, �����̻�, CS üũ ���� ����)
	 */
	public static final int PARAN_COOKIE_NOT_FOUND = -8012;
	
	/**
	 * �� �� ���� ����
	 */
	public static final int UNKNOWN_FAIL = -8080;
}
