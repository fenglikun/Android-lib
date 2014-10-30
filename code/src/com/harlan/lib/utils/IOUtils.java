package com.harlan.lib.utils;

import java.io.*;
import java.util.*;

/** IO 工具类 */
public class IOUtils {
	/**
	 * 获取目录下的全部文件
	 * 
	 * @param dir
	 * @return 文件集合
	 */
	public static List<File> listFile(File dir) {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		return new ArrayList<File>(Arrays.asList(files));
	}

	/**
	 * 获取目录的全部文件, 指定扩展名的文件
	 * 
	 * @param dir
	 * @return
	 */
	public static List<File> listFile(File dir, final String ext) {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(ext);
			}
		});
		return new ArrayList<File>(Arrays.asList(files));
	}

	/**
	 * 递归获取目录的全部文件，包括子目录下的文件
	 * 
	 * @param dir
	 * @return
	 */
	public static List<File> listAll(File dir) {
		List<File> all = listFile(dir);
		File[] subs = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for (File sub : subs) {
			all.addAll(listAll(sub));
		}
		return all;
	}

	public static void deleteAll(File dir) {

		File[] subs = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for (File sub : subs) {
			sub.delete();
		}

	}

	/**
	 * 递归获取目录的全部文件, 指定扩展名的文件
	 * 
	 * @param dir
	 * @return
	 */
	public static List<File> listAll(File dir, String ext) {
		List<File> all = listFile(dir, ext);
		File[] subs = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for (File sub : subs) {
			all.addAll(listAll(sub, ext));
		}
		return all;
	}

	/**
	 * 复制文件 把from指定的文件复制到to指定的位置
	 */
	public static void cp(String from, String to) throws IOException {
		cp(new File(from), new File(to));// 调用本类重载的方法cp
		// Thread.sleep(millis);
		// Thread t = Thread.currentThread();
	}

	/**
	 * 复制文件
	 */
	public static void cp(File from, File to) throws IOException {
		cp(new FileInputStream(from), new FileOutputStream(to));// 调用本类重载的方法cp
	}

	/**
	 * 复制文件 真正实现复制的方法 in = new FileInputStream(new File(from)); out = new
	 * FileOutputStream(new File(to));
	 */
	public static void cp(InputStream in, OutputStream out) throws IOException {
		// 1K byte 的缓冲区!
		byte[] buf = new byte[1024];
		int count;
		while ((count = in.read(buf)) != -1) {
			System.out.println(count);
			out.write(buf, 0, count);
		}
		in.close();
		out.close();
	}

	/**
	 * 从流中读取一行文本, 读取到一行的结束为止
	 * 
	 * @param in
	 * @return 一行文本
	 */
	public String readLine(InputStream in, String charset) throws IOException {
		byte[] buf = {};// buf.length = 0;
		int b;
		while (true) {
			b = in.read();
			if (b == '\n' || b == '\r' || b == -1) {// 编码是否是回车换行
				break;
			}

			// buf=Arrays.copyOf(buf, buf.length+1);//jdk1.6
			buf[buf.length - 1] = (byte) b;
		}
		if (buf.length == 0 && b == -1)
			return null;
		return new String(buf, charset);
	}

	/**
	 * 读取文件的全部内容到一个byte数组 可以缓存一个"小"文件到堆内存中
	 */
	public static byte[] read(String filename) throws IOException {
		return read(new File(filename));
	}

	/**
	 * 读取文件的全部内容到一个byte数组 可以缓存一个"小"文件到堆内存中
	 */
	public static byte[] read(File file) throws IOException {
		// 用RAF打开文件
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		// 安装文件的长度开辟 缓冲区数组(byte数组)
		byte[] buf = new byte[(int) raf.length()];
		// 读取文件的缓冲区
		raf.read(buf);
		// 关闭文件(RAF)
		raf.close();
		// 返回缓冲区数组引用.
		return buf;
	}

	/**
	 * 读取文件的全部内容到一个byte数组 可以缓存一个"小"文件到堆内存中 如: 文件内容: ABC中 读取为: {41, 42, 43, d6,
	 * d0}
	 */
	public static byte[] read(InputStream in) throws IOException {
		byte[] ary = new byte[in.available()];
		in.read(ary);
		in.close();
		return ary;
	}

	/**
	 * 连接byte 数组的全部内容为字符串, 以hex(十六进制)形式连接 如: 数组{0x41, 0x42, 0x43, 0xd6, 0xd0}
	 * ABC中 结果: "[41, 42, 43, d6, d0]"
	 */
	public static String join(byte[] ary) {
		if (ary == null || ary.length == 0)
			return "[]";
		StringBuilder buf = new StringBuilder();
		buf.append("[").append(
				leftPad(Integer.toHexString(ary[0] & 0xff), '0', 2));
		for (int i = 1; i < ary.length; i++) {
			String hex = Integer.toHexString(ary[i] & 0xff);
			buf.append(",").append(leftPad(hex, '0', 2));
		}
		buf.append("]");
		return buf.toString();
	}

	public static String toBinString(byte[] ary) {
		if (ary == null || ary.length == 0)
			return "[]";
		StringBuilder buf = new StringBuilder();
		buf.append("[").append(
				leftPad(Integer.toBinaryString(ary[0] & 0xff), '0', 8));
		for (int i = 1; i < ary.length; i++) {
			String hex = Integer.toBinaryString(ary[i] & 0xff);
			buf.append(",").append(leftPad(hex, '0', 8));
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * 实现leftPad功能, 对字符串实现左填充
	 * 
	 * @param str
	 *            被填充字符串: 5
	 * @param ch
	 *            填充字符: #
	 * @param length
	 *            填充以后的长度: 8
	 * @return "#######5"
	 */
	public static String leftPad(String str, char ch, int length) {
		if (str.length() == length) {
			return str;
		}
		char[] chs = new char[length];
		Arrays.fill(chs, ch);
		System.arraycopy(str.toCharArray(), 0, chs, length - str.length(),
				str.length());
		return new String(chs);
	}

	/**
	 * 将text追加到文件 filename的尾部 使用系统默认文本编码
	 */
	public static void println(String filename, String text) throws IOException {
		println(new File(filename), text);
	}

	public static void println(File file, String text) throws IOException {
		OutputStream out = new FileOutputStream(file, true);
		println(out, text);
		out.close();
	}

	/**
	 * 向流中输出一行字符串, 使用默认编码 不关闭流
	 * 
	 * @param out
	 *            目标流
	 * @param text
	 *            文本
	 * @throws IOException
	 */
	public static void println(OutputStream out, String text)
			throws IOException {
		out.write(text.getBytes());
		out.write('\n');
	}

	/**
	 * 向流中输出一行字符串, 使用指定的编码 不关闭流
	 * 
	 * @param out
	 *            目标流
	 * @param text
	 *            文本
	 * @param charset
	 *            指定的编码
	 * @throws IOException
	 */
	public static void println(OutputStream out, String text, String charset)
			throws IOException {
		out.write(text.getBytes(charset));
		out.write('\n');
	}

	/**
	 * 序列化
	 * 
	 * @param obj
	 *            待序列化的对象
	 * @return obj序列化后的byte数组
	 */
	public static byte[] serializing(Serializable obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.close();
		return bos.toByteArray();
	}

	/**
	 * 反序列化
	 * 
	 * @param array
	 *            字节数组
	 * @return Object 入口参数数组反序列化后的对象
	 * @throws ClassNotFoundException
	 */
	public static Object unSerializing(byte[] array) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();
		return obj;
	}

	/**
	 * 深克隆(深层复制)
	 * 
	 * @param obj
	 *            待复制的对象
	 * @return Object obj对象的副本
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object clone(Serializable obj) throws IOException,
			ClassNotFoundException {
		return unSerializing(serializing(obj));
	}

	/**
	 * 切分文件, 如: file.dat 切分为 file.dat.0, file.dat.1 ...
	 * 
	 * @param file
	 * @param size
	 *            大小, 以KByte为单位
	 */
	public static void split(String file, int size) throws IOException {
		if (size <= 0) {
			throw new IllegalArgumentException("搞啥呀!");
		}
		int idx = 0;// 文件的序号
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file
				+ "." + idx++));
		int b;
		int count = 0;
		while ((b = in.read()) != -1) {
			out.write(b);
			count++;
			if (count % (size * 1024) == 0) {
				out.close();
				out = new BufferedOutputStream(new FileOutputStream(file + "."
						+ idx++));
			}
		}
		in.close();
		out.close();
	}

	/**
	 * 将文件进行连接
	 * 
	 * @param filename
	 *            是第一个文件名,如:file.dat.0
	 */
	public static void join(String file) throws IOException {
		String filename = file.substring(0, file.lastIndexOf("."));
		String num = file.substring(file.lastIndexOf(".") + 1);
		int idx = Integer.parseInt(num);
		OutputStream out = new FileOutputStream(filename);
		File f = new File(filename + "." + idx++);
		while (f.exists()) {
			InputStream in = new FileInputStream(f);
			cp(in, out);
			in.close();
			f = new File(filename + "." + idx++);
		}
		out.close();
	}
}
