package com.king.tooth.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.util.StrUtils;

/**
 * 项目id和数据库id的映射关系
 * @author DougLei
 */
public class ProjectIdRefDatabaseIdMapping {
	private transient static final Map<String, String> projIdRefDbIdMapping = new HashMap<String, String>(22);
	
	/**
	 * 存储项目id和数据库id的映射关系
	 * @param projectId
	 * @param databaseId
	 */
	public static void setProjRefDbMapping(String projectId, String databaseId){
		if(StrUtils.isEmpty(projectId)){
			throw new NullPointerException("存储项目id和数据库id的映射关系时，projectId不能为空");
		}
		if(StrUtils.isEmpty(databaseId)){
			throw new NullPointerException("存储项目id和数据库id的映射关系时，databaseId不能为空");
		}
		projIdRefDbIdMapping.put(projectId, databaseId);
	}
	
	/**
	 * 根据项目id，获取对应的数据库id
	 * @param projectId
	 * @return
	 */
	public static String getDbId(String projectId){
		if(StrUtils.isEmpty(projectId)){
			throw new NullPointerException("projectId为空，无法获取对应的databaseId");
		}
		return projIdRefDbIdMapping.get(projectId);
	}

	/**
	 * 清空指定数据库的所有映射信息
	 * @param databaseId
	 */
	public static void clearMapping(String databaseId) {
		if(SysConfig.getSystemConfig("current.sys.database.id").equals(databaseId)){
			throw new IllegalArgumentException("不能移除系统内置的项目/数据库映射");
		}
		if(projIdRefDbIdMapping.size() > 0){
			List<String> tmpKeys = new ArrayList<String>();
			Set<Entry<String, String>> sets = projIdRefDbIdMapping.entrySet();
			for (Entry<String, String> entry : sets) {
				if(entry.getValue().equals(databaseId)){
					tmpKeys.add(entry.getKey());
				}
			}
			
			if(tmpKeys.size() > 0){
				for (String key : tmpKeys) {
					projIdRefDbIdMapping.remove(key);
				}
				tmpKeys.clear();
			}
		}
	}
	
	/**
	 * 移除映射
	 * @param projectId
	 */
	public static void removeMapping(String projectId){
		if(SysConfig.getSystemConfig("current.sys.project.id").equals(projectId)){
			throw new IllegalArgumentException("不能移除系统内置的项目/数据库映射");
		}
		projIdRefDbIdMapping.remove(projectId);
	}
}
