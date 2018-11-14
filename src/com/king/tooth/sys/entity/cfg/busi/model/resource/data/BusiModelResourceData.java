package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.cfg.sql.SqlExecutor;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.util.hibernate.HibernateUtil;
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
	public BusiModelResourceData(String busiModelResourceName, Object dataParentId, IJson datas) {
		this.busiModelResourceName = busiModelResourceName;
		this.datas = datas;
		this.dataParentId = dataParentId.toString();
	}
	
	// -----------------------------------------------------------
	private CfgBusiModelResRelations busiModelResRelations;
	/** 是否是表资源，如果不是，就是sql资源 */
	private boolean isTableResource;
	/** 引用的资源id */
	private String refResourceId;
	/** 引用的资源name */
	private String refResourceName;
	
	/**
	 * 进行业务资源数据验证
	 * @param busiModelResRelations
	 * @return
	 */
	public String doBusiResourceDataValid(CfgBusiModelResRelations busiModelResRelations) {
		try {
			this.busiModelResRelations = busiModelResRelations;
			CfgTable refTable = busiModelResRelations.getRefTable();
			refSql = busiModelResRelations.getRefSql();
			
			String validResult = null;
			if(refTable != null){
				isTableResource = true;
				refResourceId = refTable.getId();
				refResourceName = refTable.getResourceName();
				
				resourceMetadataInfos = TableResourceValidUtil.getTableResourceMetadataInfos(refResourceName);
				validResult = TableResourceValidUtil.validTableResourceMetadata("操作表资源["+refResourceName+"]时，", refResourceName, resourceMetadataInfos, datas, false, true);
			}else if(refSql != null){
				refResourceId = refSql.getId();
				refResourceName = refSql.getResourceName();
				
				resourceMetadataInfos = SqlResourceValidUtil.getSqlResourceParamsMetadataInfos(refSql);
				actualParamsList = SqlResourceValidUtil.initActualParamsList(null, datas);
				inSqlResultSetMetadataInfoList = SqlResourceValidUtil.getSqlInResultSetMetadataInfoList(refSql);
				
				validResult = SqlResourceValidUtil.doValidAndSetActualParams(refSql, false, actualParamsList, resourceMetadataInfos, inSqlResultSetMetadataInfoList);
			}
			return validResult;
		} finally{
			clearValidData();
		}
	}
	
	private List<ResourceMetadataInfo> resourceMetadataInfos;
	private List<List<CfgSqlParameter>> actualParamsList ;
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList ;
	/**
	 * 清空验证使用的数据
	 */
	private void clearValidData() {
		if(resourceMetadataInfos != null && resourceMetadataInfos.size() > 0){
			resourceMetadataInfos.clear();
		}
		if(inSqlResultSetMetadataInfoList != null && inSqlResultSetMetadataInfoList.size() > 0){
			for (List<ResourceMetadataInfo> inSqlResultSetMetadataInfos : inSqlResultSetMetadataInfoList) {
				if(inSqlResultSetMetadataInfos != null && inSqlResultSetMetadataInfos.size() > 0){
					inSqlResultSetMetadataInfos.clear();
				}
			}
			inSqlResultSetMetadataInfoList.clear();
		}
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
	/** 引用的sql资源 */
	private CfgSql refSql;
	/**
	 * sql语句中的参数值集合
	 * <p>可能有多个sql语句，所有用集合的集合封装参数</p>
	 */
	private List<List<Object>> sqlParameterValues;
	
	/**
	 * 保存业务数据
	 * <p>返回的object 要么是null，要么是JSONObject，要么是JSONArray</p>
	 */
	public Object saveBusiData(){
		// 操作结果对象
		Object resultDatas = null;
		
		String refParentResourcePropName = dataParentId==null?null:busiModelResRelations.getRefParentResourcePropName();
		rules = PropCodeRuleUtil.analyzeRules(refResourceId, refResourceName, datas);
		
		if(isTableResource){
			if(datas != null && datas.size() > 0){
				JSONObject data = null;
				Object operDataType = null;
				for(int i=0; i < datas.size(); i++){
					data = datas.get(i);
					operDataType = data.get(ResourcePropNameConstants.OPER_DATA_TYPE);
					if(operDataType == null){
						throw new NullPointerException("操作["+busiModelResourceName+"]业务模型资源，其中关联的["+refResourceName+"]表资源数据时，第"+(i+1)+"个数据传入操作类型[$operDataType$]的值为空，请检查");
					}
					if(dataParentId != null){
						data.put(refParentResourcePropName, dataParentId);
					}
					if(OperDataTypeConstants.ADD.equals(operDataType)){
						PropCodeRuleUtil.setTableResourceFinalCodeVal(data, i, rules);
						HibernateUtil.saveObject(refResourceName, data, null);
					}else if(OperDataTypeConstants.EDIT.equals(operDataType)){
						HibernateUtil.updateObject(refResourceName, data, null, null);
					}else if(OperDataTypeConstants.DELETE.equals(operDataType)){
						HibernateUtil.deleteObject(refResourceName, data);
					}
				}
			}
			resultDatas = datas.getJson();
		}else{
			sqlParameterValues = new ArrayList<List<Object>>(20);
			refSql.analysisFinalSqlScript(refSql, sqlParameterValues);
			resultDatas = new SqlExecutor().doExecuteModifySql(refSql, sqlParameterValues, datas, rules);
		}
		return resultDatas;
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		// 清除sql语句中的参数值集合
		if(sqlParameterValues.size() > 0){
			for(List<Object> list : sqlParameterValues){
				if(list != null && list.size() > 0){
					list.clear();
				}
			}
			sqlParameterValues.clear();
		}
		if(refSql != null){
			refSql.clear();
		}
		if(rules != null && rules.size() > 0){
			rules.clear();
		}
	}
}
