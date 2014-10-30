package com.harlan.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SqlUtil {
	
	/**
	 * 将可视化的sql文件内容读出，并去除里面注释，得到可执行的sql.
	 * @param sqlFilePath
	 * @return
	 */
	public static String format(String sqlFilePath){
		StringBuffer  sb = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(sqlFilePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				boolean remark = false;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if(lineTxt.startsWith("/*")){
						remark = true;
					}else if (lineTxt.startsWith("*/")){
						remark = false;
					}else if(!lineTxt.startsWith("--")){
						if(remark == false)
						sb.append(lineTxt);
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}finally{
			
		}
		return sb.toString();
	} 
	
	public static void main(String[] args) {
		//1689
		System.out.println(StringUtil.encryptionKey(format("test.sql")));
	}
}
