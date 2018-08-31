package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段编码规则表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
public class CfgColumnCodeRule extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{

	/**
	 * 关联规则的表id
	 */
	private String refTableId;
	/**
	 * 关联规则的列id
	 */
	private String refColumnId;
	/**
	 * 备注
	 */
	private String remark;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 关联规则的列的属性名
	 */
	@JSONField(serialize = false)
	private String refPropName;
	
	/**
	 * 最终的字段编码值
	 * <p>因为可以批量添加数据，这个时候就会有多个结果值，所以用集合</p>
	 */
	@JSONField(serialize = false)
	private List<String> finalCodeVals;
	
	public String getRefTableId() {
		return refTableId;
	}
	public void setRefTableId(String refTableId) {
		this.refTableId = refTableId;
	}
	public String getRefColumnId() {
		return refColumnId;
	}
	public void setRefColumnId(String refColumnId) {
		this.refColumnId = refColumnId;
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

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_COLUMN_CODE_RULE", 0);
		table.setName("字段编码规则表");
		table.setComments("字段编码规则表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinDataType.STRING, 32);
		refTableIdColumn.setName("关联规则的表id");
		refTableIdColumn.setComments("关联规则的表id");
		columns.add(refTableIdColumn);
		
		ComColumndata refColumnIdColumn = new ComColumndata("ref_column_id", BuiltinDataType.STRING, 32);
		refColumnIdColumn.setName("关联规则的列id");
		refColumnIdColumn.setComments("关联规则的列id");
		columns.add(refColumnIdColumn);
		
		ComColumndata remarkColumn = new ComColumndata("remark", BuiltinDataType.STRING, 500);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "CFG_COLUMN_CODE_RULE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgColumnCodeRule";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refTableId)){
			return "关联规则的表id不能为空";
		}
		if(StrUtils.isEmpty(refColumnId)){
			return "关联规则的列id不能为空";
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
	 * 获取指定下标的最终的字段编码值
	 * @param index
	 * @return
	 */
	public String getFinalCodeVal(int index) {
		if(finalCodeVals == null || finalCodeVals.size() < (index+1)){
			return null;
		}
		return finalCodeVals.get(index);
	}
	
	/**
	 * 处理并获取最终的字段编码值
	 * <p>将值set到finalCodeVal属性中</p>
	 * @param codeValCount 要生成编码值的数量 
	 */
	public void doProcessFinalCodeVal(int codeValCount) {
		ruleDetails = HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumnCodeRuleDetail.class, null, null, queryRuleDetailsHql, id, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(ruleDetails != null && ruleDetails.size() > 0){
			this.finalCodeVals = new ArrayList<String>(codeValCount);
			finalCodeValBuffer = new StringBuilder();
			int len = ruleDetails.size();
			int lastIndex = len-1;
			
			for(int j=0;j<codeValCount;j++){
				for(int i=0;i<len;i++){
					if(i>0 && i<lastIndex){
						finalCodeValBuffer.append(ruleDetails.get(i-1).getLinkNextSymbol());
					}
					finalCodeValBuffer.append(ruleDetails.get(i).getCurrentStageCodeVal());
				}
				this.finalCodeVals.add(finalCodeValBuffer.toString());
				finalCodeValBuffer.setLength(0);
			}
			ruleDetails.clear();
		}
	}
	// 最终编码值得缓存
	private StringBuilder finalCodeValBuffer;
	// 字段编码规则明细集合
	private List<CfgColumnCodeRuleDetail> ruleDetails;
	// 查询字段编码规则明细集合的hql
	private static final String queryRuleDetailsHql = "from CfgColumnCodeRuleDetail where columnCodeRuleId=? and projectId=? and customerId=? order by orderCode asc";
}
