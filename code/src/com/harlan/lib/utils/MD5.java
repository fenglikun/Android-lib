package com.harlan.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * MD5 算法
*/
public class MD5 {
	 protected static MessageDigest messagedigest = null;  
	    static {  
	        try {  
	            messagedigest = MessageDigest.getInstance("MD5");  
	        } catch (NoSuchAlgorithmException nsaex) {  
	            System.err.println(MD5.class.getName()  
	                    + "初始化失败，MessageDigest不支持MD5Util。");  
	            nsaex.printStackTrace();  
	        }  
	    }  
    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
 /*   private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }*/

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String md5(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    
    
    /** 
     * 生成文件的md5校验值 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(File file) throws IOException {         
        InputStream fis;  
        fis = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());  
    }  
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    } 
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同   
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换   
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  

    public static void main(String[] args) {
        //System.out.println(md5("test"));
    	  File file = new File("E:/md52.txt");  
          //098f6bcd4621d373cade4e832627b4f6
          //098f6bcd4621d373cade4e832627b4f6
          //8cff6a87456225afc3b0bd8fecb8c515
          //098f6bcd4621d373cade4e832627b4f6
          //098f6bcd4621d373cade4e832627b4f6
          String md5 = "";
		try {
			md5 = getFileMD5String(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          System.out.println(md5);
    }
}
