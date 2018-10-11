package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * Excel导入导出信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysExcelImportExportInfo extends BasicEntity implements ITable, IEntity{

	/**
	 * 操作类型
	 * <p>1：导入、2、导出</p>
	 */
	private Integer operType;
	/**
	 * 关联的文件id
	 * <p>文件表中的数据id</p>
	 */
	private String refFileId;
	/**
	 * 是否成功
	 */
	private Integer isSuccess;
	/**
	 * 结果信息
	 */
	private String resultMessage;
	
	//-------------------------------------------------------------------------
	
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public String getRefFileId() {
		return refFileId;
	}
	public void setRefFileId(String refFileId) {
		this.refFileId = refFileId;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(4+7);
		
		ComColumndata operTypeColumn = new ComColumndata("oper_type", DataTypeConstants.INTEGER, 1);
		operTypeColumn.setName("操作类型");
		operTypeColumn.setComments("1：导入、2、导出");
		columns.add(operTypeColumn);
		
		ComColumndata refFileIdColumn = new ComColumndata("ref_file_id", DataTypeConstants.STRING, 32);
		refFileIdColumn.setName("关联的文件id");
		refFileIdColumn.setComments("文件表中的数据id");
		columns.add(refFileIdColumn);
		
		ComColumndata isSuccessColumn = new ComColumndata("is_success", DataTypeConstants.INTEGER, 1);
		isSuccessColumn.setName("是否成功");
		isSuccessColumn.setComments("是否成功");
		columns.add(isSuccessColumn);
		
		ComColumndata resultMessageColumn = new ComColumndata("result_message", DataTypeConstants.STRING, 4000);
		resultMessageColumn.setName("结果信息");
		resultMessageColumn.setComments("结果信息");
		columns.add(resultMessageColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("Excel导入导出信息表");
		table.setComments("Excel导入导出信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_EXCEL_IMPORT_EXPORT_INFO";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysExcelImportExportInfo";
	}
	
	// ---------------------------------------------------------------------------
	public static final Integer IMPORT = 1;
	public static final Integer EXPORT = 2;
}