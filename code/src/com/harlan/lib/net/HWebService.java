package com.harlan.lib.net;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.harlan.lib.utils.HLog;

/**
 * WebSerivce网络数据传输入
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2013-10-6
 */
public class HWebService {
	private static final String TAG = "HWebService";
	
	/**
	 * 
	 * @param namespace 命名空间（例如：http://tempuri.org/）
	 * @param url 请求地址
	 * @param method 方法
	 * @param params 参数
	 * @return
	 */
	public static String call(String namespace,String url, String method,Map<String, String> params) {
		String result = null;
		String soapAction = namespace + method;
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(namespace, method);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String,String> entry = iterator.next();
				rpc.addProperty( entry.getKey(),entry.getValue());
			}
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本_
		HLog.i(TAG, "request-->" + method + ":" + rpc.toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(url);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
			if (envelope.getResponse() != null) {
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				result = response.toString();
			} else {
			}
		} catch (Exception e) {
			//e.printStackTrace();
			HLog.e(TAG, e.getMessage());
			result = null;
		}
		HLog.i(TAG, "response-->" + result);
		return result;
	}
}
