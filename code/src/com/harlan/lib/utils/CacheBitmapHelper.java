package com.harlan.lib.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.harlan.lib.net.HHttp;

public class CacheBitmapHelper {
	public static synchronized Bitmap downloadImage(String url,String cachePath,int width,int height){
		Bitmap bitmap = null;
		boolean downloadStatus = HHttp.httpDownload(url, cachePath);
		if(downloadStatus){
			try {
				FileInputStream fs = new FileInputStream(cachePath);
				if(width > 0 && width > 0){
					//压缩本地图片
					BitmapFactory.Options options = new BitmapFactory.Options();
					bitmap = BitmapFactory.decodeFile(cachePath, options);  
					options.inDither = false;//图片不抖动
					options.inPurgeable = true;//内存可以被回收
					options.inInputShareable = true; 
					options.inTempStorage=new byte[1024]; //临时存储  
					int size = options.outWidth / width;
					options.inSampleSize = size;
					bitmap=BitmapFactory.decodeFileDescriptor(fs.getFD(),null,options);
					String imageType = url.substring(url.lastIndexOf(".")+1, url.length());
					HImage.saveBitmap(bitmap, cachePath, imageType);
				}else{
					bitmap = BitmapFactory.decodeStream(fs);
				}
				fs.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
