package com.jaehan.portal.common;

public class BizportalCommonUtil {
	/**
	 * ���Ѻ� �ʱ�ȭ  URL ��ȯ
	 * @return ���Ѻ� �ʱ�ȭ  url
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
