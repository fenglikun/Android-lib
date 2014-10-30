package com.harlan.lib.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 
 * Description:使用JavaBean封装上传文件数据 
 * FormFile.java Create on 2012-6-15 上午11:09:51
 * @author Cosmo
 * @version 1.0
 */
public class FormFile {
	private byte[] data;// 上传文件数据
	private InputStream instream;
	private File file;
	private String filname;// 文件名
	private String parameterName;// 请求参数名
	private String contentType = "application/octet-stream";// 内容类型

	/**
	 * 上传小文件，把文件数据先读入内存
	 * 
	 * @param filname
	 * @param data
	 * @param parameterName
	 * @param contentType
	 */

	public FormFile(String filname, byte[] data, String parameterName,
		String contentType) {
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if (contentType != null)
			this.contentType = contentType;
	}
	

	/**
	 * 上传大文件，一边读文件数据一边上传
	 * 
	 * @param filname
	 * @param file
	 * @param parameterName
	 * @param contentType
	 */

	public FormFile(String filname, File file, String parameterName,
			String contentType) {
		this.filname = filname;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.instream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return instream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilname() {
		return filname;
	}

	public void setFilname(String filname) {
		this.filname = filname;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}