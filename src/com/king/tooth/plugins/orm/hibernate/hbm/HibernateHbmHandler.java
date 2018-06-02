package com.king.tooth.plugins.orm.hibernate.hbm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.StrUtils;
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
		HBM_FTL_FILE_PATH = initConfValue("hbm.flt.file.path", SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + "WEB-INF" + File.separator + "classes" + File.separator + "hibernateMapping" + File.separator + "template" + File.separator + "hibernate.hbm.xml.ftl");
	}
	
	/**
	 * 初始化配置参数值
	 * @param configKey
	 * @param defaultValue
	 * @return
	 */
	private static String initConfValue(String configKey, String defaultValue){
		String confValue = SysConfig.getSystemConfig(configKey);
		if(StrUtils.isEmpty(confValue)){
			confValue = defaultValue;
		}
		return confValue;
	}
	
	/**
	 * 根据表数据，创建hbm映射文件
	 * @param tabledata
	 * @return hbm content
	 */
	public String createHbmMappingContent(CfgTabledata tabledata){
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("table", tabledata);
		dataModel.put("columns", tabledata.getColumns());
		String hbmMappingContent = FreemarkerUtil.process(HBM_FTL_FILE_PATH, dataModel);
		
//		ComHibernateHbmConfdata hbmData = new ComHibernateHbmConfdata(
//				tabledata.getId(), hbmMappingContent, tabledata.getIsDatalinkTable());
//		HibernateUtil.saveObject(hbmData, null);
		return hbmMappingContent;
	}
}
