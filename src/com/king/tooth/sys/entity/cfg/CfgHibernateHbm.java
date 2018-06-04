package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;

/**
 * [配置系统]hibernate的hbm内容
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgHibernateHbm extends BasicEntity implements ITable{
	/**
	 * 关联的表主键
	 */
	private String tableId;
	/**
	 * hbm内容
	 */
	private String hbmContent;

	//-------------------------------------------------------------------------
	
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getHbmContent() {
		return hbmContent;
	}
	public void setHbmContent(String hbmContent) {
		this.hbmContent = hbmContent;
	}
	public String toDropTable() {
		return "CFG_HIBERNATE_HBM";
	}
	

	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "CFG_HIBERNATE_HBM");
		table.setName("[配置系统]字段数据信息资源对象表");
		table.setComments("[配置系统]字段数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(7);
		
		CfgColumndata tableIdColumn = new CfgColumndata("table_id");
		tableIdColumn.setName("关联的表主键");
		tableIdColumn.setComments("关联的表主键");
		tableIdColumn.setColumnType(DataTypeConstants.STRING);
		tableIdColumn.setLength(32);
		tableIdColumn.setOrderCode(1);
		columns.add(tableIdColumn);
		
		CfgColumndata hbmContentColumn = new CfgColumndata("hbm_content");
		hbmContentColumn.setName("显示的汉字名称");
		hbmContentColumn.setComments("显示的汉字名称");
		hbmContentColumn.setColumnType(DataTypeConstants.CLOB);
		hbmContentColumn.setOrderCode(2);
		columns.add(hbmContentColumn);
		
		table.setColumns(columns);
		return table;
	}
}
