package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性编码规则表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropCodeRule extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联规则的资源id
	 */
	private String refResourceId;
	/**
	 * 关联规则的属性id
	 */
	private String refPropId;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	private Integer isEnabled;
	/**
	 * 备注
	 */
	private String remark;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 关联规则的属性名
	 */
	@JSONField(serialize = false)
	private String refPropName;
	
	/**
	 * 最终的属性编码值
	 * <p>因为可以批量添加数据，这个时候就会有多个结果值，所以用集合</p>
	 */
	@JSONField(serialize = false)
	private List<String> finalCodeVals;
	
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public String getRefPropId() {
		return refPropId;
	}
	public void setRefPropId(String refPropId) {
		this.refPropId = refPropId;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRefPropName() {
		return refPropName;
	}
	public void setRefPropName(String refPropName) {
		this.refPropName = refPropName;
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联规则的资源id");
		refResourceIdColumn.setComments("关联规则的资源id");
		columns.add(refResourceIdColumn);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联规则的属性id");
		refPropIdColumn.setComments("关联规则的属性id");
		columns.add(refPropIdColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 500);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("属性编码规则表");
		table.setComments("属性编码规则表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_PROP_CODE_RULE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgPropCodeRule";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refResourceId)){
			return "关联规则的资源id不能为空";
		}
		if(StrUtils.isEmpty(refPropId)){
			return "关联规则的属性id不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	/**
	 * 清空
	 */
	public void clear() {
		if(finalCodeVals != null){
			finalCodeVals.clear();
		}
	}
	
	/**
	 * 获取指定下标的最终的属性编码值
	 * @param index
	 * @return
	 */
	public String getFinalCodeVal(int index) {
		if(finalCodeVals == null || finalCodeVals.size() == 0){
			return null;
		}
		if(finalCodeVals.size() <= index){
			throw new IllegalArgumentException("调用资源自动生成编码值时，finalCodeVals的长度，小于实际传入的index值，请联系后端系统开发人员");
		}
		return finalCodeVals.get(index);
	}
	
	/**
	 * 处理并获取最终的属性编码值
	 * <p>将值set到finalCodeVal属性中</p>
	 * @param ijson 
	 * @param resourceName 
	 */
	public void doProcessFinalCodeVal(IJson ijson, String resourceName) {
		ruleDetails = HibernateUtil.extendExecuteListQueryByHqlArr(CfgPropCodeRuleDetail.class, null, null, queryRuleDetailsHql, id, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(ruleDetails != null && ruleDetails.size() > 0){
			int size = ijson.size();// 要生成编码值的数量 
			
			this.finalCodeVals = new ArrayList<String>(size);
			finalCodeValBuffer = new StringBuilder();
			int len = ruleDetails.size();
			int lastIndex = len-1;
			
			JSONObject currentJsonObject;
			for(int j=0;j<size;j++){
				currentJsonObject = ijson.get(j);
				for(int i=0;i<len;i++){
					if(i>0 && i<lastIndex){
						finalCodeValBuffer.append(ruleDetails.get(i-1).getLinkNextSymbol());
					}
					finalCodeValBuffer.append(ruleDetails.get(i).getCurrentStageCodeVal(resourceName, currentJsonObject));
				}
				this.finalCodeVals.add(finalCodeValBuffer.toString());
				finalCodeValBuffer.setLength(0);
			}
			ruleDetails.clear();
		}
	}
	// 最终编码值得缓存
	private StringBuilder finalCodeValBuffer;
	// 属性编码规则明细集合
	private List<CfgPropCodeRuleDetail> ruleDetails;
	// 查询属性编码规则明细集合的hql
	private static final String queryRuleDetailsHql = "from CfgPropCodeRuleDetail where refPropCodeRuleId=? and projectId=? and customerId=? order by orderCode asc";
}
