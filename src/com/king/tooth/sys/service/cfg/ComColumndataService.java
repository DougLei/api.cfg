package com.king.tooth.sys.service.cfg;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段数据信息资源对象处理器
 * @author DougLei
 */
public class ComColumndataService extends AbstractService{

	/**
	 * 验证列关联的表是否存在
	 * @param project
	 * @return operResult
	 */
	private String validColumnRefTableIsExists(ComColumndata column) {
		if(StrUtils.isEmpty(column.getTableId())){
			return "关联的表id不能为空";
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComTabledata where id = ?", column.getTableId());
		if(count != 1){
			return "关联的id=["+column.getTableId()+"]的表信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证列名是否存在
	 * @param project
	 * @return operResult
	 */
	private String validColumnNameIsExists(ComColumndata column) {
		String hql = "select count("+ResourceNameConstants.ID+") from ComColumndata where columnName = ? and tableId = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, column.getColumnName(), column.getTableId());
		if(count > 0){
			return "列名为["+column.getColumnName()+"]的信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存列
	 * @param column
	 * @return
	 */
	public Object saveColumn(ComColumndata column) {
		String operResult = validColumnRefTableIsExists(column);
		if(operResult == null){
			operResult = validColumnNameIsExists(column);
		}
		if(operResult == null){
			return HibernateUtil.saveObject(column, null);
		}
		return operResult;
	}

	/**
	 * 修改列
	 * @param column
	 * @return
	 */
	public Object updateColumn(ComColumndata column) {
		ComColumndata oldColumn = getObjectById(column.getId(), ComColumndata.class);
		if(oldColumn == null){
			return "没有找到id为["+column.getId()+"]的列对象信息";
		}
		
		String operResult = null;
		if(!oldColumn.getColumnName().equals(column.getColumnName())){
			operResult = validColumnNameIsExists(column);
		}
		if(operResult == null){
			operResult = validColumnRefTableIsExists(column);
		}
		if(operResult == null){
			// 如果是平台的开发者,只要修改列信息，就要同时修改对应表的状态，以备后期重新建模
			if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
				HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComTabledata set isCreated = 0 where "+ResourceNameConstants.ID+" = '"+column.getTableId()+"'");
			}
			return HibernateUtil.updateObjectByHql(column, null);
		}
		return operResult;
	}

	/**
	 * 删除列
	 * @param columnIds
	 * @return
	 */
	public String deleteColumn(String columnIds) {
		return deleteDataById("ComColumndata", columnIds);
	}
}
