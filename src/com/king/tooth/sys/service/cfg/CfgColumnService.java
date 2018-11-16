package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
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
	 * @param tableNameBuffer
	 * @param column
	 * @param addColumns 
	 * @param dbTableHandler 
	 * @return
	 */
	public Object saveColumn(StringBuilder tableNameBuffer, CfgColumn column, List<CfgColumn> addColumns, DBTableHandler dbTableHandler) {
		String operResult = validColumnRefTableIsExists(column);
		if(operResult == null){
			operResult = validColumnNameIsExists(column);
		}
		if(operResult == null){
			if(tableNameBuffer.length() == 0){
				tableNameBuffer.append(HibernateUtil.executeUniqueQueryByHqlArr("select tableName from CfgTable where " + ResourcePropNameConstants.ID+"=?", column.getTableId()));
			}
			
			try {
				column.setOperStatus(CfgColumn.UN_CREATED);
				dbTableHandler.modifyColumn(tableNameBuffer.toString(), column);
				addColumns.add(column);
				
				// 最后修改表的建模状态为: 未建模
				modifyTableIsBuildModel(column.getTableId(), null, 0);
				return HibernateUtil.saveObject(column, null);
			} catch (Exception e) {
				operResult = "添加列时出现异常：" + ExceptionUtil.getErrMsg(e);
				
				// drop被添加的列
				if(addColumns != null && addColumns.size() > 0){
					recorveColumnsOperStatus(dbTableHandler, tableNameBuffer.toString(), addColumns, CfgColumn.DELETED);
					addColumns.clear();
				}
			}
		}
		return operResult;
	}

	/**
	 * 修改列
	 * @param tableNameBuffer
	 * @param column
	 * @param updateColumns 
	 * @param dbTableHandler 
	 * @return
	 */
	public Object updateColumn(StringBuilder tableNameBuffer, CfgColumn column, List<CfgColumn> updateColumns, DBTableHandler dbTableHandler) {
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
			try {
				if(column.analysisOldColumnInfo(oldColumn)){
					if(tableNameBuffer.length() == 0){
						tableNameBuffer.append(HibernateUtil.executeUniqueQueryByHqlArr("select tableName from CfgTable where " + ResourcePropNameConstants.ID+"=?", column.getTableId()));
					}
					dbTableHandler.modifyColumn(tableNameBuffer.toString(), column);
					oldColumn.analysisOldColumnInfo(column);
					updateColumns.add(oldColumn);
					
					// 最后修改表的建模状态为: 未建模
					modifyTableIsBuildModel(column.getTableId(), null, 0);
				}
				column.setOperStatus(CfgColumn.CREATED);
				return HibernateUtil.updateEntityObject(column, null);
			} catch (Exception e) {
				operResult = "修改列时出现异常：" + ExceptionUtil.getErrMsg(e);
				
				// 恢复被修改的列
				if(updateColumns != null && updateColumns.size() > 0){
					recorveColumnsOperStatus(dbTableHandler, tableNameBuffer.toString(), updateColumns, CfgColumn.MODIFIED);
					updateColumns.clear();
				}
			}
		}
		return operResult;
	}

	/**
	 * 删除列
	 * @param columnIds
	 * @return
	 */
	public String deleteColumn(String columnIds) {
		String operResult = null;
		String firstDeleteColumnId = columnIds.split(",")[0];
		CfgTable table = getObjectById(getObjectById(firstDeleteColumnId, CfgColumn.class).getTableId(), CfgTable.class);
		
		String tableName = table.getTableName();
		List<CfgColumn> columns = null;
		List<CfgColumn> dropedColumns = null;// 记录已经被drop的列
		DBTableHandler dbTableHandler = null;
		try {
			if(table.getIsCreated() == 1){// 表已经建模，要先drop字段
				String queryHql = "from CfgColumn where " + ResourcePropNameConstants.ID +" in ('" + columnIds.replace(",", "','") + "')";
				columns = HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumn.class, null, null, queryHql);
				
				dropedColumns = new ArrayList<CfgColumn>(columns.size());// 记录已经被drop的列
				dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
				for (CfgColumn c : columns) {
					c.setOperStatus(CfgColumn.DELETED);
					dbTableHandler.modifyColumn(tableName, c);
					dropedColumns.add(c);
				}
					
				// 最后修改表的建模状态为: 未建模
				modifyTableIsBuildModel(null, firstDeleteColumnId, 0);
			}
			// 最后删除信息
			deleteDataById("CfgColumn", columnIds);
		} catch (Exception e) {
			operResult = "删除列时出现异常：" + ExceptionUtil.getErrMsg(e);
			
			// 恢复被drop的列
			if(dropedColumns != null && dropedColumns.size() > 0){
				recorveColumnsOperStatus(dbTableHandler, tableName, dropedColumns, CfgColumn.CREATED);
				dropedColumns.clear();
			}
		}
		if(columns != null && columns.size() > 0){
			columns.clear();
		}
		return operResult;
	}
	
	/**
	 * 修改表是否建模的状态
	 * <p>并删除对应的资源信息</p>
	 * @param tableId
	 * @param columnId
	 * @param isBuildModel
	 */
	private void modifyTableIsBuildModel(String tableId, String columnId, Integer isBuildModel){
		if(StrUtils.isEmpty(tableId)){
			tableId = getObjectById(columnId, CfgColumn.class).getTableId();
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update CfgTable set isBuildModel = "+isBuildModel+" where "+ResourcePropNameConstants.ID+" = '"+tableId+"'");
		BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).deleteCfgResource(tableId);// 删除资源
	}
	
	/**
	 * 恢复列的操作状态
	 * <p>例如批量添加列，有几个没有添加成功，则将添加成功的也drop</p>
	 * @param dbTableHandler
	 * @param columns
	 * @param columnOperStatus
	 */
	private void recorveColumnsOperStatus(DBTableHandler dbTableHandler, String tableName, List<CfgColumn> columns, int columnOperStatus){
		if(columns != null && columns.size() > 0){
			for (CfgColumn column : columns) {
				column.setOperStatus(columnOperStatus);
			}
			dbTableHandler.modifyColumns(tableName, columns, false);
		}
	}
}
