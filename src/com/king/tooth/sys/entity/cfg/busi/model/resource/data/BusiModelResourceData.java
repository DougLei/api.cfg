package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgTable;
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
	public BusiModelResourceData(Object dataParentId, IJson datas) {
		this.datas = datas;
		this.dataParentId = dataParentId.toString();
	}
	
	// -----------------------------------------------------------
	public String getDataParentId() {
		return dataParentId;
	}
	public void setDataParentId(String dataParentId) {
		this.dataParentId = dataParentId;
	}
	public IJson getDatas() {
		return datas;
	}
	public void setDatas(IJson datas) {
		this.datas = datas;
	}
	public List<CfgPropCodeRule> getRules() {
		return rules;
	}
	public void setRules(List<CfgPropCodeRule> rules) {
		this.rules = rules;
	}

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
		this.busiModelResRelations = busiModelResRelations;
		CfgTable table = busiModelResRelations.getRefTable();
		CfgSql sql = busiModelResRelations.getRefSql();
		
		if(table != null){
			isTableResource = true;
			refResourceId = table.getId();
			refResourceName = table.getResourceName();
			
			return TableResourceValidUtil.validTableResourceMetadata("操作表资源["+refResourceName+"]时，", refResourceName, TableResourceValidUtil.getTableResourceMetadataInfos(refResourceName), datas, false, true);
		}else if(sql != null){
			refResourceId = sql.getId();
			refResourceName = sql.getResourceName();
			
			return SqlResourceValidUtil.doValidAndSetActualParams(sql, false, SqlResourceValidUtil.initActualParamsList(null, datas), SqlResourceValidUtil.getSqlResourceParamsMetadataInfos(sql), SqlResourceValidUtil.getSqlInResultSetMetadataInfoList(sql));
		}
		throw new NullPointerException("进行业务资源数据验证时，传入对象[busiModelResRelations的refTable、refSql]都为空，请联系后端系统开发人员");
	}
	
	/**
	 * 保存业务数据
	 */
	public void saveBusiData(){
		if(datas != null && datas.size() > 0){
			rules = PropCodeRuleUtil.analyzeRules(refResourceId, refResourceName, datas);
			String refParentResourcePropName = busiModelResRelations.getRefParentResourcePropName();
			Object operDataType = null;
			Object operDataId = null;
			
			JSONObject data = null;
			for(int i=0; i < datas.size(); i++){
				data = datas.get(i);
				data.put(refParentResourcePropName, dataParentId);
				operDataType = data.get(ResourcePropNameConstants.OPER_DATA_TYPE);
				
				if(operDataType == null || OperDataTypeConstants.ADD.equals(operDataType)){
					PropCodeRuleUtil.setTableResourceFinalCodeVal(data, i, rules);
					HibernateUtil.saveObject(refResourceName, data, null);
				}else if(OperDataTypeConstants.EDIT.equals(operDataType)){
					HibernateUtil.updateObject(refResourceName, data, null, null);
				}else if(OperDataTypeConstants.DELETE.equals(operDataType)){
					operDataId = data.get(ResourcePropNameConstants.ID);
					
				}
				
//				
//				saveData(requestBody.getRouteBody().getResourceName(), data);
			}
			
			
			if(isTableResource){
				
				
				
			}else{
				
			}
		}
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		// TODO Auto-generated method stub
	}
}
