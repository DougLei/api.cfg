package com.douglei.tools.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author DougLei
 */
public class IOUtil {
	
	/**
	 * 复制输入流到输出流
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		try(BufferedInputStream reader=new BufferedInputStream(in)){
			byte[] b = new byte[1024];
			short len;
			while((len =(short) reader.read(b)) > 0) {
				out.write(b, 0, len);
			}
		}
	}
	
	/**
	 * 复制文件到输出流
	 * @param srcFile
	 * @param out
	 * @throws IOException
	 */
	public static void copy(File srcFile, OutputStream out) throws IOException {
		copy(new FileInputStream(srcFile), out);
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	// 创建文件夹
	private static void mkdirs(File folder) {
		if(!folder.exists())
			folder.mkdirs();
	}
	
	/**
	 * 复制文件
	 * @param srcFile
	 * @param destFolder 这个是要复制的目标文件夹, 不是文件, 所以不要出现文件名
	 * @throws IOException 
	 */
	public static void copyFile(File srcFile, File destFolder) throws IOException {
		mkdirs(destFolder);
		try(BufferedOutputStream writer=new BufferedOutputStream(new FileOutputStream(new File(destFolder.getAbsolutePath()+File.separatorChar+srcFile.getName())))){
			copy(srcFile, writer);
		}
	}
	
	/**
	 * 复制文件夹
	 * @param srcFolder
	 * @param destFolder 这个是要复制的目标文件夹, 不是文件, 所以不要出现文件名
	 * @throws IOException 
	 */
	public static void copyFolder(File srcFolder, File destFolder) throws IOException {
		if(srcFolder.isFile()) {
			copyFile(srcFolder, destFolder);
		}else if(srcFolder.isDirectory()){
			mkdirs(destFolder);
			for (File file : srcFolder.listFiles()) {
				if(file.isFile()) {
					copyFile(file, destFolder);
				}else {
					copyFolder(file, new File(destFolder.getAbsolutePath()+File.separatorChar+file.getName()));
				}
			}
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------
	// zip压缩文件
	private static void zipFile(String parentName, File srcFile, ZipOutputStream zipWriter) throws IOException {
		try(BufferedInputStream reader=new BufferedInputStream(new FileInputStream(srcFile))){
			byte[] b = new byte[1024];
			short len;
			zipWriter.putNextEntry(new ZipEntry((parentName==null?srcFile.getName():(parentName+File.separatorChar+srcFile.getName()))));
			while((len =(short) reader.read(b)) > 0) {
				zipWriter.write(b, 0, len);
			}
			zipWriter.closeEntry();
		}
	}
	
	/**
	 * zip压缩文件
	 * @param srcFile
	 * @param destZipFile 这个是进行zip压缩后的文件绝对路径, 必须有相关的文件名, 且以.zip结尾
	 * @throws IOException 
	 */
	public static void zipFile(File srcFile, File destZipFile) throws IOException {
		mkdirs(destZipFile.getParentFile());
		try(ZipOutputStream zipWriter=new ZipOutputStream(new FileOutputStream(destZipFile))){
			zipFile(null, srcFile, zipWriter);
		}
	}
	
	/**
	 * zip压缩文件夹
	 * @param srcFolder
	 * @param destZipFile 这个是进行zip压缩后的文件绝对路径, 必须有相关的文件名, 且以.zip结尾
	 * @throws IOException 
	 */
	public static void zipFolder(File srcFolder, File destZipFile) throws IOException {
		if(srcFolder.isFile()) {
			zipFile(srcFolder, destZipFile);
		}else if(srcFolder.isDirectory()){
			try(ZipOutputStream zipWriter = new ZipOutputStream(new FileOutputStream(destZipFile))){
				for (File file : srcFolder.listFiles()) {
					if(file.isFile()) {
						zipFile(null, file, zipWriter);
					}else {
						recursiveZipFolder(zipWriter, file, file.getName());
					}
				}
			}
		}
	}
	
	// 递归压缩文件夹
	private static void recursiveZipFolder(ZipOutputStream zipWriter, File srcFolder, String folderName) throws IOException {
		for (File file : srcFolder.listFiles()) {
			if(file.isFile()) {
				zipFile(folderName, file, zipWriter);
			}else {
				recursiveZipFolder(zipWriter, file, folderName + File.separatorChar +file.getName());
			}
		}
	}
}
