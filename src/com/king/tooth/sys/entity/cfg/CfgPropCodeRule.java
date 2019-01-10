package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.ijson.IJson;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;

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
	 * 关联的属性类型
	 * <p>1:column(列)、2:sqlparam(sql参数)</p>
	 */
	private Integer refPropType;
	/**
	 * 是否有效
	 * <p>默认值为0</p>
	 */
	private Integer isEnabled;
	/**
	 * 排序
	 */
	private Integer orderCode;
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
	public Integer getRefPropType() {
		return refPropType;
	}
	public void setRefPropType(Integer refPropType) {
		this.refPropType = refPropType;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRefPropName() {
		setRefPropName();
		return refPropName;
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(6+7);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联规则的资源id");
		refResourceIdColumn.setComments("关联规则的资源id");
		columns.add(refResourceIdColumn);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联规则的属性id");
		refPropIdColumn.setComments("关联规则的属性id");
		columns.add(refPropIdColumn);
		
		CfgColumn refPropTypeColumn = new CfgColumn("ref_prop_type", DataTypeConstants.INTEGER, 1);
		refPropTypeColumn.setName("关联的属性类型");
		refPropTypeColumn.setComments("1:column(列)、2:sqlparam(sql参数)");
		columns.add(refPropTypeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为0");
		isEnabledColumn.setDefaultValue("0");
		columns.add(isEnabledColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 500);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("属性编码规则表");
		table.setRemark("属性编码规则表");
		
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
	
	private void setRefPropName() {
		if(this.refPropName == null){
			Object refPropName=null;
			if(refPropType == REF_PROP_TYPE_COLUMN){
				refPropName = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropNameHql, refPropId);
				if(StrUtils.isEmpty(refPropName)){
					throw new NullPointerException("在创建属性编码值时，没有查询到id=["+refPropId+"]的列信息");
				}
			}else if(refPropType == REF_PROP_TYPE_SQLPARAM){
				refPropName = HibernateUtil.executeUniqueQueryByHqlArr(querySqlParameterPropNameHql, refPropId);
				if(StrUtils.isEmpty(refPropName)){
					throw new NullPointerException("在创建属性编码值时，没有查询到id=["+refPropId+"]的sql参数信息");
				}
			}else{
				throw new IllegalArgumentException("不存在refPropType="+refPropType+"的属性规则，请联系后端系统开发人员");
			}
			this.refPropName = refPropName.toString();
		}
	}
	private static final String queryColumnPropNameHql = "select propName from CfgColumn where " + ResourcePropNameConstants.ID+"=?";
	private static final String querySqlParameterPropNameHql = "select name from CfgSqlParameter where " + ResourcePropNameConstants.ID+"=?";
	
	/**
	 * 处理并获取最终的属性编码值
	 * <p>将值set到finalCodeVal属性中</p>
	 * @param ijson 
	 * @param resourceName 
	 */
	public void doProcessFinalCodeVal(IJson ijson, String resourceName) {
		setRefPropName();
		ruleDetails = HibernateUtil.extendExecuteListQueryByHqlArr(CfgPropCodeRuleDetail.class, null, null, queryRuleDetailsHql, id, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(ruleDetails != null && ruleDetails.size() > 0){
			int size = ijson.size();// 要生成编码值的数量 
			
			this.finalCodeVals = new ArrayList<String>(size);
			finalCodeValBuffer = new StringBuilder();
			int len = ruleDetails.size();
			
			Lock lock = lockPropCodeRule();
			try {
				JSONObject currentJsonObject = null;
				for(int j=0;j<size;j++){
					currentJsonObject = ijson.get(j);
					for(int i=0;i<len;i++){
						if(i>0){
							finalCodeValBuffer.append(ruleDetails.get(i-1).getLinkNextSymbol());
						}
						finalCodeValBuffer.append(ruleDetails.get(i).getCurrentStageCodeVal(resourceName, refPropType, currentJsonObject));
					}
					this.finalCodeVals.add(finalCodeValBuffer.toString());
					finalCodeValBuffer.setLength(0);
				}
			} finally{
				if(lock != null){
					lock.unlock();
				}
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
	
	/**
	 * 多线程锁定属性编码规则对象
	 * 防止生成序列、流水号时出现重复
	 * @return 
	 */
	private Lock lockPropCodeRule() {
		for(CfgPropCodeRuleDetail ruleDetail: ruleDetails){
			if(ruleDetail.isNeedLock()){
				Lock lock = PropCodeRuleUtil.getPropCodeRuleLock(id);
				lock.lock();
				return lock;
			}
		}
		return null;
	}
	
	// -----------------------------------------------------
	/**
	 * 关联的属性类型      1:column(列)
	 */
	public static final Integer REF_PROP_TYPE_COLUMN = 1;
	/**
	 * 关联的属性类型      2:sqlparam(sql参数)
	 */
	public static final Integer REF_PROP_TYPE_SQLPARAM = 2;
}
