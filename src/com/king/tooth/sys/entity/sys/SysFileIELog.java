package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 文件导入导出日志表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysFileIELog extends BasicEntity implements ITable, IEntity{

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
	/**
	 * 提交的对象内容json串
	 */
	private String submitObjJson;
	
	//-------------------------------------------------------------------------
	
	public SysFileIELog() {
	}
	public SysFileIELog(Integer operType, String refFileId, String submitObjJson) {
		this.operType = operType;
		this.refFileId = refFileId;
		this.submitObjJson = submitObjJson;
	}
	
	public void recordResult(String resultMessage){
		this.resultMessage = resultMessage;
		if(resultMessage == null){
			this.isSuccess = 1;
		}else{
			this.isSuccess = 0;
		}
	}
	
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
	public String getSubmitObjJson() {
		return submitObjJson;
	}
	public void setSubmitObjJson(String submitObjJson) {
		this.submitObjJson = submitObjJson;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		CfgColumn operTypeColumn = new CfgColumn("oper_type", DataTypeConstants.INTEGER, 1);
		operTypeColumn.setName("操作类型");
		operTypeColumn.setComments("1：导入、2、导出");
		columns.add(operTypeColumn);
		
		CfgColumn refFileIdColumn = new CfgColumn("ref_file_id", DataTypeConstants.STRING, 32);
		refFileIdColumn.setName("关联的文件id");
		refFileIdColumn.setComments("文件表中的数据id");
		columns.add(refFileIdColumn);
		
		CfgColumn isSuccessColumn = new CfgColumn("is_success", DataTypeConstants.INTEGER, 1);
		isSuccessColumn.setName("是否成功");
		isSuccessColumn.setComments("是否成功");
		columns.add(isSuccessColumn);
		
		CfgColumn resultMessageColumn = new CfgColumn("result_message", DataTypeConstants.STRING, 4000);
		resultMessageColumn.setName("结果信息");
		resultMessageColumn.setComments("结果信息");
		columns.add(resultMessageColumn);
		
		CfgColumn submitObjJsonColumn = new CfgColumn("submit_obj_json", DataTypeConstants.STRING, 4000);
		submitObjJsonColumn.setName("提交的对象内容json串");
		submitObjJsonColumn.setComments("提交的对象内容json串");
		columns.add(submitObjJsonColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setResourceName(getEntityName());
		table.setName("文件导入导出日志表");
		table.setComments("文件导入导出日志表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_FILE_IE_LOG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysFileIELog";
	}
	
	// ---------------------------------------------------------------------------
	/** 操作类型:1、导入 */
	public static final Integer OPER_TYPE_IMPORT = 1;
	/** 操作类型:2、导出 */
	public static final Integer OPER_TYPE_EXPORT = 2;
}