package com.king.tooth.plugins.orm.hibernate.hbm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.freemarker.FreemarkerUtil;

/**
 * 对hibernate的hbm映射文件操作
 * @author DougLei
 */
public class HibernateHbmHandler {

	/**
	 * hibernate hbm映射文件模版所在的路径
	 */
	private static final String HBM_FTL_FILE_PATH;
	
	/**
	 * 静态块，从配置文件读取，并初始化属性值
	 */
	static{
		HBM_FTL_FILE_PATH = ResourceHandlerUtil.initConfValue("hbm.ftl.file.path", 
				SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separator + "classes" + File.separator + "hibernateMapping" + File.separator + "template" + File.separator + "hibernate.hbm.xml.ftl");
	}
	
	/**
	 * 根据表数据，创建hbm映射文件
	 * @param table
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return hbm content
	 */
	public String createHbmMappingContent(ComTabledata table, boolean isNeedInitBasicColumns){
		if(isNeedInitBasicColumns){
			DynamicBasicDataColumnUtil.initBasicColumnToTable(table);
		}
		Map<String, Object> dataModel = new HashMap<String, Object>(3);
		dataModel.put("table", table);
		dataModel.put("columns", table.getColumns());
		dataModel.put("id", ResourceNameConstants.ID);
		String hbmMappingContent = FreemarkerUtil.process(HBM_FTL_FILE_PATH, dataModel);
		return hbmMappingContent;
	}
}
