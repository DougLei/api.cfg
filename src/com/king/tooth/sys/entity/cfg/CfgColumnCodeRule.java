package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;

/**
 * 字段编码规则表
 * @author DougLei
 */
@SuppressWarnings("serial")
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

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_COLUMN_CODE_RULE", 0);
		table.setName("字段编码规则表");
		table.setComments("字段编码规则表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinCodeDataType.STRING, 32);
		refTableIdColumn.setName("关联规则的表id");
		refTableIdColumn.setComments("关联规则的表id");
		columns.add(refTableIdColumn);
		
		ComColumndata refColumnIdColumn = new ComColumndata("ref_column_id", BuiltinCodeDataType.STRING, 32);
		refColumnIdColumn.setName("关联规则的列id");
		refColumnIdColumn.setComments("关联规则的列id");
		columns.add(refColumnIdColumn);
		
		ComColumndata remarkColumn = new ComColumndata("remark", BuiltinCodeDataType.STRING, 500);
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
}
