package com.harlan.lib.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义吐司
 * 
 * @author Harlan Song
 * @date 2013-10-6
 */
public class HToast {
	private static final String TAG = "HToast";
	/**
	 * 是否自用自定义的Toast
	 */
	private static final boolean isCustomToast = false;
	 
	private static Toast toast = null;

	public static void showShortText(Context context, int resId) {
		showCustomToast(context, context.getString(resId), Toast.LENGTH_SHORT);
	}

	public static void showShortText(Context context, CharSequence text) {
		showCustomToast(context, text, Toast.LENGTH_SHORT);
	}

	public static void showLongText(Context context, int resId) {
		showCustomToast(context, context.getString(resId), Toast.LENGTH_LONG);
	}

	public static void showLongText(Context context, CharSequence text) {
		showCustomToast(context, text, Toast.LENGTH_LONG);

	}

	public static void showCustomToast(Context context, CharSequence text,
			int duration) {
		if(toast != null)
			toast.cancel();
		if (text != null && text.length() > 0) {
			if (isCustomToast) {
				TextView tv_toast = new TextView(context);
				tv_toast.setPadding(5, 5, 5, 5);
				tv_toast.setBackgroundColor(Color.WHITE);
				tv_toast.setTextColor(Color.RED);
				toast = new Toast(context);
				toast.setView(tv_toast);
				tv_toast.setText(text);
				toast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(duration);
				toast.show();
			} else {
				//toast = new Toast(context);
				Toast.makeText(context, text, duration).show();
				/*toast.setText(text);
				toast.setDuration(duration);
				toast.show();*/
				//Toast.makeText(context, text, duration).show();
			}
		} else {
			HLog.e(TAG, "Toast内容为空。");
		}

	}
}
