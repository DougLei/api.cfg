package com.king.tooth.web.builtin.method.common.util.querycondfunc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.internal.HbmConfPropMetadata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.ReflectUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.entity.HqlQueryCondFuncEntity;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.entity.IQueryCondFuncEntity;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.entity.SqlQueryCondFuncEntity;

/**
 * 内置的查询条件函数工具类
 * 例如:eq、btn等
 * @see com.king.tooth.web.builtin.method.tableresource.querycondfunc.impl下的类
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class BuiltinQueryCondFuncUtil {

	/**
	 * 系统内置查询条件方法的类对象集合
	 * <p>key=方法名，例如btn、between</p>
	 * <p>value=对应的类对象，例如BtnMethod.class</p>
	 */
	private transient static final Map<String, Class<AbstractBuiltinQueryCondFunc>> queryCondEntities = new HashMap<String, Class<AbstractBuiltinQueryCondFunc>>();
	
	/**
	 * 是否没有初始化系统内置查询条件函数配置
	 * 防止被多次初始化
	 */
	private static boolean unInitBuiltinQueryCondFuncConfig = true;
	
	/**
	 * 初始化系统内置查询条件函数配置
	 */
	public static void initBuiltinQueryCondFuncConfig(){
		if(unInitBuiltinQueryCondFuncConfig){
			JSONArray jsonArray = JSONArray.parseArray(SysConfig.getSystemConfig("builtin.query.cond.methods"));
			int len = jsonArray.size();
			JSONObject objTmp = null;
			Class<AbstractBuiltinQueryCondFunc> clz = null;
			String[] methodNameArr = null;
			for (int i = 0; i < len; i++) {
				objTmp = jsonArray.getJSONObject(i);
				clz = ReflectUtil.getClass(objTmp.getString("classpath"));
				methodNameArr = objTmp.getString("builtinQueryCondMethodNames").split(",");
				for (String methodName : methodNameArr) {
					queryCondEntities.put(methodName, clz);
				}
			}
			jsonArray.clear();
			unInitBuiltinQueryCondFuncConfig = false;
		}
	}
	
	
	/**
	 * 获取内置查询条件方法对象实例
	 * @param methodName
	 * @return
	 */
	private static AbstractBuiltinQueryCondFunc getBuiltinQueryConditionMethodInstance(String methodName){
		Class<AbstractBuiltinQueryCondFunc> clz = queryCondEntities.get(methodName.toLowerCase());
		return ReflectUtil.newInstance(clz);
	}
	
	/**
	 * 拼接查询条件的hql语句
	 * <p>同时，将条件值按顺序存储到hqlParameterValues中</p>
	 * @param requestResourceType 资源类型
	 * @param resourceName 资源名
	 * @param queryCondParamsSet 查询参数map集合
	 * @param queryCondParameterValues 要通过引用传递出去的值集合
	 * @param dbScriptStatement 要通过引用传递出去的数据库脚本语句
	 */
	public static void installQueryCondHql(int requestResourceType, String resourceName, Set<Entry<String, String>> queryCondParamsSet, List<Object> queryCondParameterValues, StringBuilder dbScriptStatement){
		installQueryCondOfDBScriptStatement(requestResourceType, resourceName, queryCondParamsSet, queryCondParameterValues, dbScriptStatement, null);
	}
	
	/**
	 * 拼接查询条件的数据库脚本语句【可以带别名】
	 * <p>同时，将条件值按顺序存储到hqlParameterValues中</p>
	 * @param requestResourceType 资源类型
	 * @param resourceName 资源名
	 * @param queryCondParamsSet 查询参数map集合
	 * @param queryCondParameterValues 要通过引用传递出去的值集合
	 * @param dbScriptStatement 要通过引用传递出去的数据库脚本语句
	 * @param alias 别名【默认为空字符串】 
	 */
	public static void installQueryCondOfDBScriptStatement(int requestResourceType, String resourceName, Set<Entry<String, String>> queryCondParamsSet, List<Object> queryCondParameterValues, StringBuilder dbScriptStatement, String alias){
		if(StrUtils.isEmpty(alias)){
			alias = "";
		}else{
			alias += ".";
		}
		
		HbmConfPropMetadata[] hibernateDefineResourceProps = null;
		if(requestResourceType == ISysResource.TABLE){
			hibernateDefineResourceProps = HibernateUtil.getHibernateDefineResourceProps(resourceName);
		}
		
		String dbScriptStatements = null;
		IQueryCondFuncEntity queryCondFuncEntity = null;
		for (Entry<String, String> entry : queryCondParamsSet) {
			if(requestResourceType == ISysResource.TABLE){
				queryCondFuncEntity = new HqlQueryCondFuncEntity(HibernateUtil.getDefinePropMetadata(hibernateDefineResourceProps, entry.getKey()), entry.getValue());
			}else if(requestResourceType == ISysResource.SQLSCRIPT){
				queryCondFuncEntity = new SqlQueryCondFuncEntity(entry.getKey(), entry.getValue());
			}
			
			dbScriptStatements = BuiltinQueryCondFuncUtil.getBuiltinQueryConditionMethodInstance(queryCondFuncEntity.getMethodName())
					.toDBScriptStatement(queryCondFuncEntity.isInversion(), queryCondFuncEntity.getPropName(), 
							queryCondFuncEntity.getValues(), queryCondParameterValues, alias);
			dbScriptStatement.append(dbScriptStatements).append(" and ");
		}
		dbScriptStatement.setLength(dbScriptStatement.length() - 4);// 删除最后一个 and 
	}
}
