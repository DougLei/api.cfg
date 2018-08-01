package com.king.tooth.util;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.sys.SysFile;

/**
 * 文件操作工具类
 * @author DougLei
 */
public class FileUtil {
	
	/**
	 * 系统保存文件的方式：db[存储在数据库]，service[存储在系统服务器上]，默认是service
	 * @see api.platform.file.properties
	 */
	public static final String fileSaveType;
	/**
	 * 是否保存到数据库
	 */
	public static final boolean saveToDB;
	/**
	 * 是否保存到服务器
	 */
	public static final boolean saveToService;
	/**
	 * 系统保存文件在服务器上的路径，只有file.save.type=service的时候才有效
	 * 要配置绝对路径，如果不配置，则默认是存储在项目部署的file文件夹下
	 * @see api.platform.file.properties
	 */
	public static final String fileSavePath;
	
	/**
	 * 上传文件的最大大小，单位kb，默认10240KB，即10M
	 * @see api.platform.file.properties
	 */
	public static final long fileMaxSize;
	
	/**
	 * 静态块，从配置文件读取，并初始化属性值
	 */
	static{
		fileSaveType = ResourceHandlerUtil.initConfValue("file.save.type", SysFile.service);
		saveToDB = SysFile.db.equals(fileSaveType);
		saveToService = SysFile.service.equals(fileSaveType);
		
		fileSavePath = ResourceHandlerUtil.initConfValue("file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file") + File.separator;
		
		fileMaxSize = Long.valueOf(ResourceHandlerUtil.initConfValue("file.max.size", "10240"));
	}
	//--------------------------------------------------------------------
	
	/**
	 * 获取文件编码
	 * @return
	 */
	public static String getFileCode() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 获取文件的路径
	 * @return
	 */
	public static String getFileDir() {
		return DateUtil.formatDate(new Date());
	}
}
