package com.harlan.lib.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * 
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2014年3月17日
 * @param <T>
 */
public abstract class LBaseAdapter<T extends List<?>> extends BaseAdapter {
	
	T t;
	Context context;
	 
	public T getT() {
		return t;
	}
	public LBaseAdapter<T> setT(T t) {
		this.t = t;
		return this;
	}
	public Context getContext() {
		return context;
	}
	public LBaseAdapter<T> setContext(Context context) {
		this.context = context;
		return this;
	}
	
	@Override
	public int getCount() {
		return t != null ? t.size(): 0;
	}
	
	@Override
	public Object getItem(int position) {
		return t != null ?  t.get(position): null;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
