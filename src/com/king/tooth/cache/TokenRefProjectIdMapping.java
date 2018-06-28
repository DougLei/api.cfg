package com.king.tooth.cache;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.util.StrUtils;

/**
 * token和项目id映射关系
 * @author DougLei
 */
public class TokenRefProjectIdMapping {
	private transient static final Map<String, String> tokenRefprojIdMapping = new HashMap<String, String>(22);
	
	/**
	 * 存储token和项目id的映射关系
	 * @param token
	 * @param projectId
	 */
	public static void setTokenRefProjMapping(String token, String projectId){
		if(StrUtils.isEmpty(token)){
			throw new NullPointerException("存储token和项目id的映射关系时，token不能为空");
		}
		if(StrUtils.isEmpty(projectId)){
			throw new NullPointerException("存储token和项目id的映射关系时，projectId不能为空");
		}
		tokenRefprojIdMapping.put(token, projectId);
	}
	
	/**
	 * 根据token，获取对应的项目id
	 * @param token
	 * @return
	 */
	public static String getProjectId(String token){
		if(StrUtils.isEmpty(token)){
			throw new NullPointerException("token为空，无法获取对应的projectId");
		}
		return tokenRefprojIdMapping.get(token);
	}

	/**
	 * 移除映射
	 * @param token
	 */
	public static void removeMapping(String token){
		tokenRefprojIdMapping.remove(token);
	}
}
