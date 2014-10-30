package com.harlan.lib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Environment;
import android.util.Log;

/**
 * 日志信息
 * 
 * @author Harlan Song
 * @createDate 2013-2-8
 */
public class HLog {

	/**
	 * 日志开关
	 */
	private static boolean LOG_OPEN_DEBUG = true;
	private static boolean LOG_OPEN_POINT = false;

	/**
	 * 日志类型开关，必须 LOG_OPEN_DEBUG = true的时候才能启作用
	 */
	private static boolean logOpeni = true;
	private static boolean logOpend = true;
	private static boolean logOpenw = true;
	private static boolean logOpene = true;

	/**
	 * 日志目录
	 */
	private static final String PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + "/owc/log/";
	private static final String PATH_LOG_INFO = PATH_ROOT + "info/";
	private static final String PATH_LOG_WARNING = PATH_ROOT + "warning/";
	private static final String PATH_LOG_ERROR = PATH_ROOT + "error/";
	private static final String AUTHOR ="HARLAN -->";
	
	public static void closeLog(){
		LOG_OPEN_DEBUG = false;
		LOG_OPEN_POINT = false;
	}

	public static void d(String tag, String message) {
		if (message != null && message != null) {
			if (LOG_OPEN_DEBUG && logOpend) {
				Log.d(tag,AUTHOR + message);
			}
			if (LOG_OPEN_POINT)
				point(PATH_LOG_INFO, tag, message);
		}

	}

	public static void i(String tag, String message) {
		if (message != null && message != null) {
			if (LOG_OPEN_DEBUG && logOpeni) {
				Log.i(tag,AUTHOR + message);
			}
			if (LOG_OPEN_POINT)
				point(PATH_LOG_INFO, tag, message);
		}

	}

	public static void w(String tag, String message) {
		if (message != null && message != null) {
			if (LOG_OPEN_DEBUG && logOpenw) {
				Log.w(tag,AUTHOR + message);
			}
			if (LOG_OPEN_POINT)
				point(PATH_LOG_WARNING, tag, message);
		}

	}

	public static void e(String tag, String message) {
		if (message != null && message != null) {
			if (LOG_OPEN_DEBUG && logOpene) {
				Log.e(tag,AUTHOR + message);
			}
			if (LOG_OPEN_POINT)
				point(PATH_LOG_ERROR, tag, message);
		}

	}

	public static void point(String path, String tag, String msg) {
		if (HPhone.sdcard()) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("",
					Locale.SIMPLIFIED_CHINESE);
			dateFormat.applyPattern("yyyy");
			path = path + dateFormat.format(date) + "/";
			dateFormat.applyPattern("MM");
			path += dateFormat.format(date) + "/";
			dateFormat.applyPattern("dd");
			path += dateFormat.format(date) + ".log";
			dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
			String time = dateFormat.format(date);
			File file = new File(path);
			if (!file.exists())
				createDipPath(path);
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
				out.write(time + " " + tag + " " + msg + "\r\n");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据文件路径 递归创建文件
	 * 
	 * @param file
	 */
	public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);
		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
