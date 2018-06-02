package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]hibernate映射配置数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComHibernateHbmConfdata extends BasicEntity implements ITable{
	/**
	 * 关联的表主键
	 */
	private String tableId;
	/**
	 * 是否是关系表映射
	 * <p>如果是关系表映射数据，tableId的值为父表的id</p>
	 */
	private int isDatalinkTableMapping;
	/**
	 * 文件内容
	 */
	private String fileContent;
	/**
	 * 版本要和动态表(SysDynamicTableData)中的版本对应
	 */
	private int version;
	
	// 构造函数
	public ComHibernateHbmConfdata() {
		this.version = 1;
	}
	public ComHibernateHbmConfdata(String tableId,String fileContent, int version, int isDatalinkTableMapping) {
		this.tableId = tableId;
		this.fileContent = fileContent;
		this.version = version;
		this.isDatalinkTableMapping = isDatalinkTableMapping;
	}
	
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getTableId() {
		return tableId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public int getIsDatalinkTableMapping() {
		return isDatalinkTableMapping;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setIsDatalinkTableMapping(int isDatalinkTableMapping) {
		this.isDatalinkTableMapping = isDatalinkTableMapping;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_HIBERNATE_HBM_CONFDATA");
		table.setName("[通用的]hibernate映射配置数据信息资源对象表");
		table.setComments("[通用的]hibernate映射配置数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(9);
		
		CfgColumndata tableIdColumn = new CfgColumndata("table_id");
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setColumnType(DataTypeConstants.STRING);
		tableIdColumn.setLength(32);
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		CfgColumndata isDatalinkTableMappingColumn = new CfgColumndata("is_datalink_table_mapping");
		isDatalinkTableMappingColumn.setName("是否是关系表映射");
		isDatalinkTableMappingColumn.setComments("是否是关系表映射:如果是关系表映射数据，tableId的值为父表的id");
		isDatalinkTableMappingColumn.setColumnType(DataTypeConstants.INTEGER);
		isDatalinkTableMappingColumn.setLength(1);
		isDatalinkTableMappingColumn.setOrderCode(2);
		columns.add(isDatalinkTableMappingColumn);
		
		CfgColumndata fileContentColumn = new CfgColumndata("file_content");
		fileContentColumn.setName("文件内容");
		fileContentColumn.setComments("文件内容");
		fileContentColumn.setColumnType(DataTypeConstants.CLOB);
		fileContentColumn.setOrderCode(3);
		columns.add(fileContentColumn);
		
		CfgColumndata versionColumn = new CfgColumndata("version");
		versionColumn.setName("版本");
		versionColumn.setComments("版本要和动态表(SysDynamicTableData)中的版本对应");
		versionColumn.setColumnType(DataTypeConstants.INTEGER);
		versionColumn.setLength(3);
		versionColumn.setOrderCode(4);
		columns.add(versionColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_HIBERNATE_HBM_CONFDATA";
	}
}
