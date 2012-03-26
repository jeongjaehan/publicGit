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
 * API ������Ʈ ó���� ���� �⺻ �ڵ鷯 Ŭ����. ��Ű ���ڵ� ó��, Ŭ���̾�Ʈ ������ �м�(����м�), API ��� ����ó��.
 * 
 */
@Component
public class APIBaseHandler 
{
	/**
	 * �ΰ�
	 */
	
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Srping DI messageSourceAccessor
	 */
	@Autowired
	private MessageSourceAccessor messageSourceAccessor = null;
	
	/**
	 * API ��� �������� ����Ʈ ���ڵ� ĳ���ͼ� 
	 */
	private static final String DEFAULT_CHARSET = CharsetName.UTF_8;

	/**
	 * Request Attribute Ű : �Ķ� MC ��Ű
	 */
	private static final String MC_KEY = "paran.cookie.mc";
	
	/**
	 * Request Attribute Ű : Ŭ���̾�Ʈ ������ 
	 */
	private static final String REMOTE_ADDR_KEY = "paran.remoteAddr";
	
	/**
	 * Request Attribute Ű : API ó�� �ð�
	 */
	private static final String API_TIME_KEY = "apiTime";
	
	/**
	 * Request Attribute Ű : API ��û Ű 
	 */
	private static final String API_REQID_KEY = "apiReqID";
	
	/**
	 * json Ÿ�� API�� �ʵ� �̸� : ���� ��� �ڵ� 
	 */
	private static final String API_DEF_RESULT = "result";
	
	/**
	 * json Ÿ�� API�� �ʵ� �̸� : ���� ��� Ÿ��
	 */
	private static final String API_DEF_RESULT_TYPE = "resulttype";
	
	/**
	 * json Ÿ�� API�� �ʵ� �� : resultOnly
	 */
	private static final String API_DEF_RESULT_ONLY = "resultonly";
	
	/**
	 * json Ÿ�� API�� �ʵ� �� : map
	 */
	private static final String API_DEF_MAP = "map";
	
	/**
	 * json Ÿ�� API�� �ʵ� �� : list
	 */
	private static final String API_DEF_LIST = "list";
	
	/**
	 * API ��û �ð��� üũ�Ѵ�. 
	 * Request Attribte�� ������ �Ǿ������� �� ���� �״�� �����ϰ�
	 * �����Ǿ� ���� ������ ���� �ð� �������� ������ �� �� ���� �����Ѵ�.
	 * 
	 * @param  request HttpServletRequest
	 * @return ���� ������, �Ǵ� ���� ������ ReqID 
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
	 * ���� ������ API ó�� �ð��� �����Ѵ�.
	 * 
	 * @param  request HttpServletRequest
	 * @return ��������� API ó�� long �ð�
	 */
	public long getAPIDurationTime(HttpServletRequest request)
	{
		long l = (Long)request.getAttribute(API_TIME_KEY);
		return System.currentTimeMillis() - l;
	}
	
	/**
	 * �Ķ� ��Ű ������ ���� �Ϸ�ó���Ѵ�. 
	 * ��� �ڵ带 �����ϰ� Request �� Attribute �� �����Ѵ�.
	 * 
	 * @param  request HttpServletRequest
	 * @param  resultCode ���� ��� �ڵ�
	 * @param  mcMap MC ��Ű ��
	 * @return MC ��Ű ��
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
	 * �Ķ� MC ��Ű ����  üũ�Ѵ�. 
	 * ����ڵ尡 �����̸� �Ķ� MC ���� �����ϰ�
	 * �ƴϸ� Exception ó�� �Ѵ�. 
	 * 
	 * @param  request HttpServletRequest
	 * @return MC ��Ű ��
	 * @throws Exception ����ڵ尡 ������ �ƴѰ�� Exception �߻�
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
	 * �Ķ� MC ��Ű ����  �����Ѵ�. 
	 * MC ��Ű���� �������� ������ ���� �����Ѵ�. (Exception �߻���Ű�� ����)
	 * 
	 * @param  request HttpServletRequest
	 * @return MC ��Ű ��
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getRequestMCMap(HttpServletRequest request)
	{
		return (HashMap<String, String>)request.getAttribute(MC_KEY);
	}
	
	/**
	 * �Ķ� ���������� ��Ű�� �߰��Ͽ� �����Ѵ�.
	 * 
	 * @param  response response
	 * @param  name ��Ű �̸�
	 * @param  val ��Ű ��
	 * @return ������ ��Ű
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
	 * HTTP Ŭ���̾�Ʈ�� UserAgent(user-agent) ���� �����Ѵ�.
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
	 * json Ÿ�� API �� ����� ���� ó���Ѵ�. 
	 * contentType �� �����ϰ� ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param resultJSONObject ��� json ��ü
	 * @param charset Response ĳ���ͼ�
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
	 * json Ÿ�� API �� result Only ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode ��� �ڵ�
	 * @param charset Response ĳ���ͼ�
	 */
	protected void writeResultOnly(HttpServletResponse response, int resultCode, String charset)	
	{
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element(API_DEF_RESULT, String.valueOf(resultCode));
		resultJSONObject.element(API_DEF_RESULT_TYPE, API_DEF_RESULT_ONLY);
		
		finalizeWrite(response, resultJSONObject, charset);
	}
	
	/**
	 * json Ÿ�� API �� result Only ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode ��� �ڵ�
	 */
	public void writeResultOnly(HttpServletResponse response, int resultCode) 
	{
		writeResultOnly(response, resultCode, null);
	}
	
	/**
	 * json Ÿ�� API �� Exception ó�� ����� ó�� �Ѵ�.
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
	 * json Ÿ�� API �� API Exception ó�� ����� write �Ѵ�.
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
	 * json Ÿ�� API �� map ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param resultCode ��� �ڵ�
	 * @param mapResult ��� ��
	 * @param charset Response ĳ���ͼ�
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
	 * json Ÿ�� API �� map ����� write �Ѵ�.
	 * 
	 * @param response response
	 * @param resultCode ��� �ڵ�
	 * @param mapResult ��� ��
	 */
	public void writeMapResult(HttpServletResponse response, int resultCode, JSONObject mapResult) 
	{
		writeMapResult(response, resultCode, mapResult, null);
	}
	
	/**
	 * json Ÿ�� API �� list ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param returnCode ��� �ڵ�
	 * @param listResult ����Ʈ ���
	 * @param charset Response ĳ���ͼ�
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
	 * json Ÿ�� API �� list ����� write �Ѵ�.
	 * 
	 * @param response HttpServletResponse
	 * @param returnCode ��� �ڵ�
	 * @param listResult ����Ʈ ���
	 */
	public void writeListResult(HttpServletResponse response, int returnCode, JSONArray listResult)
	{
		writeListResult(response, returnCode, listResult, null);
	}
}
