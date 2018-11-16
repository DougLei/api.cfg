package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import com.king.tooth.util.StrUtils;
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
		this.dataParentId = dataParentId==null?null:dataParentId.toString();
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
			CfgSql refSql = busiModelResRelations.getRefSqlForValid();
			
			String validResult = null;
			if(refTable != null){
				isTableResource = true;
				refResourceId = refTable.getId();
				refResourceName = refTable.getResourceName();
				
				validResult = TableResourceValidUtil.validTableResourceMetadata("操作业务资源["+busiModelResourceName+"]，关联的表资源["+refResourceName+"]时，", refResourceName, busiModelResRelations.getResourceMetadataInfos(), datas, false, true, true);
			}else if(refSql != null){
				refResourceId = refSql.getId();
				refResourceName = refSql.getResourceName();
				
				// 如果有父数据id，则要将其赋值到datas数据中，最后解析出实际传入的参数值集合
				String refParentResourcePropName = dataParentId==null?null:busiModelResRelations.getRefParentResourcePropName();
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
	 */
	public Object doOperBusiData(){
		// 操作结果对象
		Object resultDatas = null;
		
		rules = PropCodeRuleUtil.analyzeRules(refResourceId, refResourceName, datas);
		if(isTableResource){
			if(datas != null && datas.size() > 0){
				String refParentResourcePropName = dataParentId==null?null:busiModelResRelations.getRefParentResourcePropName();
				
				JSONObject data = null;
				Object operDataType = null;
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
			}
			resultDatas = datas.getJson();
		}else{
			CfgSql refSql = busiModelResRelations.getRefSqlForExecute();
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
		return resultDatas;
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
					subDataIds = HibernateUtil.executeListQueryByHqlArr(null,null, "select "+ResourcePropNameConstants.ID+" from "+busiModelResRelations.getRefResourceName()+" where "+busiModelResRelations.getRefParentResourcePropName()+" = ?", parentDataId);
					if(subDataIds != null && subDataIds.size() > 0){
						for (Object subDataId : subDataIds) {
							recursiveCascadeDeleteSub(subDataId, busiModelResRelations.getSubBusiModelResRelationsList());
						}
						
						deleteHqlBuffer.append("delete ").append(busiModelResRelations.getRefResourceName()).append(" where ").append(ResourcePropNameConstants.ID).append(" in (");
						for (Object subDataId : subDataIds) {
							deleteHqlBuffer.append("?,");
						}
						deleteHqlBuffer.setLength(deleteHqlBuffer.length()-1);
						HibernateUtil.executeUpdateByHql(SqlStatementTypeConstants.DELETE, deleteHqlBuffer.toString(), subDataIds);
						
						subDataIds.clear();
						deleteHqlBuffer.setLength(0);
					}
				}
			}
		}
	}
	private StringBuilder deleteHqlBuffer = new StringBuilder();
	
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
					count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from "+busiModelResRelations.getRefResourceName()+" where "+busiModelResRelations.getRefParentResourcePropName()+" = ?", parentDataId);
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
