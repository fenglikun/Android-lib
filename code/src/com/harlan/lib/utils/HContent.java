package com.harlan.lib.utils;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HContent {
	/***
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	 
	 /** * 去除特殊字符或将所有中文标号替换为英文标号
     * @param str
     * @return
     */
     public static String stringFilter(String str) {         
         str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
         String regEx = "[『』]"; // 清除掉特殊字符         
         Pattern p = Pattern.compile(regEx);
         Matcher m = p.matcher(str);
         return m.replaceAll("").trim();
    } 
     
     

 	/**
 	 * MD5加密字符串
 	 * 
 	 * @param str
 	 * @return
 	 */
 	public static String md5(String str) {
 		MessageDigest md5 = null;
 		try {
 			md5 = MessageDigest.getInstance("MD5");
 		} catch (Exception e) {
 			e.printStackTrace();
 			return "";
 		}

 		char[] charArray = str.toCharArray();
 		byte[] byteArray = new byte[charArray.length];
 		for (int i = 0; i < charArray.length; i++) {
 			byteArray[i] = (byte) charArray[i];
 		}

 		byte[] md5Bytes = md5.digest(byteArray);
 		StringBuffer hexValue = new StringBuffer();
 		for (int i = 0; i < md5Bytes.length; i++) {
 			int val = ((int) md5Bytes[i]) & 0xff;
 			if (val < 16) {
 				hexValue.append("0");
 			}
 			hexValue.append(Integer.toHexString(val));
 		}
 		return hexValue.toString();
 	}
	/**
 	 * 格式化价格
 	 * @param price
 	 * @return
 	 */
 	public static String formatMoney(double price){
		return "¥" + new java.text.DecimalFormat("#").format(price);
	}
 	
	/**
	 * 验证E-mail是否合法
	 * @param str
	 * @return
	 */
	public static boolean isMail(String str) {
		String check = "^([a-z0-9A-Z]+[-|._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(str.trim());
		boolean isMatched = matcher.matches();
		if (isMatched) {
			return true;
		} else {
			return false;
		}
	}
}
