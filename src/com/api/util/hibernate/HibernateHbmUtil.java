package com.api.util.hibernate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.api.cache.SysContext;
import com.api.constants.ResourcePropNameConstants;
import com.api.sys.entity.cfg.CfgTable;
import com.api.util.ResourceHandlerUtil;
import com.api.util.build.model.DynamicBasicColumnUtil;
import com.api.util.freemarker.FreemarkerUtil;

/**
 * 对hibernate的hbm映射文件操作工具类
 * @author DougLei
 */
public class HibernateHbmUtil {

	/**
	 * hibernate hbm映射文件模版所在的路径
	 */
	private static final String HBM_FTL_FILE_PATH;
	/**
	 * 静态块，从配置文件读取，并初始化属性值
	 */
	static{
		HBM_FTL_FILE_PATH = ResourceHandlerUtil.initConfValue("hbm.ftl.file.path", 
				SysContext.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separator + "classes" + File.separator + "hibernateMapping" + File.separator + "template" + File.separator + "hibernate.hbm.xml.ftl");
	}
	
	/**
	 * 根据表数据，创建hbm映射文件
	 * @param table
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return hbm content
	 */
	public static String createHbmMappingContent(CfgTable table, boolean isNeedInitBasicColumns){
		return createHbmMappingContent(table, isNeedInitBasicColumns, HBM_FTL_FILE_PATH);
	}
	
	/**
	 * 根据表数据，创建hbm映射文件
	 * @param table
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @param freemarkerFtlPath freemarker ftl模版文件的路径
	 * @return hbm content
	 */
	public static String createHbmMappingContent(CfgTable table, boolean isNeedInitBasicColumns, String freemarkerFtlPath){
		if(isNeedInitBasicColumns){
			DynamicBasicColumnUtil.initBasicColumnToTable(table);
		}
		Map<String, Object> dataModel = new HashMap<String, Object>(3);
		dataModel.put("table", table);
		dataModel.put("columns", table.getColumns());
		dataModel.put("id", ResourcePropNameConstants.ID);
		String hbmMappingContent = FreemarkerUtil.process(freemarkerFtlPath, dataModel);
		return hbmMappingContent;
	}
}
