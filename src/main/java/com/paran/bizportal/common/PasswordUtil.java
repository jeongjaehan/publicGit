package com.paran.bizportal.common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
	
	/**
	 * 생성될 비밀번호 길이
	 */
	public static final int KEY_LENTH = 10; 

	/**
	 *
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateKey() throws NoSuchAlgorithmException {
		String resultStr = null;
		String[] candidates = new String[] { "abcdefghijklmnopqrstuvwxyz","0123456789" };
		int count = KEY_LENTH;
		char[] generated = new char[count];
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		StringBuffer candidateAll = new StringBuffer();
		int watermark = 0;
		for (int i = 0; i < candidates.length; i++) {
			if (candidates[i] != null && !"".equals(candidates[i])) {
				generated[watermark++] = candidates[i].charAt(sr.nextInt(candidates[i].length()));
				candidateAll.append(candidates[i]);
			}
		}
		for (int i = watermark; i < count; i++) {
			generated[i] = candidateAll.toString()
					.charAt(sr.nextInt(candidateAll.length()));
		}
		for (int i = 100000; i >= 0; i--) {
			int x = sr.nextInt(count);
			int y = sr.nextInt(count);
			char tmp = generated[x];
			generated[x] = generated[y];
			generated[y] = tmp;
		}
		resultStr = new String(generated);
		return resultStr;
	}// GEN-LAST:event_jButton1ActionPerformed

}