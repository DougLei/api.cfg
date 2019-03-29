package com.api.sys.service.cfg;

import java.util.List;

import com.api.annotation.Service;
import com.api.constants.ResourceInfoConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 属性编码规则表Service
 * @author DougLei
 */
@Service
public class CfgPropCodeRuleService extends AService{
	
	/**
	 * 是否是表资源
	 * @param resourceType
	 * @return
	 */
	private boolean isTableResource(int resourceType){
		return resourceType == ResourceInfoConstants.TABLE;
	}
	
	/**
	 * 获取资源名
	 * @param refResourceId
	 * @return
	 */
	private Object getResourceName(Object refResourceId){
		return HibernateUtil.executeUniqueQueryBySqlArr(
				"select name from (" +
				"	select table_name, id from cfg_table" +
				"	union" +
				"	select name, id from cfg_sql" +
				") b where b.id =?",
				refResourceId);
	}
	
	/**
	 * 获取属性名
	 * @param refPropId
	 * @return
	 */
	private Object getPropName(Object refPropId){
		return HibernateUtil.executeUniqueQueryBySqlArr(
				"select name from (" +
				"	select column_name, id from cfg_column" +
				"	union" +
				"	select name, id from cfg_sql_parameter" +
				") b where b.id =?",
				refPropId);
	}
	
	/**
	 * 是否有规则引用要删除的资源或属性
	 * @param referenceRuleIds 引用的规则id，如果这个参数不为空，则要删除的资源或属性被其他规则引用
	 * @return
	 */
	private String isReferenced(List<Object[]> referenceRuleIds){
		if(referenceRuleIds != null && referenceRuleIds.size() > 0){
			Object[] ref = referenceRuleIds.get(0);
			if(isTableResource(Integer.valueOf(ref[2].toString()))){
				return "表资源["+getResourceName(ref[0])+"]中的列["+getPropName(ref[1])+"]，其编码规则引用到该条数据";
			}else{
				return "sql资源["+getResourceName(ref[0])+"]中的参数["+getPropName(ref[1])+"]，其编码规则引用到该条数据";
			}
		}
		return null;
	}
	
	/**
	 * 删除属性编码规则
	 * @param resourceType @see ResourceInfoConstants.TABLE/ResourceInfoConstants.SQL
	 * @param refResourceId 关联的资源id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String deletePropCodeRule(int resourceType, String refResourceId) {
		// 先判断表资源是否被其他规则占用
		if(isTableResource(resourceType)){
			List<Object[]> referenceRuleIds = HibernateUtil.executeListQueryBySqlArr(
					"select ref_resource_id, ref_prop_id, ref_prop_type from cfg_prop_code_rule where id in (" +
					"	select ref_prop_code_rule_id from cfg_prop_code_rule_detail where rec_seq_table_id=? or main_table_id=? or ref_table_id=?" +
					")", 
					refResourceId, refResourceId, refResourceId);
			String isReferenced = isReferenced(referenceRuleIds);
			if(isReferenced != null){
				return isReferenced;
			}
		}
		
		// 再获取该资源所有的属性，并循环操作：如果有规则，就删除这些规则
		String hql;
		if(isTableResource(resourceType)){
			hql = "select id from cfg_column where table_id =?";
		}else{
			hql = "select id from cfg_sql_parameter where sql_script_id =?";
		}
		List<String> propIds = HibernateUtil.executeListQueryBySqlArr(hql + " and project_id=? and customer_id=?", 
				refResourceId, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		String result = null;
		for (String propId : propIds) {
			result = deletePropCodeRule(resourceType, refResourceId, propId);
			if(result != null){
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 删除属性编码规则
	 * @param resourceType @see ResourceInfoConstants.TABLE/ResourceInfoConstants.SQL
	 * @param refResourceId 关联的资源id
	 * @param refPropId 关联的属性id
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public String deletePropCodeRule(int resourceType, String refResourceId, String refPropId) {
		// 先获取规则表的id，以及规则明细表的id，数据字典id
		Object[] ids = (Object[]) HibernateUtil.executeUniqueQueryBySqlArr(
							"select ref_prop_code_rule_id, id, code_data_dictionary_id from cfg_prop_code_rule_detail where ref_prop_code_rule_id in (" +
							"	select id from cfg_prop_code_rule where ref_resource_id=? and ref_prop_id=?" +
							")", 
							refResourceId, refPropId);
		if(ids == null || ids.length == 0){
			return null;
		}
		
		// 再判断属性是否被其他规则占用
		List<Object[]> referenceRuleIds = HibernateUtil.executeListQueryBySqlArr(
				"select ref_resource_id, ref_prop_id, ref_prop_type from cfg_prop_code_rule where id in (" +
				"	select ref_prop_code_rule_id from cfg_prop_code_rule_detail where rec_seq_code_column_id =? or rec_seq_parent_column_id =? or prop_group_seq_prop_ids like ? or main_table_code_column_id =? or main_table_cond_column_id =? or sub_table_cond_val_prop_id =? or ref_column_id =? or query_cond_column_id =? or query_cond_val_prop_id =? or order_by_column_id =? or " +
				")", 
				refPropId, refPropId, "%"+refPropId+"%", refPropId, refPropId, refPropId, refPropId, refPropId, refPropId, refPropId);
		String isReferenced = isReferenced(referenceRuleIds);
		if(isReferenced != null){
			return isReferenced;
		}
		
		// 接着判断该属性规则是否被其他规则引用
		referenceRuleIds = HibernateUtil.executeListQueryBySqlArr(
				"select ref_resource_id, ref_prop_id, ref_prop_type from cfg_prop_code_rule where ref_id=?", ids[0]);
		isReferenced = isReferenced(referenceRuleIds);
		if(isReferenced != null){
			return isReferenced + "(通过ref_id)";
		}
		
		doDeletePropCodeRule(ids[0], ids[1], ids[2]);
		return null;
	}
	
	
	/**
	 * 执行删除编码规则
	 * @param propCodeRuleId
	 * @param propCodeRuleDetailId
	 * @param codeDataDictionaryId
	 */
	private void doDeletePropCodeRule(Object propCodeRuleId, Object propCodeRuleDetailId, Object codeDataDictionaryId){
		// 1.删除序列信息
		HibernateUtil.executeUpdateBySqlArr(SqlStatementTypeConstants.DELETE, 
				"delete cfg_seq_info where ref_prop_code_rule_detail_id=?", 
				propCodeRuleDetailId);
		
		// 2.删除编码数据字典表
		if(StrUtils.notEmpty(codeDataDictionaryId)){
			HibernateUtil.executeUpdateBySqlArr(SqlStatementTypeConstants.DELETE, 
					"delete cfg_code_data_dictionary where id=? or parent_id=?", 
					codeDataDictionaryId, codeDataDictionaryId);
		}
		
		// 3.删除规则明细表
		HibernateUtil.executeUpdateBySqlArr(SqlStatementTypeConstants.DELETE, 
				"delete cfg_prop_code_rule_detail where id=?", 
				propCodeRuleDetailId);
		
		// 4.删除规则表
		HibernateUtil.executeUpdateBySqlArr(SqlStatementTypeConstants.DELETE, 
				"delete cfg_prop_code_rule where id=?", 
				propCodeRuleId);
	}
}