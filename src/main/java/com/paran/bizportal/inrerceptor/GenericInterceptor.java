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
 * API Request �� ��ó���� ���� Interceptor
 * 
 */
public class GenericInterceptor extends HandlerInterceptorAdapter
{
	/**
	 * �⺻ API �ڵ鷯
	 */
	@Autowired	private APIBaseHandler apiBaseHandler = null;
	
	/**
	 * �α� �������� ���� ��Ʈ�� ���۸� �����Ͽ� �����Ѵ�.
	 * 
	 * @param  sb �α� ��Ʈ�� ����
	 * @param  key Ű
	 * @param  value ��
	 * @param  ext1 �� �߰�1
	 * @param  ext2 �� �߰�2
	 * @return �α� ��Ʈ�� ����
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
	 * �α� �������� ���� ��Ʈ�� ���۸� �����Ͽ� �����Ѵ�.
	 * 
	 * @param  sb �α� ��Ʈ�� ����
	 * @param  key Ű
	 * @param  value ��
	 * @return �α� ��Ʈ�� ����
	 */
	private StringBuffer appendReqInfoLine(StringBuffer sb, Object key, Object value)
	{
		return appendReqInfoLine(sb, key, value, null, null);
	}
	
	/**
	 * �α� �������� ���� ��Ʈ�� ���۸� �����Ͽ� �����Ѵ�.
	 * 
	 * @param  sb �α� ��Ʈ�� ����
	 * @param  key Ű
	 * @param  value ��
	 * @return �α� ��Ʈ�� ����
	 */
	private StringBuffer appendKeyValueLine(StringBuffer sb, Object key, Object value)
	{
		return
			sb.append("[").append(key).append("=>").append(value).append("]").append(SystemUtils.LINE_SEPARATOR);
	}
	
	/**
	 * �α� �������� ���� ��Ʈ�� ���ۿ� ���ڿ��� append �Ͽ� �����Ѵ�.
	 * 
	 * @param  sb �α� ��Ʈ�� ����
	 * @param  str ��Ʈ��
	 * @return �α� ��Ʈ�� ����
	 */
	private StringBuffer appendLine(StringBuffer sb, Object str)
	{
		if(str != null)
			sb.append(str);
		
		return sb.append(SystemUtils.LINE_SEPARATOR);
	}
	
	/**
	 * API ��û ��ó���� ���� HandlerInterceptorAdapter Ŭ���� preHandle �޽�� �������̵�
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
		
		// �⺻���� ����Ʈ
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
		
		// ��Ű ����Ʈ
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
		
		// �Ķ���� ����Ʈ
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
	 * API ��û ��ó���� ���� HandlerInterceptorAdapter Ŭ���� afterCompletion �޽�� �������̵�
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
	 * apiBaseHandler getter �޽��
	 * 
	 * @return �⺻ API �ڵ鷯
	 */
	public APIBaseHandler getAPIBaseHandler() 
	{
		return apiBaseHandler;
	}
	
	/**
	 * �ΰ�
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
