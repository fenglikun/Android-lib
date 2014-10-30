package com.harlan.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

public class HPhone {
	Context context;
	public HPhone(Context context){
		this.context = context;
	}
	/**
	 * 判断是否有SD卡或SD卡是否可用。
	 * 
	 * @return
	 */
	public static boolean sdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	/** 
     * 获得SD卡总大小 
     *  
     * @return 
     */  
    public String getSDTotalSize() {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return Formatter.formatFileSize(context, blockSize * totalBlocks);  
    }  
  
    /** 
     * 获得sd卡剩余容量，即可用大小 
     *  
     * @return 
     */  
    public String getSDAvailableSize() {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return Formatter.formatFileSize(context, blockSize * availableBlocks);  
    }  
  
    /** 
     * 获得机身内存总大小 
     *  
     * @return 
     */  
    public String getRomTotalSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return Formatter.formatFileSize(context, blockSize * totalBlocks);  
    }  
  
    /** 
     * 获得机身可用内存 
     *  
     * @return 
     */  
    public  String getRomAvailableSize() {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return Formatter.formatFileSize(context, blockSize * availableBlocks);  
    }  
    
    //得到SD卡目录，如有SD卡优选取SD卡目录。
	public static String getSDPath() {
		String sdcard_path = "";
		// 得到路径
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;

				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						sdcard_path = sdcard_path.concat(columns[1]);
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						sdcard_path = sdcard_path.concat(columns[1]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HLog.i("Hphone","sdcard_path:" + sdcard_path);
		if(sdcard_path == null || sdcard_path.length() == 0){
			sdcard_path = Environment.getExternalStorageDirectory().getPath();
			return sdcard_path;
		}else{
			if(!sdcard_path.startsWith("/storage"))
				sdcard_path = Environment.getExternalStorageDirectory().getPath();
		}
		return sdcard_path;
	}
	
	
	public static String[] cpuInfo() {    
        String str1 = "/proc/cpuinfo";    
        String str2 = "";    
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率    
        String[] arrayOfString;    
        try {    
            FileReader fr = new FileReader(str1);    
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);    
            str2 = localBufferedReader.readLine();    
            arrayOfString = str2.split("\\s+");    
            for (int i = 2; i < arrayOfString.length; i++) {    
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";    
            }    
            str2 = localBufferedReader.readLine();    
            arrayOfString = str2.split("\\s+");    
            cpuInfo[1] += arrayOfString[2];    
            localBufferedReader.close();    
        } catch (IOException e) {    
        }    
        return cpuInfo;    
    } 
	
	public static String screenResolution(Context context){
		String size = "";
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		size = displayMetrics.widthPixels + "X" + displayMetrics.heightPixels;
		return size;
	}
	
	/**
	 * 得到手机信息
	 * @return
	 */
	public static String getPhoneInfo(){
		JSONObject json = new JSONObject();
		try {
			json.put("BOARD", Build.BOARD);// 主板
			json.put("BRAND", Build.BRAND); // android系统定制商
			json.put("CPU_ABI", Build.CPU_ABI); // cpu指令集
			json.put("DEVICE", Build.DEVICE); // 设备参数
			json.put("DISPLAY", Build.DISPLAY); // 显示屏参数
			json.put("FINGERPRINT", Build.FINGERPRINT); // 硬件名称
			json.put("HOST", Build.HOST);
			json.put("ID", Build.ID); // 修订版本列表
			json.put("MANUFACTURER", Build.MANUFACTURER); // 硬件制造商
			json.put("MODEL", Build.MODEL); // 版本
			json.put("PRODUCT", Build.PRODUCT); // 手机制造商
			json.put("TAGS", Build.TAGS); // 描述build的标签
			json.put("TIME", Build.TIME);
			json.put("TYPE", Build.TYPE); // builder类型
			json.put("USER", Build.USER);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
