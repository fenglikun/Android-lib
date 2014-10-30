package com.harlan.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;

/**
 * 文件相关操作
 * 
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2014年2月21日
 */
public class HFile {

	/**
	 * 根据文件路径 递归创建文件
	 * 
	 * @param file
	 */
	public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);
		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static boolean deleteFile(String path) {
		boolean bl;
		File file = new File(path);
		if (file.exists()) {
			bl = file.delete();
		} else {
			bl = false;
		}
		return bl;
	}

	// 复制文件
	public static void copyFile(String sourcePath, String toPath) {
		File sourceFile = new File(sourcePath);
		File targetFile = new File(toPath);
		createDipPath(toPath);
		try {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// 新建文件输入流并对它进行缓冲
				inBuff = new BufferedInputStream(
						new FileInputStream(sourceFile));

				// 新建文件输出流并对它进行缓冲
				outBuff = new BufferedOutputStream(new FileOutputStream(
						targetFile));

				// 缓冲数组
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// 刷新此缓冲的输出流
				outBuff.flush();
			} finally {
				// 关闭流
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) {
		try {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// 新建文件输入流并对它进行缓冲
				inBuff = new BufferedInputStream(
						new FileInputStream(sourceFile));

				// 新建文件输出流并对它进行缓冲
				outBuff = new BufferedOutputStream(new FileOutputStream(
						targetFile));

				// 缓冲数组
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// 刷新此缓冲的输出流
				outBuff.flush();
			} finally {
				// 关闭流
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 
	 * @param srcFileName
	 * @param destFileName
	 * @param srcCoding
	 * @param destCoding
	 * @throws IOException
	 */
	public static void copyFile(File srcFileName, File destFileName,
			String srcCoding, String destCoding) throws IOException {// 把文件转换为GBK文件
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					srcFileName), srcCoding));
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(destFileName), destCoding));
			char[] cbuf = new char[1024 * 5];
			int len = cbuf.length;
			int off = 0;
			int ret = 0;
			while ((ret = br.read(cbuf, off, len)) > 0) {
				off += ret;
				len -= ret;
			}
			bw.write(cbuf, 0, off);
			bw.flush();
		} finally {
			if (br != null)
				br.close();
			if (bw != null)
				bw.close();
		}
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public static void del(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	public static boolean copyTxtToSDCard(Context context, int fileForm,
			String toPath) {
		boolean copyResult = true;
		try {
			// File dir = new File(toPath);
			// 如果/sdcard/dictionary目录中存在，创建这个目录

			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）

			// 获得封装dictionary.db文件的InputStream对象
			createDipPath(toPath);
			InputStream is = context.getResources().openRawResource(fileForm);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			if (sb != null && sb.length() > 0) {
				OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(new File(toPath)), "GBK");
				fileOutputStream.write(sb.toString());
				fileOutputStream.close();
				copyResult = true;
			} else {
				copyResult = false;
			}
			br.close();
			isr.close();
			is.close();
		} catch (Exception e) {
			copyResult = false;
		}
		return copyResult;
	}

	
	File file;

	/*** 获取文件大小 ***/
	public static long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	/*** 获取文件夹大小 ***/
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		List<File> list = IOUtils.listAll(f);
		for (int i = 0; i < list.size(); i++) {
			size += list.get(i).length();
		}
		return size;
	}

	/**
	 * 将文件显示成友好形势
	 * 
	 * @param fileS
	 * @return
	 */
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 递归求取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public static long getlist(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean deleteDirectory(String dirPath) {//
		boolean flag = false;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!dirPath.endsWith(File.separator)) {
			dirPath = dirPath + File.separator;
		}
		File dirFile = new File(dirPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
		for (int i = 0; i < files.length; i++) {// 循环遍历删除文件夹下的所有文件(包括子目录)
			if (files[i].isFile()) {// 删除子文件
				flag = deleteFile(files[i].getAbsolutePath());
				System.out.println(files[i].getAbsolutePath() + " 删除成功");
				if (!flag)
					break;// 如果删除失败，则跳出
			} else {// 运用递归，删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;// 如果删除失败，则跳出
			}
		}
		if (!flag) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 将文件保存成文件
	 * 
	 * @param path
	 *            保存路径
	 * @param content
	 * @param 是否追加
	 */
	public static void saveTxtToFile(String path, String content, boolean append) {
		File file = new File(path);
		if (!file.exists())
			createDipPath(path);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append)));
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

	public static String readTxtFile(String filePath) {
		StringBuffer  sb = new StringBuffer();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}finally{
			
		}
		return sb.toString();
	}
	/**
	 * 得到文件的缩略图
	 * @param imgPath
	 * @return
	 */
	public static String getSmallImg (String imgPath ){
		String newImg = imgPath.substring(0, imgPath.lastIndexOf(".")) + "_s"  + imgPath.substring(imgPath.lastIndexOf("."), imgPath.length());
		return newImg;
	}
}
