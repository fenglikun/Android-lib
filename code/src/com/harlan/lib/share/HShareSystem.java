package com.harlan.lib.share;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * 分享
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2013-10-6
 */
public class HShareSystem {
	
	/**
	 * 分享带图片的内容
	 * @param activity
	 * @param msg
	 * @param imgPath
	 */
	public static void shareImg(Activity activity,String appName,String msg,String imgPath){
		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		//intent.setType("text/plain"); // 分享发送的数据类型
		intent.setType("image/jpg");
		File f = new File(imgPath);
		Uri u = Uri.fromFile(f);
		intent.putExtra(Intent.EXTRA_STREAM, u);
		intent.putExtra(Intent.EXTRA_SUBJECT, appName); // 分享的主题
		intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 这个也许是分享列表的背景吧
		activity.startActivity(Intent.createChooser(intent, "分享"));// 目标应用选择对话框的标题
	}
	
	/**
	 * 分享纯文字的内容
	 * @param activity
	 * @param msg
	 */
	public static void textShare(Activity activity,String appName,String msg){
		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, appName); // 分享的主题
		intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 这个也许是分享列表的背景吧
		activity.startActivity(Intent.createChooser(intent, "分享"));// 目标应用选择对话框的标题
	}
}
