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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * SD卡缓存异步加载图片
 * 注：SD卡必须可用。
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2014年4月1日
 */
public class LocalCacheBitmap {
	//private final String TAG = "LocalCacheBitmap";
	private Drawable loadDrawable;
	private String cacheDir;
	private Context context;
	//存放图片不存在的地址，下次将不再请求。
	private Map<String,Boolean> map;
	//内存中缓存图片数量
	private ExecutorService executorService;
	private final int nThreads = 3;
	public LocalCacheBitmap(Context context,Drawable loadDrawable,String cacheDir){
		this.context = context.getApplicationContext();
		this.loadDrawable = loadDrawable;
		this.cacheDir = cacheDir;
		map = new HashMap<String, Boolean>();
		executorService = Executors.newFixedThreadPool(nThreads);
	}
	
	
	public void display(ImageView imageview,String url){
		display(imageview,url,cacheDir,0,0);
	}
	
	public void display(ImageView imageview,String url,String cacheDir){
		display(imageview,url,cacheDir,0,0);
	}
	
	public void display(ImageView imageview,String url,String cacheDir,int width,int height){
		imageview.setImageDrawable(loadDrawable);
		if(HPhone.sdcard()){
			if(url != null && url.length() > 0 &&  map.get(url) == null ){
				new ImageAsyncTask().executeOnExecutor(executorService, imageview,url,cacheDir,width,height);
			}
		}
	}
	public void display(ImageView imageview,String url,int width,int height){
		display(imageview, url, cacheDir, width, height);
	}
	
	private class ImageAsyncTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageview;
		@Override
		protected Bitmap doInBackground(Object... params) {
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
			if(result != null){
				Drawable drawable = new BitmapDrawable(context.getResources(),result); 
				Bitmap previousbitmap = imageview.getDrawingCache();  
				if(previousbitmap != null && !previousbitmap.isRecycled())  
				    previousbitmap.recycle();
				imageview.setImageDrawable(drawable);  
			}
		}
	}
}
