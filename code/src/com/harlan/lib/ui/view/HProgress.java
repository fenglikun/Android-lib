package com.harlan.lib.ui.view;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 等待中加载效果。
 * 使用方法
 * 显示：HProgress.show(context,"正在登录...");
 * 结束：Hprogress.dismiss();
 * @author Harlan Song
 * 2014年10月30日
 */
public class HProgress {
	
	private static  ProgressDialog dialog = null;
	
	public static void show(Context context,String message){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
		dialog = new ProgressDialog(context);
		//dialog.setCancelable(false);
		if(message != null){
			dialog.setMessage(message);
		}else{
			dialog.setMessage("加载中...");
		}
		dialog.show();
	}
	
	public static void dismiss(){
		if(dialog != null)
			dialog.dismiss();
	}
}
