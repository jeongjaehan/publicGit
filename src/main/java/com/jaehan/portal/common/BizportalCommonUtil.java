package com.jaehan.portal.common;

public class BizportalCommonUtil {
	/**
	 * 권한별 초기화  URL 반환
	 * @return 권한별 초기화  url
	 */
	public static String returnUrl(String type_profile){
		if(type_profile.equals(BizportalCommonType.ADMIN))
			return "/admin";
		else if(type_profile.equals(BizportalCommonType.PARTNEL))
			return "/partner";
		else
			return "/developer";
	}
	
}
