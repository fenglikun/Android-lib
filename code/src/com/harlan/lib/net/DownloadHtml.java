package com.harlan.lib.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.harlan.lib.utils.HFile;
import com.harlan.lib.utils.HLog;
import com.harlan.lib.utils.MD5;



/**
 * 下载HTML页面到本地，并下载界面中涉及到的图片
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2014年2月25日
 */

public class DownloadHtml {
	
	/**
	 * 得到网页内容
	 * @param httpUrl
	 * @return
	 */
	public static String getWebContent(String httpUrl) {
		HLog.i("DownloadHtml", "getWebContent httpURL ：" + httpUrl);
		StringBuffer sb = new StringBuffer();
		try {
			java.net.URL url = new java.net.URL(httpUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) { // Report any errors that arise
			HLog.e("DownloadHtml", e.getMessage());
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		/*try {
			List<String> images = getImageUrl("http://webapp.meirenu.com/webapp/webpad/news.aspx?id=492",getWebContent("http://webapp.meirenu.com/webapp/webpad/news.aspx?id=492"));
			for(String image: images){
				System.out.println(image);
				URL url = new URL(image);
				System.out.println(url.getPath());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}*/
		
		download("http://webapp.meirenu.com/webapp/webpad/news.aspx?id=492", "D:/download");
	}
	
	public static boolean download(String htmlUrl,String saveDir){
		String htmlContent = getWebContent(htmlUrl);
		if(htmlContent != null && htmlContent.length() > 0){
			try {
				List<String> images = getImageUrl(saveDir,htmlUrl, htmlContent);
				for (int i = 0; i < images.size(); i++) {
					String image = images.get(i);
					URL url = new URL(image);
					String imageLocalPath = saveDir.substring(0, saveDir.length() -1) + url.getPath();
					httpDownload(image, imageLocalPath);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 文件下载
	 * @param httpUrl
	 * @param saveFile
	 * @return
	 */
	public static boolean httpDownload(String httpUrl, String saveFile) {
		HFile.createDipPath(saveFile);
		// 下载网络文件
		int byteread = 0;
		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(saveFile);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void saveTxtFile(String path,String content){
		File file = new File(path);
		if (!file.exists())
			HFile.createDipPath(path);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	/**
	 * 得到网页中的图片地址。
	 * @param urlStr
	 * @param htmlContent
	 * @return
	 * @throws MalformedURLException
	 */
	public static List<String> getImageUrl(String saveDir, String urlStr,
			String htmlContent) throws MalformedURLException {
		URL url = new URL(urlStr);
		String urlPath = url.getProtocol() + "://" + url.getHost();
		List<String> images = new ArrayList<String>();
		String img = "";
		Pattern p_image;
		Matcher m_image;
		String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlContent);
		while (m_image.find()) {
			img = img + "," + m_image.group();
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				String imageUrl = m.group(1);
				if (imageUrl != null && imageUrl.length() > 4) {
					if ("/".equals(imageUrl.substring(0, 1))) {
						htmlContent = htmlContent.replaceAll(imageUrl,
								imageUrl.substring(1, imageUrl.length()));
						imageUrl = urlPath + imageUrl;
					} else if (!"http".equals(imageUrl.substring(0, 4))) {
						imageUrl = urlPath + imageUrl;
					} else {
						url = new URL(imageUrl);
						String newPath = url.getPath();
						newPath = newPath.substring(1, newPath.length());
						htmlContent = htmlContent.replaceAll(imageUrl, newPath);
					}
					images.add(imageUrl);
				}
			}
		}
		String htmlFilePath = saveDir + MD5.md5(urlStr) + ".html";
		saveTxtFile(htmlFilePath, htmlContent);
		return images;
	}
}
