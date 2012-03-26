package com.jaehan.portal.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.jaehan.portal.common.CharsetName;

/**
 * API 리퀘스트 처리를 위한 기본 핸들러 클래스. 쿠키 디코딩 처리, 클라이언트 아이피 분석(헤더분석), API 결과 생성처리.
 * 
 */
@Component
public class APIBaseHandler 
{
	/**
	 * 로거
	 */
	
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Srping DI messageSourceAccessor
	 */
	@Autowired
	private MessageSourceAccessor messageSourceAccessor = null;
	
	/**
	 * API 결과 데이터의 디폴트 인코딩 캐릭터셋 
	 */
	private static final String DEFAULT_CHARSET = CharsetName.UTF_8;

	/**
	 * Request Attribute 키 : 파란 MC 쿠키
	 */
	private static final String MC_KEY = "paran.cookie.mc";
	
	/**
	 * Request Attribute 키 : 클라이언트 아이피 
	 */
	private static final String REMOTE_ADDR_KEY = "paran.remoteAddr";
	
	/**
	 * Request Attribute 키 : API 처리 시간
	 */
	private static final String API_TIME_KEY = "apiTime";
	
	/**
	 * Request Attribute 키 : API 요청 키 
	 */
	private static final String API_REQID_KEY = "apiReqID";
	
	/**
	 * json 타입 API의 필드 이름 : 리턴 결과 코드 
	 */
	private static final String API_DEF_RESULT = "result";
	
	/**
	 * json 타입 API의 필드 이름 : 리턴 결과 타입
	 */
	private static final String API_DEF_RESULT_TYPE = "resulttype";
	
	/**
	 * json 타입 API의 필드 값 : resultOnly
	 */
	private static final String API_DEF_RESULT_ONLY = "resultonly";
	
	/**
	 * json 타입 API의 필드 값 : map
	 */
	private static final String API_DEF_MAP = "map";
	
	/**
	 * json 타입 API의 필드 값 : list
	 */
	private static final String API_DEF_LIST = "list";
	
	/**
	 * API 요청 시간을 체크한다. 
	 * Request Attribte로 설정이 되어있으면 그 값을 그대로 리턴하고
	 * 설정되어 있지 않으면 현재 시간 기준으로 설정한 후 그 값을 리턴한다.
	 * 
	 * @param  request HttpServletRequest
	 * @return 현재 설정된, 또는 지금 설정한 ReqID 
	 */
	public String checkAPIReqID(HttpServletRequest request)
	{
		String apiReqID = (String)request.getAttribute(API_REQID_KEY);
		
		if(StringUtils.isEmpty(apiReqID))
		{
			long l = System.currentTimeMillis();
			request.setAttribute(API_TIME_KEY, l);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			apiReqID = dateFormat.format(new Date(l));
			request.setAttribute(API_REQID_KEY, apiReqID);
		}
		
		return apiReqID;
	}
	
	/**
	 * 현재 까지의 API 처리 시간을 리턴한다.
	 * 
	 * @param  request HttpServletRequest
	 * @return 현재까지의 API 처리 long 시간
	 */
	public long getAPIDurationTime(HttpServletRequest request)
	{
		long l = (Long)request.getAttribute(API_TIME_KEY);
		return System.currentTimeMillis() - l;
	}
	
	/**
	 * 파란 쿠키 설정을 최종 완료처리한다. 
	 * 결과 코드를 삽입하고 Request 의 Attribute 로 설정한다.
	 * 
	 * @param  request HttpServletRequest
	 * @param  resultCode 리턴 결과 코드
	 * @param  mcMap MC 쿠키 맵
	 * @return MC 쿠키 맵
	 */
	private HashMap<String, String> finalizeRequestMCMap(HttpServletRequest request, int resultCode, HashMap<String, String> mcMap)
	{
		if(mcMap == null)
			mcMap = new HashMap<String, String>();
		
		mcMap.put(API_DEF_RESULT, String.valueOf(resultCode));
		request.setAttribute(MC_KEY, mcMap);
		return mcMap;
	}

	
	/**
	 * 파란 MC 쿠키 맵을  체크한다. 
	 * 결과코드가 성공이면 파란 MC 맵을 리턴하고
	 * 아니면 Exception 처리 한다. 
	 * 
	 * @param  request HttpServletRequest
	 * @return MC 쿠키 맵
	 * @throws Exception 결과코드가 성공이 아닌경우 Exception 발생
	 */
	public HashMap<String, String> checkRequestMCMap(HttpServletRequest request)
	throws Exception
	{
		@SuppressWarnings("unchecked")
		HashMap<String, String> mcMap = (HashMap<String, String>)request.getAttribute(MC_KEY);

		if(mcMap == null || mcMap.isEmpty())
			throw new APIException(APIResult.PARAN_COOKIE_NOT_FOUND);
		
		int mcResult = NumberUtils.toInt(mcMap.get(API_DEF_RESULT), APIResult.UNKNOWN_FAIL);
		
		if(mcResult == APIResult.SUCCESS)
			return mcMap;
		else
			throw new APIException(mcResult);
	}

	/**
	 * 파란 MC 쿠키 맵을  리턴한다. 
	 * MC 쿠키맵이 존재하지 않으면 널을 리턴한다. (Exception 발생시키지 않음)
	 * 
	 * @param  request HttpServletRequest
	 * @return MC 쿠키 맵
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRequestMCMap(HttpServletRequest request)
	{
		return (HashMap<String, String>)request.getAttribute(MC_KEY);
	}
	
	/**
	 * 파란 도메인으로 쿠키를 추가하여 리턴한다.
	 * 
	 * @param  response response
	 * @param  name 쿠키 이름
	 * @param  val 쿠키 값
	 * @return 생성된 쿠키
	 */
	public Cookie addParanCookie(HttpServletResponse response, String name, String val)
	{
		Cookie cookie = new Cookie(name, val);

		cookie.setDomain(".paran.com");
		cookie.setPath("/");
		cookie.setMaxAge(-1);

		response.addCookie(cookie);
		
		return cookie;
	}
	
	/**
	 * HTTP 클라이언트의 UserAgent(user-agent) 값을 리턴한다.
	 * 
	 * @param  request HttpServletRequest
	 * @return UserAgent
	 */
	public String getUserAgent(HttpServletRequest request)
	{
		if(request == null)
			return null;
		
		return request.getHeader("user-agent");
	}
	
	/**
	 * json 타입 API 의 결과를 최종 처리한다. 
	 * contentType 을 설정하고 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param resultJSONObject 결과 json 객체
	 * @param charset Response 캐릭터셋
	 */
	private void finalizeWrite(HttpServletResponse response, JSONObject resultJSONObject, String charset) 
	{		
		if(StringUtils.isEmpty(charset))
			charset = DEFAULT_CHARSET;	
	
		try
		{
			response.setContentType("text/html;charset=" + charset);
			response.getWriter().println(resultJSONObject);
			logger.info(resultJSONObject);			
		}
		catch(Exception e)
		{
			logger.warn(e, e);
		}
	}
	
	/**
	 * json 타입 API 의 result Only 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode 결과 코드
	 * @param charset Response 캐릭터셋
	 */
	protected void writeResultOnly(HttpServletResponse response, int resultCode, String charset)	
	{
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element(API_DEF_RESULT, String.valueOf(resultCode));
		resultJSONObject.element(API_DEF_RESULT_TYPE, API_DEF_RESULT_ONLY);
		
		finalizeWrite(response, resultJSONObject, charset);
	}
	
	/**
	 * json 타입 API 의 result Only 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode 결과 코드
	 */
	public void writeResultOnly(HttpServletResponse response, int resultCode) 
	{
		writeResultOnly(response, resultCode, null);
	}
	
	/**
	 * json 타입 API 의 Exception 처리 결과를 처리 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param e Exception
	 */
	public void handleException(HttpServletResponse response, Exception e)
	{
		if(e instanceof APIException)
			writeAPIExceptionResult(response, (APIException)e);
		else if(e instanceof MissingServletRequestParameterException)
		{
			logger.warn(e, e);
			writeAPIExceptionResult(response, new APIException(APIResult.PARAMETER_VALIDATION_CHECK_FAIL));
		}
		else
		{
			logger.warn(e, e);
			writeResultOnly(response, APIResult.UNKNOWN_FAIL);
		}
	}
	
	/**
	 * json 타입 API 의 API Exception 처리 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param e APIException
	 */
	private void writeAPIExceptionResult(HttpServletResponse response, APIException e)
	{
		logger.warn(e, e);
		writeResultOnly(response, ((APIException)e).getCode());
	}

	/**
	 * json 타입 API 의 map 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode 결과 코드
	 * @param mapResult 결과 맵
	 * @param charset Response 캐릭터셋
	 */
	public void writeMapResult(HttpServletResponse response, int resultCode, JSONObject mapResult, String charset) 
	{
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element(API_DEF_RESULT, String.valueOf(resultCode));
		resultJSONObject.element(API_DEF_RESULT_TYPE, API_DEF_MAP);
		
		if(mapResult == null)
			resultJSONObject.element(API_DEF_MAP, new JSONObject());
		else
			resultJSONObject.element(API_DEF_MAP, mapResult);
		
		finalizeWrite(response, resultJSONObject, charset);
	}
	
	/**
	 * json 타입 API 의 map 결과를 write 한다.
	 * 
	 * @param response response
	 * @param resultCode 결과 코드
	 * @param mapResult 결과 맵
	 */
	public void writeMapResult(HttpServletResponse response, int resultCode, JSONObject mapResult) 
	{
		writeMapResult(response, resultCode, mapResult, null);
	}
	
	/**
	 * json 타입 API 의 list 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param returnCode 결과 코드
	 * @param listResult 리스트 결과
	 * @param charset Response 캐릭터셋
	 */
	public void writeListResult(HttpServletResponse response, int returnCode, JSONArray listResult, String charset) 
	{
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element(API_DEF_RESULT, String.valueOf(returnCode));
		resultJSONObject.element(API_DEF_RESULT_TYPE, API_DEF_LIST);
		
		if(listResult == null)
			resultJSONObject.element(API_DEF_LIST, new JSONArray());
		else
			resultJSONObject.element(API_DEF_LIST, JSONArray.fromObject(listResult));
		
		finalizeWrite(response, resultJSONObject, charset);
	}	

	/**
	 * json 타입 API 의 list 결과를 write 한다.
	 * 
	 * @param response HttpServletResponse
	 * @param returnCode 결과 코드
	 * @param listResult 리스트 결과
	 */
	public void writeListResult(HttpServletResponse response, int returnCode, JSONArray listResult)
	{
		writeListResult(response, returnCode, listResult, null);
	}
}
