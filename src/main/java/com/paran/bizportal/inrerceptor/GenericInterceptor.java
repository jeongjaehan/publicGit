package com.paran.bizportal.inrerceptor;

import java.util.Enumeration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.paran.bizportal.base.APIBaseHandler;
import com.paran.bizportal.common.BrowserUtil;
import com.paran.bizportal.common.BrowserUtil.Browser;


/**
 * API Request 의 전처리를 위한 Interceptor
 * 
 */
public class GenericInterceptor extends HandlerInterceptorAdapter
{
	/**
	 * 기본 API 핸들러
	 */
	@Autowired	private APIBaseHandler apiBaseHandler = null;
	
	/**
	 * 로그 포매팅을 위해 스트링 버퍼를 조작하여 리턴한다.
	 * 
	 * @param  sb 로그 스트링 버퍼
	 * @param  key 키
	 * @param  value 값
	 * @param  ext1 값 추가1
	 * @param  ext2 값 추가2
	 * @return 로그 스트링 버퍼
	 */
	private StringBuffer appendReqInfoLine(StringBuffer sb, Object key, Object value, Object ext1, Object ext2)
	{
		if(key != null)
			sb.append(key);

		sb.append("[").append(value).append("]");
		
		if(ext1 != null)
			sb.append(" [").append(ext1).append("]");
		
		if(ext2 != null)
			sb.append(" [").append(ext2).append("]");
		
		return sb.append(SystemUtils.LINE_SEPARATOR); 
	}
	
	/**
	 * 로그 포매팅을 위해 스트링 버퍼를 조작하여 리턴한다.
	 * 
	 * @param  sb 로그 스트링 버퍼
	 * @param  key 키
	 * @param  value 값
	 * @return 로그 스트링 버퍼
	 */
	private StringBuffer appendReqInfoLine(StringBuffer sb, Object key, Object value)
	{
		return appendReqInfoLine(sb, key, value, null, null);
	}
	
	/**
	 * 로그 포매팅을 위해 스트링 버퍼를 조작하여 리턴한다.
	 * 
	 * @param  sb 로그 스트링 버퍼
	 * @param  key 키
	 * @param  value 값
	 * @return 로그 스트링 버퍼
	 */
	private StringBuffer appendKeyValueLine(StringBuffer sb, Object key, Object value)
	{
		return
			sb.append("[").append(key).append("=>").append(value).append("]").append(SystemUtils.LINE_SEPARATOR);
	}
	
	/**
	 * 로그 포매팅을 위해 스트링 버퍼에 문자열을 append 하여 리턴한다.
	 * 
	 * @param  sb 로그 스트링 버퍼
	 * @param  str 스트링
	 * @return 로그 스트링 버퍼
	 */
	private StringBuffer appendLine(StringBuffer sb, Object str)
	{
		if(str != null)
			sb.append(str);
		
		return sb.append(SystemUtils.LINE_SEPARATOR);
	}
	
	/**
	 * API 요청 전처리를 위한 HandlerInterceptorAdapter 클래스 preHandle 메쏘드 오버라이딩
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception
	{
		String reqID = apiBaseHandler.checkAPIReqID(request);
		String charEncoding1 = request.getCharacterEncoding();
		String userAgent = apiBaseHandler.getUserAgent(request);
		Browser browser = BrowserUtil.guessBrowser(userAgent);
		String charEncoding2 = BrowserUtil.getBrowserReqCharEncoding(browser);
		
//		if(StringUtils.isEmpty(charEncoding1))
//			request.setCharacterEncoding(charEncoding2);
		
		// 기본정보 프린트
		StringBuffer logBuffer = new StringBuffer();
		appendLine(logBuffer, null);
		appendLine(logBuffer, "------------------------------------------------------------------");
		appendReqInfoLine(logBuffer,	"API URL ", 						request.getRequestURL(), reqID, null);
		appendReqInfoLine(logBuffer,	"  QueryString ",					request.getQueryString());
		appendReqInfoLine(logBuffer,	"  Server ",						request.getServerName());
		appendReqInfoLine(logBuffer,	"  Method ",						request.getMethod());
		appendReqInfoLine(logBuffer,	"  ContentType ",					request.getContentType());
		appendReqInfoLine(logBuffer,	"  UserAgent ",						userAgent);
		appendReqInfoLine(logBuffer,	"  BrowserName ",					browser.name());
		appendReqInfoLine(logBuffer,	"  ReqCharSet 1st (by Header) ",	charEncoding1);
		appendReqInfoLine(logBuffer,	"  ReqCharSet 2nd (by UA) ",		charEncoding2);
		appendReqInfoLine(logBuffer,	"  ReqCharSet Def ",				request.getCharacterEncoding());
		appendReqInfoLine(logBuffer,	"  Request.getRemoteAddr ",			request.getRemoteAddr());
		appendLine(logBuffer, "------------------------------------------------------------------");
		
		// 쿠키 프린트
		appendLine(logBuffer, "Cookie List ... ");
		
		Cookie[] cookies = request.getCookies();
		if(cookies == null)
		{
			appendLine(logBuffer, "------------------------------------------------------------------");
		}
		else
		{
			String mc = null, cs = null;
			for(int i=0;i<cookies.length;i++)
			{
				String name = cookies[i].getName();
				String val = cookies[i].getValue();
				
				if(StringUtils.equals(name, "MC"))
					mc = val;
				else if(StringUtils.equals(name, "CS"))
					cs = val;

				appendKeyValueLine(logBuffer, name, val);
			}
			
			appendLine(logBuffer, "------------------------------------------------------------------");

		}
		
		// 파라미터 프린트
		appendLine(logBuffer, "Parameter List ... ");
		
		Enumeration<String> en = request.getParameterNames();
		while(en.hasMoreElements())
		{
			String name = (String)en.nextElement();
			String val = request.getParameter(name);
			
			appendKeyValueLine(logBuffer, name, val);
		}
		
		appendLine(logBuffer, "------------------------------------------------------------------");
		
		logger.debug(logBuffer.toString());

		return true;
	}

	/**
	 * API 요청 전처리를 위한 HandlerInterceptorAdapter 클래스 afterCompletion 메쏘드 오버라이딩
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	throws Exception
	{
		StringBuffer logBuffer = new StringBuffer();

		appendReqInfoLine(logBuffer, "API URL ", request.getRequestURL(), 
				apiBaseHandler.checkAPIReqID(request), apiBaseHandler.getAPIDurationTime(request) + " ms");
		logger.debug(logBuffer.toString());		
	}

	/**
	 * apiBaseHandler getter 메쏘드
	 * 
	 * @return 기본 API 핸들러
	 */
	public APIBaseHandler getAPIBaseHandler() 
	{
		return apiBaseHandler;
	}
	
	/**
	 * 로거
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
