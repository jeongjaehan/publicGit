package com.jaehan.portal.common;

import org.apache.commons.lang.StringUtils;

/**
 * ������ ������ �����ϴ� Ŭ����. 
 * user-agent ��Ʈ�� ������ Ư�� ���ڿ� ���Կ��η� �Ǵ��ϱ� ������ 
 * ������ �þ�� ���� ��Ȯ���� ������ �� ����. 
 * 
 *
 */
public class BrowserUtil 
{
	/**
	 * ������ Ÿ�� enum
	 *
	 */
	public enum Browser 
	{
		UNKNOWN("Unknown", null),
		CHROME("Chrome", CharsetName.UTF_8), 
		FIREFOX("Firefox", null), 
		SAFARI("Safari", null), 
		OPERA("Opera", null), 
		MSIE6("MSIE 6", CharsetName.UTF_8), 
		MSIE7("MSIE 7", CharsetName.UTF_8), 
		MSIE8("MSIE 8", CharsetName.UTF_8), 
		MSIE9("MSIE 9", CharsetName.UTF_8);
		
		public String ua = null;
		public String enc = null;
		
		Browser(String ua, String enc)
		{
			this.ua = ua;
			this.enc = StringUtils.defaultString(enc, CharsetName.UTF_8);
		}
	};
	
	/**
	 * ������ ������ �߷��Ͽ� �� ������ �ش��ϴ� Browser enum �� �����Ѵ�.
	 * 
	 * @param  userAgentString ����� user-agent
	 * @return ������ ���� enum
	 */
	public static Browser guessBrowser(String userAgentString)
	{
		Browser browser = Browser.UNKNOWN;
		
		if(!StringUtils.isEmpty(userAgentString))
		{
			for(Browser br : Browser.values())
			{
				if(StringUtils.contains(userAgentString, br.ua))
				{
					browser = br;
					break;
				}
			}			
		}
		
		return browser;
	}
	
	/**
	 * ������ ������ �´� �Ķ���� ���ڵ� ĳ���ͼ��� �����Ѵ�.
	 * 
	 * @param  browser ������ ���� enum
	 * @return ĳ���ͼ�
	 */
	public static String getBrowserReqCharEncoding(Browser browser)
	{
		if(browser == null)
			return null;
		else
			return browser.enc;
	}	

	/**
	 * ������ ������ �߷��Ͽ� �� ������ �ش��ϴ� �Ķ���� ���ڵ� ĳ���ͼ��� �����Ѵ�.
	 * 
	 * @param  userAgentString ����� user-agent
	 * @return ĳ���ͼ�
	 */
	public static String guessBrowserReqCharEncoding(String userAgentString)
	{
		Browser browser = guessBrowser(userAgentString);
		return getBrowserReqCharEncoding(browser);
 	}
}
