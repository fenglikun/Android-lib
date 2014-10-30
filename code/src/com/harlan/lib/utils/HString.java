package com.harlan.lib.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String util类，
 * 详情参考http://10.249.200.45:1880/test/requestSignature.html
 *
 * @author frank.yef
 * @author zhe.yangz
 *
 */
public final class HString {
    public static final String TAG = "StringUtil";

    public static final String CHARSET_NAME_UTF8        = "UTF-8";
    public static final char[] digital                  = "0123456789ABCDEF".toCharArray();
    public static final String DEFAULT_DATA_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String EMPTY_STRING = "";

    private HString() {
    }

    public static String format(Date date) {
        String retString = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATA_TIME_FORMAT);
            retString = format.format(date);
        } catch (Exception e) {
            HLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return retString;
    }

    public static String encodeHexStr(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        char[] result = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            result[i * 2] = digital[(bytes[i] & 0xf0) >> 4];
            result[i * 2 + 1] = digital[bytes[i] & 0x0f];
        }
        return new String(result);
    }

    public static byte[] decodeHexStr(final String str) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length % 2 != 0) {
            throw new RuntimeException("hex str length must can mod 2, str:" + str);
        }
        byte[] bytes = new byte[charArray.length / 2];
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            int b;
            if (c >= '0' && c <= '9') {
                b = (c - '0') << 4;
            } else if (c >= 'A' && c <= 'F') {
                b = (c - 'A' + 10) << 4;
            } else {
                throw new RuntimeException("unsport hex str:" + str);
            }
            c = charArray[++i];
            if (c >= '0' && c <= '9') {
                b |= c - '0';
            } else if (c >= 'A' && c <= 'F') {
                b |= c - 'A' + 10;
            } else {
                throw new RuntimeException("unsport hex str:" + str);
            }
            bytes[i / 2] = (byte) b;
        }
        return bytes;
    }

    public static String encodeBase64Str(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        //Base64Coder.encode(in)
        return new String(Base64Coder.encode(bytes));
    }

    public static byte[] decodeBase64Str(final String str) {
        if (str == null) {
            return null;
        }
        return Base64Coder.decode(str);
    }

    public static String toString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, CHARSET_NAME_UTF8);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toString(final byte[] bytes, String charset) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] toBytes(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(CHARSET_NAME_UTF8);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 得到一个字符串的'字符长度',
     * 约定英文半角字符长度为1，中文全角字符长度为2。
     * @param str
     * @return 字符长度
     */
    public static int getCharLength(String str) {
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 255) {
                counter++;
            } else {
                counter = counter + 2;
            }
        }
        return counter;
    }

    /**
     * 按指定长度截断中英文混合字符串，并以后缀…结尾，此后缀长度为2。
     * 约定英文半角字符长度为1，中文全角字符长度为2。
     * 如果字符串‘字符长度’等于len，不截断
     * @param str
     * @param len
     * @return
     */
    public static String ShortenCn(String str, int len) {
        return ShortenCn(str, len, "…", 2);
    }

    /**
     * 按指定长度截断中英文混合字符串，并以指定后缀结尾。
     * 约定英文半角字符长度为1，中文全角字符长度为2。
     * 如果字符串‘字符长度’等于len，不截断
     * @param str
     * @param len
     * @param suffix
     * @return
     */
    public static String ShortenCn(String str, int len, String suffix, int suffLen) {
        if ("".equals(str) || str == null || str.trim().equals(""))
            return "";
        if (suffix.length() >= str.length())
            suffix = "";

        StringBuffer sb = new StringBuffer();
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            sb.append(c);
            if (c < 255) {
                counter++;
            } else {
                counter = counter + 2;
            }
            if (counter > len - suffLen) {
                if (i < str.length()-1) {
                    sb.delete(sb.length() - 1, sb.length());
                    sb.append(suffix);
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 截取str字符串的前charLen个字符，这里的charLen为字符长度
     * 约定英文半角字符长度为1，中文全角字符长度为2。
     * @param str
     * @param charLen
     * @return
     */
    public static String charSubString(String str, int charLen) {
        StringBuffer sb = new StringBuffer();
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            sb.append(c);
            if (c < 255) {
                counter++;
            } else {
                counter = counter + 2;
            }
            if (counter > charLen) {
                if (i < str.length()-1) {
                    sb.delete(sb.length() - 1, sb.length());
                }
                break;
            } else if (counter == charLen) {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 将某个字符串取相应长度返回（若有截断则已suffix结尾），字符长度均为1
     * @param str
     * @param length
     * @param suffix
     * @return 不可能返回null
     */
    public static String trimString(String str, int length, String suffix){
        if (suffix != null && suffix.length()>0) {
            int suffixLength = suffix.length();
            if (length < suffixLength) {
                return ".";
            }

            if (str==null) {
                return ".";
            } else if (str.length() > length) {
                return str.subSequence(0, length - suffixLength) + suffix;
            } else {
                return str;
            }
        } else {
            return ".";
        }
    }

    /**
     * 以…结尾的trim（若有截断则已suffix结尾），字符长度均为1
     * @param str
     * @param length
     * @return 不可能返回null
     */
    public static String trimString(String str, int length){
        return trimString(str, length, "…");
    }

    /**
     * 判断是否英文,含正常的英文符号
     * @return
     */
    public static boolean isMessageEnglish(String msg) {
        if (Pattern.matches("^[\\x00-\\x7F]*$", msg)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * URLEncoder.encode(url, "UTF-8")的封装
     * @param url
     * @return
     */
    public static String urlEncode(String url) {
        if (url == null) return null;
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //default
            return "";
        }
    }
    
    /**
	 * 把字节数组转换成16进制字符串
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			if(sTemp != null && sTemp.length() > 0)
				sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	/**  
	 * bytes字符串转换为Byte值  
	 * @param String src Byte字符串，每个Byte之间没有分隔符  
	 * @return byte[]  
	 */    
	public static byte[] hexStr2Bytes(String src)    
	{    
	    int m=0,n=0;    
	    int l=src.length()/2;    
	    byte[] ret = new byte[l];    
	    for (int i = 0; i < l; i++)    
	    {    
	        m=i*2+1;    
	        n=m+1;    
	        ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));    
	    }    
	    return ret;    
	}   

    public static boolean isEmailFormat(String email) {
    	Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    
    
    
    /**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */

	public static String replaceEmpty(String str) {
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n|&nbsp;");
			Matcher m = p.matcher(str);
			str = m.replaceAll("");
		}
		return str;

	}

	/**
	 * @param 待验证的字符串
	 * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
	 */
	public static boolean isEmail(String str) {
		String regex = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*$";
		return match(regex, str);
	}

	/**
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回<b>true</b>,否则为<b>false</b>
	 */
	public static boolean isUrl(String str) {
		String regex = "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*";
		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	
	 /**
	  * 去除特殊字符或将所有中文标号替换为英文标号
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
     *字符串MD5加密
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
}
 	
