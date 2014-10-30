package com.harlan.lib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

public class HImage {
	private static final String TAG = "HImage";
			
	/**
	 * 
	 * 把图片转成圆角
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	/**
	 * 把图片转成圆角
	 * @param bitmap
	 * @param angle 图角角度 建议0~90
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float angle) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, angle, angle, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//BitmapFactory.decodeFile(filePath, options);
		//options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);// 内存溢出
	}
	
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(filePath, options);// 内存溢出
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
/*	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}*/

	/**
	 * 把本地图片压缩后另存到指定目录
	 * 
	 * @param imagePath
	 * @return 压缩后的图片路径
	 */
/*	public static String compressImage(String imagePath, String saveDir) {
		String compressPath = null;
		Bitmap bitmap = getSmallBitmap(imagePath);
		if (bitmap != null) {
			compressPath = saveDir+ UUID.randomUUID()+ imagePath.substring(imagePath.lastIndexOf("."),imagePath.length());
			saveBitmap(bitmap, compressPath);
		}
		return compressPath;

	}*/

	/**
	 * 将bitmap保存到本地
	 * @param mBitmap
	 * @param imagePath
	 * @param imageType 图片类型 PNG,JPG,
	 */
	@SuppressLint("NewApi")
	public static void saveBitmap(Bitmap bitmap, String imagePath,String imageType) {
		try {
			File file = new File(imagePath);
			HFile.createDipPath(imagePath);
			FileOutputStream fOut = new FileOutputStream(file);
			if("PNG".equalsIgnoreCase(imageType)){
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			}else if("JPG".equalsIgnoreCase(imageType) || "JPEG".equalsIgnoreCase(imageType)){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			}else  if("WEBP".equalsIgnoreCase(imageType) ){
				bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fOut);
			}else{
				HLog.w(TAG, "图片保存失败，无法确定图片类型。类型为：" + imageType);
			}
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			HLog.e(TAG, e.toString());
		} catch (IOException e) {
			HLog.e(TAG, e.toString());
		}
		
	}
	
	/**
	 * 将bitmap保存到本地
	 * 
	 * @param mBitmap
	 * @param imagePath
	 */
	@SuppressLint("NewApi")
	public static void saveBitmap(Bitmap bitmap, String imagePath,int s) {
		File file = new File(imagePath);
		HFile.createDipPath(imagePath);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.WEBP, s, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据指定的大小压缩图片
	 * 
	 * @param sourceImagePath
	 * @param outDirectory
	 * @return 压缩后的图片路径, 图片不需要压缩则返回原路径
	 */
	public static  String compressImage(String sourceImagePath, String outDirectory) {
		int maxWidth = 480;
		int maxHeight = 800;
		String compressPath = null;
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(sourceImagePath, ops);
		double ratio = 1.0;
		if (ops.outWidth > ops.outHeight && ops.outWidth > maxWidth) {
			ratio = ops.outWidth / maxWidth;
		} else if (ops.outHeight > ops.outWidth && ops.outHeight > maxHeight) {
			ratio = ops.outHeight / maxHeight;
		} else {
			return sourceImagePath;
		}
		BitmapFactory.Options newOps = new BitmapFactory.Options();
		newOps.inSampleSize = (int) (ratio + 1);
		newOps.outWidth = (int) (ops.outWidth / ratio);
		newOps.outHeight = (int) (ops.outHeight / ratio);
		Bitmap bitmap = BitmapFactory.decodeFile(sourceImagePath, newOps);
		compressPath = outDirectory+ UUID.randomUUID()+ sourceImagePath.substring(sourceImagePath.lastIndexOf("."),sourceImagePath.length());
		File outFile = new File(compressPath);
		try {
			File parent = outFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			outFile.createNewFile();
			OutputStream os = new FileOutputStream(outFile);
			bitmap.compress(CompressFormat.JPEG, 100, os);
			os.close();
			bitmap.recycle();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return compressPath;
	}
	
	/**
	 * 得到网络图片
	 * 
	 * @param url
	 * @param localPath
	 * @return
	 */
	public static Bitmap getBitmap(String url, String localPath) {
		Bitmap bitmap = null;
		File file = new File(localPath);
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			bitmap = getHttpBitmap(url);
		}
		return bitmap;

	}
	
	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);

			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}
	
	/**
	 * 得到本地的Bitmap
	 * 
	 * @param localPath
	 * @return
	 */
	public static Bitmap getLocalBitmap(String localPath) {
		System.out.println("localPath:" + localPath);
		File file = new File(localPath);
		Bitmap bitmap = null;
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}
	
	/**
	 * 复制分享图片到本地目录
	 * 
	 * @param context
	 */
	public static boolean copyAppFileToSDCard(Context context, int imgForm,
			String toPath) {
		boolean copyResult = true;
		try {
			// File dir = new File(toPath);
			// 如果/sdcard/dictionary目录中存在，创建这个目录

			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）

			// 获得封装dictionary.db文件的InputStream对象
			HFile.createDipPath(toPath);
			InputStream is = context.getResources().openRawResource(imgForm);
			FileOutputStream fos = new FileOutputStream(new File(toPath));
			byte[] buffer = new byte[8192];
			int count = 0;
			// 开始复制dictionary.db文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			// setTitle("copy DB");

		} catch (Exception e) {
			copyResult = false;
		}
		return copyResult;
	}
	
	/**
	 * 批量图片压缩到指定目录
	 * @param images
	 * @param compressDir
	 * @return
	 */
	public static ArrayList<String> compressImage(List<String> images,String compressDir){
		if(images == null)
			return null;
		//图片最大上传大小，300KB
		int maxSize = 1000*1024;
		ArrayList<String> resultImage = new ArrayList<String>();
		for (int i = 0; i < images.size(); i++) {
			String imagePath = images.get(i);
			File file = new File(imagePath);
			if(file.exists()){
				long length = file.length();
				//int compressPercent = 100;
				if(length > maxSize){
					BitmapFactory.Options newOpts = new BitmapFactory.Options();  
			        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
			        newOpts.inJustDecodeBounds = true;  
			        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,newOpts);//此时返回bm为空  
			        newOpts.inJustDecodeBounds = false;  
			        int w = newOpts.outWidth;  
			        int h = newOpts.outHeight;  
			        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
			        float hh = 800f;//这里设置高度为800f  
			        float ww = 480f;//这里设置宽度为480f  
			        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
			        int be = 1;//be=1表示不缩放  
			        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
			            be = (int) (newOpts.outWidth / ww);  
			        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
			            be = (int) (newOpts.outHeight / hh);  
			        }  
			        if (be <= 0)  
			            be = 1;  
			        newOpts.inSampleSize = be;//设置缩放比例  
			        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
			        bitmap = BitmapFactory.decodeFile(imagePath, newOpts);  
					
					//保存图片
					String fileName  = file.getName();
					String newFileName = null;
					if(fileName.indexOf(".") > 0)
						newFileName = compressDir + UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."),fileName.length());
					else
						newFileName = compressDir + UUID.randomUUID().toString() +".jpg";
					
					//String saveImagePath =compressDir +file.getName();
					File saveImagefile = new File(newFileName);
					HFile.createDipPath(newFileName);
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(saveImagefile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					file = new File(imagePath);
					length = file.length();
					//compressPercent =(int)(length /maxSize);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					try {
						fOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					resultImage.add(newFileName);
				}else{
					String strNewName = compressDir + UUID.randomUUID().toString() + imagePath.substring(imagePath.lastIndexOf("."), imagePath.length());
					HFile.copyFile(imagePath, strNewName);
					resultImage.add(strNewName);
				}
			}
		}
		return resultImage;
	}
	
	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024> 512) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
	
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
	public static Bitmap compressImage(Bitmap image) {
		try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
		}catch(Exception e){
			return null;
		}
	}
	
	
	/*
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if(bitmap == null)
			return null;
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	/**
	 * 读取本地的图片得到缩略图，如图片需要旋转则旋转。
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static  Bitmap getLocalThumbImg(String path,float width,float height){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path,newOpts);//此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > width) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		bitmap = compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		int degree = readPictureDegree(path);
		bitmap = rotaingImageView(degree, bitmap);
		return bitmap;
	}
	
	
}
