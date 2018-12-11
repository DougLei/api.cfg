package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.cfg.sql.SqlExecutor;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.httpclient.HttpClientUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;
import com.king.tooth.web.entity.request.valid.data.util.SqlResourceValidUtil;
import com.king.tooth.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * 业务模型的资源数据
 * <p>和每个业务模型资源关系(CfgBusiModelResRelations)对应</p>
 * <p>@see CfgBusiModelResRelations</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BusiModelResourceData implements Serializable{
	
	private Map<String, String> tokenHeader;
	private String requestURL;
	/**
	 * 业务模型资源
	 */
	private String busiModelResourceName;
	/**
	 * 数据的父级id
	 * <p>如果是根，则该值为null</p>
	 */
	private String dataParentId;
	/**
	 * 实际的数据
	 */
	private IJson datas;
	
	/**
	 * 字段编码规则对象集合
	 */
	private List<CfgPropCodeRule> rules;

	// -----------------------------------------------------------
	public BusiModelResourceData() {
	}
	public BusiModelResourceData(String token, String requestURL, String busiModelResourceName, Object dataParentId, IJson datas) {
		tokenHeader = new HashMap<String, String>(1);
		tokenHeader.put("_token", token);
		this.requestURL = requestURL;
		this.busiModelResourceName = busiModelResourceName;
		this.datas = datas;
		this.dataParentId = dataParentId==null?null:dataParentId.toString();
	}
	
	// -----------------------------------------------------------
	public IJson getDatas() {
		return datas;
	}
	
	// -----------------------------------------------------------
	private CfgBusiModelResRelations busiModelResRelations;

	/** 是否是表资源，如果不是，就是sql资源 */
	private boolean isTableResource;
	/** 引用的资源id */
	private String refResourceId;
	/** 引用的资源name */
	private String refResourceName;
	/**是查询资源*/
	private boolean isQueryResource;
	
	/**
	 * 进行业务资源数据验证
	 * @param busiModelResRelations
	 * @return
	 */
	public String doBusiResourceDataValid(CfgBusiModelResRelations busiModelResRelations) {
		try {
			this.busiModelResRelations = busiModelResRelations;
			CfgTable refTable = busiModelResRelations.getRefTable();
			CfgSql refSql = busiModelResRelations.getRefSqlForValid();
			
			String validResult = null;
			if(refTable != null){
				isTableResource = true;
				refResourceId = refTable.getId();
				refResourceName = refTable.getResourceName();
				
				JSONArray selectDatas = new JSONArray(datas.size());
				for(int i=0;i<datas.size() ;i++){
					if(OperDataTypeConstants.SELECT.equals(datas.get(i).get(ResourcePropNameConstants.OPER_DATA_TYPE))){
						selectDatas.add(datas.remove(i--));
					}
				}
				validResult = TableResourceValidUtil.validTableResourceMetadata("操作业务资源["+busiModelResourceName+"]，关联的表资源["+refResourceName+"]时，", refResourceName, busiModelResRelations.getResourceMetadataInfos(), datas, false, true);
				if(validResult == null && selectDatas.size() > 0){
					datas.addAll(selectDatas);
					selectDatas.clear();
				}
			}else if(refSql != null){
				refResourceId = refSql.getId();
				refResourceName = refSql.getResourceName();
				
				if(!(isQueryResource = refSql.isSelectSql())){
					// 如果有父数据id，则要将其赋值到datas数据中，最后解析出实际传入的参数值集合
					String refParentResourcePropName = dataParentId==null?null:busiModelResRelations.getRefParentResourcePropName(isQueryResource);
					JSONObject data = null;
					for(int i=0; i < datas.size(); i++){
						data = datas.get(i);
						data.remove(ResourcePropNameConstants.OPER_DATA_TYPE);// 并且尝试移除$operDataType$的值
						if(dataParentId != null){
							data.put(refParentResourcePropName, dataParentId);
						}
					}
					
					actualParamsList = SqlResourceValidUtil.initActualParamsList(null, datas);
					validResult = SqlResourceValidUtil.doValidAndSetActualParams(refSql, false, actualParamsList, busiModelResRelations.getResourceMetadataInfos(), busiModelResRelations.getInSqlResultSetMetadataInfoList());
				}
			}
			return validResult;
		} finally{
			clearValidData();
		}
	}
	
	private List<List<CfgSqlParameter>> actualParamsList ;
	/**
	 * 清空验证使用的数据
	 */
	private void clearValidData() {
		if(actualParamsList != null && actualParamsList.size() > 0){
			for (List<CfgSqlParameter> actualParams : actualParamsList) {
				if(actualParams != null && actualParams.size() > 0){
					actualParams.clear();
				}
			}
			actualParamsList.clear();
		}
	}

	// -----------------------------------------------------------
	/**
	 * 操作业务数据
	 * <p>返回的object 要么是null，要么是JSONObject，要么是JSONArray</p>
	 * @param pid 
	 */
	public Object doOperBusiData(Object pid){
		// 操作结果对象
		Object resultDatas = null;
		
		if(isTableResource){
			if(datas != null && datas.size() > 0){
				String refParentResourcePropName = (dataParentId==null && pid==null)?null:busiModelResRelations.getRefParentResourcePropName(isQueryResource);
				
				Object operDataType = datas.get(0).get(ResourcePropNameConstants.OPER_DATA_TYPE);
				if(operDataType == null || OperDataTypeConstants.SELECT.equals(operDataType)){// operDataType==null的只会在查询的时候出现
					resultDatas = getQueryResultset(pid, refParentResourcePropName, "表");
				}else{
					JSONObject data = null;
					rules = PropCodeRuleUtil.analyzeRules(refResourceId, refResourceName, datas);
					for(int i=0; i < datas.size(); i++){
						data = datas.get(i);
						operDataType = data.remove(ResourcePropNameConstants.OPER_DATA_TYPE);
						if(dataParentId != null){
							data.put(refParentResourcePropName, dataParentId);
						}
						if(OperDataTypeConstants.ADD.equals(operDataType)){
							PropCodeRuleUtil.setTableResourceFinalCodeVal(data, i, rules);
							HibernateUtil.saveObject(refResourceName, data, null);
						}else if(OperDataTypeConstants.EDIT.equals(operDataType)){
							HibernateUtil.updateObject(refResourceName, data, null, null);
						}else if(OperDataTypeConstants.DELETE.equals(operDataType)){
							if(busiModelResRelations.getIsCascadeDelete() == 1){
								recursiveCascadeDeleteSub(data.get(ResourcePropNameConstants.ID), busiModelResRelations.getSubBusiModelResRelationsList());
							}else{
								validIsHaveSubDatas(data.get(ResourcePropNameConstants.ID), busiModelResRelations.getSubBusiModelResRelationsList());
							}
							HibernateUtil.deleteObject(refResourceName, data);// 删除主表数据
						}
					}
					resultDatas = datas.getJson();
				}
			}
		}else{
			CfgSql refSql = busiModelResRelations.getRefSqlForExecute();
			if(isQueryResource){
				String refParentResourcePropName = dataParentId==null?null:busiModelResRelations.getRefParentResourcePropName(isQueryResource);
				resultDatas = getQueryResultset(pid, refParentResourcePropName, "sql");
			}else{
				rules = PropCodeRuleUtil.analyzeRules(refResourceId, refResourceName, datas);
				
				List<List<Object>> sqlParameterValues = new ArrayList<List<Object>>(20);
				refSql.analysisFinalSqlScript(refSql, sqlParameterValues);
				resultDatas = new SqlExecutor().doExecuteModifySql(refSql, sqlParameterValues, datas, rules);
				
				refSql.clear();
				// 清除sql语句中的参数值集合
				if(sqlParameterValues != null && sqlParameterValues.size() > 0){
					for(List<Object> list : sqlParameterValues){
						if(list != null && list.size() > 0){
							list.clear();
						}
					}
					sqlParameterValues.clear();
				}
			}
		}
		return resultDatas;
	}
	
	private Object getQueryResultset(Object pid, String refParentResourcePropName, String desc){
		JSONObject data = datas.get(0);
		data.remove(ResourcePropNameConstants.OPER_DATA_TYPE);
		
		if(pid != null){
			data.put(refParentResourcePropName, pid);
		}
		JSONObject tmpData = JsonUtil.parseJsonObject(HttpClientUtil.doGetBasic(requestURL + "/common/" + refResourceName, data, tokenHeader));
		if(StrUtils.notEmpty(tmpData.get("message"))){
			throw new IllegalArgumentException("业务模型["+busiModelResourceName+"]，获取"+desc+"资源["+refResourceName+"]的查询结果信息时出现异常：" + tmpData.get("message"));
		}
		return tmpData.get("data");
	}
	
	/**
	 * 递归级联删除子表数据
	 * @param parentDataId
	 * @param subBusiModelResRelationsList
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void recursiveCascadeDeleteSub(Object parentDataId, List<CfgBusiModelResRelations> subBusiModelResRelationsList) {
		if(StrUtils.notEmpty(parentDataId) && subBusiModelResRelationsList != null && subBusiModelResRelationsList.size() > 0){
			List<Object> subDataIds = null;
			for (CfgBusiModelResRelations busiModelResRelations : subBusiModelResRelationsList) {
				if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_TABLE){
					resourceDataList = busiModelResRelations.getResourceDataList();
					if(resourceDataList != null && resourceDataList.size() >0){
						for (int i = 0; i < resourceDataList.size(); i++) {
							tmpDatas = resourceDataList.get(i).getDatas();
							// 这种写法，会将当前被删除对象的子数据，都直接从集合中删除，减少后续的处理，但是最后也不会将这些数据返回给前端
							if(tmpDatas != null && tmpDatas.size() > 0 && parentDataId.equals(tmpDatas.get(0).get(busiModelResRelations.getRefParentResourcePropName(isQueryResource)))){
								tmpDatas.clear();
								resourceDataList.remove(i--);
							}
							// 这种写法，会对每条数据进行处理，如果是当前被删除对象的子数据，则会修改该数据的状态为NO_OPER，最终数据也会完整的返回给前端
//							if(tmpDatas != null && tmpDatas.size() > 0 && parentDataId.equals(tmpDatas.get(0).get(busiModelResRelations.getRefParentResourcePropName()))){
//								for(int j=0;j<tmpDatas.size();j++){
//									tmpDatas.get(j).put(ResourcePropNameConstants.OPER_DATA_TYPE, OperDataTypeConstants.NO_OPER);
//								}
//							}
						}
					}
					
					subDataIds = HibernateUtil.executeListQueryByHqlArr(null,null, "select "+ResourcePropNameConstants.ID+" from "+busiModelResRelations.getRefResourceName()+" where "+busiModelResRelations.getRefParentResourcePropName(isQueryResource)+" = ?", parentDataId);
					if(subDataIds != null && subDataIds.size() > 0){
						for (Object subDataId : subDataIds) {
							recursiveCascadeDeleteSub(subDataId, busiModelResRelations.getSubBusiModelResRelationsList());
						}
						
						deleteHqlBuffer.append("delete ").append(busiModelResRelations.getRefResourceName()).append(" where ").append(ResourcePropNameConstants.ID).append(" in (");
						for (Object subDataId : subDataIds) {
							deleteHqlBuffer.append("?,");
						}
						deleteHqlBuffer.setLength(deleteHqlBuffer.length()-1);
						deleteHqlBuffer.append(")");
						HibernateUtil.executeUpdateByHql(SqlStatementTypeConstants.DELETE, deleteHqlBuffer.toString(), subDataIds);
						
						subDataIds.clear();
						deleteHqlBuffer.setLength(0);
					}
				}
			}
		}
	}
	private StringBuilder deleteHqlBuffer = new StringBuilder();
	private List<BusiModelResourceData> resourceDataList;
	private IJson tmpDatas;
	
	/**
	 * 验证是否有子表数据
	 * @param parentDataId
	 * @param busiModelResRelations
	 */
	private void validIsHaveSubDatas(Object parentDataId, List<CfgBusiModelResRelations> subBusiModelResRelationsList) {
		if(subBusiModelResRelationsList != null && subBusiModelResRelationsList.size() > 0){
			long count = 0;
			for (CfgBusiModelResRelations busiModelResRelations : subBusiModelResRelationsList) {
				if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_TABLE){
					count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from "+busiModelResRelations.getRefResourceName()+" where "+busiModelResRelations.getRefParentResourcePropName(isQueryResource)+" = ?", parentDataId);
					if(count> 0){
						throw new IllegalArgumentException("删除id=["+parentDataId+"]的["+this.busiModelResRelations.getRefResourceName()+"]资源失败，其下存在"+count+"条["+busiModelResRelations.getRefResourceName()+"}资源数据");
					}
				}
			}
		}
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		if(rules != null && rules.size() > 0){
			rules.clear();
		}
	}
}
