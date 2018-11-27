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

/**
 * 备份用户在系统中上传的所有文件的任务
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BackupUploadFileJob implements Job, Serializable{

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		if(SysFileConstants.saveToService && SysFileConstants.fileBackupPath != null){
			// 用户上传的所有文件的根目录
			File rootUploadFolder = new File(SysFileConstants.fileSavePath);
			// 备份的目标地址：例如E:\\devTools\\backup\\bak周几\\
			File backupUploadFolder = new File(SysFileConstants.fileBackupPath + File.separator + "bak" + DateUtil.getWeekend() + File.separator);
			try {
				FileUtil.batchCopyfiles(rootUploadFolder, backupUploadFolder, false);
			} catch (IOException e) {
				Log4jUtil.error("系统在备份用户在系统中上传的所有文件时出现异常，请联系系统管理员:{}", ExceptionUtil.getErrMsg(e));
			}		
		}
	}
}
