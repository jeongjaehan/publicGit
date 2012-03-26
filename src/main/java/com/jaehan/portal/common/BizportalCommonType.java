package com.jaehan.portal.common;

import java.util.HashMap;
import java.util.Map;

public class BizportalCommonType {
	/**
	 * 운영자
	 */
	public static final String ADMIN = "A";
	/**
	 * 개발자
	 */
	public static final String DEVELOPER = "D";
	/**
	 * 파트너
	 */
	public static final String PARTNEL = "P";
	
	public static Map<String, String> getTypeProfiles() {
		Map<String,String> typeProfiles = new HashMap<String,String>();
//		typeProfiles.put(ADMIN, "운영자");
		typeProfiles.put(DEVELOPER, "Developer");
		typeProfiles.put(PARTNEL, "Partner");
		return typeProfiles;
	}
}
