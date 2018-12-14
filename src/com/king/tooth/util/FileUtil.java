package com.king.tooth.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;

/**
 * 文件操作工具类
 * @author DougLei
 */
public class FileUtil {
	
	/**
	 * 获取文件编码
	 * <p>32位uuid，即在系统中文件的编号名，防止乱码</p>
	 * @return
	 */
	public static String getFileCode() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 验证文件目录是否存在
	 * <p>如果不存在，则创建</p>
	 * @param dirPath
	 * @return 
	 */
	public static String validSaveFileDirIsExists(String dirPath) {
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	 * 是否是图片
	 * @param fileSuffix 文件后缀
	 * @return
	 */
	public static boolean isImage(String fileSuffix){
		return isFileFormat(fileSuffix, imageFileSuffixArr);
	}
	private static final String[] imageFileSuffixArr = {"jpg", "jpeg", "png", "gif", "bmp"};
	
	/**
	 * 是否是指定的文件格式
	 * @param fileSuffix
	 * @param suffixes
	 * @return
	 */
	public static boolean isFileFormat(String fileSuffix, String... suffixes) {
		if(suffixes == null || suffixes.length == 0){
			throw new NullPointerException("指定的文件后缀不能为空");
		}
		for (String suffix : suffixes) {
			if(suffix.equalsIgnoreCase(fileSuffix)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 批量备份文件
	 * @param sourceFile
	 * @param targetFile
	 * @param isDeleteSource
	 * @throws IOException 
	 */
	public static void batchCopyfiles(String sourceFile, String targetFile, boolean isDeleteSource) throws IOException {
		batchCopyfiles(new File(sourceFile), new File(targetFile), isDeleteSource);
	}
	
	/**
	 * 批量备份文件
	 * @param sourceFile
	 * @param targetFile
	 * @param isDeleteSource
	 * @throws IOException 
	 */
	public static void batchCopyfiles(File sourceFile, File targetFile, boolean isDeleteSource) throws IOException {
		if(sourceFile.exists()){
			if(sourceFile.isDirectory()){
				File[] files = sourceFile.listFiles();
				File inTargetFile = null;
				for (File file : files) {
					inTargetFile = new File(targetFile.getAbsolutePath() + File.separator + file.getName());
					batchCopyfiles(file, inTargetFile, isDeleteSource);
				}
			}else{
				targetFileDir = new File(targetFile.getParent());
				if(!targetFileDir.exists()){
					targetFileDir.mkdirs();
				}
				FileCopyUtils.copy(sourceFile, targetFile);
			}
			if(isDeleteSource){
				sourceFile.delete();
			}
		}
	}
	private static File targetFileDir = null;
}
