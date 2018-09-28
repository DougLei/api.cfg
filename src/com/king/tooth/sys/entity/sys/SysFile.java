package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 文件表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
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
	 * 文件大小，单位为b
	 */
	private String sizes;
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
	 * <p>db:存储在数据库，service:存储在系统服务器上...</p>
	 */
	private String saveType;
	/**
	 * 文件内容
	 */
	private String content;
	/**
	 * 文件的密级
	 */
	private Integer secretLevel;
	/**
	 * 下载次数
	 */
	private Integer downloadCount;
	/**
	 * 批次
	 * <p>标识同一次上传的文件</p>
	 */
	private String batch;
	/**
	 * 文件类型
	 * <p>由用户自定义</p>
	 */
	private Integer type;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 备用字段01
	 */
	private String backup01;
	/**
	 * 备用字段02
	 */
	private String backup02;
	
	// ---------------------------------------------------------------------------
	
	/**
	 * 记录上传文件的临时对象
	 */
	@JSONField(serialize = false)
	private FileItem fileItem;
	
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
	public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
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
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}
	public FileItem getFileItem() {
		return fileItem;
	}
	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}
	public Integer getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBackup01() {
		return backup01;
	}
	public void setBackup01(String backup01) {
		this.backup01 = backup01;
	}
	public String getBackup02() {
		return backup02;
	}
	public void setBackup02(String backup02) {
		this.backup02 = backup02;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(22);
		
		ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", BuiltinDataType.STRING, 32);
		refDataIdColumn.setName("关联的数据主键值");
		refDataIdColumn.setComments("关联的数据主键值");
		columns.add(refDataIdColumn);
		
		ComColumndata actNameColumn = new ComColumndata("act_name", BuiltinDataType.STRING, 300);
		actNameColumn.setName("文件的实际名");
		actNameColumn.setComments("文件的实际名：即上传时的文件名称");
		columns.add(actNameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinDataType.STRING, 32);
		codeColumn.setName("文件编码");
		codeColumn.setComments("文件编码：在上传的时候，修改后的文件名");
		columns.add(codeColumn);
		
		ComColumndata sizesColumn = new ComColumndata("sizes", BuiltinDataType.STRING, 30);
		sizesColumn.setName("文件大小");
		sizesColumn.setComments("文件大小，单位为b");
		columns.add(sizesColumn);
		
		ComColumndata suffixColumn = new ComColumndata("suffix", BuiltinDataType.STRING, 10);
		suffixColumn.setName("文件后缀");
		suffixColumn.setComments("文件后缀");
		columns.add(suffixColumn);
		
		ComColumndata savePathColumn = new ComColumndata("save_path", BuiltinDataType.STRING, 1000);
		savePathColumn.setName("文件的存储路径");
		savePathColumn.setComments("文件的存储路径：存储文件的路径");
		columns.add(savePathColumn);
		
		ComColumndata saveTypeColumn = new ComColumndata("save_type", BuiltinDataType.STRING, 10);
		saveTypeColumn.setName("文件的存储方式");
		saveTypeColumn.setComments("文件的存储方式：db:存储在数据库，service:存储在系统服务器上...");
		saveTypeColumn.setDefaultValue(service);
		columns.add(saveTypeColumn);
		
		ComColumndata contentColumn = new ComColumndata("content", BuiltinDataType.BLOB, 0);
		contentColumn.setName("文件内容");
		contentColumn.setComments("文件内容");
		columns.add(contentColumn);
		
		ComColumndata secretLevelColumn = new ComColumndata("secret_level", BuiltinDataType.INTEGER, 1);
		secretLevelColumn.setName("文件的密级");
		secretLevelColumn.setComments("文件的密级");
		columns.add(secretLevelColumn);
		
		ComColumndata downloadCountColumn = new ComColumndata("download_count", BuiltinDataType.INTEGER, 10);
		downloadCountColumn.setName("文件的下载次数");
		downloadCountColumn.setComments("文件的下载次数");
		downloadCountColumn.setDefaultValue("0");
		columns.add(downloadCountColumn);
		
		ComColumndata batchColumn = new ComColumndata("batch", BuiltinDataType.STRING, 32);
		batchColumn.setName("批次");
		batchColumn.setComments("标识同一次上传的文件");
		columns.add(batchColumn);
		
		ComColumndata typeColumn = new ComColumndata("type", BuiltinDataType.INTEGER, 4);
		typeColumn.setName("文件类型");
		typeColumn.setComments("由用户自定义");
		columns.add(typeColumn);
		
		ComColumndata remarkColumn = new ComColumndata("remark", BuiltinDataType.STRING, 2000);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		ComColumndata backup01Column = new ComColumndata("backup01", BuiltinDataType.STRING, 1000);
		backup01Column.setName("备用字段01");
		backup01Column.setComments("备用字段01");
		columns.add(backup01Column);
		
		ComColumndata backup02Column = new ComColumndata("backup02", BuiltinDataType.STRING, 1000);
		backup02Column.setName("备用字段02");
		backup02Column.setComments("备用字段02");
		columns.add(backup02Column);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toGetTableName());
		table.setName("文件表");
		table.setComments("文件表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toGetTableName() {
		return "SYS_FILE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysFile";
	}

	/**
	 * 保存文件到数据库中
	 */
	public static final String db = "db";
	/**
	 * 保存文件到服务器上
	 */
	public static final String service = "service";
}
