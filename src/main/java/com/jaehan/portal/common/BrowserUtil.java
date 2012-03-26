package com.jaehan.portal.common;

import org.apache.commons.lang.StringUtils;

/**
 * 브라우져 유형을 추측하는 클래스. 
 * user-agent 스트링 값에서 특정 문자열 포함여부로 판단하기 때문에 
 * 버전이 늘어남에 따라 정확성이 떨어질 수 있음. 
 * 
 *
 */
public class BrowserUtil 
{
	/**
	 * 브라우져 타입 enum
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
	 * 브라우져 유형을 추론하여 그 유형에 해당하는 Browser enum 을 리턴한다.
	 * 
	 * @param  userAgentString 헤더의 user-agent
	 * @return 브라우져 유형 enum
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
	 * 브라우져 유형에 맞는 파라미터 인코딩 캐릭터셋을 리턴한다.
	 * 
	 * @param  browser 브라우져 유형 enum
	 * @return 캐릭터셋
	 */
	public static String getBrowserReqCharEncoding(Browser browser)
	{
		if(browser == null)
			return null;
		else
			return browser.enc;
	}	

	/**
	 * 브라우져 유형을 추론하여 그 유형에 해당하는 파라미터 인코딩 캐릭터셋을 리턴한다.
	 * 
	 * @param  userAgentString 헤더의 user-agent
	 * @return 캐릭터셋
	 */
	public static String guessBrowserReqCharEncoding(String userAgentString)
	{
		Browser browser = guessBrowser(userAgentString);
		return getBrowserReqCharEncoding(browser);
 	}
}
