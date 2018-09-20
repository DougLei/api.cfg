package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段信息表Service
 * @author DougLei
 */
@Service
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComTabledata where id = ?", column.getTableId());
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
		String hql = "select count("+ResourcePropNameConstants.ID+") from ComColumndata where columnName = ? and tableId = ? and operStatus != "+ComColumndata.DELETED;
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
			modifyTableIsBuildModel(column.getTableId(), null, 0);
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
		
		ComTabledata table = getObjectById(column.getTableId(), ComTabledata.class);
		if(table.getIsCreated() == 1){// 表已经建模，不能修改列的类型，以及缩小列的长度
			if(!oldColumn.getColumnType().equals(column.getColumnType())){
				return "系统不允许修改["+column.getColumnName()+"]字段的数据类型[从"+oldColumn.getColumnType()+"到"+column.getColumnType()+"]";
			}
			if(oldColumn.getLength()> column.getLength()){
				return "系统不允许修改["+column.getColumnName()+"]字段的数据长度[从"+oldColumn.getLength()+"降低到"+column.getLength()+"]，此操作可能会损失实际数据的精度";
			}
		}
		
		String operResult = null;
		if(operResult == null){
			operResult = validColumnRefTableIsExists(column);
		}
		if(!oldColumn.getColumnName().equals(column.getColumnName())){
			operResult = validColumnNameIsExists(column);
		}
		if(operResult == null){
			column.setOperStatus(ComColumndata.MODIFIED);
			column.analysisOldColumnInfo(oldColumn, column);
			
			// 如果是平台的开发者,只要修改列信息，就要同时修改对应表的状态，以备后期重新建模
			// TODO 单项目，取消是否平台开发者的判断
//			if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
				modifyTableIsBuildModel(column.getTableId(), null, 0);
//			}
			return HibernateUtil.updateObject(column, null);
		}
		return operResult;
	}

	/**
	 * 删除列
	 * @param columnIds
	 * @return
	 */
	public String deleteColumn(String columnIds) {
		String[] idArr = columnIds.split(",");
		
		ComColumndata column = getObjectById(idArr[0], ComColumndata.class);
		ComTabledata table = getObjectById(column.getTableId(), ComTabledata.class);
		
		if(table.getIsCreated() == 0){// 表还没有建模，这里就直接删除
			deleteDataById("ComColumndata", columnIds);
		}else if(table.getIsCreated() == 1){// 表已经建模，就修改operStatus的值为2
			StringBuilder hql = new StringBuilder("update ComColumndata set operStatus="+ComColumndata.DELETED+" where id ");
			if(idArr.length ==1){
				hql.append("= '").append(idArr[0]).append("'");
			}else if(idArr.length > 1){
				hql.append("in (");
				for (String columnId : idArr) {
					hql.append("'").append(columnId).append("',");
				}
				hql.setLength(hql.length()-1);
				hql.append(")");
			}else{
				return "删除数据时，传入的id数据数组长度小于1";
			}
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, hql.toString());
			hql.setLength(0);
			
			modifyTableIsBuildModel(null, idArr[0], 0);
		}else{
			throw new IllegalArgumentException("删除列时，相关联的表的isCreated字段值异常，值为["+table.getIsCreated()+"]，请联系后台系统开发人员");
		}
		return null;
	}
	
	/**
	 * 修改表是否建模的状态
	 * @param tableId
	 * @param columnId
	 * @param isBuildModel
	 */
	private void modifyTableIsBuildModel(String tableId, String columnId, Integer isBuildModel){
		if(StrUtils.isEmpty(tableId)){
			tableId = getObjectById(columnId, ComColumndata.class).getTableId();
		}
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComTabledata set isBuildModel = "+isBuildModel+" where "+ResourcePropNameConstants.ID+" = '"+tableId+"'");
	}
}
