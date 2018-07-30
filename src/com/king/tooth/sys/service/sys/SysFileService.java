package com.king.tooth.sys.service.sys;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 文件service
 * @author DougLei
 */
public class SysFileService extends AbstractService{
	
	/**
	 * 系统保存文件的方式：db[存储在数据库]，service[存储在系统服务器上]，默认是db
	 * @see api.platform.file.properties
	 */
	private static final String fileSaveType;
	/**
	 * 系统保存文件在服务器上的路径，只有file.save.type=service的时候才有效
	 * 要配置绝对路径，如果不配置，则默认是存储在项目部署的file文件夹下
	 * @see api.platform.file.properties
	 */
	private static final String fileSavePath;
	
	/**
	 * 静态块，从配置文件读取，并初始化属性值
	 */
	static{
		fileSaveType = ResourceHandlerUtil.initConfValue("file.save.type", "db");
		fileSavePath = ResourceHandlerUtil.initConfValue("file.save.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "file");
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @return
	 */
	public Object upload(HttpServletRequest request){
		if(request.getContentLength() <= 0){
			return "没有获得要操作的任何数据";
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload servletUpload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		try {
			fileList = servletUpload.parseRequest(request);
			if(uploadFileIsEmpty(fileList)){
				return "没有获得要操作的文件";
			}else{
				SysFile file;
				
				
			}
		} catch (FileUploadException e) {
			return ExceptionUtil.getErrMsg(e);
		} finally{
			if(fileList != null){
				fileList.clear();
			}
		}
		return null;
	}
	// 验证上传的文件是否为空
	private boolean uploadFileIsEmpty(List<FileItem> fileList){
		boolean fileIsEmpty = true; 
		for (FileItem file : fileList) {
			if(!file.isFormField()){
				fileIsEmpty = false;
				break;
			}
		}
		return fileIsEmpty;
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @return
	 */
	public Object download(HttpServletRequest request){
		return null;
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @return
	 */
	public Object delete(HttpServletRequest request){
		return null;
	}
}
