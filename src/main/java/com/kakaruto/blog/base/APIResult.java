package com.kakaruto.blog.base;

/**
 * API 결과 코드 정의 인터페이스
 * 
 * @author 조홍진
 *
 */
public interface APIResult 
{
	/**
	 * 성공
	 */
	public static final int SUCCESS = 0;
	
	/**
	 * 파라미터 유효성 체크 에러
	 */
	public static final int PARAMETER_VALIDATION_CHECK_FAIL = -8001;
	
	/**
	 * 파란 쿠키 이상 (존재하지 않음, 포맷이상, CS 체크 에러 포함)
	 */
	public static final int PARAN_COOKIE_NOT_FOUND = -8012;
	
	/**
	 * 알 수 없는 에러
	 */
	public static final int UNKNOWN_FAIL = -8080;
}
