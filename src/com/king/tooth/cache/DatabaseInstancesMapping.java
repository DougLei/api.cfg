package com.king.tooth.cache;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库示例映射
 * @author DougLei
 */
public class DatabaseInstancesMapping {
	private transient static final Map<String, CfgDatabase> databaseInstanceMapping = new HashMap<String, CfgDatabase>(16);
	static{
		databaseInstanceMapping.put(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId(), BuiltinObjectInstance.currentSysBuiltinDatabaseInstance);
	}

	/**
	 * 根据databaseId获取database实例
	 * @param databaseId
	 * @return
	 */
	public static CfgDatabase getDatabasInstance(String databaseId) {
		if(StrUtils.isEmpty(databaseId)){
			throw new NullPointerException("获取当前线程对应的database实例时，参数databaseId的值不能为空，请联系系统后台开发人员");
		}
		CfgDatabase database = databaseInstanceMapping.get(databaseId);
		if(database == null){
			throw new NullPointerException("系统中没有databaseId值为"+databaseId+"，对应的database实例，请联系系统后台开发人员");
		}
		return database;
	}
	
	/**
	 * 添加database实例到映射中
	 * @param database
	 */
	public static void addDatabaseInstance(CfgDatabase database){
		String databaseId = database.getId();
		if(databaseInstanceMapping.containsKey(databaseId)){
			Log4jUtil.info("系统中已经存在databaseId值为 [{}]的datbase实例！",databaseId);
			return;
		}
		databaseInstanceMapping.put(databaseId, database);
	}
	
	/**
	 * 从映射中删除database实例
	 * @param databaseId
	 */
	public static CfgDatabase removeDatabaseInstance(String databaseId){
		if(!databaseInstanceMapping.containsKey(databaseId)){
			Log4jUtil.info("不存在databaseId值为 [{"+databaseId+"}]的database实例！删除失败！");
			return null;
		}
		return databaseInstanceMapping.remove(databaseId);
	}

	/**
	 * 获取database实例对象集合
	 * @return
	 */
	public static Map<String, CfgDatabase> getAllDatabaseInstances() {
		return databaseInstanceMapping;
	}
}
