package com.harlan.lib.ui.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

public class HTextView {
	
	/**
	 * 超链接
	 */
	public static void addUrlSpan(TextView textView,String url) {
		SpannableString spanString = new SpannableString(url);
		URLSpan span = new URLSpan(url);
		spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 文字背景颜色
	 */
	public static void addBackColorSpan(TextView textView) {
		SpannableString spanString = new SpannableString("颜色2");
		BackgroundColorSpan span = new BackgroundColorSpan(Color.YELLOW);
		spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 文字颜色
	 */
	public static void addForeColorSpan(TextView textView) {
		SpannableString spanString = new SpannableString("颜色1");
		ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
		spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 字体大小
	 */
	public static void addFontSpan(TextView textView) {
		SpannableString spanString = new SpannableString("36号字体");
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(36);
		spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 粗体，斜体
	 */
	public static void addStyleSpan(TextView textView) {
		SpannableString spanString = new SpannableString("BIBI");
		StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);
		spanString.setSpan(span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}
	
	public static void addImageSpan(TextView textView,Drawable drawable,String content){
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
        //需要处理的文本，[smile]是需要被替代的文本  
        SpannableString spannable = new SpannableString(content+"[smile]");  
        //要让图片替代指定的文字就要用ImageSpan  
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);  
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）  
       //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12  
        spannable.setSpan(span, content.length(),content.length()+"[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);    
        textView.append(spannable);  
	}
}
