package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 文件表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysFile extends BasicEntity implements ITable, IEntity{
	/**
	 * 关联的数据主键值
	 */
	private String refDataId;
	/**
	 * 文件的实际名
	 * <p>即上传时的文件名称</p>
	 */
	private String actName;
	/**
	 * 文件编码
	 * <p>在上传的时候，修改后的文件名</p>
	 */
	private String code;
	/**
	 * 文件大小
	 */
	private Double size;
	/**
	 * 文件后缀
	 */
	private String suffix;
	/**
	 * 文件的存储路径
	 */
	private String savePath;
	/**
	 * 文件的存储方式
	 * <p>1:存储在数据库，2:存储在系统服务器上...</p>
	 */
	private Integer saveType;
	/**
	 * 文件内容
	 */
	private String content;
	/**
	 * 文件的密级
	 */
	private Integer level;
	
	// ---------------------------------------------------------------------------
	
	public String getRefDataId() {
		return refDataId;
	}
	public void setRefDataId(String refDataId) {
		this.refDataId = refDataId;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public Integer getSaveType() {
		return saveType;
	}
	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_FILE", 0);
		table.setName("文件表");
		table.setComments("文件表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(16);
		
		ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", BuiltinCodeDataType.STRING, 32);
		refDataIdColumn.setName("关联的数据主键值");
		refDataIdColumn.setComments("关联的数据主键值");
		refDataIdColumn.setOrderCode(1);
		columns.add(refDataIdColumn);
		
		ComColumndata actNameColumn = new ComColumndata("act_name", BuiltinCodeDataType.STRING, 300);
		actNameColumn.setName("文件的实际名");
		actNameColumn.setComments("文件的实际名：即上传时的文件名称");
		actNameColumn.setOrderCode(2);
		columns.add(actNameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinCodeDataType.STRING, 32);
		codeColumn.setName("文件编码");
		codeColumn.setComments("文件编码：在上传的时候，修改后的文件名");
		codeColumn.setOrderCode(3);
		columns.add(codeColumn);
		
		ComColumndata sizeColumn = new ComColumndata("size", BuiltinCodeDataType.DOUBLE, 20);
		sizeColumn.setName("文件大小");
		sizeColumn.setComments("文件大小");
		sizeColumn.setOrderCode(4);
		columns.add(sizeColumn);
		
		ComColumndata suffixColumn = new ComColumndata("suffix", BuiltinCodeDataType.STRING, 10);
		suffixColumn.setName("文件后缀");
		suffixColumn.setComments("文件后缀");
		suffixColumn.setOrderCode(5);
		columns.add(suffixColumn);
		
		ComColumndata savePathColumn = new ComColumndata("save_path", BuiltinCodeDataType.BLOB, 0);
		savePathColumn.setName("文件的存储路径");
		savePathColumn.setComments("文件的存储路径：存储文件的路径");
		savePathColumn.setOrderCode(6);
		columns.add(savePathColumn);
		
		ComColumndata saveTypeColumn = new ComColumndata("save_type", BuiltinCodeDataType.INTEGER, 1);
		saveTypeColumn.setName("文件的存储方式");
		saveTypeColumn.setComments("文件的存储方式：1:存储在数据库，2:存储在系统服务器上...");
		saveTypeColumn.setOrderCode(7);
		columns.add(saveTypeColumn);
		
		ComColumndata contentColumn = new ComColumndata("content", BuiltinCodeDataType.BLOB, 0);
		contentColumn.setName("文件内容");
		contentColumn.setComments("文件内容");
		contentColumn.setOrderCode(8);
		columns.add(contentColumn);
		
		ComColumndata secretLevelColumn = new ComColumndata("secret_level", BuiltinCodeDataType.INTEGER, 1);
		secretLevelColumn.setName("文件的密级");
		secretLevelColumn.setComments("文件的密级");
		secretLevelColumn.setOrderCode(9);
		columns.add(secretLevelColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_FILE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysFile";
	}
}
