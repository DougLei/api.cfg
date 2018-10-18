package com.king.tooth.util;

import java.io.File;
import java.util.UUID;

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
}
