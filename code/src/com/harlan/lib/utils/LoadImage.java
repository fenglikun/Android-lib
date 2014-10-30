package com.harlan.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * SD卡缓存异步加载图片
 * 待测试
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 
 */
public class LoadImage {
	//private final String TAG = "LocalCacheBitmap";
	private Drawable loadDrawable = null;
	private String cacheDir;
	private Context context;
	//存放图片不存在的地址，下次将不再请求。
	private Map<String,Boolean> mapNotFound;
	//private Map<String, SoftReference<Bitmap>> imageCache;  
	//内存中缓存图片数量
	//private final int CACHE_SIZE = 10;
	private ExecutorService executorService;
	private final int nThreads = 3;
	private static LoadImage cacheBitmap = null;
	private int bitmapMaxWidth;
	private int bitmapMaxHeight;
	public LoadImage(Context context){
		this.context = context.getApplicationContext();
		mapNotFound = new HashMap<String, Boolean>();
		executorService = Executors.newFixedThreadPool(nThreads);
		DisplayMetrics metrics  = context.getResources().getDisplayMetrics();
		bitmapMaxWidth = metrics.widthPixels;
		bitmapMaxHeight = metrics.heightPixels;
		
		//count = 0;
		//imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public static synchronized void getInstance(Context context){
		if(cacheBitmap == null){
			cacheBitmap = new LoadImage(context);
		}
	}
	
	public void configBitmapMaxWidth(int width){
		bitmapMaxWidth = width;
	}
	
	public void configBitmapMaxHeight(int height){
		bitmapMaxHeight = height;
	}
	
	
	public void display(ImageView imageview,String url){
		display(imageview,url,cacheDir,0,0);
	}
	
	public void display(ImageView imageview,String url,String cacheDir){
		display(imageview,url,cacheDir,0,0);
	}
	
	public void display(ImageView imageview,String url,String cacheDir,int width,int height){
		//HLog.i("LocalCacheBitmap", "url:" + url + "cacheDir:" + cacheDir + ";width:" + width + ";height:" + height);
		imageview.setImageDrawable(loadDrawable);
		if(HPhone.sdcard()){
			if(url != null && url.length() > 0 &&  mapNotFound.get(url) == null ){
				//new ImageAsyncTask().execute(imageview,url,cacheDir,width,height);
				new ImageAsyncTask().executeOnExecutor(executorService, imageview,url,cacheDir,width,height);
			}
			
		}
	}
	public void display(ImageView imageview,String url,int width,int height){
		//HLog.i(TAG, "url:" + url + ";width:" + width + ";height:" + height);
		display(imageview, url, cacheDir, width, height);
	}
	
	private class ImageAsyncTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageview;
		@Override
		protected Bitmap doInBackground(Object... params) {
			//count++;
			//System.out.println("有 " + count+"个线程在执行");
			imageview = (ImageView) params[0];
			String url = (String) params[1];
			String cacheDirStr = (String) params[2];
			int width = (Integer) params[3];
			int height = (Integer) params[4];
			Bitmap bitmap = null;
			String cachePath = cacheDirStr + MD5.md5(url);
			File file = new File(cachePath);
			if(file.exists()){
				try {
					FileInputStream fs = new FileInputStream(file);
					bitmap = BitmapFactory.decodeStream(fs);
					fs.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (HAndroid.networkStatusOK(context)){
				bitmap = CacheBitmapHelper.downloadImage(url, cachePath, width, height);
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			
			if(imageview != null &&  result != null){
				imageview.setImageBitmap(result);
			}
		}
	}

	
}
