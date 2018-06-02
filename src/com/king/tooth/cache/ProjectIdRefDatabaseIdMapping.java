package com.king.tooth.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * 项目id和数据库id的映射关系
 * @author DougLei
 */
public class ProjectIdRefDatabaseIdMapping {
	private transient static final Map<String, String> projIdRefDbIdMapping = new HashMap<String, String>(22);
	
	public static void main(String[] args) {
		System.out.println(ResourceHandlerUtil.getIdentity());
	}
	
	/**
	 * 初始化配置系统的项目id和数据库id映射
	 */
	public static void initBasicProjectIdRefDatabaseIdMapping(){
		projIdRefDbIdMapping.put(SysConfig.getSystemConfig("cfg.project.id"), SysConfig.getSystemConfig("cfg.database.id"));
	}
	
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
	 * @param databaseId
	 * @return
	 */
	public static String getDbId(String projectId){
		if(StrUtils.isEmpty(projectId)){
			return null;
		}
		return projIdRefDbIdMapping.get(projectId);
	}

	/**
	 * 清空指定数据库的所有映射信息
	 * @param databaseId
	 */
	public static void clearMapping(String databaseId) {
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
}
