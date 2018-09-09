package com.yusen.logistics.utils;

import android.content.Context;

import com.yusen.logistics.base.ConstantConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {

	private Context context;

	public FileService(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 写入文件到SD卡
	 * 
	 * @throws IOException
	 */
	public void saveToSD(String fileNameStr, String fileContentStr)
			throws IOException {

		// 备注:Java File类能够创建文件或者文件夹，但是不能两个一起创建
		// File file = new File("/mnt/sdcard/myfiles");
		// if(!file.exists())
		// {
		// file.mkdirs();
		// }
		File file = new File(ConstantConfig.PATH_CACHE_AREA);
		if (file.exists()) {
			file.delete();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		File file1 = new File(file, fileNameStr);
		FileOutputStream fos = new FileOutputStream(file1);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	/**
	 * 保存文件到手机
	 * 
	 * @param fileNameStr
	 *            文件名
	 * @param fileContentStr
	 *            文件内容
	 * @throws IOException
	 */
	public void save(String fileNameStr, String fileContentStr)
			throws IOException {
		// 私有操作模式：创建出来的文件只能被本应用访问，其它应用无法访问该文件，另外采用私有操作模式创建的文件，写入文件中的内容会覆盖原文件的内容
		FileOutputStream fos = context.openFileOutput(fileNameStr,
				context.MODE_PRIVATE);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	public void saveAppend(String fileNameStr, String fileContentStr)
			throws IOException {
		// 追加操作模式:不覆盖源文件，但是同样其它应用无法访问该文件
		FileOutputStream fos = context.openFileOutput(fileNameStr,
				context.MODE_APPEND);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	public void saveReadable(String fileNameStr, String fileContentStr)
			throws IOException {
		// 读取操作模式:可以被其它应用读取，但不能写入
		FileOutputStream fos = context.openFileOutput(fileNameStr,
				context.MODE_WORLD_READABLE);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	public void saveWriteable(String fileNameStr, String fileContentStr)
			throws IOException {
		// 写入操作模式:可以被其它应用写入，但不能读取
		FileOutputStream fos = context.openFileOutput(fileNameStr,
				context.MODE_WORLD_WRITEABLE);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	public void saveReadWriteable(String fileNameStr, String fileContentStr)
			throws IOException {
		// 读写操作模式:可以被其它应用读写
		FileOutputStream fos = context.openFileOutput(fileNameStr,
				context.MODE_WORLD_READABLE + context.MODE_WORLD_WRITEABLE);
		fos.write(fileContentStr.getBytes());
		fos.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileNamestr
	 *            文件名
	 * @return
	 * @throws IOException
	 */
	public String read(String fileNamestr) throws IOException {
		
		File file = new File(fileNamestr);
		if(!file.exists())return null;
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		byte[] data = bos.toByteArray();
		return new String(data);
	}

//	/**
//	 * 读取某个特定文件夹下的所有文件-没有字文件夹
//	 */
//	public static ArrayList<FileBean> readfile(String filepath)
//			throws FileNotFoundException, IOException {
//		ArrayList<FileBean> files = new ArrayList<FileBean>();
//		try {
//			File file = new File(filepath);
//			String[] filelist = file.list();
//			for (String filename : filelist) {
//				FileBean fb = new FileBean();
//				fb.setName(filename);
//				files.add(fb);
//			}
//		} catch (Exception e) {
//			System.out.println("readfile()   Exception:" + e.getMessage());
//		}
//		return files;
//	}

	/**
	 * 删除文件夹下的所有文件
	 * 
	 * @param oldPath
	 */
	public void deleteFile(File oldPath) {
		if (!oldPath.exists()) {
			oldPath.mkdirs();
			return;
		}
		if (oldPath.isDirectory()) {
			System.out.println(oldPath + "是文件夹--");
			File[] files = oldPath.listFiles();
			for (File file : files) {
				deleteFile(file);
			}
		} else {
			oldPath.delete();
		}
	}

	public void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {

		// 新建目标目录

		(new File(targetDir)).mkdirs();
		// 获取源文件夹当下的文件或目录
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
				String dir1 = sourceDir + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();

				copyDirectiory(dir1, dir2);
			}
		}

	}

	public void copyFile(File sourcefile, File targetFile) throws IOException {

		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourcefile);
		BufferedInputStream inbuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream out = new FileOutputStream(targetFile);
		BufferedOutputStream outbuff = new BufferedOutputStream(out);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len = 0;
		while ((len = inbuff.read(b)) != -1) {
			outbuff.write(b, 0, len);
		}

		// 刷新此缓冲的输出流
		outbuff.flush();

		// 关闭流
		inbuff.close();
		outbuff.close();
		out.close();
		input.close();

	}
}
