package com.king.tooth.util.freemarker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker操作工具类
 * @author DougLei
 */
public class FreemarkerUtil {
	
	/**
	 * 根据模版和数据，获得生成的结果内容
	 * @param ftlPath 模版的全路径
	 * @param dataModel 数据
	 * @return
	 */
	public static String process(String ftlPath, Map<String, Object> dataModel){
		StringWriter sr = new StringWriter();
		try {
			Template template = new Template("hibernateHbmMappingFile", new FileReader(new File(ftlPath)), null);
			template.process(dataModel, sr);
		} catch (FileNotFoundException e) {
			Log4jUtil.debug("[FreemarkerUtil.process]根据模版和数据，生成文件时出现异常:{}", e.getMessage());
		} catch (IOException e) {
			Log4jUtil.debug("[FreemarkerUtil.process]根据模版和数据，生成文件时出现异常:{}", e.getMessage());
		} catch (TemplateException e) {
			Log4jUtil.debug("[FreemarkerUtil.process]根据模版和数据，生成文件时出现异常:{}", e.getMessage());
		}finally{
			CloseUtil.closeIO(sr);
		}
		return sr.toString();
	}
}
