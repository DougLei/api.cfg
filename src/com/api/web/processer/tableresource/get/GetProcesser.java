package com.api.web.processer.tableresource.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.api.constants.ResourcePropNameConstants;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.builtin.method.common.create.exportfile.BuiltinCreateExportFileMethodProcesser;
import com.api.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.api.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;
import com.api.web.builtin.method.tableresource.query.BuiltinQueryMethodProcesser;
import com.api.web.builtin.method.tableresource.recursive.BuiltinRecursiveMethodProcesser;
import com.api.web.builtin.method.tableresource.sort.BuiltinSortMethodProcesser;
import com.api.web.builtin.method.tableresource.sublist.BuiltinSublistMethodProcesser;
import com.api.web.entity.resulttype.PageResultEntity;
import com.api.web.processer.tableresource.RequestProcesser;

/**
 * get请求处理器
 * @author DougLei
 */
public abstract class GetProcesser extends RequestProcesser {
	
	/**
	 * select
	 */
	protected BuiltinQueryMethodProcesser builtinQueryMethodProcesser;
	/**
	 * order by
	 */
	protected BuiltinSortMethodProcesser builtinSortMethodProcesser;
	/**
	 * 分页查询
	 */
	protected BuiltinPagerMethodProcesser builtinPagerMethodProcesser;
	/**
	 * 聚焦数据
	 */
	protected BuiltinFocusedIdMethodProcesser builtinFocusedIdMethodProcesser;
	/**
	 * 递归
	 */
	protected BuiltinRecursiveMethodProcesser builtinRecursiveMethodProcesser;
	/**
	 * 子资源集合
	 */
	protected BuiltinSublistMethodProcesser builtinSublistMethodProcesser;
	/**
	 * 生成导出文件
	 */
	protected BuiltinCreateExportFileMethodProcesser builtinCreateExportFileMethodProcesser;
	
	/**
	 * 处理请求
	 */
	protected final boolean doProcess() {
		initBuiltinMethods();
		boolean isKeepOn = doGetProcess();
		return isKeepOn;
	}
	
	/**
	 * 初始化内置的函数属性对象
	 * 方便子类使用
	 */
	private void initBuiltinMethods(){
		builtinQueryCondMethodProcesser = builtinTableResourceBMProcesser.getQuerycondProcesser();
		builtinQueryMethodProcesser = builtinTableResourceBMProcesser.getQueryProcesser();
		builtinSortMethodProcesser = builtinTableResourceBMProcesser.getSortProcesser();
		builtinPagerMethodProcesser = builtinTableResourceBMProcesser.getPagerProcesser();
		builtinFocusedIdMethodProcesser = builtinTableResourceBMProcesser.getFocusedIdProcesser();
		builtinRecursiveMethodProcesser = builtinTableResourceBMProcesser.getRecursiveProcesser();
		builtinParentsubQueryMethodProcesser = builtinTableResourceBMProcesser.getParentsubQueryMethodProcesser();
		builtinSublistMethodProcesser = builtinTableResourceBMProcesser.getSublistMethodProcesser();
		builtinCreateExportFileMethodProcesser = builtinTableResourceBMProcesser.getCreateExportFileProcesser();
	}
	
	/**
	 * 处理get请求
	 * @return
	 */
	protected abstract boolean doGetProcess();
	
	/**
	 * 获取包括from在内的，后续的hql语句，由各个子类实现
	 * <p>例1： from tableName where xxxx</p>
	 * <p>例2： from tableName where id in (select pid from subTableName where id = xxx) and xxx </p>
	 * @return
	 */
	protected abstract StringBuilder getFromHql();
	
	// ******************************************************************************************************
	// 以下是给子类使用的通用方法
	
	/**
	 * 分页查询，加载PageResultEntity类的实例，如果调用了分页查询的功能，则同时将查询的条件值set到query参数对象中
	 * <p>根据各个子类的功能，再决定是否使用该方法</p>
	 * @param query 
	 * @return 分页结果对象
	 */
	protected final PageResultEntity loadPageResultEntity(Query query){
		PageResultEntity pageResultEntity = null;
		if(builtinPagerMethodProcesser.getIsUsed()){
			pageResultEntity =  new PageResultEntity();
			
			// 获得查询总数量的hql语句
			String countHql = builtinPagerMethodProcesser.getHql() + getFromHql();
			Query countQuery = createQuery(countHql);
			long totalCount = (long) countQuery.uniqueResult();// 查询获得数据总数
			pageResultEntity.setTotalCount(totalCount);
			pageResultEntity.setPageNum(builtinPagerMethodProcesser.getPageQueryEntity().getPageNum());
			pageResultEntity.setPageTotalCount(builtinPagerMethodProcesser.getPageQueryEntity().getPageTotalCount(totalCount));
			
			if(builtinFocusedIdMethodProcesser.getIsUsed()){
				pageResultEntity.setFocusedId(builtinFocusedIdMethodProcesser.getFocusedId());
			}
			
			pageResultEntity.setPageSize(builtinPagerMethodProcesser.getPageQueryEntity().getPageSize());
			pageResultEntity.setFirstDataIndex(builtinPagerMethodProcesser.getPageQueryEntity().getFirstDataIndex());
			
			if(totalCount>0 && !builtinCreateExportFileMethodProcesser.getIsUsed()){// 如果有数据，同时没有开启生成导出文件功能，再进行分页查询
				query.setFirstResult(pageResultEntity.getFirstDataIndex());
				query.setMaxResults(pageResultEntity.getPageSize());
			}
		}
		return pageResultEntity;
	}
	
	/**
	 * 执行查询
	 * <p>如果有聚焦id，则要处理</p>
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final List<Map<String, Object>> executeQuery(Query query){
		List<Map<String, Object>> dataList = query.list();
		if(builtinFocusedIdMethodProcesser.getIsUsed()){
			List<Object> addFocusedIds = builtinFocusedIdMethodProcesser.getAddFocusedIds();
			if(addFocusedIds != null && addFocusedIds.size() > 0){
				List<Map<String, Object>> addDataList = removeDatas(dataList, addFocusedIds);
				
				if(addFocusedIds.size() > 0){
					StringBuilder hql = new StringBuilder("from " + requestBody.getResourceName() + " where " + ResourcePropNameConstants.ID + " in(");
					int size = addFocusedIds.size();
					for (int i=0;i<size;i++) {
						hql.append("?,");
						dataList.remove(dataList.size()-1).clear();// 挤掉最后的数据
					}
					hql.setLength(hql.length()-1);
					hql.append(")");
					hql.append(builtinSortMethodProcesser.getHql());
					
					List<Map<String, Object>> tmpAddDataList = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), addFocusedIds);
					hql.setLength(0);
					
					if(addDataList == null){
						addDataList = tmpAddDataList;
					}else{
						addDataList.addAll(0, tmpAddDataList);
						tmpAddDataList.clear();
					}
				}
				
				if(addDataList != null && addDataList.size() > 0){
					dataList.addAll(0, addDataList);
				}
			}
		}
		return dataList;
	}

	/**
	 * 移除最后的数据，或和dataId相同id的数据
	 * <p>处理聚焦定位的数据</p>
	 * @param dataList
	 * @param dataId
	 */
	private List<Map<String, Object>> removeDatas(List<Map<String, Object>> dataList, List<Object> dataIds) {
		List<Map<String, Object>> addDataList = null;
		
		String dataId;
		Map<String, Object> dataMap;
		for(int i=0;i<dataIds.size();i++){
			dataId = dataIds.get(i).toString();
			for(int j=0;j<dataList.size();j++){
				dataMap = dataList.get(j);
				if(dataId.equals(dataMap.get(ResourcePropNameConstants.ID).toString())){
					if(addDataList == null){
						addDataList = new ArrayList<Map<String,Object>>(dataIds.size());
					}
					addDataList.add(dataMap);
					dataIds.remove(i--);
					dataList.remove(j--);
					break;
				}
			}
		}
		return addDataList;
	}

	/**
	 * 二次处理查询的数据结果集合 
	 * @param dataList
	 * @return 
	 */
	protected final List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList) {
		if(builtinQueryMethodProcesser.getIsNeedProcessDataCollection() && dataList != null && dataList.size() > 0){
			dataList = builtinQueryMethodProcesser.doProcessDataCollection(dataList);
		}
		return dataList;
	}
	
	/**
	 * 处理查询子资源数据集合
	 * @param dataList
	 */
	protected final void doProcessSubListQuery(List<Map<String, Object>> dataList){
		if(builtinSublistMethodProcesser.getIsUsed() && dataList != null && dataList.size() > 0){
			String parentId;
			List<Map<String, Object>> nullSubList = null;
			for (Map<String, Object> map : dataList) {
				parentId = (String) map.get(ResourcePropNameConstants.ID);
				if(parentId == null){
					map.put("children", nullSubList);
				}else{
					map.put("children", builtinSublistMethodProcesser.querySubList(parentId));
				}
			}
		}
	}
}
