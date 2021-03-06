package com.api.sys.service.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Service;
import com.api.cache.SysContext;
import com.api.constants.EncodingConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.constants.SysFileConstants;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.entity.sys.SysFile;
import com.api.sys.entity.sys.file.ie.AIEFile;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CloseUtil;
import com.api.util.DateUtil;
import com.api.util.ExceptionUtil;
import com.api.util.FileUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.datatype.DataTypeValidUtil;
import com.api.util.hibernate.HibernateUtil;

/**
 * 文件表service
 * @author DougLei
 */
@Service
public class SysFileService extends AService{
	
	private final static List<String> uploadTargetDirPathCache = new ArrayList<String>();
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
		servletUpload.setHeaderEncoding(EncodingConstants.UTF_8);// 设置头的编码格式
		
		List<FileItem> fileList = null;
		List<String> filePathList = null;// 记录文件在服务器上的路径，如果上传出现错误，要删除已经上传的数据
		try {
			fileList = servletUpload.parseRequest(request);
			if(fileList != null && fileList.size() > 0){
				UploadFileInfo uploadFileInfo = uploadFileIsEmpty(fileList);
				if(uploadFileInfo.count == 0){
					return "没有获得要操作的文件";
				}else if(uploadFileInfo.errMsg != null){
					return uploadFileInfo.errMsg;
				}else{
					String uploadDir = validSaveFileDirIsExists(uploadFileInfo.getBuildInType(), uploadFileInfo.uploadTargetDir);
					boolean isNormalImportAndSaveToDefaultPath = (uploadFileInfo.isImport == 0 && SysFileConstants.isDefaultFileSavePath);// 是一般上传的文件，且上传文件保存的路径是系统默认路径(在服务器的files/upload文件夹中)
					filePathList = new ArrayList<String>(uploadFileInfo.count);
					Map<Integer, SysFile> sysfileMap = new HashMap<Integer, SysFile>(uploadFileInfo.count);
					SysFile sysFile;
					Integer index;
					String fileName;
					for (FileItem file : fileList) {
						index = Integer.valueOf(file.getFieldName().substring(file.getFieldName().lastIndexOf("_")+1));
						sysFile = sysfileMap.get(index);
						if(sysFile == null){
							sysFile = new SysFile();
							sysFile.setRefDataId(uploadFileInfo.refDataId);
							sysFile.setBatch(uploadFileInfo.batch);
							sysFile.setIsImport(uploadFileInfo.isImport);
							if(uploadFileInfo.isImport == 1){
								sysFile.setBuildInType(SysFileConstants.BUILD_IN_TYPE_IMPORT);
							}
							sysFile.setCode(FileUtil.getFileCode());
							sysFile.setSaveType(SysFileConstants.saveType);
							sysFile.setOrderCode(index);
							sysfileMap.put(index, sysFile);
						}
						
						if(file.isFormField()){
							if(file.getFieldName().startsWith("secretLevel_") && StrUtils.notEmpty(file.getString()) && DataTypeValidUtil.isInteger(file.getString())){
								sysFile.setSecretLevel(Integer.valueOf(file.getString()));
							}else if(file.getFieldName().startsWith("remark_") && StrUtils.notEmpty(file.getString())){
								sysFile.setRemark(getFormFieldStrValue(file.getString()));
							}else if(file.getFieldName().startsWith("backup01_") && StrUtils.notEmpty(file.getString())){
								sysFile.setBackup01(getFormFieldStrValue(file.getString()));
							}else if(file.getFieldName().startsWith("backup02_") && StrUtils.notEmpty(file.getString())){
								sysFile.setBackup02(getFormFieldStrValue(file.getString()));
							}else if(file.getFieldName().startsWith("orderCode_") && StrUtils.notEmpty(file.getString()) && DataTypeValidUtil.isInteger(file.getString())){
								sysFile.setOrderCode(Integer.valueOf(file.getString()));
							}
						}else{
							if(file.getFieldName().startsWith("file_")){
								fileName = file.getName().substring(file.getName().lastIndexOf("\\")+1);
								
								sysFile.setActName(fileName);
								sysFile.setSizes(file.getSize()+"");
								sysFile.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase());
								if(uploadFileInfo.isImport == 1 && !isSupportFileSuffix(sysFile.getSuffix())){
									return "系统不支持后缀为["+sysFile.getSuffix()+"]的导入文件，系统支持的导入文件后缀包括：" +Arrays.toString(AIEFile.supportFileSuffixArray);
								}
								sysFile.setFileItem(file);
								if(SysFileConstants.saveToService){
									if(StrUtils.isEmpty(uploadFileInfo.uploadTargetDir)){
										sysFile.setSavePath(uploadDir + sysFile.getCode() + "." + sysFile.getSuffix());
									}else{
										sysFile.setSavePath(uploadDir + fileName);
									}
									// 是一般上传的文件，且上传文件保存的路径是系统默认路径(在服务器的files/upload文件夹中) ，且是前端要求的上传文件后，可以直接通过url访问文件的后缀
									if(isNormalImportAndSaveToDefaultPath && FileUtil.isFileFormat(sysFile.getSuffix(), SysContext.getSystemConfig("web.front.end.file.suffix").split(","))){
										sysFile.setUrlPath(sysFile.getSavePath().replace(SysContext.WEB_SYSTEM_CONTEXT_REALPATH, "").replace(File.separator, "/"));
									}
									filePathList.add(sysFile.getSavePath());
								}
							}
						}
					}
					return uploadFile(sysfileMap);
				}
			}else{
				return "没有获得要操作的任何数据[fileList is null]";
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(SysFileConstants.saveToService && filePathList != null && filePathList.size() > 0){
				File tf;
				for (String fp : filePathList) {
					tf = new File(fp);
					if(tf.exists() && tf.isFile()){
						tf.delete();
					}
				}
			}
			return ExceptionUtil.getErrMsg(e);
		} finally{
			if(fileList != null){
				fileList.clear();
			}
			if(SysFileConstants.saveToService && filePathList != null){
				filePathList.clear();
			}
		}
	}
	private String getFormFieldStrValue(String formFieldStrValue) {
		return StrUtils.turnStrEncoding(formFieldStrValue, EncodingConstants.ISO8859_1, EncodingConstants.UTF_8);
	}
	/**
	 * 是否是支持的文件后缀
	 * @param fileSuffix
	 * @return
	 */
	private boolean isSupportFileSuffix(String fileSuffix){
		for (String supportFileSuffix : AIEFile.supportFileSuffixArray) {
			if(supportFileSuffix.equals(fileSuffix)){
				return true;
			}
		}
		return false;
	}

	// 验证上传的文件是否为空
	private UploadFileInfo uploadFileIsEmpty(List<FileItem> fileList) throws UnsupportedEncodingException{
		JSONObject logJsonObject = new JSONObject(fileList.size());
		
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		FileItem fi;
		String fileValue;
		for (int i = 0; i < fileList.size(); i++) {
			fi = fileList.get(i);
			if(fi.isFormField()){
				fileValue = fi.getString(EncodingConstants.UTF_8);
				logJsonObject.put(fi.getFieldName(), fileValue);
				
				if("refDataId".equals(fi.getFieldName())){
					uploadFileInfo.refDataId = fileValue;
					fileList.remove(i--);
				}
				if("batch".equals(fi.getFieldName())){
					uploadFileInfo.batch = fileValue;
					fileList.remove(i--);
				}
				if("isImport".equals(fi.getFieldName())){
					if(DataTypeValidUtil.isInteger(fileValue)){
						uploadFileInfo.isImport = "1".equals(fileValue)?1:0;
					}
					fileList.remove(i--);
				}
				if("uploadTargetDir".equals(fi.getFieldName())){
					uploadFileInfo.uploadTargetDir = fileValue;
					fileList.remove(i--);
				}
			}
		}
		
		if(uploadFileInfo.isImport == 0 && StrUtils.isEmpty(uploadFileInfo.refDataId)){
			uploadFileInfo.errMsg = "上传文件时，关联的业务数据id，即参数名为refDataId的值不能为空";
		}
		if(StrUtils.isEmpty(uploadFileInfo.batch) || uploadFileInfo.batch.length() != 32){
			uploadFileInfo.batch = ResourceHandlerUtil.getIdentity();
		}
		
		if(fileList.size() > 0){
			long fileSizeKB;
			int size = fileList.size();
			for (int i = 0; i < size; i++) {
				fi = fileList.get(i);
				uploadFileInfo.count++;
				logJsonObject.put(fi.getFieldName(), new FileInfo(fi.getName(), (fi.getSize() + " B")));
				
				if(uploadFileInfo.isImport == 0 && SysFileConstants.fileMaxSize != -1){
					fileSizeKB = fi.getSize()/1024;// 由B转换为KB
					if(fileSizeKB > SysFileConstants.fileMaxSize){
						uploadFileInfo.errMsg = "文件的大小为"+(fileSizeKB/1024)+"M，系统限制单个文件的大小不能超过"+(SysFileConstants.fileMaxSize/1024)+"M，请修改后再上传";
					}
				}
			}
		}

		CurrentThreadContext.getReqLogData().getReqLog().setReqData(logJsonObject.toString());// 记录请求体
		return uploadFileInfo;
	}
	// 上传文件的信息
	private class UploadFileInfo{
		public int isImport;// 记录是否是导入操作，如果是导入操作，不做文件大小的验证
		public String errMsg;// 记录错误的信息
		public int count;// 传递的文件数量
		public String refDataId;// 文件关联的业务数据id
		public String batch;// 同一次上传文件批次
		public String uploadTargetDir;// 上传到的目标目录，如果传入了该值，则文件会上传到指定的目录下，而非默认的文件上传目录
		
		/**
		 * 获取内置文件类型
		 * <p>是导入的文件，还是普通上传的文件</p>
		 * @return
		 */
		public int getBuildInType(){
			if(isImport == 0){
				return SysFileConstants.BUILD_IN_TYPE_NORMAL;
			}else{
				return SysFileConstants.BUILD_IN_TYPE_IMPORT;
			}
		}
	}
	// 文件信息
	private class FileInfo{
		@SuppressWarnings("unused")
		public String fileName;// 文件名
		@SuppressWarnings("unused")
		public String size;// 文件大小
		public FileInfo(String fileName, String size) {
			this.fileName = fileName;
			this.size = size;
		}
		@SuppressWarnings("unused")
		public FileInfo() {
		}
	}
	
	/**
	 * 上传文件
	 * @param sysfileMap
	 * @return
	 * @throws IOException 
	 */
	private Object uploadFile(Map<Integer, SysFile> sysfileMap) throws IOException {
		if(SysFileConstants.saveToService){
			return uploadFileToService(sysfileMap);
		}
		return "目前file.save.type的值，只支持配置为：[service]";
	}
	
	/**
	 * 保存文件到服务器中
	 * @param sysfileMap
	 * @return
	 * @throws IOException 
	 */
	private Object uploadFileToService(Map<Integer, SysFile> sysfileMap) {
		JSONArray jsonArray = new JSONArray(sysfileMap.size());
		Set<Integer> keys = sysfileMap.keySet();
		SysFile sysFile;
		File file;
		FileOutputStream fos = null;
		InputStream in = null;
		byte[] b;
		int len;
		
		for (Integer key : keys) {
			sysFile = sysfileMap.get(key);
			jsonArray.add(HibernateUtil.saveObject(sysFile, null));
			file = new File(sysFile.getSavePath());
			try {
				fos = new FileOutputStream(file);
				in = sysFile.getFileItem().getInputStream();
				
				b = new byte[1024];
				while((len = in.read(b)) > 0){
					fos.write(b, 0, len);
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				CloseUtil.closeIO(fos, in);
				sysFile.getFileItem().delete();// 删除临时文件
			}
		}
		
		if(jsonArray.size() == 1){
			return jsonArray.getJSONObject(0);
		}else{
			return jsonArray;
		}
	}

	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * 下载文件
	 * @param request
	 * @param response 
	 * @return
	 * @throws FileNotFoundException 
	 */
	public Object download(HttpServletRequest request, HttpServletResponse response){
		String fileIds = request.getParameter(BuiltinParameterKeys._IDS);
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put(BuiltinParameterKeys._IDS, fileIds);
		CurrentThreadContext.getReqLogData().getReqLog().setReqData(jsonObject.toString());// 记录请求体
		
		if(StrUtils.isEmpty(fileIds)){
			return "下载文件时，传入的_ids参数值不能为空";
		}
		request.setAttribute(BuiltinParameterKeys._IS_PRINT_RESPONSEBODY, false);
		response.setHeader("Content-Type", "application/octet-stream;charset="+ EncodingConstants.UTF_8);
		
		OutputStream out = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		InputStream input = null;
		
		Object[] fileIdArr = fileIds.split(",");
		List<SysFile> sysFileList = new ArrayList<SysFile>(fileIdArr.length);
		try {
			StringBuffer updateDownloadCountHql = new StringBuffer("update SysFile set downloadCount = downloadCount+1 where ");
			updateDownloadCountHql.append(ResourcePropNameConstants.ID).append(" in(");
			for (Object fileId : fileIdArr) {
				sysFileList.add(getObjectById(fileId.toString(), SysFile.class));
				updateDownloadCountHql.append("?,");
			}
			updateDownloadCountHql.setLength(updateDownloadCountHql.length()-1);
			updateDownloadCountHql.append(")");
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, updateDownloadCountHql.toString(), fileIdArr);
			
			File tmpFile;
			if(fileIdArr.length == 1){
				SysFile sysFile = sysFileList.get(0);
				if(SysFileConstants.SAVE_TYPE_SERVICE.equals(sysFile.getSaveType())){
					response.setHeader("Content-Disposition", "attachment;filename=" + StrUtils.turnStrEncoding(sysFile.getActName(), EncodingConstants.UTF_8, EncodingConstants.ISO8859_1));
					
					tmpFile = new File(sysFile.getSavePath());
					response.setHeader("Content-Length", "" + tmpFile.length());
					
					fis = new FileInputStream(tmpFile);
					out = response.getOutputStream();
					
					int len;
					byte[] b = new byte[1024];
					while((len = fis.read(b)) > 0){
						out.write(b, 0, len);
					}
				}
			}else{
				response.setHeader("Content-Disposition", "attachment;filename=【批量下载】["+FileUtil.getFileCode()+"].zip");
				
				out = response.getOutputStream();
				zos = new ZipOutputStream(out);
				byte[] b;
				int len;
				for (SysFile sysFile : sysFileList) {
					if(SysFileConstants.SAVE_TYPE_SERVICE.equals(sysFile.getSaveType())){
						zos.putNextEntry(new ZipEntry(sysFile.getActName()));
						tmpFile = new File(sysFile.getSavePath());
						input = new FileInputStream(tmpFile);
						b = new byte[1024];
						while((len = input.read(b)) > 0){
							zos.write(b, 0, len);
						}
						zos.closeEntry();
						input.close();
					}
				}
			}
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg(e);
		} finally{
			CloseUtil.closeIO(zos, out, fis, input);
			sysFileList.clear();
		}
		
		// 组装结果
		return jsonObject;
	}
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * 删除文件
	 * @param request
	 * @return
	 */
	public Object delete(HttpServletRequest request){
		String fileIds = request.getParameter(BuiltinParameterKeys._IDS);
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put(BuiltinParameterKeys._IDS, fileIds);
		CurrentThreadContext.getReqLogData().getReqLog().setReqData(jsonObject.toString());// 记录请求体
		
		if(StrUtils.isEmpty(fileIds)){
			return "删除文件时，传入的_ids参数值不能为空";
		}
		
		try {
			// 删除相应的文件
			String[] fileIdArr = fileIds.split(",");
			SysFile sysFile;
			File tmpFile;
			for (String fileId : fileIdArr) {
				sysFile = getObjectById(fileId, SysFile.class);
				
				if(SysFileConstants.SAVE_TYPE_SERVICE.equals(sysFile.getSaveType())){
					tmpFile = new File(sysFile.getSavePath());
					if(tmpFile.exists() && tmpFile.isFile()){
						tmpFile.delete();
					}
				}
			}
			
			// 删除数据
			deleteDataById("SysFile", fileIds);
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg(e);
		}
		
		// 组装结果
		return jsonObject;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 验证保存文件的目录是否存在
	 * <p>如果不存在，则创建</p>
	 * @param buildInType  
	 * @param uploadTargetDir  
	 * @return 
	 */
	public String validSaveFileDirIsExists(int buildInType, String uploadTargetDir) {
		if(SysFileConstants.SAVE_TYPE_SERVICE.equals(SysFileConstants.saveType)){
			String saveFileDir;
			
			if(StrUtils.isEmpty(uploadTargetDir)){
				if(buildInType == SysFileConstants.BUILD_IN_TYPE_NORMAL){
					saveFileDir = SysFileConstants.fileSavePath + DateUtil.formatDate(new Date()) + File.separator;
				}else if(buildInType == SysFileConstants.BUILD_IN_TYPE_IMPORT){
					saveFileDir = SysFileConstants.importFileSavePath + DateUtil.formatDate(new Date()) + File.separator;
				}else if(buildInType == SysFileConstants.BUILD_IN_TYPE_IMPORT_TEMPLATE){
					saveFileDir = SysFileConstants.importFileTemplateSavePath + DateUtil.formatDate(new Date()) + File.separator;
				}else if(buildInType == SysFileConstants.BUILD_IN_TYPE_EXPORT){
					saveFileDir = SysFileConstants.exportFileSavePath + DateUtil.formatDate(new Date()) + File.separator;
				}else{
					throw new IllegalArgumentException("系统目前上传文件的内置类型buildInType，值只包括[1,2,3,4]");
				}
			}else{
				if(uploadTargetDirBasePath == null){
					uploadTargetDirBasePath = SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "files" + File.separator;
				}
				saveFileDir = uploadTargetDirBasePath + uploadTargetDir + File.separator;
			}
			
			if(uploadTargetDirPathCache.contains(saveFileDir)){
				return saveFileDir;
			}
			uploadTargetDirPathCache.add(saveFileDir);
			return FileUtil.validSaveFileDirIsExists(saveFileDir);
		}
		throw new IllegalArgumentException("系统目前只支持在服务器上保存文件的方式");
	}
	private static String uploadTargetDirBasePath; 
}
