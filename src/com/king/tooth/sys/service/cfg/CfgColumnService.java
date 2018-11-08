package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段信息表Service
 * @author DougLei
 */
@Service
public class CfgColumnService extends AService{

	/**
	 * 验证列关联的表是否存在
	 * @param project
	 * @return operResult
	 */
	private String validColumnRefTableIsExists(CfgColumn column) {
		if(StrUtils.isEmpty(column.getTableId())){
			return "关联的表id不能为空";
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgTable where "+ResourcePropNameConstants.ID+" = ?", column.getTableId());
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
	private String validColumnNameIsExists(CfgColumn column) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgColumn where columnName = ? and tableId = ? and operStatus != "+CfgColumn.DELETED;
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
	public Object saveColumn(CfgColumn column) {
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
	public Object updateColumn(CfgColumn column) {
		CfgColumn oldColumn = getObjectById(column.getId(), CfgColumn.class);
		CfgTable table = getObjectById(column.getTableId(), CfgTable.class);
		if(table.getIsCreated() == 1){// 表已经建模，不能修改列的类型，以及缩小列的长度
			if(!oldColumn.getColumnType().equals(column.getColumnType())){
				return "系统不允许修改["+column.getColumnName()+"]字段的数据类型[从"+oldColumn.getColumnType()+"到"+column.getColumnType()+"]，此操作可能会损失已有数据";
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
			if(column.analysisOldColumnInfo(oldColumn)){
				modifyTableIsBuildModel(column.getTableId(), null, 0);
			}
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
		
		CfgColumn column = getObjectById(idArr[0], CfgColumn.class);
		CfgTable table = getObjectById(column.getTableId(), CfgTable.class);
		
		if(table.getIsCreated() == 0){// 表还没有建模，这里就直接删除
			deleteDataById("CfgColumn", columnIds);
		}else if(table.getIsCreated() == 1){// 表已经建模，就修改operStatus的值为2
			StringBuilder hql = new StringBuilder("update CfgColumn set operStatus="+CfgColumn.DELETED+" where id ");
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
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, hql.toString());
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
			tableId = getObjectById(columnId, CfgColumn.class).getTableId();
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgTable set isBuildModel = "+isBuildModel+" where "+ResourcePropNameConstants.ID+" = '"+tableId+"'");
	}
}
