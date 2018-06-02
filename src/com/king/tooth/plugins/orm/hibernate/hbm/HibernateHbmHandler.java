package com.king.tooth.plugins.orm.hibernate.hbm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.ComHibernateHbmConfdata;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.freemarker.FreemarkerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

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
	 * <p>并将其hbm数据加载到对应sessionfactory中</p>
	 * @param tabledata
	 * @return hbm content
	 */
	private String createHbmMappingContent(CfgTabledata tabledata){
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("table", tabledata);
		dataModel.put("columns", tabledata.getColumns());
		String hbmMappingContent = FreemarkerUtil.process(HBM_FTL_FILE_PATH, dataModel);
		
		ComHibernateHbmConfdata hbmData = new ComHibernateHbmConfdata(
				tabledata.getId(), hbmMappingContent, tabledata.getVersion(), tabledata.getIsDatalinkTable());
		HibernateUtil.saveObject(hbmData, null);
		return hbmMappingContent;
	}
	
	/**
	 * 根据表数据，创建hbm映射文件
	 * <p>并将其hbm数据加载到对应sessionfactory中</p>
	 * @param tabledatas
	 * @param sessionFactory
	 */
	public void createHbmMappingContent(List<CfgTabledata> tabledatas, SessionFactoryImpl sessionFactory){
		if(tabledatas != null && tabledatas.size() > 0){
//			DynamicDataLinkTableUtil.processParentSubTable(tabledatas); 
			List<String> hbmContents = new ArrayList<String>(tabledatas.size());
			for (CfgTabledata tabledata : tabledatas) {
				hbmContents.add(createHbmMappingContent(tabledata));
			}
			
			List<InputStream> inputs = new ArrayList<InputStream>(tabledatas.size());
			for (String hbmContent : hbmContents) {
				inputs.add(turnToInputStream(hbmContent));
			}
			hbmContents.clear();
			sessionFactory.appendNewHbmConfig(inputs);
			inputs.clear();
		}
	}
	
	/**
	 * 给sessionFactory中追加新的hbm映射信息
	 * @param tabledatas
	 * @param sessionFactory
	 */
	public void appendNewHbmConfig(List<Object> hbmContents, SessionFactoryImpl sessionFactory){
		if(hbmContents != null && hbmContents.size() > 0){
			List<InputStream> inputs = new ArrayList<InputStream>(hbmContents.size());
			for (Object hbmContent : hbmContents) {
				if(StrUtils.isEmpty(hbmContent)){
					continue;
				}
				inputs.add(turnToInputStream(hbmContent.toString()));
			}
			hbmContents.clear();
			sessionFactory.appendNewHbmConfig(inputs);
			inputs.clear();
		}
	}
	
	/**
	 * 将hbm文件字符串转换为inputStream对象
	 * @param hbmContent
	 * @return
	 */
	private InputStream turnToInputStream(String hbmContent) {
		ByteArrayInputStream bais = new ByteArrayInputStream(hbmContent.getBytes());
		return bais;
	}

	/**
	 * 根据表数据，删除hbm映射文件
	 * <p>并将其hbm数据从对应sessionfactory中移除</p>
	 * @param tabledatas
	 * @param sessionFactory
	 */
	public void dropHbmMappingContent(List<CfgTabledata> tabledatas, SessionFactoryImpl sessionFactory){
		if(tabledatas != null && tabledatas.size() > 0){
			StringBuilder deleteHql = new StringBuilder();
			deleteHql.append("delete ComHibernateHbmConfdata where tableId");
			
			int len = tabledatas.size();
			List<Object> tabledataIds = new ArrayList<Object>(len);
			List<String> resourceNames = new ArrayList<String>(len);
			if(len == 1){
				deleteHql.append(" = ?");
				tabledataIds.add(tabledatas.get(0).getId());
				resourceNames.add(tabledatas.get(0).getResourceName());
			}else{
				deleteHql.append(" in (");
				for (int i =0; i<len; i++) {
					deleteHql.append("?").append(",");
					tabledataIds.add(tabledatas.get(i).getId());
					resourceNames.add(tabledatas.get(i).getResourceName());
				}
				deleteHql.setLength(deleteHql.length() - 1);
				deleteHql.append(")");
			}
			HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, deleteHql.toString(), tabledataIds);
			tabledataIds.clear();
			
			sessionFactory.removeHbmConfig(resourceNames);
			resourceNames.clear();
		}
	}
}
