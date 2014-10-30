package com.harlan.lib.utils;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


/**
 * Android通用类
 * 
 * @author Harlan Song
 * @date 2013-10-6
 */
public class HAndroid {
	private static final String TAG = "AndroidUtil";

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int getVerCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			HLog.e(TAG, "Cannot find package and its version info.");
			return -1;
		}
	}

	public static String getVerName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			HLog.e(TAG, "Cannot find package and its version info.");
			return "no version name";
		}
	}

	/*
	 * public static String getAppName(Context context) { String verName =
	 * context.getResources().getText(R.string.app_name) // TODO NEED PROVE
	 * .toString(); return verName; }
	 */

	/**
	 * 获取DeviceId
	 * 
	 * @param context
	 * @return 当获取到的TelephonyManager为null时，将返回"null"
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return "null";
		} else {
			String id = tm.getDeviceId();
			return id == null ? "null" : id;
		}
	}

	/**
	 * 显示或隐藏IME
	 * 
	 * @param context
	 * @param bHide
	 */
	public static void hideIME(Activity context, boolean bHide) {
		if (bHide) {
			try {
				((InputMethodManager) context
						.getSystemService(Activity.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(context.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		} else { // show IME
			try {
				((InputMethodManager) context
						.getSystemService(Activity.INPUT_METHOD_SERVICE))
						.showSoftInput(context.getCurrentFocus(),
								InputMethodManager.SHOW_IMPLICIT);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}

	/**
	 * 在dialog开启前确定需要开启后跳出IME
	 * 
	 * @param dialog
	 */
	public static void showIMEonDialog(AlertDialog dialog) {
		try {
			Window window = dialog.getWindow();
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		} catch (Exception e) {
			HLog.e(TAG, e.toString());
		}
	}

	/**
	 * 判断一个apk是否安装
	 * 
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static boolean isPkgInstalled(Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return true;
	}

	public static boolean isGooglePlayInstalled(Context ctx) {
		return isAndroidMarketInstalled(ctx);
	}

	/**
	 * @deprecated use isGooglePlayInstalled(Context ctx) instead
	 * @param ctx
	 * @return
	 */
	public static boolean isAndroidMarketInstalled(Context ctx) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q=foo"));
		PackageManager pm = ctx.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void installApkWithPrompt(File apkFile, Context context) {
		Intent promptInstall = new Intent(Intent.ACTION_VIEW);
		promptInstall.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		context.startActivity(promptInstall);
	}

	/**
	 * @param context
	 *            used to check the device version and DownloadManager
	 *            information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Note: Make sure isDownloadManagerAvailable return is true before use this
	 * method.
	 * 
	 * @param apkName
	 *            Apk File Name
	 * @param fullApkUrl
	 *            url of full
	 * @param context
	 *            Context
	 */
	/*
	 * public static void downloadApkByDownloadManager(String apkName, String
	 * fullApkUrl, Context context) { DownloadManager.Request request = new
	 * DownloadManager.Request(Uri.parse(fullApkUrl));
	 * request.setDescription(fullApkUrl); request.setTitle(apkName);
	 * 
	 * // in order for this if to run, you must use the android 3.2 to compile
	 * your app if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * request.allowScanningByMediaScanner();
	 * request.setNotificationVisibility(DownloadManager
	 * .Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); }
	 * request.setDestinationInExternalPublicDir
	 * (Environment.DIRECTORY_DOWNLOADS, apkName);
	 * request.setVisibleInDownloadsUi(false);
	 * request.setMimeType("application/vnd.android.package-archive");
	 * 
	 * // get download service and enqueue file DownloadManager manager =
	 * (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	 * manager.enqueue(request); }
	 */

	public static boolean networkStatusOK(final Context context) {
		boolean netStatus = false;

		try {
			ConnectivityManager connectManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()
						&& activeNetworkInfo.isConnected()) {
					netStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HLog.i(TAG, "network:" + netStatus);
		return netStatus;
	}

	/*
	 * public static void showNetworkFailureDlg(final Activity context) { try {
	 * AlertDialog.Builder b = new AlertDialog.Builder(context)
	 * .setIcon(android.R.drawable.ic_dialog_alert)
	 * .setTitle(R.string.check_network_no_available_network_title) .setMessage(
	 * R.string.check_network_no_available_network_message);
	 * b.setPositiveButton(android.R.string.ok, null).show(); } catch (Exception
	 * e) { e.printStackTrace(); } }
	 */

	public static boolean isWifiActive(Context context) {
		Context context2 = context.getApplicationContext();
		ConnectivityManager manager = (ConnectivityManager) context2
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (manager != null) {
			info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ("WIFI".equals(info[i].getTypeName())
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String imei(Context context) {
		String Imei = ((TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
		return Imei;
	}
	
	

	/**
	 * 拨打电话
	 * 
	 * @param activity
	 * @param phone
	 */
	public static void callTelephoneNum(Activity activity, String phone) {
		try {
			if (!"".equals(phone)) {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + phone));
				activity.startActivity(phoneIntent);
			} else {
				Toast.makeText(activity, "电话号码错误或为空", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			Toast.makeText(activity, "电话号码错误或为空", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * 发送短信
	 * 
	 * @param smsBody
	 */

	public static void sendSMS(Context context,String smsBody)
	{
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}
    
    public static String  getMACAddress(Context context){
    	String macAddress = null;
    	WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
    	if (null != info) {
    	   macAddress = info.getMacAddress();
    	    //ip = Integer.toString(info.getIpAddress());
    	}
    	return macAddress;
    }
    
    
}
