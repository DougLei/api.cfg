package com.king.tooth.util;

import java.io.File;
import java.util.UUID;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.sys.SysFile;

/**
 * 文件操作工具类
 * @author DougLei
 */
public class FileUtil {
	
	/**
	 * 系统保存文件的方式：service[存储在系统服务器上]，默认是service，目前也只支持保存到服务器上
	 * @see api.platform.file.properties
	 */
	public static final String saveType;
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
	/** 系统导入文件时，在服务器上的保存路径，同fileSavePath */
	public static final String importFileSavePath;
	/** 系统导入文件的模版，在服务器上的保存路径，同fileSavePath */
	public static final String importFileTemplateSavePath;
	/** 系统导出文件时，在服务器上的保存路径，同fileSavePath */
	public static final String exportFileSavePath;
	
	/**
	 * 上传文件的最大大小，单位kb，默认10240KB，即10M
	 * @see api.platform.file.properties
	 */
	public static final long fileMaxSize;
	
	/**
	 * 静态块，从配置文件读取，并初始化属性值
	 */
	static{
		saveType = ResourceHandlerUtil.initConfValue("file.save.type", SysFile.SAVE_TYPE_SERVICE);
		fileSavePath = ResourceHandlerUtil.initConfValue("file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file" + File.separator + "upload") + File.separator;
		importFileSavePath = ResourceHandlerUtil.initConfValue("import.file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file" + File.separator + "import") + File.separator;
		importFileTemplateSavePath = ResourceHandlerUtil.initConfValue("import.file.template.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file" + File.separator + "importTemplate") + File.separator;
		exportFileSavePath = ResourceHandlerUtil.initConfValue("export.file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file" + File.separator + "export") + File.separator;
		fileMaxSize = Long.valueOf(ResourceHandlerUtil.initConfValue("file.max.size", "10240"));
		
		saveToService = SysFile.SAVE_TYPE_SERVICE.equals(saveType);
	}
	//--------------------------------------------------------------------
	
	/**
	 * 获取文件编码
	 * <p>32位uuid，即在系统中文件的编号名，防止乱码</p>
	 * @return
	 */
	public static String getFileCode() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
