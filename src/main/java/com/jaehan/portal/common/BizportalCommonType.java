package com.jaehan.portal.common;

import java.util.HashMap;
import java.util.Map;

public class BizportalCommonType {
	/**
	 * ���
	 */
	public static final String ADMIN = "A";
	/**
	 * ������
	 */
	public static final String DEVELOPER = "D";
	/**
	 * ��Ʈ��
	 */
	public static final String PARTNEL = "P";
	
	public static Map<String, String> getTypeProfiles() {
		Map<String,String> typeProfiles = new HashMap<String,String>();
//		typeProfiles.put(ADMIN, "���");
		typeProfiles.put(DEVELOPER, "Developer");
		typeProfiles.put(PARTNEL, "Partner");
		return typeProfiles;
	}
}
