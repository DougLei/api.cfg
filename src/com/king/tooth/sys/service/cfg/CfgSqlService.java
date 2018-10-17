package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.sys.service.AService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql脚本信息表Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
public class CfgSqlService extends AService {
	
	/**
	 * 根据id，获取sql脚本资源对象
	 * @param sqlScriptId
	 * @return
	 */
	public ComSqlScript findSqlScriptResourceById(String sqlScriptId){
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(sqlScript == null){
			throw new NullPointerException("不存在请求的sql脚本资源，请联系管理员");
		}
		if(sqlScript.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的sql脚本资源被禁用，请联系管理员");
		}
		sqlScript.setSqlParams(findSqlParams(sqlScriptId));
		sqlScript.setInSqlResultsets(findInSqlResultsetsList(sqlScript));
		sqlScript.setOutSqlResultsetsList(findOutSqlResultsetsList(sqlScript));
		return sqlScript;
	}
	
	/**
	 * 根据sql脚本id，查询对应的参数集合
	 * @param sqlScriptId
	 * @return
	 */
	private List<ComSqlScriptParameter> findSqlParams(String sqlScriptId) {
		return HibernateUtil.extendExecuteListQueryByHqlArr(
				ComSqlScriptParameter.class, null, null, "from ComSqlScriptParameter where sqlScriptId = ? and projectId=? and customerId=? order by orderCode asc", sqlScriptId, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
	}
	
	/**
	 * 验证sql脚本资源名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validSqlScriptResourceNameIsExists(ComSqlScript sqlScript) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComSqlScript where sqlScriptResourceName = ? and createUserId = ? and customerId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
			return "您已经创建过相同sql脚本资源名["+sqlScript.getSqlScriptResourceName()+"]的数据";
		}
		count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysResource where resourceName = ? and projectId = ? and customerId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
			return "系统中已经存在相同的资源名["+sqlScript.getSqlScriptResourceName()+"]的数据，请修sql脚本资源名";
		}
		return null;
	}
	
	/**
	 * 验证sql脚本关联的项目是否存在
	 * @param project
	 * @return operResult
	 */
	private String validSqlScriptRefProjIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComProject where id = ?", projectId);
		if(count != 1){
			return "sql脚本关联的，id为["+projectId+"]的项目信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证一个项目中，是否存在同名的sql脚本资源名
	 * @param sqlScriptResourceName
	 * @param projectId
	 * @return
	 */
	private String validSameResourceNameSqlScriptInProject(String sqlScriptResourceName, String projectId) {
		String hql = "select count(cs."+ResourcePropNameConstants.ID+") from " +
				"ComProject p, CfgProjectSqlLinks ps, ComSqlScript cs " +
				"where p.id = '"+projectId+"' and p.id = ps.leftId and cs.id = ps.rightId and cs.sqlScriptResourceName = '"+sqlScriptResourceName+"'";
		long count = (long) HibernateUtil.executeUniqueQueryByHql(hql, null);
		if(count > 0){
			return "项目关联的sql脚本中已经存在sql脚本资源名为["+sqlScriptResourceName+"]的数据";
		}
		return null;
	}
	
	/**
	 * 保存sql脚本
	 * @param sqlScript
	 * @return
	 */
	public Object saveSqlScript(ComSqlScript sqlScript) {
		String operResult = validSqlScriptResourceNameIsExists(sqlScript);
		if(operResult == null){
			String projectId = CurrentThreadContext.getConfProjectId();
			
			if(StrUtils.isEmpty(projectId)){
				return "sql脚本关联的项目id不能为空！";
			}
			operResult = validSqlScriptRefProjIsExists(projectId);
			if(operResult == null){
				operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
			}
			
			if(operResult == null){
				if(sqlScript.getIsImmediateCreate() == 1 
						&& (SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getSqlScriptType()) 
								|| SqlStatementTypeConstants.VIEW.equals(sqlScript.getSqlScriptType()))){
					DBUtil.createObject(sqlScript);
					sqlScript.setIsCreated(1);
				}
				
				JSONObject sqlScriptJsonObject = HibernateUtil.saveObject(sqlScript, null);
				String sqlScriptId = sqlScriptJsonObject.getString(ResourcePropNameConstants.ID);
				
				// 因为保存资源数据的时候，需要sqlScript对象的id，所以放到最后
				sqlScript.setId(sqlScriptId);
				BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).saveSysResource(sqlScript);
			
				HibernateUtil.saveDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
				return sqlScriptJsonObject;
			}
		}
		return operResult;
	}

	/**
	 * 修改sql脚本
	 * @param sqlScript
	 * @return
	 */
	public Object updateSqlScript(ComSqlScript sqlScript) {
		ComSqlScript oldSqlScript = getObjectById(sqlScript.getId(), ComSqlScript.class);
		String operResult = null;
		if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
			operResult = validSqlScriptResourceNameIsExists(sqlScript);
		}
		
		if(operResult == null){
			String projectId = CurrentThreadContext.getConfProjectId();
			
			if(StrUtils.isEmpty(projectId)){
				return "sql脚本关联的项目id不能为空！";
			}
			operResult = validSqlScriptRefProjIsExists(projectId);
			if(operResult == null && !oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
				operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
			}
				
			if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName()) || !oldSqlScript.getRequestMethod().equals(sqlScript.getRequestMethod())){
				// 如果修改了sql脚本的资源名，也要同步修改SysResource表中的资源名
				BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).updateResourceInfo(sqlScript.getId(), sqlScript.getSqlScriptResourceName(), sqlScript.getRequestMethod());
			}
			if(operResult == null){
				if(sqlScript.getIsImmediateCreate() == 1 
						&& (SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getSqlScriptType()) 
								|| SqlStatementTypeConstants.VIEW.equals(sqlScript.getSqlScriptType()))){
					DBUtil.createObject(sqlScript);
					sqlScript.setIsCreated(1);
				}
				return HibernateUtil.updateObject(sqlScript, null);
			}
		}
		return operResult;
	}
	
	/**
	 * 删除sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String deleteSqlScript(String sqlScriptId) {
		ComSqlScript sql = getObjectById(sqlScriptId, ComSqlScript.class);
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("CfgProjectSqlLinks", null, sqlScriptId);
		if(datalinks.size() > 1){
			List<Object> projectIds = new ArrayList<Object>(datalinks.size());
			StringBuilder hql = new StringBuilder("select projName from ComProject where id in (");
			for (JSONObject json : datalinks) {
				projectIds.add(json.getString("leftId"));
				hql.append("?,");
			}
			hql.setLength(hql.length() - 1);
			hql.append(")");
			
			List<Object> projNames = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), projectIds);
			projectIds.clear();
			return "该sql脚本关联多个项目，无法删除，请先取消和其他项目的关联，关联的项目包括：" + projNames;
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete ComSqlScript where "+ResourcePropNameConstants.ID+" = ?", sql.getId());
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete ComSqlScriptParameter where sqlScriptId = ?", sql.getId());
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgSqlResultset where sqlScriptId = ?", sql.getId());
		HibernateUtil.deleteDataLinks("CfgProjectSqlLinks", null, sqlScriptId);
		
		BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).deleteSysResource(sqlScriptId);
			
		// 删除sql脚本资源时，如果是视图、存储过程等，还需要drop对应的对象【删除数据库对象】
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getSqlScriptType()) || SqlStatementTypeConstants.VIEW.equals(sql.getSqlScriptType())){
			DBUtil.dropObject(sql);
		}

		sql.clear();
		return null;
	}

	/**
	 * 创建sql脚本对象
	 * <p>存储过程、视图等</p>
	 * @param ijson
	 * @return
	 */
	public Object createSqlObject(IJson ijson) {
		int len = ijson.size();
		List<ComSqlScript> sqls = new ArrayList<ComSqlScript>(len);
		
		StringBuilder updateHql = new StringBuilder("update ComSqlScript set isCreated=1 where id in (");
		ComSqlScript tmpSql;
		for(int i=0;i<len ;i++){
			tmpSql = getObjectById(ijson.get(i).getString(ResourcePropNameConstants.ID), ComSqlScript.class);
			if(SqlStatementTypeConstants.PROCEDURE.equals(tmpSql.getSqlScriptType()) || SqlStatementTypeConstants.VIEW.equals(tmpSql.getSqlScriptType())){
				sqls.add(tmpSql);
				updateHql.append("'").append(tmpSql.getId()).append("',");
			}else{
				return "创建资源名为["+tmpSql.getSqlScriptResourceName()+"]的sql对象出错，系统目前只支持在数据库中创建[create] [存储过程] 和 [视图]";
			}
		}
		updateHql.setLength(updateHql.length()-1);
		updateHql.append(")");
		
		try {
			DBUtil.createObjects(sqls);
			HibernateUtil.executeUpdateByHql(SqlStatementTypeConstants.UPDATE, updateHql.toString(), null);
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg(e);
		} finally {
			for (ComSqlScript sql : sqls) {
				sql.clear();
			}
		}
		return null;
	}
	
	
	
	/**
	 * 删除sql脚本对象
	 * <p>存储过程、视图等</p>
	 * @param ijson
	 * @return
	 */
	public Object dropSqlObject(IJson ijson) {
		int len = ijson.size();
		List<ComSqlScript> sqls = new ArrayList<ComSqlScript>(len);
		
		StringBuilder updateHql = new StringBuilder("update ComSqlScript set isCreated=0 where id in (");
		ComSqlScript tmpSql;
		for(int i=0;i<len ;i++){
			tmpSql = getObjectById(ijson.get(i).getString(ResourcePropNameConstants.ID), ComSqlScript.class);
			if(SqlStatementTypeConstants.PROCEDURE.equals(tmpSql.getSqlScriptType()) || SqlStatementTypeConstants.VIEW.equals(tmpSql.getSqlScriptType())){
				sqls.add(tmpSql);
				updateHql.append("'").append(tmpSql.getId()).append("',");
			}else{
				return "删除资源名为["+tmpSql.getSqlScriptResourceName()+"]的sql对象出错，系统目前只支持在数据库中删除[drop] [存储过程] 和 [视图]";
			}
		}
		updateHql.setLength(updateHql.length()-1);
		updateHql.append(")");
		
		try {
			DBUtil.dropObjects(sqls);
			HibernateUtil.executeUpdateByHql(SqlStatementTypeConstants.UPDATE, updateHql.toString(), null);
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg(e);
		} finally {
			for (ComSqlScript sql : sqls) {
				sql.clear();
			}
		}
		return null;
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String addProjSqlScriptRelation(String projectId, String sqlScriptId) {
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		String operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
		if(operResult == null){
			HibernateUtil.saveDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
		}
		return operResult;
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String cancelProjSqlScriptRelation(String projectId, String sqlScriptId) {
		HibernateUtil.deleteDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
		return null;
	}
	
	// ---------------------------------------------------------------------------------------------------------
	
	/**
	 * 根据id，验证是否存在对应的sql脚本对象
	 * @param sqlId
	 * @return
	 */
	private boolean validSqlIsExistsById(String sqlId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComSqlScript where "+ResourcePropNameConstants.ID+"=? and customerId = ?", sqlId, CurrentThreadContext.getCustomerId());
		if(count > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加sql脚本参数
	 * @param sqlParam
	 * @return
	 */
	public Object saveSqlScriptParameter(ComSqlScriptParameter sqlParam) {
		if(!validSqlIsExistsById(sqlParam.getSqlScriptId())){
			return "不存在id为["+sqlParam.getSqlScriptId()+"]的sql脚本对象";
		}
		return HibernateUtil.saveObject(sqlParam, null);
	}

	/**
	 * 修改sql脚本参数
	 * @param sqlParam
	 * @return
	 */
	public Object updateSqlScriptParameter(ComSqlScriptParameter sqlParam) {
		if(!validSqlIsExistsById(sqlParam.getSqlScriptId())){
			return "不存在id为["+sqlParam.getSqlScriptId()+"]的sql脚本对象";
		}
		return HibernateUtil.updateObject(sqlParam, null);
	}

	/**
	 * 删除sql脚本参数
	 * @param sqlScriptParameterIds
	 * @return
	 */
	public String deleteSqlScriptParameter(String sqlScriptParameterIds) {
		return deleteDataById("ComSqlScriptParameter", sqlScriptParameterIds);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 根据sql脚本id，查询对应的传入表类型的结果集信息集合
	 * @param sqlScript
	 * @return
	 */
	private List<CfgSqlResultset> findInSqlResultsetsList(ComSqlScript sqlScript) {
		List<CfgSqlResultset> sqlResultsetsList = null;
		if(SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getSqlScriptType())){
			List<ComSqlScriptParameter> sqlParams = sqlScript.getSqlParams();
			if(sqlParams != null && sqlParams.size() > 0){
				sqlResultsetsList = new ArrayList<CfgSqlResultset>(sqlParams.size());
				CfgSqlResultset cfgSqlResultset = null;
				for (ComSqlScriptParameter sqlParam : sqlParams) {
					// 只有表类型，再查询其结果集信息
					if(sqlParam.getIsTableType() == 1){
						cfgSqlResultset = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgSqlResultset.class, "from CfgSqlResultset where sqlScriptId = ? and sqlParameterId = ? and inOut = ? and projectId=? and customerId=?", sqlScript.getId(), sqlParam.getId(), CfgSqlResultset.IN, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
						cfgSqlResultset.setInSqlResultSetMetadataInfos(HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, queryTableMetadataInfosHql, cfgSqlResultset.getTableId()));
						if(cfgSqlResultset.getInSqlResultSetMetadataInfos() == null || cfgSqlResultset.getInSqlResultSetMetadataInfos().size() == 0){
							throw new IllegalArgumentException("存储过程["+sqlScript.getObjectName()+"]，参数["+sqlParam.getParameterName()+"]，引用的表类型没有查询到任何列信息，请联系系统管理员");
						}
						sqlResultsetsList.add(cfgSqlResultset);
					}
				}
			}
		}
		return sqlResultsetsList;
	}
	/** 查询表资源元数据信息集合的hql */
	private static final String queryTableMetadataInfosHql = "select new map(columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName) from ComColumndata where tableId=? and isEnabled=1 and operStatus="+ComColumndata.CREATED+" order by orderCode asc";
	
	/**
	 * 根据sql脚本id，查询对应的传出表类型的结果集信息集合
	 * @param sqlScript
	 * @return
	 */
	private List<List<CfgSqlResultset>> findOutSqlResultsetsList(ComSqlScript sqlScript) {
		List<List<CfgSqlResultset>> sqlResultsetsList = null;
		
		// 查询语句，直接查询结果集
		if(SqlStatementTypeConstants.SELECT.equals(sqlScript.getSqlScriptType())){
			sqlResultsetsList = new ArrayList<List<CfgSqlResultset>>(1);
			sqlResultsetsList.add(HibernateUtil.extendExecuteListQueryByHqlArr(
					CfgSqlResultset.class, null, null, "from CfgSqlResultset where sqlScriptId = ? and inOut = 2 and projectId=? and customerId=? order by orderCode asc", sqlScript.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId()));
		}
		
		// 存储过程，要判断是否有返回结果集，再查询对应的结果集
		else if(SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getSqlScriptType())){
			// oracle的存储过程返回结果集，通过输出参数
			if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqlScript.getDbType())){
				List<ComSqlScriptParameter> sqlParams = sqlScript.getSqlParams();
				if(sqlParams != null && sqlParams.size() > 0){
					sqlResultsetsList = new ArrayList<List<CfgSqlResultset>>(sqlParams.size());
					for (ComSqlScriptParameter sqlParam : sqlParams) {
						// 只有表类型，再查询其结果集信息
						if(sqlParam.getIsTableType() == 1){
							sqlResultsetsList.add(HibernateUtil.extendExecuteListQueryByHqlArr(
									CfgSqlResultset.class, null, null, "from CfgSqlResultset where sqlScriptId = ? and sqlParameterId = ? and inOut = 2 and projectId=? and customerId=? order by orderCode asc", sqlScript.getId(), sqlParam.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId()));
						}
					}
				}
				
			}
			// sqlserver的存储过程返回结果集，不需要参数，所以不用参数做查询
			else if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqlScript.getDbType())){
				sqlResultsetsList = new ArrayList<List<CfgSqlResultset>>(5);
				List<CfgSqlResultset> sqlResultsets = HibernateUtil.extendExecuteListQueryByHqlArr(
						CfgSqlResultset.class, null, null, "from CfgSqlResultset where sqlScriptId = ? and inOut = 2 and projectId=? and customerId=? order by batchOrder asc, orderCode asc", sqlScript.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
				if(sqlResultsets != null && sqlResultsets.size() > 0){
					int size = sqlResultsets.size();
					int count = size;
					CfgSqlResultset sqlResultset;
					List<CfgSqlResultset> tmpSqlResultsets = new ArrayList<CfgSqlResultset>(count);
					sqlResultsetsList.add(tmpSqlResultsets);// [第一次，将结果信息加入到集合中] 把属于同一个结果集信息集合的加入到大的集合中
					
					Integer batchOrder = null;
					while(sqlResultsets.size() > 0){
						count--;
						sqlResultset = sqlResultsets.get(0);
						if(batchOrder == null){
							batchOrder = sqlResultset.getBatchOrder();
						}
						if(batchOrder != sqlResultset.getBatchOrder()){
							sqlResultsetsList.add(tmpSqlResultsets);// [后续如果有新的，再加入到集合中] 把属于同一个结果集信息集合的加入到大的集合中
							
							batchOrder = sqlResultset.getBatchOrder();
							tmpSqlResultsets = new ArrayList<CfgSqlResultset>(count);
						}
						tmpSqlResultsets.add(sqlResultsets.remove(0));
					}
				}
			}
		}
		return sqlResultsetsList;
	}
}
