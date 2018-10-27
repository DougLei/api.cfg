package com.king.tooth.sys.entity.cfg.propextend.query.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgPropExtendConf;
import com.king.tooth.sys.entity.cfg.propextend.query.data.param.QueryPropExtendConfDataParam;
import com.king.tooth.sys.service.sys.imports.template.data.query.AImportTemplateDataQueryService;
import com.king.tooth.sys.service.sys.imports.template.data.query.SysUserImportTemplateDataQuery;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 根据属性扩展配置，查询相应的数据集合
 * @author DougLei
 */
@SuppressWarnings({"serial", "unchecked"})
public class PropExtendConfQueryData implements Serializable{

	public PropExtendConfQueryData(CfgPropExtendConf propExtendConf, QueryPropExtendConfDataParam queryPropExtendConfDataParam) {
		this.propExtendConf = propExtendConf;
		this.queryPropExtendConfDataParam = queryPropExtendConfDataParam;
		
		queryPropExtendConfDataParam.processConditionHqlAndConditionValues();
		this.querySize = queryPropExtendConfDataParam.getQuerySize();
		this.queryDataHql = queryPropExtendConfDataParam.getConditionHql();
		
		calcQueryDataTotalCount();
		setImportTemplateDataQueryServiceInstance();
	}
	
	private CfgPropExtendConf propExtendConf;
	private QueryPropExtendConfDataParam queryPropExtendConfDataParam;
	private AImportTemplateDataQueryService importTemplateDataQueryService;
	
	/**
	 * 一次查询的数量
	 * <p>进行分页查询，减少内存消耗，默认为100</p>
	 * <p>如果为-1，则不进行分页查询，一次查询全部</p>
	 */
	private int querySize;
	/**
	 * 查询数据的hql
	 * <p>从from 到where 条件</p>
	 */
	private String queryDataHql;
	/**
	 * 条件查询值集合
	 */
	private List<Object> conditionValues;
	
	/**
	 * 设置条件hql语句
	 * <p>包括基础字段projectId、customerId</p>
	 * @param startLinkStr 起始连接符，where或and
	 * @return
	 */
	private String setConditionHql(String startLinkStr){
		if(queryPropExtendConfDataParam.getConditionValues() != null){
			conditionValues = new ArrayList<Object>(3 + queryPropExtendConfDataParam.getConditionValues().size());
		}else{
			conditionValues = new ArrayList<Object>(3);
		}
		StringBuilder conditionHql = new StringBuilder();
		
		conditionHql.append(" ").append(startLinkStr).append(" p.customerId=?");
		conditionValues.add(CurrentThreadContext.getCustomerId());
		
		if(isProjectResource(tableResourceName)){
			conditionHql.append(" and p.projectId=?");
			conditionValues.add(CurrentThreadContext.getProjectId());
		}
		
		if(queryDataHql != null){
			conditionHql.append(" and ").append(queryDataHql);
			conditionValues.addAll(queryPropExtendConfDataParam.getConditionValues());
		}
		return conditionHql.toString();
	}
	
	/**
	 * 是否是项目资源
	 * <p>例如SysDataDictionary，就是属于项目资源，一个项目一套数据字典</p>
	 * <p>例如SysUser属于客户资源，一个客户有多个项目，但是每个项目都应引用同一套SysUser信息，这个资源再查询的时候，不应带上projectId的条件</p>
	 * @param resourceName
	 * @return
	 */
	private boolean isProjectResource(String resourceName) {
		for (String customerResource : customerResources) {
			if(customerResource.equals(resourceName)){
				return false;
			}
		}
		return true;
	}
	private static final String[] customerResources = {"SysUser", "SysDept"};

	/**
	 * 计算要查询的数据的总数
	 */
	private void calcQueryDataTotalCount(){
		String queryDataCountHql = null;
		if(StrUtils.notEmpty(propExtendConf.getDataDictionaryId())){
			queryType = 1;
			tableResourceName = "SysDataDictionary";
			queryDataHql = "from SysDataDictionary p where p.isEnabled=1 and p.isDelete=0" + setConditionHql("and") + " and parentId=?";
			queryDataCountHql = "select count(p."+ResourcePropNameConstants.ID+") " + queryDataHql;
			conditionValues.add(propExtendConf.getDataDictionaryId());
		}else if(StrUtils.notEmpty(propExtendConf.getRefTable()) && StrUtils.notEmpty(propExtendConf.getRefValueColumn())){
			queryType = 2;
			if(propExtendConf.isBuiltinTableResource()){
				tableResourceName = propExtendConf.getRefTable();
				valueColumnPropName = propExtendConf.getRefValueColumn();
			}else{
				if(StrUtils.notEmpty(propExtendConf.getRefTable()) && StrUtils.notEmpty(propExtendConf.getRefValueColumn())){
					Object obj = HibernateUtil.executeUniqueQueryByHqlArr(queryTableResourceNameByIdHql, propExtendConf.getRefTable());
					if(obj == null){
						throw new NullPointerException("id为["+propExtendConf.getId()+"]的属性扩展配置信息，配置的refTable值，无法查询到相应的表信息，请检查配置");
					}
					tableResourceName = obj.toString();
					
					if(ResourcePropNameConstants.ID.equals(propExtendConf.getRefValueColumn())){
						valueColumnPropName = ResourcePropNameConstants.ID;
					}else{
						obj = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropResourceNameByIdHql, propExtendConf.getRefValueColumn());
						if(obj == null){
							throw new NullPointerException("id为["+propExtendConf.getId()+"]的属性扩展配置信息，配置的refValueColumn值，无法查询到相应的列信息，请检查配置");
						}
						valueColumnPropName = obj.toString();
					}
				}
			}
			queryDataHql = "from " + tableResourceName + " p" + setConditionHql("where");
			queryDataCountHql = "select count(p."+ResourcePropNameConstants.ID+") " + queryDataHql;
		}
		
		dataListTotalCount = (long) HibernateUtil.executeUniqueQueryByHql(queryDataCountHql, getConditionValues());
		if(dataListTotalCount > 0 && querySize != -1){
			loopCount = (int)(dataListTotalCount/querySize +1);
			modCount = (int)(loopCount==1?dataListTotalCount:dataListTotalCount%querySize);
		}
	}
	/** 根据id查询表资源名的hql */
	private static final String queryTableResourceNameByIdHql = "select resourceName from CfgTable where "+ResourcePropNameConstants.ID+"=? and isEnabled=1 and isCreated=1 and isBuildModel=1";
	/** 根据id查询列属性名的hql */
	private static final String queryColumnPropResourceNameByIdHql = "select propName CfgColumn where "+ResourcePropNameConstants.ID+"=? and isEnabled=1 and operStatus="+CfgColumn.CREATED;
	
	/**
	 * 获取导入模版文件中的数据查询service实例
	 * <p>******这里会加</p>
	 */
	private void setImportTemplateDataQueryServiceInstance() {
		if("SysUser".equals(tableResourceName)){
			importTemplateDataQueryService = new SysUserImportTemplateDataQuery();
		}
	}
	
	/**
	 * 查询类型
	 * <p>1.查询数据字典、2.查询其他表数据</p>
	 */
	private int queryType;
	/**
	 * 查询的表的资源名
	 * <p>根据propExtendConf.getRefTable()获取</p>
	 */
	private String tableResourceName;
	/**
	 * 查询的value列的属性名(实际存储的值的属性)
	 * <p>根据propExtendConf.getRefValueColumn()获取</p>
	 */
	private String valueColumnPropName;
	
	// ---------------------------------------------------------------
	/**
	 * 查询数据列表的hql语句
	 * <p>最终在getDataList中用到的，select .. + queryDataHql，完整的查询用语句</p>
	 */
	private String queryDataListHql;
	
	/**
	 * 查询数据时，已经循环的次数
	 */
	private int currentLoopCount;
	/**
	 * 查询数据时，实际需要循环的次数
	 */
	private int loopCount;
	/**
	 * 查询数据时，最后一页查询的数量
	 */
	private int modCount;
	/**
	 * 查询数据的总数量
	 */
	private long dataListTotalCount;
	/**
	 * 查询数据时，起始位置
	 */
	private int startIndex;
	/**
	 * 查询数据时，结束位置
	 */
	private int endIndex;
	
	/**
	 * 获取要查询的数据的总数
	 * @return
	 */
	public long getDataListTotalCount() {
		return dataListTotalCount;
	}
	
	/**
	 * 获取数据集合
	 * @return
	 */
	@JSONField(serialize = false)
	public List<Object[]> getDataList() {
		installQueryDataListHql();
		if(dataListTotalCount > 0 && (currentLoopCount < loopCount || querySize == -1)){
			// 数据列表。数组的长度就为2或3，下标为0的是实际存储的值，下标为1的是展示名称，如果下标为2有值，则标识是子数据集合，依次类推，不过目前还未写出过data下标为3的查询语句
			List<Object[]> dataList = null;
			if(querySize == -1){
				startIndex = 0;
				endIndex = (int)dataListTotalCount;
			}else{
				startIndex = currentLoopCount*querySize;
				if(currentLoopCount == (loopCount-1)){
					endIndex = modCount;
				}else{
					endIndex = startIndex+querySize;
				}
			}
			
			if(queryType==1){
				dataList = HibernateUtil.executeListQueryByHql(startIndex+"", endIndex+"", queryDataListHql, getConditionValues());
			}else if(queryType==2){
				if(importTemplateDataQueryService != null){
					dataList = importTemplateDataQueryService.queryDataList(startIndex+"", endIndex+"", valueColumnPropName, queryPropExtendConfDataParam.getConditionHql(), getConditionValues(), queryPropExtendConfDataParam.getRefOrderByPropName(), queryPropExtendConfDataParam.getOrderBy());
				}else{
					dataList = HibernateUtil.executeListQueryByHql(startIndex+"", endIndex+"", queryDataListHql, getConditionValues());
				}
			}
			currentLoopCount++;
			return dataList;
		}
		return null;
	}

	/**
	 * 组装查询数据列表的hql语句
	 */
	private void installQueryDataListHql() {
		if(queryDataListHql == null && dataListTotalCount > 0){
			StringBuilder hql = new StringBuilder();
			if(queryType==1){
				hql.append("select p.val, p.caption ");
			}else if(queryType==2){
				hql.append("select p.").append(valueColumnPropName);
				if(queryPropExtendConfDataParam.getRefKeyPropName() != null){
					hql.append(",p.").append(queryPropExtendConfDataParam.getRefKeyPropName());
				}
			}
			hql.append(" ").append(queryDataHql);
			if(queryPropExtendConfDataParam.getRefOrderByPropName() != null){
				hql.append(" order by p.").append(queryPropExtendConfDataParam.getRefOrderByPropName()).append(" ").append(queryPropExtendConfDataParam.getOrderBy());
			}
			
			queryDataListHql = hql.toString();
			hql.setLength(0);
		}
	}

	/**
	 * 获取条件值集合
	 * @return
	 */
	private List<Object> getConditionValues() {
		List<Object> conditionValues = new ArrayList<Object>(this.conditionValues.size());
		conditionValues.addAll(this.conditionValues);
		return conditionValues;
	}
	
	public void clear() {
		if(conditionValues != null && conditionValues.size() > 0){
			conditionValues.clear();
		}
		if(queryPropExtendConfDataParam != null){
			queryPropExtendConfDataParam.clear();
		}
	}
}
