package com.king.tooth.constants;

import java.io.File;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 文件的常量
 * @author DougLei
 */
public class SysFileConstants {

	/** 内置文件类型：1、普通文件 */
	public static final int BUILD_IN_TYPE_NORMAL = 1;
	/** 内置文件类型：2、导入文件 */
	public static final int BUILD_IN_TYPE_IMPORT = 2;
	/** 内置文件类型：3、导入模版文件 */
	public static final int BUILD_IN_TYPE_IMPORT_TEMPLATE = 3;
	/** 内置文件类型：4、导出文件 */
	public static final int BUILD_IN_TYPE_EXPORT = 4;
	
	/** service:存储在系统服务器上 */
	public static final String SAVE_TYPE_SERVICE = "service";
	
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
	/**标识，是否文件上传时，保存文件的路径是系统默认路径，即(项目所在绝对路径/files/upload)*/
	public static final boolean isDefaultFileSavePath;
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
		saveType = ResourceHandlerUtil.initConfValue("file.save.type", SAVE_TYPE_SERVICE);
		fileSavePath = ResourceHandlerUtil.initConfValue("file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "files" + File.separator + "upload") + File.separator;
		isDefaultFileSavePath = fileSavePath.startsWith(SysConfig.WEB_SYSTEM_CONTEXT_REALPATH);
		
		importFileSavePath = ResourceHandlerUtil.initConfValue("import.file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "files" + File.separator + "import") + File.separator;
		importFileTemplateSavePath = ResourceHandlerUtil.initConfValue("import.file.template.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "files" + File.separator + "importTemplate") + File.separator;
		exportFileSavePath = ResourceHandlerUtil.initConfValue("export.file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "files" + File.separator + "export") + File.separator;
		fileMaxSize = Long.valueOf(ResourceHandlerUtil.initConfValue("file.max.size", "10240"));
		
		saveToService = SAVE_TYPE_SERVICE.equals(saveType);
	}
}
