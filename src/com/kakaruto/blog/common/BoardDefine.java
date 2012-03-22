package com.kakaruto.blog.common;

public class BoardDefine {
	public static final String TEST_SAVE_FILE_PATH = "C:/Tmp/";
	public static final String REAL_SAVE_FILE_PATH = "/home/kakaruto/data/board/upload_data/";
	
	public static final String getSaveFilePath(){
		String OSNAME = System.getProperty("os.name").toUpperCase();
		if(OSNAME.equals("WINDOWS XP")){
			return TEST_SAVE_FILE_PATH;
		}else{
			return REAL_SAVE_FILE_PATH;
		}
	}
}
