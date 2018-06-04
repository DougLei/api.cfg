package com.king.tooth.sys.entity.cfg.database;

import java.io.File;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库的文件对象
 * @see CfgDynamicDatabaseData使用到
 * <p>sqlserver的数据/日志文件</p>
 * <p>oracle的表空间/临时表空间文件</p>
 * @author DougLei
 */
public class DBFile {
	/**
	 * 数据文件名称
	 */
	private String name;
	/**
	 * 数据文件全路径
	 * 不包括文件名和文件后缀
	 */
	private String filepath;
	
	/**
	 * 数据文件的初始大小
	 * 默认初始大小为100
	 * (单位：mb)
	 */
	private int size;
	/**
	 * 数据文件的增量
	 * 默认大小是200
	 * (单位：mb)
	 */
	private int fileGrowth;
	/**
	 * 数据文件增长的最大值
	 * 默认大小是5120
	 * (单位：mb)
	 * 0代表不做限制
	 */
	private int maxSize;
	
	/**
	 * 临时数据文件/日志文件的初始大小
	 * 默认大小为50
	 * (单位：mb)
	 */
	private int tmpSize;
	/**
	 * 临时数据文件/日志文件的增量
	 * 默认大小是50
	 * (单位：mb)
	 */
	private int tmpFileGrowth;
	/**
	 * 临时数据文件/日志文件的增长的最大值
	 * 默认大小为1024
	 * (单位：mb)
	 * 0代表不做限制
	 */
	private int tmpMaxSize;
	
	/**
	 * 无参构造函数
	 * <pre>
	 * 	如果使用无参构造函数创建对象，创建出来的对象必须调用以下方法
	 * 		setName
	 * </pre>
	 */
	public DBFile() {
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 构造函数
	 * <p>其他参数赋值，可以通过调用setXXX方法赋值[除去构造函数中参数对应的属性不需要调用setXXX方法]</p>
	 * @param name
	 */
	public DBFile(String name){
		this.name = name;
	}

	/**
	 * 获取该对象的json字符串
	 * @return
	 */
	public String toJsonString(){
		return JsonUtil.toJsonString(this, false);
	}
	
	public String getName() {
		return name;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setFileGrowth(int fileGrowth) {
		this.fileGrowth = fileGrowth;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public void setTmpSize(int tmpSize) {
		this.tmpSize = tmpSize;
	}
	public void setTmpFileGrowth(int tmpFileGrowth) {
		this.tmpFileGrowth = tmpFileGrowth;
	}
	public void setTmpMaxSize(int tmpMaxSize) {
		this.tmpMaxSize = tmpMaxSize;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getFilepath() {
		if(StrUtils.isEmpty(filepath)){
			filepath = SysConfig.getSystemConfig("db.default.filepath") + File.separator + name + File.separator;
		}
		// 拼装数据库文件路径：基础路径/数据库实例名/，并判断该路径下的文件夹是否都存在，不存在则创建
		File dir = new File(filepath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return filepath;
	}
	public int getSize() {
		if(size < 1){
			size = Integer.valueOf(SysConfig.getSystemConfig("db.size"));
		}
		return size;
	}
	public int getFileGrowth() {
		if(fileGrowth < 1){
			fileGrowth = Integer.valueOf(SysConfig.getSystemConfig("db.file.growth"));
		}
		return fileGrowth;
	}
	public int getMaxSize() {
		if(maxSize < 0){
			maxSize = Integer.valueOf(SysConfig.getSystemConfig("db.max.size"));
		}
		return maxSize;
	}
	public int getTmpSize() {
		if(tmpSize < 1){
			tmpSize = Integer.valueOf(SysConfig.getSystemConfig("db.tmp.size"));
		}
		return tmpSize;
	}
	public int getTmpFileGrowth() {
		if(tmpFileGrowth < 1){
			tmpFileGrowth = Integer.valueOf(SysConfig.getSystemConfig("db.tmp.file.growth"));
		}
		return tmpFileGrowth;
	}
	public int getTmpMaxSize() {
		if(tmpMaxSize < 0){
			tmpMaxSize = Integer.valueOf(SysConfig.getSystemConfig("db.tmp.max.size"));
		}
		return tmpMaxSize;
	}
}
