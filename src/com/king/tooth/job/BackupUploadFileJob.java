package com.king.tooth.job;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.king.tooth.constants.SysFileConstants;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.FileUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * 备份用户在系统中上传的所有文件的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BackupUploadFileJob implements Job, Serializable{

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		if(SysFileConstants.saveToService && SysFileConstants.fileBackupPath != null){
			// 用户上传的所有文件的根目录
			String sourceUploadFile = SysFileConstants.fileSavePath;
			// 备份的目标地址：例如E:\\devTools\\backup\\bak周几\\
			String targetUploadFile = SysFileConstants.fileBackupPath + File.separator + "bak" + DateUtil.getWeekend() + File.separator;
			fileBak(sourceUploadFile, targetUploadFile);
		}
		
		bakOther();
	}
	
	private static Boolean isBakOther;
	private static String[] sources;
	private static String[] targets;
	private static int length;
	/**备份其他文件*/
	private void bakOther() {
		if(isBakOther == null){
			String source = ResourceHandlerUtil.initConfValue("other.file.source.dir", null);
			String target = ResourceHandlerUtil.initConfValue("other.file.bak.target.dir", null);
			
			if(StrUtils.notEmpty(source) && StrUtils.notEmpty(target)){
				sources = source.split(",");
				targets = target.split(",");
				length = sources.length;
				if(length > targets.length){
					length = targets.length;
				}
				if(length > 0){
					isBakOther = true;
				}
			}
		}
		if(isBakOther == null){
			isBakOther = false;
		}
		if(isBakOther){
			for(int i=0;i<length;i++){
				fileBak(sources[i], targets[i]);
			}
		}
	}
	
	private void fileBak(String source, String target){
		// 文件的根目录
		File rootUploadFolder = new File(source);
		// 备份的目标地址
		File backupUploadFolder = new File(target);
		try {
			FileUtil.batchCopyfiles(rootUploadFolder, backupUploadFolder, false);
		} catch (IOException e) {
			Log4jUtil.error("系统在备份文件时出现异常，请联系系统管理员:{}", ExceptionUtil.getErrMsg(e));
		}		
	}
}
