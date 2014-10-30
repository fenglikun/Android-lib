package com.harlan.lib.utils;

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

public class HText {
	
	/**
	 * 超链接
	 */
	public static void addUrlSpan(TextView textView,String url,int begin,int end) {
		SpannableString spanString = new SpannableString(url);
		URLSpan span = new URLSpan(url);
		spanString.setSpan(span, begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 文字背景颜色
	 */
	public static void addBackColorSpan(TextView textView,String text,int begin,int end) {
		SpannableString spanString = new SpannableString(text);
		BackgroundColorSpan span = new BackgroundColorSpan(Color.YELLOW);
		spanString.setSpan(span,begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 文字颜色
	 */
	public static void addForeColorSpan(TextView textView,String text,int begin,int end) {
		SpannableString spanString = new SpannableString(text);
		ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
		spanString.setSpan(span, begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 字体大小
	 */
	public static void addFontSpan(TextView textView,String text,int begin,int end) {
		SpannableString spanString = new SpannableString(text);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(36);
		spanString.setSpan(span, begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(spanString);
	}

	/**
	 * 粗体，斜体
	 */
	public static void addStyleSpan(TextView textView,String text,int begin,int end) {
		SpannableString spanString = new SpannableString(text);
		StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);
		spanString.setSpan(span, begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
