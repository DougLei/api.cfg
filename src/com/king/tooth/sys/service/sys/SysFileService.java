package com.king.tooth.sys.service.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.FileUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 文件表service
 * @author DougLei
 */
@Service
public class SysFileService extends AbstractService{
	
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
				if(uploadFileInfo.isEmpty){
					return "没有获得要操作的文件";
				}else if(uploadFileInfo.errMsg != null){
					return uploadFileInfo.errMsg;
				}else{
					String uploadDir = validUploadDirIsExists();
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
							sysFile.setCode(FileUtil.getFileCode());
							sysFile.setSaveType(FileUtil.fileSaveType);
							sysfileMap.put(index, sysFile);
						}
						
						if(file.isFormField()){
							if(file.getFieldName().startsWith("secretLevel_") && StrUtils.notEmpty(file.getString())){
								sysFile.setSecretLevel(Integer.valueOf(file.getString()));
							}
						}else{
							if(file.getFieldName().startsWith("file_")){
								fileName = file.getName().substring(file.getName().lastIndexOf("\\")+1);
								
								sysFile.setActName(fileName);
								sysFile.setSizes(file.getSize()+"");
								sysFile.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1));
								sysFile.setFileItem(file);
								if(FileUtil.saveToService){
									sysFile.setSavePath(uploadDir + File.separator + sysFile.getCode() + "." + sysFile.getSuffix());
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
			if(FileUtil.saveToService && filePathList != null && filePathList.size() > 0){
				File tf;
				for (String fp : filePathList) {
					tf = new File(fp);
					if(tf.exists() && tf.isFile()){
						tf.delete();
					}
				}
			}
			return ExceptionUtil.getErrMsg("SysFileService", "upload", e);
		} finally{
			if(fileList != null){
				fileList.clear();
			}
			if(FileUtil.saveToService && filePathList != null){
				filePathList.clear();
			}
		}
	}
	
	// 验证上传的文件是否为空
	private UploadFileInfo uploadFileIsEmpty(List<FileItem> fileList) throws UnsupportedEncodingException{
		JSONObject logJsonObject = new JSONObject(fileList.size());
		
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		FileItem fi;
		String fiValue;
		long fileSizeKB;
		for (int i = 0; i < fileList.size(); i++) {
			fi = fileList.get(i);
			if(fi.isFormField()){
				fiValue = fi.getString(EncodingConstants.UTF_8);
				logJsonObject.put(fi.getFieldName(), fiValue);
				
				if("refDataId".equals(fi.getFieldName())){
					uploadFileInfo.refDataId = fiValue;
					fileList.remove(i--);
				}
				if("batch".equals(fi.getFieldName())){
					uploadFileInfo.batch = fiValue;
					fileList.remove(i--);
				}
			}else{
				logJsonObject.put(fi.getFieldName(), new FileInfo(fi.getName(), (fi.getSize() + " B")));
				
				uploadFileInfo.isEmpty = false;
				uploadFileInfo.count++;
				
				fileSizeKB = fi.getSize()/1024;// 由B转换为KB
				if(fileSizeKB > FileUtil.fileMaxSize){
					uploadFileInfo.errMsg = "文件的大小为"+(fileSizeKB/1024)+"M，系统限制单个文件的大小不能超过"+(FileUtil.fileMaxSize/1024)+"M，请修改后再上传";
				}
			}
		}

		if(StrUtils.isEmpty(uploadFileInfo.refDataId) || uploadFileInfo.refDataId.length() != 32){
			uploadFileInfo.errMsg = "上传文件时，关联的业务数据id格式错误，即参数名为refDataId的值格式错误：不能为空，或长度不符合要求";
		}
		if(StrUtils.isEmpty(uploadFileInfo.batch) || uploadFileInfo.batch.length() != 32){
			uploadFileInfo.batch = ResourceHandlerUtil.getIdentity();
		}
		
		CurrentThreadContext.getReqLogData().getReqLog().setReqData(logJsonObject.toString());// 记录请求体
		return uploadFileInfo;
	}
	// 上传文件的信息
	private class UploadFileInfo{
		public String errMsg;// 记录错误的信息
		public boolean isEmpty = true;// 在调用上传文件接口时，是否传递了文件
		public int count;// 传递的文件数量
		public String refDataId;// 文件关联的业务数据id
		public String batch;// 同一次上传文件批次
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
		if(FileUtil.saveToService){
			return uploadFileToService(sysfileMap);
		}else if(FileUtil.saveToDB){
			return uploadFileToDB(sysfileMap);
		}
		return "目前file.save.type的值，只支持配置为：[service]或[db]";
	}
	
	/**
	 * 保存文件到数据库中
	 * @param sysfileMap
	 * @return
	 */
	private Object uploadFileToDB(Map<Integer, SysFile> sysfileMap) {
		return "系统目前不支持向数据库中上传文件";
	}
	
	/**
	 * 保存文件到服务器中
	 * @param sysfileMap
	 * @return
	 * @throws IOException 
	 */
	private Object uploadFileToService(Map<Integer, SysFile> sysfileMap) throws IOException {
		JSONArray jsonArray = new JSONArray(sysfileMap.size());
		Set<Integer> keys = sysfileMap.keySet();
		SysFile sysFile;
		File file;
		FileOutputStream fos;
		InputStream in;
		byte[] b;
		int len;
		
		for (Integer key : keys) {
			sysFile = sysfileMap.get(key);
			jsonArray.add(HibernateUtil.saveObject(sysFile, null));
			
			file = new File(sysFile.getSavePath());
			fos = new FileOutputStream(file);
			in = sysFile.getFileItem().getInputStream();
			
			b = new byte[1024];
			while((len = in.read(b)) > 0){
				fos.write(b, 0, len);
			}
			CloseUtil.closeIO(fos, in);
			sysFile.getFileItem().delete();// 删除临时文件
		}
		
		if(jsonArray.size() == 1){
			return jsonArray.getJSONObject(0);
		}else{
			return jsonArray;
		}
	}

	/**
	 * 验证上传文件的目录是否存在
	 * <p>如果不存在，则创建</p>
	 */
	private String validUploadDirIsExists() {
		if(SysFile.service.equals(FileUtil.fileSaveType)){
			String uploadDir = FileUtil.fileSavePath + FileUtil.getFileDir();
			File dir = new File(uploadDir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			return uploadDir;
		}
		return null;
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
			StringBuffer updateDownloadCountHql = new StringBuffer("update SysFile set downloadCount = downloadCount+1 where id in (");
			for (Object fileId : fileIdArr) {
				sysFileList.add(getObjectById(fileId.toString(), SysFile.class));
				updateDownloadCountHql.append("?,");
			}
			updateDownloadCountHql.setLength(updateDownloadCountHql.length()-1);
			updateDownloadCountHql.append(")");
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, updateDownloadCountHql.toString(), fileIdArr);
			
			File tmpFile;
			if(fileIdArr.length == 1){
				SysFile sysFile = sysFileList.get(0);
				if(SysFile.service.equals(sysFile.getSaveType())){
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
				}else if(SysFile.db.equals(sysFile.getSaveType())){
					return "系统目前不支持从数据库中下载文件";
				}
			}else{
				response.setHeader("Content-Disposition", "attachment;filename=【批量下载】["+FileUtil.getFileCode()+"].zip");
				
				out = response.getOutputStream();
				zos = new ZipOutputStream(out);
				byte[] b;
				int len;
				for (SysFile sysFile : sysFileList) {
					if(SysFile.service.equals(sysFile.getSaveType())){
						zos.putNextEntry(new ZipEntry(sysFile.getActName()));
						tmpFile = new File(sysFile.getSavePath());
						input = new FileInputStream(tmpFile);
						b = new byte[1024];
						while((len = input.read(b)) > 0){
							zos.write(b, 0, len);
						}
						zos.closeEntry();
						input.close();
					}else if(SysFile.db.equals(sysFile.getSaveType())){
						zos.putNextEntry(new ZipEntry(sysFile.getActName().substring(0, sysFile.getActName().lastIndexOf(".")) + ".txt" ));
						zos.write(new String("【"+sysFile.getActName()+"】文件下载失败：系统目前不支持从数据库中下载文件").getBytes(EncodingConstants.UTF_8));
						continue;
					}
				}
			}
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg("SysFileService", "download", e);
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
				
				if(SysFile.service.equals(sysFile.getSaveType())){
					tmpFile = new File(sysFile.getSavePath());
					if(tmpFile.exists() && tmpFile.isFile()){
						tmpFile.delete();
					}
				}
			}
			
			// 删除数据
			deleteDataById("SysFile", fileIds);
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg("SysFileService", "delete", e);
		}
		
		// 组装结果
		return jsonObject;
	}
}
